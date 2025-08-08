package com.rifqi.trackfunds.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val uid: String,
    val email: String,
    val hashedPassword: String,
    val fullName: String?,
    val photoUrl: String?,
    val phoneNumber: String?,
    val birthdate: LocalDate?,
    val createdAt: Long = System.currentTimeMillis()
)