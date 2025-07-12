package com.rifqi.trackfunds.core.domain.usecase.account

import com.rifqi.trackfunds.core.domain.model.AccountItem

interface AddAccountUseCase {
    /**
     * Menambahkan akun baru.
     *
     * @param account Akun yang akan ditambahkan.
     */
    suspend operator fun invoke(account: AccountItem)
}