package com.rifqi.trackfunds.feature.reports.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.utils.formatCurrency
import kotlinx.coroutines.launch

data class CashflowData(
    val label: String,
    val value: Float,
    val color: Color
)

@Composable
fun CashflowBarChart(
    data: List<CashflowData>,
    modifier: Modifier = Modifier,
) {
    val textMeasurer = rememberTextMeasurer()

    val animationProgress = remember { data.map { Animatable(0f) } }

    // Konversi Dp ke Px dilakukan di sini
    val yAxisLabelWidthPx = with(LocalDensity.current) { 50.dp.toPx() }
    val xAxisLabelHeightPx = with(LocalDensity.current) { 30.dp.toPx() }
    val barLabelSpacingPx = with(LocalDensity.current) { 8.dp.toPx() }
    val valueLabelSpacingPx = with(LocalDensity.current) { 4.dp.toPx() }

    LaunchedEffect(data) {
        animationProgress.forEachIndexed { index, animatable ->
            launch {
                animatable.animateTo(
                    targetValue = data[index].value,
                    animationSpec = tween(durationMillis = 1000)
                )
            }
        }
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        // --- Konstanta & Perhitungan ---
        val chartWidth = size.width - yAxisLabelWidthPx
        val chartHeight = size.height - xAxisLabelHeightPx
        val maxDataValue = data.maxOfOrNull { it.value } ?: 0f
        val maxValue = maxDataValue * 1.25f // Ruang ekstra 25% di atas

        // Gambar Sumbu Y
        drawYAxis(textMeasurer, maxValue, chartHeight, yAxisLabelWidthPx)

        // --- Gambar Bar dan Labelnya ---
        val slotWidth = chartWidth / data.size
        val barWidth = slotWidth / 2

        data.forEachIndexed { index, chartData ->
            val barHeight = (animationProgress[index].value / maxValue) * chartHeight
            val barTopLeftX = yAxisLabelWidthPx + (slotWidth * index) + (slotWidth - barWidth) / 2
            val barTopY = chartHeight - barHeight

            // Gambar Bar
            drawRect(
                color = chartData.color,
                topLeft = Offset(x = barTopLeftX, y = barTopY),
                size = Size(width = barWidth, height = barHeight)
            )

            // --- FIX: TAMBAHKAN BLOK INI UNTUK MENGGAMBAR TEKS DI ATAS BAR ---
            val valueText = formatCurrency(chartData.value.toBigDecimal())
            val valueTextLayoutResult = textMeasurer.measure(AnnotatedString(valueText))
            val valueTextTopLeftX =
                barTopLeftX + (barWidth / 2) - (valueTextLayoutResult.size.width / 2)

            drawText(
                textMeasurer = textMeasurer,
                text = valueText,
                topLeft = Offset(
                    x = valueTextTopLeftX,
                    y = barTopY - valueTextLayoutResult.size.height - valueLabelSpacingPx // Posisi di atas bar
                ),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = chartData.color
                )
            )

            // Gambar label sumbu X (tidak berubah)
            val labelText = chartData.label
            val labelSize = textMeasurer.measure(AnnotatedString(labelText))
            val textTopLeftX = barTopLeftX + (barWidth / 2) - (labelSize.size.width / 2)
            drawText(
                textMeasurer = textMeasurer,
                text = labelText,
                topLeft = Offset(x = textTopLeftX, y = chartHeight + barLabelSpacingPx),
                style = TextStyle(fontSize = 12.sp, color = Color.Gray)
            )
        }
    }
}

private fun DrawScope.drawYAxis(
    textMeasurer: TextMeasurer,
    maxValue: Float,
    chartHeight: Float,
    labelWidth: Float
) {
    val numLabels = 5
    for (i in 0..numLabels) {
        val value = maxValue * (i.toFloat() / numLabels)
        val y = chartHeight * (1 - (i.toFloat() / numLabels))

        if (i != 0) { // Jangan gambar garis untuk nilai 0
            drawLine(
                color = Color.LightGray.copy(alpha = 0.5f),
                start = Offset(x = labelWidth, y = y),
                end = Offset(x = size.width, y = y),
                strokeWidth = 1f
            )
        }

        // FIX: Logika format label yang lebih cerdas
        val labelText = when {
            value >= 1_000_000 -> "${"%.1f".format(value / 1_000_000f).replace(".0", "")} jt"
            value >= 1_000 -> "${(value / 1_000f).toInt()} rb"
            else -> value.toInt().toString()
        }

        drawText(
            textMeasurer = textMeasurer,
            text = labelText,
            topLeft = Offset(x = 0f, y = y - (textMeasurer.measure(labelText).size.height / 2)),
            style = TextStyle(fontSize = 12.sp, color = Color.Gray)
        )
    }
}

// --- DUMMY DATA UNTUK PREVIEW ---
private val previewCashflowData = listOf(
    CashflowData("Income", 9000000f, Color(0xFF4CAF50)),
    CashflowData("Expenses", 2300000f, Color(0xFFF44336)),
)

@Preview(showBackground = true, name = "Custom Bar Chart Preview")
@Composable
private fun CustomBarChartPreview() {
    TrackFundsTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            CashflowBarChart(data = previewCashflowData)
        }
    }
}
