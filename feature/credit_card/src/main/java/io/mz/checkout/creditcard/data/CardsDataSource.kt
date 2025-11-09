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
package io.mz.checkout.creditcard.data

import io.mz.checkout.creditcard.data.entity.CardContainer
import io.mz.checkout.paynow.common.utils.safe
import javax.inject.Inject
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

interface CardsDataSource {
  suspend fun getCards(): CardContainer
}

class MockCardsDataSource @Inject constructor() : CardsDataSource {
  override suspend fun getCards(): CardContainer {
    return safe {
      Json.decodeFromStream<CardContainer>(
        this.javaClass.classLoader!!.getResourceAsStream(
          "issuing_cards.json"
        )
      )
    }.getOrDefault(CardContainer(data = emptyList()))
  }
}
