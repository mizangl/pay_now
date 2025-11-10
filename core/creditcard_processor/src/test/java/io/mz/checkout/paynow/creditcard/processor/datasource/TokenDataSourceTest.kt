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
package io.mz.checkout.paynow.creditcard.processor.datasource

import io.mz.checkout.paynow.creditcard.processor.api.TokenApi
import io.mz.checkout.paynow.creditcard.processor.datasource.impl.TokenRemoteDataSource
import io.mz.checkout.paynow.creditcard.processor.entity.request.TokenRequest
import io.mz.checkout.paynow.creditcard.processor.entity.response.TokenResponse
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import okio.IOException

class TokenDataSourceTest {

  @Test
  fun `test data source return token`() = runTest {
    val expectedTokenResponse = TokenResponse("token")

    val fakeApi = object : TokenApi {
      override suspend fun fetchToken(body: TokenRequest): TokenResponse {
        return TokenResponse("token")
      }
    }
    val dataSource = TokenRemoteDataSource(fakeApi)

    val tokenResponse = dataSource.fetchToken(
      TokenRequest(number = "", expiryMonth = "", expiryYear = "", cvv = "")
    )

    assertEquals(expectedTokenResponse, tokenResponse)
  }

  @Test
  fun `test data source with exception`() = runTest {
    val expectedTokenResponse = TokenResponse("")

    val fakeApi = object : TokenApi {
      override suspend fun fetchToken(body: TokenRequest): TokenResponse {
        throw IOException()
      }
    }
    val dataSource = TokenRemoteDataSource(fakeApi)

    val tokenResponse = dataSource.fetchToken(
      TokenRequest(number = "", expiryMonth = "", expiryYear = "", cvv = "")
    )

    assertEquals(expectedTokenResponse, tokenResponse)
  }
}
