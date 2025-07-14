package com.rifqi.trackfunds.core.domain.usecase.category

interface DeleteCategoryUseCase {
    suspend operator fun invoke(categoryId: String)
}