package com.rifqi.trackfunds.core.domain.settings.usecase

import com.rifqi.trackfunds.core.domain.common.repository.AppPrefsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface ObserveLocaleUseCase {
    operator fun invoke(): Flow<String>
}

class ObserveLocaleUseCaseImpl @Inject constructor(
    private val prefs: AppPrefsRepository
) : ObserveLocaleUseCase {
    override fun invoke(): Flow<String> =
        prefs.localeTag
            .map { tag -> if (tag == "in") "id" else tag }
}