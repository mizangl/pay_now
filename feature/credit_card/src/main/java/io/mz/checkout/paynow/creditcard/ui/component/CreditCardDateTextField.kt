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
package io.mz.checkout.paynow.creditcard.ui.component

import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CreditCardDateTextField(
  modifier: Modifier = Modifier,
  @StringRes placeholder: Int = 0,
  defaultPlaceHolder: Boolean = true,
  errors: List<Int> = emptyList(),
  textFieldState: TextFieldState = rememberTextFieldState(),
  isError: Boolean = false,
  onFocusChanged: (String, Boolean) -> Unit = { _, _ -> },
  onNextClicked: () -> Unit = {}
) {
  val currentOnFocusChanged by rememberUpdatedState(onFocusChanged)

  OutlinedTextField(
    modifier = modifier.onFocusChanged { focusState ->
      currentOnFocusChanged(textFieldState.text.toString(), focusState.isFocused)
    }.testTag(TestTags.CreditCardDate.OUTLINE_TEXT_FIELD),
    state = textFieldState,
    isError = errors.isNotEmpty(),
    supportingText = @Composable {
      TextFieldError(errors = errors)
    },
    placeholder = {
      if (placeholder != Resources.ID_NULL) {
        Text(text = stringResource(id = placeholder))
      } else if (defaultPlaceHolder) {
        PlaceHolderText(default = DEFAULT_CALENDAR_PLACEHOLDER)
      }
    },
    trailingIcon = {
      Icon(Icons.Default.CalendarMonth, contentDescription = null)
    },
    outputTransformation = CreditCardDateOutputTransformation,
    inputTransformation = CreditCardDateInputTransformation,
    lineLimits = TextFieldLineLimits.SingleLine,
    keyboardOptions = KeyboardOptions(
      keyboardType = KeyboardType.NumberPassword,
      imeAction = ImeAction.Next
    ),
    onKeyboardAction = {
      onNextClicked()
    }
  )
}

@Preview
@Composable
private fun CreditCardDateTextFieldPreview() {
  CreditCardDateTextField()
}

object CreditCardDateOutputTransformation : OutputTransformation {
  override fun TextFieldBuffer.transformOutput() {
    val digits = this.toString().filter { it.isDigit() }.take(6)

    val transformed = when {
      digits.isEmpty() -> ""

      digits.length == 1 -> {
        val d = digits[0]
        if (d == '0') "01" else "0$d"
      }

      else -> {
        val rawMonth = digits.substring(0, 2)
        val monthNum = (rawMonth.toIntOrNull() ?: 1).coerceIn(1, 12)
        val month = "%02d".format(monthNum)

        val year = digits.drop(2).take(4)
        if (year.isNotEmpty()) "$month/$year" else month
      }
    }
    replace(0, length, transformed)
  }
}

object CreditCardDateInputTransformation : InputTransformation {
  override fun TextFieldBuffer.transformInput() {
    val digits = asCharSequence().filter { it.isDigit() }.take(6)

    val transformed = when {
      digits.isEmpty() -> ""
      // Do NOT auto-pad single digit months; wait for 2 digits
      digits.length < 2 -> digits
      else -> {
        val rawMonth = digits.substring(0, 2)
        val monthNum = (rawMonth.toIntOrNull() ?: 1).coerceIn(1, 12)
        val month = "%02d".format(monthNum)

        val year = digits.drop(2)
        if (year.isNotEmpty()) "$month/$year" else month
      }
    }

    // Avoid redundant edits
    val current = this.toString()
    if (current != transformed) {
      replace(0, length, transformed)
    }
  }
}

private const val DEFAULT_CALENDAR_PLACEHOLDER = "MM/YYYY"
