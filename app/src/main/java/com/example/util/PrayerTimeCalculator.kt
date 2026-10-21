package com.example.util

import java.util.Calendar
import java.util.Date
import java.util.TimeZone
import kotlin.math.*

/**
 * Astronomical and geographic calculation of Islamic Prayer Times based on location (latitude, longitude)
 * Supports multiple calculation methods (Umm Al-Qura, Muslim World League, Egyptian General Authority, etc.)
 */
object PrayerTimeCalculator {

    enum class CalculationMethod(
        val arabicTitle: String,
        val fajrAngle: Double,
        val ishaAngle: Double,
        val ishaIntervalMinutes: Int? = null
    ) {
        UMM_AL_QURA("أم القرى (مكة المكرمة)", 18.5, 0.0, 90),
        MUSLIM_WORLD_LEAGUE("رابطة العالم الإسلامي", 18.0, 17.0),
        EGYPTIAN("الهيئة المصرية العامة للمساحة", 19.5, 17.5),
        DAMASCUS_SHAM("ديوان الأوقاف بدمشق وبلاد الشام", 18.5, 17.5),
        KARACHI("جامعة العلوم الإسلامية بكراتشي", 18.0, 18.0),
        NORTH_AMERICA("أمريكا الشمالية (ISNA)", 15.0, 15.0),
        DUBAI("دائرة الشؤون الإسلامية بدبي", 18.2, 18.2)
    }

    data class AccurateDateInfo(
        val dayOfWeekArabic: String,
        val gregorianFormatted: String,
        val hijriFormatted: String,
        val hijriDay: Int,
        val hijriMonthName: String,
        val hijriYear: Int
    )

    data class DailyPrayerTimes(
        val fajr: String,
        val sunrise: String,
        val dhuhr: String,
        val asr: String,
        val maghrib: String,
        val isha: String,
        val nextPrayerName: String,
        val remainingSecondsToNext: Long
    )

    fun calculate(
        latitude: Double,
        longitude: Double,
        date: Date = Date(),
        timeZoneOffsetHours: Double = TimeZone.getDefault().rawOffset / 3600000.0,
        method: CalculationMethod = CalculationMethod.UMM_AL_QURA,
        fajrOffsetMin: Int = 0,
        sunriseOffsetMin: Int = 0,
        dhuhrOffsetMin: Int = 0,
        asrOffsetMin: Int = 0,
        maghribOffsetMin: Int = 0,
        ishaOffsetMin: Int = 0
    ): DailyPrayerTimes {
        val cal = Calendar.getInstance()
        cal.time = date
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        val day = cal.get(Calendar.DAY_OF_MONTH)

        // Julian date
        val julianDate = julianDate(year, month, day) - longitude / (15.0 * 24.0)

        // Sun position
        val d = julianDate - 2451545.0
        val g = fixAngle(357.529 + 0.98560028 * d)
        val q = fixAngle(280.459 + 0.98564736 * d)
        val l = fixAngle(q + 1.915 * sin(toRadians(g)) + 0.020 * sin(toRadians(2 * g)))
        val e = 23.439 - 0.00000036 * d
        val ra = fixAngle(toDegrees(atan2(cos(toRadians(e)) * sin(toRadians(l)), cos(toRadians(l))))) / 15.0
        val declination = toDegrees(asin(sin(toRadians(e)) * sin(toRadians(l))))
        val eqT = q / 15.0 - ra

        // Midday (Dhuhr)
        val midday = fixHour(12.0 + timeZoneOffsetHours - longitude / 15.0 - eqT)

        // Sun altitude helper
        fun sunAngleTime(angle: Double, direction: Boolean): Double {
            val sinVal = (-sin(toRadians(angle)) - sin(toRadians(declination)) * sin(toRadians(latitude))) /
                    (cos(toRadians(declination)) * cos(toRadians(latitude)))
            if (sinVal < -1.0 || sinVal > 1.0) return midday
            val hourAngle = toDegrees(acos(sinVal)) / 15.0
            return if (direction) midday - hourAngle else midday + hourAngle
        }

        // Asr angle (Shafi'i = 1 shadow length)
        fun asrTime(factor: Double): Double {
            val angle = -toDegrees(atan(1.0 / (factor + tan(toRadians(abs(latitude - declination))))))
            val sinVal = (-sin(toRadians(angle)) - sin(toRadians(declination)) * sin(toRadians(latitude))) /
                    (cos(toRadians(declination)) * cos(toRadians(latitude)))
            val clamped = sinVal.coerceIn(-1.0, 1.0)
            val hourAngle = toDegrees(acos(clamped)) / 15.0
            return midday + hourAngle
        }

        val baseFajr = sunAngleTime(method.fajrAngle, true)
        val baseSunrise = sunAngleTime(0.833, true)
        val baseDhuhr = midday
        val baseAsr = asrTime(1.0)
        val baseMaghrib = sunAngleTime(0.833, false)
        val baseIsha = if (method.ishaIntervalMinutes != null) {
            baseMaghrib + method.ishaIntervalMinutes / 60.0
        } else {
            sunAngleTime(method.ishaAngle, false)
        }

        // Apply manual fine-tuning offsets in minutes
        val fajrTime = baseFajr + fajrOffsetMin / 60.0
        val sunriseTime = baseSunrise + sunriseOffsetMin / 60.0
        val dhuhrTime = baseDhuhr + dhuhrOffsetMin / 60.0
        val asrTime = baseAsr + asrOffsetMin / 60.0
        val maghribTime = baseMaghrib + maghribOffsetMin / 60.0
        val ishaTime = baseIsha + ishaOffsetMin / 60.0

        val currentHour = cal.get(Calendar.HOUR_OF_DAY) + cal.get(Calendar.MINUTE) / 60.0 + cal.get(Calendar.SECOND) / 3600.0

        val timesList = listOf(
            "الفجر" to fajrTime,
            "الشروق" to sunriseTime,
            "الظهر" to dhuhrTime,
            "العصر" to asrTime,
            "المغرب" to maghribTime,
            "العشاء" to ishaTime
        )

        var nextName = "الفجر"
        var nextTimeHours = fajrTime + 24.0
        for ((name, t) in timesList) {
            if (t > currentHour) {
                nextName = name
                nextTimeHours = t
                break
            }
        }

        val remainingSec = ((nextTimeHours - currentHour) * 3600.0).toLong().coerceAtLeast(0L)

        return DailyPrayerTimes(
            fajr = formatTime(fajrTime),
            sunrise = formatTime(sunriseTime),
            dhuhr = formatTime(dhuhrTime),
            asr = formatTime(asrTime),
            maghrib = formatTime(maghribTime),
            isha = formatTime(ishaTime),
            nextPrayerName = nextName,
            remainingSecondsToNext = remainingSec
        )
    }

