package com.example.model

enum class PlatformType(val displayName: String) {
    AMAZON("Amazon"),
    FLIPKART("Flipkart"),
    MEESHO("Meesho")
}

/**
 * Core domain data class for DealDropz products.
 * Contains title, image URL, category, price map across platforms (Amazon/Flipkart/Meesho),
 * reel code, and star rating.
 */
data class Product(
    val id: String,
    val title: String,
    val imageUrl: String,
    val category: String,
    val priceMap: Map<PlatformType, Int>,
    val reelCode: String,
    val rating: Float = 4.5f,
    val reviewCount: Int = 1200,
    val amazonUrl: String = "https://www.amazon.in",
    val flipkartUrl: String = "https://www.flipkart.com",
    val meeshoUrl: String = "https://www.meesho.com",
    val dealTag: String = "🔥 Loot Price",
    val secretTip: String = "💡 Apply ₹50 instant coupon at checkout",
    val upvotes: Int = 142,
    val downvotes: Int = 8,
    val creatorName: String = "@fashionfinds_in",
    val reelAudioTrack: String = "Trending Audio • Viral Sound",
    val submittedBy: String? = null,
    val isPickedForYou: Boolean = false,
    val description: String = "Premium quality fabric, trending viral design as seen in top Instagram reels."
) {
    // Secondary constructor to maintain full compatibility with legacy parameter order
    constructor(
        id: String,
        title: String,
        reelCode: String,
        category: String,
        imageUrl: String,
        rating: Float = 4.5f,
        reviewCount: Int = 1200,
        amazonPrice: Int,
        flipkartPrice: Int,
        meeshoPrice: Int,
        amazonUrl: String = "https://www.amazon.in",
        flipkartUrl: String = "https://www.flipkart.com",
        meeshoUrl: String = "https://www.meesho.com",
        dealTag: String = "🔥 Loot Price",
        secretTip: String = "💡 Apply ₹50 instant coupon at checkout",
        upvotes: Int = 142,
        downvotes: Int = 8,
        creatorName: String = "@fashionfinds_in",
        reelAudioTrack: String = "Trending Audio • Viral Sound",
        submittedBy: String? = null,
        isPickedForYou: Boolean = false,
        description: String = "Premium quality fabric, trending viral design as seen in top Instagram reels."
    ) : this(
        id = id,
        title = title,
        imageUrl = imageUrl,
        category = category,
        priceMap = mapOf(
            PlatformType.AMAZON to amazonPrice,
            PlatformType.FLIPKART to flipkartPrice,
            PlatformType.MEESHO to meeshoPrice
        ),
        reelCode = reelCode,
        rating = rating,
        reviewCount = reviewCount,
        amazonUrl = amazonUrl,
        flipkartUrl = flipkartUrl,
        meeshoUrl = meeshoUrl,
        dealTag = dealTag,
        secretTip = secretTip,
        upvotes = upvotes,
        downvotes = downvotes,
        creatorName = creatorName,
        reelAudioTrack = reelAudioTrack,
        submittedBy = submittedBy,
        isPickedForYou = isPickedForYou,
        description = description
    )

    val amazonPrice: Int
        get() = priceMap[PlatformType.AMAZON] ?: 0

    val flipkartPrice: Int
        get() = priceMap[PlatformType.FLIPKART] ?: 0

    val meeshoPrice: Int
        get() = priceMap[PlatformType.MEESHO] ?: 0

    val pricesMap: Map<PlatformType, Int>
        get() = priceMap.filter { it.value > 0 }

    val lowestPlatformEntry: Pair<PlatformType, Int>?
        get() = pricesMap.minByOrNull { it.value }?.toPair()

    val lowestPrice: Int
        get() = lowestPlatformEntry?.second ?: 0

    val highestPrice: Int
        get() = pricesMap.maxOfOrNull { it.value } ?: lowestPrice

    val maxSavings: Int
        get() = (highestPrice - lowestPrice).coerceAtLeast(0)

    val savingsPercent: Int
        get() = if (highestPrice > 0) ((highestPrice - lowestPrice) * 100) / highestPrice else 0

    val hotPercentage: Int
        get() {
            val total = upvotes + downvotes
            return if (total > 0) (upvotes * 100) / total else 95
        }
}

data class UserSession(
    val isLoggedIn: Boolean = false,
    val userName: String = "Guest Shopper",
    val userEmail: String = "",
    val avatarEmoji: String = "🛍️",
    val isAdmin: Boolean = false
)

data class AdminDetails(
    val email: String = "parul301008@gmail.com",
    val name: String = "Parul",
    val role: String = "Super Admin & Store Owner",
    val phone: String = "+91 98765 43210",
    val storeName: String = "DealDropz Official",
    val amazonAssociateId: String = "dealdropz-21",
    val flipkartAffiliateId: String = "aff_dealdropz",
    val meeshoPartnerId: String = "meesho_parul"
)

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

enum class SubmissionStatus {
    PENDING, APPROVED, REJECTED
}

data class CommunitySubmission(
    val id: String,
    val title: String,
    val reelCode: String,
    val category: String,
    val amazonPrice: Int,
    val flipkartPrice: Int,
    val meeshoPrice: Int,
    val submittedBy: String,
    val secretTip: String,
    val status: SubmissionStatus = SubmissionStatus.PENDING,
    val timestamp: Long = System.currentTimeMillis()
)

data class ClickAnalytics(
    val amazonClicks: Int = 248,
    val flipkartClicks: Int = 389,
    val meeshoClicks: Int = 612,
    val whatsappShares: Int = 184,
    val reelsWatched: Int = 430
) {
    val totalClicks: Int
        get() = amazonClicks + flipkartClicks + meeshoClicks
}

data class PriceAlert(
    val productId: String,
    val targetPrice: Int,
    val isEnabled: Boolean = true
)
