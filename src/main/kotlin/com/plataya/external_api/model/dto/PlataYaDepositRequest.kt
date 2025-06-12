package com.plataya.external_api.model.dto

data class PlataYaDepositRequest(
    val sourceCvu: Long,        // CVU where money comes from (external)
    val destinationCvu: Long,   // CVU where money goes to (internal)
    val amount: Double,
    val currency: String,
    val externalReference: String
) 