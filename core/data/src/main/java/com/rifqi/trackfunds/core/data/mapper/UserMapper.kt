package com.rifqi.trackfunds.core.data.mapper

import com.rifqi.trackfunds.core.data.local.entity.UserEntity
import com.rifqi.trackfunds.core.domain.model.UserModel

/**
 * Mengubah UserEntity (objek database) menjadi User (objek domain).
 * Digunakan saat mengambil data dari database.
 */
fun UserEntity.toDomainModel(): UserModel {
    return UserModel(
        uid = this.uid,
        email = this.email,
        username = this.username,
        fullName = this.fullName,
        photoUrl = this.photoUrl,
        phoneNumber = this.phoneNumber,
        gender = this.gender,
        birthdate = this.birthdate
    )
}

/**
 * Mengubah User (objek domain) menjadi UserEntity (objek database).
 * Digunakan saat menyimpan data ke database.
 */
fun UserModel.toEntity(): UserEntity {
    return UserEntity(
        uid = this.uid,
        email = this.email ?: "", // Email di entity tidak boleh null
        username = this.username,
        fullName = this.fullName,
        photoUrl = this.photoUrl,
        phoneNumber = this.phoneNumber,
        gender = this.gender,
        birthdate = this.birthdate
    )
}