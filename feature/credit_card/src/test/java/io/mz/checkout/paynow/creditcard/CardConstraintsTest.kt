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
package io.mz.checkout.paynow.creditcard

import io.mz.checkout.paynow.creditcard.ui.model.DateConstraint
import io.mz.checkout.paynow.creditcard.ui.model.DefaultDateConstraint
import io.mz.checkout.paynow.common.constraint.Constraint
import kotlin.test.Test
import kotlin.test.assertTrue

class CardConstraintsTest {

  @Test
  fun `test card expired constraint`() {
    val errors =
      Constraint.validate(value = "10/2022", constraints = listOf(DefaultDateConstraint))

    assertTrue(errors.isNotEmpty())
  }

  @Test
  fun `test card is not expired constraint`() {
    val constraint = DateConstraint()

    val errors =
      Constraint.validate(value = "11/2026", constraints = listOf(DefaultDateConstraint))

    assertTrue(errors.isEmpty())
  }
}
