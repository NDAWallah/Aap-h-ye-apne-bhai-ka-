package com.example.ai

import com.example.BuildConfig
import com.example.model.ChatMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiChatService {

    // Model per user instructions: gemini-3.8-flash
    private const val MODEL_NAME = "gemini-3.8-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private const val SYSTEM_INSTRUCTION = """
You are DealBot, the official AI Shopping Assistant & Personal Stylist for DealDropz.
Your mission is to help shoppers discover viral fashion deals (Girls Kurtis, Alia cut, Chikankari, Boys Streetwear, Y2K cargo pants, acid-wash tees) and trending lifestyle gadgets.
You provide instant price comparison insights across Amazon, Flipkart, and Meesho, highlighting who has the lowest price and which secret coupon codes to apply.
You also know products by Reel Codes like #105 (Anouk Chikankari Kurti, ₹349 on Meesho), #108 (Alia Cut Anarkali, ₹479 on Meesho), #112 (Short Rayon Kurti Under ₹199), #204 (Streetwear Acid Wash Tee, ₹289 on Meesho), #208 (Tokyo Y2K Cargo, ₹499), and #301 (Sunset Lamp, ₹169).
Keep your tone stylish, friendly, upbeat, and concise with emojis.
"""

    suspend fun sendMessage(
        history: List<ChatMessage>,
        userMessage: String
    ): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }

        // Check if API key is provided and valid (not empty and not placeholder)
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext getSmartLocalResponse(userMessage)
        }

        try {
            val url = "$BASE_URL$MODEL_NAME:generateContent?key=$apiKey"

            val contentsArray = JSONArray()

            // Append past turns (up to last 10 messages for context)
            val recentHistory = history.takeLast(10)
            for (msg in recentHistory) {
                val role = if (msg.isUser) "user" else "model"
                val partObj = JSONObject().put("text", msg.text)
                val contentObj = JSONObject()
                    .put("role", role)
                    .put("parts", JSONArray().put(partObj))
                contentsArray.put(contentObj)
            }

            // Append current user message
            val currentPartObj = JSONObject().put("text", userMessage)
            val currentContentObj = JSONObject()
                .put("role", "user")
                .put("parts", JSONArray().put(currentPartObj))
            contentsArray.put(currentContentObj)

            // Build full payload with System Instruction & Generation Config
            val payload = JSONObject().apply {
                put("contents", contentsArray)

                val systemPart = JSONObject().put("text", SYSTEM_INSTRUCTION)
                val systemContent = JSONObject().put("parts", JSONArray().put(systemPart))
                put("systemInstruction", systemContent)

                val generationConfig = JSONObject().apply {
                    put("temperature", 0.7)
                    put("topP", 0.95)
                    put("maxOutputTokens", 800)
                }
                put("generationConfig", generationConfig)
            }

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = payload.toString().toRequestBody(mediaType)

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = okHttpClient.newCall(request).execute()
            val responseBody = response.body?.string()

            if (response.isSuccessful && !responseBody.isNullOrBlank()) {
                val json = JSONObject(responseBody)
                val candidates = json.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val content = candidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        return@withContext parts.getJSONObject(0).optString("text", "Here's what I found on DealDropz!")
                    }
                }
            }

            // If API responded with quota or error, fallback gracefully
            getSmartLocalResponse(userMessage)
        } catch (e: Exception) {
            getSmartLocalResponse(userMessage)
        }
    }

    private fun getSmartLocalResponse(query: String): String {
        val q = query.lowercase()
        return when {
            q.contains("105") || q.contains("chikankari") ->
                "✨ **Reel #105: Anouk Chikankari Georgette Kurti**\n" +
                "🏆 **Lowest Price**: ₹349 on **Meesho** (vs ₹499 Flipkart / ₹649 Amazon).\n" +
                "💡 **Secret Tip**: Meesho gives an extra ₹40 off on prepaid UPI payments!"

            q.contains("108") || q.contains("alia") || q.contains("anarkali") ->
                "👗 **Reel #108: Jaipur Alia-Cut Flared Anarkali Kurta Set**\n" +
                "🏆 **Lowest Price**: ₹479 on **Meesho** (vs ₹649 Flipkart / ₹799 Amazon).\n" +
                "💡 **Secret Tip**: Apply the ₹75 banner coupon on Flipkart if you have SuperCoins!"

            q.contains("199") || q.contains("under 199") ->
                "🔥 **Top Loot Under ₹199!**\n" +
                "Check out **Reel #112: Rayon Short Kurti** at just **₹189 on Meesho** (or 2-pack for ₹320) and **Reel #301: Sunset Lamp** at **₹169**!"

            q.contains("204") || q.contains("acid wash") || q.contains("tee") ->
                "🛹 **Reel #204: Oversized Acid-Wash Streetwear Tee (240 GSM)**\n" +
                "🏆 **Lowest Price**: ₹289 on **Meesho** (vs ₹449 Flipkart / ₹599 Amazon).\n" +
                "💡 **Secret Tip**: Super drop-shoulder fit, pairs amazingly with baggy cargo trousers!"

            q.contains("cargo") || q.contains("208") ->
                "👖 **Reel #208: Tokyo Y2K 6-Pocket Baggy Cargo Trousers**\n" +
                "🏆 **Lowest Price**: ₹499 on **Meesho** (vs ₹699 Flipkart / ₹999 Amazon).\n" +
                "💡 **Secret Tip**: Select the Olive Green color for an extra ₹50 discount!"

            q.contains("sunset") || q.contains("301") || q.contains("lamp") ->
                "🌅 **Reel #301: 16-Color RGB Sunset Projector Lamp**\n" +
                "🏆 **Lowest Price**: ₹169 on **Meesho** (vs ₹299 Flipkart / ₹349 Amazon).\n" +
                "💡 **Secret Tip**: Amazon's ₹349 listing includes an extra 1-year replacement warranty!"

            q.contains("cheapest") || q.contains("meesho") || q.contains("flipkart") || q.contains("amazon") ->
                "💡 **Platform Cheat Sheet on DealDropz**:\n" +
                "• **Meesho**: Almost always lowest for ethnic kurtis and viral TikTok fashion finds.\n" +
                "• **Flipkart**: Best for student fashion and SuperCoin instant discounts.\n" +
                "• **Amazon**: Best for electronic gadgets with fast Prime replacement."

            else ->
                "Hey there! I'm **DealBot**, your AI Personal Stylist & Loot Scout on DealDropz. 🛍️\n\n" +
                "Ask me about any viral outfit or Reel code (e.g. **#105 Chikankari Kurti**, **#108 Alia Cut**, **#204 Streetwear Tee**), " +
                "or say *'Find kurtis under ₹199'* and I'll find the cheapest prices across Amazon, Flipkart, and Meesho for you!"
        }
    }
}
