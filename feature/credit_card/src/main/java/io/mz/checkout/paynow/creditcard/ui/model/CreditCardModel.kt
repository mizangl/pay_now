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

import androidx.compose.runtime.Stable
import io.mz.checkout.paynow.creditcard.ui.component.model.CardEntry
import io.mz.checkout.paynow.common.constraint.Constraint

@Stable
data class CreditCardModel(
  val id: Long,
  val innRawPattern: String,
  val iinConstraints: List<Constraint<*>>,
  val cvvConstraint: List<Constraint<*>>,
  val format: FormatModel,
  val enabled: Boolean,
  val resource: String
)

@Stable
data class FormatModel(
  val groups: List<Int>,
  val maxLengths: List<Int>,
  val cvvMaxLength: Int
)

object CardEntryMapper {
  fun fromModel(model: CreditCardModel): CardEntry = CardEntry(
    resource = CardEntry.Companion.resolveDrawable(model.resource),
    pattern = model.innRawPattern.toRegex(),
    groups = model.format.groups,
    numberLength = model.format.maxLengths.max(),
    cvvLength = model.format.cvvMaxLength,
    id = model.id
  )

  fun List<CreditCardModel>.toCardEntryList(): List<CardEntry> =
    map(::fromModel)
}
