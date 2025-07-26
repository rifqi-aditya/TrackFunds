package com.rifqi.trackfunds.core.domain.usecase.account

import com.rifqi.trackfunds.core.domain.model.AccountModel

interface UpdateAccountUseCase {
    suspend operator fun invoke(account: AccountModel)
}