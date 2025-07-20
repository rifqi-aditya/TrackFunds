package com.rifqi.trackfunds.core.domain.repository

interface AuthRepository {
    suspend fun login(email: String, pass: String): Result<Unit>
    suspend fun register(email: String, pass: String): Result<Unit>
    suspend fun logout()
}