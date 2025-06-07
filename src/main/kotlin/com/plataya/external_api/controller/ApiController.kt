package com.plataya.external_api.controller

import com.plataya.external_api.model.dto.*
import com.plataya.external_api.service.*
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class ApiController(
    private val withdrawalService: WithdrawalService,
    private val cvuValidationService: CvuValidationService,
    private val balanceValidationService: BalanceValidationService,
    private val incomingDepositService: IncomingDepositService,
    private val walletService: InMemoryWalletService
) {
    
    @PostMapping("/wallet/withdraw")
    fun processWithdraw(@RequestBody request: WithdrawalRequest): ResponseEntity<WithdrawalResponse> {
        return when (val result = withdrawalService.processWithdrawal(request)) {
            is WithdrawalResult.Success -> ResponseEntity.ok(result.response)
            is WithdrawalResult.Error -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result.response)
        }
    }

    @PostMapping("/wallet/validate-cvu")
    fun validateExternalCvu(@RequestBody request: ExternalCvuValidationRequest): ResponseEntity<ExternalWalletValidationDTO> {
        val result = cvuValidationService.validateCvu(request)
        return if (result.exists) {
            ResponseEntity.ok(result)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(result)
        }
    }

    @PostMapping("/wallet/validate-balance")
    fun validateExternalCvuBalance(@RequestBody request: ExternalBalanceValidationRequest): ResponseEntity<ExternalWalletValidationDTO> {
        return when (val result = balanceValidationService.validateBalance(request)) {
            is ValidationResult.Success -> ResponseEntity.ok(result.data)
            is ValidationResult.InsufficientFunds -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.data)
            is ValidationResult.CvuNotFound -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(result.data)
        }
    }

    @PostMapping("/wallet/deposit")
    fun receiveDeposit(@RequestBody request: IncomingDepositRequest): ResponseEntity<IncomingDepositResponse> {
        return when (val result = incomingDepositService.processIncomingDeposit(request)) {
            is IncomingDepositResult.Success -> ResponseEntity.ok(result.response)
            is IncomingDepositResult.CvuNotFound -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.response)
            is IncomingDepositResult.Error -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result.response)
        }
    }

    @GetMapping("/accounts")
    fun getAllAccounts(): ResponseEntity<Map<String, Any>> {
        val accounts = walletService.getAllAccounts()
        return ResponseEntity.ok(
            mapOf(
                "totalAccounts" to accounts.size,
                "accounts" to accounts.values
            )
        )
    }
}