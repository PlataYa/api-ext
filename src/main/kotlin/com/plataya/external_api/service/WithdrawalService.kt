package com.plataya.external_api.service

import com.plataya.external_api.model.dto.WithdrawalRequest
import com.plataya.external_api.model.dto.WithdrawalResponse
import com.plataya.external_api.model.dto.PlataYaDepositRequest
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class WithdrawalService(
    private val plataYaService: PlataYaService,
    private val walletService: InMemoryWalletService
) {
    
    fun processWithdrawal(request: WithdrawalRequest): WithdrawalResult {
        val sourceCvuString = request.sourceCvu.toString()
        
        // First, check if source account exists
        val sourceAccount = walletService.findByCvu(sourceCvuString)
        if (sourceAccount == null) {
            return WithdrawalResult.Error(
                createFailedResponse(request, "Source CVU not found"),
                "Source CVU ${request.sourceCvu} does not exist"
            )
        }
        
        // Check if source account has sufficient funds (our validation)
        if (sourceAccount.balance < request.amount) {
            return WithdrawalResult.Error(
                createFailedResponse(request, "Insufficient funds"),
                "Source CVU ${request.sourceCvu} has insufficient funds. Available: ${sourceAccount.balance}, Required: ${request.amount}"
            )
        }
        
        return try {
            // Create request to PlataYa backend (PlataYa will also validate balance as double-check)
            val plataYaRequest = PlataYaDepositRequest(
                sourceCvu = request.sourceCvu,      // External CVU (source of funds)
                destinationCvu = request.destinationCvu, // Internal CVU (destination)
                amount = request.amount,
                currency = request.currency,
                externalReference = sourceAccount.bankName
            )
            
            // Call PlataYa backend - they will also call our validate-balance endpoint as double-check
            val plataYaResponse = plataYaService.processDeposit(plataYaRequest)
            
            // If PlataYa succeeded, now we deduct the funds from our external system
            val deductionSuccess = walletService.deductBalance(sourceCvuString, request.amount)
            if (!deductionSuccess) {
                return WithdrawalResult.Error(
                    createFailedResponse(request, "Failed to deduct funds"),
                    "Could not deduct funds from source account after PlataYa approval"
                )
            }
            
            WithdrawalResult.Success(
                WithdrawalResponse(
                    transactionId = plataYaResponse.transactionId.toString(),
                    status = plataYaResponse.status,
                    amount = plataYaResponse.amount,
                    destinationCvu = plataYaResponse.destinationCvu,
                    createdAt = plataYaResponse.createdAt
                )
            )
        } catch (e: Exception) {
            WithdrawalResult.Error(
                createFailedResponse(request, "PlataYa backend error"),
                "PlataYa backend call failed: ${e.message}"
            )
        }
    }
    
    private fun createFailedResponse(request: WithdrawalRequest, status: String): WithdrawalResponse {
        return WithdrawalResponse(
            transactionId = "",
            status = status,
            amount = request.amount,
            destinationCvu = request.destinationCvu,
            createdAt = LocalDateTime.now()
        )
    }
}

sealed class WithdrawalResult {
    data class Success(val response: WithdrawalResponse) : WithdrawalResult()
    data class Error(val response: WithdrawalResponse, val errorMessage: String) : WithdrawalResult()
} 