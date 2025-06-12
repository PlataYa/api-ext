package com.plataya.external_api.model.dto

data class ExternalWalletValidationDTO(
    val cvu: Long,
    val exists: Boolean,
    val balance: Double? = null,
    val hasSufficientFunds: Boolean = false
) 