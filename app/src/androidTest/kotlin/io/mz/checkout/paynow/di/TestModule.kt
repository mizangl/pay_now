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
package io.mz.checkout.paynow.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mz.checkout.paynow.creditcard.processor.api.TokenApi
import io.mz.checkout.paynow.creditcard.processor.di.ApiModule as ApiModuleCreditCardProcessor
import io.mz.checkout.paynow.creditcard.processor.di.TokenClient
import io.mz.checkout.paynow.creditcard.processor.entity.request.TokenRequest
import io.mz.checkout.paynow.creditcard.processor.entity.response.TokenResponse
import io.mz.checkout.paynow.creditcard.processor.interceptor.AuthInterceptor as CreditCardProcessorAuthInterceptor
import io.mz.checkout.paynow.payment.processor.api.PaymentProcessorApi
import io.mz.checkout.paynow.payment.processor.di.ApiModule as ApiModulePaymentProcessor
import io.mz.checkout.paynow.payment.processor.di.ProcessorClient
import io.mz.checkout.paynow.payment.processor.entity.processor.request.ProcessorRequest
import io.mz.checkout.paynow.payment.processor.entity.processor.response.Link
import io.mz.checkout.paynow.payment.processor.entity.processor.response.Links
import io.mz.checkout.paynow.payment.processor.entity.processor.response.PaymentProcessorResponse
import io.mz.checkout.paynow.payment.processor.entity.processor.response.Self
import io.mz.checkout.paynow.payment.processor.interceptor.AuthInterceptor as PaymentProcessorAuthInterceptor
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient

object FakePaymentProcessorApi : PaymentProcessorApi {
  var url: String = ""
  override suspend fun fetchToken(body: ProcessorRequest): PaymentProcessorResponse {
    return PaymentProcessorResponse(
      status = "success",
      id = "id",
      links = Links(
        self = Self(href = "href"),
        actions = Link(href = "href"),
        redirect = Link(href = url)
      )
    )
  }
}

@Module
@TestInstallIn(
  components = [SingletonComponent::class],
  replaces = [ApiModuleCreditCardProcessor::class, ApiModulePaymentProcessor::class]
)
object TestModule {

  @Provides
  @Singleton
  fun provideTokenApi(
    @ProcessorClient client: OkHttpClient,
    json: Json
  ): PaymentProcessorApi {
    return FakePaymentProcessorApi
    /*return object : PaymentProcessorApi {
      override suspend fun fetchToken(body: ProcessorRequest): PaymentProcessorResponse {
        return PaymentProcessorResponse(
          status = "success",
          id = "id",
          links = Links(
            self = Self(href = "href"),
            actions = Link(href = "href"),
            redirect = Link(href = "href")
          )
        )
      }
    }*/
  }

  @Provides
  @Singleton
  @ProcessorClient
  fun provideProcessorHttpClient(): OkHttpClient {
    return OkHttpClient.Builder().addInterceptor(CreditCardProcessorAuthInterceptor()).build()
  }

  @Provides
  @Singleton
  fun provideProcessorApi(
    @TokenClient client: OkHttpClient,
    json: Json
  ): TokenApi {
    return object : TokenApi {
      override suspend fun fetchToken(body: TokenRequest): TokenResponse {
        return TokenResponse("token")
      }
    }
  }

  @Provides
  @Singleton
  @TokenClient
  fun provideHttpClient(): OkHttpClient {
    return OkHttpClient.Builder().addInterceptor(PaymentProcessorAuthInterceptor()).build()
  }
}
