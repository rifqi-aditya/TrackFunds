package com.rifqi.trackfunds.core.domain.usecase.account

import com.rifqi.trackfunds.core.domain.model.AccountItem

interface GetAccountsByIdsUseCase {
    suspend operator fun invoke(ids: List<String>): List<AccountItem>
}