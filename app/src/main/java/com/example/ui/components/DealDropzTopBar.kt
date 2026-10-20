package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserSession
import com.example.ui.theme.DealGold
import com.example.ui.theme.DealPink
import com.example.ui.theme.DealPinkContainer
import com.example.ui.theme.DealPinkDark
import com.example.ui.theme.DealPurple

@Composable
fun DealDropzTopBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    userSession: UserSession,
    wishlistCount: Int,
    isAdminMode: Boolean,
    onOpenLogin: () -> Unit,
    onToggleAdmin: () -> Unit,
    onOpenWishlist: () -> Unit,
    onOpenChat: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showTooltip by remember { mutableStateOf(true) }
    val categories = listOf("All", "Girls Kurtis", "Boys Wear", "Viral Reels", "Under ₹199", "Under ₹499")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        // Top Brand Header Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Brand Logo & Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.testTag("brand_header")
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(DealPink, DealPurple)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = "DealDropz Logo",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "DealDropz",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onBackground,
                            letterSpacing = (-0.5).sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Deals",
                            tint = DealGold,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = "Meesho • Flipkart • Amazon Loot Deals",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DealPinkDark
                    )
                }
            }

            // Action Buttons
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Admin Toggle Button
                IconButton(
                    onClick = onToggleAdmin,
                    modifier = Modifier
                        .testTag("admin_toggle_button")
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = "Admin Panel",
                        tint = if (isAdminMode) DealPink else Color(0xFF64748B)
                    )
                }

                // DealBot AI Chat Button
                IconButton(
                    onClick = onOpenChat,
                    modifier = Modifier
                        .testTag("open_dealbot_ai_button")
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = "Ask DealBot AI",
                        tint = DealPurple
                    )
                }

                // Wishlist Icon with Badge
                IconButton(
                    onClick = onOpenWishlist,
                    modifier = Modifier
                        .testTag("wishlist_button")
                        .size(40.dp)
                ) {
                    BadgedBox(
                        badge = {
                            if (wishlistCount > 0) {
                                Badge(
                                    containerColor = DealPink,
                                    contentColor = Color.White
                                ) {
                                    Text(wishlistCount.toString(), fontSize = 10.sp)
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Wishlist",
                            tint = if (wishlistCount > 0) DealPink else Color(0xFF64748B)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Profile / Login Button
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (userSession.isLoggedIn) DealPinkContainer else Color(0xFFF1F5F9),
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable(onClick = onOpenLogin)
                        .testTag("login_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (userSession.isLoggedIn) {
                            Text(userSession.avatarEmoji, fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = userSession.userName.split(" ").firstOrNull() ?: "User",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = DealPinkDark
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Sign In",
                                modifier = Modifier.size(16.dp),
                                tint = DealPink
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Sign In",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = DealPink
                            )
                        }
                    }
                }
            }
        }

        // Welcome Tooltip (as requested: "Sign in for a personalized experience tailored just for you — save your favorite Reel finds & track price drops!")
        AnimatedVisibility(
            visible = showTooltip && !userSession.isLoggedIn,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFFFF1F4),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFD4DF))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🎁 Sign in for a personalized experience tailored just for you — save your favorite Reel finds & track price drops!",
                        fontSize = 11.sp,
                        color = Color(0xFF9F1239),
                        lineHeight = 15.sp,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { showTooltip = false },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Dismiss tooltip",
                            tint = Color(0xFF9F1239),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }

        // Search Bar with Reel Code Search Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_input"),
                placeholder = {
                    Text(
                        text = "Search product or Reel Code (#105)...",
                        fontSize = 13.sp,
                        color = Color(0xFF94A3B8)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = DealPink
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = Color(0xFF94A3B8)
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DealPink,
                    unfocusedBorderColor = Color(0xFFE2E8F0),
                    focusedContainerColor = Color(0xFFFFF9FA),
                    unfocusedContainerColor = Color(0xFFF8FAFC)
                )
            )
        }

        // Quick Category Filter Tabs (Meesho/Flipkart Style)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { category ->
                val isSelected = selectedCategory == category
                FilterChip(
                    selected = isSelected,
                    onClick = { onCategorySelect(category) },
                    label = {
                        Text(
                            text = category,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = DealPink,
                        selectedLabelColor = Color.White,
                        containerColor = Color(0xFFF1F5F9),
                        labelColor = Color(0xFF334155)
                    ),
                    border = null,
                    modifier = Modifier.testTag("category_chip_$category")
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}
