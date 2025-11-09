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
package io.mz.checkout.paynow.creditcard.ui.model

import io.mz.checkout.paynow.common.constraint.Constraint
import io.mz.checkout.paynow.common.validator.Validator
import kotlin.time.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime

class DateValidator : Validator<String> {

  override fun validate(value: String, constraint: Constraint<String>): Boolean {
    val dateValue =
      parseMonthYearToInstant(value, DefaultTimeZone)?.toLocalDateTime(DefaultTimeZone)
        ?: return false
    val constraintDate = LocalDateTime.parse(constraint.value, DefaultLocalDateTimeFormatter)
    return constraintDate <= dateValue
  }

  fun parseMonthYearToInstant(raw: String, tz: TimeZone = TimeZone.UTC): Instant? {
    val parts = raw.trim().split('/')
    if (parts.size != 2) return null

    val month = parts[0].toIntOrNull()?.takeIf { it in 1..12 } ?: return null
    val year = parts[1].toIntOrNull()?.takeIf { it in 1..9999 } ?: return null

    val date = LocalDate(year, month, 1)
    return date.atStartOfDayIn(tz)
  }
}

class CVVValidator : Validator<Int> {
  override fun validate(value: Int, constraint: Constraint<Int>): Boolean {
    return constraint.value == value
  }
}

class LuhnValidator : Validator<String> {
  override fun validate(
    value: String,
    constraint: Constraint<String>
  ): Boolean {
    var isOdd = true
    var sum = 0

    for (index in value.indices.reversed()) {
      val digit = value[index] - '0'
      sum += if (isOdd) digit else (digit * 2).let { (it / 10) + (it % 10) }
      isOdd = !isOdd
    }

    return (sum % 10) == 0
  }
}
