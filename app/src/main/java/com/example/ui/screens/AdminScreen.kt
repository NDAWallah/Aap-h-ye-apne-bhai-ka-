package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AdminDetails
import com.example.model.ClickAnalytics
import com.example.model.CommunitySubmission
import com.example.model.Product
import com.example.model.SubmissionStatus
import com.example.ui.theme.AmazonColor
import com.example.ui.theme.DealGold
import com.example.ui.theme.DealPink
import com.example.ui.theme.DealPurple
import com.example.ui.theme.FlipkartColor
import com.example.ui.theme.MeeshoColor
import com.example.ui.theme.WinnerGreen
import java.util.UUID

@Composable
fun AdminScreen(
    isAdminAuthenticated: Boolean,
    adminDetails: AdminDetails,
    analytics: ClickAnalytics,
    submissions: List<CommunitySubmission>,
    products: List<Product>,
    onLoginAdmin: (String, String) -> Boolean,
    onLogoutAdmin: () -> Unit,
    onApproveSubmission: (CommunitySubmission) -> Unit,
    onRejectSubmission: (CommunitySubmission) -> Unit,
    onAddNewProduct: (Product) -> Unit,
    onDeleteProduct: (String) -> Unit,
    onExitAdmin: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isAdminAuthenticated) {
        // Render Admin Login Gateway
        AdminLoginGateway(
            onLogin = onLoginAdmin,
            onBack = onExitAdmin,
            modifier = modifier
        )
    } else {
        // Render Authenticated Admin Dashboard with Admin Details
        AdminDashboardContent(
            adminDetails = adminDetails,
            analytics = analytics,
            submissions = submissions,
            products = products,
            onLogout = onLogoutAdmin,
            onApproveSubmission = onApproveSubmission,
            onRejectSubmission = onRejectSubmission,
            onAddNewProduct = onAddNewProduct,
            onDeleteProduct = onDeleteProduct,
            onExitAdmin = onExitAdmin,
            modifier = modifier
        )
    }
}

