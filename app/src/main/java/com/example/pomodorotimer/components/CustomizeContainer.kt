package com.example.pomodorotimer.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pomodorotimer.R
import com.example.pomodorotimer.ui.theme.DarkPink
import com.example.pomodorotimer.ui.theme.GrayWhite
import com.example.pomodorotimer.ui.theme.Inter
import com.example.pomodorotimer.ui.theme.KumbhsansBold
import com.example.pomodorotimer.ui.theme.LightPurple
import com.example.pomodorotimer.ui.theme.Roboto
import com.example.pomodorotimer.ui.theme.RobotoBold
import com.example.pomodorotimer.ui.theme.SkyBlue
import com.example.pomodorotimer.ui.theme.Ubuntu
import com.example.pomodorotimer.utility.Route
import com.example.pomodorotimer.viewmodel.PomodoroViewModel

@Preview(showSystemUi = true)
@Composable
private fun Prev() {
    val pomodoroViewModel: PomodoroViewModel = viewModel()
    val navController = rememberNavController()
    CustomizeContainer(navController = navController, pomodoroViewModel)
}

@Composable
fun CustomizeContainer(navController: NavController, pomodoroViewModel: PomodoroViewModel) {
    val pomodoroTimer = pomodoroViewModel.pomodoroTimer.collectAsState()
    val shortBreakTimer = pomodoroViewModel.shortBreakTimer.collectAsState()
    val longBreakTimer = pomodoroViewModel.longBreakTimer.collectAsState()
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp)
            .background(Color.White, shape = RoundedCornerShape(15.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        SettingHeader(navController)
        Box(modifier = Modifier
            .padding(0.dp, 10.dp, 0.dp, 0.dp)
            .fillMaxWidth()
            .height(2.dp)
            .background(Color.LightGray))
        CustomizeTime(
            pomodoroTimer = pomodoroTimer,
            shortBreakTimer = shortBreakTimer,
            longBreakTimer = longBreakTimer,
            onTimeChange = { type, time ->
                    when(type) {
                        "pomodoro" -> pomodoroViewModel.setPomodoroTimer(time)
                        "short break" -> pomodoroViewModel.setShortBreakTimer(time)
                        else -> pomodoroViewModel.setLongBreakTimer(time)
                    }
            }
        )
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .padding(40.dp, 0.dp)
            .background(Color.LightGray))
        CustomizeFont(
          font =  pomodoroViewModel.font.collectAsState(),
          onFontChange = {  fontFamily ->
              pomodoroViewModel.setFont(fontFamily)
          }
        )
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .padding(40.dp, 0.dp)
            .background(Color.LightGray))
        CustomizeColor(
            color =  pomodoroViewModel.theme.collectAsState(),
            onColorChange = {  theme ->
                pomodoroViewModel.setTheme(theme)
            }
        )
        ApplyButton(navController, pomodoroViewModel.theme.collectAsState(),
            onCurrentTimerChanger = { 
                when(pomodoroViewModel.navigationTab.value){
                    "pomodoro" -> pomodoroViewModel.setCurrentTime(pomodoroTimer.value * 60)
                    "short break" -> pomodoroViewModel.setCurrentTime(shortBreakTimer.value * 60)
                    else -> pomodoroViewModel.setCurrentTime(longBreakTimer.value * 60)
                }
        })
    }
}

@Composable
fun SettingHeader(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Settings",
            style = TextStyle(
                fontFamily = KumbhsansBold,
                fontSize = 20.sp
            ),
        )
        Image(
            modifier = Modifier
                .clickable {
                    navController.navigate(Route.PomodoroScreen.route)
                }
                .size(16.dp),
            painter = painterResource(id = R.drawable.cancel),
            contentDescription = "cancel"
        )
    }
}

@Composable
fun CustomizeTime(
    pomodoroTimer : State<Int>,
    shortBreakTimer : State<Int>,
    longBreakTimer : State<Int>,
    onTimeChange: (String, Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(0.dp,0.dp,0.dp, 10.dp),
            text = "T I M E  ( M I N U T E S )",
            style = TextStyle(
                fontFamily = Roboto,
                fontSize = 16.sp
            ),
        )
        TimeBlock(type = "pomodoro", time = pomodoroTimer, onTimeChange = onTimeChange )
        TimeBlock(type = "short break", time = shortBreakTimer, onTimeChange = onTimeChange )
        TimeBlock(type = "long break", time = longBreakTimer, onTimeChange = onTimeChange )
    }
}

