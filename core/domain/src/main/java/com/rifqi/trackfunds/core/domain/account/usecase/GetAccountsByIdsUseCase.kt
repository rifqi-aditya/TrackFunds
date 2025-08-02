package com.rifqi.trackfunds.core.domain.account.usecase

import com.rifqi.trackfunds.core.domain.account.model.Account

interface GetAccountsByIdsUseCase {
    suspend operator fun invoke(ids: List<String>): List<Account>
}