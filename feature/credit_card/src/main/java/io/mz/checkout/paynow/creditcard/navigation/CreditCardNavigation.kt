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
package io.mz.checkout.paynow.creditcard.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import io.mz.checkout.paynow.creditcard.ui.CreditCardScreen
import kotlinx.serialization.Serializable

@Serializable
data object CreditCardRoute

fun NavController.navigateToCreditCard(navOptions: NavOptions) =
  navigate(CreditCardRoute, navOptions)

fun NavGraphBuilder.creditCardScreen(
  onPayClicked: (String) -> Unit = {}
) {
  composable<CreditCardRoute> {
    CreditCardScreen(
      onPayClicked = onPayClicked
    )
  }
}
