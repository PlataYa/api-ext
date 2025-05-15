package com.plataya.external_api.repository
import com.plataya.external_api.entity.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AccountRepository : JpaRepository<Account, UUID> {
    fun existsByCvu(cvu: String): Boolean
    fun findByCvu(cvu: String): Account?
}