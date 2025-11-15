package com.coding.montaser.covid_19composeapp.ui


import kotlin.math.sin
import kotlin.random.Random



class CovidRepository {



    private val countries = listOf("Sudan", "Egypt", "Ethiopia")

     fun loadData(): List<CovidRecord> {
        val data = mutableListOf<CovidRecord>()
        val days = 120
        val baseDate = java.time.LocalDate.of(2021, 1, 1)

        countries.forEachIndexed { index, country ->
            val baseCases = 300 + index * 200
            val baseDeaths = 5 + index * 3
            val baseVacc = 1000 + index * 800

            repeat(days) { day ->
                val date = baseDate.plusDays(day.toLong()).toString()

                val waveFactor = 1 + sin(day / 15.0) * 0.6
                val randomFactor = Random.nextDouble(0.85, 1.15)
                val deathFactor = Random.nextDouble(0.8, 1.2)
                val vaccFactor = Random.nextDouble(0.9, 1.1)

                val cases = baseCases * waveFactor * randomFactor
                val deaths = baseDeaths * waveFactor * deathFactor
                val vacc = baseVacc * (0.5 + day / days.toDouble()) * vaccFactor

                data += CovidRecord(
                    date = date,
                    location = country,
                    newCases = cases,
                    newDeaths = deaths,
                    newVaccinations = vacc
                )
            }
        }
        return data
    }

}


