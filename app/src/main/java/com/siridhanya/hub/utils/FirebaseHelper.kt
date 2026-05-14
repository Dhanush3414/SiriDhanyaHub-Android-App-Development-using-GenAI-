package com.siridhanya.hub.utils

import android.os.Handler
import android.os.Looper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.siridhanya.hub.models.FpoContact
import com.siridhanya.hub.models.HealthBenefit
import com.siridhanya.hub.models.MandiPrice
import com.siridhanya.hub.models.Recipe
import java.util.concurrent.Executors

object FirebaseHelper {

    // Using default instance which uses the URL from google-services.json
    private val db = FirebaseDatabase.getInstance()
    private val executor = Executors.newSingleThreadExecutor()
    private val mainHandler = Handler(Looper.getMainLooper())

    // ── SMART SEED — Only if Firebase is empty ────────────
    fun seedSampleDataIfEmpty() {
        db.getReference("mandi_prices")
            .limitToFirst(1)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (!snapshot.exists()) {
                            // Firebase is empty — seed all data
                            seedMandiPrices()
                            seedRecipes()
                            seedHealthBenefits()
                            seedFpoContacts()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {}
                }
            )
    }

    // ── GET MANDI PRICES ──────────────────────────────────
    fun getMandiPrices(
        onSuccess: (List<MandiPrice>) -> Unit,
        onError: (String) -> Unit
    ) {
        db.getReference("mandi_prices")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Offload parsing to background thread to avoid ANR
                    executor.execute {
                        val prices = mutableListOf<MandiPrice>()
                        for (child in snapshot.children) {
                            try {
                                val history = child.child("history")
                                    .children
                                    .mapNotNull {
                                        it.getValue(Double::class.java)?.toFloat()
                                    }
                                prices.add(
                                    MandiPrice(
                                        id = child.key ?: "",
                                        milletType = child.child("milletType")
                                            .getValue(String::class.java) ?: "",
                                        city = child.child("city")
                                            .getValue(String::class.java) ?: "",
                                        currentPrice = child.child("currentPrice")
                                            .getValue(Long::class.java)?.toInt() ?: 0,
                                        unit = child.child("unit")
                                            .getValue(String::class.java) ?: "per quintal",
                                        trend = child.child("trend")
                                            .getValue(String::class.java) ?: "stable",
                                        weekHigh = child.child("weekHigh")
                                            .getValue(Long::class.java)?.toInt() ?: 0,
                                        weekLow = child.child("weekLow")
                                            .getValue(Long::class.java)?.toInt() ?: 0,
                                        history = history
                                    )
                                )
                            } catch (e: Exception) { }
                        }
                        val sorted = prices.sortedByDescending { it.currentPrice }
                        mainHandler.post { onSuccess(sorted) }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    onError(error.message)
                }
            })
    }

    // ── GET RECIPES ───────────────────────────────────────
    fun getRecipes(
        onSuccess: (List<Recipe>) -> Unit,
        onError: (String) -> Unit
    ) {
        db.getReference("recipes")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    executor.execute {
                        val recipes = mutableListOf<Recipe>()
                        for (child in snapshot.children) {
                            try {
                                val ingredients = child.child("ingredients")
                                    .children
                                    .mapNotNull { it.getValue(String::class.java) }
                                val steps = child.child("steps")
                                    .children
                                    .mapNotNull { it.getValue(String::class.java) }
                                val healthTags = child.child("healthTags")
                                    .children
                                    .mapNotNull { it.getValue(String::class.java) }
                                recipes.add(
                                    Recipe(
                                        id = child.key ?: "",
                                        name = child.child("name")
                                            .getValue(String::class.java) ?: "",
                                        kannada = child.child("kannada")
                                            .getValue(String::class.java) ?: "",
                                        milletType = child.child("milletType")
                                            .getValue(String::class.java) ?: "",
                                        category = child.child("category")
                                            .getValue(String::class.java) ?: "Breakfast",
                                        cookTime = child.child("cookTime")
                                            .getValue(String::class.java) ?: "30 minutes",
                                        servings = child.child("servings")
                                            .getValue(Long::class.java)?.toInt() ?: 4,
                                        healthTags = healthTags,
                                        imageUrl = child.child("imageUrl")
                                            .getValue(String::class.java) ?: "",
                                        ingredients = ingredients,
                                        steps = steps
                                    )
                                )
                            } catch (e: Exception) { }
                        }
                        mainHandler.post { onSuccess(recipes) }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    onError(error.message)
                }
            })
    }

    // ── GET HEALTH BENEFITS ───────────────────────────────
    fun getHealthBenefits(
        onSuccess: (List<HealthBenefit>) -> Unit,
        onError: (String) -> Unit
    ) {
        db.getReference("health_benefits")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    executor.execute {
                        val benefits = mutableListOf<HealthBenefit>()
                        for (child in snapshot.children) {
                            try {
                                val benefitsList = child.child("benefits")
                                    .children
                                    .mapNotNull { it.getValue(String::class.java) }
                                benefits.add(
                                    HealthBenefit(
                                        id = child.key ?: "",
                                        milletName = child.child("milletName")
                                            .getValue(String::class.java) ?: "",
                                        scientificName = child.child("scientificName")
                                            .getValue(String::class.java) ?: "",
                                        tagline = child.child("tagline")
                                            .getValue(String::class.java) ?: "",
                                        benefits = benefitsList,
                                        waterUsage = child.child("waterUsage")
                                            .getValue(String::class.java) ?: "",
                                        co2Saving = child.child("co2Saving")
                                            .getValue(String::class.java) ?: "",
                                        primaryColor = child.child("primaryColor")
                                            .getValue(String::class.java) ?: "#8B4513"
                                    )
                                )
                            } catch (e: Exception) { }
                        }
                        mainHandler.post { onSuccess(benefits) }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    onError(error.message)
                }
            })
    }

    // ── GET FPO CONTACTS ──────────────────────────────────
    fun getFpoContacts(
        onSuccess: (List<FpoContact>) -> Unit,
        onError: (String) -> Unit
    ) {
        db.getReference("fpo_contacts")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    executor.execute {
                        val contacts = mutableListOf<FpoContact>()
                        for (child in snapshot.children) {
                            try {
                                val millets = child.child("millets")
                                    .children
                                    .mapNotNull { it.getValue(String::class.java) }
                                contacts.add(
                                    FpoContact(
                                        id = child.key ?: "",
                                        name = child.child("name")
                                            .getValue(String::class.java) ?: "",
                                        location = child.child("location")
                                            .getValue(String::class.java) ?: "",
                                        phone = child.child("phone")
                                            .getValue(String::class.java) ?: "",
                                        millets = millets,
                                        rating = child.child("rating")
                                            .getValue(Double::class.java)
                                            ?.toFloat() ?: 0f,
                                        verified = child.child("verified")
                                            .getValue(Boolean::class.java) ?: false
                                    )
                                )
                            } catch (e: Exception) { }
                        }
                        mainHandler.post { onSuccess(contacts) }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    onError(error.message)
                }
            })
    }

    // ── SEED MANDI PRICES ─────────────────────────────────
    private fun seedMandiPrices() {
        val ref = db.reference
        val prices = mapOf(
            "ragi_bengaluru" to mapOf(
                "milletType" to "Ragi (Finger Millet)",
                "city" to "Bengaluru",
                "currentPrice" to 3800,
                "unit" to "per quintal",
                "trend" to "up",
                "weekHigh" to 3900, "weekLow" to 3500,
                "history" to listOf(3500,3600,3650,3700,3720,3780,3800)
            ),
            "ragi_davangere" to mapOf(
                "milletType" to "Ragi (Finger Millet)",
                "city" to "Davangere",
                "currentPrice" to 3700,
                "unit" to "per quintal",
                "trend" to "up",
                "weekHigh" to 3800, "weekLow" to 3400,
                "history" to listOf(3400,3450,3500,3550,3600,3650,3700)
            ),
            "ragi_mysuru" to mapOf(
                "milletType" to "Ragi (Finger Millet)",
                "city" to "Mysuru",
                "currentPrice" to 3650,
                "unit" to "per quintal",
                "trend" to "stable",
                "weekHigh" to 3750, "weekLow" to 3550,
                "history" to listOf(3650,3680,3700,3670,3660,3640,3650)
            ),
            "ragi_hubli" to mapOf(
                "milletType" to "Ragi (Finger Millet)",
                "city" to "Hubballi",
                "currentPrice" to 3500,
                "unit" to "per quintal",
                "trend" to "up",
                "weekHigh" to 3600, "weekLow" to 3200,
                "history" to listOf(3200,3250,3300,3350,3400,3450,3500)
            ),
            "ragi_shivamogga" to mapOf(
                "milletType" to "Ragi (Finger Millet)",
                "city" to "Shivamogga",
                "currentPrice" to 3600,
                "unit" to "per quintal",
                "trend" to "up",
                "weekHigh" to 3700, "weekLow" to 3300,
                "history" to listOf(3300,3380,3420,3470,3520,3560,3600)
            ),
            "ragi_tumakuru" to mapOf(
                "milletType" to "Ragi (Finger Millet)",
                "city" to "Tumakuru",
                "currentPrice" to 3550,
                "unit" to "per quintal",
                "trend" to "down",
                "weekHigh" to 3800, "weekLow" to 3500,
                "history" to listOf(3800,3750,3700,3650,3620,3580,3550)
            ),
            "ragi_hassan" to mapOf(
                "milletType" to "Ragi (Finger Millet)",
                "city" to "Hassan",
                "currentPrice" to 3480,
                "unit" to "per quintal",
                "trend" to "stable",
                "weekHigh" to 3600, "weekLow" to 3400,
                "history" to listOf(3500,3480,3490,3500,3470,3460,3480)
            ),
            "ragi_chitradurga" to mapOf(
                "milletType" to "Ragi (Finger Millet)",
                "city" to "Chitradurga",
                "currentPrice" to 3420,
                "unit" to "per quintal",
                "trend" to "up",
                "weekHigh" to 3500, "weekLow" to 3200,
                "history" to listOf(3200,3250,3280,3320,3360,3400,3420)
            ),
            "navane_davangere" to mapOf(
                "milletType" to "Navane (Foxtail Millet)",
                "city" to "Davangere",
                "currentPrice" to 3200,
                "unit" to "per quintal",
                "trend" to "up",
                "weekHigh" to 3400, "weekLow" to 2900,
                "history" to listOf(2900,3000,3100,3050,3150,3200,3200)
            ),
            "navane_gadag" to mapOf(
                "milletType" to "Navane (Foxtail Millet)",
                "city" to "Gadag",
                "currentPrice" to 3100,
                "unit" to "per quintal",
                "trend" to "up",
                "weekHigh" to 3200, "weekLow" to 2800,
                "history" to listOf(2800,2900,2950,3000,3020,3080,3100)
            ),
            "navane_dharwad" to mapOf(
                "milletType" to "Navane (Foxtail Millet)",
                "city" to "Dharwad",
                "currentPrice" to 3050,
                "unit" to "per quintal",
                "trend" to "stable",
                "weekHigh" to 3200, "weekLow" to 2900,
                "history" to listOf(3000,3050,3080,3060,3040,3050,3050)
            ),
            "navane_bellary" to mapOf(
                "milletType" to "Navane (Foxtail Millet)",
                "city" to "Ballari",
                "currentPrice" to 2950,
                "unit" to "per quintal",
                "trend" to "down",
                "weekHigh" to 3300, "weekLow" to 2900,
                "history" to listOf(3300,3200,3100,3050,3000,2970,2950)
            ),
            "navane_raichur" to mapOf(
                "milletType" to "Navane (Foxtail Millet)",
                "city" to "Raichur",
                "currentPrice" to 2850,
                "unit" to "per quintal",
                "trend" to "up",
                "weekHigh" to 3000, "weekLow" to 2600,
                "history" to listOf(2600,2680,2720,2760,2800,2830,2850)
            ),
            "sajje_bengaluru" to mapOf(
                "milletType" to "Sajje (Pearl Millet)",
                "city" to "Bengaluru",
                "currentPrice" to 2800,
                "unit" to "per quintal",
                "trend" to "down",
                "weekHigh" to 3100, "weekLow" to 2700,
                "history" to listOf(3100,3000,2950,2900,2850,2800,2800)
            ),
            "sajje_vijayapura" to mapOf(
                "milletType" to "Sajje (Pearl Millet)",
                "city" to "Vijayapura",
                "currentPrice" to 2750,
                "unit" to "per quintal",
                "trend" to "stable",
                "weekHigh" to 2900, "weekLow" to 2600,
                "history" to listOf(2700,2720,2750,2780,2760,2740,2750)
            ),
            "sajje_kalaburagi" to mapOf(
                "milletType" to "Sajje (Pearl Millet)",
                "city" to "Kalaburagi",
                "currentPrice" to 2650,
                "unit" to "per quintal",
                "trend" to "up",
                "weekHigh" to 2800, "weekLow" to 2400,
                "history" to listOf(2400,2480,2520,2560,2600,2630,2650)
            ),
            "baragu_mysuru" to mapOf(
                "milletType" to "Baragu (Proso Millet)",
                "city" to "Mysuru",
                "currentPrice" to 2600,
                "unit" to "per quintal",
                "trend" to "up",
                "weekHigh" to 2700, "weekLow" to 2400,
                "history" to listOf(2400,2450,2500,2520,2550,2580,2600)
            ),
            "baragu_mandya" to mapOf(
                "milletType" to "Baragu (Proso Millet)",
                "city" to "Mandya",
                "currentPrice" to 2550,
                "unit" to "per quintal",
                "trend" to "up",
                "weekHigh" to 2650, "weekLow" to 2300,
                "history" to listOf(2300,2350,2400,2430,2470,2520,2550)
            ),
            "oodalu_gadag" to mapOf(
                "milletType" to "Oodalu (Barnyard Millet)",
                "city" to "Gadag",
                "currentPrice" to 4200,
                "unit" to "per quintal",
                "trend" to "stable",
                "weekHigh" to 4300, "weekLow" to 4100,
                "history" to listOf(4100,4150,4200,4180,4200,4220,4200)
            ),
            "oodalu_haveri" to mapOf(
                "milletType" to "Oodalu (Barnyard Millet)",
                "city" to "Haveri",
                "currentPrice" to 4100,
                "unit" to "per quintal",
                "trend" to "up",
                "weekHigh" to 4200, "weekLow" to 3900,
                "history" to listOf(3900,3950,4000,4020,4050,4080,4100)
            ),
            "korralu_bengaluru" to mapOf(
                "milletType" to "Korralu (Little Millet)",
                "city" to "Bengaluru",
                "currentPrice" to 4500,
                "unit" to "per quintal",
                "trend" to "up",
                "weekHigh" to 4700, "weekLow" to 4200,
                "history" to listOf(4200,4280,4320,4380,4420,4460,4500)
            ),
            "saame_shivamogga" to mapOf(
                "milletType" to "Saame (Kodo Millet)",
                "city" to "Shivamogga",
                "currentPrice" to 3900,
                "unit" to "per quintal",
                "trend" to "up",
                "weekHigh" to 4000, "weekLow" to 3600,
                "history" to listOf(3600,3680,3720,3780,3820,3860,3900)
            ),
            "saame_mangaluru" to mapOf(
                "milletType" to "Saame (Kodo Millet)",
                "city" to "Mangaluru",
                "currentPrice" to 4100,
                "unit" to "per quintal",
                "trend" to "stable",
                "weekHigh" to 4200, "weekLow" to 3900,
                "history" to listOf(4100,4080,4120,4110,4090,4100,4100)
            )
        )
        ref.child("mandi_prices").setValue(prices)
    }

    // ── SEED RECIPES ──────────────────────────────────────
    private fun seedRecipes() {
        val ref = db.reference
        val recipes = mapOf(
            "ragi_mudde" to mapOf(
                "name" to "Ragi Mudde",
                "kannada" to "ರಾಗಿ ಮುದ್ದೆ",
                "milletType" to "Ragi",
                "category" to "Main Course",
                "cookTime" to "20 minutes",
                "servings" to 4,
                "healthTags" to listOf("Calcium Rich","Iron Boost","High Fiber"),
                "imageUrl" to "placeholder_recipe",
                "ingredients" to listOf(
                    "2 cups Ragi flour",
                    "3 cups water",
                    "1 tsp salt",
                    "1 tbsp ghee"
                ),
                "steps" to listOf(
                    "Bring water to boil with salt",
                    "Add ragi flour gradually while stirring",
                    "Stir continuously for 8-10 minutes",
                    "Cover and steam on low heat 5 minutes",
                    "Shape into balls with wet hands",
                    "Serve hot with sambar or saaru"
                )
            ),
            "ragi_dosa" to mapOf(
                "name" to "Ragi Dosa",
                "kannada" to "ರಾಗಿ ದೋಸೆ",
                "milletType" to "Ragi",
                "category" to "Breakfast",
                "cookTime" to "25 minutes",
                "servings" to 4,
                "healthTags" to listOf("Calcium Rich","Gluten Free","Weight Loss"),
                "imageUrl" to "placeholder_recipe",
                "ingredients" to listOf(
                    "1.5 cups Ragi flour",
                    "0.5 cup rice flour",
                    "1 cup buttermilk",
                    "2 green chillies chopped",
                    "1 tsp cumin seeds",
                    "Curry leaves",
                    "Salt to taste",
                    "Oil for cooking"
                ),
                "steps" to listOf(
                    "Mix ragi flour and rice flour",
                    "Add buttermilk and mix to smooth batter",
                    "Add green chillies, cumin, curry leaves",
                    "Rest batter for 30 minutes",
                    "Heat iron tawa on medium flame",
                    "Pour batter and spread in circles",
                    "Add oil on edges and cook till crisp",
                    "Serve with coconut chutney"
                )
            ),
            "ragi_laddu" to mapOf(
                "name" to "Ragi Laddu",
                "kannada" to "ರಾಗಿ ಲಾಡು",
                "milletType" to "Ragi",
                "category" to "Dessert",
                "cookTime" to "30 minutes",
                "servings" to 6,
                "healthTags" to listOf("Calcium Rich","Energy Boost","Kids Friendly"),
                "imageUrl" to "placeholder_recipe",
                "ingredients" to listOf(
                    "2 cups Ragi flour",
                    "1 cup jaggery powder",
                    "0.5 cup ghee",
                    "0.25 cup roasted groundnuts",
                    "2 tbsp sesame seeds",
                    "1 tsp cardamom powder"
                ),
                "steps" to listOf(
                    "Dry roast ragi flour 8-10 minutes",
                    "Cool and add jaggery powder",
                    "Add warm ghee gradually",
                    "Add cardamom, groundnuts, sesame",
                    "Shape into round balls while warm",
                    "Store in airtight container"
                )
            ),
            "navane_upma" to mapOf(
                "name" to "Navane Upma",
                "kannada" to "ನವಣೆ ಉಪ್ಮಾ",
                "milletType" to "Navane",
                "category" to "Breakfast",
                "cookTime" to "25 minutes",
                "servings" to 3,
                "healthTags" to listOf("Diabetic Friendly","Low GI","Weight Loss"),
                "imageUrl" to "placeholder_recipe",
                "ingredients" to listOf(
                    "1.5 cups Navane",
                    "1 onion sliced",
                    "2 green chillies",
                    "Curry leaves",
                    "1 tsp mustard seeds",
                    "2 tsp oil",
                    "Salt to taste"
                ),
                "steps" to listOf(
                    "Dry roast navane 3-4 minutes",
                    "Heat oil and splutter mustard seeds",
                    "Add curry leaves and chillies",
                    "Add onions and saute till soft",
                    "Add 3 cups water and salt",
                    "Add navane and stir well",
                    "Cook covered 15 minutes",
                    "Garnish and serve warm"
                )
            ),
            "navane_pulao" to mapOf(
                "name" to "Navane Vegetable Pulao",
                "kannada" to "ನವಣೆ ಪುಲಾವ್",
                "milletType" to "Navane",
                "category" to "Lunch",
                "cookTime" to "35 minutes",
                "servings" to 4,
                "healthTags" to listOf("Diabetic Friendly","One Pot Meal"),
                "imageUrl" to "placeholder_recipe",
                "ingredients" to listOf(
                    "2 cups Navane",
                    "1 cup mixed vegetables",
                    "1 onion sliced",
                    "1 tsp garam masala",
                    "3 cups water",
                    "2 tbsp oil",
                    "Salt to taste"
                ),
                "steps" to listOf(
                    "Soak navane 20 minutes",
                    "Fry onions in oil till golden",
                    "Add vegetables and masala",
                    "Add navane and water",
                    "Pressure cook 2 whistles",
                    "Serve with raita"
                )
            ),
            "sajje_rotti" to mapOf(
                "name" to "Sajje Rotti",
                "kannada" to "ಸಜ್ಜೆ ರೊಟ್ಟಿ",
                "milletType" to "Sajje",
                "category" to "Breakfast",
                "cookTime" to "30 minutes",
                "servings" to 4,
                "healthTags" to listOf("High Protein","Gluten Free"),
                "imageUrl" to "placeholder_recipe",
                "ingredients" to listOf(
                    "2 cups Sajje flour",
                    "Warm water as needed",
                    "Salt to taste",
                    "Ghee to serve"
                ),
                "steps" to listOf(
                    "Mix flour and salt",
                    "Add warm water to make soft dough",
                    "Pat on banana leaf to thin circle",
                    "Transfer to hot tawa",
                    "Cook both sides till brown",
                    "Serve with groundnut chutney"
                )
            ),
            "sajje_ambali" to mapOf(
                "name" to "Sajje Ambali",
                "kannada" to "ಸಜ್ಜೆ ಅಂಬಲಿ",
                "milletType" to "Sajje",
                "category" to "Breakfast",
                "cookTime" to "15 minutes",
                "servings" to 2,
                "healthTags" to listOf("High Protein","Summer Cooler"),
                "imageUrl" to "placeholder_recipe",
                "ingredients" to listOf(
                    "3 tbsp Sajje flour",
                    "2 cups water",
                    "1 cup buttermilk",
                    "Salt to taste",
                    "Cumin powder"
                ),
                "steps" to listOf(
                    "Mix sajje flour in cold water",
                    "Cook stirring continuously 5-7 minutes",
                    "Cool to room temperature",
                    "Add buttermilk and whisk",
                    "Add salt and cumin",
                    "Serve chilled"
                )
            ),
            "baragu_khichdi" to mapOf(
                "name" to "Baragu Khichdi",
                "kannada" to "ಬರಗು ಖಿಚಡಿ",
                "milletType" to "Baragu",
                "category" to "Lunch",
                "cookTime" to "35 minutes",
                "servings" to 4,
                "healthTags" to listOf("Detox","Gut Health","Healing"),
                "imageUrl" to "placeholder_recipe",
                "ingredients" to listOf(
                    "1 cup Baragu",
                    "0.5 cup yellow moong dal",
                    "1 tomato", "1 onion",
                    "2 tsp ghee",
                    "0.5 tsp turmeric",
                    "Salt to taste"
                ),
                "steps" to listOf(
                    "Wash baragu and dal 3 times",
                    "Saute onion and tomato in ghee",
                    "Add baragu, dal, turmeric, salt",
                    "Add 3 cups water",
                    "Pressure cook 3 whistles",
                    "Serve with yogurt"
                )
            ),
            "baragu_pongal" to mapOf(
                "name" to "Baragu Pongal",
                "kannada" to "ಬರಗು ಪೊಂಗಲ್",
                "milletType" to "Baragu",
                "category" to "Breakfast",
                "cookTime" to "30 minutes",
                "servings" to 3,
                "healthTags" to listOf("Detox","High Fiber","Traditional"),
                "imageUrl" to "placeholder_recipe",
                "ingredients" to listOf(
                    "1 cup Baragu",
                    "0.25 cup moong dal",
                    "2 tbsp ghee",
                    "1 tsp black pepper",
                    "Curry leaves",
                    "Cashews",
                    "Salt to taste"
                ),
                "steps" to listOf(
                    "Roast baragu and dal lightly",
                    "Cook with 3 cups water till soft",
                    "Heat ghee and fry pepper, cashews",
                    "Add curry leaves",
                    "Pour over baragu mixture",
                    "Serve with coconut chutney"
                )
            ),
            "oodalu_idli" to mapOf(
                "name" to "Oodalu Idli",
                "kannada" to "ಊದಲು ಇಡ್ಲಿ",
                "milletType" to "Oodalu",
                "category" to "Breakfast",
                "cookTime" to "40 minutes",
                "servings" to 4,
                "healthTags" to listOf("Iron Rich","Gluten Free","Probiotic"),
                "imageUrl" to "placeholder_recipe",
                "ingredients" to listOf(
                    "2 cups Oodalu",
                    "1 cup urad dal",
                    "1 tsp fenugreek seeds",
                    "Salt to taste"
                ),
                "steps" to listOf(
                    "Soak oodalu and fenugreek overnight",
                    "Soak urad dal 4 hours",
                    "Grind urad dal to fluffy batter",
                    "Grind oodalu to coarse batter",
                    "Mix with salt and ferment 8 hours",
                    "Steam in idli molds 12-15 minutes",
                    "Serve with sambar and chutney"
                )
            )
        )
        ref.child("recipes").setValue(recipes)
    }

    // ── SEED HEALTH BENEFITS ──────────────────────────────
    private fun seedHealthBenefits() {
        val ref = db.reference
        val health = mapOf(
            "ragi" to mapOf(
                "milletName" to "Ragi",
                "scientificName" to "Eleusine coracana",
                "tagline" to "The Calcium Powerhouse",
                "benefits" to listOf(
                    "Highest Calcium among all cereals",
                    "Natural coolant for Karnataka summers",
                    "Anti-aging amino acids - methionine and lysine",
                    "Controls bad cholesterol levels",
                    "Prevents anaemia with high iron",
                    "Excellent for children and pregnant women"
                ),
                "waterUsage" to "40% less water than paddy",
                "co2Saving" to "Saves 0.5 tons CO2 per acre",
                "primaryColor" to "#6D4C41"
            ),
            "navane" to mapOf(
                "milletName" to "Navane",
                "scientificName" to "Setaria italica",
                "tagline" to "The Diabetic Best Friend",
                "benefits" to listOf(
                    "Lowest Glycemic Index - controls blood sugar",
                    "Rich in Iron and Zinc",
                    "High dietary fiber improves digestion",
                    "Magnesium supports heart health",
                    "Helps in weight management",
                    "Reduces risk of Type 2 diabetes"
                ),
                "waterUsage" to "35% less water than paddy",
                "co2Saving" to "Saves 0.4 tons CO2 per acre",
                "primaryColor" to "#558B2F"
            ),
            "sajje" to mapOf(
                "milletName" to "Sajje",
                "scientificName" to "Pennisetum glaucum",
                "tagline" to "The Energy Powerhouse",
                "benefits" to listOf(
                    "Highest protein content among millets",
                    "Gluten-free - safe for celiac patients",
                    "Rich in B vitamins for energy",
                    "High phosphorus for bones and brain",
                    "Improves muscle strength",
                    "Traditional Ambali fights summer heat"
                ),
                "waterUsage" to "30% less water than paddy",
                "co2Saving" to "Saves 0.3 tons CO2 per acre",
                "primaryColor" to "#F57C00"
            ),
            "baragu" to mapOf(
                "milletName" to "Baragu",
                "scientificName" to "Panicum miliaceum",
                "tagline" to "The Gentle Detoxifier",
                "benefits" to listOf(
                    "Easiest to digest among millets",
                    "Excellent detox food - cleanses gut",
                    "High silicon - good for skin and hair",
                    "Alkaline nature reduces acidity",
                    "Ideal for sick patients and elderly",
                    "Prevents constipation with high fiber"
                ),
                "waterUsage" to "50% less water than paddy",
                "co2Saving" to "Saves 0.6 tons CO2 per acre",
                "primaryColor" to "#0277BD"
            ),
            "oodalu" to mapOf(
                "milletName" to "Oodalu",
                "scientificName" to "Echinochloa frumentacea",
                "tagline" to "The Fasting Food Champion",
                "benefits" to listOf(
                    "Highest Iron content - fights anaemia",
                    "Used in Hindu fasting traditions",
                    "Rich in B vitamins and fiber",
                    "Controls obesity and reduces weight",
                    "Good for liver health",
                    "Boosts immunity with antioxidants"
                ),
                "waterUsage" to "45% less water than paddy",
                "co2Saving" to "Saves 0.5 tons CO2 per acre",
                "primaryColor" to "#6A1B9A"
            ),
            "korralu" to mapOf(
                "milletName" to "Korralu",
                "scientificName" to "Panicum sumatrense",
                "tagline" to "The Tiny Nutrition Giant",
                "benefits" to listOf(
                    "Highest Iron and Zinc among millets",
                    "Excellent for pregnant women",
                    "Anti-inflammatory properties",
                    "Reduces cardiovascular disease risk",
                    "High antioxidants fight free radicals",
                    "Supports healthy nervous system"
                ),
                "waterUsage" to "55% less water than paddy",
                "co2Saving" to "Saves 0.7 tons CO2 per acre",
                "primaryColor" to "#00695C"
            )
        )
        health.forEach { (key, value) -> ref.child("health_benefits").child(key).setValue(value) }
    }

    // ── SEED FPO CONTACTS ─────────────────────────────────
    private fun seedFpoContacts() {
        val ref = db.reference
        val fpos = mapOf(
            "dharwad_fpo" to mapOf(
                "name" to "Dharwad Millet Farmers Collective",
                "location" to "Dharwad, Karnataka",
                "phone" to "+918360000001",
                "millets" to listOf("Navane","Sajje","Baragu"),
                "rating" to 4.5, "verified" to true
            ),
            "davangere_fpo" to mapOf(
                "name" to "Davangere Siri-Dhanya FPO",
                "location" to "Davangere, Karnataka",
                "phone" to "+918190000002",
                "millets" to listOf("Ragi","Navane","Oodalu"),
                "rating" to 4.8, "verified" to true
            ),
            "tumakuru_fpo" to mapOf(
                "name" to "Tumakuru Millet Growers Association",
                "location" to "Tumakuru, Karnataka",
                "phone" to "+918160000003",
                "millets" to listOf("Ragi","Korralu","Navane"),
                "rating" to 4.6, "verified" to true
            ),
            "mysuru_fpo" to mapOf(
                "name" to "Mysuru Organic Millet FPO",
                "location" to "Mysuru, Karnataka",
                "phone" to "+918210000004",
                "millets" to listOf("Ragi","Baragu","Oodalu"),
                "rating" to 4.7, "verified" to true
            ),
            "ballari_fpo" to mapOf(
                "name" to "Ballari Siri Dhanya Sangha",
                "location" to "Ballari, Karnataka",
                "phone" to "+918390000005",
                "millets" to listOf("Sajje","Navane","Baragu"),
                "rating" to 4.4, "verified" to true
            ),
            "shivamogga_fpo" to mapOf(
                "name" to "Shivamogga Saame Farmers Group",
                "location" to "Shivamogga, Karnataka",
                "phone" to "+918182000008",
                "millets" to listOf("Saame","Ragi","Baragu"),
                "rating" to 4.6, "verified" to true
            )
        )
        fpos.forEach { (key, value) -> ref.child("fpo_contacts").child(key).setValue(value) }
    }
}