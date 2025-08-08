package com.rifqi.trackfunds.core.domain.user.repository

import android.net.Uri
import com.rifqi.trackfunds.core.domain.common.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing user profile data.
 */
interface UserRepository {
    /**
     * Observes changes to the user's profile.
     *
     * @return A [Flow] that emits the current [User] profile, or `null` if no profile exists.
     */
    fun observeProfile(): Flow<User?>
    /**
     * Saves the user's profile information.
     *
     * @param user The [User] object containing the profile data to save.
     * @param newImageUri The [Uri] of a new profile image, or `null` if the image is not being changed.
     * @return A [Result] indicating success or failure of the operation.
     */
    suspend fun saveProfile(user: User, newImageUri: Uri?): Result<Unit>
    /**
     * Deletes the user's profile.
     *
     * @return A [Result] indicating success or failure of the operation.
     */
    suspend fun deleteProfile(): Result<Unit>
}