package com.plataya.external_api.service

import com.plataya.external_api.model.entity.Account
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class InMemoryWalletService {
    
    private val accounts = ConcurrentHashMap<String, Account>()
    
    @EventListener(ApplicationReadyEvent::class)
    fun initializeAccounts() {
        // Initialize some test accounts with balances
        accounts["200000000001"] = Account("200000000001", 1500.75, "Banco Nación")
        accounts["200000000002"] = Account("200000000002", 2500.00, "Banco Galicia")
        accounts["200000000003"] = Account("200000000003", 750.25, "Banco Santander")
        accounts["200000000004"] = Account("200000000004", 50.00, "Banco BBVA")
        accounts["200000000005"] = Account("200000000005", 10000.00, "Banco Macro")
        accounts["200000000006"] = Account("200000000006", 5000.00, "External Bank")
        
        println("Initialized ${accounts.size} test accounts:")
        accounts.values.forEach { account ->
            println("CVU: ${account.cvu}, Balance: ${account.balance}, Bank: ${account.bankName}")
        }
    }
    
    fun findByCvu(cvu: String): Account? {
        return accounts[cvu]
    }
    
    fun deductBalance(cvu: String, amount: Double): Boolean {
        return accounts[cvu]?.let { account ->
            if (account.balance >= amount) {
                val updatedAccount = account.copy(balance = account.balance - amount)
                accounts[cvu] = updatedAccount
                true
            } else {
                false
            }
        } ?: false
    }
    
    fun addBalance(cvu: String, amount: Double): Boolean {
        return accounts[cvu]?.let { account ->
            val updatedAccount = account.copy(balance = account.balance + amount)
            accounts[cvu] = updatedAccount
            true
        } ?: false
    }
    
    fun getAllAccounts(): Map<String, Account> {
        return accounts.toMap()
    }
} 