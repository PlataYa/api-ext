package com.plataya.external_api.service

import com.plataya.external_api.model.dto.ExternalCvuValidationRequest
import com.plataya.external_api.model.dto.ExternalWalletValidationDTO
import org.springframework.stereotype.Service

@Service
class CvuValidationService(
    private val walletService: InMemoryWalletService
) {
    
    fun validateCvu(request: ExternalCvuValidationRequest): ExternalWalletValidationDTO {
        val cvuString = request.cvu.toString()
        val account = walletService.findByCvu(cvuString)

        return if (account != null) {
            ExternalWalletValidationDTO(
                cvu = request.cvu,
                exists = true,
                balance = account.balance,
                hasSufficientFunds = false // Default as per spec for this endpoint
            )
        } else {
            ExternalWalletValidationDTO(
                cvu = request.cvu,
                exists = false
            )
        }
    }
} 