@Composable
fun AdminLoginGateway(
    onLogin: (String, String) -> Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var emailInput by remember { mutableStateOf("parul301008@gmail.com") }
    var passwordInput by remember { mutableStateOf("Parul@2008") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(20.dp)
            .testTag("admin_login_screen"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Shield / Lock Icon
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(DealPink, DealPurple)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "Admin Shield",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "DealDropz Store Owner Access",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0F172A)
                )

                Text(
                    text = "Sign in with your administrator credentials",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Credentials Info Banner
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFFFF1F4),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFD4DF)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Key,
                                contentDescription = "Credentials",
                                tint = DealPink,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Admin Login Details:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF9F1239)
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "Email: parul301008@gmail.com",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF881337)
                        )
                        Text(
                            text = "Password: Parul@2008",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF881337)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Email Input
                OutlinedTextField(
                    value = emailInput,
                    onValueChange = {
                        emailInput = it
                        errorMessage = null
                    },
                    label = { Text("Admin E-mail") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email",
                            tint = DealPink
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_email_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Password Input
                OutlinedTextField(
                    value = passwordInput,
                    onValueChange = {
                        passwordInput = it
                        errorMessage = null
                    },
                    label = { Text("Admin Password") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Password",
                            tint = DealPink
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Toggle password",
                                tint = Color(0xFF64748B)
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_password_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                errorMessage?.let { error ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = error, fontSize = 11.sp, color = Color(0xFFDC2626), fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Login Button
                Button(
                    onClick = {
                        val success = onLogin(emailInput, passwordInput)
                        if (!success) {
                            errorMessage = "Invalid credentials. Use email parul301008@gmail.com and password Parul@2008"
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("admin_login_submit_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DealPink)
                ) {
                    Text("Unlock Admin Dashboard", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Auto-Fill Button for instant testing
                OutlinedButton(
                    onClick = {
                        emailInput = "parul301008@gmail.com"
                        passwordInput = "Parul@2008"
                        onLogin("parul301008@gmail.com", "Parul@2008")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("autofill_admin_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = "Fast fill",
                        tint = DealPink,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("1-Tap Login as Parul (Admin)", fontSize = 12.sp, color = Color(0xFF334155), fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Return to Store", fontSize = 12.sp, color = Color(0xFF64748B))
                }
            }
        }
    }
}

@Composable
fun AdminDashboardContent(
    adminDetails: AdminDetails,
    analytics: ClickAnalytics,
    submissions: List<CommunitySubmission>,
    products: List<Product>,
    onLogout: () -> Unit,
    onApproveSubmission: (CommunitySubmission) -> Unit,
    onRejectSubmission: (CommunitySubmission) -> Unit,
    onAddNewProduct: (Product) -> Unit,
    onDeleteProduct: (String) -> Unit,
    onExitAdmin: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isAddProductExpanded by remember { mutableStateOf(false) }

    // Form fields for new product
    var newTitle by remember { mutableStateOf("") }
    var newReelCode by remember { mutableStateOf("#") }
    var newCategory by remember { mutableStateOf("Girls Kurtis") }
    var newImageUrl by remember { mutableStateOf("https://images.unsplash.com/photo-1583391733956-3750e0ff4e8b?auto=format&fit=crop&w=800&q=80") }
    var newAmazonPrice by remember { mutableStateOf("599") }
    var newFlipkartPrice by remember { mutableStateOf("449") }
    var newMeeshoPrice by remember { mutableStateOf("299") }
    var newSecretTip by remember { mutableStateOf("💡 Apply ₹50 coupon on checkout page!") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .testTag("admin_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Authenticated Admin Profile Card (Showing Parul's Details)
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_details_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(DealPink),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("👑", fontSize = 24.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = adminDetails.name,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = WinnerGreen
                                    ) {
                                        Text(
                                            text = "SUPER ADMIN",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = adminDetails.email,
                                    fontSize = 12.sp,
                                    color = Color(0xFFCBD5E1)
                                )
                            }
                        }

                        IconButton(
                            onClick = onLogout,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF334155))
                                .testTag("admin_logout_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Lock Admin",
                                tint = Color(0xFFF87171),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Store & Contact Details Grid
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF0F172A),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            AdminDetailRow(icon = Icons.Default.Storefront, label = "Store", value = adminDetails.storeName)
                            AdminDetailRow(icon = Icons.Default.Email, label = "Admin Email", value = adminDetails.email)
                            AdminDetailRow(icon = Icons.Default.Phone, label = "Owner Phone", value = adminDetails.phone)
                            AdminDetailRow(icon = Icons.Default.Key, label = "Security", value = "Password Protected (Parul@2008)")
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Affiliate Partner IDs
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF334155),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("Amazon Tag", fontSize = 9.sp, color = AmazonColor, fontWeight = FontWeight.Bold)
                                Text(adminDetails.amazonAssociateId, fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                            }
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF334155),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("Flipkart ID", fontSize = 9.sp, color = FlipkartColor, fontWeight = FontWeight.Bold)
                                Text(adminDetails.flipkartAffiliateId, fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                            }
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF334155),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("Meesho ID", fontSize = 9.sp, color = MeeshoColor, fontWeight = FontWeight.Bold)
                                Text(adminDetails.meeshoPartnerId, fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }

        // Live Click Analytics Counter (Requirement 6)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Analytics,
                                contentDescription = "Analytics",
                                tint = Color(0xFF2563EB),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Affiliate Click & Conversion Tracker",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                        }

                        // Estimated Earnings Callout
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFD1FAE5)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("₹42,850 Est. Earnings", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WinnerGreen)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AnalyticsMetricCard(
                            label = "Amazon Clicks",
                            value = "${analytics.amazonClicks}",
                            color = AmazonColor,
                            modifier = Modifier.weight(1f)
                        )
                        AnalyticsMetricCard(
                            label = "Flipkart Clicks",
                            value = "${analytics.flipkartClicks}",
                            color = FlipkartColor,
                            modifier = Modifier.weight(1f)
                        )
                        AnalyticsMetricCard(
                            label = "Meesho Clicks",
                            value = "${analytics.meeshoClicks}",
                            color = MeeshoColor,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AnalyticsMetricCard(
                            label = "Total Redirects",
                            value = "${analytics.totalClicks}",
                            color = WinnerGreen,
                            modifier = Modifier.weight(1f)
                        )
                        AnalyticsMetricCard(
                            label = "WhatsApp Shares",
                            value = "${analytics.whatsappShares}",
                            color = Color(0xFF25D366),
                            modifier = Modifier.weight(1f)
                        )
                        AnalyticsMetricCard(
                            label = "Reels Watched",
                            value = "${analytics.reelsWatched}",
                            color = DealPink,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Add Product Section
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Add New Deal / Outfit",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Button(
                            onClick = { isAddProductExpanded = !isAddProductExpanded },
                            colors = ButtonDefaults.buttonColors(containerColor = DealPink),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = if (isAddProductExpanded) Icons.Default.Close else Icons.Default.Add,
                                contentDescription = "Toggle Add",
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isAddProductExpanded) "Close" else "Add Product", fontSize = 11.sp)
                        }
                    }

                    if (isAddProductExpanded) {
                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = newTitle,
                            onValueChange = { newTitle = it },
                            label = { Text("Product Title") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = newReelCode,
                                onValueChange = { newReelCode = it },
                                label = { Text("Reel Code (#)") },
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = newCategory,
                                onValueChange = { newCategory = it },
                                label = { Text("Category") },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            OutlinedTextField(
                                value = newAmazonPrice,
                                onValueChange = { newAmazonPrice = it },
                                label = { Text("Amazon ₹") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = newFlipkartPrice,
                                onValueChange = { newFlipkartPrice = it },
                                label = { Text("Flipkart ₹") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = newMeeshoPrice,
                                onValueChange = { newMeeshoPrice = it },
                                label = { Text("Meesho ₹") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = newSecretTip,
                            onValueChange = { newSecretTip = it },
                            label = { Text("Secret Coupon / Tip") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                if (newTitle.isNotBlank()) {
                                    val product = Product(
                                        id = "dd_custom_${UUID.randomUUID().toString().take(6)}",
                                        title = newTitle,
                                        reelCode = if (newReelCode.startsWith("#")) newReelCode else "#$newReelCode",
                                        category = newCategory,
                                        imageUrl = newImageUrl,
                                        amazonPrice = newAmazonPrice.toIntOrNull() ?: 599,
                                        flipkartPrice = newFlipkartPrice.toIntOrNull() ?: 449,
                                        meeshoPrice = newMeeshoPrice.toIntOrNull() ?: 299,
                                        secretTip = newSecretTip,
                                        isPickedForYou = false
                                    )
                                    onAddNewProduct(product)
                                    newTitle = ""
                                    isAddProductExpanded = false
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = WinnerGreen),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Publish to DealDropz Feed", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Review Community Submissions
        item {
            Text(
                text = "Community Deal Submissions (${submissions.size})",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
        }

        items(submissions, key = { it.id }) { sub ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${sub.reelCode} • ${sub.category}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = DealPink
                        )
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = when (sub.status) {
                                SubmissionStatus.APPROVED -> Color(0xFFD1FAE5)
                                SubmissionStatus.PENDING -> Color(0xFFFEF3C7)
                                SubmissionStatus.REJECTED -> Color(0xFFFEE2E2)
                            }
                        ) {
                            Text(
                                text = sub.status.name,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (sub.status) {
                                    SubmissionStatus.APPROVED -> Color(0xFF065F46)
                                    SubmissionStatus.PENDING -> Color(0xFF92400E)
                                    SubmissionStatus.REJECTED -> Color(0xFF991B1B)
                                },
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = sub.title, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "Submitted by @${sub.submittedBy} • Amazon: ₹${sub.amazonPrice} | Flipkart: ₹${sub.flipkartPrice} | Meesho: ₹${sub.meeshoPrice}",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )

                    if (sub.status == SubmissionStatus.PENDING) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            OutlinedButton(
                                onClick = { onRejectSubmission(sub) },
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text("Reject", fontSize = 11.sp, color = Color(0xFFEF4444))
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = { onApproveSubmission(sub) },
                                shape = RoundedCornerShape(6.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = WinnerGreen),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text("Approve & Publish", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminDetailRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = label, tint = DealPink, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "$label: ", fontSize = 11.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.Medium)
        Text(text = value, fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun AnalyticsMetricCard(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFF8FAFC),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Black, color = color)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = label, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
        }
    }
}
