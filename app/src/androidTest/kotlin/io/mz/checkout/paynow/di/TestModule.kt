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
import io.mz.checkout.paynow.creditcard.processor.interceptor.AuthInterceptor as CreditCardProcessorAuthInterceptor
import io.mz.checkout.paynow.dispatcher.MOCK_SERVER
import io.mz.checkout.paynow.payment.processor.api.PaymentProcessorApi
import io.mz.checkout.paynow.payment.processor.di.ApiModule as ApiModulePaymentProcessor
import io.mz.checkout.paynow.payment.processor.di.ProcessorClient
import io.mz.checkout.paynow.payment.processor.interceptor.AuthInterceptor as PaymentProcessorAuthInterceptor
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit

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
    retrofit: Retrofit.Builder
  ): PaymentProcessorApi {
    return retrofit.baseUrl(MOCK_SERVER)
      .client(client)
      .build()
      .create((PaymentProcessorApi::class.java))
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
    retrofit: Retrofit.Builder
  ): TokenApi {
    return retrofit.baseUrl(MOCK_SERVER)
      .client(client)
      .build()
      .create((TokenApi::class.java))
  }

  @Provides
  @Singleton
  @TokenClient
  fun provideHttpClient(): OkHttpClient {
    return OkHttpClient.Builder().addInterceptor(PaymentProcessorAuthInterceptor()).build()
  }
}
