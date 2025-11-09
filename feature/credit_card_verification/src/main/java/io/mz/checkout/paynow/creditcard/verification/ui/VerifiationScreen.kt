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
package io.mz.checkout.paynow.creditcard.verification.ui

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.mz.checkout.paynow.creditcard.verification.ui.model.VerificationFailure
import io.mz.checkout.paynow.creditcard.verification.ui.model.VerificationInProgress
import io.mz.checkout.paynow.creditcard.verification.ui.model.VerificationSuccess
import io.mz.checkout.paynow.creditcard.verification.R

@Composable
fun VerificationScreen(
  modifier: Modifier = Modifier,
  token: String = "",
  viewModel: VerificationViewModel = hiltViewModel(),
  onBackPressed: () -> Unit = {}
) {
  LaunchedEffect(Unit) {
    viewModel.processPayment(token)
  }

  Box(
    modifier = Modifier.fillMaxSize()
  ) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (val current = state) {
      VerificationInProgress -> CircularProgressIndicator(
        modifier = Modifier.align(Alignment.Center)
      )
      is VerificationSuccess -> WebView(current.url)

      is VerificationFailure -> Error(
        modifier = Modifier.align(Alignment.Center),
        state = current
      )
    }
  }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebView(token: String) {
  AndroidView(
    modifier = Modifier
      .fillMaxSize()
      .testTag("webview"),
    factory = {
      WebView(it).apply {
        layoutParams = ViewGroup.LayoutParams(
          ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.MATCH_PARENT
        )
        settings.javaScriptEnabled = true
      }
    },
    update = {
      it.loadUrl(token)
    }
  )
}

@Composable
fun Error(
  modifier: Modifier = Modifier,
  state: VerificationFailure
) {
  Column(
    modifier = modifier
      .padding(horizontal = 16.dp)
      .fillMaxWidth()
  ) {
    Text(
      modifier = Modifier
        .padding(vertical = 8.dp)
        .fillMaxWidth(),
      text = stringResource(state.id),
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedButton(
      modifier = Modifier.fillMaxWidth(),
      onClick = { },
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
private fun ErrorPreview() {
  Error(
    state = VerificationFailure(R.string.verification_error)
  )
}
