package com.rifqi.trackfunds.core.domain.auth.repository

interface AuthRepository {
    suspend fun login(email: String, pass: String): Result<Unit>
    suspend fun register(email: String, pass: String): Result<String>
    suspend fun logout()
}