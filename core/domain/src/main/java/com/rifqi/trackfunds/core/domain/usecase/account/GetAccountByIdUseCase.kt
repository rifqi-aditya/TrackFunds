package com.rifqi.trackfunds.core.domain.usecase.account

import com.rifqi.trackfunds.core.domain.model.AccountItem

interface GetAccountByIdUseCase {
    suspend operator fun invoke(accountId: String): AccountItem?
}