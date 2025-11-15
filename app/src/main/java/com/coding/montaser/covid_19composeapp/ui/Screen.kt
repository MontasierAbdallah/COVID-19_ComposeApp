package com.coding.montaser.covid_19composeapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.collections.map

import kotlin.collections.filter

@Composable
fun DashboardScreen(list: List<CovidRecord>) {
    val countries = list.map { it.location }.distinct().sorted()
    var selectedCountry by remember { mutableStateOf("Sudan") }

    val cases = list.filter { it.location == selectedCountry }.map { it.newCases ?: 0.0 }
    val deaths = list.filter { it.location == selectedCountry }.map { it.newDeaths ?: 0.0 }
    val vacc = list.filter { it.location == selectedCountry }.map { it.newVaccinations ?: 0.0 }

    LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text("COVID‑19 Dashboard", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(12.dp))

            // Country selector
            CountryDropdown(countries, selectedCountry) {
                selectedCountry = it
            }

            Spacer(Modifier.height(20.dp))
            Text("Touch Tooltip: New Cases")
            LineChartWithTooltip(cases)

            Spacer(Modifier.height(30.dp))
            Text("Bar Chart: Deaths")
            BarChart(deaths)

            Spacer(Modifier.height(30.dp))
            Text("Bar Chart: Vaccinations")
            BarChart(vacc)
        }
    }
}

// ------------------------------------------------------------
// COUNTRY DROPDOWN
// ------------------------------------------------------------
@Composable
fun CountryDropdown(list: List<String>, selected: String, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Button(onClick = { expanded = true }) {
            Text(selected)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            list.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        expanded = false
                        onSelect(it)
                    }
                )
            }
        }
    }
}