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
package io.mz.checkout.paynow.payment.processor.datasource.impl

import io.mz.checkout.paynow.common.utils.safe
import io.mz.checkout.paynow.payment.processor.api.PaymentProcessorApi
import io.mz.checkout.paynow.payment.processor.datasource.ProcessorDataSource
import io.mz.checkout.paynow.payment.processor.entity.processor.request.ProcessorRequest
import io.mz.checkout.paynow.payment.processor.entity.processor.response.PaymentProcessorResponse
import javax.inject.Inject

class ProcessorRemoteDataSource @Inject constructor(
  private val api: PaymentProcessorApi
) : ProcessorDataSource {
  override suspend fun fetchToken(payload: ProcessorRequest): Result<PaymentProcessorResponse> {
    return safe {
      api.fetchToken(payload)
    }
  }
}
