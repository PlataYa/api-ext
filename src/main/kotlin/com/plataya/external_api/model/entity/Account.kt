package com.plataya.external_api.model.entity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.*

@Entity
data class Account (
    @Id
    @Column(unique = true, nullable = false)
    val cvu: String,
    val balance: Double,
    val bankName: String
)