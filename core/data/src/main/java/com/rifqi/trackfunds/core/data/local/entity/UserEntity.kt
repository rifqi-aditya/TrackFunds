package com.rifqi.trackfunds.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val uid: String,
    val email: String,
    val username: String?,
    val fullName: String?,
    val photoUrl: String?,
    val phoneNumber: String?,
    val gender: String?,
    val birthdate: Long?
)