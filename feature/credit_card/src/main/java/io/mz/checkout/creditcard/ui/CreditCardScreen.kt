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
package io.mz.checkout.creditcard.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component1
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component2
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.mz.checkout.creditcard.ui.component.CreditCardCVVTextField
import io.mz.checkout.creditcard.ui.component.CreditCardDateTextField
import io.mz.checkout.creditcard.ui.component.CreditCardNumberTextField
import io.mz.checkout.creditcard.ui.component.TestTags
import io.mz.checkout.creditcard.ui.component.model.CardEntry
import io.mz.checkout.creditcard.ui.component.rememberCreditCardNumberState
import io.mz.checkout.creditcard.ui.model.FormState

@Composable
fun CreditCardScreen(
  modifier: Modifier = Modifier,
  viewModel: CreditCardFormViewModel = hiltViewModel(),
  onPayClicked: () -> Unit
) {
  val entries by viewModel.entries.collectAsStateWithLifecycle()

  val formState by viewModel.formState.collectAsStateWithLifecycle()

  CreditCardForm(
    modifier = Modifier
      .fillMaxWidth()
      .testTag(TestTags.CreditCardForm.CREDIT_CARD_FORM),
    cardEntries = entries,
    formState = { formState },
    onSelectedCardEntry = { value, entry, active ->
      viewModel.updateCreditCardNumber(value, entry, active)
    },
    onSelectedCvv = { value, active -> viewModel.updateCreditCardCVV(value, active) },
    onSelectedDate = { value, active -> viewModel.updateCreditCardDate(value, active) },
    onPayClicked = onPayClicked
  )
}

@Composable
fun CreditCardForm(
  modifier: Modifier = Modifier,
  cardEntries: List<CardEntry> = emptyList(),
  onSelectedCardEntry: (String, CardEntry, Boolean) -> Unit = { _, _, _ -> },
  onSelectedCvv: (String, Boolean) -> Unit = { _, _ -> },
  onSelectedDate: (String, Boolean) -> Unit = { _, _ -> },
  formState: () -> FormState = { FormState() },
  onPayClicked: () -> Unit = {}
) {
  val (focusRequesterDate, focusRequesterCVV) = remember { FocusRequester.createRefs() }

  val focusManager = LocalFocusManager.current

  Column(
    modifier = Modifier
      .padding(horizontal = 16.dp)
      .fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(24.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Spacer(modifier = Modifier.weight(1f))

    val creditCardNumberState = rememberCreditCardNumberState(
      cardEntries = cardEntries
    )

    CreditCardNumberTextField(
      modifier = Modifier.fillMaxWidth(),
      onNextClicked = { },
      onFocusChanged = onSelectedCardEntry,
      creditCardNumberState = creditCardNumberState,
      errors = formState().number.errors
    )

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(24.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      CreditCardDateTextField(
        modifier = Modifier
          .weight(1f)
          .focusRequester(focusRequesterDate),
        errors = formState().date.errors,
        onNextClicked = { },
        onFocusChanged = onSelectedDate // onFocusChangedDate,
      )

      CreditCardCVVTextField(
        modifier = Modifier
          .weight(1f)
          .focusRequester(focusRequesterCVV),
        errors = formState().cvv.errors,
        onDoneClicked = { focusManager.clearFocus() },
        onFocusChanged = onSelectedCvv,
        cvvLength = { formState().number.cvvLength }
      )
    }

    Spacer(
      modifier = Modifier.weight(1f)
    )

    // TODO refactor as a component
    OutlinedButton(
      modifier = Modifier
        .align(Alignment.CenterHorizontally)
        .fillMaxWidth(),
      onClick = { },
      shape = MaterialTheme.shapes.extraLarge.copy(all = CornerSize(2.dp)),
      colors = ButtonColors(
        containerColor = Color.Black.copy(alpha = 0.9f),
        contentColor = Color.White.copy(alpha = 0.9f),
        disabledContentColor = Color.White.copy(alpha = 0.6f),
        disabledContainerColor = Color.Black.copy(alpha = 0.4f)
      ),
      enabled = formState().payButtonEnabled()
    ) {
      Text(
        "Pay",
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.headlineMedium
      )
    }
  }
}
