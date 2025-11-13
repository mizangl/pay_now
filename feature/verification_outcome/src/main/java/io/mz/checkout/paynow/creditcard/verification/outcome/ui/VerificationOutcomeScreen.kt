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

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.mz.checkout.paynow.creditcard.verification.outcome.R
import io.mz.checkout.paynow.creditcard.verification.outcome.ui.theme.ColorFailure
import io.mz.checkout.paynow.creditcard.verification.outcome.ui.theme.ColorSuccess

@Composable
fun OutcomeScreen(
    modifier: Modifier = Modifier,
    outcomeText: String,
    onBackPressed: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        OutcomeText(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .testTag(TestTags.VerificationOutcomeScreen.OUTCOME_RESULT),
            outcomeText = outcomeText
        )

        OutlinedButton(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
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
fun OutcomeText(modifier: Modifier = Modifier, outcomeText: String) {
    val isSuccess = outcomeText.equals("success", ignoreCase = true)
    val isFailure = outcomeText.equals("failure", ignoreCase = true)
    val color = when {
        isSuccess -> ColorSuccess
        isFailure -> ColorFailure
        else -> ColorFailure
    }

    val transition = rememberInfiniteTransition(label = "pulse")
    val scale by transition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val label = when {
        isSuccess -> stringResource(R.string.result_approved)
        isFailure -> stringResource(R.string.result_declined)
        else -> stringResource(id = R.string.result_declined)
    }

    OutcomeContent(modifier, scale, color, isSuccess, label)
}

@Composable
private fun OutcomeContent(
    modifier: Modifier,
    scale: Float,
    color: Color,
    isSuccess: Boolean,
    label: String
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(160.dp)) {
                val radius = (size.minDimension / 2f) * scale
                drawCircle(color = color.copy(alpha = 0.25f), radius = radius)
                drawCircle(color = color, radius = size.minDimension / 2f * 0.9f)
            }

            val iconChar = if (isSuccess) "\u2713" else "\u2715"
            Text(
                iconChar,
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(text = label, textAlign = TextAlign.Center, color = color)
    }
}

@Preview
@Composable
private fun OutcomeScreenPreview() {
    OutcomeScreen(outcomeText = "success")
}
