package com.coding.montaser.covid_19composeapp.ui


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp


// ------------------------------------------------------------
// 1) TOUCH TOOLTIP LINE CHART
// ------------------------------------------------------------
@Composable
fun LineChartWithTooltip(values: List<Double>) {
    var selectedIndex by remember { mutableStateOf(-1) }
    var selectedPos by remember { mutableStateOf(Offset.Zero) }

    Box(Modifier.fillMaxWidth()) {

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .pointerInput(Unit) {
                    detectTapGestures { tap ->
                        if (values.isNotEmpty()) {
                            val stepX = size.width / (values.size - 1)
                            val index = (tap.x / stepX)
                                .toInt()
                                .coerceIn(0, values.size - 1)

                            selectedIndex = index

                            selectedPos = Offset(
                                x = (stepX * index).toFloat(),
                                y = tap.y
                            )
                        }
                    }
                }
        ) {
            if (values.isEmpty()) return@Canvas

            val maxY = values.maxOrNull()!!.toFloat()
            val minY = 0f
            val stepX = size.width / (values.size - 1)

            // Draw main line
            val path = Path()

            values.forEachIndexed { i, v ->
                val x = stepX * i
                val y: Float = (size.height - ((v - minY) / (maxY - minY) * size.height)).toFloat()

                if (i == 0) path.moveTo(x, y)
                else path.lineTo(x, y)
            }

            drawPath(path, color = Color.Red, style = Stroke(5f))

            // Selected point circle
            if (selectedIndex >= 0) {
                val x = stepX * selectedIndex
                val v = values[selectedIndex].toFloat()
                val y = size.height - ((v - minY) / (maxY - minY) * size.height)

                drawCircle(
                    color = Color.Blue,
                    radius = 12f,
                    center = Offset(x, y)
                )
            }
        }

        if (selectedIndex >= 0) {
            TooltipBox(
                value = values[selectedIndex],
                position = selectedPos
            )
        }
    }
}

@Composable
fun TooltipBox(value: Double, position: Offset) {
    Box(
        modifier = Modifier
            .offset(
                x = position.x.dp - 40.dp,
                y = position.y.dp - 60.dp
            )
            .background(Color.Black, MaterialTheme.shapes.small)
            .border(1.dp, Color.White)
            .padding(6.dp)
    ) {
        Text(
            text = value.toInt().toString(),
            color = Color.White,
            style = MaterialTheme.typography.bodySmall
        )
    }
}


// ------------------------------------------------------------
// 2) MULTI-LINE COMPARISON CHART
// ------------------------------------------------------------
@Composable
fun MultiLineChart(datasets: Map<String, List<Double>>) {
    val colors = listOf(Color.Red, Color.Green, Color.Blue, Color.Magenta, Color.Cyan)

    Column {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            if (datasets.isEmpty()) return@Canvas

            val maxPoints = datasets.values.maxOf { it.size }
            val maxY = datasets.values.flatten().maxOrNull()!!.toFloat()
            val stepX = size.width / (maxPoints - 1)

            datasets.entries.forEachIndexed { index, entry ->
                val (country, values) = entry
                val color = colors[index % colors.size]
                val path = Path()

                values.forEachIndexed { i, v ->
                    val x = stepX * i
                    val y = size.height - (v.toFloat() / maxY * size.height)
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }

                drawPath(path, color, style = Stroke(4f))
            }
        }

        Spacer(Modifier.height(6.dp))
        Column {
            datasets.keys.forEachIndexed { i, country ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(12.dp).background(colors[i % colors.size]))
                    Spacer(Modifier.width(8.dp))
                    Text(country)
                }
            }
        }
    }
}

// ------------------------------------------------------------
// 3) BAR CHART (Deaths / Vaccinations)
// ------------------------------------------------------------
@Composable
fun BarChart(values: List<Double>, barColor: Color = Color(0xFF4A90E2)) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        if (values.isEmpty()) return@Canvas

        val maxY = values.maxOrNull()!!.toFloat()
        val barWidth = size.width / values.size

        values.forEachIndexed { i, v ->
            val x = i * barWidth
            val h = (v.toFloat() / maxY) * size.height

            drawRect(
                color = barColor,
                topLeft = Offset(x + barWidth * 0.1f, size.height - h),
                size = Size(barWidth * 0.8f, h)
            )
        }
    }
}