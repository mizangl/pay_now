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
package io.mz.checkout.paynow.creditcard

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.requestFocus
import io.mz.checkout.paynow.creditcard.ui.CreditCardForm
import io.mz.checkout.paynow.creditcard.ui.component.TestTags
import io.mz.checkout.paynow.creditcard.ui.component.model.CardEntry
import io.mz.checkout.paynow.creditcard.ui.model.CreditCardCVVState
import io.mz.checkout.paynow.creditcard.ui.model.CreditCardDateState
import io.mz.checkout.paynow.creditcard.ui.model.CreditCardNumberState
import io.mz.checkout.paynow.creditcard.ui.model.FormState
import kotlin.test.Test
import org.junit.Rule
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class CreditCardFormTest {

  @get:Rule
  val composeRule = createComposeRule()

  @Test
  fun testCreditCardScreenIsDisplayed() {
    composeRule.setContent {
      CreditCardForm()
    }

    composeRule.onNodeWithTag(TestTags.CreditCardForm.CREDIT_CARD_FORM).isDisplayed()
    composeRule.onNodeWithTag(TestTags.CreditCardNumber.CREDIT_CARD_FIELD).isDisplayed()
    composeRule.onNodeWithTag(TestTags.CreditCardDate.CREDIT_CARD_FIELD).isDisplayed()
    composeRule.onNodeWithTag(TestTags.CreditCardCVV.CREDIT_CARD_FIELD).isDisplayed()
    composeRule.onNodeWithTag(TestTags.CreditCardPayButton.CREDIT_CARD_FIELD).isDisplayed()
  }

  @Test
  fun testCreditFormNumberIsTriggered() {
    val creditCardNumber = "4242424242424242"
    val mockOnSelectedCardEntry = mock<(String, CardEntry, Boolean) -> Unit>()
    composeRule.setContent {
      CreditCardForm(
        onSelectedCardEntry = mockOnSelectedCardEntry,
        onPayClicked = {}
      )
    }

    composeRule.onNodeWithTag(TestTags.CreditCardNumber.CREDIT_CARD_FIELD).isDisplayed()

    composeRule.onNodeWithTag(TestTags.CreditCardNumber.CREDIT_CARD_FIELD).performTextInput(
      creditCardNumber
    )
    composeRule.onNodeWithTag(TestTags.CreditCardNumber.CREDIT_CARD_FIELD).assert(
      hasText(creditCardNumber)
    )
    composeRule.onNodeWithTag(TestTags.CreditCardDate.CREDIT_CARD_FIELD).requestFocus()

    verify(mockOnSelectedCardEntry, times(3)).invoke(any(), any(), any())
  }

  @Test
  fun testCreditFormCompletedIsTriggered() {
    val creditCardNumber = "4242424242424242"
    val date = "122026"
    val cvv = "1234"
    val mockOnPayClicked = mock<() -> Unit>()
    val fakeFormState = FormState(
      CreditCardNumberState(number = creditCardNumber),
      date = CreditCardDateState(expireAt = date),
      cvv = CreditCardCVVState(cvv = cvv)
    )

    composeRule.setContent {
      CreditCardForm(
        formState = { fakeFormState },
        onPayClicked = mockOnPayClicked
      )
    }

    composeRule.onNodeWithTag(TestTags.CreditCardNumber.CREDIT_CARD_FIELD).isDisplayed()

    composeRule.onNodeWithTag(TestTags.CreditCardNumber.CREDIT_CARD_FIELD).performTextInput(
      creditCardNumber
    )
    composeRule.onNodeWithTag(TestTags.CreditCardNumber.CREDIT_CARD_FIELD).assert(
      hasText(creditCardNumber)
    )
    composeRule.onNodeWithTag(TestTags.CreditCardDate.CREDIT_CARD_FIELD).isDisplayed()
    composeRule.onNodeWithTag(TestTags.CreditCardDate.CREDIT_CARD_FIELD).performTextInput(date)

    composeRule.onNodeWithTag(TestTags.CreditCardCVV.CREDIT_CARD_FIELD).isDisplayed()
    composeRule.onNodeWithTag(TestTags.CreditCardCVV.CREDIT_CARD_FIELD).performTextInput(cvv)
    composeRule.onNodeWithTag(TestTags.CreditCardCVV.CREDIT_CARD_FIELD).performImeAction()

    composeRule.onNodeWithTag(TestTags.CreditCardPayButton.CREDIT_CARD_FIELD).isDisplayed()
    composeRule.onNodeWithTag(TestTags.CreditCardPayButton.CREDIT_CARD_FIELD).assertIsEnabled()
    composeRule.onNodeWithTag(TestTags.CreditCardPayButton.CREDIT_CARD_FIELD).performClick()

    verify(mockOnPayClicked, times(1)).invoke()
  }
}
