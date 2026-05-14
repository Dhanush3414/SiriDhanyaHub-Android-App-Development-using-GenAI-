package com.siridhanya.hub.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.siridhanya.hub.R
import com.siridhanya.hub.databinding.ItemHealthBenefitBinding
import com.siridhanya.hub.models.HealthBenefit

class HealthAdapter : ListAdapter<HealthBenefit, HealthAdapter.HealthViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        HealthViewHolder(
            ItemHealthBenefitBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: HealthViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.alpha = 0f
        holder.itemView.translationX = -60f
        holder.itemView.animate()
            .alpha(1f).translationX(0f)
            .setDuration(400)
            .setStartDelay(position * 100L)
            .start()
    }

    inner class HealthViewHolder(private val b: ItemHealthBenefitBinding) :
        RecyclerView.ViewHolder(b.root) {

        fun bind(benefit: HealthBenefit) {
            b.apply {
                tvMilletName.text = benefit.milletName
                tvKannadaName.text = benefit.kannadaNameDisplay
                tvScientificName.text = benefit.scientificName
                tvTagline.text = "\"${benefit.tagline}\""
                tvWaterUsage.text = "💧 ${benefit.waterUsage}"
                tvCo2Saving.text = "🌱 ${benefit.co2Saving}"
                tvBenefits.text = benefit.benefits
                    .mapIndexed { i, b -> "  ${i + 1}. $b" }
                    .joinToString("\n")
                try {
                    cardHeader.setBackgroundColor(
                        Color.parseColor(benefit.primaryColor)
                    )
                } catch (e: Exception) {
                    cardHeader.setBackgroundColor(
                        b.root.context.getColor(R.color.colorPrimary)
                    )
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<HealthBenefit>() {
        override fun areItemsTheSame(a: HealthBenefit, b: HealthBenefit) =
            a.id == b.id
        override fun areContentsTheSame(a: HealthBenefit, b: HealthBenefit) =
            a == b
    }
}