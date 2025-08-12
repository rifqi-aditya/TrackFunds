package com.rifqi.trackfunds.feature.reports.ui.components

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.utils.formatCurrency
import com.rifqi.trackfunds.feature.reports.ui.state.CategorySummaryUiModel
import java.math.BigDecimal
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin


@Composable
fun CustomDonutChart(
    data: List<CategorySummaryUiModel>,
    modifier: Modifier = Modifier,
) {
    if (data.isEmpty()) {
        Box(
            modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("No expense data for this period.")
        }
        return
    }

    // Samakan sumber warna (legend & slice) → gunakan mapping deterministik default-nya
    val entries = data // sudah berwarna dari VM


    val totalAmount = remember(entries) {
        entries.fold(BigDecimal.ZERO) { acc, x -> acc + x.totalAmount }
    }

    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Chart + label tengah
        Box(
            modifier = Modifier
                .padding(8.dp)
                .size(220.dp),
            contentAlignment = Alignment.Center
        ) {
            // label tengah: total atau detail slice terpilih
            val centerTitle =
                if (selectedIndex == null) "Total" else entries[selectedIndex!!].categoryName
            val centerValue =
                if (selectedIndex == null) totalAmount else entries[selectedIndex!!].totalAmount

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(centerTitle, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = formatCurrency(centerValue),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            DonutChartCanvas(
                data = entries,
                totalValue = totalAmount,
                selectedIndex = selectedIndex,
                onSliceClick = { idx -> selectedIndex = if (selectedIndex == idx) null else idx }
            )
        }

        Spacer(Modifier.height(16.dp))

        // Legend
        ChartLegend(
            data = entries,
            selectedIndex = selectedIndex,
            onItemClick = { idx -> selectedIndex = if (selectedIndex == idx) null else idx }
        )
    }
}

/* =========================
   CANVAS & HIT TEST
   ========================= */

