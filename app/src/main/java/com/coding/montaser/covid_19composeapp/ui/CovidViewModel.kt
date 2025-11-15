package com.coding.montaser.covid_19composeapp.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class CovidViewModel : ViewModel() {
    var items by mutableStateOf(listOf<CovidRecord>())
    var loading by mutableStateOf(true)
    var liveUpdatesEnabled by mutableStateOf(true)
    var updateIntervalMillis by mutableStateOf(1000L)

    private val repo = CovidRepository()

    init {
        viewModelScope.launch {
            loading = true
            // initial historical data
            items = repo.generateInitialData()
            loading = false

            // start incremental live updates
            while (true) {
                if (liveUpdatesEnabled) {
                    val nextDay = repo.generateNextDay()
                    // append new day's records
                    items = items + nextDay
                }
                delay(updateIntervalMillis)
            }
        }
    }
}