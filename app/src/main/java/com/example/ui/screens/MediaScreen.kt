package com.example.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.BroadcastOnHome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PsychologyAlt
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MasterReciter
import com.example.data.RadioStation
import com.example.data.SampleData
import com.example.ui.components.AnimatedAudioWave
import com.example.ui.components.ArabesqueBackgroundPattern
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight
import com.example.ui.theme.Primary
import com.example.ui.theme.Secondary
import com.example.ui.theme.SecondaryFixed
import com.example.util.AudioContentType
import com.example.util.GlobalAudioController
import java.util.Locale

@Composable
fun MediaScreen(
    onShowMessage: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    // Audio Controller Reactive State
    val isPlaying by GlobalAudioController.isPlaying.collectAsState()
    val isBuffering by GlobalAudioController.isBuffering.collectAsState()
    val audioTitle by GlobalAudioController.currentTitle.collectAsState()
    val audioSubtitle by GlobalAudioController.currentSubtitle.collectAsState()
    val activeStation by GlobalAudioController.currentRadioStation.collectAsState()
    val currentType by GlobalAudioController.currentType.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilterCategory by remember { mutableStateOf("الكل (٢٦)") }
    var favoriteStationIds by remember { mutableStateOf(setOf(1, 2, 3, 7, 8)) }

    val filterCategories = listOf("الكل (٢٦)", "المفضلة ⭐", "الإذاعات الرسمية والعواصم", "كبار القراء", "تلاوات خاشعة وسكينة", "تفسير وفتاوى وأذكار")

    // Fallback current station
    val displayedStation = activeStation ?: SampleData.radioStations[0]

    val filteredStations = remember(searchQuery, selectedFilterCategory, favoriteStationIds) {
        SampleData.radioStations.filter { station ->
            val matchesQuery = searchQuery.isBlank() ||
                station.name.contains(searchQuery, ignoreCase = true) ||
                station.currentTrack.contains(searchQuery, ignoreCase = true) ||
                station.badge.contains(searchQuery, ignoreCase = true)

            val matchesCategory = when (selectedFilterCategory) {
                "المفضلة ⭐" -> favoriteStationIds.contains(station.id)
                "الإذاعات الرسمية والعواصم" -> station.badge.contains("دمشق") || station.badge.contains("القاهرة") ||
                    station.badge.contains("الحرم") || station.badge.contains("المسجد") || station.badge.contains("العالم")
                "كبار القراء" -> station.name.contains("الشيخ") || station.badge.contains("المنشاوي") ||
                    station.badge.contains("عبد الباسط") || station.badge.contains("الحصري") || station.badge.contains("العفاسي")
                "تلاوات خاشعة وسكينة" -> station.badge.contains("خاشعة") || station.badge.contains("البقرة") || station.badge.contains("السكينة")
                "تفسير وفتاوى وأذكار" -> station.badge.contains("تفسير") || station.badge.contains("فتاوى") || station.badge.contains("أذكار")
                else -> true
            }

            matchesQuery && matchesCategory
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            // 1. Active Live Broadcast Master Hero Card
            item {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("live_broadcast_hero")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Status Top Bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.35f))
                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(if (isPlaying) Color(0xFF4CAF50) else GoldAccent)
                                )
                                Text(
                                    text = if (isBuffering) "جاري الاتصال بالبث..." else if (isPlaying) "بث مباشر حي ومستمر" else "البث متوقف مؤقتاً",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                IconButton(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(displayedStation.streamUrl))
                                        onShowMessage("تم نسخ رابط بث ${displayedStation.name}")
                                    },
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.15f))
                                ) {
                                    Icon(imageVector = Icons.Default.Share, contentDescription = "مشاركة", tint = Color.White, modifier = Modifier.size(16.dp))
                                }
                            }
                        }

                        // Current Station Info
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(58.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Primary),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isBuffering) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(28.dp),
                                        color = GoldLight,
                                        strokeWidth = 2.5.dp
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Mosque,
                                        contentDescription = null,
                                        tint = SecondaryFixed,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Verified, contentDescription = null, tint = GoldLight, modifier = Modifier.size(14.dp))
                                    Text(
                                        text = displayedStation.badge + " • " + displayedStation.frequency,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = GoldLight
                                    )
                                }
                                Text(
                                    text = if (currentType == AudioContentType.RADIO && audioTitle.isNotBlank()) audioTitle else displayedStation.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = if (currentType == AudioContentType.RADIO && audioSubtitle.isNotBlank()) audioSubtitle else displayedStation.currentTrack,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.85f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        // Reactive Waveform Display
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Primary.copy(alpha = 0.6f))
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "HQ Audio", style = MaterialTheme.typography.labelSmall, color = GoldLight)
                                AnimatedAudioWave(isPlaying = isPlaying)
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Headset, contentDescription = null, tint = GoldLight, modifier = Modifier.size(16.dp))
                                    Text(text = displayedStation.listeners, style = MaterialTheme.typography.labelSmall, color = Color.White)
                                }
                            }
                        }

                        // Controls Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                IconButton(
                                    onClick = {
                                        if (currentType == AudioContentType.RADIO && activeStation?.id == displayedStation.id) {
                                            GlobalAudioController.togglePlayPause()
                                        } else {
                                            GlobalAudioController.playRadio(displayedStation, context)
                                            onShowMessage("جاري تشغيل ${displayedStation.name}")
                                        }
                                    },
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(CircleShape)
                                        .background(Secondary)
                                        .testTag("media_master_play_btn")
                                ) {
                                    Icon(
                                        imageVector = if (isPlaying && currentType == AudioContentType.RADIO) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(30.dp)
                                    )
                                }

                                Column {
                                    Text(
                                        text = if (isPlaying) "استماع مباشر" else "إيقاف مؤقت",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "المشغل يعمل في الخلفية",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        color = GoldLight
                                    )
                                }
                            }

                            // Quick Famous Stations Switch Chips
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = GoldAccent.copy(alpha = 0.25f),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldLight),
                                    modifier = Modifier.clickable {
                                        val damascus = SampleData.radioStations.find { it.name.contains("دمشق") } ?: displayedStation
                                        GlobalAudioController.playRadio(damascus, context)
                                        onShowMessage("تم الانتقال إلى إذاعة دمشق 🇸🇾")
                                    }
                                ) {
                                    Text(
                                        text = "دمشق 🇸🇾",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color.White.copy(alpha = 0.18f),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.4f)),
                                    modifier = Modifier.clickable {
                                        val cairo = SampleData.radioStations.find { it.name.contains("القاهرة") } ?: displayedStation
                                        GlobalAudioController.playRadio(cairo, context)
                                        onShowMessage("تم الانتقال إلى إذاعة القرآن الكريم من القاهرة 🇪🇬")
                                    }
                                ) {
                                    Text(
                                        text = "القاهرة 🇪🇬",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color.White.copy(alpha = 0.18f),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.4f)),
                                    modifier = Modifier.clickable {
                                        val makkah = SampleData.radioStations.find { it.name.contains("مكة") } ?: displayedStation
                                        GlobalAudioController.playRadio(makkah, context)
                                        onShowMessage("تم الانتقال إلى إذاعة الحرم المكي 🕋")
                                    }
                                ) {
                                    Text(
                                        text = "مكة 🕋",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 2. Search & Category Filters for 26 Stations
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Search Bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("radio_search_field"),
                        placeholder = {
                            Text(
                                text = "ابحث في ٢٦ إذاعة (دمشق، مكة، القاهرة، المنشاوي...)",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Primary)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(imageVector = Icons.Default.Clear, contentDescription = "مسح", tint = Color.Gray)
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                        )
                    )

                    // Category Filter Chips
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filterCategories) { cat ->
                            val isSelected = cat == selectedFilterCategory
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        if (isSelected) Primary else MaterialTheme.colorScheme.surfaceContainerLow
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) GoldAccent else Color.Transparent,
                                        shape = RoundedCornerShape(14.dp)
                                    )
                                    .clickable { selectedFilterCategory = cat }
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = cat,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal),
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // 3. Radio Stations Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(4.dp)
                                .height(18.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(Primary)
                        )
                        Text(
                            text = "قنوات إذاعة القرآن الكريم (${filteredStations.size} قناة)",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "بث مباشر عبر الإنترنت",
                        style = MaterialTheme.typography.labelSmall,
                        color = Secondary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // 4. Stations List Items
            items(filteredStations, key = { it.id }) { station ->
                val isCurrent = activeStation?.id == station.id
                val isThisStationPlaying = isCurrent && isPlaying

                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCurrent) Color(0xFFEBF7F2) else MaterialTheme.colorScheme.surfaceContainerLowest
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (isCurrent) 3.dp else 1.dp),
                    border = if (isCurrent) androidx.compose.foundation.BorderStroke(1.2.dp, Primary) else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (isThisStationPlaying) {
                                GlobalAudioController.togglePlayPause()
                            } else {
                                GlobalAudioController.playRadio(station, context)
                                onShowMessage("تم التحويل إلى ${station.name}")
                            }
                        }
                        .testTag("radio_station_${station.id}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (isCurrent) Primary
                                        else MaterialTheme.colorScheme.surfaceContainerHigh
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isCurrent && isBuffering) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(22.dp),
                                        color = GoldLight,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Icon(
                                        imageVector = if (isCurrent) Icons.Default.BroadcastOnHome else Icons.Default.Radio,
                                        contentDescription = null,
                                        tint = if (isCurrent) GoldLight else Primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = station.name,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = Primary,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Secondary.copy(alpha = 0.15f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = station.badge,
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                            color = Secondary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Text(
                                    text = station.currentTrack,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        // Action Buttons: Favorite Star & Play/Pause
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            val isFavorite = favoriteStationIds.contains(station.id)
                            IconButton(
                                onClick = {
                                    favoriteStationIds = if (isFavorite) {
                                        favoriteStationIds - station.id
                                    } else {
                                        favoriteStationIds + station.id
                                    }
                                },
                                modifier = Modifier.size(34.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Stars,
                                    contentDescription = "المفضلة",
                                    tint = if (isFavorite) GoldAccent else Color.Gray.copy(alpha = 0.4f),
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isThisStationPlaying) Secondary
                                        else Primary.copy(alpha = 0.12f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isThisStationPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = if (isThisStationPlaying) Color.White else Primary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 5. Golden Legacy: Master Reciters
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(18.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(Primary)
                            )
                            Text(
                                text = "سجل التلاوات لكبار القراء (استماع فوري)",
                                style = MaterialTheme.typography.titleMedium,
                                color = Primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "صوت عالي النقاء",
                            style = MaterialTheme.typography.labelSmall,
                            color = Secondary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Master Reciters Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ReciterCard(
                            reciter = SampleData.masterReciters[0],
                            icon = Icons.Default.Stars,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                val url = String.format(Locale.US, SampleData.masterReciters[0].audioUrlPattern, 1)
                                GlobalAudioController.playQuran(
                                    surahNumber = 1,
                                    surahName = "الفاتحة",
                                    reciterName = SampleData.masterReciters[0].name,
                                    audioUrl = url,
                                    context = context
                                )
                                onShowMessage("جاري تشغيل سورة الفاتحة بصوت ${SampleData.masterReciters[0].name}")
                            }
                        )
                        ReciterCard(
                            reciter = SampleData.masterReciters[1],
                            icon = Icons.Default.MenuBook,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                val url = String.format(Locale.US, SampleData.masterReciters[1].audioUrlPattern, 1)
                                GlobalAudioController.playQuran(
                                    surahNumber = 1,
                                    surahName = "الفاتحة",
                                    reciterName = SampleData.masterReciters[1].name,
                                    audioUrl = url,
                                    context = context
                                )
                                onShowMessage("جاري تشغيل سورة الفاتحة بصوت ${SampleData.masterReciters[1].name}")
                            }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ReciterCard(
                            reciter = SampleData.masterReciters[2],
                            icon = Icons.Default.WorkspacePremium,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                val url = String.format(Locale.US, SampleData.masterReciters[2].audioUrlPattern, 1)
                                GlobalAudioController.playQuran(
                                    surahNumber = 1,
                                    surahName = "الفاتحة",
                                    reciterName = SampleData.masterReciters[2].name,
                                    audioUrl = url,
                                    context = context
                                )
                                onShowMessage("جاري تشغيل سورة الفاتحة بصوت ${SampleData.masterReciters[2].name}")
                            }
                        )
                        ReciterCard(
                            reciter = SampleData.masterReciters[3],
                            icon = Icons.Default.AutoStories,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                val url = String.format(Locale.US, SampleData.masterReciters[3].audioUrlPattern, 1)
                                GlobalAudioController.playQuran(
                                    surahNumber = 1,
                                    surahName = "الفاتحة",
                                    reciterName = SampleData.masterReciters[3].name,
                                    audioUrl = url,
                                    context = context
                                )
                                onShowMessage("جاري تشغيل سورة الفاتحة بصوت ${SampleData.masterReciters[3].name}")
                            }
                        )
                    }
                }
            }

            // 6. Audio Lectures Section
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(18.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(Secondary)
                            )
                            Text(
                                text = "الدروس والمحاضرات الصوتية",
                                style = MaterialTheme.typography.titleMedium,
                                color = Primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "محدثة أسبوعياً",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    SampleData.audioLectures.forEach { lecture ->
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val damascusStation = SampleData.radioStations.find { it.badge.contains("دمشق") } ?: SampleData.radioStations[0]
                                    GlobalAudioController.playRadio(damascusStation, context)
                                    onShowMessage("بدء تشغيل: ${lecture.title}")
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(46.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Primary),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PsychologyAlt,
                                            contentDescription = null,
                                            tint = SecondaryFixed,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }

                                    Column {
                                        Text(
                                            text = lecture.title,
                                            style = MaterialTheme.typography.titleSmall,
                                            color = Primary,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "${lecture.category} • ${lecture.subInfo}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(MaterialTheme.colorScheme.surfaceContainerLow)
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = lecture.duration,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clip(CircleShape)
                                            .background(Primary.copy(alpha = 0.12f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = "استماع",
                                            tint = Primary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(90.dp)) }
        }
    }
}

@Composable
fun ReciterCard(
    reciter: MasterReciter,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .clickable(onClick = onClick)
            .testTag("reciter_card_${reciter.name}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(Primary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(28.dp)
                )
            }

            Text(
                text = reciter.name,
                style = MaterialTheme.typography.titleSmall,
                color = Primary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = reciter.title,
                style = MaterialTheme.typography.labelSmall,
                color = Secondary,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LibraryMusic,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = reciter.recordingsCount,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
