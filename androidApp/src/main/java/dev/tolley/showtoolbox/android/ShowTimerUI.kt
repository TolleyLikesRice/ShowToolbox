package dev.tolley.showtoolbox.android

import android.content.res.Configuration
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Undo
import androidx.compose.material3.*
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
val act1TimeElapsed = mutableStateOf("??:??:??")

val houseOpenButtonColor = mutableStateOf(Color(144, 194, 234, 0))
val houseOpenButtonTextColor = mutableStateOf(Color(255, 255, 255, 255))
val houseOpenText = mutableStateOf("House Open")

val clearanceButtonColor = mutableStateOf(Color(144, 194, 234, 0))
val clearanceButtonTextColor = mutableStateOf(Color(255, 255, 255, 255))
val clearanceText = mutableStateOf("Clearance")

var showtimer = Showtimer()

class ShowTimerUI {

    // idk
    lateinit var mainHandler: Handler

    // Functions
    private val updateTextTask = object : Runnable {
        override fun run() {
            currentTime.value = showtimer.currentTimeText()
            act1TimeElapsed.value = showtimer.timeSince("act1Start")
            mainHandler.postDelayed(this, 100)
        }
    }

    fun startTextUpdates() {
        mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(updateTextTask)
    }

    fun stopTextUpdate() {
        mainHandler.removeCallbacks(updateTextTask)
    }

}

var lastAction: String = "none"

private fun undo() {
    if (lastAction == "houseOpen") undoHouseOpen()
    else if (lastAction == "clearance") undoClearance()
}

private fun houseOpen() {
    if (!isHouseOpen) {
        houseOpenButtonColor.value = Color(144, 194, 234, 255)
        houseOpenButtonTextColor.value = Color(28, 27, 31, 255)
        houseOpenText.value = "House Open\n${showtimer.record("houseOpen")}"
        isHouseOpen = true
        lastAction = "houseOpen"
    }
}

private fun undoHouseOpen() {
    if (isHouseOpen) {
        houseOpenButtonColor.value = Color(144, 194, 234, 0)
        houseOpenButtonTextColor.value = Color(255, 255, 255, 255)
        houseOpenText.value = "House Open"
        showtimer.deleteTime("houseOpen")
        isHouseOpen = false
    }
}

private fun clearance() {
    if (!haveClearance) {
        clearanceButtonColor.value = Color(230, 201, 126, 255)
        clearanceButtonTextColor.value = Color(28, 27, 31, 255)
        haveClearance = true
        clearanceText.value = "Clearance\n${showtimer.record("clearance")}"
        lastAction = "clearance"
    }
}

private fun undoClearance() {
    clearanceButtonColor.value = Color(144, 194, 234, 0)
    clearanceButtonTextColor.value = Color(255, 255, 255, 255)
    clearanceText.value = "Clearance"
    showtimer.deleteTime("clearance")
    haveClearance = false
}

private fun startAct1() {
}

// TODO: Act1 Undo

// --- Common Components ---

@Composable
private fun SettingsBar(screenHeight: Int) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Spacer(Modifier.height((screenHeight * 0.1 * 0.08).dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(0.95f)
        ) {
            IconButton(onClick = { undo() }, modifier = Modifier.height(35.dp)) {
                Icon(
                    Icons.Outlined.Undo,
                    contentDescription = "Undo",
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                )
            }
            IconButton(onClick = { }, modifier = Modifier.height(35.dp)) {
                Icon(
                    Icons.Outlined.Settings,
                    contentDescription = "Settings",
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                )
            }
        }
    }
}


// --- Views ---

