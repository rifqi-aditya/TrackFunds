package com.rifqi.trackfunds.feature.reports.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.ceil

// --- Data Classes for Chart ---
data class BarData(val label: String, val values: List<Float>)
data class BarRenderInfo(val rect: Rect, val value: Float, val groupLabel: String, val groupIndex: Int, val valueIndex: Int)

// --- Chart Colors & Constants ---
private val DarkBackground = Color(0xFF1A1A1A)
private val BarColor1 = Color(0xFF5850FE)
private val BarColor2 = Color(0xFF84D4F8)
private val AxisColor = Color.White.copy(alpha = 0.6f)
private val TooltipBackground = Color.White
private val TooltipTextColor = Color.Black

private val DEFAULT_Y_AXIS_LABEL_WIDTH = 60.dp
private val DEFAULT_X_AXIS_LABEL_HEIGHT = 40.dp
private val BAR_CORNER_RADIUS = CornerRadius(12f, 12f)
private val TOOLTIP_POINTER_HEIGHT = 8.dp
private val TOOLTIP_CORNER_RADIUS = CornerRadius(10f)
private val AXIS_LABEL_FONT_SIZE = 12.sp // Renamed for generic use
private const val Y_AXIS_GRID_LINE_ALPHA = 0.2f
private val Y_AXIS_GRID_LINE_STROKE_WIDTH = 1.dp
private val Y_AXIS_LABEL_PADDING_END = 8.dp
private val TOOLTIP_TEXT_FONT_SIZE = 14.sp
private val TOOLTIP_POINTER_WIDTH = 12.dp

/**
 * Main composable that sets up the screen and holds the chart.
 */
@Composable
fun FinanceChartScreen() {
    val chartData = remember {
        listOf(
            BarData("Jan", listOf(6_000f, 9_500f)),
            BarData("Feb", listOf(10_000f, 7_500f)),
            BarData("Mar", listOf(18_500f, 11_000f)),
            BarData("May", listOf(16_000f, 15_800f)),
            BarData("Jun", listOf(20_520f, 7_500f))
        )
    }

    var selectedBarForTooltip by remember { mutableStateOf<BarRenderInfo?>(null) }
    var initialSelectionDone by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = DarkBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Monthly Overview",
                color = Color.White,
                fontSize = 22.sp,
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(32.dp))
            GroupedBarChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                barData = chartData,
                selectedBar = selectedBarForTooltip,
                onBarSelected = { barInfo -> selectedBarForTooltip = barInfo },
                onInitialSelectionRequest = { allBarInfos ->
                    if (!initialSelectionDone && allBarInfos.isNotEmpty()) {
                        val junData = allBarInfos.filter { it.groupLabel == "Jun" }
                        val highestJunBar = junData.maxByOrNull { it.value }
                        selectedBarForTooltip = highestJunBar
                        initialSelectionDone = true
                    }
                }
            )
        }
    }
}

/**
 * The main composable for the Grouped Bar Chart.
 */
