package com.rifqi.trackfunds.core.domain.usecase.account

interface DeleteAccountUseCase {
    suspend operator fun invoke(accountId: String)
}