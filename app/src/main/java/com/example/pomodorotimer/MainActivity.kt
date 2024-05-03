package com.example.pomodorotimer

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pomodorotimer.components.CustomizeContainer
import com.example.pomodorotimer.ui.theme.DarkBlue
import com.example.pomodorotimer.ui.theme.GrayWhite
import com.example.pomodorotimer.ui.theme.Kumbhsans
import com.example.pomodorotimer.ui.theme.PomodoroTimerTheme
import com.example.pomodorotimer.ui.theme.Roboto
import com.example.pomodorotimer.ui.theme.RobotoBold
import com.example.pomodorotimer.ui.theme.VeryDarkBlue
import com.example.pomodorotimer.utility.Route
import com.example.pomodorotimer.utility.formatSecondsToMinutesAndSeconds
import com.example.pomodorotimer.viewmodel.PomodoroViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PomodoroTimerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = VeryDarkBlue
                ) {
                    val navController = rememberNavController()
                    val pomodoroViewModel: PomodoroViewModel = viewModel()
                    NavHost(navController = navController, startDestination = Route.PomodoroScreen.route){
                        composable(Route.PomodoroScreen.route) {
                            App(navController,pomodoroViewModel)
                        }
                        composable(Route.CustomizeScreen.route) {
                            CustomizeContainer(navController,pomodoroViewModel)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Prev() {
    val pomodoroViewModel: PomodoroViewModel = viewModel()
    val navController = rememberNavController()
    App(navController, pomodoroViewModel)
}

@Composable
private fun App(navController: NavController, pomodoroViewModel: PomodoroViewModel) {

    val pomodoroTimer = pomodoroViewModel.pomodoroTimer.collectAsState()
    val shortBreakTimer = pomodoroViewModel.shortBreakTimer.collectAsState()
    val longBreakTimer = pomodoroViewModel.longBreakTimer.collectAsState()
    val navigationState = pomodoroViewModel.navigationTab.collectAsState()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {

        PomodoroTitle()
        Navigation(
            navigationState = navigationState,
            onNavigationChange = { tab -> pomodoroViewModel.setNavigationTab(tab)},
            color = pomodoroViewModel.theme.collectAsState(),
            font = pomodoroViewModel.font.collectAsState(),
            isTimerRunning = pomodoroViewModel.isTimerRunning.collectAsState(),
            onCurrentTimerChange = { tab ->
                when(tab){
                    "pomodoro" -> pomodoroViewModel.setCurrentTime(pomodoroTimer.value * 60)
                    "short break" -> pomodoroViewModel.setCurrentTime(shortBreakTimer.value*60)
                    else -> pomodoroViewModel.setCurrentTime(longBreakTimer.value * 60)
                }
            }
        )
        PomodoroTimer(
           isTimerRunning = pomodoroViewModel.isTimerRunning.collectAsState(),
            onTimerChange = { isRunning ->
                pomodoroViewModel.setIsTimerRunning(isRunning)
            },
            totalTime = when(navigationState.value){
                  "pomodoro" -> pomodoroTimer
                   "short break" -> shortBreakTimer
                     else -> longBreakTimer
            },
            color = pomodoroViewModel.theme.collectAsState(),
            currentTime = pomodoroViewModel.currentTime.collectAsState()
        )
        CustomizeButton(navController, isTimerRunning = pomodoroViewModel.isTimerRunning.collectAsState())
    }
}

@Composable
fun PomodoroTitle() {
    Text (
        modifier = Modifier
            .padding(0.dp,20.dp,0.dp,0.dp),
        style = TextStyle(
            fontFamily = Kumbhsans,
            fontSize = 28.sp
        ),
        color = GrayWhite,
        text = "pomodoro"
    )
}

@Composable
fun CustomizeButton(navController: NavController, isTimerRunning: State<Boolean>) {
    val context = LocalContext.current
    Image(
        modifier = Modifier
            .size(32.dp)
            .clickable {
                if (!isTimerRunning.value) navController.navigate(Route.CustomizeScreen.route)
                else Toast
                    .makeText(context, "Timer is still running", Toast.LENGTH_SHORT)
                    .show()
            },
        painter = painterResource(id = R.drawable.setting),
        contentDescription = "setting"
    )
}
@Composable
fun NavigationTab(
    modifier: Modifier = Modifier,
    tab: String,
    navigationState: State<String>,
    color: State<Color>,
    font: State<FontFamily>,
    isTimerRunning: State<Boolean>,
    onNavigationChange: (String) -> Unit,
    onCurrentTimerChange: (String) -> Unit
) {
    val context = LocalContext.current
    val isActive = navigationState.value == tab
    val backgroundColor by animateColorAsState(
        targetValue = if (isActive) color.value else Color.Transparent,
        label = "color animation",
        animationSpec = tween(durationMillis = 1000)
    )
    val textColor by animateColorAsState(
        targetValue = if (isActive) VeryDarkBlue else GrayWhite,
        label = "color animation",
        animationSpec = tween(durationMillis = 1000)
    )

    Text(
        modifier = modifier
            .padding(10.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(25.dp)
            )
            .clickable {
                if (!isTimerRunning.value) {
                    onNavigationChange(tab)
                    onCurrentTimerChange(tab)
                } else Toast
                    .makeText(context, "Timer is still running", Toast.LENGTH_SHORT)
                    .show()
            }
            .padding(10.dp),
        text = tab,
        color = textColor,
        style = TextStyle(
            fontFamily = font.value,
            fontSize = 14.sp
        )
    )
}

@Composable
fun Navigation(
    navigationState: State<String>,
    color: State<Color>,
    font: State<FontFamily>,
    isTimerRunning: State<Boolean>,
    onNavigationChange: (String) -> Unit,
    onCurrentTimerChange: (String) -> Unit
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 0.dp)
            .background(VeryDarkBlue, shape = RoundedCornerShape(30.dp)),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ){
        NavigationTab(tab = "pomodoro", navigationState = navigationState, onNavigationChange = onNavigationChange, color = color, font = font, isTimerRunning = isTimerRunning, onCurrentTimerChange = onCurrentTimerChange)
        NavigationTab(tab = "short break", navigationState = navigationState, onNavigationChange = onNavigationChange, color = color, font = font, isTimerRunning = isTimerRunning, onCurrentTimerChange = onCurrentTimerChange)
        NavigationTab(tab = "long break", navigationState = navigationState, onNavigationChange = onNavigationChange , color = color, font = font, isTimerRunning = isTimerRunning, onCurrentTimerChange = onCurrentTimerChange)
    }
}

@Composable
fun PomodoroTimer(
    isTimerRunning: State<Boolean>,
    color: State<Color>,
    currentTime: State<Int>,
    totalTime: State<Int>,
    onTimerChange: (Boolean) -> Unit,
) {
        Box(
            modifier = Modifier
                .padding(10.dp)
                .aspectRatio(1f)
                .background(
                    Brush.linearGradient(
                        0.0f to Color(0xFF020414),
                        0.5f to Color(0xFF020744),
                        1.0f to Color(0xFF414563),
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp)
                    .background(
                        VeryDarkBlue,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                ProgressIndicator(color = color, currentTime = currentTime, totalTime = totalTime)
                TimerContent(isTimerRunning = isTimerRunning, onTimerChange = onTimerChange, currentTime = currentTime)
            }
        }
}
@Composable
fun ProgressIndicator(
    color: State<Color>,
    currentTime: State<Int>,
    totalTime: State<Int>
) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val radius = size.minDimension / 2
        val strokeWidth = 24f
        val startAngle = -90f

        val progress = ((totalTime.value.toFloat()*60) - currentTime.value.toFloat()) / (totalTime.value.toFloat()*60)
        val sweepAngle = progress * 360

        drawArc(
            color = color.value,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            topLeft = Offset(centerX - radius, centerY - radius),
            size = Size(radius * 2, radius * 2)
        )
    }
}

@Composable
fun TimerContent(
    isTimerRunning: State<Boolean>,
    currentTime: State<Int>,
    onTimerChange: (Boolean) -> Unit,
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = formatSecondsToMinutesAndSeconds(currentTime.value),
            style = TextStyle(
                fontFamily = RobotoBold,
                fontSize = 70.sp
            ),
            color = GrayWhite
        )
        Text(
           modifier = Modifier
               .clickable {
                   onTimerChange(!isTimerRunning.value)
               },
            text = if(isTimerRunning.value) "S T O P" else "S T A R T",
            style = TextStyle(
                fontFamily = Roboto,
                fontSize = 25.sp
            ),
            color = GrayWhite
        )
    }
}