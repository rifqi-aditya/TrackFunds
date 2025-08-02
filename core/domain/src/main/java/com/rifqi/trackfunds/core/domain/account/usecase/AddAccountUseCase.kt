package com.rifqi.trackfunds.core.domain.account.usecase

import com.rifqi.trackfunds.core.domain.account.model.Account

interface AddAccountUseCase {
    suspend operator fun invoke(account: Account)
}