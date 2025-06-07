package com.plataya.external_api.service

import com.plataya.external_api.model.dto.IncomingDepositRequest
import com.plataya.external_api.model.dto.IncomingDepositResponse
import org.springframework.stereotype.Service

@Service
class IncomingDepositService(
    private val walletService: InMemoryWalletService
) {
    
    fun processIncomingDeposit(request: IncomingDepositRequest): IncomingDepositResult {
        val destinationCvuString = request.destinationCvu.toString()
        
        // Check if destination account exists
        val destinationAccount = walletService.findByCvu(destinationCvuString)
        if (destinationAccount == null) {
            return IncomingDepositResult.CvuNotFound(
                IncomingDepositResponse(
                    transactionId = "",
                    destinationCvu = request.destinationCvu,
                    amount = request.amount,
                    currency = request.currency ?: "ARS",
                    status = "FAILED",
                    message = "Destination CVU not found"
                )
            )
        }
        
        // Add funds to destination account
        val additionSuccess = walletService.addBalance(destinationCvuString, request.amount)
        return if (additionSuccess) {
            IncomingDepositResult.Success(
                IncomingDepositResponse(
                    transactionId = generateTransactionId(),
                    destinationCvu = request.destinationCvu,
                    amount = request.amount,
                    currency = request.currency ?: "ARS",
                    status = "COMPLETED",
                    message = "Deposit successful"
                )
            )
        } else {
            IncomingDepositResult.Error(    
                IncomingDepositResponse(
                    transactionId = "",
                    destinationCvu = request.destinationCvu,
                    amount = request.amount,
                    currency = request.currency ?: "ARS",
                    status = "FAILED",
                    message = "Failed to add funds to account"
                )
            )
        }
    }
    
    private fun generateTransactionId(): String {
        return "ext-ref-${System.currentTimeMillis()}"
    }
}

sealed class IncomingDepositResult {
    data class Success(val response: IncomingDepositResponse) : IncomingDepositResult()
    data class CvuNotFound(val response: IncomingDepositResponse) : IncomingDepositResult()
    data class Error(val response: IncomingDepositResponse) : IncomingDepositResult()
} 