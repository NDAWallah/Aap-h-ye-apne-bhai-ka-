package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.PlatformType
import com.example.model.Product
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("DealDropz", appName)
  }

  @Test
  fun `price comparison lowest winner calculation`() {
    val product = Product(
      id = "test_1",
      title = "Test Kurti",
      reelCode = "#101",
      category = "Girls Kurtis",
      imageUrl = "",
      amazonPrice = 649,
      flipkartPrice = 499,
      meeshoPrice = 349
    )

    assertEquals(349, product.lowestPrice)
    assertEquals(PlatformType.MEESHO, product.lowestPlatformEntry?.first)
    assertEquals(300, product.maxSavings)
  }
}
