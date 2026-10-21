package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WbIncandescent
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material.icons.outlined.Bedtime
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PrayerTimeItem
import com.example.data.SampleData
import com.example.ui.components.AppTab
import com.example.ui.components.ArabesqueBackgroundPattern
import com.example.ui.components.GeographicPrayerTimesCard
import com.example.ui.components.IslamicLightWatermarkBackground
import com.example.ui.components.toArabicNumerals
import com.example.ui.theme.*
import com.example.util.PrayerTimeCalculator
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
  onNavigateToQuran: () -> Unit,
  onNavigateToQibla: () -> Unit,
  onNavigateToAthkar: () -> Unit,
  onNavigateToMedia: () -> Unit,
  onOpenCalendar: () -> Unit,
  onOpenAdhanSettings: () -> Unit,
  onShowMessage: (String) -> Unit = {},
  isDarkTheme: Boolean = false,
  onTriggerBubbleTest: (AppTab) -> Unit = {}
) {
  var isRadioPlaying by remember { mutableStateOf(false) }
  var tasbeehCount by remember { mutableIntStateOf(33) }
  var isAyahBookmarked by remember { mutableStateOf(false) }

  // Animated seconds countdown for next prayer
  var remainingSeconds by remember { mutableIntStateOf(5070) } // ~01:24:30
  LaunchedEffect(Unit) {
    while (true) {
      delay(1000)
      if (remainingSeconds > 0) remainingSeconds--
    }
  }

  val hours = remainingSeconds / 3600
  val minutes = (remainingSeconds % 3600) / 60
  val seconds = remainingSeconds % 60
  val formattedCountdown = String.format("%02d:%02d:%02d", hours, minutes, seconds)

  val accurateDate = remember { PrayerTimeCalculator.getAccurateDateInfo() }

  val glassCardBg = if (isDarkTheme) Color(0xFF161A3D).copy(alpha = 0.58f) else MaterialTheme.colorScheme.surfaceContainerLowest
  val glassCardBorder = if (isDarkTheme) BorderStroke(1.dp, Color(0xFFFFD54F).copy(alpha = 0.28f)) else BorderStroke(1.dp, Color(0xFFD4AF37).copy(alpha = 0.20f))
  val glassCardTitle = if (isDarkTheme) Color.White else MaterialTheme.colorScheme.primary
  val glassCardSub = if (isDarkTheme) Color(0xFFFFD54F) else MaterialTheme.colorScheme.secondary
  val glassCardTextVariant = if (isDarkTheme) Color.White.copy(alpha = 0.75f) else MaterialTheme.colorScheme.onSurfaceVariant

  Box(modifier = Modifier.fillMaxSize()) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      item { Spacer(modifier = Modifier.height(4.dp)) }

      // الزخرفة الإسلامية ونجمة الزجاج السائل المركزية (بطاقة زجاج سائل فخمة)
      item {
        val heroCardBg = if (isDarkTheme) {
          Color(0xFF1E1B4B).copy(alpha = 0.72f)
        } else {
          Color.White.copy(alpha = 0.85f)
        }
        val heroBorderColor = if (isDarkTheme) Color(0xFFFFD54F).copy(alpha = 0.65f) else Color(0xFFD4AF37).copy(alpha = 0.50f)
        val heroTitleColor = if (isDarkTheme) Color.White else Color(0xFF1F3A2E)
        val heroSubColor = if (isDarkTheme) Color(0xFFFFD54F) else Color(0xFF1B4D3E).copy(alpha = 0.80f)

        Card(
          shape = RoundedCornerShape(26.dp),
          colors = CardDefaults.cardColors(containerColor = heroCardBg),
          elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
          border = BorderStroke(1.4.dp, heroBorderColor),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("islamic_liquid_glass_hero")
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 20.dp, horizontal = 16.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.Center
            ) {
              // زخرفة نجمة إسلامية مركزية أيقونية ذهبية مشعة
              Box(
                modifier = Modifier
                  .size(76.dp)
                  .clip(CircleShape)
                  .background(if (isDarkTheme) Color(0xFF0F172A).copy(alpha = 0.85f) else Color(0xFF1B4D3E).copy(alpha = 0.08f))
                  .border(
                    width = 1.8.dp,
                    color = if (isDarkTheme) Color(0xFFFFD54F) else Color(0xFFD4AF37).copy(alpha = 0.7f),
                    shape = CircleShape
                  )
                  .shadow(
                    elevation = 14.dp,
                    shape = CircleShape,
                    spotColor = Color(0xFFD4AF37).copy(alpha = 0.35f)
                  ),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.AutoAwesome,
                  contentDescription = "الزخرفة الإسلامية المركزية",
                  tint = if (isDarkTheme) Color(0xFFFFD54F) else Color(0xFFC59B27),
                  modifier = Modifier.size(42.dp)
                )
              }

              Spacer(modifier = Modifier.height(14.dp))

              Text(
                text = if (isDarkTheme) "تطبيق الزجاج السائل والزخارف الإسلامية" else "لا تنسى ذكر الله • منار الإسلام",
                style = MaterialTheme.typography.titleLarge.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 21.sp,
                  letterSpacing = 0.5.sp
                ),
                color = heroTitleColor,
                textAlign = TextAlign.Center
              )

              Text(
                text = if (isDarkTheme)
                  "خلفية هندسية متدرجة فخمة • زجاج سائل عائم • نجوم إسلامية أصيلة"
                else
                  "تطبيق إسلامي فخم • زجاج سائل وزخارف نباتية وهندسية عاجية",
                style = MaterialTheme.typography.bodySmall.copy(
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Medium
                ),
                color = heroSubColor,
                textAlign = TextAlign.Center
              )

              Spacer(modifier = Modifier.height(14.dp))

              // شريط التلميح التفاعلي لعرض وتجربة الفقاعة الزجاجية السائلة
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(22.dp))
                  .background(
                    if (isDarkTheme) Color.White.copy(alpha = 0.12f)
                    else Color(0xFF1B4D3E).copy(alpha = 0.09f)
                  )
                  .border(
                    width = 1.2.dp,
                    color = if (isDarkTheme) Color(0xFFFFD54F).copy(alpha = 0.55f) else Color(0xFFD4AF37).copy(alpha = 0.45f),
                    shape = RoundedCornerShape(22.dp)
                  )
                  .clickable {
                    onTriggerBubbleTest(AppTab.Home)
                    onShowMessage("تم فتح فقاعة الزجاج السائل لقسم الرئيسية! ✨ (يمكنك أيضاً الضغط مطولاً على أي أيقونة)")
                  }
                  .padding(horizontal = 16.dp, vertical = 8.dp)
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = if (isDarkTheme) Color(0xFFFFD54F) else Color(0xFFC59B27),
                    modifier = Modifier.size(16.dp)
                  )
                  Text(
                    text = "اضغط مطولاً على أيقونات الشريط لعرض الفقاعة • اضغط هنا لتجربتها الآن 🌟",
                    style = MaterialTheme.typography.bodySmall.copy(
                      fontSize = 11.5.sp,
                      fontWeight = FontWeight.Bold
                    ),
                    color = heroTitleColor,
                    textAlign = TextAlign.Center
                  )
                }
              }
            }
          }
        }
      }

      // 1. Spiritual Motto & Accurate Hijri/Gregorian Date Header Card
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = glassCardBg),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          border = glassCardBorder,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("spiritual_header_card")
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
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(42.dp)
                  .clip(CircleShape)
                  .background(if (isDarkTheme) Color(0xFF2E1065).copy(alpha = 0.6f) else MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Spa,
                  contentDescription = null,
                  tint = if (isDarkTheme) Color(0xFFFFD54F) else MaterialTheme.colorScheme.onSecondaryContainer,
                  modifier = Modifier.size(22.dp)
                )
              }
              Column {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  Text(
                    text = "طُمأنينة القلب",
                    style = MaterialTheme.typography.labelSmall,
                    color = glassCardSub,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = "• ${accurateDate.gregorianFormatted}",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = glassCardTextVariant
                  )
                }
                Text(
                  text = "ألا بذكر الله تطمئن القلوب",
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = FontFamily.Serif
                  ),
                  color = glassCardTitle,
                  fontWeight = FontWeight.Bold
                )
              }
            }

            // Accurate Hijri Date Stamp
            Column(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(if (isDarkTheme) Color.White.copy(alpha = 0.10f) else MaterialTheme.colorScheme.surfaceContainerLowest)
                .clickable(onClick = onOpenCalendar)
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .testTag("hijri_date_badge"),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = accurateDate.dayOfWeekArabic,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = glassCardSub
              )
              Text(
                text = toArabicNumerals(accurateDate.hijriDay.toString()),
                style = MaterialTheme.typography.titleMedium,
                color = glassCardTitle,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "${accurateDate.hijriMonthName} ${toArabicNumerals(accurateDate.hijriYear.toString())}",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = glassCardTextVariant
              )
            }
          }
        }
      }

      // 2. Next Prayer Countdown & Geographic Prayer Times Card
      item {
        GeographicPrayerTimesCard(
          onOpenAdhanSettings = onOpenAdhanSettings,
          onShowMessage = onShowMessage
        )
      }

      // 3. Continue Reading Card ("أكمل قراءتك")
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = glassCardBg),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          border = glassCardBorder,
          modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onNavigateToQuran)
            .testTag("continue_reading_card")
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
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
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerLow),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(24.dp)
                  )
                }

                Column {
                  Text(
                    text = "متابعة الختمة الحالية",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                  Text(
                    text = "سورة الكهف • آية ٢٨",
                    style = MaterialTheme.typography.titleMedium.copy(
                      fontFamily = FontFamily.Serif
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = "الجزء الخامس عشر • الحزب ٣٠",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.SemiBold
                  )
                }
              }

              Box(
                modifier = Modifier
                  .size(38.dp)
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.ChevronLeft,
                  contentDescription = "متابعة القراءة",
                  tint = MaterialTheme.colorScheme.onPrimary,
                  modifier = Modifier.size(20.dp)
                )
              }
            }

            // Progress bar
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "نسبة الإنجاز",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Text(
                text = "٤٨٪",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
              )
            }
            LinearProgressIndicator(
              progress = { 0.48f },
              modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(4.dp)),
              color = MaterialTheme.colorScheme.secondary,
              trackColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
          }
        }
      }

      // 4. Luxury Emerald Verse of the Day Card ("قبس من الذكر الحكيم")
      item {
        Card(
          shape = RoundedCornerShape(22.dp),
          colors = CardDefaults.cardColors(
            containerColor = if (isDarkTheme) Color(0xFF1B153D).copy(alpha = 0.70f) else MaterialTheme.colorScheme.tertiaryContainer
          ),
          border = BorderStroke(1.2.dp, if (isDarkTheme) Color(0xFFFFD54F).copy(alpha = 0.40f) else Color(0xFFD4AF37).copy(alpha = 0.30f)),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("verse_of_the_day_card")
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            // Gold badge
            Row(
              modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Primary.copy(alpha = 0.4f))
                .padding(horizontal = 12.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = SecondaryFixed,
                modifier = Modifier.size(16.dp)
              )
              Text(
                text = "قبس من الذكر الحكيم • آية اليوم",
                style = MaterialTheme.typography.labelSmall,
                color = SecondaryFixed,
                fontWeight = FontWeight.Bold
              )
            }

            // Quran Verse Text
            Text(
              text = "﴿ وَمَن يَتَّقِ اللَّهَ يَجْعَل لَّهُ مَخْرَجًا ۝ وَيَرْزُقْهُ مِنْ حَيْثُ لَا يَحْتَسِبُ ﴾",
              style = MaterialTheme.typography.headlineSmall.copy(
                fontFamily = FontFamily.Serif,
                fontSize = 20.sp,
                lineHeight = 36.sp
              ),
              color = Color.White,
              textAlign = TextAlign.Center,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 4.dp)
            )

            // Metadata & Action Icons
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "سورة الطلاق • الآية ٢-٣",
                style = MaterialTheme.typography.labelSmall,
                color = PrimaryFixedDim
              )

              Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(
                  onClick = { onShowMessage("تشغيل التلاوة الصوتية للآية") },
                  modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Primary.copy(alpha = 0.5f))
                ) {
                  Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "استماع",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                  )
                }

                IconButton(
                  onClick = {
                    isAyahBookmarked = !isAyahBookmarked
                    onShowMessage(if (isAyahBookmarked) "تمت إضافة الآية للمفضلة" else "تمت إزالة الآية")
                  },
                  modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Primary.copy(alpha = 0.5f))
                ) {
                  Icon(
                    imageVector = if (isAyahBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = "حفظ",
                    tint = if (isAyahBookmarked) SecondaryFixed else Color.White,
                    modifier = Modifier.size(16.dp)
                  )
                }

                IconButton(
                  onClick = { onShowMessage("تم نسخ الآية المباركة للمشاركة") },
                  modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                ) {
                  Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "مشاركة",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(16.dp)
                  )
                }
              }
            }
          }
        }
      }

      // 5. Four Pillars of Knowledge (أركان منار المعرفية)
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
                  .background(MaterialTheme.colorScheme.secondary)
              )
              Text(
                text = "أركان منار المعرفية",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
              )
            }
            Text(
              text = "بوابات النور",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.secondary,
              fontWeight = FontWeight.SemiBold
            )
          }

          // 2x2 Grid
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            // Pillar 1: Quran
            PillarCard(
              title = "القرآن الكريم",
              subtitle = "تلاوة، تدبر، وترتيل متقن",
              badge = "١١٤ سورة",
              icon = Icons.Default.MenuBook,
              iconColor = MaterialTheme.colorScheme.secondary,
              iconBg = SecondaryFixed.copy(alpha = 0.3f),
              modifier = Modifier.weight(1f),
              onClick = onNavigateToQuran
            )

            // Pillar 2: Sunnah
            PillarCard(
              title = "السنة النبوية",
              subtitle = "الأحاديث النبوية وشروحها",
              badge = "الصحاح",
              icon = Icons.Default.Spa,
              iconColor = MaterialTheme.colorScheme.primary,
              iconBg = TertiaryFixed,
              modifier = Modifier.weight(1f),
              onClick = { onShowMessage("فتح موسوعة السنة النبوية الشريفة") }
            )
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            // Pillar 3: Library
            PillarCard(
              title = "مكتبة منار",
              subtitle = "أمهات الكتب والتفاسير",
              badge = "موسوعات",
              icon = Icons.Default.HistoryEdu,
              iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
              iconBg = MaterialTheme.colorScheme.surfaceContainerHigh,
              modifier = Modifier.weight(1f),
              onClick = { onShowMessage("فتح مكتبة منار الإسلامية") }
            )

            // Pillar 4: Education
            PillarCard(
              title = "تعليم منار",
              subtitle = "دروس التجويد والعقيدة",
              badge = "دورات",
              icon = Icons.Default.WbIncandescent,
              iconColor = MaterialTheme.colorScheme.primary,
              iconBg = PrimaryFixed,
              modifier = Modifier.weight(1f),
              onClick = { onShowMessage("فتح دورات تعليم منار") }
            )
          }
        }
      }

      // 6. Quick Spiritual Access (الوصول السريع الروحاني)
      item {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Box(
              modifier = Modifier
                .width(4.dp)
                .height(18.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(MaterialTheme.colorScheme.secondary)
            )
            Text(
              text = "الوصول السريع الروحاني",
              style = MaterialTheme.typography.titleMedium,
              color = MaterialTheme.colorScheme.primary,
              fontWeight = FontWeight.Bold
            )
          }

          // Live Radio Card
          Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = glassCardBg),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            border = glassCardBorder,
            modifier = Modifier
              .fillMaxWidth()
              .clickable(onClick = onNavigateToMedia)
              .testTag("radio_quick_card")
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
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (isDarkTheme) Color(0xFF2E1065).copy(alpha = 0.6f) else MaterialTheme.colorScheme.secondaryContainer),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Radio,
                    contentDescription = null,
                    tint = if (isDarkTheme) Color(0xFFFFD54F) else MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(22.dp)
                  )
                }
                Column {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                  ) {
                    Text(
                      text = "إذاعة القرآن الكريم",
                      style = MaterialTheme.typography.titleSmall,
                      color = glassCardTitle,
                      fontWeight = FontWeight.Bold
                    )
                    Box(
                      modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.errorContainer)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                      Text(
                        text = "مباشر",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontWeight = FontWeight.Bold
                      )
                    }
                  }
                  Text(
                    text = "بث القاهرة ومكة المكرمة • كبار القراء",
                    style = MaterialTheme.typography.bodySmall,
                    color = glassCardTextVariant
                  )
                }
              }

              IconButton(
                onClick = {
                  isRadioPlaying = !isRadioPlaying
                  onShowMessage(if (isRadioPlaying) "تم بدء البث المباشر لإذاعة القرآن" else "تم إيقاف البث مؤقتاً")
                },
                modifier = Modifier
                  .size(42.dp)
                  .clip(CircleShape)
                  .background(if (isRadioPlaying) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary)
                  .testTag("home_radio_play_button")
              ) {
                Icon(
                  imageVector = if (isRadioPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                  contentDescription = "تشغيل الإذاعة",
                  tint = Color.White,
                  modifier = Modifier.size(24.dp)
                )
              }
            }
          }

          // Qibla Compass Card
          Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = glassCardBg),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            border = glassCardBorder,
            modifier = Modifier
              .fillMaxWidth()
              .clickable(onClick = onNavigateToQibla)
              .testTag("qibla_quick_card")
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
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (isDarkTheme) Color(0xFF2E1065).copy(alpha = 0.6f) else TertiaryFixed),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Explore,
                    contentDescription = null,
                    tint = if (isDarkTheme) Color(0xFFFFD54F) else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp)
                  )
                }
                Column {
                  Text(
                    text = "اتجاه القبلة الشريفة",
                    style = MaterialTheme.typography.titleSmall,
                    color = glassCardTitle,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = "الكعبة المشرفة على بعد 136° جنوب شرق",
                    style = MaterialTheme.typography.bodySmall,
                    color = glassCardSub,
                    fontWeight = FontWeight.SemiBold
                  )
                }
              }

              Row(
                modifier = Modifier
                  .clip(RoundedCornerShape(10.dp))
                  .background(if (isDarkTheme) Color.White.copy(alpha = 0.10f) else MaterialTheme.colorScheme.surfaceContainerLowest)
                  .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Text(
                  text = "معايرة",
                  style = MaterialTheme.typography.labelSmall,
                  color = glassCardSub,
                  fontWeight = FontWeight.Bold
                )
                Icon(
                  imageVector = Icons.Default.Explore,
                  contentDescription = null,
                  tint = glassCardSub,
                  modifier = Modifier.size(16.dp)
                )
              }
            }
          }

          // Smart Tasbeeh Card with Interactive Counter
          Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = glassCardBg),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            border = glassCardBorder,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("tasbeeh_quick_card")
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
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = glassCardSub,
                    modifier = Modifier.size(18.dp)
                  )
                  Text(
                    text = "سبحة الاستغفار الذكية",
                    style = MaterialTheme.typography.titleSmall,
                    color = glassCardTitle,
                    fontWeight = FontWeight.Bold
                  )
                }
                Text(
                  text = "تم ${toArabicNumerals(tasbeehCount)} مرة",
                  style = MaterialTheme.typography.labelSmall,
                  color = glassCardTextVariant
                )
              }

              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(14.dp))
                  .background(if (isDarkTheme) Color.White.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceContainerLow)
                  .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = "أَسْتَغْفِرُ اللَّهَ الْعَظِيمَ وَأَتُوبُ إِلَيْهِ",
                    style = MaterialTheme.typography.titleSmall.copy(
                      fontFamily = FontFamily.Serif
                    ),
                    color = glassCardTitle,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = "سيد الاستغفار ونماء الرزق والبركة",
                    style = MaterialTheme.typography.labelSmall,
                    color = glassCardSub
                  )
                }

                // Interactive Tap Button
                var isPressed by remember { mutableStateOf(false) }
                val scale by animateFloatAsState(targetValue = if (isPressed) 0.9f else 1f, label = "press")

                Box(
                  modifier = Modifier
                    .size(54.dp)
                    .scale(scale)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable {
                      isPressed = true
                      tasbeehCount++
                    }
                    .testTag("home_tasbeeh_button"),
                  contentAlignment = Alignment.Center
                ) {
                  LaunchedEffect(isPressed) {
                    if (isPressed) {
                      delay(100)
                      isPressed = false
                    }
                  }
                  Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                      text = toArabicNumerals(tasbeehCount),
                      style = MaterialTheme.typography.titleMedium,
                      color = SecondaryFixed,
                      fontWeight = FontWeight.Bold
                    )
                    Text(
                      text = "تسبيحة",
                      style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                      color = PrimaryFixedDim
                    )
                  }
                }
              }
            }
          }
        }
      }

      item { Spacer(modifier = Modifier.height(80.dp)) }
    }
  }
}

@Composable
fun PillarCard(
  title: String,
  subtitle: String,
  badge: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  iconColor: Color,
  iconBg: Color,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    modifier = modifier
      .clickable(onClick = onClick)
      .testTag("pillar_card_${title}")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(iconBg),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = title,
            tint = iconColor,
            modifier = Modifier.size(24.dp)
          )
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
          Text(
            text = badge,
            style = MaterialTheme.typography.labelSmall,
            color = iconColor,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold
      )
      Text(
        text = subtitle,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}
