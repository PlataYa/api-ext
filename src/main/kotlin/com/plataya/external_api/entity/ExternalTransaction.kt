package com.plataya.external_api.entity
import jakarta.persistence.*
import java.util.*

@Entity
data class ExternalTransaction(
    @Id
    val id: UUID = UUID.randomUUID(),
    val fromCvu: String,
    val toCvu: String,
    val amount: Double
)