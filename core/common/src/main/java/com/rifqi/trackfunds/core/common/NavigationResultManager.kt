package com.rifqi.trackfunds.core.common

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationResultManager @Inject constructor() {
    private val _result = MutableStateFlow<Any?>(null)
    val result = _result.asStateFlow()

    fun setResult(result: Any?) {
        _result.value = result
    }
}