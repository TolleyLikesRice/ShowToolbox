package dev.tolley.showtoolbox.android

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.tolley.showtoolbox.Showtimer

class MainActivity : ComponentActivity() {

    val currentTime = mutableStateOf("??:??:??")
    lateinit var mainHandler: Handler
    var showtimer = Showtimer()

    private val updateTextTask = object : Runnable {
        override fun run() {
            currentTime.value = showtimer.currentTimeText()
            mainHandler.postDelayed(this, 100)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    GreetingView()
                }
            }
        }
        mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(updateTextTask)
    }

    @Preview
    @Composable
    fun GreetingView() {
        val currentTime by currentTime
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(currentTime)
        }
    }

}
