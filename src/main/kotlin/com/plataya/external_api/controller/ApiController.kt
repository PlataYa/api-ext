package com.plataya.external_api.controller

import com.plataya.external_api.dto.CvuValidationResponseDTO
import com.plataya.external_api.repository.AccountRepository
import com.plataya.external_api.repository.TransactionRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class ApiController(
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository
) {
    @GetMapping("/valid/cvu")
    fun validateCvu(@RequestParam cvu: String): ResponseEntity<CvuValidationResponseDTO>{
        val exists = accountRepository.existsByCvu(cvu)
        return ResponseEntity.ok(CvuValidationResponseDTO(valid = exists))
    }
}