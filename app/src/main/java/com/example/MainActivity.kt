package com.example

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.PlatformType
import com.example.model.Product
import com.example.ui.components.DealDropzTopBar
import com.example.ui.components.GeminiChatDialog
import com.example.ui.components.LoginDialog
import com.example.ui.components.PriceDropDialog
import com.example.ui.components.ReelPreviewDialog
import com.example.ui.components.ReportDialog
import com.example.ui.components.SpotDealDialog
import com.example.ui.components.StickyLootGroupBanner
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.CommunityScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.WishlistScreen
import com.example.ui.theme.DealDropzTheme
import com.example.ui.theme.DealPink
import com.example.ui.theme.DealPinkContainer
import com.example.ui.theme.DealPinkDark
import com.example.ui.theme.DealPurple
import com.example.viewmodel.DealDropzViewModel

enum class DealDropzTab(val title: String) {
    DEALS("Deals"),
    WISHLIST("Wishlist"),
    COMMUNITY("Community"),
    ADMIN("Admin")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DealDropzTheme {
                DealDropzApp()
            }
        }
    }
}

@Composable
fun DealDropzApp(
    viewModel: DealDropzViewModel = viewModel()
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    var currentTab by remember { mutableStateOf(DealDropzTab.DEALS) }

    val products by viewModel.filteredProducts.collectAsStateWithLifecycle()
    val pickedForYou by viewModel.pickedForYouProducts.collectAsStateWithLifecycle()
    val wishlistProducts by viewModel.wishlistProducts.collectAsStateWithLifecycle()
    val wishlistIds by viewModel.wishlistIds.collectAsStateWithLifecycle()
    val votedMap by viewModel.votedMap.collectAsStateWithLifecycle()
    val priceAlerts by viewModel.priceAlerts.collectAsStateWithLifecycle()
    val submissions by viewModel.communitySubmissions.collectAsStateWithLifecycle()
    val clickAnalytics by viewModel.clickAnalytics.collectAsStateWithLifecycle()
    val userSession by viewModel.userSession.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val isAdminMode by viewModel.isAdminMode.collectAsStateWithLifecycle()
    val isAdminAuthenticated by viewModel.isAdminAuthenticated.collectAsStateWithLifecycle()
    val adminDetails by viewModel.adminDetails.collectAsStateWithLifecycle()

    val activeReelProduct by viewModel.activeReelProduct.collectAsStateWithLifecycle()
    val activePriceAlertProduct by viewModel.activePriceAlertProduct.collectAsStateWithLifecycle()
    val activeReportProduct by viewModel.activeReportProduct.collectAsStateWithLifecycle()
    val isSpotDealOpen by viewModel.isSpotDealOpen.collectAsStateWithLifecycle()
    val isLoginDialogOpen by viewModel.isLoginDialogOpen.collectAsStateWithLifecycle()
    val isChatOpen by viewModel.isChatOpen.collectAsStateWithLifecycle()
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val isChatLoading by viewModel.isChatLoading.collectAsStateWithLifecycle()
    val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()

    // Handle Back Press when not on DEALS tab
    BackHandler(enabled = currentTab != DealDropzTab.DEALS) {
        currentTab = DealDropzTab.DEALS
    }

    // Show Snackbar whenever a toastMessage is published
    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearToast()
        }
    }

    // Helper functions for external links & WhatsApp share
    fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Opening $url", Toast.LENGTH_SHORT).show()
        }
    }

    fun shareDealOnWhatsApp(product: Product) {
        viewModel.recordWhatsAppShare(product)
        val winner = product.lowestPlatformEntry
        val winnerText = if (winner != null) "Lowest on ${winner.first.displayName} at ₹${winner.second}!" else "Price comparison live on DealDropz"
        val shareText = "🔥 Check out this viral deal: ${product.title}\nReel Code: ${product.reelCode}\n🏆 $winnerText\n💡 ${product.secretTip}\nShop on DealDropz: https://dealdropz.com/deal/${product.id}"

        try {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText)
                setPackage("com.whatsapp")
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fallback to normal chooser
            val chooser = Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText)
            }, "Share Deal via")
            context.startActivity(chooser)
        }
    }

    fun handlePlatformClick(platform: PlatformType, product: Product) {
        viewModel.recordAffiliateClick(platform, product)
        val url = when (platform) {
            PlatformType.AMAZON -> product.amazonUrl
            PlatformType.FLIPKART -> product.flipkartUrl
            PlatformType.MEESHO -> product.meeshoUrl
        }
        openUrl(url)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (currentTab == DealDropzTab.DEALS) {
                DealDropzTopBar(
                    searchQuery = searchQuery,
                    onSearchQueryChange = { viewModel.setSearchQuery(it) },
                    selectedCategory = selectedCategory,
                    onCategorySelect = { viewModel.setSelectedCategory(it) },
                    userSession = userSession,
                    wishlistCount = wishlistIds.size,
                    isAdminMode = isAdminMode,
                    onOpenLogin = { viewModel.setLoginDialogOpen(true) },
                    onToggleAdmin = {
                        currentTab = DealDropzTab.ADMIN
                    },
                    onOpenWishlist = { currentTab = DealDropzTab.WISHLIST },
                    onOpenChat = { viewModel.setChatOpen(true) }
                )
            }
        },
        floatingActionButton = {
            if (currentTab == DealDropzTab.DEALS) {
                ExtendedFloatingActionButton(
                    onClick = { viewModel.setChatOpen(true) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "DealBot AI",
                            tint = Color.White
                        )
                    },
                    text = {
                        Text(
                            text = "DealBot AI",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    },
                    containerColor = DealPurple,
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .padding(bottom = 60.dp)
                        .testTag("floating_dealbot_ai_button")
                )
            }
        },
        bottomBar = {
            Column {
                // Sticky VIP Loot Banner above bottom bar
                if (currentTab == DealDropzTab.DEALS || currentTab == DealDropzTab.COMMUNITY) {
                    StickyLootGroupBanner(
                        onJoinWhatsApp = {
                            openUrl("https://chat.whatsapp.com/dealdropz_vip_loot")
                        },
                        onJoinTelegram = {
                            openUrl("https://t.me/dealdropz_alerts")
                        }
                    )
                }

                // Bottom Navigation Bar (M3)
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("bottom_nav_bar")
                ) {
                    // Deals Tab
                    NavigationBarItem(
                        selected = currentTab == DealDropzTab.DEALS,
                        onClick = { currentTab = DealDropzTab.DEALS },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == DealDropzTab.DEALS) Icons.Filled.ShoppingBag else Icons.Outlined.ShoppingBag,
                                contentDescription = "Deals"
                            )
                        },
                        label = { Text("Deals", fontWeight = FontWeight.Bold, fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DealPink,
                            selectedTextColor = DealPink,
                            indicatorColor = DealPinkContainer,
                            unselectedIconColor = Color(0xFF64748B),
                            unselectedTextColor = Color(0xFF64748B)
                        ),
                        modifier = Modifier.testTag("nav_tab_deals")
                    )

                    // Wishlist Tab
                    NavigationBarItem(
                        selected = currentTab == DealDropzTab.WISHLIST,
                        onClick = { currentTab = DealDropzTab.WISHLIST },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (wishlistIds.isNotEmpty()) {
                                        Badge(
                                            containerColor = DealPink,
                                            contentColor = Color.White
                                        ) {
                                            Text("${wishlistIds.size}", fontSize = 10.sp)
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (currentTab == DealDropzTab.WISHLIST) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                    contentDescription = "Wishlist"
                                )
                            }
                        },
                        label = { Text("Wishlist", fontWeight = FontWeight.Bold, fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DealPink,
                            selectedTextColor = DealPink,
                            indicatorColor = DealPinkContainer,
                            unselectedIconColor = Color(0xFF64748B),
                            unselectedTextColor = Color(0xFF64748B)
                        ),
                        modifier = Modifier.testTag("nav_tab_wishlist")
                    )

                    // Community Tab
                    NavigationBarItem(
                        selected = currentTab == DealDropzTab.COMMUNITY,
                        onClick = { currentTab = DealDropzTab.COMMUNITY },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == DealDropzTab.COMMUNITY) Icons.Filled.Groups else Icons.Outlined.Groups,
                                contentDescription = "Community"
                            )
                        },
                        label = { Text("Community", fontWeight = FontWeight.Bold, fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DealPink,
                            selectedTextColor = DealPink,
                            indicatorColor = DealPinkContainer,
                            unselectedIconColor = Color(0xFF64748B),
                            unselectedTextColor = Color(0xFF64748B)
                        ),
                        modifier = Modifier.testTag("nav_tab_community")
                    )

                    // Admin Tab
                    NavigationBarItem(
                        selected = currentTab == DealDropzTab.ADMIN,
                        onClick = { currentTab = DealDropzTab.ADMIN },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == DealDropzTab.ADMIN) Icons.Filled.AdminPanelSettings else Icons.Outlined.AdminPanelSettings,
                                contentDescription = "Admin"
                            )
                        },
                        label = { Text("Admin", fontWeight = FontWeight.Bold, fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DealPink,
                            selectedTextColor = DealPink,
                            indicatorColor = DealPinkContainer,
                            unselectedIconColor = Color(0xFF64748B),
                            unselectedTextColor = Color(0xFF64748B)
                        ),
                        modifier = Modifier.testTag("nav_tab_admin")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                DealDropzTab.DEALS -> {
                    HomeScreen(
                        products = products,
                        pickedForYou = pickedForYou,
                        wishlistIds = wishlistIds,
                        votedMap = votedMap,
                        searchQuery = searchQuery,
                        onClearSearch = {
                            viewModel.setSearchQuery("")
                            viewModel.setSelectedCategory("All")
                        },
                        onToggleWishlist = { viewModel.toggleWishlist(it) },
                        onVoteHot = { id, isHot -> viewModel.voteProduct(id, isHot) },
                        onPlatformClick = { platform, prod -> handlePlatformClick(platform, prod) },
                        onWatchReel = { viewModel.openReelModal(it) },
                        onWhatsAppShare = { shareDealOnWhatsApp(it) },
                        onSetPriceAlert = { viewModel.openPriceAlert(it) },
                        onReportPrice = { viewModel.openReportModal(it) }
                    )
                }

                DealDropzTab.WISHLIST -> {
                    WishlistScreen(
                        wishlistProducts = wishlistProducts,
                        priceAlerts = priceAlerts,
                        onRemoveFromWishlist = { viewModel.toggleWishlist(it) },
                        onPlatformClick = { platform, prod -> handlePlatformClick(platform, prod) },
                        onWatchReel = { viewModel.openReelModal(it) },
                        onExploreDeals = { currentTab = DealDropzTab.DEALS }
                    )
                }

                DealDropzTab.COMMUNITY -> {
                    CommunityScreen(
                        submissions = submissions,
                        onOpenSpotDeal = { viewModel.setSpotDealOpen(true) },
                        onJoinWhatsApp = { openUrl("https://chat.whatsapp.com/dealdropz_vip_loot") },
                        onJoinTelegram = { openUrl("https://t.me/dealdropz_alerts") },
                        onOpenInstagram = { openUrl("https://instagram.com/DealDropz.Reels") },
                        onOpenYouTube = { openUrl("https://youtube.com/@DealDropzReviews") }
                    )
                }

                DealDropzTab.ADMIN -> {
                    AdminScreen(
                        isAdminAuthenticated = isAdminAuthenticated,
                        adminDetails = adminDetails,
                        analytics = clickAnalytics,
                        submissions = submissions,
                        products = products,
                        onLoginAdmin = { email, pass -> viewModel.loginAdmin(email, pass) },
                        onLogoutAdmin = { viewModel.logoutAdmin() },
                        onApproveSubmission = { viewModel.approveSubmission(it) },
                        onRejectSubmission = { viewModel.rejectSubmission(it) },
                        onAddNewProduct = { viewModel.addNewProduct(it) },
                        onDeleteProduct = { viewModel.deleteProduct(it) },
                        onExitAdmin = { currentTab = DealDropzTab.DEALS }
                    )
                }
            }
        }
    }

    // Modal Overlays
    activeReelProduct?.let { product ->
        ReelPreviewDialog(
            product = product,
            onDismiss = { viewModel.closeReelModal() },
            onShopProduct = { platform ->
                viewModel.closeReelModal()
                handlePlatformClick(platform, product)
            },
            onWhatsAppShare = { shareDealOnWhatsApp(product) },
            onVoteHot = { viewModel.voteProduct(product.id, true) }
        )
    }

    activePriceAlertProduct?.let { product ->
        PriceDropDialog(
            product = product,
            onDismiss = { viewModel.closePriceAlert() },
            onSetAlert = { targetPrice -> viewModel.setPriceAlert(product.id, targetPrice) }
        )
    }

    activeReportProduct?.let { product ->
        ReportDialog(
            product = product,
            onDismiss = { viewModel.closeReportModal() },
            onSubmitReport = { reason -> viewModel.submitPriceReport(product.id, reason) }
        )
    }

    if (isSpotDealOpen) {
        SpotDealDialog(
            onDismiss = { viewModel.setSpotDealOpen(false) },
            onSubmitDeal = { title, reelCode, cat, amz, flp, mee, tip ->
                viewModel.submitCommunityDeal(title, reelCode, cat, amz, flp, mee, tip)
            }
        )
    }

    if (isLoginDialogOpen) {
        LoginDialog(
            onDismiss = { viewModel.setLoginDialogOpen(false) },
            onGoogleSignIn = { viewModel.signInWithGoogle() },
            onEmailSignIn = { name, email -> viewModel.signInWithEmail(name, email) },
            onAdminSignIn = {
                viewModel.setLoginDialogOpen(false)
                currentTab = DealDropzTab.ADMIN
            }
        )
    }

    if (isChatOpen) {
        GeminiChatDialog(
            messages = chatMessages,
            isLoading = isChatLoading,
            onSendMessage = { viewModel.sendChatMessage(it) },
            onClearChat = { viewModel.clearChat() },
            onDismiss = { viewModel.setChatOpen(false) }
        )
    }
}
