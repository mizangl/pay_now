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

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mz.checkout.paynow.creditcard.ui.component.TestTags as CreditCardFormTestTags
import io.mz.checkout.paynow.creditcard.verification.outcome.ui.TestTags as VerificationTestTags
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class AppNavigationTest {

  @get:Rule(order = 0)
  val hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1)
  val composeRule = createAndroidComposeRule<MainActivity>()

  private val context: Context = ApplicationProvider.getApplicationContext()

  private val numberField = composeRule.onNodeWithTag(
    CreditCardFormTestTags.CreditCardNumber.CREDIT_CARD_FIELD
  )
  private val dateField = composeRule.onNodeWithTag(
    CreditCardFormTestTags.CreditCardDate.CREDIT_CARD_FIELD
  )
  private val cvvField = composeRule.onNodeWithTag(
    CreditCardFormTestTags.CreditCardCVV.CREDIT_CARD_FIELD
  )
  private val payNowButton = composeRule.onNodeWithTag(
    CreditCardFormTestTags.CreditCardPayButton.CREDIT_CARD_FIELD
  )

  private val outcomeField = composeRule.onNodeWithTag(
    VerificationTestTags.VerificationOutcomeScreen.OUTCOME_RESULT
  )

  @Before
  fun setup() {
    hiltRule.inject()
  }

  @Test
  fun payNow_successFlow() {
    val creditCardNumber = "4242424242424242"
    val date = "122026"
    val cvv = "1234"
    numberField.isDisplayed()
    numberField.performTextInput(creditCardNumber)
    dateField.performTextInput(date)
    cvvField.performTextInput(cvv)
    cvvField.performImeAction()

    payNowButton.performClick()

    val deepLinkUri = Uri.parse("paynow://callback-processing/result/success")
    val intent = Intent(Intent.ACTION_VIEW, deepLinkUri)

    composeRule.activityRule.scenario.onActivity {
      it.startActivity(intent)
    }

    outcomeField.assertTextEquals("APPROVED")
  }

  @Test
  fun payNow_FailureFlow() {
    val creditCardNumber = "4242424242424242"
    val date = "122026"
    val cvv = "1234"
    numberField.isDisplayed()
    numberField.performTextInput(creditCardNumber)
    dateField.performTextInput(date)
    cvvField.performTextInput(cvv)
    cvvField.performImeAction()

    payNowButton.performClick()

    val deepLinkUri = Uri.parse("paynow://callback-processing/result/failure")
    val intent = Intent(Intent.ACTION_VIEW, deepLinkUri)

    composeRule.activityRule.scenario.onActivity {
      it.startActivity(intent)
    }

    outcomeField.assertTextEquals("DECLINED")
  }
}
