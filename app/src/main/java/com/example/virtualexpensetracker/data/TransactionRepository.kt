package com.example.virtualexpensetracker.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TransactionRepository(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    private fun getUserTransactionsKey(): String? {
        val userEmail = CurrentUser.email
        return if (userEmail != null) {
            "$TRANSACTIONS_KEY_PREFIX$userEmail"
        } else {
            null
        }
    }

    fun saveTransactions(transactions: List<Transaction>) {
        val userKey = getUserTransactionsKey() ?: return
        val json = gson.toJson(transactions)
        sharedPreferences.edit().putString(userKey, json).apply()
    }

    fun loadTransactions(): MutableList<Transaction> {
        val userKey = getUserTransactionsKey() ?: return mutableListOf()
        val json = sharedPreferences.getString(userKey, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<Transaction>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

    companion object {
        private const val PREFS_NAME = "ExpenseTrackerPrefs"
        private const val TRANSACTIONS_KEY_PREFIX = "transactions_"
    }
}
