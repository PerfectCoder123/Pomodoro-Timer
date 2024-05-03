package com.example.pomodorotimer.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomodorotimer.ui.theme.DarkPink
import com.example.pomodorotimer.ui.theme.Roboto
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PomodoroViewModel : ViewModel() {
    private val _navigationTab = MutableStateFlow("pomodoro")
    private val _pomodoroTimer = MutableStateFlow(25)
    private val _shortBreakTimer = MutableStateFlow(5)
    private val _longBreakTimer = MutableStateFlow(30)
    private val _currentTime = MutableStateFlow(_pomodoroTimer.value*60)
    private val _font = MutableStateFlow(Roboto)
    private val _theme = MutableStateFlow(DarkPink)
    private val _isTimerRunning = MutableStateFlow(false)

    val navigationTab: StateFlow<String> = _navigationTab
    val pomodoroTimer: StateFlow<Int> = _pomodoroTimer
    val shortBreakTimer: StateFlow<Int> = _shortBreakTimer
    val longBreakTimer: StateFlow<Int> = _longBreakTimer
    val currentTime: StateFlow<Int> = _currentTime
    val font: StateFlow<FontFamily> = _font
    val theme: StateFlow<Color> = _theme
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning

    fun setNavigationTab(tab: String) {
        viewModelScope.launch {
            _navigationTab.emit(tab)
        }
    }

    fun setPomodoroTimer(time: Int) {
        viewModelScope.launch {
            _pomodoroTimer.emit(time)
        }
    }

    fun setShortBreakTimer(time: Int) {
        viewModelScope.launch {
            _shortBreakTimer.emit(time)
        }
    }

    fun setLongBreakTimer(time: Int) {
        viewModelScope.launch {
            _longBreakTimer.emit(time)
        }
    }

    fun setCurrentTime(time: Int) {
        _currentTime.value = time
    }

    fun setFont(font: FontFamily) {
        viewModelScope.launch {
            _font.emit(font)
        }
    }

    fun setTheme(theme: Color) {
        viewModelScope.launch {
            _theme.emit(theme)
        }
    }

    fun setIsTimerRunning(isTimerRunning : Boolean) {
        _isTimerRunning.value = isTimerRunning
        startTimer()
    }

     private fun startTimer() {
        viewModelScope.launch {
            if(_currentTime.value == 0) {
                when(navigationTab.value){
                    "pomodoro" -> setCurrentTime(pomodoroTimer.value * 60)
                    "short break" -> setCurrentTime(shortBreakTimer.value*60)
                    else -> setCurrentTime(longBreakTimer.value * 60)
                }
            }

            while (_currentTime.value > 0 && _isTimerRunning.value) {
                setCurrentTime(_currentTime.value - 1)
                delay(1000)
            }
            if(_currentTime.value == 0) {
                setIsTimerRunning(false)
            }
        }
    }
}
