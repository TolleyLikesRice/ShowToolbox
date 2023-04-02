package dev.tolley.showtoolbox.android

import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Undo
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.elevatedCardColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import dev.tolley.showtoolbox.R
import dev.tolley.showtoolbox.Showtimer
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

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
    else if (lastAction == "act1Clearance") undoAct1Clearance()
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

private fun act1Clearance() {
    if (!haveClearance) {
        clearanceButtonColor.value = Color(230, 201, 126, 255)
        clearanceButtonTextColor.value = Color(28, 27, 31, 255)
        haveClearance = true
        clearanceText.value = "Clearance\n${showtimer.record("act1Clearance")}"
        lastAction = "act1Clearance"
    }
}

private fun undoAct1Clearance() {
    clearanceButtonColor.value = Color(144, 194, 234, 0)
    clearanceButtonTextColor.value = Color(255, 255, 255, 255)
    clearanceText.value = "Clearance"
    showtimer.deleteTime("act1Clearance")
    haveClearance = false
}

private fun startAct1() {
}

// TODO: Act1 Undo

// --- Common Components ---

@Composable
private fun SettingsBar(
    navigateToSettings: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Spacer(Modifier.height(6.256.dp))
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
            IconButton(onClick = {
                navigateToSettings()
            }, modifier = Modifier.height(35.dp)) {
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
fun ShowTimerStartView(
    navigateToAct1: () -> Unit,
    navigateToSettings: () -> Unit,
    showConfiguration: MutableMap<String, Any>
) {
    val currentTime by currentTime

    val houseOpenButtonColor by houseOpenButtonColor
    val houseOpenButtonTextColor by houseOpenButtonTextColor
    val houseOpenText by houseOpenText

    val clearanceButtonColor by clearanceButtonColor
    val clearanceButtonTextColor by clearanceButtonTextColor
    val clearanceText by clearanceText

    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        SettingsBar(navigateToSettings)
        Column(
            modifier = Modifier
                .height(391.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(58.65.dp))
            Text(
                text = showConfiguration["name"].toString(),
                textAlign = TextAlign.Center,
                fontSize = 35.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = (1.4f).em,
                modifier = Modifier.fillMaxWidth(0.9f)
            )
            Spacer(Modifier.height(58.65.dp))
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
                .height(391.dp)
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
                    .height(78.2.dp)
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

            Spacer(Modifier.height(19.55.dp))

            // Clearance
            Button(
                onClick = fun() {
                    act1Clearance()
                },
                colors = ButtonDefaults.buttonColors(clearanceButtonColor),
                border = BorderStroke(4.dp, Color(230, 201, 126, 255)),
                shape = CardDefaults.outlinedShape,
                modifier = Modifier
                    .height(78.2.dp)
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
            Spacer(Modifier.height(19.55.dp))
            Button(
                onClick = fun() {
                    showtimer.record("act1Start")
                    navigateToAct1()
                },
                colors = ButtonDefaults.buttonColors(Color(16, 201, 122, 0)),
                shape = CardDefaults.outlinedShape,
                border = BorderStroke(4.dp, Color(16, 201, 122, 255)),
                modifier = Modifier
                    .height(156.4.dp)
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
            Spacer(Modifier.height(39.1.dp))
        }
    }
}


@Composable
fun ShowTimerAct1View(
    navigateToSummary: () -> Unit,
    navigateToSettings: () -> Unit,
    showConfiguration: MutableMap<String, Any>
) {
    var endText = "????"

    if (showConfiguration["isInterval"] == true) endText = "End Act 1\n& Start Interval"
    else if (showConfiguration["isInterval"] == false) endText = "End Show"


    val currentTime by currentTime
    val act1TimeElapsed by act1TimeElapsed

    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        SettingsBar(navigateToSettings)

        Column(
            modifier = Modifier
                .height(586.5.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(58.65.dp))
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
            Spacer(Modifier.height(50.83.dp))
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
            Spacer(Modifier.height(234.6.dp))
        }
        Column(
            modifier = Modifier
                .height(195.5.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(19.55.dp))
            Button(
                onClick = fun() {
                    if (showConfiguration["acts"] == 1) {
                        showtimer.record("act1End")
                        navigateToSummary()
                    }
                    // TODO: Next act, no intervals - to interval, etc
                },
                colors = ButtonDefaults.buttonColors(Color(230, 100, 96, 0)),
                shape = CardDefaults.outlinedShape,
                border = BorderStroke(4.dp, Color(230, 100, 96, 255)),
                modifier = Modifier
                    .height(156.4.dp)
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
            Spacer(Modifier.height(39.1.dp))
        }
    }
}

@Composable
fun ShowTimerSummaryView(
    //navigateToSummary: () -> Unit,
    navigateToSettings: () -> Unit,
    showConfiguration: MutableMap<String, Any>
) {

    val screenWidth = LocalConfiguration.current.screenWidthDp

    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        SettingsBar(navigateToSettings)

        Column(
            modifier = Modifier
                .height(391.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(58.65.dp))
            Text(
                text = showConfiguration["name"].toString(),
                textAlign = TextAlign.Center,
                fontSize = 35.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = (1.4f).em,
                modifier = Modifier.fillMaxWidth(0.9f)
            )
            Spacer(Modifier.height(19.55.dp))
            LazyColumn {

                item {

                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    val context = LocalContext.current

                    Button(
                        onClick = {
                            context.startActivity(shareIntent)
                        },
                        colors = ButtonDefaults.buttonColors(Color(230, 100, 96, 0)),
                        shape = CardDefaults.outlinedShape,
                        border = BorderStroke(4.dp, Color(56, 179, 164, 255)),
                        modifier = Modifier
                            .height(60.dp)
                            .fillMaxWidth(0.9f)
                    ) {
                        Text(
                            text = "Share",
                            color = Color(255, 255, 255, 255),
                            fontFamily = incosolataFamily,
                            fontWeight = FontWeight.Light,
                            lineHeight = (1.2f).em,
                            textAlign = TextAlign.Center,
                            fontSize = 30.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                }

                item {
                    ElevatedCard(
                        modifier = Modifier
                            .height(52.dp)
                            .fillMaxWidth(0.9f),
                        colors = elevatedCardColors(Color(79, 86, 96, 255))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .width((screenWidth * 0.9 * 0.45).dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Summary",
                                textAlign = TextAlign.Center,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Medium,
                                lineHeight = (1.2f).em,
                                modifier = Modifier.fillMaxWidth(0.9f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                }

                item {
                    // Header elevatedcard with one text saying "Breakdown", no time requried
                    ElevatedCard(
                        modifier = Modifier
                            .height(52.dp)
                            .fillMaxWidth(0.9f),
                        colors = elevatedCardColors(Color(79, 86, 96, 255))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .width((screenWidth * 0.9 * 0.45).dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Breakdown",
                                textAlign = TextAlign.Center,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Medium,
                                lineHeight = (1.2f).em,
                                modifier = Modifier.fillMaxWidth(0.9f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                }

                item {
                    ElevatedCard(
                        modifier = Modifier
                            .height(70.dp)
                            .fillMaxWidth(0.9f),
                    ) {
                        Row(modifier = Modifier.fillMaxSize()) {
                            Spacer(modifier = Modifier.width((screenWidth * 0.9 * 0.05).dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width((screenWidth * 0.9 * 0.45).dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "House Open",
                                    textAlign = TextAlign.Left,
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Normal,
                                    lineHeight = (1.2f).em,
                                    modifier = Modifier.fillMaxWidth(0.9f)
                                )
                            }
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = showtimer.getTimeAsString("houseOpen"),
                                    textAlign = TextAlign.Right,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Light,
                                    fontFamily = incosolataFamily,
                                    modifier = Modifier.fillMaxWidth(0.9f)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                }

                // TODO: Autogenerate rest of acts and interval with this structure
                item {
                    ElevatedCard(
                        modifier = Modifier
                            .height(179.86.dp)
                            .fillMaxWidth(0.9f),
                    ) {
                        Row(modifier = Modifier.fillMaxSize()) {
                            Spacer(modifier = Modifier.width((screenWidth * 0.9 * 0.05).dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width((screenWidth * 0.9 * 0.3).dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Act 1",
                                    textAlign = TextAlign.Left,
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Normal,
                                    lineHeight = (1.2f).em,
                                    modifier = Modifier.fillMaxWidth(0.9f)
                                )
                            }
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Clearance: " + showtimer.getTimeAsString("act1Clearance"),
                                    textAlign = TextAlign.Right,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Light,
                                    fontFamily = incosolataFamily,
                                    modifier = Modifier.fillMaxWidth(0.9f)
                                )
                                Text(
                                    text = "Up: " + showtimer.getTimeAsString("act1Start"),
                                    textAlign = TextAlign.Right,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Light,
                                    fontFamily = incosolataFamily,
                                    modifier = Modifier.fillMaxWidth(0.9f)
                                )
                                Text(
                                    text = "Down: " + showtimer.getTimeAsString("act1End"),
                                    textAlign = TextAlign.Right,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Light,
                                    fontFamily = incosolataFamily,
                                    modifier = Modifier.fillMaxWidth(0.9f)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Duration: " + showtimer.duration(
                                        "act1Start",
                                        "act1End"
                                    ),
                                    textAlign = TextAlign.Right,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = incosolataFamily,
                                    modifier = Modifier.fillMaxWidth(0.9f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShowTimerSettingsView(
    showConfiguration: MutableMap<String, Any>
) {
   
}