package com.tsrewrite.client.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * The original TeamSpeak dimensions, screens and controls are untouched.
 * Only palette values change when a user opts into one of the extra themes.
 */
object Ts {
    var Blue by mutableStateOf(Color(0xFF1A91D0)); private set
    var BlueDim by mutableStateOf(Color(0xFF14709F)); private set
    var Bg by mutableStateOf(Color(0xFF111111)); private set
    var Surface by mutableStateOf(Color(0xFF181A1C)); private set
    var SurfaceHigh by mutableStateOf(Color(0xFF212325)); private set
    var SurfaceHighest by mutableStateOf(Color(0xFF2A2D30)); private set
    var Outline by mutableStateOf(Color(0xFF34383C)); private set
    val Text = Color(0xFFE6E8EA)
    val TextDim = Color(0xFF9BA1A6)
    val TextFaint = Color(0xFF6B7176)
    val Red = Color(0xFFF44336)
    var Green by mutableStateOf(Color(0xFF4CAF50)); private set
    var Yellow by mutableStateOf(Color(0xFFFFC107)); private set
    var Orange by mutableStateOf(Color(0xFFFF9800)); private set
    var Purple by mutableStateOf(Color(0xFFAB47BC)); private set
    var Selection by mutableStateOf(Color(0x331A91D0)); private set

    var ThemeName by mutableStateOf("standard"); private set
    private var current = "standard"
    fun useTheme(name: String) {
        if (current == name) return
        current = name
        ThemeName = name
        when (name) {
            "christmas" -> {
                Blue = Color(0xFFD83746); BlueDim = Color(0xFFAF2937)
                Bg = Color(0xFF091B17); Surface = Color(0xFF112721)
                SurfaceHigh = Color(0xFF19372B); SurfaceHighest = Color(0xFF264435)
                Outline = Color(0xFF42634D); Green = Color(0xFF36BA61)
                Yellow = Color(0xFFFFCC53); Orange = Color(0xFFDBA44C)
                Purple = Color(0xFFD799DA); Selection = Color(0x33D83746)
            }
            "27mc" -> {
                Blue = Color(0xFFFFAE39); BlueDim = Color(0xFFC67A1B)
                Bg = Color(0xFF0F1415); Surface = Color(0xFF232522)
                SurfaceHigh = Color(0xFF34352E); SurfaceHighest = Color(0xFF414237)
                Outline = Color(0xFF67624C); Green = Color(0xFF95B64B)
                Yellow = Color(0xFFFFD365); Orange = Color(0xFFFF981B)
                Purple = Color(0xFFCDA37A); Selection = Color(0x33FFAE39)
            }
            "press2talk" -> {
                Blue = Color(0xFF39B7FF); BlueDim = Color(0xFF1765A9)
                Bg = Color(0xFF071529); Surface = Color(0xFF13243C)
                SurfaceHigh = Color(0xFF1B2D47); SurfaceHighest = Color(0xFF254467)
                Outline = Color(0xFF3E688F); Green = Color(0xFF00E8A5)
                Yellow = Color(0xFFFFCF66); Orange = Color(0xFFF8A55D)
                Purple = Color(0xFFAB87FB); Selection = Color(0x3339B7FF)
            }
            "responder" -> {
                Blue = Color(0xFFFF334A); BlueDim = Color(0xFFAF1830)
                Bg = Color(0xFF090B10); Surface = Color(0xFF20131A)
                SurfaceHigh = Color(0xFF341925); SurfaceHighest = Color(0xFF442533)
                Outline = Color(0xFF913342); Green = Color(0xFF45E87C)
                Yellow = Color(0xFFFFCE69); Orange = Color(0xFFFF7965)
                Purple = Color(0xFFCF74BC); Selection = Color(0x33FF334A)
            }
            "cyber" -> {
                Blue = Color(0xFFC84BFF); BlueDim = Color(0xFF7B3AC7)
                Bg = Color(0xFF08091E); Surface = Color(0xFF18142E)
                SurfaceHigh = Color(0xFF2D1F50); SurfaceHighest = Color(0xFF263B62)
                Outline = Color(0xFF7751BB); Green = Color(0xFF29F5B0)
                Yellow = Color(0xFFFFD76B); Orange = Color(0xFFFF8EBF)
                Purple = Color(0xFF1BE3FF); Selection = Color(0x33C84BFF)
            }
            "christmasradio" -> {
                Blue = Color(0xFFFFCB5F); BlueDim = Color(0xFFB97028)
                Bg = Color(0xFF190915); Surface = Color(0xFF451529)
                SurfaceHigh = Color(0xFF611A34); SurfaceHighest = Color(0xFF792A3F)
                Outline = Color(0xFFC96D58); Green = Color(0xFF53E992)
                Yellow = Color(0xFFFFE496); Orange = Color(0xFFFF8D68)
                Purple = Color(0xFFFF8CBD); Selection = Color(0x33FFCB5F)
            }
            "space" -> {
                Blue = Color(0xFF36CEFA); BlueDim = Color(0xFF1585BA)
                Bg = Color(0xFF080D25); Surface = Color(0xFF141B39)
                SurfaceHigh = Color(0xFF202B4E); SurfaceHighest = Color(0xFF2C3761)
                Outline = Color(0xFF45538A); Green = Color(0xFF4FDAD7)
                Yellow = Color(0xFFFFC86B); Orange = Color(0xFFFF9870)
                Purple = Color(0xFFC17BFA); Selection = Color(0x3336CEFA)
            }
            else -> {
                Blue = Color(0xFF1A91D0); BlueDim = Color(0xFF14709F)
                Bg = Color(0xFF111111); Surface = Color(0xFF181A1C)
                SurfaceHigh = Color(0xFF212325); SurfaceHighest = Color(0xFF2A2D30)
                Outline = Color(0xFF34383C); Green = Color(0xFF4CAF50)
                Yellow = Color(0xFFFFC107); Orange = Color(0xFFFF9800)
                Purple = Color(0xFFAB47BC); Selection = Color(0x331A91D0)
            }
        }
    }
}

