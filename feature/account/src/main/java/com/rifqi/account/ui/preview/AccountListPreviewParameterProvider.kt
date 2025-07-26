package com.rifqi.account.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.rifqi.trackfunds.core.domain.model.AccountModel
import com.rifqi.trackfunds.core.ui.preview.DummyData

class AccountListPreviewParameterProvider : PreviewParameterProvider<List<AccountModel>> {
    override val values = sequenceOf(
        DummyData.dummyAccounts
    )
}