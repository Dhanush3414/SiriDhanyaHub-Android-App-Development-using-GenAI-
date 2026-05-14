package com.siridhanya.hub.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.siridhanya.hub.databinding.ItemFpoContactBinding
import com.siridhanya.hub.models.FpoContact

class FpoAdapter(
    private val onCallClick: (String) -> Unit
) : ListAdapter<FpoContact, FpoAdapter.FpoViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FpoViewHolder(
            ItemFpoContactBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: FpoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FpoViewHolder(private val b: ItemFpoContactBinding) :
        RecyclerView.ViewHolder(b.root) {

        fun bind(fpo: FpoContact) {
            b.apply {
                tvFpoName.text = fpo.name
                tvLocation.text = "📍 ${fpo.location}"
                tvPhone.text = fpo.phone
                tvMillets.text = fpo.millets.joinToString(" • ")
                tvRating.text = "⭐ ${fpo.rating}"
                tvVerified.visibility =
                    if (fpo.verified) android.view.View.VISIBLE
                    else android.view.View.GONE
                btnCall.setOnClickListener {
                    onCallClick(fpo.phone)
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<FpoContact>() {
        override fun areItemsTheSame(a: FpoContact, b: FpoContact) =
            a.id == b.id
        override fun areContentsTheSame(a: FpoContact, b: FpoContact) =
            a == b
    }
}