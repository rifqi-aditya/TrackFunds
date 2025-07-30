package com.rifqi.trackfunds.core.domain.usecase.account

import com.rifqi.trackfunds.core.domain.model.Account

interface AddAccountUseCase {
    suspend operator fun invoke(account: Account)
}