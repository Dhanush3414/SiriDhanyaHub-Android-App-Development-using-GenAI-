package com.siridhanya.hub.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.siridhanya.hub.databinding.FragmentAiRecipeBinding
import com.siridhanya.hub.utils.GeminiHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AiRecipeFragment : Fragment() {

    private var _binding: FragmentAiRecipeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAiRecipeBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtons()
    }

    private fun setupButtons() {

        // Generate Recipe
        binding.btnGenerateRecipe.setOnClickListener {
            val millets = getSelectedMillets()
            if (millets.isEmpty()) {
                showSnackbar("Please select at least one millet!")
                return@setOnClickListener
            }
            val mealType = when {
                binding.chipBreakfast.isChecked -> "Breakfast"
                binding.chipLunch.isChecked -> "Lunch"
                binding.chipDinner.isChecked -> "Dinner"
                binding.chipSnack.isChecked -> "Snack"
                binding.chipDessert.isChecked -> "Dessert"
                else -> "any"
            }
            generateRecipe(millets, mealType)
        }

        // Try Again
        binding.btnTryAgain.setOnClickListener {
            binding.cardAiResponse.visibility = View.GONE
            binding.btnGenerateRecipe.performClick()
        }

        // Share Recipe
        binding.ivShareRecipe.setOnClickListener {
            val text = binding.tvAiResponse.text.toString()
            if (text.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "🌾 AI Recipe from Siri-Dhanya Hub\n\n$text"
                    )
                }
                startActivity(
                    Intent.createChooser(intent, "Share Recipe")
                )
            }
        }

        // Ask Question
        binding.btnAskQuestion.setOnClickListener {
            val question = binding.etQuestion.text
                .toString().trim()
            if (question.isEmpty()) {
                showSnackbar("Please type a question first!")
                return@setOnClickListener
            }
            askQuestion(question)
        }
    }

    private fun getSelectedMillets(): List<String> {
        val millets = mutableListOf<String>()
        if (binding.chipRagi.isChecked) millets.add("Ragi")
        if (binding.chipNavane.isChecked) millets.add("Navane")
        if (binding.chipSajje.isChecked) millets.add("Sajje")
        if (binding.chipBaragu.isChecked) millets.add("Baragu")
        if (binding.chipOodalu.isChecked) millets.add("Oodalu")
        if (binding.chipKorralu.isChecked) millets.add("Korralu")
        return millets
    }

    private fun generateRecipe(
        millets: List<String>,
        mealType: String
    ) {
        showLoading(true)

        viewLifecycleOwner.lifecycleScope.launch {
            val recipe = withContext(Dispatchers.IO) {
                GeminiHelper.suggestRecipe(millets, mealType)
            }
            showLoading(false)
            binding.tvAiResponse.text = recipe
            binding.cardAiResponse.visibility = View.VISIBLE
            binding.cardAiResponse.alpha = 0f
            binding.cardAiResponse.animate()
                .alpha(1f).setDuration(500).start()
        }
    }

    private fun askQuestion(question: String) {
        binding.btnAskQuestion.isEnabled = false
        binding.btnAskQuestion.text = "⏳ Asking..."
        binding.tvAnswer.visibility = View.GONE

        val millet = getSelectedMillets()
            .firstOrNull() ?: "millets"

        viewLifecycleOwner.lifecycleScope.launch {
            val answer = withContext(Dispatchers.IO) {
                GeminiHelper.answerHealthQuestion(millet, question)
            }
            binding.btnAskQuestion.isEnabled = true
            binding.btnAskQuestion.text = "💬 Ask Gemini"
            binding.tvAnswer.text = "🤖 $answer"
            binding.tvAnswer.visibility = View.VISIBLE
            binding.tvAnswer.alpha = 0f
            binding.tvAnswer.animate()
                .alpha(1f).setDuration(400).start()
        }
    }

    private fun showLoading(loading: Boolean) {
        binding.layoutLoading.visibility =
            if (loading) View.VISIBLE else View.GONE
        binding.btnGenerateRecipe.isEnabled = !loading
        binding.btnGenerateRecipe.text =
            if (loading) "⏳ Generating..." else "✨ Generate AI Recipe"
        if (loading) {
            binding.cardAiResponse.visibility = View.GONE
        }
    }

    private fun showSnackbar(message: String) {
        com.google.android.material.snackbar.Snackbar.make(
            binding.root,
            message,
            com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}