    /**
     * Compute exact Gregorian and Hijri calendar dates accurately in Arabic
     */
    fun getAccurateDateInfo(calendar: Calendar = Calendar.getInstance(), hijriOffsetDays: Int = 2): AccurateDateInfo {
        val daysArabic = listOf("الأحد", "الاثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة", "السبت")
        val dayOfWeekIndex = calendar.get(Calendar.DAY_OF_WEEK) - 1
        val dayOfWeekArabic = daysArabic.getOrElse(dayOfWeekIndex) { "الجمعة" }

        val gYear = calendar.get(Calendar.YEAR)
        val gMonth = calendar.get(Calendar.MONTH) + 1
        val gDay = calendar.get(Calendar.DAY_OF_MONTH)

        val gMonthsArabic = listOf(
            "", "يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو",
            "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر"
        )
        val gMonthName = gMonthsArabic.getOrElse(gMonth) { "" }
        val gregorianFormatted = "$dayOfWeekArabic، $gDay $gMonthName $gYear م"

        // Julian Day Number Calculation
        var y = gYear
        var m = gMonth
        if (m < 3) {
            y -= 1
            m += 12
        }
        val a = floor(y / 100.0)
        val b = 2 - a + floor(a / 4.0)
        val jd = floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + gDay + b - 1524.5
        val adjustedJd = floor(jd) + hijriOffsetDays

        // Umm Al-Qura / Julian to Hijri algorithm
        val l = adjustedJd - 1948440 + 10632
        val n = floor((l - 1) / 10631.0)
        val l2 = l - 10631 * n + 354
        val j = (floor((10985 - l2) / 5316.0)) * (floor((50 * l2) / 17719.0)) + (floor(l2 / 5670.0)) * (floor((43 * l2) / 15238.0))
        val l3 = l2 - (floor((30 - j) / 15.0)) * (floor((17719 * j) / 50.0)) - (floor(j / 16.0)) * (floor((15238 * j) / 43.0)) + 29
        val hMonth = floor((24 * l3) / 709.0).toInt().coerceIn(1, 12)
        val hDay = (l3 - floor((709 * hMonth) / 24.0)).toInt().coerceIn(1, 30)
        val hYear = (30 * n + j - 30).toInt()

        val hijriMonthsArabic = listOf(
            "", "محرم", "صفر", "ربيع الأول", "ربيع الثاني", "جمادى الأولى",
            "جمادى الآخرة", "رجب", "شعبان", "رمضان", "شوال", "ذو القعدة", "ذو الحجة"
        )
        val hijriMonthName = hijriMonthsArabic.getOrElse(hMonth) { "ربيع الثاني" }
        val hijriFormatted = "$hDay $hijriMonthName $hYear هـ"

        return AccurateDateInfo(
            dayOfWeekArabic = dayOfWeekArabic,
            gregorianFormatted = gregorianFormatted,
            hijriFormatted = hijriFormatted,
            hijriDay = hDay,
            hijriMonthName = hijriMonthName,
            hijriYear = hYear
        )
    }

    private fun formatTime(hours: Double): String {
        val fixed = fixHour(hours)
        val h = fixed.toInt()
        val m = ((fixed - h) * 60.0).roundToInt()
        val adjustedH = if (m == 60) (h + 1) % 24 else h
        val adjustedM = if (m == 60) 0 else m
        return String.format("%02d:%02d", adjustedH, adjustedM)
    }

    private fun julianDate(year: Int, month: Int, day: Int): Double {
        var y = year
        var m = month
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = floor(y / 100.0)
        val b = 2 - a + floor(a / 4.0)
        return floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day + b - 1524.5
    }

    private fun fixAngle(a: Double): Double {
        var res = a - 360.0 * floor(a / 360.0)
        if (res < 0) res += 360.0
        return res
    }

    private fun fixHour(h: Double): Double {
        var res = h - 24.0 * floor(h / 24.0)
        if (res < 0) res += 24.0
        return res
    }

    private fun toRadians(deg: Double) = deg * Math.PI / 180.0
    private fun toDegrees(rad: Double) = rad * 180.0 / Math.PI
}
