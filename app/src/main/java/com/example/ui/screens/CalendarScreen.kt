package com.example.ui.screens

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlarmOn
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Flare
import androidx.compose.material.icons.filled.HotelClass
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.NotificationAdd
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HijriCalendarDay
import com.example.data.SampleData
import com.example.ui.components.ArabesqueBackgroundPattern
import com.example.ui.components.toArabicNumerals
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
  onBack: () -> Unit,
  onShowMessage: (String) -> Unit = {}
) {
  var selectedDay by remember { mutableIntStateOf(14) }
  var viewMode by remember { mutableStateOf("شهري") }
  var hilalOffset by remember { mutableIntStateOf(0) }
  var showDayDetailSheet by remember { mutableStateOf(false) }
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  val activeDay = SampleData.ramadanDays.find { it.day == selectedDay }
    ?: SampleData.ramadanDays[13]

  val dayNames = listOf("السبت", "الأحد", "الإثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة")
  val activeDayName = dayNames[(selectedDay - 1) % 7]

  Box(modifier = Modifier.fillMaxSize()) {
    ArabesqueBackgroundPattern()

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      item { Spacer(modifier = Modifier.height(4.dp)) }

      // 1. Navigation & View Switcher Bar
      item {
        Card(
          shape = RoundedCornerShape(24.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                IconButton(
                  onClick = { onShowMessage("الشهر السابق: شعبان ١٤٤٦ هـ") },
                  modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                ) {
                  Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "السابق", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                }
                IconButton(
                  onClick = { onShowMessage("الشهر القادم: شوال ١٤٤٦ هـ") },
                  modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                ) {
                  Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = "التالي", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                }
              }

              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(18.dp))
                  Text(
                    text = "رمضان ١٤٤٦ هـ",
                    style = MaterialTheme.typography.titleMedium.copy(fontFamily = FontFamily.Serif),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                  )
                }
                Text(
                  text = "مارس – أبريل ٢٠٢٥ م",
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }

              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(20.dp))
                  .background(MaterialTheme.colorScheme.primary)
                  .clickable {
                    selectedDay = 14
                    onShowMessage("الانتقال إلى اليوم الحالي: ١٤ رمضان")
                  }
                  .padding(horizontal = 14.dp, vertical = 6.dp)
                  .testTag("btn_calendar_today")
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Icon(imageVector = Icons.Default.Today, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                  Text(text = "اليوم", style = MaterialTheme.typography.labelSmall, color = Color.White, fontWeight = FontWeight.Bold)
                }
              }
            }

            // Segmented View Modes
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(3.dp),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              listOf("شهري", "أسبوعي", "المناسبات").forEach { mode ->
                val isSelected = viewMode == mode
                Box(
                  modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                      if (isSelected) MaterialTheme.colorScheme.surfaceContainerLowest
                      else Color.Transparent
                    )
                    .clickable { viewMode = mode }
                    .padding(vertical = 6.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = mode,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                  )
                }
              }
            }
          }
        }
      }

      // 2. Lunar Overview Banner (Tactile Manuscript Motif)
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(48.dp)
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Bedtime,
                  contentDescription = null,
                  tint = SecondaryFixed,
                  modifier = Modifier.size(28.dp)
                )
              }

              Column {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  Text(
                    text = "${activeDay.moonPhase} (ليلة ${toArabicNumerals(selectedDay)})",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.secondaryFixed,
                    fontWeight = FontWeight.Bold
                  )
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(10.dp))
                      .background(MaterialTheme.colorScheme.primaryContainer)
                      .padding(horizontal = 6.dp, vertical = 2.dp)
                  ) {
                    Text(
                      text = "مكتمل ${activeDay.moonPercent}",
                      style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                      color = MaterialTheme.colorScheme.tertiaryFixed,
                      fontWeight = FontWeight.Bold
                    )
                  }
                }
                Text(
                  text = "منتصف شهر الرحمة والمغفرة • الأيام البيض",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.surfaceVariant
                )
              }
            }

            Column(horizontalAlignment = Alignment.End) {
              Text(
                text = "العدة الفلكية",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primaryFixedDim
              )
              Text(
                text = "٣٠ يوماً",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }

      // 3. Interactive Hijri Calendar Grid
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            // Days of Week Header
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceAround
            ) {
              dayNames.forEach { dayName ->
                val isFri = dayName == "الجمعة"
                Box(
                  modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isFri) SecondaryFixed.copy(alpha = 0.25f) else Color.Transparent)
                    .padding(vertical = 4.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = dayName,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    color = if (isFri) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            }

            // 7 Columns Grid for Ramadan 30 Days
            val days = SampleData.ramadanDays
            val rows = days.chunked(7)

            rows.forEach { week ->
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                week.forEach { d ->
                  val isSelected = d.day == selectedDay
                  val isToday = d.day == 14

                  Box(
                    modifier = Modifier
                      .weight(1f)
                      .height(58.dp)
                      .clip(RoundedCornerShape(8.dp))
                      .background(
                        when {
                          isSelected -> MaterialTheme.colorScheme.primary
                          d.isLaylatAlQadr -> MaterialTheme.colorScheme.secondaryContainer
                          d.isWhiteDay -> SecondaryFixed.copy(alpha = 0.35f)
                          d.isLastTen -> TertiaryFixed.copy(alpha = 0.4f)
                          d.isFriday -> SecondaryFixed.copy(alpha = 0.2f)
                          else -> MaterialTheme.colorScheme.surfaceContainerLow
                        }
                      )
                      .clickable {
                        selectedDay = d.day
                        showDayDetailSheet = true
                      }
                      .padding(3.dp)
                      .testTag("cal_day_${d.day}"),
                    contentAlignment = Alignment.Center
                  ) {
                    Column(
                      modifier = Modifier.fillMaxSize(),
                      horizontalAlignment = Alignment.CenterHorizontally,
                      verticalArrangement = Arrangement.SpaceBetween
                    ) {
                      Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                      ) {
                        Text(
                          text = toArabicNumerals(d.day),
                          style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                          color = if (isSelected) MaterialTheme.colorScheme.secondaryFixed else MaterialTheme.colorScheme.outline
                        )
                        if (d.isSunnah || d.isFriday || d.isWhiteDay) {
                          Box(
                            modifier = Modifier
                              .size(4.dp)
                              .clip(CircleShape)
                              .background(if (isSelected) MaterialTheme.colorScheme.secondaryFixed else MaterialTheme.colorScheme.secondary)
                          )
                        }
                      }

                      Text(
                        text = toArabicNumerals(d.day),
                        style = MaterialTheme.typography.titleSmall,
                        color = when {
                          isSelected -> Color.White
                          d.isFriday -> MaterialTheme.colorScheme.secondary
                          d.isLaylatAlQadr -> MaterialTheme.colorScheme.onSecondaryContainer
                          else -> MaterialTheme.colorScheme.primary
                        },
                        fontWeight = FontWeight.Bold
                      )

                      Text(
                        text = d.tag.ifEmpty { "-" },
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                        color = when {
                          isSelected -> MaterialTheme.colorScheme.secondaryContainer
                          d.tag.isEmpty() -> Color.Transparent
                          else -> MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        maxLines = 1
                      )
                    }
                  }
                }

                // If week is shorter than 7, fill empty slots
                if (week.size < 7) {
                  repeat(7 - week.size) {
                    Spacer(modifier = Modifier.weight(1f))
                  }
                }
              }
            }
          }
        }
      }

      // 4. Legend & Indicators Section
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
          elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(MaterialTheme.colorScheme.secondary))
              Text(text = "الأيام البيض (١٣-١٥)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(MaterialTheme.colorScheme.tertiary))
              Text(text = "سنن الإثنين والخميس", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(12.dp))
              Text(text = "العشر الأواخر", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }
        }
      }

      // 5. Selected Day Interactive Details Card
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("selected_day_card")
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            // Card Header
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.Top
            ) {
              Column {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(10.dp))
                      .background(MaterialTheme.colorScheme.primary)
                      .padding(horizontal = 8.dp, vertical = 2.dp)
                  ) {
                    Text(text = "اليوم المحدد", style = MaterialTheme.typography.labelSmall, color = Color.White)
                  }
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(10.dp))
                      .background(SecondaryFixed)
                      .padding(horizontal = 8.dp, vertical = 2.dp)
                  ) {
                    Text(
                      text = "$activeDayName المبارك",
                      style = MaterialTheme.typography.labelSmall,
                      color = MaterialTheme.colorScheme.onSecondaryContainer,
                      fontWeight = FontWeight.Bold
                    )
                  }
                }

                Text(
                  text = "$activeDayName ${toArabicNumerals(selectedDay)} رمضان ١٤٤٦ هـ",
                  style = MaterialTheme.typography.titleLarge.copy(fontFamily = FontFamily.Serif),
                  color = MaterialTheme.colorScheme.primary,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                  text = "٢١ مارس ٢٠٢٥ م • ثاني الأيام البيض • اكتمال بدر التمام",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }

              Box(
                modifier = Modifier
                  .size(44.dp)
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Nightlight,
                  contentDescription = null,
                  tint = SecondaryFixed,
                  modifier = Modifier.size(24.dp)
                )
              }
            }

            // Prayer Times Strip
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(text = "مواقيت الصلاة لمكة المكرمة", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Text(text = "توقيت أم القرى", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
              }

              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(12.dp))
                  .background(MaterialTheme.colorScheme.surfaceContainerLow)
                  .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceAround
              ) {
                SampleData.prayerTimes.forEach { p ->
                  Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = p.name, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                    Text(
                      text = toArabicNumerals(p.time),
                      style = MaterialTheme.typography.labelMedium,
                      color = if (p.name == "الظهر") MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                      fontWeight = FontWeight.Bold
                    )
                  }
                }
              }
            }

            // Daily Sunan & Reminders
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
              Text(
                text = "سنن وفضائل اليوم:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
              )

              SunnahItemRow(
                title = "قراءة سورة الكهف",
                desc = "نورٌ يضيء للمؤمن ما بين الجمعتين",
                icon = Icons.Default.MenuBook,
                iconBg = SecondaryFixed.copy(alpha = 0.5f),
                iconColor = MaterialTheme.colorScheme.secondary
              )
              SunnahItemRow(
                title = "الإكثار من الصلاة على النبي ﷺ",
                desc = "«إن من أفضل أيامكم يوم الجمعة فأكثروا عليّ من الصلاة فيه»",
                icon = Icons.Default.VolunteerActivism,
                iconBg = PrimaryFixed,
                iconColor = MaterialTheme.colorScheme.primary
              )
              SunnahItemRow(
                title = "تحري ساعة الإجابة",
                desc = "أرجى ساعات الجمعة بعد العصر حتى غروب الشمس",
                icon = Icons.Default.Schedule,
                iconBg = TertiaryFixed,
                iconColor = MaterialTheme.colorScheme.tertiary
              )
            }

            // Action Buttons (Add Reminder & Export Imsakiya)
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Box(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(14.dp))
                  .background(MaterialTheme.colorScheme.primary)
                  .clickable { onShowMessage("تم فتح نافذة جدولة التذكيرات لليوم المحدد") }
                  .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  Icon(imageVector = Icons.Default.NotificationAdd, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                  Text(text = "إضافة تذكير أو ورد", style = MaterialTheme.typography.labelMedium, color = Color.White, fontWeight = FontWeight.Bold)
                }
              }

              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(14.dp))
                  .background(MaterialTheme.colorScheme.surfaceContainer)
                  .clickable { onShowMessage("جاري تجهيز وتحميل إمساكية شهر رمضان ١٤٤٦ هـ بصيغة PDF") }
                  .padding(horizontal = 14.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Icon(imageVector = Icons.Default.Download, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                  Text(text = "إمساكية (PDF)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
              }
            }
          }
        }
      }

      // 6. Upcoming Major Events Teaser (عيد الفطر، ليلة القدر، etc.)
      item {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "أبرز محطات شهر رمضان المبارك",
              style = MaterialTheme.typography.titleMedium,
              color = MaterialTheme.colorScheme.primary,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "١٤٤٦ هـ",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.secondary,
              fontWeight = FontWeight.Bold
            )
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            // Event 1
            Card(
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
              elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
              modifier = Modifier.weight(1f)
            ) {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  Icon(imageVector = Icons.Default.HotelClass, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(18.dp))
                  Text(text = "ليلة القدر المرجوة", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "ليلة ٢٧ رمضان (مساء الخميس ٢٧ مارس)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(SecondaryFixed)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                  Text(text = "بعد ١٣ يوماً", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSecondaryContainer, fontWeight = FontWeight.Bold)
                }
              }
            }

            // Event 2
            Card(
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
              elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
              modifier = Modifier.weight(1f)
            ) {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  Icon(imageVector = Icons.Default.Celebration, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                  Text(text = "عيد الفطر المبارك", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "الأول من شوال (الإثنين ٣١ مارس)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(TertiaryFixed)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                  Text(text = "بعد ١٧ يوماً", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onTertiaryContainer, fontWeight = FontWeight.Bold)
                }
              }
            }
          }
        }
      }

      item { Spacer(modifier = Modifier.height(80.dp)) }
    }
  }

  // Day Detail Smooth Bottom Sheet Modal
  if (showDayDetailSheet) {
    ModalBottomSheet(
      onDismissRequest = { showDayDetailSheet = false },
      sheetState = sheetState,
      containerColor = MaterialTheme.colorScheme.surface,
      shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
      dragHandle = {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 4.dp),
          contentAlignment = Alignment.Center
        ) {
          Box(
            modifier = Modifier
              .width(44.dp)
              .height(5.dp)
              .clip(RoundedCornerShape(3.dp))
              .background(MaterialTheme.colorScheme.outlineVariant)
          )
        }
      }
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // Sheet Header Banner
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                  Text(text = "$activeDayName المبارك", style = MaterialTheme.typography.labelSmall, color = Color.White)
                }
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(SecondaryFixed)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                  Text(text = activeDay.tag.ifEmpty { "يوم الصيام" }, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSecondaryContainer, fontWeight = FontWeight.Bold)
                }
              }

              Text(
                text = "$activeDayName ${toArabicNumerals(selectedDay)} رمضان ١٤٤٦ هـ",
                style = MaterialTheme.typography.titleLarge.copy(fontFamily = FontFamily.Serif),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp)
              )
              Text(
                text = "${activeDay.gregorianDate} مارس ٢٠٢٥ م • منتصف الشهر المبارك",
                style = MaterialTheme.typography.bodySmall,
                color = SecondaryFixed
              )
            }

            Box(
              modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.12f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(imageVector = Icons.Default.Bedtime, contentDescription = null, tint = SecondaryFixed, modifier = Modifier.size(28.dp))
            }
          }
        }

        // Actions inside sheet
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(14.dp))
              .background(MaterialTheme.colorScheme.primary)
              .clickable {
                showDayDetailSheet = false
                onShowMessage("تمت إضافة تذكيرات سنن $activeDayName والورد اليومي بنجاح")
              }
              .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Icon(imageVector = Icons.Default.AlarmOn, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
              Text(text = "إضافة تذكير للتقويم", style = MaterialTheme.typography.labelMedium, color = Color.White, fontWeight = FontWeight.Bold)
            }
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(14.dp))
              .background(MaterialTheme.colorScheme.surfaceContainerHigh)
              .clickable {
                showDayDetailSheet = false
                onShowMessage("تم نسخ تفاصيل هذا اليوم المبارك للمشاركة")
              }
              .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
          ) {
            Icon(imageVector = Icons.Default.Share, contentDescription = "مشاركة", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
          }
        }

        Spacer(modifier = Modifier.height(20.dp))
      }
    }
  }
}

@Composable
fun SunnahItemRow(
  title: String,
  desc: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  iconBg: Color,
  iconColor: Color
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(MaterialTheme.colorScheme.surfaceContainerLow)
      .padding(10.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    Box(
      modifier = Modifier
        .size(36.dp)
        .clip(CircleShape)
        .background(iconBg),
      contentAlignment = Alignment.Center
    ) {
      Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(18.dp))
    }
    Column {
      Text(text = title, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
      Text(text = desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
  }
}
