package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AyahItem
import com.example.data.QuranRepository
import com.example.ui.components.ArabesqueBackgroundPattern
import com.example.ui.components.AyahRosetteMarker
import com.example.ui.components.toArabicNumerals
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.Secondary
import com.example.ui.theme.SecondaryContainer
import com.example.ui.theme.SecondaryFixed
import com.example.util.AudioContentType
import com.example.util.GlobalAudioController
import java.util.Locale
import kotlinx.coroutines.launch

/**
 * Authentic Mushaf Reader with Book-like Horizontal Page Flipping (التقليب مثل المصحف الشريف)
 * Includes all 114 Surahs, famous reciters with Sheikh Muhammad Al-Luhaidan, tafsir and audio.
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun QuranScreen(
    onShowMessage: (String) -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    val pages = QuranRepository.pages
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { pages.size })

    var selectedReciter by remember { mutableStateOf(QuranRepository.masterReciters.first()) }
    var isReciterMenuOpen by remember { mutableStateOf(false) }

    var selectedAyah by remember { mutableStateOf<AyahItem?>(null) }
    var fontSizeSp by remember { mutableIntStateOf(23) }
    var isSurahIndexOpen by remember { mutableStateOf(false) }
    var surahSearchQuery by remember { mutableStateOf("") }
    var isTafsirOpen by remember { mutableStateOf(false) }

    val isGlobalPlaying by GlobalAudioController.isPlaying.collectAsState()
    val isBuffering by GlobalAudioController.isBuffering.collectAsState()
    val currentAudioType by GlobalAudioController.currentType.collectAsState()
    val currentAudioTitle by GlobalAudioController.currentTitle.collectAsState()

    val currentPage = pages[pagerState.currentPage]

    Box(modifier = Modifier.fillMaxSize()) {
        ArabesqueBackgroundPattern()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            // 1. Top Surah Meta Bar & Fast Controls
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth().testTag("quran_top_controls")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Surah Selector Button (Opens 114 Surah Index)
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Primary)
                                    .clickable { isSurahIndexOpen = true }
                                    .padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MenuBook,
                                    contentDescription = null,
                                    tint = GoldLight,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = currentPage.surahName,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontFamily = FontFamily.Serif,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color.White
                                )
                                Text(
                                    text = "▾",
                                    color = SecondaryFixed,
                                    fontSize = 14.sp
                                )
                            }

                            // Juz & Page Info Badge
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "الجزء ${toArabicNumerals(currentPage.juzNumber)} • ص ${toArabicNumerals(currentPage.pageNumber)}",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        // Reciter Picker Ribbon (with Muhammad Al-Luhaidan)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                                .clickable { isReciterMenuOpen = true }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Column {
                                    Text(
                                        text = "القارئ: ${selectedReciter.name}",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = selectedReciter.title,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "تغيير القارئ",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(text = "▾", color = MaterialTheme.colorScheme.primary)
                            }
                        }

                        // Dropdown for Reciters selection
                        DropdownMenu(
                            expanded = isReciterMenuOpen,
                            onDismissRequest = { isReciterMenuOpen = false }
                        ) {
                            QuranRepository.masterReciters.forEach { reciter ->
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column {
                                                Text(reciter.name, fontWeight = FontWeight.Bold)
                                                Text(reciter.title, fontSize = 11.sp, color = MaterialTheme.colorScheme.secondary)
                                            }
                                            if (selectedReciter.name == reciter.name) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = null,
                                                    tint = Primary,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                        }
                                    },
                                    onClick = {
                                        selectedReciter = reciter
                                        isReciterMenuOpen = false
                                        onShowMessage("تم اختيار ${reciter.name}")
                                        if (currentAudioType == AudioContentType.QURAN && isGlobalPlaying) {
                                            val surahNumber = currentPage.surahNumber
                                            val surahUrl = String.format(Locale.US, reciter.audioUrlPattern, surahNumber)
                                            GlobalAudioController.playQuran(
                                                surahNumber = surahNumber,
                                                surahName = currentPage.surahName,
                                                reciterName = reciter.name,
                                                audioUrl = surahUrl,
                                                context = context
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // 2. THE HOLY MUSHAF FLIPPER (تقليب الصفحات مثل المصحف)
            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier.fillMaxWidth().testTag("mushaf_flip_container")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        // Double manuscript border framing
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                                .padding(5.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFFFFDF8))
                                .border(1.5.dp, GoldAccent.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                                .padding(14.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Horizontal Pager for True Page-Flipping Experience
                                HorizontalPager(
                                    state = pagerState,
                                    modifier = Modifier.fillMaxWidth()
                                ) { pageIdx ->
                                    val pageData = pages[pageIdx]

                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        // Surah Header Ribbon
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(14.dp))
                                                .background(
                                                    Brush.horizontalGradient(
                                                        colors = listOf(
                                                            Secondary.copy(alpha = 0.15f),
                                                            Secondary.copy(alpha = 0.35f),
                                                            Secondary.copy(alpha = 0.15f)
                                                        )
                                                    )
                                                )
                                                .padding(vertical = 8.dp, horizontal = 12.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                                ) {
                                                    Text(text = "❖", color = Secondary, fontWeight = FontWeight.Bold)
                                                    Text(
                                                        text = pageData.surahName,
                                                        style = MaterialTheme.typography.titleLarge.copy(
                                                            fontFamily = FontFamily.Serif,
                                                            fontWeight = FontWeight.Bold
                                                        ),
                                                        color = Primary
                                                    )
                                                    Text(text = "❖", color = Secondary, fontWeight = FontWeight.Bold)
                                                }

                                                Text(
                                                    text = "${pageData.surahType} • آياتها ${toArabicNumerals(pageData.versesCount)}",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = Color(0xFF6B7280)
                                                )
                                            }
                                        }

                                        // Basmalah Header Ornament (Unless Tawbah)
                                        if (pageData.surahName != "سورة التوبة") {
                                            Text(
                                                text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                                                style = MaterialTheme.typography.titleLarge.copy(
                                                    fontFamily = FontFamily.Serif,
                                                    fontSize = 22.sp
                                                ),
                                                color = Primary,
                                                fontWeight = FontWeight.Bold,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.padding(vertical = 4.dp)
                                            )
                                        }

                                        // Continuous Mushaf Verses Flow
                                        FlowRow(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.Start,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            pageData.verses.forEach { ayah ->
                                                val isSelected = selectedAyah?.number == ayah.number

                                                Text(
                                                    text = "${ayah.arabicText} ",
                                                    style = MaterialTheme.typography.bodyLarge.copy(
                                                        fontFamily = FontFamily.Serif,
                                                        fontSize = fontSizeSp.sp,
                                                        lineHeight = (fontSizeSp * 2.2).sp
                                                    ),
                                                    color = Primary,
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(6.dp))
                                                        .background(
                                                            if (isSelected) SecondaryFixed.copy(alpha = 0.6f)
                                                            else Color.Transparent
                                                        )
                                                        .clickable {
                                                            selectedAyah = ayah
                                                            isTafsirOpen = true
                                                        }
                                                        .padding(horizontal = 3.dp, vertical = 2.dp)
                                                )

                                                AyahRosetteMarker(
                                                    ayahNumber = ayah.number,
                                                    isHighlighted = isSelected,
                                                    onClick = {
                                                        selectedAyah = ayah
                                                        isTafsirOpen = true
                                                    },
                                                    modifier = Modifier.padding(horizontal = 2.dp, vertical = 3.dp)
                                                )
                                            }
                                        }

                                        // Page Footer with Page Number
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 10.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "الجزء ${toArabicNumerals(pageData.juzNumber)}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color.Gray
                                            )

                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Text(text = "♦", color = Secondary, fontSize = 10.sp)
                                                Text(
                                                    text = toArabicNumerals(pageData.pageNumber),
                                                    style = MaterialTheme.typography.titleMedium.copy(fontFamily = FontFamily.Serif),
                                                    color = Primary,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(text = "♦", color = Secondary, fontSize = 10.sp)
                                            }

                                            Text(
                                                text = pageData.surahName,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color.Gray
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Page Turning Navigation Buttons
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Previous Page Button
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.surfaceContainerLow)
                                    .clickable {
                                        if (pagerState.currentPage > 0) {
                                            coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
                                        }
                                    }
                                    .padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = Primary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "الصفحة السابقة",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Primary
                                )
                            }

                            // Page Counter
                            Text(
                                text = "صفحة ${toArabicNumerals(pagerState.currentPage + 1)} من ${toArabicNumerals(pages.size)}",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.secondary
                            )

                            // Next Page Button
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.surfaceContainerLow)
                                    .clickable {
                                        if (pagerState.currentPage < pages.size - 1) {
                                            coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                                        }
                                    }
                                    .padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "الصفحة التالية",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Primary
                                )
                                Icon(
                                    imageVector = Icons.Default.ChevronLeft,
                                    contentDescription = null,
                                    tint = Primary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 3. Audio Player Dock for Selected Sheikh
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    modifier = Modifier.fillMaxWidth().testTag("sheikh_audio_dock")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        val isThisSurahPlaying = currentAudioType == AudioContentType.QURAN && isGlobalPlaying

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(Primary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VolumeUp,
                                        contentDescription = null,
                                        tint = GoldLight,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                Column {
                                    Text(
                                        text = "تلاوة ${selectedReciter.name}",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Primary
                                    )
                                    Text(
                                        text = "${currentPage.surahName} • تسجيل عالي النقاء",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Secondary
                                    )
                                }
                            }

                            // Play / Pause Button
                            IconButton(
                                onClick = {
                                    if (isThisSurahPlaying) {
                                        GlobalAudioController.togglePlayPause()
                                        onShowMessage("تم إيقاف التلاوة مؤقتاً")
                                    } else {
                                        val surahNumber = currentPage.surahNumber
                                        val surahUrl = String.format(Locale.US, selectedReciter.audioUrlPattern, surahNumber)
                                        GlobalAudioController.playQuran(
                                            surahNumber = surahNumber,
                                            surahName = currentPage.surahName,
                                            reciterName = selectedReciter.name,
                                            audioUrl = surahUrl,
                                            context = context
                                        )
                                        onShowMessage("بدء تلاوة ${currentPage.surahName} بصوت ${selectedReciter.name}")
                                    }
                                },
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(Secondary)
                                    .testTag("quran_play_pause_button")
                            ) {
                                if (isBuffering && currentAudioType == AudioContentType.QURAN) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = Color.White,
                                        strokeWidth = 2.5.dp
                                    )
                                } else {
                                    Icon(
                                        imageVector = if (isThisSurahPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = if (isThisSurahPlaying) "إيقاف" else "تشغيل",
                                        tint = Color.White,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                            }
                        }

                        LinearProgressIndicator(
                            progress = { if (isThisSurahPlaying) 0.65f else 0.05f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = Secondary,
                            trackColor = MaterialTheme.colorScheme.surfaceContainer
                        )
                    }
                }
            }

            // 4. Tafsir Al-Muyassar Card (When an Ayah is selected)
            if (isTafsirOpen && selectedAyah != null) {
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "التفسير الميسر • الآية ${toArabicNumerals(selectedAyah!!.number)}",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Primary
                                )

                                Row {
                                    IconButton(
                                        onClick = {
                                            clipboardManager.setText(AnnotatedString("${selectedAyah!!.arabicText}\n${selectedAyah!!.tafsir}"))
                                            onShowMessage("تم نسخ الآية وتفسيرها")
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ContentCopy,
                                            contentDescription = "نسخ",
                                            tint = Primary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    IconButton(onClick = { isTafsirOpen = false }) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "إغلاق",
                                            tint = Color.Gray,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }

                            Text(
                                text = selectedAyah!!.tafsir,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 24.sp
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(90.dp)) }
        }

        // 5. 114 Surahs Directory Bottom Sheet
        if (isSurahIndexOpen) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { isSurahIndexOpen = false },
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Card(
                        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(600.dp)
                            .clickable(enabled = false) {}
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "فهرس القرآن الكريم (١١٤ سورة)",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontFamily = FontFamily.Serif,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Primary
                                )

                                IconButton(onClick = { isSurahIndexOpen = false }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "إغلاق",
                                        tint = Primary
                                    )
                                }
                            }

                            // Search bar
                            OutlinedTextField(
                                value = surahSearchQuery,
                                onValueChange = { surahSearchQuery = it },
                                placeholder = { Text("ابحث عن اسم السورة...") },
                                leadingIcon = {
                                    Icon(Icons.Default.Search, contentDescription = null, tint = Primary)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                singleLine = true
                            )

                            // Surah list
                            val filteredSurahs = QuranRepository.allSurahs.filter {
                                it.name.contains(surahSearchQuery.trim()) || it.number.toString() == surahSearchQuery.trim()
                            }

                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(filteredSurahs) { surah ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(MaterialTheme.colorScheme.surfaceContainerLow)
                                            .clickable {
                                                isSurahIndexOpen = false
                                                // Find matching page index or jump to page
                                                val foundIdx = pages.indexOfFirst { it.surahName.contains(surah.name) }
                                                if (foundIdx != -1) {
                                                    coroutineScope.launch { pagerState.scrollToPage(foundIdx) }
                                                }
                                                onShowMessage("تم فتح سورة ${surah.name}")
                                            }
                                            .padding(horizontal = 14.dp, vertical = 12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(36.dp)
                                                    .clip(CircleShape)
                                                    .background(Primary),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = toArabicNumerals(surah.number),
                                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                    color = Color.White
                                                )
                                            }

                                            Column {
                                                Text(
                                                    text = "سورة ${surah.name}",
                                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                                    color = Primary
                                                )
                                                Text(
                                                    text = "${surah.type} • آياتها ${toArabicNumerals(surah.versesCount)}",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = Color.Gray
                                                )
                                            }
                                        }

                                        Text(
                                            text = "صفحة ${toArabicNumerals(surah.page)}",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = Secondary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
