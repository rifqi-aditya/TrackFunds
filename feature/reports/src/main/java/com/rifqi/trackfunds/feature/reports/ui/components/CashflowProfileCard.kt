package com.rifqi.trackfunds.feature.reports.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rifqi.trackfunds.core.domain.model.CashFlowSummary
import com.rifqi.trackfunds.core.ui.theme.AppTheme
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.DividerProperties
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.PopupProperties

@Composable
fun CashflowProfileCard(
    summary: CashFlowSummary,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Profil arus kas kamu",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(24.dp))

            val incomeBrush = Brush.verticalGradient(
                colors = listOf(
                    AppTheme.extendedColors.textIncome,
                    AppTheme.extendedColors.textIncome.copy(alpha = 0.5f)
                )
            )
            val expenseBrush = Brush.verticalGradient(
                colors = listOf(
                    AppTheme.extendedColors.textExpense,
                    AppTheme.extendedColors.textExpense.copy(alpha = 0.5f)
                )
            )
            val savingsBrush = Brush.verticalGradient(
                colors = listOf(
                    AppTheme.extendedColors.chartConditionGood,
                    AppTheme.extendedColors.chartConditionGood.copy(alpha = 0.5f)
                )
            )

            ColumnChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                data = remember(summary) {
                    listOf(
                        Bars(
                            label = "",
                            values = listOf(
                                Bars.Data(
                                    label = "Income",
                                    value = summary.totalIncome.toDouble(),
                                    color = incomeBrush,
                                ),
                                Bars.Data(
                                    label = "Expense",
                                    value = summary.totalExpense.toDouble(),
                                    color = expenseBrush
                                ),
                                Bars.Data(
                                    label = "Savings",
                                    value = 1_000_000.0,
                                    color = savingsBrush
                                )
                            ),
                        )
                    )
                },
                labelProperties = LabelProperties(
                    enabled = true,
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 24.sp
                    )
                ),
                barProperties = BarProperties(
                    cornerRadius = Bars.Data.Radius.Rectangle(
                        topRight = 6.dp,
                        topLeft = 6.dp
                    ),
                    spacing = 30.dp,
                    thickness = 70.dp
                ),
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                dividerProperties = DividerProperties(enabled = false),
                gridProperties = GridProperties(
                    enabled = true,
                    xAxisProperties = GridProperties.AxisProperties(
                        thickness = (.4).dp,
                    ),
                    yAxisProperties = GridProperties.AxisProperties(
                        enabled = false,
                        thickness = (.1).dp,
                    )
                ),
                indicatorProperties = HorizontalIndicatorProperties(
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 12.sp,
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
                labelHelperProperties = LabelHelperProperties(
                    enabled = true,
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 12.sp
                    )
                ),
                popupProperties = PopupProperties(
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
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