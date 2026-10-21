package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Mosque
import androidx.compose.material.icons.outlined.Radio
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.Secondary
import com.example.ui.theme.SecondaryContainer
import com.example.ui.theme.SecondaryFixed
import com.example.ui.theme.SurfaceContainerLow

enum class AppTab(val title: String) {
  Home("الرئيسية"),
  Quran("القراء"),
  Media("قوائم التشغيل"),
  Athkar("الإعدادات"),
  Qibla("بحث")
}

@Composable
fun AppHeader(
  title: String = "الرئيسية",
  subtitle: String = "منار الإسلام",
  isDarkTheme: Boolean = false,
  onThemeToggle: () -> Unit = {},
  onNotificationsClick: () -> Unit = {},
  onSearchClick: () -> Unit = {},
  onSettingsClick: () -> Unit = {},
  onVolumeClick: () -> Unit = {}
) {
  val headerBg = if (isDarkTheme) {
    Color(0xFF0F172A).copy(alpha = 0.88f)
  } else {
    Color(0xFFF7F5F0).copy(alpha = 0.94f)
  }
  val titleColor = if (isDarkTheme) Color.White else Color(0xFF1F3A2E)
  val subtitleColor = if (isDarkTheme) Color(0xFFFFD54F) else Color(0xFF1B4D3E).copy(alpha = 0.80f)

  Surface(
    modifier = Modifier.fillMaxWidth(),
    color = headerBg,
    shadowElevation = 4.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(68.dp)
        .padding(horizontal = 14.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Action Buttons (الزجاج الدائري الأنيق)
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        // زر تبديل المظهر الإسلامي (ليلي فخم / عاجي أصيل)
        TopIconButton(
          icon = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
          contentDescription = "تبديل المظهر والزخارف (ليلي ملكي / عاجي أصيل)",
          onClick = onThemeToggle,
          testTag = "theme_toggle_button",
          isDarkTheme = isDarkTheme
        )
        TopIconButton(
          icon = Icons.Default.Settings,
          contentDescription = "الإعدادات",
          onClick = onSettingsClick,
          testTag = "settings_button",
          isDarkTheme = isDarkTheme
        )
        TopIconButton(
          icon = Icons.Default.NotificationsNone,
          contentDescription = "التنبيهات والأذان",
          onClick = onNotificationsClick,
          testTag = "notifications_button",
          isDarkTheme = isDarkTheme
        )
        TopIconButton(
          icon = Icons.Default.VolumeUp,
          contentDescription = "الصوت والإذاعة",
          onClick = onVolumeClick,
          testTag = "volume_button",
          isDarkTheme = isDarkTheme
        )
        TopIconButton(
          icon = Icons.Default.Search,
          contentDescription = "البحث السريع",
          onClick = onSearchClick,
          testTag = "search_button",
          isDarkTheme = isDarkTheme
        )
      }

      // Title & Motto (لا تنسى ذكر الله)
      Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Center
      ) {
        Text(
          text = if (title == "الرئيسية" || title == "Home") "لا تنسى ذكر الله" else title,
          style = MaterialTheme.typography.titleMedium.copy(
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
          ),
          color = titleColor
        )
        Text(
          text = subtitle,
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.sp
          ),
          color = subtitleColor
        )
      }
    }
  }
}

@Composable
fun TopIconButton(
  icon: ImageVector,
  contentDescription: String?,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  testTag: String = "",
  isDarkTheme: Boolean = false
) {
  val btnBg = if (isDarkTheme) {
    Color.White.copy(alpha = 0.12f)
  } else {
    Color(0xFF1B4D3E)
  }
  val borderColor = if (isDarkTheme) Color(0xFFFFD54F).copy(alpha = 0.45f) else Color(0xFFD4AF37).copy(alpha = 0.40f)

  Box(
    modifier = modifier
      .size(36.dp)
      .shadow(elevation = 3.dp, shape = CircleShape, spotColor = Color.Black.copy(alpha = 0.12f))
      .clip(CircleShape)
      .background(btnBg)
      .border(1.dp, borderColor, CircleShape)
      .clickable(onClick = onClick)
      .then(if (testTag.isNotEmpty()) Modifier.testTag(testTag) else Modifier),
    contentAlignment = Alignment.Center
  ) {
    Icon(
      imageVector = icon,
      contentDescription = contentDescription,
      tint = if (isDarkTheme) Color(0xFFFFD54F) else Color.White,
      modifier = Modifier.size(18.dp)
    )
  }
}

