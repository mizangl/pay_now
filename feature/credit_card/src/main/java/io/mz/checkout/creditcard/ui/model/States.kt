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
package io.mz.checkout.creditcard.ui.model

import androidx.compose.runtime.Stable

@Stable
data class FormState(
  val number: CreditCardNumberState = CreditCardNumberState(),
  val cvv: CreditCardCVVState = CreditCardCVVState(),
  val date: CreditCardDateState = CreditCardDateState()
) {

  fun isValid(): Boolean {
    return number.errors.isEmpty() && cvv.errors.isEmpty() && date.errors.isEmpty()
  }

  fun isNumberValid(): Boolean {
    return number.errors.isEmpty()
  }

  fun isCVVValid(): Boolean {
    return cvv.errors.isEmpty()
  }

  fun isDateValid(): Boolean {
    return date.errors.isEmpty()
  }

  fun isNoEmptyField(): Boolean {
    return number.number.isNotEmpty() || cvv.cvv.isNotEmpty() || date.expireAt.isNotEmpty()
  }

  fun payButtonEnabled(): Boolean = isValid() && isNoEmptyField()
}

@Stable
data class CreditCardNumberState(
  val number: String = "",
  val id: Long = 0,
  val cvvLength: Int = 0,
  val errors: List<Int> = emptyList()
)

@Stable
data class CreditCardCVVState(
  val cvv: String = "",
  val errors: List<Int> = emptyList()
)

@Stable
data class CreditCardDateState(
  val expireAt: String = "",
  val errors: List<Int> = emptyList()
)
