package com.coding.montaser.covid_19composeapp.ui


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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


@Composable
fun LineChartWithTooltip(values: List<Double>) {
    var selectedIndex by remember { mutableStateOf(-1) }
    var selectedPos by remember { mutableStateOf(Offset.Zero) }

    // animated floats for each point
    val animatedValues = values.map { v ->
        animateFloatAsState(targetValue = v.toFloat(), animationSpec = tween(durationMillis = 600)).value
    }

    Box(Modifier.fillMaxWidth()) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .pointerInput(values) {
                    detectTapGestures { tap ->
                        if (animatedValues.isNotEmpty()) {
                            val stepX = size.width / (animatedValues.size - 1)
                            val index = (tap.x / stepX).toInt().coerceIn(0, animatedValues.size - 1)
                            selectedIndex = index
                            selectedPos = Offset((stepX * index).toFloat(), tap.y)
                        }
                    }
                }
        ) {
            if (animatedValues.isEmpty()) return@Canvas

            val maxY = animatedValues.maxOrNull() ?: 1f
            val stepX = size.width / (animatedValues.size - 1)

            val path = Path()
            animatedValues.forEachIndexed { i, v ->
                val x = stepX * i
                val y = size.height - (v / maxY * size.height)
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }

            drawPath(path = path, color = Color.Red, style = Stroke(width = 4f))

            if (selectedIndex >= 0) {
                val x = stepX * selectedIndex
                val v = animatedValues[selectedIndex]
                val y = size.height - (v / (animatedValues.maxOrNull() ?: 1f) * size.height)
                drawCircle(color = Color.Blue, radius = 10f, center = Offset(x, y))
            }
        }

        if (selectedIndex >= 0) {
            TooltipBox(values[selectedIndex], selectedPos)
        }
    }
}

@Composable
fun TooltipBox(value: Double, pos: Offset) {
    Box(
        modifier = Modifier
            .offset(x = pos.x.dp - 40.dp, y = pos.y.dp - 60.dp)
            .background(Color.Black, shape = CircleShape)
            .border(1.dp, Color.White)
            .padding(6.dp)
    ) {
        Text(text = value.toInt().toString(), color = Color.White)
    }
}




// -------------------------
// 3) Bar Chart (animated)
// -------------------------
@Composable
fun BarChart(values: List<Double>, barColor: Color = Color(0xFF4A90E2)) {
    // animated heights
    val animatedHeights = values.map { animateFloatAsState(it.toFloat(), animationSpec = tween(600)).value }

    Canvas(modifier = Modifier.fillMaxWidth().height(250.dp)) {
        if (animatedHeights.isEmpty()) return@Canvas

        val maxY = animatedHeights.maxOrNull() ?: 1f
        val barWidth = size.width / animatedHeights.size

        animatedHeights.forEachIndexed { i, v ->
            val x = i * barWidth
            val h = (v / maxY) * size.height
            drawRect(color = barColor, topLeft = Offset(x + barWidth * 0.1f, size.height - h), size = androidx.compose.ui.geometry.Size(barWidth * 0.8f, h))
        }
    }
}