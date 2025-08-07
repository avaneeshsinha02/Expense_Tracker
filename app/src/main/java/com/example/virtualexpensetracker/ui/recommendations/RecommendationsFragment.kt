package com.example.virtualexpensetracker.ui.recommendations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.virtualexpensetracker.databinding.FragmentRecommendationsBinding
import kotlin.random.Random

class RecommendationsFragment : Fragment() {

    private var _binding: FragmentRecommendationsBinding? = null
    private val binding get() = _binding!!

    private val recommendationMessages = listOf(
        "You've been spending a lot on 'Shopping'. Consider setting a monthly budget for it!",
        "Great job keeping your 'Transport' costs low this month!",
        "Did you know you could save up to ₹1000 a month by brewing your own coffee?",
        "Consider reviewing your subscriptions to find potential savings.",
        "Your spending on 'Food' is consistent. Try looking for weekly deals to save more!",
        "Planning a no-spend weekend can be a fun challenge and a great way to save money."
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecommendationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val randomIndex = Random.nextInt(recommendationMessages.size)
        binding.recommendationText.text = recommendationMessages[randomIndex]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
