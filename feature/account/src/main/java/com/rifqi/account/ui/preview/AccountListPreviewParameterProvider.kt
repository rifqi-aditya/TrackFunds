package com.rifqi.account.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.rifqi.trackfunds.core.domain.account.model.Account
import com.rifqi.trackfunds.core.ui.preview.DummyData

class AccountListPreviewParameterProvider : PreviewParameterProvider<List<Account>> {
    override val values = sequenceOf(
        DummyData.dummyAccounts
    )
}