package com.siridhanya.hub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.siridhanya.hub.databinding.ActivityMainBinding
import com.siridhanya.hub.fragments.AiRecipeFragment
import com.siridhanya.hub.fragments.DirectBuyFragment
import com.siridhanya.hub.fragments.HealthFragment
import com.siridhanya.hub.fragments.MandiWatchFragment
import com.siridhanya.hub.fragments.RecipeLabFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mandiFragment by lazy { MandiWatchFragment() }
    private val recipeFragment by lazy { RecipeLabFragment() }
    private val aiFragment by lazy { AiRecipeFragment() }
    private val healthFragment by lazy { HealthFragment() }
    private val buyFragment by lazy { DirectBuyFragment() }

    private var activeFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Only load the first fragment initially to save resources and prevent ANR
        if (savedInstanceState == null) {
            setupInitialFragment()
        }
        setupBottomNav()
    }

    private fun setupInitialFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, mandiFragment, "mandi")
            .commit()
        activeFragment = mandiFragment
    }

    private fun setupBottomNav() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            val target = when (item.itemId) {
                R.id.nav_mandi -> mandiFragment
                R.id.nav_recipes -> recipeFragment
                R.id.nav_ai -> aiFragment
                R.id.nav_health -> healthFragment
                R.id.nav_buy -> buyFragment
                else -> return@setOnItemSelectedListener false
            }
            switchFragment(target, item.itemId.toString())
            true
        }
    }

    private fun switchFragment(target: Fragment, tag: String) {
        if (target == activeFragment) return
        
        val transaction = supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )

        // Hide current
        activeFragment?.let { transaction.hide(it) }

        // Show or Add target
        if (target.isAdded) {
            transaction.show(target)
        } else {
            transaction.add(R.id.fragmentContainer, target, tag)
        }

        transaction.commit()
        activeFragment = target
    }

    override fun onBackPressed() {
        if (activeFragment != mandiFragment) {
            binding.bottomNav.selectedItemId = R.id.nav_mandi
        } else {
            super.onBackPressed()
        }
    }
}