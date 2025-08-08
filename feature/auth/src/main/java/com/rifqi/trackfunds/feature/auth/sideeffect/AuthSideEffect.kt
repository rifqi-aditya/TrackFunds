package com.rifqi.trackfunds.feature.auth.sideeffect

sealed interface AuthSideEffect {
    data object NavigateToHome : AuthSideEffect
}