@Composable
fun AppBottomNav(
  currentTab: AppTab,
  onTabSelected: (AppTab) -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.98f),
    shadowElevation = 8.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(68.dp)
        .navigationBarsPadding()
        .padding(horizontal = 8.dp),
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically
    ) {
      AppTab.entries.forEach { tab ->
        val selected = tab == currentTab
        val (icon, outlinedIcon) = when (tab) {
          AppTab.Home -> Icons.Filled.Mosque to Icons.Outlined.Mosque
          AppTab.Quran -> Icons.Filled.MenuBook to Icons.Outlined.MenuBook
          AppTab.Athkar -> Icons.Default.AutoAwesome to Icons.Outlined.Timer
          AppTab.Qibla -> Icons.Filled.Explore to Icons.Outlined.Explore
          AppTab.Media -> Icons.Filled.Radio to Icons.Outlined.Radio
        }

        Column(
          modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onTabSelected(tab) }
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .testTag("nav_tab_${tab.name.lowercase()}"),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Box(
            modifier = Modifier
              .size(34.dp)
              .clip(CircleShape)
              .background(
                if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                else Color.Transparent
              ),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (selected) icon else outlinedIcon,
              contentDescription = tab.title,
              tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(24.dp)
            )
          }
          Text(
            text = tab.title,
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
            ),
            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }
  }
}

@Composable
fun ArabesqueBackgroundPattern(
  modifier: Modifier = Modifier,
  patternColor: Color = Color(0xFFD1E8DF).copy(alpha = 0.4f)
) {
  Canvas(modifier = modifier.fillMaxSize()) {
    val step = 20.dp.toPx()
    val radius = 1.2.dp.toPx()
    val width = size.width
    val height = size.height

    var x = 0f
    while (x < width) {
      var y = 0f
      while (y < height) {
        drawCircle(
          color = patternColor,
          radius = radius,
          center = Offset(x, y)
        )
        y += step
      }
      x += step
    }
  }
}

/**
 * خلفية الزخرفة الإسلامية الهندسية المتناسقة الفخمة المرسومة برمجياً
 * مطابقة تماماً لكود Flutter: IslamicGeometricPatternPainter
 * تدرج لوني داكن ملكي (أزرق داكن، نيلي عميق، بنفسجي ملكي) ونجمات ثمانية إسلامية متشابكة بوضوح عالي
 */
