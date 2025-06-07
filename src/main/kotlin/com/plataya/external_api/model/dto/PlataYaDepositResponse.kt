package com.plataya.external_api.model.dto

import java.time.LocalDateTime

data class PlataYaDepositResponse(
    val transactionId: Long,
    val amount: Double,
    val currency: String,
    val status: String,
    val createdAt: LocalDateTime,
    val type: String,    // "DEPOSIT" or"WITHDRAWAL"
    val sourceCvu: Long,    
    val destinationCvu: Long,
    val externalReference: String? // only for external transactions
) 