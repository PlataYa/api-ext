package com.plataya.external_api.model.dto

data class IncomingDepositRequest(
    val destinationCvu: Long,
    val amount: Double,
    val currency: String? = "ARS"
) 