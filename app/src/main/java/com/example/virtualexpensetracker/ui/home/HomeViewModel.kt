package com.example.virtualexpensetracker.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.virtualexpensetracker.data.Transaction
import com.example.virtualexpensetracker.data.TransactionRepository

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TransactionRepository(application)
    private val _transactions = MutableLiveData<MutableList<Transaction>>()
    val transactions: LiveData<MutableList<Transaction>> = _transactions

    init {
        _transactions.value = repository.loadTransactions()
    }

    fun addTransaction(transaction: Transaction) {
        val list = _transactions.value ?: mutableListOf()
        list.add(0, transaction)
        _transactions.value = list
        repository.saveTransactions(list)
    }

    fun removeTransaction(transaction: Transaction) {
        val list = _transactions.value ?: return
        list.remove(transaction)
        _transactions.value = list
        repository.saveTransactions(list)
    }
}
