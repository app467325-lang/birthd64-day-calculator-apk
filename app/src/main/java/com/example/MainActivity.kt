package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.ui.components.AppHeader
import com.example.ui.components.AppTab
import com.example.ui.components.FloatingBottomNavDock
import com.example.ui.components.IslamicGeometricPatternBackground
import com.example.ui.components.IslamicLightWatermarkBackground
import com.example.ui.screens.AdhanSettingsScreen
import com.example.ui.screens.AthkarScreen
import com.example.ui.screens.CalendarScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MediaScreen
import com.example.ui.screens.QiblaScreen
import com.example.ui.screens.QuranScreen
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
          MainAppContent()
        }
      }
    }
  }
}

@Composable
fun MainAppContent() {
  var currentTab by remember { mutableStateOf(AppTab.Home) }
  var showCalendarScreen by remember { mutableStateOf(false) }
  var showAdhanSettings by remember { mutableStateOf(false) }
  var isDarkLuxuryTheme by remember { mutableStateOf(true) }
  var requestedBubbleTab by remember { mutableStateOf<AppTab?>(null) }

  val snackbarHostState = remember { SnackbarHostState() }
  val coroutineScope = rememberCoroutineScope()

  val showMessage: (String) -> Unit = { message ->
    coroutineScope.launch {
      snackbarHostState.showSnackbar(message)
    }
  }

  Box(modifier = Modifier.fillMaxSize()) {
    // 1. الخلفية الإسلامية المرسومة برمجياً (تبديل بين النمط الليلي الملكي والنمط العاجي الأصيل)
    if (isDarkLuxuryTheme) {
      IslamicGeometricPatternBackground()
    } else {
      IslamicLightWatermarkBackground()
    }

    Scaffold(
      modifier = Modifier.fillMaxSize(),
      containerColor = Color.Transparent,
      topBar = {
        if (!showCalendarScreen && !showAdhanSettings) {
          val (title, subtitle) = when (currentTab) {
            AppTab.Home -> "الرئيسية" to "منار الإسلام"
            AppTab.Quran -> "القراء" to "تلاوات وقراءات كبار القراء"
            AppTab.Media -> "قوائم التشغيل" to "إذاعات وبث مباشر وتلاوات نادرة"
            AppTab.Athkar -> "الإعدادات" to "تنبيهات الأذان والمواقيت والضبط"
            AppTab.Qibla -> "بحث" to "البحث السريع واتجاه القبلة"
          }
          AppHeader(
            title = title,
            subtitle = subtitle,
            isDarkTheme = isDarkLuxuryTheme,
            onThemeToggle = {
              isDarkLuxuryTheme = !isDarkLuxuryTheme
              showMessage(
                if (isDarkLuxuryTheme) "تم تفعيل النمط الليلي الملكي والزخارف الهندسية الذهبية 🌙"
                else "تم تفعيل النمط العاجي الأصيل والزخارف النباتية الفاتحة ☀️"
              )
            },
            onNotificationsClick = { showAdhanSettings = true },
            onSettingsClick = { showAdhanSettings = true },
            onVolumeClick = { currentTab = AppTab.Media },
            onSearchClick = { showMessage("البحث السريع في القرآن الكريم والأذكار والإذاعات") }
          )
        }
      },
      bottomBar = {
        if (!showCalendarScreen && !showAdhanSettings) {
          FloatingBottomNavDock(
            currentTab = currentTab,
            onTabSelected = { currentTab = it },
            isDarkTheme = isDarkLuxuryTheme,
            requestedBubbleTab = requestedBubbleTab,
            onDismissRequestedBubble = { requestedBubbleTab = null }
          )
        }
      },
      snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
      }
    ) { innerPadding ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
      ) {
        when {
          showCalendarScreen -> {
            CalendarScreen(
              onBack = { showCalendarScreen = false },
              onShowMessage = showMessage
            )
          }
          showAdhanSettings -> {
            AdhanSettingsScreen(
              onBack = { showAdhanSettings = false },
              onShowMessage = showMessage
            )
          }
          else -> {
            AnimatedContent(
              targetState = currentTab,
              transitionSpec = { fadeIn() togetherWith fadeOut() },
              label = "tab_transition"
            ) { tab ->
              when (tab) {
                AppTab.Home -> HomeScreen(
                  onNavigateToQuran = { currentTab = AppTab.Quran },
                  onNavigateToQibla = { currentTab = AppTab.Qibla },
                  onNavigateToAthkar = { currentTab = AppTab.Athkar },
                  onNavigateToMedia = { currentTab = AppTab.Media },
                  onOpenCalendar = { showCalendarScreen = true },
                  onOpenAdhanSettings = { showAdhanSettings = true },
                  onShowMessage = showMessage,
                  isDarkTheme = isDarkLuxuryTheme,
                  onTriggerBubbleTest = { requestedBubbleTab = it }
                )

                AppTab.Quran -> QuranScreen(
                  onShowMessage = showMessage
                )

                AppTab.Athkar -> AdhanSettingsScreen(
                  onBack = { currentTab = AppTab.Home },
                  onShowMessage = showMessage
                )

                AppTab.Qibla -> QiblaScreen(
                  onShowMessage = showMessage
                )

                AppTab.Media -> MediaScreen(
                  onShowMessage = showMessage
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  androidx.compose.material3.Text(text = "Hello $name!", modifier = modifier)
}
