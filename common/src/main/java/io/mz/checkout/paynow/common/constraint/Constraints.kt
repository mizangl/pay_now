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
package io.mz.checkout.paynow.common.constraint

import androidx.annotation.StringRes
import io.mz.checkout.paynow.common.validator.Validator
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@MustBeDocumented
annotation class Constraints<T>(
  val validator: KClass<out Validator<T>>
)

interface Constraint<out T> {
  @StringRes
  fun getErrorMessage(): Int

  val value: T

  companion object {

    /**
     * Validates a given value against a list of constraints and returns a list of error messages
     * represented as resource IDs for the constraints that the value fails to satisfy.
     *
     * @param T The type of value being validated.
     * @param constraints A list of constraints to validate the value against.
     * @param value The value to be validated.
     * @return A list of resource IDs representing error messages for the violated constraints.
     */
    inline fun <reified T> validate(
      constraints: List<Constraint<T>>,
      value: T
    ): List<Int> {
      return constraints.map { constraint ->
        val validator =
          constraint::class.findAnnotation<Constraints<T>>()?.validator?.createInstance()
        constraint to validator
      }.mapNotNull { (constraint, validator) ->
        if (validator?.validate(value, constraint) == true) null else constraint.getErrorMessage()
      }
    }
  }
}
