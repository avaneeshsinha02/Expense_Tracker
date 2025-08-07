package com.example.virtualexpensetracker.data

data class Transaction(
    val name: String,
    val category: String,
    val amount: Double,
    val date: String
)
