package dev.tolley.showtoolbox.android

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.tolley.showtoolbox.R
import dev.tolley.showtoolbox.Showtimer

class MainActivity : ComponentActivity() {

    val incosolataFamily = FontFamily(
        Font(R.font.inconsolata_extralight, FontWeight.ExtraLight),
        Font(R.font.inconsolata_light, FontWeight.Light),
        Font(R.font.inconsolata_regular, FontWeight.Normal),
        Font(R.font.inconsolata_medium, FontWeight.Medium),
        Font(R.font.inconsolata_semibold, FontWeight.SemiBold),
        Font(R.font.inconsolata_bold, FontWeight.Bold),
        Font(R.font.inconsolata_extrabold, FontWeight.ExtraBold)
    )

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
                    color = MaterialTheme.colorScheme.background
                ) {
                    ShowTimerView()
                }
            }
        }
        mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(updateTextTask)
    }

    @Preview
    @Composable
    fun ShowTimerView() {
        val currentTime by currentTime
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Current Time:",
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                fontWeight = FontWeight.Light
            )
            Text(
                text = currentTime,
                textAlign = TextAlign.Center,
                fontSize = 60.sp,
                fontFamily = incosolataFamily,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(60.dp))
            Button(
                onClick = fun() {

                },
                colors = buttonColors(Color(16, 201, 122, 255))
            ) {
                Text("Start Show")
            }
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = fun() {

                },
                colors = buttonColors(Color(54, 57, 63, 255))
            ) {
                Text(
                    text = "Setup",
                    color = Color(230, 225, 229, 255)
                )
            }
            Spacer(Modifier.height(50.dp))
        }
    }

}
