package com.siridhanya.hub.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.siridhanya.hub.R
import com.siridhanya.hub.RecipeDetailActivity
import com.siridhanya.hub.databinding.ItemRecipeCardBinding
import com.siridhanya.hub.models.Recipe
import com.siridhanya.hub.utils.PrefsHelper

class RecipeAdapter(
    private val prefs: PrefsHelper,
    private val onFavoriteToggle: (Recipe) -> Unit
) : ListAdapter<Recipe, RecipeAdapter.RecipeViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ItemRecipeCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.alpha = 0f
        holder.itemView.animate()
            .alpha(1f)
            .setDuration(300)
            .setStartDelay(position * 80L)
            .start()
    }

    inner class RecipeViewHolder(private val b: ItemRecipeCardBinding) :
        RecyclerView.ViewHolder(b.root) {

        fun bind(recipe: Recipe) {
            val ctx = b.root.context
            b.apply {
                tvRecipeName.text = recipe.name
                tvKannadaName.text = recipe.kannada
                tvMilletType.text = recipe.milletType
                tvMilletType.setBackgroundColor(recipe.milletColor)
                tvCookTime.text = "⏱ ${recipe.cookTime}"
                tvServings.text = "👤 ${recipe.servings} serves"
                tvCategory.text = "${recipe.categoryEmoji} ${recipe.category}"

                if (recipe.healthTags.isNotEmpty()) {
                    tvHealthTag.text = recipe.healthTags.first()
                    tvHealthTag.visibility = android.view.View.VISIBLE
                } else {
                    tvHealthTag.visibility = android.view.View.GONE
                }

                val isFav = prefs.isFavorite(recipe.id)
                ivFavorite.setImageResource(
                    if (isFav) R.drawable.ic_heart_filled
                    else R.drawable.ic_heart_outline
                )

                ivFavorite.setOnClickListener {
                    onFavoriteToggle(recipe)
                    it.animate()
                        .scaleX(1.3f).scaleY(1.3f).setDuration(150)
                        .withEndAction {
                            it.animate().scaleX(1f).scaleY(1f)
                                .setDuration(100).start()
                        }.start()
                    val nowFav = prefs.isFavorite(recipe.id)
                    ivFavorite.setImageResource(
                        if (nowFav) R.drawable.ic_heart_filled
                        else R.drawable.ic_heart_outline
                    )
                }

                val imageResId = ctx.resources.getIdentifier(
                    recipe.imageUrl, "drawable", ctx.packageName
                )
                Glide.with(ctx)
                    .load(if (imageResId != 0) imageResId
                    else R.drawable.placeholder_recipe)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_recipe)
                    .into(ivRecipeImage)

                root.setOnClickListener {
                    val intent = Intent(ctx, RecipeDetailActivity::class.java)
                    intent.putExtra("recipe", recipe)
                    ctx.startActivity(intent)
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe) =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe) =
            oldItem == newItem
    }
}