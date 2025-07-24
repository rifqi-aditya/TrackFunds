package com.rifqi.trackfunds.core.domain.usecase.category

/**
 * Deletes a user-owned category by its ID.
 */
interface DeleteCategoryUseCase {
    suspend operator fun invoke(categoryId: String): Result<Unit>
}