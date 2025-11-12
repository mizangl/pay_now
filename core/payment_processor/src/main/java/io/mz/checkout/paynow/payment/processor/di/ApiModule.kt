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
package io.mz.checkout.paynow.payment.processor.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mz.checkout.paynow.payment.processor.api.PaymentProcessorApi
import io.mz.checkout.paynow.payment.processor.interceptor.AuthInterceptor
import javax.inject.Qualifier
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

  @Provides
  @Singleton
  fun provideTokenApi(
    @ProcessorClient client: OkHttpClient,
    retrofit: Retrofit.Builder
  ): PaymentProcessorApi {
    return retrofit.client(client).build().create(PaymentProcessorApi::class.java)
  }

  @Provides
  @Singleton
  @ProcessorClient
  fun provideHttpClient(): OkHttpClient {
    return OkHttpClient.Builder().addNetworkInterceptor(
      HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
      }
    ).addInterceptor(AuthInterceptor()).build()
  }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ProcessorClient
