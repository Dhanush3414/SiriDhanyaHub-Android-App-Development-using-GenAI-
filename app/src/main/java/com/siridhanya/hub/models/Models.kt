package com.siridhanya.hub.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(
    val id: String = "",
    val name: String = "",
    val kannada: String = "",
    val milletType: String = "",
    val category: String = "Breakfast",
    val cookTime: String = "30 minutes",
    val servings: Int = 4,
    val healthTags: List<String> = emptyList(),
    val imageUrl: String = "",
    val ingredients: List<String> = emptyList(),
    val steps: List<String> = emptyList(),
    var isFavorite: Boolean = false
) : Parcelable {

    val categoryEmoji: String get() = when (category.lowercase()) {
        "breakfast" -> "🌅"
        "main course", "lunch", "dinner" -> "🍽️"
        "snack" -> "🫙"
        "dessert" -> "🍮"
        else -> "🌾"
    }

    val milletColor: Int get() = when (milletType.lowercase()) {
        "ragi" -> 0xFF6D4C41.toInt()
        "navane" -> 0xFF558B2F.toInt()
        "sajje" -> 0xFFF57C00.toInt()
        "baragu" -> 0xFF0277BD.toInt()
        "oodalu" -> 0xFF6A1B9A.toInt()
        else -> 0xFF8B4513.toInt()
    }
}

@Parcelize
data class HealthBenefit(
    val id: String = "",
    val milletName: String = "",
    val kannadaName: String = "",
    val scientificName: String = "",
    val tagline: String = "",
    val benefits: List<String> = emptyList(),
    val waterUsage: String = "",
    val co2Saving: String = "",
    val primaryColor: String = "#8B4513"
) : Parcelable {

    val kannadaNameDisplay: String get() = when (milletName.lowercase()) {
        "ragi" -> "ರಾಗಿ"
        "navane" -> "ನವಣೆ"
        "sajje" -> "ಸಜ್ಜೆ"
        "baragu" -> "ಬರಗು"
        "oodalu" -> "ಊದಲು"
        else -> milletName
    }
}

@Parcelize
data class FpoContact(
    val id: String = "",
    val name: String = "",
    val location: String = "",
    val phone: String = "",
    val millets: List<String> = emptyList(),
    val rating: Float = 0f,
    val verified: Boolean = false
) : Parcelable