package com.rifqi.trackfunds.core.domain.usecase.account

import com.rifqi.trackfunds.core.domain.model.AccountModel
import kotlinx.coroutines.flow.Flow

interface GetAccountsUseCase {
    operator fun invoke(): Flow<List<AccountModel>>
}