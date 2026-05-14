package com.siridhanya.hub.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.siridhanya.hub.adapters.FpoAdapter
import com.siridhanya.hub.databinding.FragmentDirectBuyBinding
import com.siridhanya.hub.utils.FirebaseHelper

class DirectBuyFragment : Fragment() {

    private var _binding: FragmentDirectBuyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDirectBuyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = FpoAdapter { phone ->
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone")))
        }
        binding.rvFpo.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }
        binding.shimmerLayout.startShimmer()
        FirebaseHelper.getFpoContacts(
            onSuccess = { contacts ->
                if (isAdded) requireActivity().runOnUiThread {
                    adapter.submitList(contacts)
                    binding.shimmerLayout.stopShimmer()
                    binding.shimmerLayout.visibility = View.GONE
                    binding.rvFpo.visibility = View.VISIBLE
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