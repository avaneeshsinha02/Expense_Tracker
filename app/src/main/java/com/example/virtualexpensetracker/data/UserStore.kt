package com.example.virtualexpensetracker.data

import android.content.Context
import android.content.SharedPreferences

object UserStore {
    private const val PREFS_NAME = "UserPrefs"
    private const val USERS_KEY = "registered_users"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun addUser(context: Context, email: String) {
        val prefs = getPrefs(context)
        val users = prefs.getStringSet(USERS_KEY, mutableSetOf()) ?: mutableSetOf()
        users.add(email.lowercase())
        prefs.edit().putStringSet(USERS_KEY, users).apply()
    }

    fun isUserRegistered(context: Context, email: String): Boolean {
        val prefs = getPrefs(context)
        val users = prefs.getStringSet(USERS_KEY, emptySet()) ?: emptySet()
        return users.contains(email.lowercase())
    }
}
