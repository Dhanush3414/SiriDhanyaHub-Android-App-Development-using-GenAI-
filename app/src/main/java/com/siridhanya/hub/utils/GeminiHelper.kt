package com.siridhanya.hub.utils

import com.siridhanya.hub.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

object GeminiHelper {

    private const val MODEL = "gemini-2.5-flash"
    private val API_KEY get() = BuildConfig.GEMINI_API_KEY

    private suspend fun callGemini(prompt: String): String =
        withContext(Dispatchers.IO) {
            try {
                val url = URL(
                    "https://generativelanguage.googleapis.com/v1beta/models/$MODEL:generateContent?key=$API_KEY"
                )

                val connection = url.openConnection() as HttpURLConnection
                connection.apply {
                    requestMethod = "POST"
                    setRequestProperty("Content-Type", "application/json")
                    doOutput = true
                    connectTimeout = 30000
                    readTimeout = 30000
                }

                // Build request JSON
                val requestBody = JSONObject().apply {
                    put("contents", JSONArray().apply {
                        put(JSONObject().apply {
                            put("parts", JSONArray().apply {
                                put(JSONObject().apply {
                                    put("text", prompt)
                                })
                            })
                        })
                    })
                }

                // Send request
                OutputStreamWriter(connection.outputStream).use {
                    it.write(requestBody.toString())
                    it.flush()
                }

                // Read response
                val responseCode = connection.responseCode
                val reader = if (responseCode == 200) {
                    BufferedReader(InputStreamReader(connection.inputStream))
                } else {
                    BufferedReader(InputStreamReader(connection.errorStream))
                }

                val response = StringBuilder()
                reader.use { br ->
                    br.lines().forEach { line ->
                        response.append(line)
                    }
                }

                if (responseCode == 200) {
                    // Parse response
                    val jsonResponse = JSONObject(response.toString())
                    jsonResponse
                        .getJSONArray("candidates")
                        .getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0)
                        .getString("text")
                } else {
                    "Error $responseCode: ${response.toString()}"
                }

            } catch (e: Exception) {
                "Error: ${e.message}"
            }
        }

    // ── RECIPE SUGGESTION ─────────────────────────────────
    suspend fun suggestRecipe(
        availableMillets: List<String>,
        mealType: String = "any"
    ): String {
        val prompt = """
            You are a Karnataka millet cuisine expert.
            Available millets: ${availableMillets.joinToString(", ")}
            Meal type wanted: $mealType
            
            Suggest ONE simple Karnataka millet recipe with:
            1. Recipe name in English and Kannada
            2. Why this millet is healthy (2 points)
            3. Simple ingredients list
            4. Step by step cooking method (5-6 steps)
            5. One health tip
            
            Keep it friendly and simple for Karnataka home cooks.
        """.trimIndent()

        return callGemini(prompt)
    }

    // ── HEALTH QUESTION ────────────────────────────────────
    suspend fun answerHealthQuestion(
        milletName: String,
        question: String
    ): String {
        val prompt = """
            You are a nutrition expert for Karnataka millets.
            Millet: $milletName
            Question: $question
            
            Answer in simple English within 100 words.
            Focus on Karnataka and Indian health context.
        """.trimIndent()

        return callGemini(prompt)
    }

    // ── PRICE ANALYSIS ─────────────────────────────────────
    suspend fun analyzePriceTrend(
        milletType: String,
        currentPrice: Int,
        weekHigh: Int,
        weekLow: Int,
        trend: String,
        city: String
    ): String {
        val prompt = """
            Agricultural market expert for Karnataka, India.
            Millet: $milletType, City: $city
            Price: Rs.$currentPrice, High: Rs.$weekHigh, Low: Rs.$weekLow
            Trend: $trend
            
            Give 3 sentence analysis:
            1. Good time to sell?
            2. Next week prediction  
            3. Farmer advice
        """.trimIndent()

        return callGemini(prompt)
    }
}