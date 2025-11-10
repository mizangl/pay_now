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
package io.mz.checkout.paynow.payment.processor

import io.mz.checkout.paynow.payment.processor.datasource.ProcessorDataSource
import io.mz.checkout.paynow.payment.processor.entity.processor.request.ProcessorRequest
import io.mz.checkout.paynow.payment.processor.entity.processor.request.Source
import io.mz.checkout.paynow.payment.processor.entity.processor.response.Link
import io.mz.checkout.paynow.payment.processor.entity.processor.response.Links
import io.mz.checkout.paynow.payment.processor.entity.processor.response.PaymentProcessorResponse
import io.mz.checkout.paynow.payment.processor.entity.processor.response.Self
import io.mz.checkout.paynow.payment.processor.repository.impl.PaymentProcessorRepositoryImpl
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest

class PaymentProcessorRepositoryTest {

  @Test
  fun `test payment processor repository`() = runTest {
    val expectedResponse = PaymentProcessorResponse(
      status = "success",
      id = "id",
      links = Links(
        self = Self(href = "href"),
        actions = Link(href = "href"),
        redirect = Link(href = "href")
      )
    )

    val fakeDataSource = object : ProcessorDataSource {
      override suspend fun processAuth(payload: ProcessorRequest):
        Result<PaymentProcessorResponse> {
        return Result.success(
          PaymentProcessorResponse(
            status = "success",
            id = "id",
            links = Links(
              self = Self(href = "href"),
              actions = Link(href = "href"),
              redirect = Link(href = "href")
            )
          )
        )
      }
    }

    val paymentProcessorRepository = PaymentProcessorRepositoryImpl(fakeDataSource)

    val response = paymentProcessorRepository.processAuth(
      ProcessorRequest(
        Source(token = "token"),
        successUrl = "",
        failureUrl = ""
      )
    )

    assertEquals(expectedResponse, response.getOrThrow())
  }
}
