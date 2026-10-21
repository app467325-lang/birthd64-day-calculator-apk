package com.example.data

data class PrayerTimeItem(
  val name: String,
  val time: String,
  val isNext: Boolean = false,
  val isPassed: Boolean = false,
  val iconName: String = "schedule"
)

data class AyahItem(
  val number: Int,
  val arabicText: String,
  val tafsir: String,
  val isBookmarked: Boolean = false
)

data class SurahItem(
  val number: Int,
  val name: String,
  val type: String,
  val versesCount: Int,
  val page: Int
)

data class DhikrItem(
  val id: Int,
  val text: String,
  val initialCount: Int,
  var currentCount: Int,
  val virtue: String,
  val category: String,
  val isFavorite: Boolean = false
)

data class RadioStation(
  val id: Int,
  val name: String,
  val frequency: String,
  val currentTrack: String,
  val listeners: String,
  val badge: String = "مباشر 24/7",
  val streamUrl: String = "https://qurango.net/radio/tarateel"
)

data class MasterReciter(
  val name: String,
  val title: String,
  val details: String,
  val recordingsCount: String,
  val audioUrlPattern: String = "https://server8.mp3quran.net/lhdan/%03d.mp3"
)

data class LectureItem(
  val title: String,
  val category: String,
  val subInfo: String,
  val duration: String
)

data class HijriCalendarDay(
  val day: Int,
  val gregorianDate: String,
  val isFriday: Boolean = false,
  val isSunnah: Boolean = false,
  val isWhiteDay: Boolean = false,
  val isLastTen: Boolean = false,
  val isLaylatAlQadr: Boolean = false,
  val tag: String = "",
  val moonPhase: String = "بدر التمام",
  val moonPercent: String = "٩٩٪"
)

data class IslamicEvent(
  val title: String,
  val description: String,
  val hijriDate: String,
  val gregorianDate: String,
  val remainingDays: String,
  val isImminent: Boolean = false
)

data class AdhanSetting(
  val prayerName: String,
  val time: String,
  val period: String,
  val reciter: String,
  val reminderDetail: String,
  val isEnabled: Boolean = true,
  val isNext: Boolean = false,
  val audioUrl: String = "https://server8.mp3quran.net/athan/1.mp3"
)
