package com.example.ui.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ArabesqueBackgroundPattern
import com.example.ui.components.toArabicNumerals
import com.example.ui.theme.*
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun QiblaScreen(
  onShowMessage: (String) -> Unit = {}
) {
  val context = LocalContext.current
  val qiblaTarget = 136f // Qibla angle from Riyadh to Makkah
  var currentHeading by remember { mutableFloatStateOf(94f) }
  var showCalibrationDialog by remember { mutableStateOf(false) }

  // Animated heading angle
  val animatedHeading by animateFloatAsState(
    targetValue = currentHeading,
    animationSpec = tween(durationMillis = 250),
    label = "heading"
  )

  // Device orientation sensor listener
  DisposableEffect(Unit) {
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    val rotationSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

    val listener = object : SensorEventListener {
      override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
          val rotationMatrix = FloatArray(9)
          SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
          val orientation = FloatArray(3)
          SensorManager.getOrientation(rotationMatrix, orientation)
          val azimuthInDegrees = Math.toDegrees(orientation[0].toDouble()).toFloat()
          currentHeading = (azimuthInDegrees + 360f) % 360f
        }
      }
      override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    if (sensorManager != null && rotationSensor != null) {
      sensorManager.registerListener(listener, rotationSensor, SensorManager.SENSOR_DELAY_UI)
    }

    onDispose {
      sensorManager?.unregisterListener(listener)
    }
  }

  val angleDiff = abs(currentHeading - qiblaTarget)
  val isAligned = angleDiff <= 4f || angleDiff >= 356f

  Box(modifier = Modifier.fillMaxSize()) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      item { Spacer(modifier = Modifier.height(4.dp)) }

      // 1. Sensor Calibration & Accuracy Pill Header
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.9f)),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(10.dp)
                  .clip(CircleShape)
                  .background(if (isAligned) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary)
              )
              Text(
                text = "مستشعر البوصلة: دقة ممتازة (±1°)",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
              )
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .clickable { showCalibrationDialog = true }
                .padding(horizontal = 10.dp, vertical = 4.dp)
                .testTag("btn_calibrate_qibla")
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Tune,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.secondary,
                  modifier = Modifier.size(14.dp)
                )
                Text(
                  text = "معايرة (8)",
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
        }
      }

      // 2. Hero Info Card: Location, Target Angle, Distance to Kaaba
      item {
        Card(
          shape = RoundedCornerShape(22.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("qibla_hero_card")
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                modifier = Modifier
                  .clip(RoundedCornerShape(20.dp))
                  .background(MaterialTheme.colorScheme.tertiaryContainer)
                  .padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.NearMe,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.secondaryFixed,
                  modifier = Modifier.size(16.dp)
                )
                Text(
                  text = "الرياض، المملكة العربية السعودية",
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.tertiaryFixed,
                  fontWeight = FontWeight.SemiBold
                )
              }

              Text(
                text = "24.7136° N, 46.6753° E",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.tertiaryFixedDim
              )
            }

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.Bottom
            ) {
              Column {
                Text(
                  text = "زاوية القبلة الدقيقة",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.tertiaryFixedDim
                )
                Row(
                  verticalAlignment = Alignment.Bottom,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Text(
                    text = "136°",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.secondaryFixed,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = "جنوب شرق (SE)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.surfaceVariant
                  )
                }
              }

              Column(horizontalAlignment = Alignment.End) {
                Text(
                  text = "المسافة إلى الكعبة",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.tertiaryFixedDim
                )
                Row(
                  verticalAlignment = Alignment.Bottom,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Text(
                    text = "٨٧٠",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = "كم",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondaryFixed
                  )
                }
              }
            }
          }
        }
      }

      // 3. Dynamic Live Alignment Status Banner
      item {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(
            containerColor = if (isAligned) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.surfaceContainer
          ),
          elevation = CardDefaults.cardElevation(defaultElevation = if (isAligned) 3.dp else 1.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = if (isAligned) Icons.Default.CheckCircle else Icons.Default.ScreenRotation,
              contentDescription = null,
              tint = if (isAligned) MaterialTheme.colorScheme.secondaryFixed else MaterialTheme.colorScheme.secondary,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (isAligned) "أنت الآن باتجاه القبلة المشرفة بدقة!"
              else "أدر هاتفك بلطف لمحاذاة السهم مع الكعبة المشرفة",
              style = MaterialTheme.typography.labelMedium,
              color = if (isAligned) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }

      // 4. Central Dial & Compass Canvas
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          // Touch on compass to toggle test alignment
          Box(
            modifier = Modifier
              .size(280.dp)
              .clickable {
                currentHeading = if (isAligned) 94f else qiblaTarget
              }
              .testTag("compass_wheel"),
            contentAlignment = Alignment.Center
          ) {
            // Compass Canvas
            Canvas(modifier = Modifier.fillMaxSize()) {
              val center = Offset(size.width / 2f, size.height / 2f)
              val radius = size.minDimension / 2f - 12.dp.toPx()

              // Outer Ring
              drawCircle(
                color = Color(0xFFE9E8E3),
                radius = radius + 6.dp.toPx(),
                center = center,
                style = Stroke(width = 2.dp.toPx())
              )
              drawCircle(
                color = GoldAccent.copy(alpha = 0.4f),
                radius = radius + 2.dp.toPx(),
                center = center,
                style = Stroke(
                  width = 1.5.dp.toPx(),
                  pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f))
                )
              )

              // Dial background parchment
              drawCircle(
                brush = Brush.radialGradient(
                  colors = listOf(Color(0xFFFFFFFF), Color(0xFFF5F4EF), Color(0xFFE9E8E3)),
                  center = center,
                  radius = radius
                ),
                radius = radius,
                center = center
              )

              // Inner rotating elements (rotate inversely to device heading)
              rotate(-animatedHeading, pivot = center) {
                // Ticks for cardinal and degree directions
                for (deg in 0 until 360 step 30) {
                  val rad = Math.toRadians(deg.toDouble() - 90)
                  val tickLen = if (deg % 90 == 0) 14.dp.toPx() else 8.dp.toPx()
                  val startX = (center.x + (radius - tickLen) * Math.cos(rad)).toFloat()
                  val startY = (center.y + (radius - tickLen) * Math.sin(rad)).toFloat()
                  val endX = (center.x + radius * Math.cos(rad)).toFloat()
                  val endY = (center.y + radius * Math.sin(rad)).toFloat()

                  drawLine(
                    color = if (deg == 0) Color(0xFFBA1A1A) else Color(0xFF707975),
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = if (deg % 90 == 0) 2.5.dp.toPx() else 1.2.dp.toPx()
                  )
                }

                // Kaaba Vector Indicator (at 136 degrees)
                rotate(qiblaTarget, pivot = center) {
                  // Dashed line to Kaaba beacon
                  drawLine(
                    color = Secondary,
                    start = center,
                    end = Offset(center.x, center.y - radius + 18.dp.toPx()),
                    strokeWidth = 2.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f))
                  )

                  // Golden Kaaba Beacon Pin
                  val kaabaCenter = Offset(center.x, center.y - radius + 18.dp.toPx())
                  drawCircle(
                    color = SecondaryContainer,
                    radius = 14.dp.toPx(),
                    center = kaabaCenter
                  )
                  // Kaaba Cube
                  val cubeSize = 14.dp.toPx()
                  drawRect(
                    color = Color(0xFF1B1C19),
                    topLeft = Offset(kaabaCenter.x - cubeSize / 2, kaabaCenter.y - cubeSize / 2),
                    size = androidx.compose.ui.geometry.Size(cubeSize, cubeSize)
                  )
                  // Golden Kiswah line
                  drawLine(
                    color = SecondaryFixed,
                    start = Offset(kaabaCenter.x - cubeSize / 2, kaabaCenter.y - cubeSize / 4),
                    end = Offset(kaabaCenter.x + cubeSize / 2, kaabaCenter.y - cubeSize / 4),
                    strokeWidth = 1.5.dp.toPx()
                  )
                }

                // Needle: North (Deep Emerald) and South (Silver Gray)
                val needlePathNorth = Path().apply {
                  moveTo(center.x, center.y - radius + 30.dp.toPx())
                  lineTo(center.x + 7.dp.toPx(), center.y - 8.dp.toPx())
                  lineTo(center.x, center.y)
                  lineTo(center.x - 7.dp.toPx(), center.y - 8.dp.toPx())
                  close()
                }
                drawPath(path = needlePathNorth, color = Primary)

                val needlePathSouth = Path().apply {
                  moveTo(center.x, center.y + radius - 30.dp.toPx())
                  lineTo(center.x + 7.dp.toPx(), center.y + 8.dp.toPx())
                  lineTo(center.x, center.y)
                  lineTo(center.x - 7.dp.toPx(), center.y + 8.dp.toPx())
                  close()
                }
                drawPath(path = needlePathSouth, color = Color(0xFFBFC9C4))

                // Needle Center jewel cap
                drawCircle(color = Color(0xFFE9E8E3), radius = 8.dp.toPx(), center = center)
                drawCircle(color = PrimaryContainer, radius = 5.dp.toPx(), center = center)
                drawCircle(color = SecondaryFixed, radius = 2.dp.toPx(), center = center)
              }
            }

            // Top Fixed Golden Reticle / Sight Notch
            Column(
              modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 4.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Box(
                modifier = Modifier
                  .size(10.dp)
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.secondary)
              )
            }
          }

          // Live Heading Degrees Readout
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(top = 8.dp)
          ) {
            Text(
              text = "${toArabicNumerals(animatedHeading.roundToInt().toString().padStart(3, '0'))}°",
              style = MaterialTheme.typography.headlineLarge,
              color = MaterialTheme.colorScheme.primary,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "زاوية الهاتف الحالية",
              style = MaterialTheme.typography.titleMedium,
              color = MaterialTheme.colorScheme.secondary,
              fontWeight = FontWeight.Bold
            )
          }

          Text(
            text = "المس الشاشة لتدوير البوصلة تجريبياً ومحاكاة محاذاة القبلة المباركة",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
          )
        }
      }

      // 5. Interactive Utility Actions Bar (3 Cards)
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          // Action 1: Re-calibrate
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier
              .weight(1f)
              .clickable { showCalibrationDialog = true }
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Box(
                modifier = Modifier
                  .size(38.dp)
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.surfaceContainerLow),
                contentAlignment = Alignment.Center
              ) {
                Icon(imageVector = Icons.Default.AllInclusive, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
              }
              Spacer(modifier = Modifier.height(6.dp))
              Text(text = "إعادة الضبط", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
              Text(text = "حركة الرقم 8", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }

          // Action 2: Confirm Map
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier
              .weight(1f)
              .clickable { onShowMessage("تم تأكيد خط الإسقاط عبر الخريطة العالمية") }
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Box(
                modifier = Modifier
                  .size(38.dp)
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.surfaceContainerLow),
                contentAlignment = Alignment.Center
              ) {
                Icon(imageVector = Icons.Default.Map, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
              }
              Spacer(modifier = Modifier.height(6.dp))
              Text(text = "تأكيد الخريطة", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
              Text(text = "خط الإسقاط", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }

          // Action 3: AR Camera Mode
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier
              .weight(1f)
              .clickable { onShowMessage("تم تشغيل وضع كاميرا التوجيه الواقعي المعزز (AR)") }
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Box(
                modifier = Modifier
                  .size(38.dp)
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.secondaryFixed),
                contentAlignment = Alignment.Center
              ) {
                Icon(imageVector = Icons.Default.ViewInAr, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondaryFixed, modifier = Modifier.size(20.dp))
              }
              Spacer(modifier = Modifier.height(6.dp))
              Text(text = "كاميرا الواقع", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
              Text(text = "توجيه حي", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
            }
          }
        }
      }

      // 6. Geographic Trajectory Arc Card
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier.fillMaxWidth()
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
                Icon(imageVector = Icons.Default.Explore, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                Text(text = "المسار الجغرافي المباشر", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
              }
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                  .padding(horizontal = 8.dp, vertical = 2.dp)
              ) {
                Text(text = "خط الدائرة العظمى", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }

            // Visual Arc
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                .padding(horizontal = 16.dp),
              contentAlignment = Alignment.Center
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Box(
                    modifier = Modifier
                      .size(34.dp)
                      .clip(CircleShape)
                      .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(imageVector = Icons.Default.MyLocation, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                  }
                  Text(text = "موقعك", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                }

                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                  ) {
                    Icon(imageVector = Icons.Default.Straighten, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(14.dp))
                    Text(text = "٨٧٠ كم باتجاه 136°", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                  }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Box(
                    modifier = Modifier
                      .size(34.dp)
                      .clip(CircleShape)
                      .background(MaterialTheme.colorScheme.secondary),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(imageVector = Icons.Default.Mosque, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                  }
                  Text(text = "الكعبة المشرفة", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                }
              }
            }
          }
        }
      }

      item { Spacer(modifier = Modifier.height(80.dp)) }
    }
  }

  // Calibration Modal
  if (showCalibrationDialog) {
    AlertDialog(
      onDismissRequest = { showCalibrationDialog = false },
      confirmButton = {
        Button(
          onClick = { showCalibrationDialog = false },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(text = "تمت المعايرة بنجاح", color = Color.White, fontWeight = FontWeight.Bold)
        }
      },
      icon = {
        Box(
          modifier = Modifier
            .size(54.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.AllInclusive,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(32.dp)
          )
        }
      },
      title = {
        Text(
          text = "معايرة مستشعر الهاتف",
          style = MaterialTheme.typography.titleMedium,
          color = MaterialTheme.colorScheme.primary,
          fontWeight = FontWeight.Bold,
          textAlign = TextAlign.Center
        )
      },
      text = {
        Text(
          text = "حرّك هاتفك في الهواء بهدوء على شكل رقم (8) لضمان دقة البوصلة ومطابقة خط الشمال المغناطيسي.",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          textAlign = TextAlign.Center
        )
      },
      shape = RoundedCornerShape(20.dp),
      containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
    )
  }
}
