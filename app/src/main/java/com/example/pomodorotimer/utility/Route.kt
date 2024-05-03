package com.example.pomodorotimer.utility

sealed class Route(val route: String) {
    data object PomodoroScreen : Route("pomodoro")
    data object CustomizeScreen : Route("customize")
}