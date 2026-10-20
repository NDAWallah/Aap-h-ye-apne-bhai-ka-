package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PlatformType
import com.example.model.Product
import com.example.ui.theme.AmazonBg
import com.example.ui.theme.AmazonColor
import com.example.ui.theme.FlipkartBg
import com.example.ui.theme.FlipkartColor
import com.example.ui.theme.MeeshoBg
import com.example.ui.theme.MeeshoColor
import com.example.ui.theme.WinnerGreen
import com.example.ui.theme.WinnerGreenContainer
import com.example.ui.theme.WinnerGreenOnContainer

@Composable
fun PriceComparisonBox(
    product: Product,
    onPlatformClick: (PlatformType) -> Unit,
    modifier: Modifier = Modifier
) {
    val lowestWinner = product.lowestPlatformEntry?.first

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
            .padding(8.dp)
    ) {
        // Winner Callout Header
        lowestWinner?.let { winner ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(WinnerGreenContainer)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = "Lowest Price Winner",
                    tint = WinnerGreen,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "🏆 Lowest on ${winner.displayName} at ₹${product.lowestPrice}!",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = WinnerGreenOnContainer
                )
                if (product.maxSavings > 0) {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Save ₹${product.maxSavings} (${product.savingsPercent}%)",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = WinnerGreen
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // 3 Platform Price Comparison Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            PlatformPriceColumn(
                platform = PlatformType.AMAZON,
                price = product.amazonPrice,
                isLowest = lowestWinner == PlatformType.AMAZON,
                onClick = { onPlatformClick(PlatformType.AMAZON) },
                modifier = Modifier.weight(1f)
            )
            PlatformPriceColumn(
                platform = PlatformType.FLIPKART,
                price = product.flipkartPrice,
                isLowest = lowestWinner == PlatformType.FLIPKART,
                onClick = { onPlatformClick(PlatformType.FLIPKART) },
                modifier = Modifier.weight(1f)
            )
            PlatformPriceColumn(
                platform = PlatformType.MEESHO,
                price = product.meeshoPrice,
                isLowest = lowestWinner == PlatformType.MEESHO,
                onClick = { onPlatformClick(PlatformType.MEESHO) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun PlatformPriceColumn(
    platform: PlatformType,
    price: Int,
    isLowest: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (brandColor, bgColor) = when (platform) {
        PlatformType.AMAZON -> AmazonColor to AmazonBg
        PlatformType.FLIPKART -> FlipkartColor to FlipkartBg
        PlatformType.MEESHO -> MeeshoColor to MeeshoBg
    }

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isLowest) Color.White else bgColor,
        border = if (isLowest) androidx.compose.foundation.BorderStroke(2.dp, WinnerGreen) else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .testTag("buy_${platform.name.lowercase()}_button")
    ) {
        Box {
            if (isLowest) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .background(
                            WinnerGreen,
                            RoundedCornerShape(bottomStart = 8.dp)
                        )
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "CHEAPEST",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
            }

            Column(
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = platform.displayName,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = brandColor
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "₹$price",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isLowest) WinnerGreen else Color(0xFF1E293B)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Buy",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = brandColor
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                        contentDescription = "Buy on ${platform.displayName}",
                        tint = brandColor,
                        modifier = Modifier.size(10.dp)
                    )
                }
            }
        }
    }
}
