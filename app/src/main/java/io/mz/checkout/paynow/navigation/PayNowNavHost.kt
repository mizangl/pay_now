package io.mz.checkout.paynow.navigation

import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Composable
fun PayNowNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {

    NavHost(
        navController = navController,
        startDestination = Main,
        modifier = modifier
    ) {

        composable<Main>() {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Button(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(24.dp)
                        .fillMaxWidth(),
                    onClick = { navController.navigate(Process) }
                ) {
                    Text("Pay")
                }
            }
        }

        composable<Process>() {
            AndroidView(
                modifier = Modifier.fillMaxSize().testTag("webview"),
                factory = {
                    WebView(it).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                }, update = {
                    it.loadUrl("https://www.google.com")
                })
        }

        composable<Result>(
            deepLinks = listOf(
                navDeepLink<Result>(basePath = "$uri/result")
            )
        ) { backStackEntry ->
            val processResult = backStackEntry.toRoute<Result>().processResult
            Box {
                Text(processResult)
            }
        }
    }
}

@Serializable
data object Main

@Serializable
data object Process

@Serializable
data class Result(val processResult: String)

val uri = "paynow://callback-processing"