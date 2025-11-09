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
package io.mz.checkout.creditcard.verification.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import io.mz.checkout.creditcard.verification.ui.VerificationScreen
import kotlinx.serialization.Serializable

@Serializable
data class VerificationRoute(val token: String)

fun NavGraphBuilder.verificationScreen(
  onBackPressed: () -> Unit = {}
) {
  composable<VerificationRoute> { backStackEntry ->
    val token = backStackEntry.toRoute<VerificationRoute>().token
    VerificationScreen(
      token = token,
      onBackPressed = onBackPressed
    )
  }
}
