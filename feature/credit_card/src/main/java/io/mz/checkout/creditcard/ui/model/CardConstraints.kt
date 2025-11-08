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

import androidx.annotation.StringRes
import io.mz.checkout.paynow.common.constraint.Constraint
import io.mz.checkout.paynow.common.constraint.Constraints
import io.mz.checkout.paynow.creditcard.R
import kotlin.time.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format

@Constraints<String>(validator = LuhnValidator::class)
data class DigitConstraint(override val value: String) : Constraint<String> {
  @StringRes
  override fun getErrorMessage(): Int = R.string.feature_credit_card_card_digit_error
}

@Constraints<Int>(validator = CVVValidator::class)
data class MaxLengthConstraint(override val value: Int) : Constraint<Int> {
  @StringRes
  override fun getErrorMessage(): Int = R.string.feature_credit_card_max_length_error
}

val DefaultDateConstraint = DateConstraint()

@Constraints<String>(validator = DateValidator::class)
class DateConstraint : Constraint<String> {
  private val timeZone = TimeZone.UTC

  override fun getErrorMessage(): Int = R.string.feature_credit_card_expired_error

  override val value: String
    get() {
      return Clock.System.now().format(DefaultTimeComponentFormatter)
    }
}
