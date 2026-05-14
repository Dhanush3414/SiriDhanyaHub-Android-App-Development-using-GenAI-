package com.siridhanya.hub.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.siridhanya.hub.adapters.HealthAdapter
import com.siridhanya.hub.databinding.FragmentHealthBinding
import com.siridhanya.hub.utils.FirebaseHelper

class HealthFragment : Fragment() {

    private var _binding: FragmentHealthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHealthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = HealthAdapter()
        binding.rvHealth.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }
        binding.shimmerLayout.startShimmer()
        FirebaseHelper.getHealthBenefits(
            onSuccess = { benefits ->
                if (isAdded) requireActivity().runOnUiThread {
                    adapter.submitList(benefits)
                    binding.shimmerLayout.stopShimmer()
                    binding.shimmerLayout.visibility = View.GONE
                    binding.rvHealth.visibility = View.VISIBLE
                }
            },
            onError = {
                if (isAdded) requireActivity().runOnUiThread {
                    binding.shimmerLayout.stopShimmer()
                    binding.shimmerLayout.visibility = View.GONE
                }
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}