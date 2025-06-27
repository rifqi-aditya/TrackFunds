package com.rifqi.trackfunds.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScanRequestDto(
    @SerialName("data")
    val data: String
)