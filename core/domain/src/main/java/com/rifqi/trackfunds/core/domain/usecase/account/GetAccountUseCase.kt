package com.rifqi.trackfunds.core.domain.usecase.account

import com.rifqi.trackfunds.core.domain.model.AccountItem

/**
 * Use case untuk mengambil detail satu akun berdasarkan ID.
 */
interface GetAccountUseCase {
    suspend operator fun invoke(accountId: String): AccountItem?
}