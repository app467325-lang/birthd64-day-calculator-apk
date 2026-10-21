package com.example.ui.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.outlined.Bedtime
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material.icons.outlined.WbTwilight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.Secondary
import com.example.ui.theme.SecondaryContainer
import com.example.ui.theme.SecondaryFixed
import com.example.util.PrayerTimeCalculator
import kotlinx.coroutines.delay

data class CityLocation(
    val arabicName: String,
    val country: String,
    val lat: Double,
    val lng: Double,
    val timeZoneOffset: Double
)

val popularIslamicCities = listOf(
    CityLocation("مكة المكرمة", "المملكة العربية السعودية", 21.4225, 39.8262, 3.0),
    CityLocation("المدينة المنورة", "المملكة العربية السعودية", 24.4672, 39.6111, 3.0),
    CityLocation("دمشق", "الجمهورية العربية السورية", 33.5138, 36.2765, 3.0),
    CityLocation("القدس الشريف", "فلسطين", 31.7683, 35.2137, 2.0),
    CityLocation("القاهرة", "جمهورية مصر العربية", 30.0444, 31.2357, 2.0),
    CityLocation("الرياض", "المملكة العربية السعودية", 24.7136, 46.6753, 3.0),
    CityLocation("دبي / أبوظبي", "الإمارات العربية المتحدة", 25.2048, 55.2708, 4.0),
    CityLocation("إسطنبول", "تركيا", 41.0082, 28.9784, 3.0),
    CityLocation("بغداد", "العراق", 33.3152, 44.3661, 3.0),
    CityLocation("الدار البيضاء / الرباط", "المملكة المغربية", 33.5731, -7.5898, 1.0),
    CityLocation("الجزائر العاصمة", "الجزائر", 36.7538, 3.0588, 1.0),
    CityLocation("تونس", "تونس", 36.8065, 10.1815, 1.0),
    CityLocation("عمان", "المملكة الأردنية", 31.9454, 35.9284, 3.0),
    CityLocation("الدوحة", "قطر", 25.2854, 51.5310, 3.0),
    CityLocation("الكويت", "الكويت", 29.3759, 47.9774, 3.0)
)

/**
 * High-precision Daily Prayer Times Card driven by Geographic Location
 * (GPS device sensor or chosen Islamic City)
 */
