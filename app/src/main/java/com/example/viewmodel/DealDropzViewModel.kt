package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SampleData
import com.example.model.ClickAnalytics
import com.example.model.CommunitySubmission
import com.example.model.PlatformType
import com.example.model.Product
import com.example.model.SubmissionStatus
import com.example.model.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class DealDropzViewModel : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(SampleData.initialProducts)
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _userSession = MutableStateFlow(UserSession())
    val userSession: StateFlow<UserSession> = _userSession.asStateFlow()

    private val _wishlistIds = MutableStateFlow<Set<String>>(setOf("dd_105", "dd_204"))
    val wishlistIds: StateFlow<Set<String>> = _wishlistIds.asStateFlow()

    private val _votedMap = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val votedMap: StateFlow<Map<String, Boolean>> = _votedMap.asStateFlow()

    private val _priceAlerts = MutableStateFlow<Map<String, Int>>(mapOf("dd_105" to 300))
    val priceAlerts: StateFlow<Map<String, Int>> = _priceAlerts.asStateFlow()

    private val _communitySubmissions = MutableStateFlow<List<CommunitySubmission>>(SampleData.initialSubmissions)
    val communitySubmissions: StateFlow<List<CommunitySubmission>> = _communitySubmissions.asStateFlow()

    private val _clickAnalytics = MutableStateFlow(ClickAnalytics())
    val clickAnalytics: StateFlow<ClickAnalytics> = _clickAnalytics.asStateFlow()

    private val _activeReelProduct = MutableStateFlow<Product?>(null)
    val activeReelProduct: StateFlow<Product?> = _activeReelProduct.asStateFlow()

    private val _activePriceAlertProduct = MutableStateFlow<Product?>(null)
    val activePriceAlertProduct: StateFlow<Product?> = _activePriceAlertProduct.asStateFlow()

    private val _activeReportProduct = MutableStateFlow<Product?>(null)
    val activeReportProduct: StateFlow<Product?> = _activeReportProduct.asStateFlow()

    private val _isSpotDealOpen = MutableStateFlow(false)
    val isSpotDealOpen: StateFlow<Boolean> = _isSpotDealOpen.asStateFlow()

    private val _isLoginDialogOpen = MutableStateFlow(false)
    val isLoginDialogOpen: StateFlow<Boolean> = _isLoginDialogOpen.asStateFlow()

    private val _isAdminMode = MutableStateFlow(false)
    val isAdminMode: StateFlow<Boolean> = _isAdminMode.asStateFlow()

    private val _isAdminAuthenticated = MutableStateFlow(false)
    val isAdminAuthenticated: StateFlow<Boolean> = _isAdminAuthenticated.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<com.example.model.ChatMessage>>(
        listOf(
            com.example.model.ChatMessage(
                text = "Hey! I'm DealBot, your AI Personal Stylist & Loot Scout 🛍️ Ask me about any viral Kurti, Streetwear tee, or Reel code (e.g. #105, #204) to see the lowest price across Amazon, Flipkart & Meesho!",
                isUser = false
            )
        )
    )
    val chatMessages: StateFlow<List<com.example.model.ChatMessage>> = _chatMessages.asStateFlow()

    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading: StateFlow<Boolean> = _isChatLoading.asStateFlow()

    private val _isChatOpen = MutableStateFlow(false)
    val isChatOpen: StateFlow<Boolean> = _isChatOpen.asStateFlow()

    private val _adminDetails = MutableStateFlow(com.example.model.AdminDetails())
    val adminDetails: StateFlow<com.example.model.AdminDetails> = _adminDetails.asStateFlow()

    companion object {
        const val ADMIN_EMAIL = "parul301008@gmail.com"
        const val ADMIN_PASSWORD = "Parul@2008"
    }

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    // Filtered products list based on search (by title or reel code e.g. #105) and quick category
    val filteredProducts: StateFlow<List<Product>> = combine(
        _products,
        _searchQuery,
        _selectedCategory
    ) { allProducts, query, category ->
        var list = allProducts

        // Category filter
        when (category) {
            "All" -> {}
            "Girls Kurtis" -> list = list.filter { it.category == "Girls Kurtis" }
            "Boys Wear" -> list = list.filter { it.category == "Boys Wear" }
            "Viral Reels" -> list = list.filter { it.category == "Viral Reels" }
            "Under ₹199" -> list = list.filter { it.lowestPrice <= 199 }
            "Under ₹499" -> list = list.filter { it.lowestPrice <= 499 }
            else -> list = list.filter { it.category.equals(category, ignoreCase = true) }
        }

        // Search filter (name or reel code)
        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            list = list.filter {
                it.title.lowercase().contains(q) ||
                it.reelCode.lowercase().contains(q) ||
                it.category.lowercase().contains(q) ||
                (it.submittedBy?.lowercase()?.contains(q) == true)
            }
        }

        list
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SampleData.initialProducts)

    val pickedForYouProducts: StateFlow<List<Product>> = _products.combine(_userSession) { all, session ->
        all.filter { it.isPickedForYou }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val wishlistProducts: StateFlow<List<Product>> = combine(
        _products,
        _wishlistIds
    ) { all, ids ->
        all.filter { ids.contains(it.id) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    fun toggleWishlist(product: Product) {
        val current = _wishlistIds.value.toMutableSet()
        if (current.contains(product.id)) {
            current.remove(product.id)
            showToast("Removed '${product.title.take(20)}...' from Wishlist")
        } else {
            current.add(product.id)
            showToast("❤️ Saved '${product.title.take(20)}...' to Wishlist!")
        }
        _wishlistIds.value = current
    }

    fun voteProduct(productId: String, isHot: Boolean) {
        val existingVote = _votedMap.value[productId]
        if (existingVote == isHot) return // already voted same

        _votedMap.value = _votedMap.value + (productId to isHot)
        _products.value = _products.value.map { item ->
            if (item.id == productId) {
                if (isHot) {
                    item.copy(
                        upvotes = item.upvotes + 1,
                        downvotes = if (existingVote == false) (item.downvotes - 1).coerceAtLeast(0) else item.downvotes
                    )
                } else {
                    item.copy(
                        downvotes = item.downvotes + 1,
                        upvotes = if (existingVote == true) (item.upvotes - 1).coerceAtLeast(0) else item.upvotes
                    )
                }
            } else {
                item
            }
        }
        showToast(if (isHot) "🔥 Voted HOT! Deal rating updated." else "❄️ Voted NOT! Feedback saved.")
    }

    fun recordAffiliateClick(platform: PlatformType, product: Product) {
        val current = _clickAnalytics.value
        _clickAnalytics.value = when (platform) {
            PlatformType.AMAZON -> current.copy(amazonClicks = current.amazonClicks + 1)
            PlatformType.FLIPKART -> current.copy(flipkartClicks = current.flipkartClicks + 1)
            PlatformType.MEESHO -> current.copy(meeshoClicks = current.meeshoClicks + 1)
        }
        showToast("Redirecting to ${platform.displayName} with affiliate deal...")
    }

    fun recordWhatsAppShare(product: Product) {
        _clickAnalytics.value = _clickAnalytics.value.copy(
            whatsappShares = _clickAnalytics.value.whatsappShares + 1
        )
    }

    fun openReelModal(product: Product) {
        _clickAnalytics.value = _clickAnalytics.value.copy(
            reelsWatched = _clickAnalytics.value.reelsWatched + 1
        )
        _activeReelProduct.value = product
    }

    fun closeReelModal() {
        _activeReelProduct.value = null
    }

    fun openPriceAlert(product: Product) {
        _activePriceAlertProduct.value = product
    }

    fun closePriceAlert() {
        _activePriceAlertProduct.value = null
    }

    fun setPriceAlert(productId: String, targetPrice: Int) {
        _priceAlerts.value = _priceAlerts.value + (productId to targetPrice)
        _activePriceAlertProduct.value = null
        showToast("🔔 Price drop alert set for ₹$targetPrice! We'll notify you.")
    }

    fun openReportModal(product: Product) {
        _activeReportProduct.value = product
    }

    fun closeReportModal() {
        _activeReportProduct.value = null
    }

    fun submitPriceReport(productId: String, reason: String) {
        _activeReportProduct.value = null
        showToast("✅ Thank you! Report submitted. Our deal scouts will verify.")
    }

    fun setSpotDealOpen(open: Boolean) {
        _isSpotDealOpen.value = open
    }

    fun submitCommunityDeal(
        title: String,
        reelCode: String,
        category: String,
        amazonPrice: Int,
        flipkartPrice: Int,
        meeshoPrice: Int,
        secretTip: String
    ) {
        val userName = if (_userSession.value.isLoggedIn) _userSession.value.userName else "deal_scout_${(100..999).random()}"
        val submission = CommunitySubmission(
            id = "sub_${UUID.randomUUID().toString().take(6)}",
            title = title,
            reelCode = if (reelCode.startsWith("#")) reelCode else "#$reelCode",
            category = category,
            amazonPrice = amazonPrice,
            flipkartPrice = flipkartPrice,
            meeshoPrice = meeshoPrice,
            submittedBy = userName,
            secretTip = secretTip,
            status = SubmissionStatus.PENDING
        )
        _communitySubmissions.value = listOf(submission) + _communitySubmissions.value
        _isSpotDealOpen.value = false
        showToast("🎉 Deal submitted for review! Credited to @$userName")
    }

    fun approveSubmission(sub: CommunitySubmission) {
        _communitySubmissions.value = _communitySubmissions.value.map {
            if (it.id == sub.id) it.copy(status = SubmissionStatus.APPROVED) else it
        }
        val newProduct = Product(
            id = "dd_comm_${sub.id}",
            title = sub.title,
            reelCode = sub.reelCode,
            category = sub.category,
            imageUrl = "https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?auto=format&fit=crop&w=800&q=80",
            amazonPrice = sub.amazonPrice,
            flipkartPrice = sub.flipkartPrice,
            meeshoPrice = sub.meeshoPrice,
            dealTag = "🔥 Community Loot",
            secretTip = sub.secretTip,
            submittedBy = sub.submittedBy
        )
        _products.value = listOf(newProduct) + _products.value
        showToast("Approved! Deal '${sub.title.take(20)}...' published to feed.")
    }

    fun rejectSubmission(sub: CommunitySubmission) {
        _communitySubmissions.value = _communitySubmissions.value.map {
            if (it.id == sub.id) it.copy(status = SubmissionStatus.REJECTED) else it
        }
        showToast("Deal submission rejected.")
    }

    fun addNewProduct(product: Product) {
        _products.value = listOf(product) + _products.value
        showToast("Product added successfully!")
    }

    fun deleteProduct(productId: String) {
        _products.value = _products.value.filter { it.id != productId }
        showToast("Product deleted.")
    }

    fun setLoginDialogOpen(open: Boolean) {
        _isLoginDialogOpen.value = open
    }

    fun signInWithGoogle() {
        _userSession.value = UserSession(
            isLoggedIn = true,
            userName = "Priya Sharma",
            userEmail = "priya.sharma@gmail.com",
            avatarEmoji = "✨"
        )
        _isLoginDialogOpen.value = false
        showToast("Signed in as Priya Sharma via Google 1-Click!")
    }

    fun signInWithEmail(name: String, email: String) {
        val formattedName = if (name.isNotBlank()) name else "Smart Shopper"
        _userSession.value = UserSession(
            isLoggedIn = true,
            userName = formattedName,
            userEmail = if (email.isNotBlank()) email else "shopper@dealdropz.com",
            avatarEmoji = "👗"
        )
        _isLoginDialogOpen.value = false
        showToast("Welcome back, $formattedName!")
    }

    fun signOut() {
        _userSession.value = UserSession()
        showToast("Signed out successfully.")
    }

    fun loginAdmin(email: String, pass: String): Boolean {
        val trimmedEmail = email.trim()
        if (trimmedEmail.equals(ADMIN_EMAIL, ignoreCase = true) && pass == ADMIN_PASSWORD) {
            _isAdminAuthenticated.value = true
            _isAdminMode.value = true
            _userSession.value = UserSession(
                isLoggedIn = true,
                userName = "Parul (Admin)",
                userEmail = ADMIN_EMAIL,
                avatarEmoji = "👑",
                isAdmin = true
            )
            showToast("👑 Verified! Welcome, Parul. Admin controls unlocked.")
            return true
        } else {
            showToast("❌ Incorrect Admin credentials! Check email or password.")
            return false
        }
    }

    fun logoutAdmin() {
        _isAdminAuthenticated.value = false
        _isAdminMode.value = false
        if (_userSession.value.isAdmin) {
            _userSession.value = UserSession()
        }
        showToast("Admin session locked.")
    }

    fun updateAdminDetails(details: com.example.model.AdminDetails) {
        _adminDetails.value = details
        showToast("Admin details updated successfully.")
    }

    fun setChatOpen(open: Boolean) {
        _isChatOpen.value = open
    }

    fun sendChatMessage(text: String) {
        if (text.isBlank()) return
        val userMsg = com.example.model.ChatMessage(text = text.trim(), isUser = true)
        val currentHistory = _chatMessages.value
        _chatMessages.value = currentHistory + userMsg
        _isChatLoading.value = true

        viewModelScope.launch {
            val reply = com.example.ai.GeminiChatService.sendMessage(currentHistory, text.trim())
            _chatMessages.value = _chatMessages.value + com.example.model.ChatMessage(text = reply, isUser = false)
            _isChatLoading.value = false
        }
    }

    fun clearChat() {
        _chatMessages.value = listOf(
            com.example.model.ChatMessage(
                text = "Chat history refreshed! What viral loot, fashion advice, or Reel code can I help you find today? ✨",
                isUser = false
            )
        )
    }

    fun showToast(message: String) {
        _toastMessage.value = message
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}
