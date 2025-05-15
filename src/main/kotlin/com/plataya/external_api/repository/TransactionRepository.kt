package com.plataya.external_api.repository
import com.plataya.external_api.entity.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface TransactionRepository : JpaRepository<Transaction, UUID> {

}