private val type = Typography().run {
    copy(
        titleLarge = titleLarge.copy(fontWeight = FontWeight.SemiBold, fontSize = 20.sp),
        titleMedium = titleMedium.copy(fontWeight = FontWeight.SemiBold),
        labelSmall = labelSmall.copy(letterSpacing = 0.4.sp),
    )
}

@Composable
fun TsTheme(theme: String = "standard", content: @Composable () -> Unit) {
    LaunchedEffect(theme) { Ts.useTheme(theme) }
    val scheme = darkColorScheme(
        primary = Ts.Blue,
        onPrimary = Color.White,
        primaryContainer = Ts.BlueDim,
        onPrimaryContainer = Color.White,
        secondary = Ts.Blue,
        onSecondary = Color.White,
        secondaryContainer = Ts.SurfaceHighest,
        onSecondaryContainer = Ts.Text,
        tertiary = Ts.Green,
        background = Ts.Bg,
        onBackground = Ts.Text,
        surface = Ts.Bg,
        onSurface = Ts.Text,
        surfaceVariant = Ts.SurfaceHigh,
        onSurfaceVariant = Ts.TextDim,
        surfaceContainerLowest = Ts.Bg,
        surfaceContainerLow = Ts.Surface,
        surfaceContainer = Ts.Surface,
        surfaceContainerHigh = Ts.SurfaceHigh,
        surfaceContainerHighest = Ts.SurfaceHighest,
        outline = Ts.Outline,
        outlineVariant = Ts.Outline,
        error = Ts.Red,
    )
    MaterialTheme(
        colorScheme = scheme,
        typography = type,
        shapes = Shapes(
            extraSmall = RoundedCornerShape(6.dp), small = RoundedCornerShape(8.dp),
            medium = RoundedCornerShape(12.dp), large = RoundedCornerShape(16.dp), extraLarge = RoundedCornerShape(24.dp),
        ),
        content = content,
    )
}

val TreeText = TextStyle(fontSize = 14.sp, lineHeight = 18.sp, color = Ts.Text)
