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
package io.mz.checkout.creditcard.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.mz.checkout.creditcard.data.repository.CardsRepository
import io.mz.checkout.creditcard.ui.component.model.CardEntry
import io.mz.checkout.creditcard.ui.model.CardEntryMapper.toCardEntryList
import io.mz.checkout.creditcard.ui.model.CreditCardModel
import io.mz.checkout.creditcard.ui.model.DefaultDateConstraint
import io.mz.checkout.creditcard.ui.model.FormState
import io.mz.checkout.paynow.common.constraint.Constraint
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class CreditCardFormViewModel @Inject constructor(
  private val cardsRepository: CardsRepository
) : ViewModel() {

  val cards: StateFlow<List<CreditCardModel>> = flow {
    val entries = cardsRepository.getIssuing()
    emit(entries)
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
  )

  val entries: StateFlow<List<CardEntry>> = cards.map { it.toCardEntryList() }
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

  val formState = MutableStateFlow(FormState())

  fun updateCreditCardNumber(number: String, entry: CardEntry, active: Boolean) {
    val errors = if (active) emptyList() else validateCreditCardNumber(number, entry.id)
    formState.update {
      it.copy(
        number = it.number.copy(
          number = number,
          id = entry.id,
          cvvLength = entry.cvvLength,
          errors = errors
        )
      )
    }
  }

  fun updateCreditCardCVV(cvv: String, active: Boolean) {
    val errors = if (active) emptyList() else validateCreditCardCVV(cvv, formState.value.number.id)
    formState.update {
      it.copy(
        cvv = it.cvv.copy(
          cvv = cvv,
          errors = errors
        )
      )
    }
  }

  fun updateCreditCardDate(expireAt: String, active: Boolean) {
    val errors = if (active) emptyList() else validateCreditCardDate(expireAt)
    formState.update {
      it.copy(
        date = it.date.copy(
          expireAt = expireAt,
          errors = errors
        )
      )
    }
  }

  private fun validateCreditCardNumber(number: String, id: Long): List<Int> {
    if (id == 0L) return emptyList()
    return runValidation(number, cards.value.firstOrNull { it.id == id }?.iinConstraints)
  }

  private fun validateCreditCardCVV(cvv: String, id: Long): List<Int> {
    if (id == 0L) return emptyList()
    return runValidation(cvv.length, cards.value.firstOrNull { it.id == id }?.cvvConstraint)
  }

  private fun validateCreditCardDate(expireAt: String): List<Int> {
    if (expireAt.isEmpty()) return listOf()
    return runValidation(expireAt, listOf(DefaultDateConstraint))
  }

  private inline fun <reified T> runValidation(
    value: T,
    constraints: List<Constraint<T>>?
  ): List<Int> {
    return constraints?.let { constraints ->
      Constraint.validate(constraints, value = value)
    } ?: emptyList()
  }

  fun processForm() {
  }
}