@Composable
private fun DonutChartCanvas(
    data: List<CategorySummaryUiModel>,
    totalValue: BigDecimal,
    selectedIndex: Int?,
    onSliceClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val animSweep = remember { Animatable(0f) }

    // animasi masuk
    LaunchedEffect(data) {
        animSweep.snapTo(0f)
        animSweep.animateTo(1f, animationSpec = tween(900))
    }

    val density = LocalDensity.current
    val baseStroke = with(density) { 20.dp.toPx() }
    val selectedStroke = with(density) { 20.dp.toPx() }
    val explodeOffsetPx = with(density) { 8.dp.toPx() }

    // proporsi per-slice
    val proportions = remember(data, totalValue) {
        if (totalValue <= BigDecimal.ZERO) {
            List(data.size) { 0f }
        } else {
            data.map { (it.totalAmount.toDouble() / totalValue.toDouble()).toFloat() }
        }
    }

    // ----- 🔸 Animasi di LUAR Canvas: alpha & highlight per-slice -----
    val targetAlphas = remember(selectedIndex, data) {
        data.indices.map { idx ->
            if (selectedIndex == null || selectedIndex == idx) 1f else 0.35f
        }
    }
    val alphas = targetAlphas.mapIndexed { idx, target ->
        val state by animateFloatAsState(
            targetValue = target,
            animationSpec = tween(200),
            label = "alpha_$idx"
        )
        state
    }

    val targetHighlights = remember(selectedIndex, data) {
        data.indices.map { idx -> if (selectedIndex == idx) 1f else 0f }
    }
    val highlights = targetHighlights.mapIndexed { idx, target ->
        val state by animateFloatAsState(
            targetValue = target,
            animationSpec = tween(200),
            label = "highlight_$idx"
        )
        state
    }
    // ------------------------------------------------------------------

    // precompute angle starts untuk hit-test
    val sweepsFull = remember(proportions) { proportions.map { it * 360f } }
    val cumStarts = remember(sweepsFull) {
        val acc = ArrayList<Float>(sweepsFull.size)
        var s = -90f // mulai jam 12
        for (w in sweepsFull) {
            acc.add(s)
            s += w
        }
        acc
    }

    // pointer input untuk deteksi klik slice
    var canvasSize by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(data, cumStarts, sweepsFull, onSliceClick) {
                detectTapGestures { offset ->
                    if (totalValue <= BigDecimal.ZERO) return@detectTapGestures

                    val center = Offset(canvasSize.width / 2f, canvasSize.height / 2f)
                    val dx = offset.x - center.x
                    val dy = offset.y - center.y
                    val r = hypot(dx, dy)

                    val outerR = min(canvasSize.width, canvasSize.height) / 2f
                    val innerR = outerR - baseStroke

                    // tap harus di ring
                    if (r < innerR || r > outerR) return@detectTapGestures

                    // konversi ke sudut derajat sumbu jam 12
                    var angle = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat() + 90f
                    if (angle < 0f) angle += 360f

                    // cari slice
                    cumStarts.forEachIndexed { idx, start ->
                        val sweep = sweepsFull[idx]
                        val end = start + sweep
                        val normAngle = angle // sudah 0..360
                        if (normAngle in start..end) {
                            onSliceClick(idx)
                            return@detectTapGestures
                        }
                    }
                }
            }
    ) {
        canvasSize = size

        val outerR = min(size.width, size.height) / 2f
        val innerR = outerR - baseStroke
        var startAngle = -90f

        data.forEachIndexed { index, item ->
            if (totalValue > BigDecimal.ZERO) {
                val proportion = proportions.getOrElse(index) { 0f }
                val sweepAngle = proportion * 360f * animSweep.value

                val alpha = alphas[index]
                val highlight = highlights[index]
                val stroke = baseStroke + (selectedStroke - baseStroke) * highlight

                // offset keluar (explode) untuk slice terpilih
                val midAngle = startAngle + sweepAngle / 2f
                val rad = Math.toRadians(midAngle.toDouble())
                val dx = (cos(rad) * explodeOffsetPx * highlight).toFloat()
                val dy = (sin(rad) * explodeOffsetPx * highlight).toFloat()

                // gambar arc
                withTransform({
                    translate(left = dx, top = dy)
                }) {
                    drawArc(
                        color = item.color.copy(alpha = alpha),
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        style = Stroke(width = stroke, cap = StrokeCap.Butt)
                    )
                }

                // label garis & persentase (hanya untuk slice cukup besar)
                if (sweepAngle > 12f) {
                    val angleRad = Math.toRadians((startAngle + sweepAngle / 2f).toDouble())

                    val lineStartRadius = innerR + (baseStroke / 2f)
                    val lineStartX = center.x + (lineStartRadius * cos(angleRad)).toFloat() + dx
                    val lineStartY = center.y + (lineStartRadius * sin(angleRad)).toFloat() + dy

                    val lineMiddleRadius = outerR + 16.dp.toPx()
                    val lineMiddleX = center.x + (lineMiddleRadius * cos(angleRad)).toFloat() + dx
                    val lineMiddleY = center.y + (lineMiddleRadius * sin(angleRad)).toFloat() + dy

                    val lineEndX =
                        if (cos(angleRad) > 0) lineMiddleX + 10.dp.toPx() else lineMiddleX - 10.dp.toPx()

                    // garis
                    drawLine(
                        color = item.color.copy(alpha = alpha),
                        start = Offset(lineStartX, lineStartY),
                        end = Offset(lineMiddleX, lineMiddleY),
                        strokeWidth = 1.5.dp.toPx()
                    )
                    drawLine(
                        color = item.color.copy(alpha = alpha),
                        start = Offset(lineMiddleX, lineMiddleY),
                        end = Offset(lineEndX, lineMiddleY),
                        strokeWidth = 1.5.dp.toPx()
                    )

                    // teks persentase
                    val pct = (proportion * 100f)
                    val labelText = "${pct.roundToInt()}%"
                    val layout = textMeasurer.measure(
                        text = AnnotatedString(labelText),
                        style = TextStyle(
                            color = item.color.copy(alpha = alpha),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    val angleDeg = Math.toDegrees(angleRad)
                    val textX = if (angleDeg in -90.0..90.0) {
                        lineEndX + 2.dp.toPx()
                    } else {
                        lineEndX - layout.size.width - 2.dp.toPx()
                    }
                    val textY = lineMiddleY - (layout.size.height / 2f)

                    drawText(
                        textLayoutResult = layout,
                        topLeft = Offset(textX, textY)
                    )
                }

                startAngle += proportion * 360f
            }
        }
    }
}

/* =========================
   LEGEND
   ========================= */

@Composable
private fun ChartLegend(
    data: List<CategorySummaryUiModel>,
    selectedIndex: Int?,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(data) { index, item ->
            val selected = selectedIndex == index
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onItemClick(index) }
                    .padding(vertical = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(if (selected) 12.dp else 8.dp)
                        .background(item.color, CircleShape)
                        .border(
                            width = if (selected) 1.5.dp else 0.dp,
                            color = if (selected) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f) else Color.Transparent,
                            shape = CircleShape
                        )
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = item.categoryName,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (selected) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                )
            }
        }
    }
}

/* =========================
   PREVIEW
   ========================= */

private val previewSpendingData = listOf(
    CategorySummaryUiModel("Food", BigDecimal(1500), 0.30f, Color(0xFF358AC8)),
    CategorySummaryUiModel("Transport", BigDecimal(1000), 0.20f, Color(0xFF59C4B0)),
    CategorySummaryUiModel("Fun", BigDecimal(500), 0.10f, Color(0xFFF56666)),
    CategorySummaryUiModel("Utilities", BigDecimal(2000), 0.40f, Color(0xFF8378D1)),
)

@Preview(name = "Donut Chart - Light", showBackground = true)
@Preview(
    name = "Donut Chart - Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun CustomDonutChartPreview() {
    TrackFundsTheme {
        CustomDonutChart(
            data = previewSpendingData,
        )
    }
}

@Preview(name = "Donut Chart - Empty", showBackground = true)
@Composable
private fun CustomDonutChartEmptyPreview() {
    TrackFundsTheme {
        CustomDonutChart(data = emptyList())
    }
}