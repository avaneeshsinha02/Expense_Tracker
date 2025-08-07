package com.example.virtualexpensetracker.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.virtualexpensetracker.data.Transaction
import com.example.virtualexpensetracker.databinding.DialogAddTransactionBinding
import java.text.SimpleDateFormat
import java.util.*

class AddTransactionDialogFragment : DialogFragment() {

    private var _binding: DialogAddTransactionBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCategorySpinner()

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        binding.addButton.setOnClickListener {
            addTransaction()
        }
    }

    private fun setupCategorySpinner() {
        val categories = arrayOf("Food", "Travel", "Bills", "Fun", "Shopping", "Other")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = adapter
    }

    private fun addTransaction() {
        val name = binding.nameEditText.text.toString()
        val amountStr = binding.amountEditText.text.toString()
        val category = binding.categorySpinner.selectedItem.toString()

        if (name.isBlank() || amountStr.isBlank()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val amount = amountStr.toDouble()
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = sdf.format(Date())

            val transaction = Transaction(name, category, amount, date)
            homeViewModel.addTransaction(transaction)
            dismiss()
        } catch (e: NumberFormatException) {
            Toast.makeText(requireContext(), "Please enter a valid amount", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
