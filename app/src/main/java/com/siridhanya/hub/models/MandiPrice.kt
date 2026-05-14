package com.siridhanya.hub.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MandiPrice(
    val id: String = "",
    val milletType: String = "",
    val city: String = "",
    val currentPrice: Int = 0,
    val unit: String = "per quintal",
    val trend: String = "stable",
    val weekHigh: Int = 0,
    val weekLow: Int = 0,
    val history: List<Float> = emptyList(),
    val lastUpdated: Long = System.currentTimeMillis()
) : Parcelable {

    val priceChange: Int get() = if (history.size >= 2)
        currentPrice - history[history.size - 2].toInt() else 0

    val isTrendUp: Boolean get() = trend == "up"
    val isTrendDown: Boolean get() = trend == "down"
    val isStable: Boolean get() = trend == "stable"

    val cityKannada: String get() = when (city.lowercase()) {
        "davangere" -> "ದಾವಣಗೆರೆ"
        "bengaluru", "bangalore" -> "ಬೆಂಗಳೂರು"
        "mysuru", "mysore" -> "ಮೈಸೂರು"
        "hubballi", "hubli" -> "ಹುಬ್ಬಳ್ಳಿ"
        "gadag" -> "ಗದಗ"
        else -> city
    }

    val milletEmoji: String get() = when {
        milletType.contains("Ragi", ignoreCase = true) -> "🌾"
        milletType.contains("Navane", ignoreCase = true) -> "🌿"
        milletType.contains("Sajje", ignoreCase = true) -> "🌻"
        milletType.contains("Baragu", ignoreCase = true) -> "🫘"
        milletType.contains("Oodalu", ignoreCase = true) -> "🌱"
        else -> "🌾"
    }
}