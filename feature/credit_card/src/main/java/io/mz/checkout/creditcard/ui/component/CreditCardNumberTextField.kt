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
package io.mz.checkout.creditcard.ui.component

import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.insert
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.then
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.mz.checkout.creditcard.ui.component.model.CardEntry
import io.mz.checkout.creditcard.ui.component.model.CardEntryDefault
import io.mz.checkout.creditcard.ui.component.model.CardEntryMatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

// TODO
@Composable
fun CreditCardNumberTextField(
  modifier: Modifier = Modifier,
  isError: Boolean = false,
  errors: List<Int> = emptyList(),
  @StringRes placeholder: Int = 0,
  showPlaceHolder: Boolean = true,
  creditCardNumberState: CreditCardNumberState = rememberCreditCardNumberState(),
  onFocusChanged: (String, CardEntry, Boolean) -> Unit = { _, _, _ -> },
  onNextClicked: () -> Unit = {}
) {
  val entry by creditCardNumberState.currentEntry.collectAsStateWithLifecycle()

  val currentOnFocusChanged by rememberUpdatedState(onFocusChanged)
  val currentOnNextClicked by rememberUpdatedState(onNextClicked)

  OutlinedTextField(
    modifier = modifier
      .onFocusChanged { focusState ->
        currentOnFocusChanged(
          creditCardNumberState.value.toString(),
          entry,
          focusState.isFocused
        )
      }
      .testTag(TestTags.CreditCardNumber.OUTLINE_TEXT_FIELD),
    isError = errors.isNotEmpty(),
    state = creditCardNumberState.textFieldState,
    supportingText = @Composable {
      TextFieldError(errors = errors)
    },
    placeholder = {
      if (placeholder != Resources.ID_NULL) {
        Text(text = stringResource(id = placeholder))
      } else if (showPlaceHolder) {
        PlaceHolderText(default = DEFAULT_CARD_PLACEHOLDER)
      }
    },
    inputTransformation =
    InputTransformation.maxLength(entry.numberLength).then {
      if (!this.asCharSequence().isDigitsOnly()) {
        revertAllChanges()
      }
    },
    trailingIcon = @Composable {
      Image(
        painter = painterResource(entry.resource),
        contentDescription = null
      ) // TODO accessibility
    },
    outputTransformation = CreditCardNumberOutputTransformation(entry.groups),
    lineLimits = TextFieldLineLimits.SingleLine,
    keyboardOptions = KeyboardOptions(
      keyboardType = KeyboardType.NumberPassword,
      imeAction = ImeAction.Next
    ),
    onKeyboardAction = {
      currentOnNextClicked()
    }
  )
}

@Preview
@Composable
private fun CreditCardNumberTextFieldPreview() {
  CreditCardNumberTextField()
}

@Composable
fun rememberCreditCardNumberState(
  initialText: String = "",
  textFieldState: TextFieldState = rememberTextFieldState(initialText = initialText),
  coroutineScope: CoroutineScope = rememberCoroutineScope(),
  cardEntries: List<CardEntry> = emptyList()
): CreditCardNumberState {
  return remember(
    textFieldState,
    coroutineScope,
    cardEntries
  ) {
    CreditCardNumberState(
      textFieldState = textFieldState,
      coroutineScope = coroutineScope,
      cardEntries = cardEntries
    )
  }
}

/**
 * Hoist [CreditCardNumberTextField] state
 *
 * @property textFieldState The current state of the associated text field.
 * @property coroutineScope used as the scope ffor the [snapshotFlow]
 * @property cardEntries A list of [CardEntry]
 * @property currentEntry A reactive `StateFlow` instance that emits the current matching [CardEntry]
 * @property value Provides the current raw text input from the [TextFieldState]
 */
data class CreditCardNumberState(
  val textFieldState: TextFieldState,
  val coroutineScope: CoroutineScope,
  val cardEntries: List<CardEntry> = emptyList(),
  val defaultDebounce: Long = 150L
) {
  val currentEntry: StateFlow<CardEntry> = snapshotFlow { textFieldState.text }
    .debounce(defaultDebounce)
    .map { CardEntryMatcher.match(it.toString(), cardEntries) }
    .stateIn(
      scope = coroutineScope,
      started = SharingStarted.Lazily,
      initialValue = CardEntryDefault
    )

  val value: CharSequence
    get() = textFieldState.text
}

/**
 * A class for transforming the output representation of a credit card number in a text field.
 *
 * @property grouping A list of integers indicating the number of digits in each group. For example,
 * a grouping of [4, 4, 4, 4] will separate the text into chunks of 4 digits, common for credit card formats.
 * @property char The masking character used for the concealed portion of the credit card number. Defaults to `CHAR_DEFAULT`.
 */
class CreditCardNumberOutputTransformation(
  private val grouping: List<Int>,
  private val char: String = CHAR_DEFAULT
) : OutputTransformation {

  private val groupEnds: IntArray = run {
    val ends = mutableListOf<Int>()
    var acc = 0
    for (g in grouping) {
      if (g > 0) {
        acc += g
        ends += acc
      }
    }
    ends.toIntArray()
  }

  override fun TextFieldBuffer.transformOutput() {
    val rawLen = length
    if (rawLen == 0 || groupEnds.isEmpty()) return

    var maskCount = 0
    for (end in groupEnds) {
      if (end < rawLen) maskCount = end else break
    }
    if (maskCount > 0) {
      replace(0, maskCount, char.repeat(maskCount))
    }

    for (i in groupEnds.size - 2 downTo 0) {
      val boundary = groupEnds[i]
      if (rawLen > boundary) {
        insert(boundary, " ")
      }
    }
  }
}

private const val CHAR_DEFAULT = "\u2022"
private const val DEFAULT_CARD_PLACEHOLDER = "XXXX XXXX XXXX XXXX"
