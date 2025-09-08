package com.rifqi.trackfunds.core.data.repository

import at.favre.lib.crypto.bcrypt.BCrypt
import com.rifqi.trackfunds.core.data.local.dao.UserDao
import com.rifqi.trackfunds.core.data.local.entity.UserEntity
import com.rifqi.trackfunds.core.domain.auth.exception.AuthException
import com.rifqi.trackfunds.core.domain.auth.repository.AuthRepository
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val appPrefsRepositoryImpl: AppPrefsRepositoryImpl
) : AuthRepository {

    override suspend fun login(email: String, pass: String): Result<Unit> {
        return runCatching {
            val user = userDao.getUserByEmail(email)
                ?: throw AuthException("Invalid email or password.")

            val result = BCrypt.verifyer().verify(pass.toCharArray(), user.hashedPassword)
            if (!result.verified) {
                throw AuthException("Invalid email or password.")
            }

            appPrefsRepositoryImpl.setActiveUserId(user.uid)
        }
    }

    override suspend fun register(email: String, pass: String, fullName: String): Result<String> {
        return runCatching {
            if (userDao.getUserByEmail(email) != null) {
                throw AuthException("Email is already registered.")
            }

            val hashedPassword = BCrypt.withDefaults().hashToString(12, pass.toCharArray())
            val newUid = UUID.randomUUID().toString()

            val newUser = UserEntity(
                uid = newUid,
                email = email,
                hashedPassword = hashedPassword,
                fullName = fullName,
                photoUrl = null,
                phoneNumber = null,
                birthdate = null
            )

            userDao.upsert(newUser)

            appPrefsRepositoryImpl.setActiveUserId(newUser.uid)
            newUid
        }
    }

    override suspend fun logout(): Result<Unit> {
        return runCatching {
            appPrefsRepositoryImpl.clearActiveUserId()
        }
    }
}