@Composable
fun GroupedBarChart(
    modifier: Modifier = Modifier,
    barData: List<BarData>,
    selectedBar: BarRenderInfo?,
    onBarSelected: (BarRenderInfo?) -> Unit,
    onInitialSelectionRequest: (List<BarRenderInfo>) -> Unit
) {
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current

    val barRenderInfos = remember { mutableStateListOf<BarRenderInfo>() }

    SideEffect {
        if (barRenderInfos.isNotEmpty()) {
            onInitialSelectionRequest(barRenderInfos.toList())
        }
    }

    // --- Remembered values for drawing ---
    val yAxisLabelStyle = remember {
        TextStyle(color = AxisColor, fontSize = AXIS_LABEL_FONT_SIZE)
    }
    val xAxisLabelStyle = remember {
        TextStyle(color = AxisColor, fontSize = AXIS_LABEL_FONT_SIZE, textAlign = TextAlign.Center)
    }
    val gridLineColor = remember { AxisColor.copy(alpha = Y_AXIS_GRID_LINE_ALPHA) }

    val yAxisLabelWidthPx = remember(density) { with(density) { DEFAULT_Y_AXIS_LABEL_WIDTH.toPx() } }
    val xAxisLabelHeightPx = remember(density) { with(density) { DEFAULT_X_AXIS_LABEL_HEIGHT.toPx() } }
    val gridLineStrokeWidthPx = remember(density) { with(density) { Y_AXIS_GRID_LINE_STROKE_WIDTH.toPx() } }
    val yAxisLabelPaddingEndPx = remember(density) { with(density) { Y_AXIS_LABEL_PADDING_END.toPx() } }

    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(barData, barRenderInfos) {
                    detectTapGestures { offset ->
                        val tappedBar = barRenderInfos.find { it.rect.contains(offset) }
                        onBarSelected(tappedBar)
                    }
                }
        ) {
            // --- Chart Calculations (dependent on size, which is from DrawScope) ---
            // These are not remembered with keys here because they depend on `size` from DrawScope.
            // They will be recalculated on each draw pass if size changes.
            val chartAreaWidth = size.width - yAxisLabelWidthPx
            val chartAreaHeight = size.height - xAxisLabelHeightPx

            val allValues = barData.flatMap { it.values } // Keep this calculation here, it's cheap
            val maxValue = if (allValues.isEmpty()) 0f else ceil(allValues.maxOrNull()!! / 5000f) * 5000f

            // Y-axis values could also be dynamic, for now, they are static
            val yAxisValues = listOf(2000f, 5000f, 10000f, 15000f, 20000f, 25000f)

            barRenderInfos.clear()

            // --- Draw Y-Axis ---
            drawYAxis(
                scope = this,
                yAxisValues = yAxisValues,
                maxValue = maxValue,
                chartAreaHeight = chartAreaHeight,
                labelReservedWidth = yAxisLabelWidthPx,
                textMeasurer = textMeasurer,
                labelStyle = yAxisLabelStyle, // Pass remembered style
                gridLineColor = gridLineColor, // Pass remembered color
                gridLineStrokeWidthPx = gridLineStrokeWidthPx, // Pass remembered Px
                labelPaddingEndPx = yAxisLabelPaddingEndPx // Pass remembered Px
            )

            // --- Draw X-Axis and Bars ---
            if (barData.isNotEmpty()) {
                val groupWidth = chartAreaWidth / barData.size
                val barColors = listOf(BarColor1, BarColor2) // Static colors
                val numBarsInGroup = barData.firstOrNull()?.values?.size ?: 1
                val totalBarWidthRatio = 0.7f

                val singleBarNominalWidth = (groupWidth * totalBarWidthRatio) / numBarsInGroup
                val barSpacing = singleBarNominalWidth * 0.1f
                val actualBarWidth = singleBarNominalWidth - barSpacing
                val groupPadding = groupWidth * (1 - totalBarWidthRatio) / 2f


                barData.forEachIndexed { groupIndex, data ->
                    val groupLeft = yAxisLabelWidthPx + groupIndex * groupWidth + groupPadding

                    drawXAxisLabel(
                        scope = this,
                        label = data.label,
                        groupLeft = groupLeft,
                        groupWidth = groupWidth - (groupPadding * 2), // Usable width for label
                        canvasHeight = size.height, // Pass current canvas height
                        textMeasurer = textMeasurer,
                        labelStyle = xAxisLabelStyle // Pass remembered style
                    )

                    data.values.forEachIndexed { valueIndex, value ->
                        val barHeight = if (maxValue > 0) (value / maxValue) * chartAreaHeight else 0f
                        val barLeft = groupLeft + (actualBarWidth + barSpacing) * valueIndex

                        val barRect = Rect(
                            left = barLeft,
                            top = chartAreaHeight - barHeight,
                            right = barLeft + actualBarWidth,
                            bottom = chartAreaHeight
                        )

                        barRenderInfos.add(
                            BarRenderInfo(barRect, value, data.label, groupIndex, valueIndex)
                        )

                        drawRoundRect(
                            color = barColors[valueIndex % barColors.size],
                            topLeft = barRect.topLeft,
                            size = barRect.size,
                            cornerRadius = BAR_CORNER_RADIUS
                        )
                    }
                }
            }
        }

        selectedBar?.let { info ->
            Tooltip(
                barInfo = info,
                tooltipWidth = 100.dp,
                tooltipHeight = 40.dp
            )
        }
    }
}

/**
 * Draws the Y-axis labels and the faint grid lines.
 * All potentially remembered values are now passed as parameters.
 */
private fun drawYAxis(
    scope: DrawScope,
    yAxisValues: List<Float>,
    maxValue: Float,
    chartAreaHeight: Float,
    labelReservedWidth: Float,
    textMeasurer: TextMeasurer,
    labelStyle: TextStyle,         // Parameter
    gridLineColor: Color,          // Parameter
    gridLineStrokeWidthPx: Float,  // Parameter
    labelPaddingEndPx: Float       // Parameter
) {
    scope.apply {
        yAxisValues.forEach { value ->
            if (maxValue == 0f) return@forEach // Avoid division by zero

            val yPosition = chartAreaHeight * (1f - (value / maxValue))
            if (yPosition in 0f..chartAreaHeight) { // Draw only within chart bounds
                drawLine(
                    color = gridLineColor,
                    start = Offset(labelReservedWidth, yPosition),
                    end = Offset(size.width, yPosition),
                    strokeWidth = gridLineStrokeWidthPx
                )

                val label = formatYAxisLabel(value)
                val textLayoutResult = textMeasurer.measure(
                    text = AnnotatedString(label),
                    style = labelStyle // Use passed style
                )
                drawText(
                    textLayoutResult = textLayoutResult,
                    topLeft = Offset(
                        labelReservedWidth - textLayoutResult.size.width - labelPaddingEndPx,
                        yPosition - textLayoutResult.size.height / 2f
                    )
                )
            }
        }
    }
}