@Composable
fun IslamicGeometricPatternBackground(
  modifier: Modifier = Modifier,
  gradientColors: List<Color> = listOf(
    Color(0xFF090D16), // خلفية داكنة فخمة من كود Flutter: Color(0xFF090D16)
    Color(0xFF0C1938), // أزرق داكن فخم (deep navy blue)
    Color(0xFF161A4D), // نيلي عميق (deep indigo)
    Color(0xFF28114B), // بنفسجي ملكي (royal purple)
    Color(0xFF1A0B2E), // بنفسجي داكن عميق
    Color(0xFF090D16)  // قاعدة داكنة فخمة
  ),
  starColor: Color = Color(0xFFFFD54F).copy(alpha = 0.52f),
  accentStarColor: Color = Color(0xFFFFA000).copy(alpha = 0.65f),
  lineColor: Color = Color(0xFFFFD54F).copy(alpha = 0.28f)
) {
  Box(
    modifier = modifier
      .fillMaxSize()
      .background(
        Brush.linearGradient(
          colors = gradientColors,
          start = Offset(0f, 0f),
          end = Offset(1000f, 1800f)
        )
      )
  ) {
    Canvas(modifier = Modifier.fillMaxSize()) {
      val w = size.width
      val h = size.height
      val spacing = 75.dp.toPx()
      val radius = 26.dp.toPx()
      val strokeW = 1.6.dp.toPx()

      // 1. شبكة النجوم الهندسية الثمانية الإسلامية المتداخلة
      var x = 0f
      while (x < w + spacing) {
        var y = 0f
        while (y < h + spacing) {
          // رسم النجمة الإسلامية المثمنة الأساسية
          drawIslamicStar(
            center = Offset(x, y),
            radius = radius,
            color = starColor,
            strokeWidth = strokeW
          )

          // خطوط شبكية هندسية تربط بين رؤوس النجوم المتقابلة لتشكيل فسيفساء إسلامية أصيلة
          drawLine(
            color = lineColor,
            start = Offset(x - radius, y),
            end = Offset(x - spacing + radius, y),
            strokeWidth = 1.dp.toPx()
          )
          drawLine(
            color = lineColor,
            start = Offset(x, y - radius),
            end = Offset(x, y - spacing + radius),
            strokeWidth = 1.dp.toPx()
          )

          y += spacing
        }
        x += spacing
      }

      // 2. ميدالية النجمة الإسلامية الملكية المركزية المشعة
      val center = Offset(w / 2f, h / 2f)
      drawCircle(
        color = Color(0xFFFFD54F).copy(alpha = 0.08f),
        radius = 160.dp.toPx(),
        center = center
      )
      drawCircle(
        color = Color(0xFFFFD54F).copy(alpha = 0.35f),
        radius = 120.dp.toPx(),
        center = center,
        style = Stroke(width = 1.8.dp.toPx())
      )
      drawCircle(
        color = Color(0xFFFFD54F).copy(alpha = 0.20f),
        radius = 80.dp.toPx(),
        center = center,
        style = Stroke(width = 1.2.dp.toPx())
      )
      drawIslamicStar(
        center = center,
        radius = 65.dp.toPx(),
        color = Color(0xFFFFD54F).copy(alpha = 0.60f),
        strokeWidth = 2.4.dp.toPx()
      )
      drawIslamicStar(
        center = center,
        radius = 45.dp.toPx(),
        color = Color(0xFFFFA000).copy(alpha = 0.70f),
        strokeWidth = 2.0.dp.toPx()
      )
    }
  }
}

/**
 * دالة هندسية لرسم النجمة الإسلامية المثمنة (8-pointed star motif)
 * مع النجمة الثانية المتداخلة بزاوية مختلفة لإعطاء الطابع الإسلامي الأصيل
 */
fun DrawScope.drawIslamicStar(
  center: Offset,
  radius: Float,
  color: Color,
  strokeWidth: Float
) {
  val path = Path()
  val points = 8
  val angleStep = (Math.PI * 2) / points

  for (i in 0 until points) {
    val angle = i * angleStep
    val x = (center.x + radius * Math.cos(angle)).toFloat()
    val y = (center.y + radius * Math.sin(angle)).toFloat()
    if (i == 0) {
      path.moveTo(x, y)
    } else {
      path.lineTo(x, y)
    }
  }
  path.close()

  // رسم نجمة ثانية متداخلة بزاوية مختلفة لإعطاء الطابع الإسلامي الأصيل
  val path2 = Path()
  for (i in 0 until points) {
    val angle = (i * angleStep) + (Math.PI / points)
    val x = (center.x + (radius * 0.72f) * Math.cos(angle)).toFloat()
    val y = (center.y + (radius * 0.72f) * Math.sin(angle)).toFloat()
    if (i == 0) {
      path2.moveTo(x, y)
    } else {
      path2.lineTo(x, y)
    }
  }
  path2.close()

  drawPath(path = path, color = color, style = Stroke(width = strokeWidth))
  drawPath(path = path2, color = color, style = Stroke(width = strokeWidth))
}

/**
 * خلفية الزخارف الإسلامية النباتية والدائرية الفاتحة (Watermark)
 * مبنية مباشرة ومطابقة لكود Flutter: IslamicLightWatermarkPainter
 * خلفية بيج عاجي فاتحة (#F7F5F0) مع زخارف نباتية وهندسية ذهبية بارزة وأصيلة
 */
