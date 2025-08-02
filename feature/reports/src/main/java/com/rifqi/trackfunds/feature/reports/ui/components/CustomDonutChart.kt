package com.rifqi.trackfunds.feature.reports.ui.components

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rifqi.trackfunds.core.domain.common.model.CategorySpending
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.utils.formatCurrency
import java.math.BigDecimal
import kotlin.math.cos
import kotlin.math.sin

private val chartColors = listOf(
    Color(0xFF358AC8), Color(0xFF59C4B0), Color(0xFFF56666),
    Color(0xFF8378D1), Color(0xFFFFCA28), Color(0xFF4CAF50),
    Color(0xFF9E9E9E)
)

@Composable
fun CustomDonutChart(
    data: List<CategorySpending>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) {
        Box(
            modifier
                .fillMaxWidth()
                .padding(32.dp), contentAlignment = Alignment.Center
        ) {
            Text("No expense data for this period.")
        }
        return
    }

    val totalExpense = remember(data) { data.sumOf { it.totalAmount } }
    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Box untuk Chart Donat
        Box(
            modifier = Modifier
                .padding(8.dp)
                .size(200.dp),
            contentAlignment = Alignment.Center
        ) {
            // Teks di tengah Donat
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Total", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = formatCurrency(totalExpense),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            // Canvas untuk menggambar Donat
            DonutChartCanvas(
                data = data,
                totalValue = totalExpense,
                selectedIndex = selectedIndex
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))

        ChartLegend(
            data = data,
            totalValue = totalExpense,
            onItemClick = { index ->
                selectedIndex = if (selectedIndex == index) null else index
            }
        )
    }
}


@Composable
private fun DonutChartCanvas(
    data: List<CategorySpending>,
    totalValue: BigDecimal,
    selectedIndex: Int?
) {
    val textMeasurer = rememberTextMeasurer()
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(data) {
        animatedProgress.animateTo(1f, animationSpec = tween(durationMillis = 1000))
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val strokeWidth = 40f
        var startAngle = -90f

        data.forEachIndexed { index, item ->
            if (totalValue > BigDecimal.ZERO) {
                val proportion = item.totalAmount.toFloat() / totalValue.toFloat()
                val sweepAngle = proportion * 360f * animatedProgress.value
                val alpha = if (selectedIndex == null || selectedIndex == index) 1f else 0.3f
                val color = chartColors[index % chartColors.size].copy(alpha = alpha)

                // Gambar irisan donat
                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                )

                // Tampilkan hanya untuk irisan yang cukup besar
                if (sweepAngle > 10) {
                    // Tentukan sudut tengah dari irisan untuk menempatkan garis dan teks
                    val angleInRadians = Math.toRadians(startAngle + sweepAngle / 2.0)

                    // Titik awal garis (di tengah-tengah tebalnya irisan donat)
                    val lineStartRadius = (size.width / 2) - (strokeWidth / 2)
                    val lineStartX = center.x + (lineStartRadius * cos(angleInRadians)).toFloat()
                    val lineStartY = center.y + (lineStartRadius * sin(angleInRadians)).toFloat()

                    // Titik tengah garis (belokan)
                    val lineMiddleRadius = (size.width / 2) + 20.dp.toPx()
                    val lineMiddleX = center.x + (lineMiddleRadius * cos(angleInRadians)).toFloat()
                    val lineMiddleY = center.y + (lineMiddleRadius * sin(angleInRadians)).toFloat()

                    // Titik akhir garis (horizontal)
                    val lineEndX =
                        if (cos(angleInRadians) > 0) lineMiddleX + 10.dp.toPx() else lineMiddleX - 10.dp.toPx()

                    // Gambar garis penunjuk
                    drawLine(
                        color = color,
                        start = Offset(lineStartX, lineStartY),
                        end = Offset(lineMiddleX, lineMiddleY),
                        strokeWidth = 1.5.dp.toPx()
                    )
                    drawLine(
                        color = color,
                        start = Offset(lineMiddleX, lineMiddleY),
                        end = Offset(lineEndX, lineMiddleY),
                        strokeWidth = 1.5.dp.toPx()
                    )

                    // Siapkan dan gambar teks persentase
                    val percentage = (proportion * 100)
                    val labelText = "${"%.0f".format(percentage)}%"
                    val textLayoutResult = textMeasurer.measure(
                        text = AnnotatedString(labelText),
                        style = TextStyle(
                            color = color,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    val angleDegrees = Math.toDegrees(angleInRadians)

                    val textX = when {
                        angleDegrees in -90.0..90.0 -> {
                            lineEndX + 2.dp.toPx()
                        }

                        else -> { // Kuadran kiri
                            lineEndX - textLayoutResult.size.width - 2.dp.toPx()
                        }
                    }

                    val textY = lineMiddleY - (textLayoutResult.size.height / 2)

                    drawText(
                        textLayoutResult = textLayoutResult,
                        topLeft = Offset(x = textX, y = textY)
                    )
                }

                startAngle += sweepAngle
            }
        }
    }
}

@Composable
private fun ChartLegend(
    data: List<CategorySpending>,
    totalValue: BigDecimal,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        itemsIndexed(data) { index, item ->

            // Setiap item legenda sekarang adalah satu unit yang bisa diklik
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onItemClick(index) }
            ) {
                // Kotak warna (dot)
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            chartColors[index % chartColors.size],
                            shape = CircleShape
                        )
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Teks nama kategori
                Text(item.categoryName, style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.width(4.dp))

                // Teks persentase
                if (totalValue > BigDecimal.ZERO) {
                    val percentage = (item.totalAmount.toFloat() / totalValue.toFloat()) * 100
                    Text(
                        text = "(${"%.0f".format(percentage)}%)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

}

// --- DUMMY DATA UNTUK PREVIEW ---
private val previewSpendingData = listOf(
    CategorySpending("Makan & Minum", BigDecimal("1250000")),
    CategorySpending("Transportasi", BigDecimal("600000")),
    CategorySpending("Belanja", BigDecimal("850000")),
    CategorySpending("Tagihan", BigDecimal("450000"))
)

// --- FUNGSI PREVIEW ---

@Preview(name = "Donut Chart - Light Mode", showBackground = true)
@Preview(
    name = "Donut Chart - Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun CustomDonutChartPreview() {
    TrackFundsTheme {
        CustomDonutChart(data = previewSpendingData)
    }
}

@Preview(name = "Donut Chart - No Data", showBackground = true)
@Composable
private fun CustomDonutChartEmptyPreview() {
    TrackFundsTheme {
        // Berikan list kosong untuk melihat tampilan "No data"
        CustomDonutChart(data = emptyList())
    }
}