package com.coding.montaser.covid_19composeapp.ui


import kotlin.math.sin
import kotlin.random.Random



class CovidRepository {



    private val countryProfiles = mapOf(
        "Sudan" to CountryProfile(vaxMultiplier = 0.4, deathMultiplier = 1.2),
        "Egypt" to CountryProfile(vaxMultiplier = 0.85, deathMultiplier = 0.9),
        "Ethiopia" to CountryProfile(vaxMultiplier = 0.55, deathMultiplier = 1.0)
    )

    private val countries = countryProfiles.keys.toList()

    private var currentDayIndex = 0
    private val initialDays = 120
    private val baseDate = java.time.LocalDate.of(2021, 1, 1)

    // Variant waves (startDay, endDay, caseMultiplier)
    private val variants = listOf(
        Variant("Alpha", start = 20, end = 50, multiplier = 1.1),
        Variant("Delta", start = 60, end = 95, multiplier = 1.6),
        Variant("Omicron", start = 100, end = 140, multiplier = 2.2)
    )

    data class Variant(val name: String, val start: Int, val end: Int, val multiplier: Double)
    data class CountryProfile(val vaxMultiplier: Double, val deathMultiplier: Double)

    // Generate initial historical data (initialDays)
    suspend fun generateInitialData(): List<CovidRecord> {
        val data = mutableListOf<CovidRecord>()

        repeat(initialDays) { day ->
            val date = baseDate.plusDays(day.toLong()).toString()
            countries.forEachIndexed { index, country ->
                val profile = countryProfiles[country]!!

                val baseCases = 200 + index * 300
                val baseDeaths = 3 + index * 4
                val baseVacc = 500 + index * 700

                val variantMul = getVariantMultiplier(day)
                val waveFactor = 1 + sin(day / 15.0) * 0.6
                val randomFactor = Random.nextDouble(0.85, 1.15)

                val cases = (baseCases * waveFactor * variantMul * randomFactor).coerceAtLeast(0.0)
                val deaths = (baseDeaths * waveFactor * variantMul * profile.deathMultiplier * Random.nextDouble(0.8, 1.2)).coerceAtLeast(0.0)
                val vacc = (baseVacc * (0.2 + day / initialDays.toDouble()) * profile.vaxMultiplier * Random.nextDouble(0.9, 1.1)).coerceAtLeast(0.0)

                data += CovidRecord(date = date, location = country, newCases = cases, newDeaths = deaths, newVaccinations = vacc)
            }
        }

        currentDayIndex = initialDays
        return data
    }

    // Generate next day data (incremental)
    fun generateNextDay(): List<CovidRecord> {
        val day = currentDayIndex
        val date = baseDate.plusDays(day.toLong()).toString()
        val newEntries = mutableListOf<CovidRecord>()

        countries.forEachIndexed { index, country ->
            val profile = countryProfiles[country]!!

            val baseCases = 200 + index * 300
            val baseDeaths = 3 + index * 4
            val baseVacc = 500 + index * 700

            val variantMul = getVariantMultiplier(day)
            val waveFactor = 1 + sin(day / 15.0) * 0.6
            val randomFactor = Random.nextDouble(0.85, 1.15)

            val cases = (baseCases * waveFactor * variantMul * randomFactor).coerceAtLeast(0.0)
            val deaths = (baseDeaths * waveFactor * variantMul * profile.deathMultiplier * Random.nextDouble(0.8, 1.2)).coerceAtLeast(0.0)
            val vacc = (baseVacc * (0.2 + day / initialDays.toDouble()) * profile.vaxMultiplier * Random.nextDouble(0.9, 1.1)).coerceAtLeast(0.0)

            newEntries += CovidRecord(date = date, location = country, newCases = cases, newDeaths = deaths, newVaccinations = vacc)
        }

        currentDayIndex += 1
        return newEntries
    }

    private fun getVariantMultiplier(day: Int): Double {
        return variants.firstOrNull { day in it.start..it.end }?.multiplier ?: 1.0
    }

}




