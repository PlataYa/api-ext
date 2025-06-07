package com.plataya.external_api.service

import com.plataya.external_api.model.dto.PlataYaDepositRequest
import com.plataya.external_api.model.dto.PlataYaDepositResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class PlataYaService(
    private val restTemplate: RestTemplate,
    @Value("\${plataya.backend.url}")
    private val plataYaBackendUrl: String
) {
    
    fun processDeposit(request: PlataYaDepositRequest): PlataYaDepositResponse {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        
        val entity = HttpEntity(request, headers)
        val url = "$plataYaBackendUrl/api/v1/transaction/deposit"
        
        return restTemplate.postForObject(url, entity, PlataYaDepositResponse::class.java)
            ?: throw RuntimeException("Failed to process deposit with PlataYa backend")
    }
} 