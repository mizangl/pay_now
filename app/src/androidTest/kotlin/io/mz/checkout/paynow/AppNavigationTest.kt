package io.mz.checkout.paynow

import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import io.mz.checkout.paynow.navigation.PayNowNavHost
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AppNavigationTest {

    @get:Rule
    val composeRule = createComposeRule()
    lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        composeRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            PayNowNavHost(navController = navController)
        }
    }

    @Test
    fun payNowNavHost_mainStartDestination() {
        composeRule.onNodeWithText("Pay").assertIsDisplayed()
    }

    @Test
    fun payNowNavHost_navigateTo3dsProcessing() {
        composeRule.onNodeWithText("Pay").performClick()
        composeRule.onNodeWithTag("webview").assertIsDisplayed()
    }

    @Test
    fun payNowNavHost_interceptSuccessDeepLink() {
        composeRule.onNodeWithText("Pay").performClick()
        composeRule.onNodeWithTag("webview").assertIsDisplayed()

        val deepLinkUri = Uri.parse("paynow://callback-processing/result/success")
        val intent = Intent(Intent.ACTION_VIEW, deepLinkUri)

        composeRule.runOnUiThread {
            navController.handleDeepLink(intent)
        }

        composeRule.onNodeWithText("success").assertIsDisplayed()
    }

    @Test
    fun payNowNavHost_interceptFailureDeepLink() {
        composeRule.onNodeWithText("Pay").performClick()
        composeRule.onNodeWithTag("webview").assertIsDisplayed()

        val deepLinkUri = Uri.parse("paynow://callback-processing/result/failure")
        val intent = Intent(Intent.ACTION_VIEW, deepLinkUri)

        composeRule.runOnUiThread {
            navController.handleDeepLink(intent)
        }

        composeRule.onNodeWithText("failure").assertIsDisplayed()

    }
}
