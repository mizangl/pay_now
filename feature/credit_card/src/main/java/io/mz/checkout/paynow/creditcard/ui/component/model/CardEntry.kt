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
package io.mz.checkout.paynow.creditcard.ui.component.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Stable
import io.mz.checkout.paynow.creditcard.R

/**
 * A data class representing an entry for card details. This class is primarily used to define
 * the characteristics of a specific card type and its validation rules.
 *
 * * @property id A unique identifier for this card entry, typically used to distinguish it from other card entries.
 * @property resource A drawable resource associated with the card.
 * @property pattern A regular expression to match the card's identifying number.
 * @property groups A list of integers specifying how the card number should be grouped for display purposes.
 * @property numberLength The expected total length of the card number.
 * @property cvvLength The expected length of the card's CVV (Card Verification Value) for validation purposes.
 */
@Stable
data class CardEntry(
  val id: Long,
  @DrawableRes val resource: Int,
  val pattern: Regex,
  val groups: List<Int> = emptyList(),
  val numberLength: Int,
  val cvvLength: Int
) {

  companion object {
    /**
     * Retrieves a drawable resource identifier associated with a specific card type.
     * TODO: needs refactor
     */
    @DrawableRes
    fun resolveDrawable(issuing: String): Int {
      return when (issuing) {
        "american_express" -> R.drawable.american_express
        "diners" -> R.drawable.diners
        "mastercard" -> R.drawable.mastercard
        "visa" -> R.drawable.visa
        else -> R.drawable.default_card
      }
    }
  }
}

val CardEntryDefault = CardEntry(
  id = 0,
  resource = R.drawable.default_card,
  pattern = "".toRegex(),
  groups = listOf(4, 4, 4, 4),
  numberLength = 16,
  cvvLength = 6
)

/**
 * Provides functionality to match a card input with a corresponding card entry
 * based on [CardEntry.pattern].
 */
object CardEntryMatcher {
  const val MIN_LENGTH = 4

  fun match(value: String, cards: List<CardEntry>): CardEntry {
    if (value.isEmpty() || value.length < MIN_LENGTH) return CardEntryDefault
    return cards.firstOrNull { it.pattern.matches(value.subSequence(0, MIN_LENGTH)) }
      ?: CardEntryDefault
  }
}
