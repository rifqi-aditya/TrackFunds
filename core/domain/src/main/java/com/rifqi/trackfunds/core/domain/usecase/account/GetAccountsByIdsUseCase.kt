package com.rifqi.trackfunds.core.domain.usecase.account

import com.rifqi.trackfunds.core.domain.model.AccountModel

interface GetAccountsByIdsUseCase {
    suspend operator fun invoke(ids: List<String>): List<AccountModel>
}