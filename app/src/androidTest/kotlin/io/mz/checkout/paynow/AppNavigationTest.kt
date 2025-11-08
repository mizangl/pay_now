/*
 * Copyright 2025 Martin Zangl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
