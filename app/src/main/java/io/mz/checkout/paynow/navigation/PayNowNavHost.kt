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
package io.mz.checkout.paynow.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import io.mz.checkout.paynow.creditcard.navigation.CreditCardRoute
import io.mz.checkout.paynow.creditcard.navigation.creditCardScreen
import io.mz.checkout.paynow.creditcard.verification.navigation.VerificationRoute
import io.mz.checkout.paynow.creditcard.verification.navigation.verificationScreen
import io.mz.checkout.paynow.creditcard.verification.outcome.navigation.outcomeScreen

@Composable
fun PayNowNavHost(
  modifier: Modifier = Modifier,
  navController: NavHostController
) {
  NavHost(
    navController = navController,
    startDestination = CreditCardRoute,
    modifier = modifier
  ) {
    creditCardScreen(
      onPayClicked = { token ->
        navController.navigate(VerificationRoute(token = token))
      }
    )

    verificationScreen(
      onBackPressed = {
        navController.popBackStack()
      }
    )

    outcomeScreen(onBackPressed = {
      navController.popBackStack()
    })
  }
}
