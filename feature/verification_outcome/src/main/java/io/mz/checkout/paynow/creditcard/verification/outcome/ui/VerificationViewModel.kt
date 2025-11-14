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
package io.mz.checkout.paynow.creditcard.verification.outcome.ui

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.mz.checkout.paynow.creditcard.verification.outcome.R
import io.mz.checkout.paynow.creditcard.verification.outcome.ui.theme.ColorFailure
import io.mz.checkout.paynow.creditcard.verification.outcome.ui.theme.ColorSuccess
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class VerificationViewModel @Inject constructor() : ViewModel() {

  private val state = MutableStateFlow<VerificationOutcome>(Empty)

  val outComeState: StateFlow<VerificationOutcome> = state.asStateFlow()

  fun setOutcome(outcome: String) {
    val currentState = when (outcome) {
      "success" -> Success
      "failure" -> Failure
      else -> Error
    }
    state.update { currentState }
  }
}

sealed class VerificationOutcome(val color: Color, @StringRes val message: Int)
data object Success : VerificationOutcome(ColorSuccess, R.string.result_approved)
data object Failure : VerificationOutcome(ColorFailure, R.string.result_declined)
data object Error : VerificationOutcome(ColorFailure, R.string.result_error)
data object Empty : VerificationOutcome(Color.White, android.R.string.unknownName)
