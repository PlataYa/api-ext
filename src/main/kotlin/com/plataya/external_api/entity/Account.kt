package com.plataya.external_api.entity
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.*

@Entity
data class Account (
    @Id
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val balance: Double,
)