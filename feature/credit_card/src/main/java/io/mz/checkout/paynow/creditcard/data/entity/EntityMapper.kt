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
package io.mz.checkout.paynow.creditcard.data.entity

import io.mz.checkout.paynow.creditcard.ui.model.CreditCardModel
import io.mz.checkout.paynow.creditcard.ui.model.DigitConstraint
import io.mz.checkout.paynow.creditcard.ui.model.FormatModel
import io.mz.checkout.paynow.creditcard.ui.model.MaxLengthConstraint

object EntityMapper {

  fun fromEntity(entity: CreditCardEntity): CreditCardModel =
    CreditCardModel(
      id = entity.id,
      innRawPattern = entity.iin,
      iinConstraints = listOf(
        DigitConstraint(value = "")
      ),
      cvvConstraint = listOf(
        MaxLengthConstraint(value = entity.constraints.cvv.length)
      ),
      format = FormatModel(
        groups = entity.format.groups,
        maxLengths = entity.format.maxLengths,
        cvvMaxLength = entity.constraints.cvv.length
      ),
      enabled = entity.enabled,
      resource = entity.name
    )

  fun List<CreditCardEntity>.toModel(): List<CreditCardModel> = map(::fromEntity)
}
