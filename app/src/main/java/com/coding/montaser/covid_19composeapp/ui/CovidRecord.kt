package com.coding.montaser.covid_19composeapp.ui

data class CovidRecord(
    val date: String,
    val location: String,
    val newCases: Double?,
    val newDeaths: Double?,
    val newVaccinations: Double?
)
