package com.siridhanya.hub.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.siridhanya.hub.adapters.MandiAdapter
import com.siridhanya.hub.databinding.FragmentMandiWatchBinding
import com.siridhanya.hub.models.MandiPrice
import com.siridhanya.hub.utils.FirebaseHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MandiWatchFragment : Fragment() {

    private var _binding: FragmentMandiWatchBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MandiAdapter
    private var allPrices = listOf<MandiPrice>()
    private var selectedCity = "All Cities"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMandiWatchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSwipeRefresh()
        loadPrices()
    }

    private fun setupRecyclerView() {
        adapter = MandiAdapter()
        binding.rvMandiPrices.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@MandiWatchFragment.adapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            loadPrices()
        }
    }

    private fun loadPrices() {
        showShimmer(true)
        FirebaseHelper.getMandiPrices(
            onSuccess = { prices ->
                if (isAdded) requireActivity().runOnUiThread {
                    allPrices = prices
                    showShimmer(false)
                    binding.swipeRefresh.isRefreshing = false
                    setupCityFilter(prices)
                    filterPrices()
                    updateSummaryCard(prices)
                    updateTimestamp()
                }
            },
            onError = {
                if (isAdded) requireActivity().runOnUiThread {
                    showShimmer(false)
                    binding.swipeRefresh.isRefreshing = false
                }
            }
        )
    }

    private fun setupCityFilter(prices: List<MandiPrice>) {
        val cities = mutableListOf("All Cities")
        cities.addAll(prices.map { it.city }.distinct().sorted())
        val chipGroup = binding.chipGroupCity
        chipGroup.removeAllViews()
        cities.forEach { city ->
            val chip = com.google.android.material.chip.Chip(requireContext()).apply {
                text = city
                isCheckable = true
                isChecked = city == selectedCity
            }
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedCity = city
                    filterPrices()
                }
            }
            chipGroup.addView(chip)
        }
    }

    private fun filterPrices() {
        val filtered = if (selectedCity == "All Cities") allPrices
        else allPrices.filter { it.city == selectedCity }
        if (filtered.isEmpty()) {
            binding.layoutEmpty.visibility = View.VISIBLE
            binding.rvMandiPrices.visibility = View.GONE
        } else {
            binding.layoutEmpty.visibility = View.GONE
            binding.rvMandiPrices.visibility = View.VISIBLE
            adapter.submitList(filtered)
        }
    }

    private fun updateSummaryCard(prices: List<MandiPrice>) {
        if (prices.isEmpty()) return
        val highest = prices.maxByOrNull { it.currentPrice }
        val lowest = prices.minByOrNull { it.currentPrice }
        binding.tvHighestPrice.text = "₹${highest?.currentPrice ?: 0}"
        binding.tvHighestMillet.text = highest?.milletType?.split(" ")?.first() ?: ""
        binding.tvHighestCity.text = highest?.city ?: ""
        binding.tvLowestPrice.text = "₹${lowest?.currentPrice ?: 0}"
        binding.tvLowestMillet.text = lowest?.milletType?.split(" ")?.first() ?: ""
        binding.tvLowestCity.text = lowest?.city ?: ""
        binding.tvTotalMarkets.text = "${prices.map { it.city }.distinct().size}"
        binding.tvTotalMillets.text = "${prices.map { it.milletType }.distinct().size}"
    }

    private fun updateTimestamp() {
        val formatter = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
        binding.tvLastUpdated.text = "Updated: ${formatter.format(Date())}"
    }

    private fun showShimmer(show: Boolean) {
        binding.shimmerLayout.visibility = if (show) View.VISIBLE else View.GONE
        binding.rvMandiPrices.visibility = if (show) View.GONE else View.VISIBLE
        if (show) binding.shimmerLayout.startShimmer()
        else binding.shimmerLayout.stopShimmer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}