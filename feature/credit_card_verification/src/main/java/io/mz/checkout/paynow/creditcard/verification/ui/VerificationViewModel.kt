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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.mz.checkout.paynow.creditcard.verification.ui.model.VerificationFailure
import io.mz.checkout.paynow.creditcard.verification.ui.model.VerificationInProgress
import io.mz.checkout.paynow.creditcard.verification.ui.model.VerificationState
import io.mz.checkout.paynow.creditcard.verification.ui.model.VerificationSuccess
import io.mz.checkout.paynow.payment.processor.entity.processor.request.ProcessorRequest
import io.mz.checkout.paynow.payment.processor.entity.processor.request.Source
import io.mz.checkout.paynow.payment.processor.repository.ConfigurationRepository
import io.mz.checkout.paynow.payment.processor.repository.PaymentProcessorRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 *
 * This ViewModel provides state management for the verification process
 *
 * @property paymentRepository Handles payment processing by communicating with the appropriate data source.
 * @property configurationRepository Provides configuration for success and failure URLs.
 */
@HiltViewModel
class VerificationViewModel @Inject constructor(
  private val paymentRepository: PaymentProcessorRepository,
  private val configurationRepository: ConfigurationRepository
) : ViewModel() {

  private val mutableState: MutableStateFlow<VerificationState> = MutableStateFlow(
    VerificationInProgress
  )

  val state: StateFlow<VerificationState> = mutableState.asStateFlow()

  fun processPayment(token: String) {
    viewModelScope.launch {
      mutableState.update { VerificationInProgress }
      val configuration = configurationRepository.fetchConfiguration()

      paymentRepository.processAuth(
        ProcessorRequest(
          Source(token = token),
          successUrl = configuration.successUrl,
          failureUrl = configuration.failureUrl
        )
      ).onSuccess { data ->
        mutableState.update { VerificationSuccess(data.links.redirect.href) }
      }.onFailure {
        mutableState.update { VerificationFailure() }
      }
    }
  }
}