@Composable
fun ShowTimerView1(
    navigateToAct1: () -> Unit,
    configuration: Configuration = LocalConfiguration.current,
    showConfiguration: MutableMap<String, Any>
) {
    val currentTime by currentTime

    val houseOpenButtonColor by houseOpenButtonColor
    val houseOpenButtonTextColor by houseOpenButtonTextColor
    val houseOpenText by houseOpenText

    val clearanceButtonColor by clearanceButtonColor
    val clearanceButtonTextColor by clearanceButtonTextColor
    val clearanceText by clearanceText

    val screenHeight = configuration.screenHeightDp
    val screenWidth = configuration.screenWidthDp
    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        SettingsBar(screenHeight)
        Column(
            modifier = Modifier
                .height((screenHeight * 0.5).dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height((screenHeight * 0.5 * 0.15).dp))
            Text(
                text = showConfiguration["name"].toString(),
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
            modifier = Modifier
                .height((screenHeight * 0.5).dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.fillMaxHeight(0.5f * 0.05f))

            // House Open
            Button(
                onClick = { houseOpen() },
                colors = ButtonDefaults.buttonColors(houseOpenButtonColor),
                border = BorderStroke(4.dp, Color(144, 194, 234, 255)),
                shape = CardDefaults.outlinedShape,
                modifier = Modifier
                    .height((screenHeight * 0.5 * 0.2).dp)
                    .fillMaxWidth(0.9f)
            ) {
                Text(
                    text = houseOpenText,
                    color = houseOpenButtonTextColor,
                    fontFamily = incosolataFamily,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center,
                    lineHeight = (1.2f).em,
                    fontSize = 25.sp,
                )
            }

            Spacer(Modifier.height((screenHeight * 0.5 * 0.05).dp))

            // Clearance
            Button(
                onClick = fun() {
                    clearance()
                },
                colors = ButtonDefaults.buttonColors(clearanceButtonColor),
                border = BorderStroke(4.dp, Color(230, 201, 126, 255)),
                shape = CardDefaults.outlinedShape,
                modifier = Modifier
                    .height((screenHeight * 0.5 * 0.2).dp)
                    .fillMaxWidth(0.9f)
            ) {
                Text(
                    text = clearanceText,
                    color = clearanceButtonTextColor,
                    fontFamily = incosolataFamily,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center,
                    lineHeight = (1.2f).em,
                    fontSize = 25.sp,
                )
            }
            Spacer(Modifier.height((screenHeight * 0.5 * 0.05).dp))
            Button(
                onClick = fun() {
                    showtimer.record("act1Start")
                    navigateToAct1()
                },
                colors = ButtonDefaults.buttonColors(Color(16, 201, 122, 0)),
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


@Composable
fun ShowTimerView2(
    //navigateToAct1: () -> Unit,
    configuration: Configuration = LocalConfiguration.current,
    showConfiguration: MutableMap<String, Any>
) {
    var endText = "????"

    if (showConfiguration["isInterval"] == true) endText = "End Act 1\n& Start Interval"
    else if (showConfiguration["isInterval"] == false) endText = "End Show"


    val currentTime by currentTime
    val act1TimeElapsed by act1TimeElapsed

    val screenHeight = configuration.screenHeightDp
    val screenWidth = configuration.screenWidthDp

    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        SettingsBar(screenHeight)

        Column(
            modifier = Modifier
                .height((screenHeight * 0.75).dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
            Spacer(Modifier.height((screenHeight * 0.75 * 0.13).dp))
            Text(
                text = "Time Elapsed:",
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                fontWeight = FontWeight.Light
            )
            Text(
                text = act1TimeElapsed,
                textAlign = TextAlign.Center,
                fontSize = 60.sp,
                fontFamily = incosolataFamily,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height((screenHeight * 0.3).dp))
        }
        Column(
            modifier = Modifier
                .height((screenHeight * 0.25).dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height((screenHeight * 0.5 * 0.05).dp))
            Button(
                onClick = fun() {
                    //navigateToAct1()
                },
                colors = ButtonDefaults.buttonColors(Color(230, 100, 96, 0)),
                shape = CardDefaults.outlinedShape,
                border = BorderStroke(4.dp, Color(230, 100, 96, 255)),
                modifier = Modifier
                    .height((screenHeight * 0.5 * 0.4).dp)
                    .fillMaxWidth(0.9f)
            ) {
                Text(
                    text = endText,
                    color = Color(255, 255, 255, 255),
                    fontFamily = incosolataFamily,
                    fontWeight = FontWeight.Light,
                    lineHeight = (1.2f).em,
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp
                )
            }
            Spacer(Modifier.height((screenHeight * 0.5 * 0.1).dp))
        }
    }
}