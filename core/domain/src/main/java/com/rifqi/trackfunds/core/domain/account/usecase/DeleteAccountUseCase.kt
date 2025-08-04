package com.rifqi.trackfunds.core.domain.account.usecase

interface DeleteAccountUseCase {
    suspend operator fun invoke(accountId: String) : Result<Unit>
}