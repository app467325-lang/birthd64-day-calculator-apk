package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Flare
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.NotificationImportant
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.data.AdhanSetting
import com.example.data.SampleData
import com.example.ui.components.ArabesqueBackgroundPattern
import com.example.ui.components.toArabicNumerals
import com.example.ui.theme.*
import com.example.util.AudioContentType
import com.example.util.GlobalAudioController
import com.example.util.NotificationHelper

@Composable
fun AdhanSettingsScreen(
  onBack: () -> Unit,
  onShowMessage: (String) -> Unit = {}
) {
  val context = LocalContext.current
  val isAudioPlaying by GlobalAudioController.isPlaying.collectAsState()
  val isAudioBuffering by GlobalAudioController.isBuffering.collectAsState()
  val activeAudioTitle by GlobalAudioController.currentTitle.collectAsState()
  val activeAudioType by GlobalAudioController.currentType.collectAsState()

  var isMasterEnabled by remember { mutableStateOf(true) }
  var gradualVolume by remember { mutableStateOf(true) }
  var flipToSilence by remember { mutableStateOf(true) }
  var wudhuReminder by remember { mutableStateOf(true) }
  var iqamahReminder by remember { mutableStateOf(true) }

  val notificationPermissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
  ) { isGranted ->
    if (isGranted) {
      NotificationHelper.sendAdhanNotification(
        context = context,
        title = "حان الآن موعد صلاة العصر",
        content = "حي على الصلاة، حي على الفلاح • بصوت مؤذن الحرم المكي الشريف"
      )
      onShowMessage("تم تفعيل الإشعارات وإرسال إشعار أذان تجريبي!")
    } else {
      onShowMessage("يرجى تفعيل صلاحية الإشعارات من إعدادات النظام")
    }
  }

  fun triggerTestNotification() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
        NotificationHelper.sendAdhanNotification(
          context = context,
          title = "حان الآن موعد صلاة العصر",
          content = "حي على الصلاة، حي على الفلاح • بصوت مؤذن الحرم المكي الشريف"
        )
        onShowMessage("تم إرسال إشعار الأذان لشريط الإشعارات بنجاح!")
      } else {
        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
      }
    } else {
      NotificationHelper.sendAdhanNotification(
        context = context,
        title = "حان الآن موعد صلاة العصر",
        content = "حي على الصلاة، حي على الفلاح • بصوت مؤذن الحرم المكي الشريف"
      )
      onShowMessage("تم إرسال إشعار الأذان بنجاح!")
    }
  }

  val muezzins = remember {
    listOf(
      MuezzinVoiceItem("أذان الحرم المكي الشريف", "الشيخ علي ملا", Icons.Default.Mosque, "https://archive.org/download/MakkahFajrAdhan6913SheikhAliMullah/Makkah%20Fajr%20Adhan%206-9-13%20Sheikh%20Ali%20Mullah.mp3", "الحرم المكي"),
      MuezzinVoiceItem("أذان المسجد النبوي الشريف", "الشيخ عصام بخاري", Icons.Default.Domain, "https://archive.org/download/adhan_202303/alharam-almudnaa.mp3", "المسجد النبوي"),
      MuezzinVoiceItem("أذان المسجد الأقصى المبارك", "التكبيرات المقدسية", Icons.Default.LocationCity, "https://archive.org/download/adhan_202303/adhan-alquds.mp3", "القدس الشريف"),
      MuezzinVoiceItem("أذان الشيخ عبد الباسط", "الشيخ عبد الباسط عبد الصمد", Icons.Default.Campaign, "https://archive.org/download/adhan_202303/eabd-albasit-eabdalsamad.mp3", "التراث الذهبي"),
      MuezzinVoiceItem("أذان الشيخ مصطفى إسماعيل", "الشيخ مصطفى إسماعيل", Icons.Default.Mic, "https://archive.org/download/adhan_202303/mustafaa-iismaeil.mp3", "تراث مصر"),
      MuezzinVoiceItem("أذان الشيخ مشاري العفاسي", "الشيخ مشاري راشد العفاسي", Icons.Default.AutoStories, "https://archive.org/download/adhan_202303/musharaa-bin%20rashid-aleafasaa.mp3", "الكويت"),
      MuezzinVoiceItem("أذان الشيخ محمود الطبلاوي", "الشيخ محمود محمد الطبلاوي", Icons.Default.Mic, "https://archive.org/download/adhan_202303/mahmud-muhamad-altablawi.mp3", "تراث مصر"),
      MuezzinVoiceItem("أذان الشيخ ناصر القطامي", "الشيخ ناصر القطامي", Icons.Default.Explore, "https://archive.org/download/adhan_202303/nasir-alqatamaa.mp3", "الرياض"),
      MuezzinVoiceItem("أذان الشيخ ياسر الدوسري", "الشيخ ياسر الدوسري", Icons.Default.Mosque, "https://archive.org/download/adhan_202303/yasir-aldawsari.mp3", "الحرم المكي"),
      MuezzinVoiceItem("أذان الشيخ سيد النقشبندي", "الشيخ سيد النقشبندي", Icons.Default.Campaign, "https://archive.org/download/adhan_202303/sayid-alnaqshabandaa.mp3", "ابتهالات وأذان")
    )
  }

  Box(modifier = Modifier.fillMaxSize()) {
    ArabesqueBackgroundPattern()

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      item { Spacer(modifier = Modifier.height(4.dp)) }

      // Top Back and Title Header
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            IconButton(
              onClick = onBack,
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
            ) {
              Icon(imageVector = Icons.Default.Close, contentDescription = "رجوع", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            }
            Column {
              Text(
                text = "منبه الأذان والتنبيهات",
                style = MaterialTheme.typography.titleLarge.copy(fontFamily = FontFamily.Serif),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "ضبط المواقيت والأصوات لكل صلاة",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }

      // Master Adhan Banner & Countdown Widget
      item {
        Card(
          shape = RoundedCornerShape(22.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
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
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.NotificationsActive,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondaryFixed,
                    modifier = Modifier.size(22.dp)
                  )
                }
                Column {
                  Text(
                    text = "التنبيهات لجميع الصلوات",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = if (isMasterEnabled) "مفعلة تلقائياً" else "معطلة",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primaryFixedDim
                  )
                }
              }

              Switch(
                checked = isMasterEnabled,
                onCheckedChange = { isMasterEnabled = it },
                colors = SwitchDefaults.colors(
                  checkedThumbColor = Color.White,
                  checkedTrackColor = MaterialTheme.colorScheme.secondary
                )
              )
            }

            // Live Next Prayer Countdown Alert Box
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White.copy(alpha = 0.1f))
                .padding(12.dp),
              horizontalArrangement = Arrangement.spacedBy(10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Timer,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondaryFixed,
                modifier = Modifier.size(22.dp)
              )
              Column(modifier = Modifier.weight(1f)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text(
                    text = "الأذان القادم: صلاة العصر",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondaryFixed,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = "01:24:30",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                  )
                }
                Text(
                  text = "بصوت الشيخ محمد رفعت • تذكير الوضوء قبل ١٥ دقيقة",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.surfaceVariant
                )
              }
            }
          }
        }
      }

      // Prayers List Header
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
                .background(MaterialTheme.colorScheme.secondary)
            )
            Text(
              text = "أوقات الأذان والمؤذن المخصص",
              style = MaterialTheme.typography.titleMedium,
              color = MaterialTheme.colorScheme.primary,
              fontWeight = FontWeight.Bold
            )
          }
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .background(MaterialTheme.colorScheme.surfaceContainerHigh)
              .padding(horizontal = 8.dp, vertical = 2.dp)
          ) {
            Text(
              text = "٧ مواقيت مفعلة",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      // List of Prayer Notification Cards
      items(SampleData.adhanSettingsList) { adhan ->
        var enabled by remember { mutableStateOf(adhan.isEnabled) }
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(
            containerColor = if (adhan.isNext) MaterialTheme.colorScheme.surfaceContainerLow
            else MaterialTheme.colorScheme.surfaceContainerLowest
          ),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
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
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                      if (adhan.isNext) MaterialTheme.colorScheme.primaryContainer
                      else MaterialTheme.colorScheme.surfaceContainerLow
                    ),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = when {
                      adhan.prayerName.contains("الفجر") -> Icons.Default.NightsStay
                      adhan.prayerName.contains("الشروق") -> Icons.Default.WbTwilight
                      adhan.prayerName.contains("العصر") -> Icons.Default.WbSunny
                      adhan.prayerName.contains("قيام") -> Icons.Default.Flare
                      else -> Icons.Default.NotificationsActive
                    },
                    contentDescription = null,
                    tint = if (adhan.isNext) Color.White else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                  )
                }

                Column {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                  ) {
                    Text(
                      text = adhan.prayerName,
                      style = MaterialTheme.typography.titleSmall,
                      color = MaterialTheme.colorScheme.primary,
                      fontWeight = FontWeight.Bold
                    )
                    if (adhan.isNext) {
                      Box(
                        modifier = Modifier
                          .clip(RoundedCornerShape(8.dp))
                          .background(MaterialTheme.colorScheme.secondary)
                          .padding(horizontal = 6.dp, vertical = 2.dp)
                      ) {
                        Text(text = "الأذان القادم", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp), color = Color.White, fontWeight = FontWeight.Bold)
                      }
                    }
                  }
                  Text(
                    text = adhan.reciter,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }

              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                Column(horizontalAlignment = Alignment.End) {
                  Text(
                    text = adhan.time,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = adhan.period,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }

                Switch(
                  checked = enabled,
                  onCheckedChange = { enabled = it },
                  colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = MaterialTheme.colorScheme.primary
                  )
                )
              }
            }

            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                .padding(8.dp)
            ) {
              Text(
                text = adhan.reminderDetail,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }

      // Notification Test & Activation Banner
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier.fillMaxWidth().testTag("adhan_notification_activation_card")
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
                  .background(Primary),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Campaign,
                  contentDescription = null,
                  tint = GoldLight,
                  modifier = Modifier.size(22.dp)
                )
              }
              Column {
                Text(
                  text = "إشعار الأذان الفعلي للجهاز",
                  style = MaterialTheme.typography.titleSmall,
                  fontWeight = FontWeight.Bold,
                  color = Primary
                )
                Text(
                  text = "تفعيل وتجربة إشعار الأذان في شريط الإشعارات",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Secondary)
                .clickable { triggerTestNotification() }
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .testTag("trigger_test_notification_btn")
            ) {
              Text(
                text = "إرسال إشعار تجريبي",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White
              )
            }
          }
        }
      }

      // Audio Reciter Selection Carousel
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
              .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "مؤذنو الحرمين الشريفين والتراث (استماع فوري)",
                  style = MaterialTheme.typography.titleMedium,
                  color = Primary,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = "انقر للاستماع واختيار الأذان المفضل للصلوات",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
              Icon(imageVector = Icons.Default.GraphicEq, contentDescription = null, tint = Secondary, modifier = Modifier.size(24.dp))
            }

            muezzins.chunked(2).forEach { rowPair ->
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                rowPair.forEach { muezzin ->
                  val isThisPlaying = activeAudioType == AudioContentType.ADHAN && isAudioPlaying && activeAudioTitle == muezzin.title
                  val isThisBuffering = activeAudioType == AudioContentType.ADHAN && isAudioBuffering && activeAudioTitle == muezzin.title

                  AdhanSampleCard(
                    title = muezzin.title,
                    reciter = muezzin.reciter,
                    icon = muezzin.icon,
                    tag = muezzin.tag,
                    isPlaying = isThisPlaying,
                    isBuffering = isThisBuffering,
                    modifier = Modifier.weight(1f),
                    onPlay = {
                      if (isThisPlaying) {
                        GlobalAudioController.togglePlayPause()
                        onShowMessage("تم إيقاف صوت الأذان")
                      } else {
                        GlobalAudioController.playAdhan(
                          title = muezzin.title,
                          muezzinName = muezzin.reciter,
                          audioUrl = muezzin.audioUrl,
                          context = context
                        )
                        onShowMessage("بدء تشغيل ${muezzin.title} بصوت ${muezzin.reciter}")
                      }
                    }
                  )
                }
              }
            }
          }
        }
      }

      // Interactive Custom Notification Preferences (Toggles)
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
              .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Text(
              text = "خيارات التنبيه المخصص والذكي",
              style = MaterialTheme.typography.titleMedium,
              color = MaterialTheme.colorScheme.primary,
              fontWeight = FontWeight.Bold
            )

            NotificationOptionRow(
              title = "تصاعد صوت الأذان تدريجيًا",
              desc = "يبدأ هادئاً ثم يتصاعد برفق",
              icon = Icons.Default.VolumeUp,
              checked = gradualVolume,
              onCheckedChange = { gradualVolume = it }
            )

            NotificationOptionRow(
              title = "كتم الصوت الذكي بقلب الهاتف",
              desc = "اقلب الجهاز لوجهه لكتم صوت الأذان فوراً",
              icon = Icons.Default.ScreenRotation,
              checked = flipToSilence,
              onCheckedChange = { flipToSilence = it }
            )

            NotificationOptionRow(
              title = "تذكير الاستعداد للوضوء والسنن",
              desc = "إشعار هادئ قبل الأذان بـ ١٥ دقيقة",
              icon = Icons.Default.HourglassTop,
              checked = wudhuReminder,
              onCheckedChange = { wudhuReminder = it }
            )

            NotificationOptionRow(
              title = "تنبيه إقامة الصلاة",
              desc = "صوت تكبيرة بعد الأذان بـ ١٥ دقيقة",
              icon = Icons.Default.NotificationImportant,
              checked = iqamahReminder,
              onCheckedChange = { iqamahReminder = it }
            )
          }
        }
      }

      // Live Audio Test Button
      item {
        val isTestPlaying = activeAudioType == AudioContentType.ADHAN && isAudioPlaying
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (isTestPlaying) Secondary else Primary)
            .clickable {
              if (isTestPlaying) {
                GlobalAudioController.stop()
                onShowMessage("تم إيقاف صوت الأذان")
              } else {
                GlobalAudioController.playAdhan(
                  title = "أذان الحرم المكي الشريف",
                  muezzinName = "الشيخ علي ملا",
                  audioUrl = "https://archive.org/download/MakkahFajrAdhan6913SheikhAliMullah/Makkah%20Fajr%20Adhan%206-9-13%20Sheikh%20Ali%20Mullah.mp3",
                  context = context
                )
                onShowMessage("جاري تجربة أذان الحرم المكي (الشيخ علي ملا)...")
              }
            }
            .padding(vertical = 14.dp)
            .testTag("adhan_bottom_test_audio_btn"),
          contentAlignment = Alignment.Center
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(
              imageVector = if (isTestPlaying) Icons.Default.StopCircle else Icons.Default.VolumeUp,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(22.dp)
            )
            Text(
              text = if (isTestPlaying) "إيقاف صوت الأذان" else "تجربة صوت الأذان الفوري الآن",
              style = MaterialTheme.typography.titleMedium,
              color = Color.White,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      item { Spacer(modifier = Modifier.height(80.dp)) }
    }
  }
}

