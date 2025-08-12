package com.rifqi.trackfunds.feature.reports.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.feature.reports.ui.state.CashFlowChartItem
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.PopupProperties

@Composable
fun CashflowBarChart(
    monthlyFlows: List<CashFlowChartItem>,
    modifier: Modifier = Modifier
) {
    val incomeBrush = Brush.verticalGradient(
        colors = listOf(
            TrackFundsTheme.extendedColors.income,
            TrackFundsTheme.extendedColors.income.copy(alpha = 0.5f)
        )
    )
    val expenseBrush = Brush.verticalGradient(
        colors = listOf(
            TrackFundsTheme.extendedColors.expense,
            TrackFundsTheme.extendedColors.expense.copy(alpha = 0.5f)
        )
    )

    val chartData = remember(monthlyFlows) {
        monthlyFlows.map { flow ->
            Bars(
                label = flow.monthLabel,
                values = listOf(
                    Bars.Data(
                        label = "Income",
                        value = flow.incomeAmount.toDouble(),
                        color = incomeBrush
                    ),
                    Bars.Data(
                        label = "Expense",
                        value = flow.expenseAmount.toDouble(),
                        color = expenseBrush
                    )
                )
            )
        }
    }

    BoxWithConstraints(
        modifier = modifier.fillMaxWidth()
    ) {
        val screenWidth = this.maxWidth

        val requiredWidth = (monthlyFlows.size * (20.dp + 50.dp)) + 22.dp
        val isScrollable = requiredWidth > screenWidth

        val chartModifier = if (isScrollable) {
            Modifier.width(requiredWidth)
        } else {
            Modifier.fillMaxWidth()
        }

        val containerModifier = if (isScrollable) {
            Modifier.horizontalScroll(rememberScrollState())
        } else {
            Modifier
        }

        Box(modifier = containerModifier.padding(16.dp)) {
            ColumnChart(
                modifier = chartModifier
                    .height(250.dp)
                    .padding(bottom = 22.dp),
                data = chartData,
                barProperties = BarProperties(
                    cornerRadius = Bars.Data.Radius.Rectangle(
                        topRight = 6.dp,
                        topLeft = 6.dp
                    ),
                    spacing = 0.dp,
                    thickness = 20.dp
                ),
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                labelProperties = LabelProperties(
                    enabled = true,
                    textStyle = androidx.compose.ui.text.TextStyle(
                        color = MaterialTheme.colorScheme.onSurface
                    )
                ),
                labelHelperProperties = LabelHelperProperties(
                    enabled = true,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                ),
                indicatorProperties = HorizontalIndicatorProperties(
                    textStyle = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    contentBuilder = { value ->
                        when {
                            value >= 1_000_000 -> {
                                val millions = value / 1_000_000.0
                                "${
                                    "%.1f".format(millions).replace(".0", "")
                                } jt"
                            }

                            value >= 1_000 -> {
                                "${(value / 1_000).toInt()} rb"
                            }

                            else -> {
                                value.toInt().toString()
                            }
                        }
                    },
                ),
                popupProperties = PopupProperties(
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.inverseOnSurface
                    ),
                    containerColor = MaterialTheme.colorScheme.inverseSurface,
                    contentBuilder = { _, _, value ->
                        when {
                            value >= 1_000_000 -> {
                                val millions = value / 1_000_000.0
                                "${
                                    "%.1f".format(millions).replace(".0", "")
                                } jt"
                            }

                            value >= 1_000 -> {
                                "${(value / 1_000).toInt()} rb"
                            }

                            else -> {
                                value.toInt().toString()
                            }
                        }
                    }
                )
            )
        }
    }
}