package com.rifqi.trackfunds.feature.home.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.rifqi.trackfunds.core.ui.preview.DummyData
import com.rifqi.trackfunds.feature.home.ui.home.HomeUiState
import java.math.BigDecimal

class HomeUiStatePreviewParameterProvider : PreviewParameterProvider<HomeUiState> {
    override val values: Sequence<HomeUiState> = sequenceOf(
        HomeUiState(isLoading = true),
        HomeUiState(
            isLoading = false,
            totalBalance = DummyData.dummyAccounts.sumOf { it.balance },
        ),
        HomeUiState(
            isLoading = false,
            totalBalance = BigDecimal.ZERO,
        )
    )
}