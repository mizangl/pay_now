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

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mz.checkout.paynow.payment.processor.datasource.ConfigurationDataSource
import io.mz.checkout.paynow.payment.processor.datasource.ProcessorDataSource
import io.mz.checkout.paynow.payment.processor.datasource.impl.ConfigurationDataSourceImpl
import io.mz.checkout.paynow.payment.processor.datasource.impl.ProcessorRemoteDataSource
import io.mz.checkout.paynow.payment.processor.repository.ConfigurationRepository
import io.mz.checkout.paynow.payment.processor.repository.PaymentProcessorRepository
import io.mz.checkout.paynow.payment.processor.repository.impl.BasicConfigurationRepository
import io.mz.checkout.paynow.payment.processor.repository.impl.PaymentProcessorRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface CoreModule {

  @Binds
  @Singleton
  fun bindPaymentProcessorRepository(
    impl: PaymentProcessorRepositoryImpl
  ): PaymentProcessorRepository

  @Binds
  @Singleton
  fun bindProcessorDataSource(impl: ProcessorRemoteDataSource): ProcessorDataSource

  @Binds
  @Singleton
  fun bindConfigurationDataSource(
    impl: ConfigurationDataSourceImpl
  ): ConfigurationDataSource

  @Binds
  @Singleton
  fun bindBasicConfigurationRepository(
    repository: BasicConfigurationRepository
  ): ConfigurationRepository
}
