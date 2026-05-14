package com.siridhanya.hub

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.siridhanya.hub.databinding.ActivityRecipeDetailBinding
import com.siridhanya.hub.models.Recipe
import com.siridhanya.hub.utils.PrefsHelper

class RecipeDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecipeDetailBinding
    private lateinit var prefs: PrefsHelper
    private var recipe: Recipe? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefs = PrefsHelper(this)
        recipe = intent.getParcelableExtra("recipe")
        setupToolbar()
        recipe?.let { bindRecipe(it) }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = ""
        }
    }

    private fun bindRecipe(r: Recipe) {
        binding.apply {
            val imageResId = resources.getIdentifier(
                r.imageUrl, "drawable", packageName
            )
            Glide.with(this@RecipeDetailActivity)
                .load(if (imageResId != 0) imageResId
                else R.drawable.placeholder_recipe)
                .centerCrop()
                .into(ivHeroImage)

            tvRecipeName.text = r.name
            tvKannadaName.text = r.kannada
            tvMilletType.text = r.milletType
            tvMilletType.setBackgroundColor(r.milletColor)
            tvCategory.text = "${r.categoryEmoji} ${r.category}"
            tvCookTime.text = r.cookTime
            tvServings.text = "${r.servings} servings"

            val tagsText = r.healthTags.joinToString("  •  ")
            tvHealthTags.text = tagsText
            tvHealthTags.visibility =
                if (tagsText.isNotEmpty()) View.VISIBLE
                else View.GONE

            tvIngredients.text = r.ingredients
                .joinToString("\n") { "  • $it" }

            tvSteps.text = r.steps
                .mapIndexed { i, step -> "  ${i + 1}.  $step" }
                .joinToString("\n\n")

            val isFav = prefs.isFavorite(r.id)
            fabFavorite.setImageResource(
                if (isFav) R.drawable.ic_heart_filled
                else R.drawable.ic_heart_outline
            )

            fabFavorite.setOnClickListener {
                val added = prefs.toggleFavorite(r.id)
                fabFavorite.setImageResource(
                    if (added) R.drawable.ic_heart_filled
                    else R.drawable.ic_heart_outline
                )
                fabFavorite.animate()
                    .scaleX(1.2f).scaleY(1.2f)
                    .setDuration(120)
                    .withEndAction {
                        fabFavorite.animate()
                            .scaleX(1f).scaleY(1f)
                            .setDuration(100).start()
                    }.start()

                val msg = if (added) "❤️ Recipe saved!"
                else "Removed from favourites"
                com.google.android.material.snackbar.Snackbar
                    .make(binding.root, msg,
                        com.google.android.material.snackbar
                            .Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
        binding.scrollView.alpha = 0f
        binding.scrollView.animate()
            .alpha(1f).setDuration(500)
            .setStartDelay(200).start()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}