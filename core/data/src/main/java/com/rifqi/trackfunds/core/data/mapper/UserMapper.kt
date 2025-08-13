package com.rifqi.trackfunds.core.data.mapper

import com.rifqi.trackfunds.core.data.local.entity.UserEntity
import com.rifqi.trackfunds.core.domain.user.model.User

/**
 * Mengubah UserEntity (objek database) menjadi User (objek domain).
 * Digunakan saat mengambil data dari database.
 */
fun UserEntity.toDomain(): User {
    return User(
        uid = this.uid,
        email = this.email,
        fullName = this.fullName,
        photoUrl = this.photoUrl,
        phoneNumber = this.phoneNumber,
        birthdate = this.birthdate,
        createdAt = this.createdAt
    )
}

/**
 * Mengubah User (objek domain) menjadi UserEntity (objek database).
 * Digunakan saat menyimpan data ke database.
 */
fun User.toEntity(existingEntity: UserEntity): UserEntity {
    return existingEntity.copy(
        fullName = this.fullName,
        photoUrl = this.photoUrl,
        phoneNumber = this.phoneNumber,
        birthdate = this.birthdate
    )
}