@Composable
fun TimeBlock(
    type : String,
    time : State<Int>,
    onTimeChange : (String, Int) -> Unit
) {
    val  context= LocalContext.current
    Row (
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            modifier = Modifier
                .weight(1f),
            text = type,
            style = TextStyle(
                fontFamily = RobotoBold,
                fontSize = 16.sp
            ),
            color = Color.Gray
        )

        Row (
            modifier = Modifier
                .weight(1f)
                .background(Color(0xFFEFF1FA), shape = RoundedCornerShape(20.dp)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){

            BasicTextField(
                value = time.value.toString(),
                onValueChange = {
                    if (it.isNotEmpty() && (it.toInt() in (5..60))) {
                        when(type) {
                            "pomodoro" -> onTimeChange(type, it.toInt())
                            "short break" -> onTimeChange(type, it.toInt())
                            "long break" -> onTimeChange(type, it.toInt())
                        }
                    } else {
                        Toast.makeText(context, "Time must be between 5 and 60 minutes.", Toast.LENGTH_SHORT).show()
                    }
                },
                textStyle = TextStyle(
                    fontFamily = RobotoBold,
                    fontSize = 16.sp,
                    color = Color.Black,
                ),
                modifier = Modifier
                    .weight(2f)
                    .padding(20.dp, 0.dp),
            )
            Column (
                modifier = Modifier
                    .weight(1f)
                    .padding(0.dp, 10.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Image(
                    modifier = Modifier
                        .size(16.dp)
                        .clickable {
                            if (time.value + 1 in (5..60)) {
                                when (type) {
                                    "pomodoro" -> onTimeChange(type, time.value + 1)
                                    "short break" -> onTimeChange(type, time.value + 1)
                                    "long break" -> onTimeChange(type, time.value + 1)
                                }
                            } else {
                                Toast
                                    .makeText(
                                        context,
                                        "Time must be between 5 and 60 minutes.",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            }
                        },
                    painter = painterResource(id = R.drawable.up),
                    contentDescription = "increase"
                )
                Image(
                    modifier = Modifier
                        .size(16.dp)
                        .clickable {
                            if (time.value - 1 in (5..60)) {
                                when (type) {
                                    "pomodoro" -> onTimeChange(type, time.value - 1)
                                    "short break" -> onTimeChange(type, time.value - 1)
                                    "long break" -> onTimeChange(type, time.value - 1)
                                }
                            } else {
                                Toast
                                    .makeText(
                                        context,
                                        "Time must be between 5 and 60 minutes.",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            }
                        },
                    painter = painterResource(id = R.drawable.down),
                    contentDescription = "decrease"
                )
            }
        }
    }
}

@Composable
fun CustomizeFont(font: State<FontFamily>,onFontChange : (FontFamily) -> Unit) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(0.dp,0.dp,0.dp, 10.dp),
            text = "F O N T",
            style = TextStyle(
                fontFamily = RobotoBold,
                fontSize = 14.sp
            ),
        )
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            FontButton(type  = Roboto, font = font, onFontChange = onFontChange)
            FontButton(type = Ubuntu, font = font, onFontChange = onFontChange)
            FontButton(type = Inter, font = font, onFontChange = onFontChange)
        }
    }
}

@Composable
fun FontButton(
    type : FontFamily,
    font : State<FontFamily>,
    onFontChange : (FontFamily) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(10.dp)
            .size(50.dp)
            .background(
                if (font.value == type) Color.Black else GrayWhite,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ){
        Text(
            modifier = Modifier
                .clickable {
                     onFontChange(type)
                },
            text = "Aa",
            style = TextStyle(
                fontFamily = type,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            color = if (font.value == type) GrayWhite else Color.Black
        )
    }
}

@Composable
fun CustomizeColor(color: State<Color>,onColorChange : (Color) -> Unit) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(0.dp, 0.dp, 0.dp, 10.dp),
            text = "C O L O R",
            style = TextStyle(
                fontFamily = RobotoBold,
                fontSize = 14.sp
            ),
        )
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            ColorButton(theme = DarkPink,color = color, onColorChange = onColorChange)
            ColorButton(theme = SkyBlue ,color = color, onColorChange = onColorChange)
            ColorButton(theme = LightPurple,color = color, onColorChange = onColorChange)
        }
    }
}
@Composable
fun ColorButton(
    theme : Color,
    color : State<Color>,
    onColorChange : (Color) -> Unit
) {
    val isActive = theme == color.value
    Box(
        modifier = Modifier
            .padding(10.dp)
            .size(50.dp)
            .background(
                theme,
                shape = CircleShape
            )
            .clickable {
                onColorChange(theme)
            },
        contentAlignment = Alignment.Center
    ){
       if(isActive) Image(
           modifier =  Modifier
               .size(24.dp),
           painter = painterResource(id = R.drawable.tick),
           contentDescription = "active"
       )
    }
}

@Composable
fun ApplyButton(navController: NavController, color: State<Color>, onCurrentTimerChanger : () -> Unit) {
    Box(
        modifier = Modifier
            .height(50.dp)
            .width(150.dp)
            .offset(y = (25).dp)
            .zIndex(1f)
            .background(color.value, shape = RoundedCornerShape(30.dp))
            .clickable {
                onCurrentTimerChanger()
                navController.navigate(Route.PomodoroScreen.route)
            },
        contentAlignment = Alignment.Center
    ){
        Text(
            modifier = Modifier,
            text = "Apply",
            style = TextStyle(
                fontFamily = RobotoBold,
                fontSize = 16.sp
            ),
            color = Color.White
        )
    }
}