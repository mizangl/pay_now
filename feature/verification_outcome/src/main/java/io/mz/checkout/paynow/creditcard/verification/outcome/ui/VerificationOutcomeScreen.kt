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
package io.mz.checkout.paynow.creditcard.verification.outcome.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.mz.checkout.paynow.creditcard.verification.outcome.R
import kotlin.math.PI
import kotlin.math.sin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

private const val animationDuration = 1200

private const val waveAmplitude = 0.06f

private const val wavelength = 1.2f

private const val waveStep = 40f

@Composable
fun OutcomeScreen(
  modifier: Modifier = Modifier,
  viewModel: VerificationViewModel = hiltViewModel(),
  outcomeText: String,
  onBackPressed: () -> Unit = {}
) {
  viewModel.setOutcome(outcomeText)
  val state by viewModel.outComeState.collectAsStateWithLifecycle()

  OutcomeContent(
    modifier = modifier,
    state = state,
    onBackPressed = onBackPressed
  )
}

@Composable
fun OutcomeContent(
  modifier: Modifier = Modifier,
  state: VerificationOutcome,
  onBackPressed: () -> Unit
) {
  val fillProgress = remember { Animatable(0f) }
  val wavePhase = remember { Animatable(0f) }
  LaunchedEffect(Unit) {
    coroutineScope {
      launch {
        fillProgress.animateTo(
          targetValue = 1f,
          animationSpec = tween(durationMillis = 2200, easing = FastOutSlowInEasing)
        )
      }
      launch {
        wavePhase.animateTo(
          targetValue = (2f * PI).toFloat(),
          animationSpec = tween(durationMillis = animationDuration, easing = LinearEasing)
        )
      }
    }
  }

  var containerTopPx by remember { mutableFloatStateOf(0f) }
  var backButtonTopPx by remember { mutableFloatStateOf(Float.NaN) }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .onGloballyPositioned { coords ->
        containerTopPx = coords.positionInRoot().y
      },
    contentAlignment = Alignment.Center
  ) {
    Canvas(modifier = Modifier.matchParentSize()) {
      val bottom = if (backButtonTopPx.isNaN()) size.height else backButtonTopPx.coerceIn(
        0f,
        size.height
      )
      if (bottom > 0f) {
        clipRect(left = 0f, top = 0f, right = size.width, bottom = bottom) {
          val amplitude = size.height * waveAmplitude
          val wavelength = size.width / wavelength
          val baselineY = (bottom * (1f - fillProgress.value)).coerceIn(0f, bottom)

          val wave = Path().apply {
            moveTo(0f, bottom)
            lineTo(0f, baselineY)
            var x = 0f
            val step = size.width / waveStep
            while (x <= size.width + step) {
              val phase: Double =
                (2.0 * PI * (x.toDouble() / wavelength.toDouble())) + wavePhase.value.toDouble()
              val y = (baselineY - amplitude * sin(phase)).toFloat()
              lineTo(x.coerceAtMost(size.width), y)
              x += step
            }
            lineTo(size.width, bottom)
            close()
          }
          drawPath(path = wave, color = state.color)
        }
      }
    }

    OutcomeText(
      modifier = Modifier
        .padding(horizontal = 16.dp)
        .testTag(TestTags.VerificationOutcomeScreen.OUTCOME_RESULT),
      state = state
    )

    OutlinedButton(
      modifier = Modifier
        .padding(horizontal = 16.dp)
        .fillMaxWidth()
        .align(Alignment.BottomCenter)
        .onGloballyPositioned { coords ->
          val buttonTopInRoot = coords.positionInRoot().y
          backButtonTopPx = (buttonTopInRoot - containerTopPx)
        },
      onClick = { onBackPressed() },
      shape = MaterialTheme.shapes.extraLarge.copy(all = CornerSize(2.dp)),
      colors = ButtonColors(
        containerColor = Color.Black.copy(alpha = 0.9f),
        contentColor = Color.White.copy(alpha = 0.9f),
        disabledContentColor = Color.White.copy(alpha = 0.6f),
        disabledContainerColor = Color.Black.copy(alpha = 0.4f)
      )
    ) {
      Text(
        text = stringResource(R.string.navigate_back),
        textAlign = TextAlign.Center
      )
    }
  }
}

@Composable
fun OutcomeText(modifier: Modifier = Modifier, state: VerificationOutcome) {
  Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
    Box(contentAlignment = Alignment.Center) {
      val iconChar = if (state is Success) "\u2713" else "\u2715"
      Text(
        iconChar,
        color = Color.White,
        style = MaterialTheme.typography.headlineLarge,
        textAlign = TextAlign.Center
      )
    }

    Spacer(modifier = Modifier.height(24.dp))
    Text(
      text = stringResource(state.message),
      textAlign = TextAlign.Center,
      color = Color.White
    )
  }
}

@Preview
@Composable
private fun OutcomeContentPreview() {
  OutcomeContent(state = Success, onBackPressed = {})
}
