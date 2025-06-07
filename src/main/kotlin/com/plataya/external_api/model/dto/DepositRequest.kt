package com.plataya.external_api.model.dto

data class WithdrawalRequest(
    val destinationCvu: Long,
    val amount: Double,
    val currency: String,
    val sourceCvu: Long
) 