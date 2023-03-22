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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.tolley.showtoolbox.R
import dev.tolley.showtoolbox.Showtimer

class MainActivity : ComponentActivity() {

    // UI Stuff
    private val incosolataFamily = FontFamily(
        Font(R.font.inconsolata_extralight, FontWeight.ExtraLight),
        Font(R.font.inconsolata_light, FontWeight.Light),
        Font(R.font.inconsolata_regular, FontWeight.Normal),
        Font(R.font.inconsolata_medium, FontWeight.Medium),
        Font(R.font.inconsolata_semibold, FontWeight.SemiBold),
        Font(R.font.inconsolata_bold, FontWeight.Bold),
        Font(R.font.inconsolata_extrabold, FontWeight.ExtraBold)
    )

    // Changy on screen stuff
    val currentTime = mutableStateOf("??:??:??")

    // idk
    lateinit var mainHandler: Handler
    var showtimer = Showtimer()

    // Functions
    private val updateTextTask = object : Runnable {
        override fun run() {
            currentTime.value = showtimer.currentTimeText()
            mainHandler.postDelayed(this, 100)
        }
    }

    // idk
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    ShowTimerView()
                }
            }
        }
        mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(updateTextTask)
    }

    @Composable
    fun ShowTimerView() {
        val currentTime by currentTime

        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp
        val screenWidth = configuration.screenWidthDp

        Column(
            modifier = Modifier.height((screenHeight * 0.5).dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height((screenHeight * 0.5 * 0.1).dp))
            Text(
                text = "Show Name",
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                fontWeight = FontWeight.Light,
            )
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
        }
        Column(
            modifier = Modifier.height((screenHeight * 0.5).dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.fillMaxHeight(0.5f * 0.05f))
            Button(
                onClick = fun() {

                },
                colors = buttonColors(Color(144, 194, 234, 255)),
                shape = CardDefaults.outlinedShape,
                modifier = Modifier
                    .height((screenHeight * 0.5 * 0.2).dp)
                    .fillMaxWidth(0.9f)
            ) {
                Text(
                    text = "House Open",
                    color = Color(28, 27, 31, 255),
                    fontFamily = incosolataFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = 30.sp,
                )
            }
            Spacer(Modifier.height((screenHeight * 0.5 * 0.05).dp))
            Button(
                onClick = fun() {

                },
                colors = buttonColors(Color(230, 201, 126, 255)),
                shape = CardDefaults.outlinedShape,
                modifier = Modifier
                    .height((screenHeight * 0.5 * 0.2).dp)
                    .fillMaxWidth(0.9f)
            ) {
                Text(
                    text = "Clearance",
                    color = Color(28, 27, 31, 255),
                    fontFamily = incosolataFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = 30.sp,
                )
            }
            Spacer(Modifier.height((screenHeight * 0.5 * 0.05).dp))
            Button(
                onClick = fun() {

                },
                colors = buttonColors(Color(16, 201, 122, 255)),
                shape = CardDefaults.outlinedShape,
                modifier = Modifier
                    .height((screenHeight * 0.5 * 0.4).dp)
                    .fillMaxWidth(0.9f)
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    Text(
                        text = "Start Act 1",
                        color = Color(28, 27, 31, 255),
                        fontFamily = incosolataFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 30.sp,
                    )
                }
            }
            Spacer(Modifier.height((screenHeight * 0.5 * 0.1).dp))
        }
    }

}
