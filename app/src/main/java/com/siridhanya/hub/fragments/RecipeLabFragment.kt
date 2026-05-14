package com.siridhanya.hub.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.siridhanya.hub.R
import com.siridhanya.hub.adapters.RecipeAdapter
import com.siridhanya.hub.databinding.FragmentRecipeLabBinding
import com.siridhanya.hub.models.Recipe
import com.siridhanya.hub.utils.FirebaseHelper
import com.siridhanya.hub.utils.PrefsHelper

class RecipeLabFragment : Fragment() {

    private var _binding: FragmentRecipeLabBinding? = null
    private val binding get() = _binding!!
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var prefs: PrefsHelper
    private var allRecipes = listOf<Recipe>()
    private var selectedMillet = "All"
    private var showFavoritesOnly = false
    private var searchQuery = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeLabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = PrefsHelper(requireContext())
        setupRecyclerView()
        setupSearch()
        setupMilletFilter()
        setupFavoritesToggle()
        loadRecipes()
    }

    private fun setupRecyclerView() {
        recipeAdapter = RecipeAdapter(prefs) { recipe ->
            val added = prefs.toggleFavorite(recipe.id)
            val msg = if (added) "❤️ Saved!" else "Removed from Favourites"
            com.google.android.material.snackbar.Snackbar
                .make(binding.root, msg,
                    com.google.android.material.snackbar.Snackbar.LENGTH_SHORT)
                .show()
            if (showFavoritesOnly) applyFilters()
        }
        binding.rvRecipes.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = recipeAdapter
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                searchQuery = s?.toString() ?: ""
                binding.ivClearSearch.visibility =
                    if (searchQuery.isNotEmpty()) View.VISIBLE else View.GONE
                applyFilters()
            }
        })
        binding.ivClearSearch.setOnClickListener {
            binding.etSearch.text?.clear()
        }
    }

    private fun setupMilletFilter() {
        val millets = listOf("All","Ragi","Navane","Sajje","Baragu","Oodalu")
        binding.chipGroupMillet.removeAllViews()
        millets.forEach { millet ->
            val chip = com.google.android.material.chip.Chip(requireContext()).apply {
                text = if (millet == "All") "🌾 All" else millet
                isCheckable = true
                isChecked = millet == selectedMillet
            }
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) { selectedMillet = millet; applyFilters() }
            }
            binding.chipGroupMillet.addView(chip)
        }
    }

    private fun setupFavoritesToggle() {
        binding.btnFavorites.setOnClickListener {
            showFavoritesOnly = !showFavoritesOnly
            binding.btnFavorites.text =
                if (showFavoritesOnly) "❤️ Favourites" else "🤍 Favourites"
            applyFilters()
        }
    }

    private fun loadRecipes() {
        showShimmer(true)
        FirebaseHelper.getRecipes(
            onSuccess = { recipes ->
                if (isAdded) requireActivity().runOnUiThread {
                    allRecipes = recipes
                    showShimmer(false)
                    applyFilters()
                }
            },
            onError = {
                if (isAdded) requireActivity().runOnUiThread {
                    showShimmer(false)
                }
            }
        )
    }

    private fun applyFilters() {
        var filtered = allRecipes
        if (selectedMillet != "All")
            filtered = filtered.filter {
                it.milletType.equals(selectedMillet, ignoreCase = true)
            }
        if (searchQuery.isNotBlank())
            filtered = filtered.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                        it.kannada.contains(searchQuery, ignoreCase = true) ||
                        it.milletType.contains(searchQuery, ignoreCase = true)
            }
        if (showFavoritesOnly) {
            val favIds = prefs.getFavoriteIds()
            filtered = filtered.filter { favIds.contains(it.id) }
        }
        binding.tvRecipeCount.text = "${filtered.size} recipes"
        if (filtered.isEmpty()) {
            binding.layoutEmpty.visibility = View.VISIBLE
            binding.rvRecipes.visibility = View.GONE
        } else {
            binding.layoutEmpty.visibility = View.GONE
            binding.rvRecipes.visibility = View.VISIBLE
            recipeAdapter.submitList(filtered)
        }
    }

    private fun showShimmer(show: Boolean) {
        binding.shimmerLayout.visibility = if (show) View.VISIBLE else View.GONE
        binding.rvRecipes.visibility = if (show) View.GONE else View.VISIBLE
        if (show) binding.shimmerLayout.startShimmer()
        else binding.shimmerLayout.stopShimmer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}