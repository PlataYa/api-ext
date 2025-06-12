package com.plataya.external_api.service

import com.plataya.external_api.model.dto.ExternalCvuValidationRequest
import com.plataya.external_api.model.dto.ExternalCvuValidationDTO
import org.springframework.stereotype.Service

@Service
class CvuValidationService(
    private val walletService: InMemoryWalletService
) {
    
    fun validateCvu(request: ExternalCvuValidationRequest): ExternalCvuValidationDTO {
        val cvuString = request.cvu.toString()
        val account = walletService.findByCvu(cvuString)

        return if (account != null) {
            ExternalCvuValidationDTO(
                exists = true,
                bankName = account.bankName
            )
        } else {
            ExternalCvuValidationDTO(
                exists = false,
                bankName = ""
            )
        }
    }
} 