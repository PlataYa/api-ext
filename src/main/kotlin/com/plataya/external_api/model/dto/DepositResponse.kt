package com.plataya.external_api.model.dto

import java.time.LocalDateTime

data class WithdrawalResponse(
    val transactionId: String,
    val status: String,
    val amount: Double,
    val destinationCvu: Long,
    val createdAt: LocalDateTime
) 