private fun formatYAxisLabel(value: Float): String {
    return when {
        value >= 1000 -> "${(value / 1000).toInt()}K"
        else -> value.toInt().toString()
    }
}

/**
 * Draws the X-axis labels (months).
 * All potentially remembered values are now passed as parameters.
 */
private fun drawXAxisLabel(
    scope: DrawScope,
    label: String,
    groupLeft: Float,
    groupWidth: Float,
    canvasHeight: Float,
    textMeasurer: TextMeasurer,
    labelStyle: TextStyle // Parameter
) {
    scope.apply {
        val textLayoutResult = textMeasurer.measure(
            text = AnnotatedString(label),
            style = labelStyle // Use passed style
        )
        // Center the text within the groupWidth
        val textX = groupLeft + (groupWidth / 2f) - (textLayoutResult.size.width / 2f)
        // Positioned at the bottom, above the conceptual x-axis line (if any)
        // Adjusted to use canvasHeight directly for positioning from bottom
        val textY = canvasHeight - textLayoutResult.size.height - (with(scope) { DEFAULT_X_AXIS_LABEL_HEIGHT.toPx() } / 4) // Small padding from bottom

        drawText(
            textLayoutResult = textLayoutResult,
            topLeft = Offset(textX, textY)
        )
    }
}

/**
 * A composable for the tooltip that appears when a bar is tapped.
 */
@Composable
private fun Tooltip(
    modifier: Modifier = Modifier, // Keep modifier for potential external use
    barInfo: BarRenderInfo,
    tooltipWidth: Dp,
    tooltipHeight: Dp
) {
    val density = LocalDensity.current

    val tooltipWidthPx = remember(density, tooltipWidth) { with(density) { tooltipWidth.toPx() } }
    val tooltipHeightPx = remember(density, tooltipHeight) { with(density) { tooltipHeight.toPx() } }
    val pointerHeightPx = remember(density) { with(density) { TOOLTIP_POINTER_HEIGHT.toPx() } }
    val pointerWidthPx = remember(density) { with(density) { TOOLTIP_POINTER_WIDTH.toPx() } }


    val tooltipX by remember(barInfo.rect.center.x, tooltipWidthPx) {
        derivedStateOf { barInfo.rect.center.x - (tooltipWidthPx / 2f) }
    }
    val tooltipY by remember(barInfo.rect.top, tooltipHeightPx, pointerHeightPx) {
        derivedStateOf { barInfo.rect.top - tooltipHeightPx - pointerHeightPx }
    }

    val currencyFormat = remember {
        NumberFormat.getCurrencyInstance(Locale("en", "US")).apply {
            maximumFractionDigits = 0
        }
    }
    val formattedValue = remember(barInfo.value, currencyFormat) {
        currencyFormat.format(barInfo.value)
    }

    // Convert calculated Px tooltip position back to Dp for the .offset modifier
    val tooltipOffsetX = remember(density, tooltipX) { with(density) { tooltipX.toDp() } }
    val tooltipOffsetY = remember(density, tooltipY) { with(density) { tooltipY.toDp() } }

    val tooltipTextStyle = remember {
        TextStyle(fontWeight = FontWeight.Bold, color = TooltipTextColor, fontSize = TOOLTIP_TEXT_FONT_SIZE)
    }

    Box(
        modifier = modifier // Apply the passed modifier first
            .offset(x = tooltipOffsetX, y = tooltipOffsetY)
            .size(tooltipWidth, tooltipHeight) // Then size
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val path = Path().apply {
                addRoundRect(
                    RoundRect(
                        rect = Rect(Offset.Zero, Size(size.width, size.height - pointerHeightPx)),
                        cornerRadius = TOOLTIP_CORNER_RADIUS
                    )
                )
                moveTo(size.width / 2f - pointerWidthPx / 2f, size.height - pointerHeightPx)
                lineTo(size.width / 2f, size.height)
                lineTo(size.width / 2f + pointerWidthPx / 2f, size.height - pointerHeightPx)
                close()
            }
            // Simple shadow by drawing path offset and slightly darker
            drawPath(path = path, color = Color.Black.copy(alpha = 0.2f), style = Fill, blendMode = BlendMode.SrcOver) // Basic shadow
            drawPath(path = path, color = TooltipBackground, style = Fill)
        }
        Text(
            text = formattedValue,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = TOOLTIP_POINTER_HEIGHT / 2), // Adjust padding for text within tooltip body
            style = tooltipTextStyle
        )
    }
}

// --- Preview ---
@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun FinanceChartScreenPreview() {
    FinanceChartScreen()
}
