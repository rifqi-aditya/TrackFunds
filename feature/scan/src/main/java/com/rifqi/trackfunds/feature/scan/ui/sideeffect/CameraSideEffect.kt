package com.rifqi.trackfunds.feature.scan.ui.sideeffect

sealed interface CameraSideEffect {
    data object NavigateBack : CameraSideEffect
}