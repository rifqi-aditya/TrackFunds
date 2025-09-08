package com.rifqi.trackfunds.core.domain.auth.exception

class NotAuthenticatedException(
    message: String = "User not authenticated.",
    cause: Throwable? = null
) : IllegalStateException(message, cause)