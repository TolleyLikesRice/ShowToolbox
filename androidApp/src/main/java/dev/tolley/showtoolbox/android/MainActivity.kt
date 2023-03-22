package dev.tolley.showtoolbox.android

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Undo
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
import androidx.compose.ui.unit.em
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

    // State
    private var isHouseOpen: Boolean = false
    private var haveClearance: Boolean = false

    // Changy on screen stuff
    val currentTime = mutableStateOf("??:??:??")
    private val houseOpenButtonColor = mutableStateOf(Color(144, 194, 234, 0))
    private val houseOpenButtonTextColor = mutableStateOf(Color(255, 255, 255, 255))
    private val clearanceButtonColor = mutableStateOf(Color(144, 194, 234, 0))
    private val clearanceButtonTextColor = mutableStateOf(Color(255, 255, 255, 255))

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

    private fun houseOpen() {
        if (!isHouseOpen) {
            houseOpenButtonColor.value = Color(144, 194, 234, 255)
            houseOpenButtonTextColor.value = Color(28, 27, 31, 255)
            isHouseOpen = true
        }
    }

    private fun undoHouseOpen() {
        if (isHouseOpen) {
            houseOpenButtonColor.value = Color(144, 194, 234, 0)
            houseOpenButtonTextColor.value = Color(255, 255, 255, 255)
            isHouseOpen = false
        }
    }

    private fun clearance() {
        if (!haveClearance) {
            clearanceButtonColor.value = Color(230, 201, 126, 255)
            clearanceButtonTextColor.value = Color(28, 27, 31, 255)
            haveClearance = true
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
        val houseOpenButtonColor by houseOpenButtonColor
        val houseOpenButtonTextColor by houseOpenButtonTextColor
        val clearanceButtonColor by clearanceButtonColor
        val clearanceButtonTextColor by clearanceButtonTextColor

        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp
        val screenWidth = configuration.screenWidthDp

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(0.95f)
            ) {
                IconButton(onClick = { undoHouseOpen() }) {
                    Icon(Icons.Outlined.Undo, contentDescription = "Localized description")
                }
                IconButton(onClick = { }) {
                    Icon(Icons.Outlined.Settings, contentDescription = "Localized description")
                }
            }
        }

        Column(
            modifier = Modifier.height((screenHeight * 0.5).dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height((screenHeight * 0.5 * 0.15).dp))
            Text(
                text = "The Hunchback of Notre Dame",
                textAlign = TextAlign.Center,
                fontSize = 35.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = (1.4f).em,
                modifier = Modifier.fillMaxWidth(0.9f)
            )
            Spacer(Modifier.height((screenHeight * 0.5 * 0.15).dp))
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

            // House Open
            Button(
                onClick = { houseOpen() },
                colors = buttonColors(houseOpenButtonColor),
                border = BorderStroke(4.dp, Color(144, 194, 234, 255)),
                shape = CardDefaults.outlinedShape,
                modifier = Modifier
                    .height((screenHeight * 0.5 * 0.2).dp)
                    .fillMaxWidth(0.9f)
            ) {
                Text(
                    text = "House Open",
                    color = houseOpenButtonTextColor,
                    fontFamily = incosolataFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = 30.sp,
                )
            }

            Spacer(Modifier.height((screenHeight * 0.5 * 0.05).dp))

            // Clearance
            Button(
                onClick = fun() {
                    clearance()
                },
                colors = buttonColors(clearanceButtonColor),
                border = BorderStroke(4.dp, Color(230, 201, 126, 255)),
                shape = CardDefaults.outlinedShape,
                modifier = Modifier
                    .height((screenHeight * 0.5 * 0.2).dp)
                    .fillMaxWidth(0.9f)
            ) {
                Text(
                    text = "Clearance",
                    color = clearanceButtonTextColor,
                    fontFamily = incosolataFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = 30.sp,
                )
            }
            Spacer(Modifier.height((screenHeight * 0.5 * 0.05).dp))
            Button(
                onClick = fun() {

                },
                colors = buttonColors(Color(16, 201, 122, 0)),
                shape = CardDefaults.outlinedShape,
                border = BorderStroke(4.dp, Color(16, 201, 122, 255)),
                modifier = Modifier
                    .height((screenHeight * 0.5 * 0.4).dp)
                    .fillMaxWidth(0.9f)
            ) {
                Text(
                    text = "Start Act 1",
                    color = Color(255, 255, 255, 255),
                    fontFamily = incosolataFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = 30.sp
                )
            }
            Spacer(Modifier.height((screenHeight * 0.5 * 0.1).dp))
        }
    }

}
