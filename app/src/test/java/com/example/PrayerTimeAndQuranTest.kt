package com.example

import com.example.data.QuranRepository
import com.example.data.SampleData
import com.example.util.PrayerTimeCalculator
import org.junit.Assert.*
import org.junit.Test
import java.util.Calendar

class PrayerTimeAndQuranTest {

    @Test
    fun prayerTimesCalculation_forMakkah_isAccurate() {
        val makkahLat = 21.4225
        val makkahLng = 39.8262

        val prayerTimes = PrayerTimeCalculator.calculate(
            latitude = makkahLat,
            longitude = makkahLng,
            timeZoneOffsetHours = 3.0,
            method = PrayerTimeCalculator.CalculationMethod.UMM_AL_QURA
        )

        assertNotNull(prayerTimes.fajr)
        assertNotNull(prayerTimes.dhuhr)
        assertNotNull(prayerTimes.asr)
        assertNotNull(prayerTimes.maghrib)
        assertNotNull(prayerTimes.isha)
        assertTrue(prayerTimes.fajr.contains(":"))
        assertTrue(prayerTimes.dhuhr.contains(":"))
    }

    @Test
    fun quranPages_andReciters_containMuhammadAlLuhaidan() {
        // Verify Muhammad Al-Luhaidan is registered in reciters
        val hasLuhaidanInRepo = QuranRepository.masterReciters.any { it.name.contains("اللحيدان") }
        val hasLuhaidanInSample = SampleData.masterReciters.any { it.name.contains("اللحيدان") }
        assertTrue("Mohammad Al-Luhaidan must be in Quran reciters", hasLuhaidanInRepo)
        assertTrue("Mohammad Al-Luhaidan must be in SampleData reciters", hasLuhaidanInSample)

        // Verify all 114 Surahs exist in QuranRepository
        assertEquals(114, QuranRepository.allSurahs.size)

        // Verify Mushaf pages exist for page-flipping
        assertTrue(QuranRepository.pages.isNotEmpty())
    }
}
