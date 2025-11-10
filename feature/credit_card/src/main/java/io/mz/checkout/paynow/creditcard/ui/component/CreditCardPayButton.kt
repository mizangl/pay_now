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

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.mz.checkout.paynow.creditcard.ui.model.FormState

@Composable
fun CreditCardPayButton(
  modifier: Modifier = Modifier,
  currentPayClicked: () -> Unit,
  formState: () -> FormState
) {
  val formState = formState()

  OutlinedButton(
    modifier = modifier.semantics(mergeDescendants = true) {
      testTag = TestTags.CreditCardPayButton.OUTLINED_BUTTON
    },
    onClick = { currentPayClicked() },
    shape = MaterialTheme.shapes.extraLarge.copy(all = CornerSize(2.dp)),
    colors = ButtonColors(
      containerColor = Color.Black.copy(alpha = 0.9f),
      contentColor = Color.White.copy(alpha = 0.9f),
      disabledContentColor = Color.White.copy(alpha = 0.6f),
      disabledContainerColor = Color.Black.copy(alpha = 0.4f)
    ),
    enabled = formState.payButtonEnabled()
  ) {
    if (formState.isProcessing) {
      CircularProgressIndicator(
        color = Color.White.copy(alpha = 0.9f),
        trackColor = Color.White.copy(alpha = 0.6f)
      )
    } else {
      Text(
        "Pay",
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.headlineMedium
      )
    }
  }
}
