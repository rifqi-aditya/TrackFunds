package com.rifqi.trackfunds.core.domain.settings.usecase

import javax.inject.Inject
import javax.inject.Named

interface GetAppVersionUseCase {
    operator fun invoke(): String
}

class GetAppVersionUseCaseImpl @Inject constructor(
    @Named("AppVersion") private val appVersion: String
) : GetAppVersionUseCase {
    override operator fun invoke(): String {
        return appVersion
    }
}