@Composable
fun IslamicLightWatermarkBackground(
  modifier: Modifier = Modifier,
  backgroundColor: Color = Color(0xFFF7F5F0),
  watermarkColor: Color = Color(0xFFC59B27).copy(alpha = 0.52f),
  fillColor: Color = Color(0xFFD4AF37).copy(alpha = 0.10f)
) {
  Box(
    modifier = modifier
      .fillMaxSize()
      .background(backgroundColor)
  ) {
    Canvas(modifier = Modifier.fillMaxSize()) {
      val w = size.width
      val h = size.height

      // 1. زخارف نباتية دائرية متداخلة واضحة جداً عبر خلفية الشاشة
      val motifs = listOf(
        Offset(w * 0.85f, h * 0.12f) to 125.dp.toPx(),
        Offset(w * 0.15f, h * 0.26f) to 110.dp.toPx(),
        Offset(w * 0.50f, h * 0.48f) to 140.dp.toPx(),
        Offset(w * 0.88f, h * 0.70f) to 120.dp.toPx(),
        Offset(w * 0.16f, h * 0.86f) to 135.dp.toPx(),
        Offset(w * 0.50f, h * 0.94f) to 115.dp.toPx(),
        Offset(w * 0.08f, h * 0.56f) to 100.dp.toPx()
      )

      motifs.forEach { (pos, rad) ->
        drawFloralMotif(
          center = pos,
          radius = rad,
          color = watermarkColor,
          fillColor = fillColor,
          strokeWidth = 2.0.dp.toPx()
        )
      }

      // 2. نجوم إسلامية هندسية مائية ناعمة بين الزخارف
      val stars = listOf(
        Offset(w * 0.50f, h * 0.16f) to 36.dp.toPx(),
        Offset(w * 0.82f, h * 0.38f) to 32.dp.toPx(),
        Offset(w * 0.18f, h * 0.42f) to 30.dp.toPx(),
        Offset(w * 0.72f, h * 0.84f) to 34.dp.toPx(),
        Offset(w * 0.32f, h * 0.72f) to 28.dp.toPx()
      )

      stars.forEach { (pos, rad) ->
        drawIslamicStar(
          center = pos,
          radius = rad,
          color = watermarkColor.copy(alpha = 0.45f),
          strokeWidth = 1.8.dp.toPx()
        )
      }

      // 3. إطار هندسي إسلامي ناعم في أعلى الشاشة وأسفلها (شريط أرابيسك)
      val barY1 = 8.dp.toPx()
      drawLine(
        color = watermarkColor.copy(alpha = 0.35f),
        start = Offset(0f, barY1),
        end = Offset(w, barY1),
        strokeWidth = 1.5.dp.toPx()
      )
    }
  }
}

/**
 * دالة رسم البتلات الهندسية النباتية الدائرية المتداخلة والدوائر المركزية
 */
fun DrawScope.drawFloralMotif(
  center: Offset,
  radius: Float,
  color: Color,
  fillColor: Color = Color.Transparent,
  strokeWidth: Float
) {
  val strokeStyle = Stroke(width = strokeWidth)

  // 8 بتلات دائرية متداخلة بزوايا 45 درجة
  for (i in 0 until 8) {
    val angleDegrees = i * 45f
    withTransform({
      translate(center.x, center.y)
      rotate(angleDegrees, pivot = Offset.Zero)
    }) {
      val ovalW = radius * 0.45f
      val ovalH = radius * 0.7f
      // رسم التعبئة الخفيفة لإبراز الزخرفة
      if (fillColor != Color.Transparent) {
        drawOval(
          color = fillColor,
          topLeft = Offset(-ovalW / 2f, -radius * 0.35f - ovalH / 2f),
          size = Size(ovalW, ovalH)
        )
      }
      // رسم الإطار الخارجي الذهبي
      drawOval(
        color = color,
        topLeft = Offset(-ovalW / 2f, -radius * 0.35f - ovalH / 2f),
        size = Size(ovalW, ovalH),
        style = strokeStyle
      )
    }
  }

  // دوائر مركزية متداخلة
  drawCircle(
    color = color,
    radius = radius * 0.18f,
    center = center,
    style = strokeStyle
  )
  drawCircle(
    color = color,
    radius = radius * 0.45f,
    center = center,
    style = strokeStyle
  )
}

