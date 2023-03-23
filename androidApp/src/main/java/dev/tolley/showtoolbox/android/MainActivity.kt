package dev.tolley.showtoolbox.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {

    private val showTimerUI = ShowTimerUI()

    private var showConfiguration = mutableMapOf<String, Any>(
        "name" to "The Hunchback of Notre Dame",
        "isInterval" to true
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    ShowToolboxNavHost(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    )
                }
            }
        }
        showTimerUI.startTextUpdates()
    }

    @Composable
    fun ShowToolboxNavHost(
        modifier: Modifier = Modifier,
        navController: NavHostController = rememberNavController(),
        startDestination: String = "showtimer-start"
    ) {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = startDestination
        ) {
            composable("showtimer-start") {
                ShowTimerView1(
                    navigateToAct1 = { navController.navigate("showtimer-act1") },
                    showConfiguration = showConfiguration
                )
            }
            composable("showtimer-act1") {
                ShowTimerView2(showConfiguration = showConfiguration)
            }
        }
    }

}
