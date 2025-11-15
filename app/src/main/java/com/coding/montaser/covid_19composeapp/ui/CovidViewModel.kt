package com.coding.montaser.covid_19composeapp.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlin.random.Random

class CovidViewModel : ViewModel() {
    private val repo = CovidRepository()

    var items by mutableStateOf(listOf<CovidRecord>())
    var loading by mutableStateOf(true)

    init {
        viewModelScope.launch {
            loading = true
            items = repo.loadData()

            loading = false
// live update every second
            while (true) {
                kotlinx.coroutines.delay(1000)
                items = repo.loadData() // generate new fake wave every second
            }
        }
    }
}