@Composable
fun AdhanSampleCard(
  title: String,
  reciter: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  tag: String,
  isPlaying: Boolean,
  isBuffering: Boolean,
  modifier: Modifier = Modifier,
  onPlay: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isPlaying) Color(0xFFEBF7F2) else MaterialTheme.colorScheme.surfaceContainerLow
    ),
    border = if (isPlaying) androidx.compose.foundation.BorderStroke(1.2.dp, Primary) else null,
    modifier = modifier.clickable(onClick = onPlay).testTag("adhan_card_${title}")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp),
      verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(if (isPlaying) Primary else MaterialTheme.colorScheme.primaryContainer),
          contentAlignment = Alignment.Center
        ) {
          Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
        }

        Box(
          modifier = Modifier
            .size(30.dp)
            .clip(CircleShape)
            .background(if (isPlaying) Secondary else Primary.copy(alpha = 0.15f)),
          contentAlignment = Alignment.Center
        ) {
          if (isBuffering) {
            CircularProgressIndicator(
              modifier = Modifier.size(16.dp),
              color = if (isPlaying) Color.White else Primary,
              strokeWidth = 2.dp
            )
          } else {
            Icon(
              imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
              contentDescription = null,
              tint = if (isPlaying) Color.White else Primary,
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }

      Text(
        text = title,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = Primary
      )
      Text(
        text = reciter,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
      if (tag.isNotBlank()) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Secondary.copy(alpha = 0.12f))
            .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Text(text = tag, style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp), color = Secondary, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

data class MuezzinVoiceItem(
  val title: String,
  val reciter: String,
  val icon: androidx.compose.ui.graphics.vector.ImageVector,
  val audioUrl: String,
  val tag: String = ""
)

@Composable
fun NotificationOptionRow(
  title: String,
  desc: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(MaterialTheme.colorScheme.surfaceContainerLow)
      .padding(12.dp),
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
          .size(36.dp)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.surfaceContainer),
        contentAlignment = Alignment.Center
      ) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
      }
      Column {
        Text(text = title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Text(text = desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
    }

    Switch(
      checked = checked,
      onCheckedChange = onCheckedChange,
      colors = SwitchDefaults.colors(
        checkedThumbColor = Color.White,
        checkedTrackColor = MaterialTheme.colorScheme.primary
      )
    )
  }
}
