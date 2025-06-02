package com.plataya.external_api.controller

import com.plataya.external_api.model.dto.CvuValidationResponseDTO
import com.plataya.external_api.model.dto.ExternalBalanceValidationRequest
import com.plataya.external_api.model.dto.ExternalCvuValidationRequest
import com.plataya.external_api.model.dto.ExternalWalletValidationDTO
import com.plataya.external_api.repository.AccountRepository
import com.plataya.external_api.repository.TransactionRepository
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class ApiController(
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository
) {
    @GetMapping("/wallet/validate-cvu")
    fun validateCvu(@RequestBody request: ExternalCvuValidationRequest): ResponseEntity<CvuValidationResponseDTO>{
        val wallet = accountRepository.findById(request.cvu.toString())
        return ResponseEntity.ok(CvuValidationResponseDTO(valid = wallet.isPresent))
    }

    @PostMapping("/wallet/validate-deposit")
    fun validateExternalCvuBalance(@RequestBody request: ExternalBalanceValidationRequest): ResponseEntity<ExternalWalletValidationDTO> {
        val cvuString = request.cvu.toString()
        val account = accountRepository.findById(cvuString)

        return if (account.isPresent) {
            val wallet = account.get()
            val currentBalance = wallet.balance
            val hasSufficientFunds = currentBalance >= request.amount
            if (hasSufficientFunds) {
                ResponseEntity.ok(
                    ExternalWalletValidationDTO(
                        cvu = request.cvu,
                        exists = true,
                        balance = currentBalance.toFloat(),
                        hasSufficientFunds = true
                    )
                )
            } else {
                // Insufficient funds
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ExternalWalletValidationDTO(
                        cvu = request.cvu,
                        exists = true,
                        balance = currentBalance.toFloat(),
                        hasSufficientFunds = false
                    )
                )
            }
        } else {
            // CVU not found
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ExternalWalletValidationDTO(
                    cvu = request.cvu,
                    exists = false
                )
            )
        }
    }
}