@Composable
fun AyahRosetteMarker(
  ayahNumber: Int,
  modifier: Modifier = Modifier,
  isHighlighted: Boolean = false,
  onClick: () -> Unit = {}
) {
  Box(
    modifier = modifier
      .size(30.dp)
      .clip(CircleShape)
      .clickable(onClick = onClick)
      .testTag("ayah_marker_$ayahNumber"),
    contentAlignment = Alignment.Center
  ) {
    Canvas(modifier = Modifier.fillMaxSize()) {
      val center = Offset(size.width / 2f, size.height / 2f)
      val radius = size.minDimension / 2.3f

      // 8-point geometric star rosette
      val path = Path()
      val points = 8
      val innerRadius = radius * 0.72f
      for (i in 0 until points * 2) {
        val r = if (i % 2 == 0) radius else innerRadius
        val angle = Math.toRadians((i * 360.0 / (points * 2) - 90))
        val x = (center.x + r * Math.cos(angle)).toFloat()
        val y = (center.y + r * Math.sin(angle)).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
      }
      path.close()

      drawPath(
        path = path,
        color = if (isHighlighted) Secondary else GoldAccent
      )
      drawCircle(
        color = if (isHighlighted) SecondaryFixed else Color(0xFFFFF9EE),
        radius = radius * 0.65f,
        center = center
      )
    }

    Text(
      text = toArabicNumerals(ayahNumber),
      style = MaterialTheme.typography.labelSmall.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp
      ),
      color = if (isHighlighted) Color(0xFF5E4200) else Color(0xFF1B1C19),
      textAlign = TextAlign.Center
    )
  }
}

@Composable
fun AnimatedAudioWave(
  isPlaying: Boolean,
  modifier: Modifier = Modifier,
  color: Color = SecondaryFixed
) {
  val infiniteTransition = rememberInfiniteTransition(label = "wave")
  val h1 by infiniteTransition.animateFloat(
    initialValue = 0.3f, targetValue = 0.9f,
    animationSpec = infiniteRepeatable(tween(700, easing = FastOutSlowInEasing), RepeatMode.Reverse),
    label = "h1"
  )
  val h2 by infiniteTransition.animateFloat(
    initialValue = 0.8f, targetValue = 0.2f,
    animationSpec = infiniteRepeatable(tween(900, easing = FastOutSlowInEasing), RepeatMode.Reverse),
    label = "h2"
  )
  val h3 by infiniteTransition.animateFloat(
    initialValue = 0.4f, targetValue = 1f,
    animationSpec = infiniteRepeatable(tween(600, easing = FastOutSlowInEasing), RepeatMode.Reverse),
    label = "h3"
  )
  val h4 by infiniteTransition.animateFloat(
    initialValue = 0.9f, targetValue = 0.3f,
    animationSpec = infiniteRepeatable(tween(1100, easing = FastOutSlowInEasing), RepeatMode.Reverse),
    label = "h4"
  )

  Row(
    modifier = modifier.height(32.dp),
    horizontalArrangement = Arrangement.spacedBy(4.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    val factors = listOf(h1, h2, h3, h4, h2, h1, h3, h4, h2, h1)
    factors.forEach { factor ->
      val heightFraction = if (isPlaying) factor else 0.2f
      Box(
        modifier = Modifier
          .width(3.dp)
          .height(30.dp * heightFraction)
          .clip(RoundedCornerShape(2.dp))
          .background(color)
      )
    }
  }
}

fun toArabicNumerals(num: Int): String {
  val arabicDigits = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
  val str = num.toString()
  val sb = StringBuilder()
  for (ch in str) {
    if (ch in '0'..'9') {
      sb.append(arabicDigits[ch - '0'])
    } else {
      sb.append(ch)
    }
  }
  return sb.toString()
}

fun toArabicNumerals(str: String): String {
  val arabicDigits = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
  val sb = StringBuilder()
  for (ch in str) {
    if (ch in '0'..'9') {
      sb.append(arabicDigits[ch - '0'])
    } else {
      sb.append(ch)
    }
  }
  return sb.toString()
}
