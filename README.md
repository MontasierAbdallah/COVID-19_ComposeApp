# 📱 COVID-19 Jetpack Compose Demo App

A fully offline, fake-data–powered COVID‑19 dashboard built with **Jetpack Compose**, featuring:

✔ Live incremental data updates (every second)
✔ Animated charts (line, multi-line, bar)
✔ Tooltip on touch
✔ Variant waves simulation (Alpha, Delta, Omicron)
✔ Country‑specific profiles (high vax, low vax, high deaths)
✔ No network required — OWID‑style fake generator

---

## 🚀 Features

### 🧪 Realistic Fake COVID‑19 Data

* Generated from an offline repository (`CovidRepository`)
* Uses:

  * Sinusoidal waves
  * Country profiles
  * Variant multipliers
  * Random jitter for realism

### 🔄 Live Incremental Updates

* New “day” added every second
* Old data preserved (growing timeline)
* Perfect for dashboards and demos

### 📊 Animated Charts

* **Line chart** with point selection + tooltip
* **Multi-line chart** for country comparison
* **Bar chart** for deaths and vaccinations
* Uses `animateFloatAsState()` for real-time transitions

---

## 📁 Project Structure

```
app/
 └── src/
      └── main/
           ├── java/com/example/covidapp/
           │        ├── MainActivity.kt
           │        ├── CovidRecord.kt
           │        ├── CovidRepository.kt
           │        ├── CovidViewModel.kt
           │        ├── Charts.kt
           │        └── DashboardScreen.kt
           └── res/
                ├── values/
                │      ├── themes.xml
                │      └── colors.xml
                └── values/strings.xml
```

---

## 📦 Dependencies

Add inside `build.gradle(app)`:

```gradle
// Jetpack Compose
implementation platform('androidx.compose:compose-bom:2024.02.00')
implementation 'androidx.compose.ui:ui'
implementation 'androidx.compose.material3:material3'
implementation 'androidx.compose.ui:ui-tooling-preview'
debugImplementation 'androidx.compose.ui:ui-tooling'

// ViewModel + Coroutines
implementation "androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2"
implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3"

// CSV (if needed for real data later)
implementation "com.github.doyaaaaaken:kotlin-csv-jvm:1.9.2"
```

---

## ▶ How to Run

1. Open project in **Android Studio**.
2. Build & Run.
3. The dashboard will:

   * Load 120 days of historical data
   * Start generating new daily records incrementally
   * Animate charts automatically

---

## ⚙ Live Update Controls (optional)

Add this to the dashboard if you want user control:

```kotlin
LiveControls(vm)
```

Provides:

* Play / Pause
* Interval slider (200–5000 ms)

---

## 🧩 Extending the App

Possible enhancements:

* Export chart as PNG
* Add map visualization
* Add per-country settings
* Save generated history to local storage
* Live variant mutation events

---

## 📄 License

MIT — free to use, modify, and integrate.
