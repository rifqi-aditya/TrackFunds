package com.rifqi.trackfunds.core.data.mapper

import com.google.firebase.firestore.DocumentSnapshot
import com.rifqi.trackfunds.core.domain.model.User

/**
 * Mengubah objek DocumentSnapshot dari Firestore menjadi objek User dari domain layer.
 *
 * @param uid UID pengguna yang didapat secara terpisah (karena merupakan ID dokumen).
 * @return Objek User yang berisi data profil.
 */
fun DocumentSnapshot.toUser(uid: String): User {
    return User(
        uid = uid,
        fullName = this.getString("fullName"),
        username = this.getString("username"),
        email = this.getString("email"),
        photoUrl = this.getString("photoUrl"),
        phoneNumber = this.getString("phoneNumber"),
        birthdate = this.getLong("birthdate"),
        gender = this.getString("gender")
    )
}