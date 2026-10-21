package com.example.ui.screens

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.DhikrItem
import com.example.data.SampleData
import com.example.ui.components.ArabesqueBackgroundPattern
import com.example.ui.components.toArabicNumerals
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight
import com.example.ui.theme.Primary
import com.example.ui.theme.Secondary

/**
 * Athkar Category Popup Dialog (شاشة منبثقة مستقلة لعرض أذكار القسم المختار)
 * Fully replaces the in-list accordion animation with a dedicated full popup window.
 */
@Composable
fun AthkarCategoryPopupDialog(
    categoryName: String,
    onDismiss: () -> Unit,
    onShowMessage: (String) -> Unit = {}
) {
    val clipboardManager = LocalClipboardManager.current

    // Prepare Athkar items for this category
    val initialItems = remember(categoryName) {
        val filtered = SampleData.sampleAthkar.filter {
            when (categoryName) {
                "أذكار الصباح والمساء", "قسم الأذكار" -> it.category.contains("الصباح") || it.category.contains("المساء") || it.category.contains("أذكار")
                "قسم الأدعية" -> it.category.contains("أدعية") || it.virtue.contains("دعاء") || it.category.contains("الصلاة")
                "قسم الفضائل" -> it.virtue.isNotEmpty()
                "الرقية الشرعية" -> it.category.contains("الرقية") || it.virtue.contains("تحصين") || it.text.contains("أعوذ")
                else -> true
            }
        }
        if (filtered.isNotEmpty()) filtered else SampleData.sampleAthkar
    }

    var itemsList by remember(categoryName) {
        mutableStateOf(initialItems.map { it.copy(currentCount = it.initialCount) })
    }

    val completedCount = itemsList.count { it.currentCount == 0 }
    val totalCount = itemsList.size
    val progress = if (totalCount > 0) completedCount.toFloat() / totalCount.toFloat() else 0f

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                ArabesqueBackgroundPattern()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                ) {
                    // Top App Bar for the Popup Screen
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Primary,
                        shadowElevation = 6.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {
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
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(GoldAccent.copy(alpha = 0.2f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AutoAwesome,
                                            contentDescription = null,
                                            tint = GoldLight,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }

                                    Column {
                                        Text(
                                            text = categoryName,
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                            color = Color.White
                                        )
                                        Text(
                                            text = "مكتمل: ${toArabicNumerals(completedCount)} من ${toArabicNumerals(totalCount)} أذكار",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = GoldLight
                                        )
                                    }
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    // Reset All Button
                                    IconButton(
                                        onClick = {
                                            itemsList = itemsList.map { it.copy(currentCount = it.initialCount) }
                                            onShowMessage("تمت إعادة ضبط عداد الأذكار")
                                        },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Refresh,
                                            contentDescription = "إعادة ضبط",
                                            tint = Color.White,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }

                                    // Close Dialog Button
                                    IconButton(
                                        onClick = onDismiss,
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(Color.White.copy(alpha = 0.15f))
                                            .testTag("popup_close_button")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "إغلاق النافذة",
                                            tint = Color.White,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Overall Section Progress Bar
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = GoldAccent,
                                trackColor = Color.White.copy(alpha = 0.2f)
                            )
                        }
                    }

                    // Content Scroll List of Athkar Cards
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item { Spacer(modifier = Modifier.height(6.dp)) }

                        items(itemsList, key = { it.id }) { dhikr ->
                            val isCompleted = dhikr.currentCount == 0

                            Card(
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isCompleted) Color(0xFFF1F8F5) else MaterialTheme.colorScheme.surfaceContainerLowest
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = if (isCompleted) 1.dp else 3.dp),
                                border = if (isCompleted) androidx.compose.foundation.BorderStroke(1.dp, Primary.copy(alpha = 0.4f)) else null,
                                modifier = Modifier.fillMaxWidth().testTag("popup_dhikr_${dhikr.id}")
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    // Arabic Text with Tashkeel
                                    Text(
                                        text = dhikr.text,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontFamily = FontFamily.Serif,
                                            fontSize = 18.sp,
                                            lineHeight = 32.sp
                                        ),
                                        color = if (isCompleted) Primary.copy(alpha = 0.8f) else Primary,
                                        fontWeight = FontWeight.SemiBold,
                                        textAlign = TextAlign.Start
                                    )

                                    // Virtue Box
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(
                                                if (isCompleted) Color(0xFFE2F0EA)
                                                else MaterialTheme.colorScheme.surfaceContainerLow
                                            )
                                            .padding(10.dp)
                                    ) {
                                        Text(
                                            text = "الفضل: ${dhikr.virtue}",
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp),
                                            color = Secondary
                                        )
                                    }

                                    // Action Row
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            IconButton(
                                                onClick = {
                                                    clipboardManager.setText(AnnotatedString(dhikr.text))
                                                    onShowMessage("تم نسخ الذكر المبارك إلى الحافظة")
                                                },
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.ContentCopy,
                                                    contentDescription = "نسخ",
                                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }

                                            Text(
                                                text = "العدد المطلوب: ${toArabicNumerals(dhikr.initialCount)}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }

                                        // Big Interactive Tasbeeh Decrement Button
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(14.dp))
                                                .background(
                                                    if (isCompleted) Brush.linearGradient(listOf(Primary, Primary))
                                                    else Brush.verticalGradient(listOf(Secondary, Color(0xFF9E7B27)))
                                                )
                                                .clickable {
                                                    if (dhikr.currentCount > 0) {
                                                        val newCount = dhikr.currentCount - 1
                                                        itemsList = itemsList.map {
                                                            if (it.id == dhikr.id) it.copy(currentCount = newCount) else it
                                                        }
                                                        if (newCount == 0) {
                                                            onShowMessage("ما شاء الله! تم إتمام هذا الذكر المبارك ✓")
                                                        }
                                                    } else {
                                                        // Reset individual
                                                        itemsList = itemsList.map {
                                                            if (it.id == dhikr.id) it.copy(currentCount = it.initialCount) else it
                                                        }
                                                    }
                                                }
                                                .padding(horizontal = 22.dp, vertical = 10.dp)
                                                .testTag("popup_counter_btn_${dhikr.id}"),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (isCompleted) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Check,
                                                        contentDescription = null,
                                                        tint = GoldLight,
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                    Text(
                                                        text = "تم بحمد الله",
                                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                                        color = Color.White
                                                    )
                                                }
                                            } else {
                                                Text(
                                                    text = toArabicNumerals(dhikr.currentCount),
                                                    style = MaterialTheme.typography.titleMedium.copy(
                                                        fontSize = 19.sp,
                                                        fontWeight = FontWeight.Bold
                                                    ),
                                                    color = Color.White
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        item { Spacer(modifier = Modifier.height(20.dp)) }
                    }

                    // Bottom Floating Close Button
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        shadowElevation = 8.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Button(
                                onClick = onDismiss,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Primary)
                            ) {
                                Text(
                                    text = "إغلاق والعودة إلى قائمة الأذكار",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
