package com.example.virtualexpensetracker.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.example.virtualexpensetracker.data.Transaction
import com.example.virtualexpensetracker.databinding.FragmentHomeBinding
import com.example.virtualexpensetracker.workers.BudgetReminderWorker
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var transactionAdapter: TransactionAdapter
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupPieChart()
        observeViewModel()
        setupSwipeToDelete()
        scheduleBudgetReminders()

        binding.fab.setOnClickListener {
            AddTransactionDialogFragment().show(parentFragmentManager, "AddTransactionDialog")
        }
    }

    private fun setupRecyclerView() {
        transactionAdapter = TransactionAdapter(emptyList())
        binding.transactionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = transactionAdapter
        }
    }

    private fun setupPieChart() {
        binding.pieChart.apply {
            description.isEnabled = false
            isDrawHoleEnabled = true
            setHoleColor(Color.WHITE)
            setTransparentCircleColor(Color.WHITE)
            setCenterTextSize(16f)
            legend.isEnabled = false
            isRotationEnabled = true
            setDrawEntryLabels(false)
            setExtraOffsets(50f, 10f, 50f, 10f)
        }
    }

    private fun observeViewModel() {
        homeViewModel.transactions.observe(viewLifecycleOwner) { transactions ->
            transactionAdapter.updateData(transactions)
            updatePieChart(transactions)
        }
    }

    private fun updatePieChart(transactions: List<Transaction>) {
        if (transactions.isEmpty()) {
            binding.pieChart.clear()
            binding.pieChart.centerText = "No expenses yet"
            binding.pieChart.invalidate()
            return
        }

        val spendingByCategory = transactions.groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount } }

        val entries = ArrayList<PieEntry>()
        spendingByCategory.forEach { (category, total) ->
            entries.add(PieEntry(total.toFloat(), category))
        }

        val dataSet = PieDataSet(entries, "Expense Categories")

        dataSet.setDrawValues(true)
        dataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        dataSet.valueLinePart1OffsetPercentage = 100f
        dataSet.valueLinePart1Length = 0.6f
        dataSet.valueLinePart2Length = 0.6f
        dataSet.valueLineColor = Color.DKGRAY

        val colors = ArrayList<Int>()
        colors.add(Color.parseColor("#455A64")) // Blue Grey
        colors.add(Color.parseColor("#D32F2F")) // Red
        colors.add(Color.parseColor("#388E3C")) // Green
        colors.add(Color.parseColor("#FBC02D")) // Yellow
        colors.add(Color.parseColor("#7B1FA2")) // Purple
        colors.add(Color.parseColor("#C2185B")) // Pink
        colors.add(Color.parseColor("#00796B")) // Teal
        colors.add(Color.parseColor("#E64A19")) // Deep Orange
        dataSet.colors = colors

        dataSet.sliceSpace = 3f

        val data = PieData(dataSet)
        data.setValueTextSize(12f)
        data.setValueTextColor(Color.BLACK)

        data.setValueFormatter(object : ValueFormatter() {
            override fun getPieLabel(value: Float, pieEntry: PieEntry?): String {
                return "${pieEntry?.label} (${value.toInt()})"
            }
        })


        binding.pieChart.data = data
        binding.pieChart.centerText = "Spending by Category"
        binding.pieChart.invalidate()
    }

    private fun setupSwipeToDelete() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val transaction = transactionAdapter.getTransactionAt(position)
                homeViewModel.removeTransaction(transaction)
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.transactionsRecyclerView)
    }

    private fun scheduleBudgetReminders() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        val periodicWorkRequest = PeriodicWorkRequestBuilder<BudgetReminderWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            "BudgetReminder",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
