package com.plataya.external_api.service

import com.plataya.external_api.model.dto.ExternalBalanceValidationRequest
import com.plataya.external_api.model.dto.ExternalWalletValidationDTO
import org.springframework.stereotype.Service

@Service
class BalanceValidationService(
    private val walletService: InMemoryWalletService
) {
    
    fun validateBalance(request: ExternalBalanceValidationRequest): ValidationResult {
        println("Validating balance for CVU: ${request.cvu}, Amount: ${request.amount}")
        val cvuString = request.cvu.toString()
        val account = walletService.findByCvu(cvuString)

        println("Account found: ${account != null}")

        return if (account != null) {
            val currentBalance = account.balance
            val hasSufficientFunds = currentBalance >= request.amount
            println("Current balance: $currentBalance, Has sufficient funds: $hasSufficientFunds")

            if (hasSufficientFunds) {
                ValidationResult.Success(
                    ExternalWalletValidationDTO(
                        cvu = request.cvu,
                        exists = true,
                        balance = currentBalance,
                        hasSufficientFunds = true
                    )
                )
            } else {
                ValidationResult.InsufficientFunds(
                    ExternalWalletValidationDTO(
                        cvu = request.cvu,
                        exists = true,
                        balance = currentBalance,
                        hasSufficientFunds = false
                    )
                )
            }
        } else {
            ValidationResult.CvuNotFound(
                ExternalWalletValidationDTO(
                    cvu = request.cvu,
                    exists = false
                )
            )
        }
    }
}

sealed class ValidationResult {
    data class Success(val data: ExternalWalletValidationDTO) : ValidationResult()
    data class InsufficientFunds(val data: ExternalWalletValidationDTO) : ValidationResult()
    data class CvuNotFound(val data: ExternalWalletValidationDTO) : ValidationResult()
} 