@Composable
fun GeographicPrayerTimesCard(
    onOpenAdhanSettings: () -> Unit,
    onShowMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var selectedCity by remember { mutableStateOf(popularIslamicCities[0]) }
    var isUsingGps by remember { mutableStateOf(false) }
    var currentLatitude by remember { mutableDoubleStateOf(popularIslamicCities[0].lat) }
    var currentLongitude by remember { mutableDoubleStateOf(popularIslamicCities[0].lng) }
    var locationDisplayName by remember { mutableStateOf(popularIslamicCities[0].arabicName) }
    var showCityDropdown by remember { mutableStateOf(false) }

    // Location Permission launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        if (fineGranted || coarseGranted) {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            var bestLoc: Location? = null
            try {
                val providers = locationManager?.getProviders(true) ?: emptyList()
                for (p in providers) {
                    val l = locationManager?.getLastKnownLocation(p) ?: continue
                    if (bestLoc == null || l.accuracy < bestLoc.accuracy) {
                        bestLoc = l
                    }
                }
            } catch (_: SecurityException) {}

            if (bestLoc != null) {
                currentLatitude = bestLoc.latitude
                currentLongitude = bestLoc.longitude
                isUsingGps = true
                locationDisplayName = "موقعك الحالي (GPS)"
                onShowMessage("تم تحديد إحداثيات موقعك الجغرافي بنجاح")
            } else {
                onShowMessage("تم تفعيل إذن الموقع، يُرجى التأكد من تشغيل GPS بالجهاز")
            }
        } else {
            onShowMessage("تم رفض إذن الموقع، تم ضبط المواقيت تلقائياً على ${selectedCity.arabicName}")
        }
    }

    var selectedMethod by remember { mutableStateOf(PrayerTimeCalculator.CalculationMethod.UMM_AL_QURA) }
    var showMethodDropdown by remember { mutableStateOf(false) }
    var showAdjustDialog by remember { mutableStateOf(false) }

    // Manual fine-tuning minute offsets
    var fajrOffset by remember { mutableStateOf(0) }
    var sunriseOffset by remember { mutableStateOf(0) }
    var dhuhrOffset by remember { mutableStateOf(0) }
    var asrOffset by remember { mutableStateOf(0) }
    var maghribOffset by remember { mutableStateOf(0) }
    var ishaOffset by remember { mutableStateOf(0) }

    fun recomputePrayerTimes(): PrayerTimeCalculator.DailyPrayerTimes {
        return PrayerTimeCalculator.calculate(
            latitude = currentLatitude,
            longitude = currentLongitude,
            timeZoneOffsetHours = selectedCity.timeZoneOffset,
            method = selectedMethod,
            fajrOffsetMin = fajrOffset,
            sunriseOffsetMin = sunriseOffset,
            dhuhrOffsetMin = dhuhrOffset,
            asrOffsetMin = asrOffset,
            maghribOffsetMin = maghribOffset,
            ishaOffsetMin = ishaOffset
        )
    }

    // Prayer times state calculated from coordinates
    var prayerTimes by remember(currentLatitude, currentLongitude, selectedMethod, fajrOffset, sunriseOffset, dhuhrOffset, asrOffset, maghribOffset, ishaOffset) {
        mutableStateOf(recomputePrayerTimes())
    }

    // Countdown timer
    var remainingSeconds by remember { mutableLongStateOf(prayerTimes.remainingSecondsToNext) }
    LaunchedEffect(currentLatitude, currentLongitude, selectedMethod, fajrOffset, sunriseOffset, dhuhrOffset, asrOffset, maghribOffset, ishaOffset) {
        prayerTimes = recomputePrayerTimes()
        remainingSeconds = prayerTimes.remainingSecondsToNext
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            if (remainingSeconds > 0) {
                remainingSeconds--
            } else {
                prayerTimes = recomputePrayerTimes()
                remainingSeconds = prayerTimes.remainingSecondsToNext
            }
        }
    }

    val hours = remainingSeconds / 3600
    val minutes = (remainingSeconds % 3600) / 60
    val secs = remainingSeconds % 60
    val formattedCountdown = String.format("%02d:%02d:%02d", hours, minutes, secs)

    Card(
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = Primary),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("geographic_prayer_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Location Header: Selector & GPS button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // City dropdown anchor
                Box {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White.copy(alpha = 0.14f))
                            .clickable { showCityDropdown = true }
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = if (isUsingGps) Icons.Default.MyLocation else Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = SecondaryFixed,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = locationDisplayName,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = SecondaryFixed,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = showCityDropdown,
                        onDismissRequest = { showCityDropdown = false }
                    ) {
                        popularIslamicCities.forEach { city ->
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(city.arabicName, fontWeight = FontWeight.Bold)
                                            Text(city.country, fontSize = 11.sp, color = Color.Gray)
                                        }
                                        if (selectedCity == city && !isUsingGps) {
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
                                    selectedCity = city
                                    currentLatitude = city.lat
                                    currentLongitude = city.lng
                                    locationDisplayName = city.arabicName
                                    isUsingGps = false
                                    showCityDropdown = false
                                    onShowMessage("تم تحديث مواقيت الصلاة لمدينة ${city.arabicName}")
                                }
                            )
                        }
                    }
                }

                // GPS Detect location icon button
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Secondary.copy(alpha = 0.35f))
                        .clickable {
                            val fineCheck = ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            )
                            if (fineCheck == PackageManager.PERMISSION_GRANTED) {
                                val lm = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
                                val loc = lm?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                                    ?: lm?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                                if (loc != null) {
                                    currentLatitude = loc.latitude
                                    currentLongitude = loc.longitude
                                    isUsingGps = true
                                    locationDisplayName = "موقعي الحالي (GPS)"
                                    onShowMessage("تم حساب المواقيت بناء على إحداثيات موقعك الجغرافي")
                                } else {
                                    onShowMessage("جاري انتظار إشارة الأقمار الصناعية GPS...")
                                }
                            } else {
                                locationPermissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                            }
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = "استخدام الموقع الفعلي",
                        tint = GoldLight,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "موقعي",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }

            // Next Prayer Big Hero Display & Countdown
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(SecondaryFixed)
                        )
                        Text(
                            text = "الصلاة القادمة: صلاة ${prayerTimes.nextPrayerName}",
                            style = MaterialTheme.typography.labelMedium,
                            color = SecondaryFixed,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    val nextTimeStr = when (prayerTimes.nextPrayerName) {
                        "الفجر" -> prayerTimes.fajr
                        "الشروق" -> prayerTimes.sunrise
                        "الظهر" -> prayerTimes.dhuhr
                        "العصر" -> prayerTimes.asr
                        "المغرب" -> prayerTimes.maghrib
                        else -> prayerTimes.isha
                    }

                    Text(
                        text = toArabicNumerals(nextTimeStr),
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 42.sp
                        ),
                        color = Color.White
                    )
                }

                // Countdown badge
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color.White.copy(alpha = 0.16f))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = SecondaryFixed,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "متبقي $formattedCountdown",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                ),
                                color = Color.White
                            )
                        }
                    }

                    Box {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { showMethodDropdown = true }
                                .padding(horizontal = 4.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "طريقة الحساب: ${selectedMethod.arabicTitle} ▾",
                                style = MaterialTheme.typography.labelSmall,
                                color = GoldLight,
                                fontSize = 10.sp
                            )
                        }

                        DropdownMenu(
                            expanded = showMethodDropdown,
                            onDismissRequest = { showMethodDropdown = false }
                        ) {
                            PrayerTimeCalculator.CalculationMethod.entries.forEach { method ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            method.arabicTitle,
                                            fontWeight = if (method == selectedMethod) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    onClick = {
                                        selectedMethod = method
                                        showMethodDropdown = false
                                        prayerTimes = recomputePrayerTimes()
                                        onShowMessage("تم ضبط طريقة الحساب على: ${method.arabicTitle}")
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // 6 Prayers Strip (الفجر، الشروق، الظهر، العصر، المغرب، العشاء)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val prayerItems = listOf(
                    Triple("الفجر", prayerTimes.fajr, Icons.Outlined.Bedtime),
                    Triple("الشروق", prayerTimes.sunrise, Icons.Outlined.WbTwilight),
                    Triple("الظهر", prayerTimes.dhuhr, Icons.Outlined.LightMode),
                    Triple("العصر", prayerTimes.asr, Icons.Outlined.WbSunny),
                    Triple("المغرب", prayerTimes.maghrib, Icons.Outlined.WbTwilight),
                    Triple("العشاء", prayerTimes.isha, Icons.Outlined.Bedtime)
                )

                prayerItems.forEach { (name, time, icon) ->
                    val isNext = name == prayerTimes.nextPrayerName
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                if (isNext) SecondaryContainer
                                else Color.White.copy(alpha = 0.12f)
                            )
                            .clickable { showAdjustDialog = true }
                            .padding(vertical = 10.dp, horizontal = 2.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = name,
                            tint = if (isNext) MaterialTheme.colorScheme.onSecondaryContainer else SecondaryFixed,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = name,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isNext) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 11.sp
                            ),
                            color = if (isNext) MaterialTheme.colorScheme.onSecondaryContainer else Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = toArabicNumerals(time),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            ),
                            color = if (isNext) MaterialTheme.colorScheme.onSecondaryContainer else Color.White
                        )
                    }
                }
            }

            // Quick Adhan Settings Pill & Minute Adjustment Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Minute adjustment button
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White.copy(alpha = 0.12f))
                        .clickable { showAdjustDialog = true }
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = null,
                        tint = GoldLight,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "تعديل المواقيت (دقيقة بدقيقة)",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }

                // Adhan preferences button
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White.copy(alpha = 0.12f))
                        .clickable(onClick = onOpenAdhanSettings)
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = null,
                        tint = SecondaryFixed,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "أصوات الأذان ↢",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = SecondaryFixed
                    )
                }
            }
        }
    }

    // Interactive Fine-Tuning Dialog for Prayer Times (+/- Minutes)
    if (showAdjustDialog) {
        AlertDialog(
            onDismissRequest = { showAdjustDialog = false },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Tune, contentDescription = null, tint = Primary)
                    Text("تعديل أوقات الصلاة دقيقة بدقيقة", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "يمكنك تقديم أو تأخير وقت كل صلاة لمطابقة التوقيت المحلي لمدينتك بدقة تامة:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    val adjustList = listOf(
                        Triple("الفجر", fajrOffset) { delta: Int -> fajrOffset += delta },
                        Triple("الشروق", sunriseOffset) { delta: Int -> sunriseOffset += delta },
                        Triple("الظهر", dhuhrOffset) { delta: Int -> dhuhrOffset += delta },
                        Triple("العصر", asrOffset) { delta: Int -> asrOffset += delta },
                        Triple("المغرب", maghribOffset) { delta: Int -> maghribOffset += delta },
                        Triple("العشاء", ishaOffset) { delta: Int -> ishaOffset += delta }
                    )

                    adjustList.forEach { (name, offset, update) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = name, fontWeight = FontWeight.Bold, color = Primary)

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                IconButton(
                                    onClick = { update(-1); prayerTimes = recomputePrayerTimes() },
                                    modifier = Modifier.size(30.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Remove, contentDescription = "إنقاص دقيقة", tint = MaterialTheme.colorScheme.error)
                                }

                                Text(
                                    text = if (offset > 0) "+$offset دقيقة" else if (offset < 0) "$offset دقيقة" else "0 دقيقة",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (offset != 0) Secondary else MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                IconButton(
                                    onClick = { update(1); prayerTimes = recomputePrayerTimes() },
                                    modifier = Modifier.size(30.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Add, contentDescription = "زيادة دقيقة", tint = Primary)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showAdjustDialog = false
                    prayerTimes = recomputePrayerTimes()
                    onShowMessage("تم حفظ وتطبيق تعديل أوقات الصلاة بنجاح")
                }) {
                    Text("تم وحفظ", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    fajrOffset = 0
                    sunriseOffset = 0
                    dhuhrOffset = 0
                    asrOffset = 0
                    maghribOffset = 0
                    ishaOffset = 0
                    prayerTimes = recomputePrayerTimes()
                    onShowMessage("تمت استعادة المواقيت الفلكية الأصلية")
                }) {
                    Text("استعادة الافتراضي")
                }
            }
        )
    }
}
