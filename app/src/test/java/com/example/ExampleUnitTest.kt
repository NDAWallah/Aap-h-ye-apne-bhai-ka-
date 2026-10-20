package com.example

import com.example.model.PlatformType
import com.example.model.Product
import com.example.viewmodel.DealDropzViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

  @Test
  fun testProductDataClassFields() {
    // Testing data class Product with explicit fields: title, imageUrl, category, priceMap, reelCode, rating
    val priceMap = mapOf(
      PlatformType.AMAZON to 699,
      PlatformType.FLIPKART to 499,
      PlatformType.MEESHO to 349
    )
    val product = Product(
      id = "p_101",
      title = "Anouk Floral Kurti",
      imageUrl = "https://example.com/kurti.jpg",
      category = "Girls Kurtis",
      priceMap = priceMap,
      reelCode = "#105",
      rating = 4.6f
    )

    assertEquals("Anouk Floral Kurti", product.title)
    assertEquals("https://example.com/kurti.jpg", product.imageUrl)
    assertEquals("Girls Kurtis", product.category)
    assertEquals(priceMap, product.priceMap)
    assertEquals("#105", product.reelCode)
    assertEquals(4.6f, product.rating)

    // Verification of lowest price computation
    assertEquals(349, product.lowestPrice)
    assertEquals(PlatformType.MEESHO, product.lowestPlatformEntry?.first)
    assertEquals(350, product.maxSavings)
  }

  @Test
  fun testAdminLoginSuccess() {
    val viewModel = DealDropzViewModel()
    val result = viewModel.loginAdmin("parul301008@gmail.com", "Parul@2008")
    assertTrue("Admin login should succeed with correct credentials", result)
    assertTrue("Admin authenticated state should be true", viewModel.isAdminAuthenticated.value)
    assertTrue("Admin mode should be enabled", viewModel.isAdminMode.value)
  }

  @Test
  fun testAdminLoginCaseInsensitiveEmail() {
    val viewModel = DealDropzViewModel()
    val result = viewModel.loginAdmin("Parul301008@gmail.com ", "Parul@2008")
    assertTrue("Admin login should accept case-insensitive trimmed email", result)
  }

  @Test
  fun testAdminLoginFailure() {
    val viewModel = DealDropzViewModel()
    val result = viewModel.loginAdmin("parul301008@gmail.com", "wrong_password")
    assertFalse("Admin login should fail with incorrect password", result)
    assertFalse("Admin authenticated state should remain false", viewModel.isAdminAuthenticated.value)
  }

  @Test
  fun testChatbotInitialMessage() {
    val viewModel = DealDropzViewModel()
    assertTrue("Chat should have initial greeting message", viewModel.chatMessages.value.isNotEmpty())
    assertFalse("Greeting should be from DealBot", viewModel.chatMessages.value.first().isUser)
  }
}
