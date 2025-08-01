package com.rifqi.trackfunds.core.common

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationResultManager @Inject constructor() {
    private val _result = MutableStateFlow<Any?>(null)
    val result: StateFlow<Any?> = _result

    fun setResult(data: Any?) {
        _result.value = data
    }

    private val _argument = MutableStateFlow<Any?>(null)
    val argument: StateFlow<Any?> = _argument

    fun setArgument(data: Any?) {
        _argument.value = data
    }
}