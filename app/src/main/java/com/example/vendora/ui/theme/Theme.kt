package com.example.vendora.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

//private val DarkColorScheme = darkColorScheme(
//    primary = Purple80,
//    secondary = PurpleGrey80,
//    tertiary = Pink80
//)

val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFfafafa),       // Primary brand color (lighter for dark mode)
    onPrimary = Color.Black,
    secondary = Color(0xFFA5D6A7),
    onSecondary = Color(0xFF35383f),
    background = Color(0xFF121212),    // Dark background
    onBackground = Color(0xFFE0E0E0),
    surface = Color(0xFF1e2129),
    onSurface = Color(0xFFE0E0E0),
    error = Color(0xFFEF9A9A),
    onError = Color.Black,
    surfaceContainer = Color(0xFF35383f),
    surfaceTint = Color(0xff1f222b)
)

val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1E88E5),       // Primary brand color
    onPrimary = Color.White,           // Text/icon color on primary
    secondary = Color(0xFF43A047),     // Secondary brand color
    onSecondary = Color.White,
    background = Color(0xFFF5F5F5),    // Light background
    onBackground = Color(0xFF212121),  // Text/icon color on background
    surface = Color(0xffe8e8e8),             // Surface color
    onSurface = Color(0xFF222528),     // Text/icon color on surface
    error = Color(0xFFD32F2F),         // Error color
    onError = Color.White,
    surfaceContainer = Color(0xFFf2f2f2),
    surfaceTint = Color.White
)

//private val LightColorScheme = lightColorScheme(
//    primary = Purple40,
//    secondary = PurpleGrey40,
//    tertiary = Pink40
//
//    /* Other default colors to override
//    background = Color(0xFFFFFBFE),
//    surface = Color(0xFFFFFBFE),
//    onPrimary = Color.White,
//    onSecondary = Color.White,
//    onTertiary = Color.White,
//    onBackground = Color(0xFF1C1B1F),
//    onSurface = Color(0xFF1C1B1F),
//    */
//)

@Composable
fun VendoraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}