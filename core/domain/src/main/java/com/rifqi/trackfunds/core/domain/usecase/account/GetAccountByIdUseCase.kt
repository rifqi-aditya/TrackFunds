package com.rifqi.trackfunds.core.domain.usecase.account

import com.rifqi.trackfunds.core.domain.model.AccountModel

interface GetAccountByIdUseCase {
    suspend operator fun invoke(accountId: String): Result<AccountModel>
}