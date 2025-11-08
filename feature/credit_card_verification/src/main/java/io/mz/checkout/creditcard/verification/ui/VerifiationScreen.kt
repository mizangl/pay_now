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
package io.mz.checkout.creditcard.verification.ui

import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun VerificationScreen(
  modifier: Modifier = Modifier,
  verificationUrl: String = ""
) {
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
      }
    },
    update = {
      it.loadUrl(verificationUrl)
    }
  )
}
