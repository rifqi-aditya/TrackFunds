package com.rifqi.trackfunds.core.domain.category.usecase

/**
 * Deletes a user-owned category by its ID.
 */
interface DeleteCategoryUseCase {
    suspend operator fun invoke(categoryId: String): Result<Unit>
}