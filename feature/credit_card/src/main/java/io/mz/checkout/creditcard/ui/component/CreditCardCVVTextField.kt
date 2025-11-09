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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.then
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.text.isDigitsOnly

@Composable
fun CreditCardCVVTextField(
  modifier: Modifier = Modifier,
  @StringRes placeholder: Int = 0,
  defaultPlaceHolder: Boolean = true,
  isError: Boolean = false,
  textFieldState: TextFieldState = rememberTextFieldState(),
  errors: List<Int> = emptyList(),
  cvvLength: () -> Int = { 3 },
  onFocusChanged: (String, Boolean) -> Unit = { _, _ -> },
  onDoneClicked: () -> Unit = {}
) {
  var showCode by remember { mutableStateOf(false) }

  val currentOnFocusChanged by rememberUpdatedState(onFocusChanged)

  OutlinedTextField(
    modifier = modifier.onFocusChanged { focusState ->
      currentOnFocusChanged(textFieldState.text.toString(), focusState.isFocused)
    },
    state = textFieldState,
    isError = isError || errors.isNotEmpty(),
    supportingText = @Composable {
      TextFieldError(errors = errors)
    },
    placeholder = {
      if (placeholder != Resources.ID_NULL) {
        Text(text = stringResource(id = placeholder))
      } else if (defaultPlaceHolder) {
        PlaceHolderText(default = DEFAULT_CVV_PLACEHOLDER)
      }
    },
    trailingIcon = {
      IconButton(
        onClick = { showCode = !showCode }
      ) {
        val icon = if (showCode) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff
        val contentDescription = if (showCode) "Hide CVV" else "Show CVV"
        Icon(icon, contentDescription = contentDescription)
      }
    },
    inputTransformation =
    InputTransformation.maxLength(cvvLength()).then {
      if (!this.asCharSequence().isDigitsOnly()) {
        revertAllChanges()
      }
    },
    outputTransformation = CreditCardCVVOutputTransformation(showCode),
    lineLimits = TextFieldLineLimits.SingleLine,
    keyboardOptions = KeyboardOptions(
      keyboardType = KeyboardType.NumberPassword,
      imeAction = ImeAction.Done
    ),
    onKeyboardAction = {
      onDoneClicked()
    }
  )
}

@Preview
@Composable
private fun CreditCardDateTextFieldPreview() {
  CreditCardCVVTextField()
}

data class CreditCardCVVOutputTransformation(val showCVV: Boolean) : OutputTransformation {
  override fun TextFieldBuffer.transformOutput() {
    if (showCVV || length == 0) return
    replace(0, length, CHAR_DEFAULT.repeat(length))
  }
}

private const val DEFAULT_CVV_PLACEHOLDER = "CVV"
private const val CHAR_DEFAULT = "\u2022"
