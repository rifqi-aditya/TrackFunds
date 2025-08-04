package com.rifqi.trackfunds.core.domain.account.model

data class UpdateAccountParams(
    val id: String,
    val name: String,
    val iconIdentifier: String
)