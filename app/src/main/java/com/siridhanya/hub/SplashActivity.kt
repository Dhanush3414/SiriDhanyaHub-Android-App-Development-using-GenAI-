package com.siridhanya.hub

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.siridhanya.hub.databinding.ActivitySplashBinding
import com.siridhanya.hub.utils.FirebaseHelper
import com.siridhanya.hub.utils.PrefsHelper

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startAnimations()

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
        }, 3000L)
    }

    private fun startAnimations() {
        val logoAnim = AnimationSet(true).apply {
            addAnimation(ScaleAnimation(
                0f, 1f, 0f, 1f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f
            ).apply {
                duration = 800
                interpolator = AccelerateDecelerateInterpolator()
            })
            addAnimation(AlphaAnimation(0f, 1f).apply {
                duration = 800
            })
        }
        binding.ivLogo.startAnimation(logoAnim)

        val titleAnim = AnimationSet(true).apply {
            addAnimation(TranslateAnimation(
                0f, 0f, 80f, 0f
            ).apply {
                duration = 700
                startOffset = 500
                interpolator = AccelerateDecelerateInterpolator()
            })
            addAnimation(AlphaAnimation(0f, 1f).apply {
                duration = 700
                startOffset = 500
            })
        }
        binding.tvAppName.startAnimation(titleAnim)

        Handler(Looper.getMainLooper()).postDelayed({
            binding.tvSubtitle.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(600)
                .start()
            binding.progressBar.animate()
                .alpha(1f)
                .setDuration(400)
                .start()
        }, 1200)
    }
}