package com.example.virtualexpensetracker.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.virtualexpensetracker.data.Transaction
import com.example.virtualexpensetracker.databinding.ListItemTransactionBinding

class TransactionAdapter(private var transactions: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding =
            ListItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    override fun getItemCount() = transactions.size

    fun updateData(newTransactions: List<Transaction>) {
        transactions = newTransactions
        notifyDataSetChanged()
    }

    fun getTransactionAt(position: Int): Transaction {
        return transactions[position]
    }

    class TransactionViewHolder(private val binding: ListItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: Transaction) {
            binding.transactionName.text = transaction.name
            binding.transactionCategory.text = transaction.category
            binding.transactionAmount.text = String.format("-₹%.0f", transaction.amount)
        }
    }
}
