package com.siridhanya.hub.utils

import android.content.Context
import android.content.SharedPreferences

class PrefsHelper(context: Context) {

    companion object {
        private const val PREFS_NAME = "siri_dhanya_prefs"
        private const val KEY_FAVORITES = "favorite_recipe_ids"
        private const val KEY_FIRST_LAUNCH = "first_launch"
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getFavoriteIds(): Set<String> =
        prefs.getStringSet(KEY_FAVORITES, emptySet()) ?: emptySet()

    fun isFavorite(recipeId: String): Boolean =
        getFavoriteIds().contains(recipeId)

    fun toggleFavorite(recipeId: String): Boolean {
        val favorites = getFavoriteIds().toMutableSet()
        val wasAdded = if (favorites.contains(recipeId)) {
            favorites.remove(recipeId)
            false
        } else {
            favorites.add(recipeId)
            true
        }
        prefs.edit().putStringSet(KEY_FAVORITES, favorites).apply()
        return wasAdded
    }

    fun isFirstLaunch(): Boolean =
        prefs.getBoolean(KEY_FIRST_LAUNCH, true)

    fun setFirstLaunchDone() =
        prefs.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply()

    fun resetFirstLaunch() {
        prefs.edit().putBoolean(KEY_FIRST_LAUNCH, true).apply()
    }
}