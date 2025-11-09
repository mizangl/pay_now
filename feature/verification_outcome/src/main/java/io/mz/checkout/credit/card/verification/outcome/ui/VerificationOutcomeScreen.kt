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
package io.mz.checkout.credit.card.verification.outcome.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.mz.checkout.paynow.creditcard.verification.outcome.R

@Composable
fun OutcomeScreen(
  modifier: Modifier = Modifier,
  outcomeText: String,
  onBackPressed: () -> Unit = {}
) {
  Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
  ) {
    when (outcomeText) {
      "success" -> Text(text = "Approved")
      "failure" -> Text(text = "Declined")
    }

    OutlinedButton(
      modifier = Modifier
        .padding(horizontal = 16.dp)
        .fillMaxWidth()
        .align(Alignment.BottomCenter),
      onClick = { onBackPressed() },
      shape = MaterialTheme.shapes.extraLarge.copy(all = CornerSize(2.dp)),
      colors = ButtonColors(
        containerColor = Color.Black.copy(alpha = 0.9f),
        contentColor = Color.White.copy(alpha = 0.9f),
        disabledContentColor = Color.White.copy(alpha = 0.6f),
        disabledContainerColor = Color.Black.copy(alpha = 0.4f)
      )
    ) {
      Text(
        text = stringResource(R.string.navigate_back),
        textAlign = TextAlign.Center
      )
    }
  }
}

@Preview
@Composable
private fun OutcomeScreenPreview() {
  OutcomeScreen(outcomeText = "success")
}
