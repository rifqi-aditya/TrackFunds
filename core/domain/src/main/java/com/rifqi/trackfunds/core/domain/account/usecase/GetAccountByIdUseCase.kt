package com.rifqi.trackfunds.core.domain.account.usecase

import com.rifqi.trackfunds.core.domain.account.model.Account

interface GetAccountByIdUseCase {
    suspend operator fun invoke(accountId: String): Result<Account>
}