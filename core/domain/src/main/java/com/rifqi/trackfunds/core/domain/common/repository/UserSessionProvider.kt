package com.rifqi.trackfunds.core.domain.common.repository

import kotlinx.coroutines.flow.Flow

interface UserSessionProvider {
    /** Mengambil UID sebagai Flow untuk observasi reaktif. */
    fun getUidFlow(): Flow<String?>

    /** Mengambil UID sekali jalan, akan gagal jika user belum login. */
    suspend fun getUid(): String
}