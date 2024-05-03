package com.example.pomodorotimer.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.pomodorotimer.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

val Roboto = FontFamily(
    Font(R.font.roboto_regular)
)
val RobotoBold = FontFamily(
    Font(R.font.roboto_bold)
)

val Ubuntu = FontFamily(
    Font(R.font.ubuntu_regular)
)

val Inter = FontFamily(
    Font(R.font.inter_regular)
)
val  Kumbhsans = FontFamily(
    Font(R.font.kumbhsans_regular)
)
val  KumbhsansBold = FontFamily(
    Font(R.font.kumbhsans_black)
)
