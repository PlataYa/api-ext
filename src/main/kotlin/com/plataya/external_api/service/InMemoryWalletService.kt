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
        accounts["111"] = Account("111", 1500.75, "Banco Nación")
        accounts["222"] = Account("222", 2500.00, "Banco Galicia")
        accounts["333"] = Account("333", 750.25, "Banco Santander")
        accounts["444"] = Account("444", 50.00, "Banco BBVA")
        accounts["555"] = Account("555", 1000000000.00, "Banco Macro")
        accounts["999"] = Account("999", 5000.00, "External Bank")
        
        println("Initialized ${accounts.size} test accounts:")
        accounts.values.forEach { account ->
            println("CVU: ${account.cvu}, Balance: ${account.balance}, Bank: ${account.bankName}")
        }
    }
    
    fun findByCvu(cvu: String): Account? {
        return accounts[cvu]
    }
    
    fun existsByCvu(cvu: String): Boolean {
        return accounts.containsKey(cvu)
    }
    
    fun updateBalance(cvu: String, newBalance: Double): Account? {
        val account = accounts[cvu]
        return if (account != null) {
            val updatedAccount = account.copy(balance = newBalance)
            accounts[cvu] = updatedAccount
            updatedAccount
        } else {
            null
        }
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