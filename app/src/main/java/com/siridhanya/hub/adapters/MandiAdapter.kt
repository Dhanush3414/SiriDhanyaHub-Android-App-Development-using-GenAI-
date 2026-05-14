package com.siridhanya.hub.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.siridhanya.hub.R
import com.siridhanya.hub.databinding.ItemMandiPriceBinding
import com.siridhanya.hub.models.MandiPrice

class MandiAdapter : ListAdapter<MandiPrice, MandiAdapter.MandiViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MandiViewHolder {
        val binding = ItemMandiPriceBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MandiViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MandiViewHolder, position: Int) {
        holder.bind(getItem(position))
        
        // Only animate if it's the first time the view is being shown
        if (holder.itemView.alpha == 0f) {
            holder.itemView.alpha = 0f
            holder.itemView.translationY = 40f
            holder.itemView.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(300)
                .setStartDelay((position % 10) * 50L) // Cap delay
                .start()
        }
    }

    inner class MandiViewHolder(private val b: ItemMandiPriceBinding) :
        RecyclerView.ViewHolder(b.root) {

        fun bind(price: MandiPrice) {
            b.apply {
                tvMilletName.text = price.milletType
                tvMilletEmoji.text = price.milletEmoji
                tvCity.text = "📍 ${price.city}  •  ${price.cityKannada}"
                tvCurrentPrice.text = "₹${price.currentPrice}"
                tvUnit.text = "/ quintal"
                tvWeekHigh.text = "₹${price.weekHigh}"
                tvWeekLow.text = "₹${price.weekLow}"

                val ctx = root.context
                when (price.trend) {
                    "up" -> {
                        tvTrend.text = "↑ Rising"
                        tvTrend.setTextColor(ctx.getColor(R.color.colorTrendUp))
                        tvTrend.setBackgroundColor(ctx.getColor(R.color.colorTrendUpBg))
                        tvCurrentPrice.setTextColor(ctx.getColor(R.color.colorTrendUp))
                    }
                    "down" -> {
                        tvTrend.text = "↓ Falling"
                        tvTrend.setTextColor(ctx.getColor(R.color.colorTrendDown))
                        tvTrend.setBackgroundColor(ctx.getColor(R.color.colorTrendDownBg))
                        tvCurrentPrice.setTextColor(ctx.getColor(R.color.colorTrendDown))
                    }
                    else -> {
                        tvTrend.text = "→ Stable"
                        tvTrend.setTextColor(ctx.getColor(R.color.colorTrendStable))
                        tvTrend.setBackgroundColor(ctx.getColor(R.color.colorTrendStableBg))
                        tvCurrentPrice.setTextColor(ctx.getColor(R.color.colorPrimary))
                    }
                }

                if (price.history.isNotEmpty()) {
                    setupSparkline(price)
                    sparklineChart.visibility = View.VISIBLE
                } else {
                    sparklineChart.visibility = View.GONE
                }
            }
        }

        private fun setupSparkline(price: MandiPrice) {
            val entries = price.history.mapIndexed { i, v ->
                Entry(i.toFloat(), v)
            }
            val ctx = b.root.context
            val lineColor = when (price.trend) {
                "up" -> ctx.getColor(R.color.colorTrendUp)
                "down" -> ctx.getColor(R.color.colorTrendDown)
                else -> ctx.getColor(R.color.colorAccent)
            }
            val dataSet = LineDataSet(entries, "7-Day").apply {
                color = lineColor
                lineWidth = 2f
                setDrawCircles(false)
                setDrawValues(false)
                setDrawFilled(true)
                fillColor = lineColor
                fillAlpha = 30
                mode = LineDataSet.Mode.CUBIC_BEZIER
            }
            b.sparklineChart.apply {
                data = LineData(dataSet)
                description.isEnabled = false
                legend.isEnabled = false
                setTouchEnabled(false)
                setDrawGridBackground(false)
                setDrawBorders(false)
                axisLeft.isEnabled = false
                axisRight.isEnabled = false
                xAxis.isEnabled = false
                setBackgroundColor(Color.TRANSPARENT)
                invalidate()
                // Removed animateX(800) from here as it's too heavy for scroll
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MandiPrice>() {
        override fun areItemsTheSame(oldItem: MandiPrice, newItem: MandiPrice) =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: MandiPrice, newItem: MandiPrice) =
            oldItem == newItem
    }
}