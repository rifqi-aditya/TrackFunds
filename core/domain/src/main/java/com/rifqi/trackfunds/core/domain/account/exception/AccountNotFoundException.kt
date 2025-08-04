package com.rifqi.trackfunds.core.domain.account.exception

/**
 * Dilempar (thrown) saat sebuah operasi mencoba mengakses akun
 * yang tidak ada di database.
 */
class AccountNotFoundException(message: String) : Exception(message)