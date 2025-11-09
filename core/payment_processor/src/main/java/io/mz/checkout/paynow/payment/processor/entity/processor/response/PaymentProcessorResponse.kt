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
package io.mz.checkout.paynow.payment.processor.entity.processor.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class PaymentProcessorResponse(
  val id: String,
  val status: String,
  @JsonNames("_links")
  val links: Links
)

@Serializable
data class Links(
  val self: Self,
  val actions: Link,
  val redirect: Link
)

@Serializable
data class Self(val href: String)

@Serializable
data class Link(val href: String)
