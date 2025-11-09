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
package io.mz.checkout.paynow.payment.processor.entity.processor.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProcessorRequest(
  val source: Source,
  val amount: Int = 6540,
  val currency: String = "GBP",
  @SerialName("3ds")
  val threeDS: ThreeDS = ThreeDS(),
  @SerialName("success_url")
  val successUrl: String,
  @SerialName("failure_url")
  val failureUrl: String
)

@Serializable
data class Source(
  val type: String = "token",
  val token: String
)

@Serializable
data class ThreeDS(
  val enabled: Boolean = true
)

/*

{
    "source": {
        "type": "token",
        "token": "tok_xow4j7zvpk3unbrr7cccb55j7q"
    },
    "amount": 6540,
    "currency": "GBP",
    "3ds": {
        "enabled": true
    },
    "success_url": "https://example.com/payments/success",
    "failure_url": "https://example.com/payments/fail"
}

 */
