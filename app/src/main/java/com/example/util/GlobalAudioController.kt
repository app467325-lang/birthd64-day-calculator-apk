package com.example.util

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.PowerManager
import android.util.Log
import com.example.data.RadioStation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

enum class AudioContentType {
    NONE,
    RADIO,
    QURAN,
    ADHAN
}

/**
 * Robust Central Audio Player Controller for Background Audio Playback
 * Supports live radio streaming, full Quran Surahs, and Adhan call to prayer.
 */
object GlobalAudioController {
    private const val TAG = "GlobalAudioController"

    private var mediaPlayer: MediaPlayer? = null
    private var progressJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main)

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _isBuffering = MutableStateFlow(false)
    val isBuffering: StateFlow<Boolean> = _isBuffering.asStateFlow()

    private val _currentTitle = MutableStateFlow("إذاعة القرآن الكريم")
    val currentTitle: StateFlow<String> = _currentTitle.asStateFlow()

    private val _currentSubtitle = MutableStateFlow("بث مباشر على مدار الساعة")
    val currentSubtitle: StateFlow<String> = _currentSubtitle.asStateFlow()

    private val _currentType = MutableStateFlow(AudioContentType.NONE)
    val currentType: StateFlow<AudioContentType> = _currentType.asStateFlow()

    private val _currentUrl = MutableStateFlow("")
    val currentUrl: StateFlow<String> = _currentUrl.asStateFlow()

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress.asStateFlow()

    private val _activeStationId = MutableStateFlow<Int?>(null)
    val activeStationId: StateFlow<Int?> = _activeStationId.asStateFlow()

    private val _currentRadioStation = MutableStateFlow<RadioStation?>(null)
    val currentRadioStation: StateFlow<RadioStation?> = _currentRadioStation.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun init(context: Context) {
        // Prepare context if needed for wakelock
    }

    private fun prepareMediaPlayer(context: Context? = null) {
        try {
            mediaPlayer?.release()
        } catch (_: Exception) {}

        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            if (context != null) {
                try {
                    setWakeMode(context.applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
                } catch (_: Exception) {}
            }
            setOnPreparedListener { mp ->
                _isBuffering.value = false
                try {
                    mp.start()
                    _isPlaying.value = true
                    _errorMessage.value = null
                    startProgressTracker()
                } catch (e: Exception) {
                    Log.e(TAG, "Error starting MediaPlayer", e)
                }
            }
            setOnBufferingUpdateListener { _, percent ->
                if (percent < 100 && !_isPlaying.value) {
                    _isBuffering.value = true
                }
            }
            setOnCompletionListener {
                _isPlaying.value = false
                _isBuffering.value = false
                _progress.value = 1f
                stopProgressTracker()
            }
            setOnErrorListener { _, what, extra ->
                Log.e(TAG, "MediaPlayer error: what=$what, extra=$extra")
                _isBuffering.value = false
                _isPlaying.value = false
                _errorMessage.value = "تعذر تشغيل البث، جاري المحاولة..."
                true
            }
        }
    }

    fun playRadio(station: RadioStation, context: Context? = null) {
        _currentRadioStation.value = station
        _activeStationId.value = station.id
        _currentTitle.value = station.name
        _currentSubtitle.value = station.currentTrack
        _currentType.value = AudioContentType.RADIO
        _currentUrl.value = station.streamUrl
        _progress.value = 0f

        playUrl(station.streamUrl, context)
    }

    fun playQuran(
        surahNumber: Int,
        surahName: String,
        reciterName: String,
        audioUrl: String,
        context: Context? = null
    ) {
        _activeStationId.value = null
        _currentRadioStation.value = null
        _currentTitle.value = "سورة $surahName"
        _currentSubtitle.value = "تلاوة $reciterName"
        _currentType.value = AudioContentType.QURAN
        _currentUrl.value = audioUrl
        _progress.value = 0f

        playUrl(audioUrl, context)
    }

    fun playAdhan(
        title: String,
        muadhin: String = "",
        audioUrl: String,
        context: Context? = null,
        muezzinName: String = muadhin
    ) {
        val voiceName = if (muezzinName.isNotBlank()) muezzinName else muadhin
        _activeStationId.value = null
        _currentRadioStation.value = null
        _currentTitle.value = title
        _currentSubtitle.value = "بصوت $voiceName"
        _currentType.value = AudioContentType.ADHAN
        _currentUrl.value = audioUrl
        _progress.value = 0f

        playUrl(audioUrl, context)
    }

    private fun playUrl(url: String, context: Context?) {
        scope.launch(Dispatchers.IO) {
            try {
                _isBuffering.value = true
                _errorMessage.value = null
                prepareMediaPlayer(context)

                // Resolve possible redirects (e.g. 301/302 redirects for radio and audio archives)
                var resolvedUrl = url
                try {
                    var currentUrl = url
                    var redirectsCount = 0
                    while (redirectsCount < 4) {
                        val connection = java.net.URL(currentUrl).openConnection() as java.net.HttpURLConnection
                        connection.instanceFollowRedirects = false
                        connection.connectTimeout = 5000
                        connection.readTimeout = 5000
                        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                        connection.setRequestProperty("Icy-MetaData", "1")
                        connection.connect()
                        val code = connection.responseCode
                        if (code in 300..399) {
                            val location = connection.getHeaderField("Location")
                            connection.disconnect()
                            if (!location.isNullOrBlank()) {
                                currentUrl = location
                                redirectsCount++
                            } else {
                                break
                            }
                        } else {
                            connection.disconnect()
                            break
                        }
                    }
                    resolvedUrl = currentUrl
                } catch (e: Exception) {
                    Log.w(TAG, "Redirect resolution skipped: ${e.message}")
                }

                mediaPlayer?.apply {
                    reset()
                    setDataSource(resolvedUrl)
                    prepareAsync()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error setting data source: $url", e)
                _isBuffering.value = false
                _isPlaying.value = false
                _errorMessage.value = "تعذر الاتصال بالبث الصوتي، يرجى المحاولة ثانية"
            }
        }
    }

    fun togglePlayPause() {
        if (_isPlaying.value) {
            pause()
        } else {
            resume()
        }
    }

    fun pause() {
        try {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.pause()
                }
            }
            _isPlaying.value = false
            _isBuffering.value = false
            stopProgressTracker()
        } catch (e: Exception) {
            Log.e(TAG, "Error pausing", e)
        }
    }

    fun resume() {
        try {
            mediaPlayer?.let {
                it.start()
                _isPlaying.value = true
                startProgressTracker()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error resuming", e)
        }
    }

    fun stop() {
        try {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                }
                it.reset()
            }
            _isPlaying.value = false
            _isBuffering.value = false
            _activeStationId.value = null
            stopProgressTracker()
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping", e)
        }
    }

    private fun startProgressTracker() {
        stopProgressTracker()
        progressJob = scope.launch {
            while (isActive && _isPlaying.value) {
                try {
                    val mp = mediaPlayer
                    if (mp != null && mp.isPlaying && mp.duration > 0) {
                        _progress.value = (mp.currentPosition.toFloat() / mp.duration.toFloat()).coerceIn(0f, 1f)
                    }
                } catch (_: Exception) {}
                delay(500)
            }
        }
    }

    private fun stopProgressTracker() {
        progressJob?.cancel()
        progressJob = null
    }

    fun release() {
        stopProgressTracker()
        try {
            mediaPlayer?.release()
        } catch (_: Exception) {}
        mediaPlayer = null
        _isPlaying.value = false
        _isBuffering.value = false
    }
}
