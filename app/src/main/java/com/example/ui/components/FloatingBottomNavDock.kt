package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Mosque
import androidx.compose.material.icons.outlined.Radio
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight
import com.example.ui.theme.Primary
import com.example.ui.theme.Secondary
import com.example.ui.theme.SecondaryFixed
import com.example.util.AudioContentType
import com.example.util.GlobalAudioController
import kotlinx.coroutines.delay

/**
 * Liquid Glass Floating Navigation Bar (شريط الزجاج السائل العائم)
 * Rebuilt based on the user's LiquidGlassApp concept:
 * - Frosted glassmorphism floating bar with specular border and deep blur shadow.
 * - Interactive long-press gesture displaying an animated liquid glass floating bubble
 *   ("الفقاعة الشفافة: خصائص ...") above the active tab.
 * - Liquid pill capsule transitions for active tabs with amber / gold accent highlights.
 * - Integrated live audio mini-player with glass styling.
 */
@Composable
fun FloatingBottomNavDock(
    currentTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = false,
    requestedBubbleTab: AppTab? = null,
    onDismissRequestedBubble: () -> Unit = {}
) {
    val isPlaying by GlobalAudioController.isPlaying.collectAsState()
    val isBuffering by GlobalAudioController.isBuffering.collectAsState()
    val audioTitle by GlobalAudioController.currentTitle.collectAsState()
    val audioSubtitle by GlobalAudioController.currentSubtitle.collectAsState()
    val audioType by GlobalAudioController.currentType.collectAsState()

    // State for tracking which item is being long-pressed to show the floating glass bubble
    var internalBubbleTab by remember { mutableStateOf<AppTab?>(null) }
    val bubbleTab = requestedBubbleTab ?: internalBubbleTab

    // Auto-dismiss bubble after 5 seconds if not dismissed by tap
    LaunchedEffect(bubbleTab) {
        if (bubbleTab != null) {
            delay(5000)
            internalBubbleTab = null
            onDismissRequestedBubble()
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Floating Mini Player when Audio is active or buffering (Liquid Glass styled)
        AnimatedVisibility(visible = audioType != AudioContentType.NONE && (isPlaying || isBuffering)) {
            val miniPlayerBg = if (isDarkTheme) {
                Color(0xFF1E1B4B).copy(alpha = 0.90f)
            } else {
                Color.White.copy(alpha = 0.92f)
            }
            val titleColor = if (isDarkTheme) Color.White else Color(0xFF1F3A2E)
            val subColor = if (isDarkTheme) Color(0xFFFFD54F) else Color(0xFF1B4D3E).copy(alpha = 0.85f)

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 16.dp,
                        shape = RoundedCornerShape(24.dp),
                        spotColor = Color.Black.copy(alpha = 0.15f)
                    ),
                shape = RoundedCornerShape(24.dp),
                color = Color.Transparent,
                border = BorderStroke(
                    width = 1.4.dp,
                    brush = Brush.linearGradient(
                        listOf(Color(0xFFD4AF37), Color.White.copy(alpha = 0.8f), Color(0xFFD4AF37))
                    )
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(miniPlayerBg)
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF1B4D3E))
                                    .border(1.dp, Color(0xFFD4AF37).copy(alpha = 0.6f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isBuffering) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = Color(0xFFD4AF37),
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Icon(
                                        imageVector = if (audioType == AudioContentType.RADIO) Icons.Default.Radio else Icons.Default.MenuBook,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = audioTitle,
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = titleColor,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = if (isBuffering) "جاري الاتصال بالبث المباشر..." else audioSubtitle,
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    color = subColor,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            IconButton(
                                onClick = { GlobalAudioController.togglePlayPause() },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = if (isPlaying) "إيقاف" else "تشغيل",
                                    tint = if (isDarkTheme) Color(0xFFFFD54F) else Color(0xFF1B4D3E),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            IconButton(
                                onClick = { GlobalAudioController.stop() },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "إغلاق",
                                    tint = (if (isDarkTheme) Color.White else Color(0xFF1B4D3E)).copy(alpha = 0.7f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Relative Box for Dock + Floating Liquid Glass Bubble (مطابق لكود Flutter تماماً)
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            // 1. الفقاعة الشفافة التي تظهر عند الضغط المطول فوق العنصر
            androidx.compose.animation.AnimatedVisibility(
                visible = bubbleTab != null,
                enter = scaleIn(initialScale = 0.65f, animationSpec = spring(dampingRatio = 0.75f, stiffness = 450f)) + fadeIn(),
                exit = scaleOut(targetScale = 0.65f, animationSpec = spring(stiffness = 450f)) + fadeOut()
            ) {
                if (bubbleTab != null) {
                    val currentBubble = bubbleTab
                    Box(
                        modifier = Modifier
                            .padding(bottom = 72.dp)
                            .shadow(
                                elevation = 20.dp,
                                shape = RoundedCornerShape(22.dp),
                                spotColor = Color.Black.copy(alpha = 0.40f)
                            )
                            .clip(RoundedCornerShape(22.dp))
                            .background(
                                if (isDarkTheme) Color.White.copy(alpha = 0.15f)
                                else Color(0xFF1B4D3E).copy(alpha = 0.90f)
                            )
                            .border(
                                width = 1.2.dp,
                                color = if (isDarkTheme) Color.White.copy(alpha = 0.35f) else Color(0xFFD4AF37).copy(alpha = 0.60f),
                                shape = RoundedCornerShape(22.dp)
                            )
                            .clickable {
                                onTabSelected(currentBubble)
                                internalBubbleTab = null
                                onDismissRequestedBubble()
                            }
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AutoAwesome,
                                contentDescription = null,
                                tint = Color(0xFFFFD54F), // amberAccent
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "الانتقال إلى: ${currentBubble.title}",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "إغلاق",
                                tint = Color.White.copy(alpha = 0.75f),
                                modifier = Modifier
                                    .size(14.dp)
                                    .clickable {
                                        internalBubbleTab = null
                                        onDismissRequestedBubble()
                                    }
                            )
                        }
                    }
                }
            }

            // 2. الشريط السفلي الرئيسي (الزجاج السائل)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 30.dp,
                        shape = RoundedCornerShape(35.dp),
                        spotColor = Color.Black.copy(alpha = 0.50f)
                    ),
                shape = RoundedCornerShape(35.dp),
                color = Color.Transparent,
                border = BorderStroke(
                    width = 1.2.dp,
                    color = if (isDarkTheme) Color.White.copy(alpha = 0.18f) else Color(0xFFD4AF37).copy(alpha = 0.40f)
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (isDarkTheme) Color.White.copy(alpha = 0.07f)
                            else Color.White.copy(alpha = 0.88f)
                        )
                        .padding(horizontal = 10.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // عناصر الشريط السفلي الخمسة:
                        // الرئيسية -> القراء -> قوائم التشغيل -> الإعدادات -> بحث
                        val navTabs = listOf(
                            AppTab.Home,
                            AppTab.Quran,
                            AppTab.Media,
                            AppTab.Athkar,
                            AppTab.Qibla
                        )

                        navTabs.forEach { tab ->
                            val isSelected = tab == currentTab

                            val icon = when (tab) {
                                AppTab.Home -> Icons.Default.Home
                                AppTab.Quran -> Icons.Default.MenuBook
                                AppTab.Media -> Icons.Filled.QueueMusic
                                AppTab.Athkar -> Icons.Default.Settings
                                AppTab.Qibla -> Icons.Default.Search
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(25.dp))
                                    .background(
                                        if (isSelected) {
                                            if (isDarkTheme) Color.White.copy(alpha = 0.18f)
                                            else Color(0xFF1B4D3E).copy(alpha = 0.14f)
                                        } else Color.Transparent
                                    )
                                    .pointerInput(tab) {
                                        detectTapGestures(
                                            onTap = {
                                                onTabSelected(tab)
                                                internalBubbleTab = null
                                                onDismissRequestedBubble()
                                            },
                                            onLongPress = {
                                                // إظهار الفقاعة الشفافة عند الاستمرار بالضغط مطولاً
                                                internalBubbleTab = tab
                                            }
                                        )
                                    }
                                    .padding(
                                        horizontal = if (isSelected) 14.dp else 10.dp,
                                        vertical = 8.dp
                                    )
                                    .testTag("nav_tab_${tab.name.lowercase()}"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = tab.title,
                                        tint = if (isSelected) {
                                            Color(0xFFFFD54F) // amberAccent
                                        } else {
                                            if (isDarkTheme) Color.White.copy(alpha = 0.70f)
                                            else Color(0xFF1B4D3E).copy(alpha = 0.60f)
                                        },
                                        modifier = Modifier.size(22.dp)
                                    )

                                    AnimatedVisibility(
                                        visible = isSelected,
                                        enter = fadeIn(animationSpec = spring(stiffness = 300f)) + expandHorizontally(),
                                        exit = fadeOut(animationSpec = spring(stiffness = 300f)) + shrinkHorizontally()
                                    ) {
                                        Row {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = tab.title,
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = if (isDarkTheme) Color.White else Color(0xFF1B4D3E),
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 12.sp
                                                )
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
}
