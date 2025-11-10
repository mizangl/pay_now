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
package io.mz.checkout.paynow.creditcard.component

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import io.mz.checkout.paynow.creditcard.ui.component.CreditCardNumberTextField
import io.mz.checkout.paynow.creditcard.ui.component.TestTags
import io.mz.checkout.paynow.creditcard.ui.component.rememberCreditCardNumberState
import kotlin.test.Test
import org.junit.Rule

class CreditCardNumberTextFieldTest {

  @get:Rule
  val composeRule = createComposeRule()

  @Test
  fun testCreditCardNumberIsDisplayed() {
    val creditCardNumber = "4242424242424242"
    composeRule.setContent {
      CreditCardNumberTextField(
        creditCardNumberState = rememberCreditCardNumberState(
          initialText = creditCardNumber
        )
      )
    }

    composeRule.onNodeWithTag(TestTags.CreditCardNumber.CREDIT_CARD_FIELD).isDisplayed()
  }

  @Test
  fun testCreditCardNumberTriggered() {
    val creditCardNumber = "4242424242424242"
    composeRule.setContent {
      CreditCardNumberTextField()
    }

    composeRule.onNodeWithTag(TestTags.CreditCardNumber.CREDIT_CARD_FIELD).isDisplayed()
    composeRule.onNodeWithTag(TestTags.CreditCardNumber.OUTLINE_TEXT_FIELD).performTextInput(
      creditCardNumber
    )

    composeRule.onNodeWithTag(TestTags.CreditCardNumber.OUTLINE_TEXT_FIELD).assert(
      hasText(creditCardNumber)
    )
  }
}
