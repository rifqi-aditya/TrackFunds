package com.rifqi.trackfunds.core.domain.usecase.account

import com.rifqi.trackfunds.core.domain.model.Account
import kotlinx.coroutines.flow.Flow

interface GetAccountsUseCase {
    operator fun invoke(): Flow<List<Account>>
}