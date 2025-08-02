package com.rifqi.trackfunds.core.domain.account.usecase

import com.rifqi.trackfunds.core.domain.account.model.Account

interface UpdateAccountUseCase {
    suspend operator fun invoke(account: Account)
}