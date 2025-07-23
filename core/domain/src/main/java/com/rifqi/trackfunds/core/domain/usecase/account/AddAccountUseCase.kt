package com.rifqi.trackfunds.core.domain.usecase.account

import com.rifqi.trackfunds.core.domain.model.AccountItem

interface AddAccountUseCase {
    suspend operator fun invoke(account: AccountItem)
}