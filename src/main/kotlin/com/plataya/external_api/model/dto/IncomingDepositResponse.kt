package com.plataya.external_api.model.dto

data class IncomingDepositResponse(
    val transactionId: String,
    val destinationCvu: Long,
    val amount: Double,
    val currency: String,
    val status: String,
    val message: String
) 