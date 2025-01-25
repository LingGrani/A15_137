package com.example.projekakhirpam.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = primaryDarkContrast,
    onPrimary = onPrimaryDarkContrast,
    primaryContainer = primaryContainerDarkContrast,
    onPrimaryContainer = onPrimaryContainerDarkContrast,
    secondary = secondaryDarkContrast,
    onSecondary = onSecondaryDarkContrast,
    secondaryContainer = secondaryContainerDarkContrast,
    onSecondaryContainer = onSecondaryContainerDarkContrast,
    tertiary = tertiaryDarkContrast,
    onTertiary = onTertiaryDarkContrast,
    tertiaryContainer = tertiaryContainerDarkContrast,
    onTertiaryContainer = onTertiaryContainerDarkContrast,
    error = errorDarkContrast,
    onError = onErrorDarkContrast,
    errorContainer = errorContainerDarkContrast,
    onErrorContainer = onErrorContainerDarkContrast,
    background = backgroundDarkContrast,
    onBackground = onBackgroundDarkContrast,
    surface = surfaceDarkContrast,
    onSurface = onSurfaceDarkContrast,
    surfaceVariant = surfaceVariantDarkContrast,
    onSurfaceVariant = onSurfaceVariantDarkContrast,
    outline = outlineDarkContrast,
    outlineVariant = outlineVariantDarkContrast,
    scrim = scrimDarkContrast,
    inverseSurface = inverseSurfaceDarkContrast,
    inverseOnSurface = inverseOnSurfaceDarkContrast,
    inversePrimary = inversePrimaryDarkContrast,

)

private val LightColorScheme = lightColorScheme(
    primary = primaryLightContrast,
    onPrimary = onPrimaryLightContrast,
    primaryContainer = primaryContainerLightContrast,
    onPrimaryContainer = onPrimaryContainerLightContrast,
    secondary = secondaryLightContrast,
    onSecondary = onSecondaryLightContrast,
    secondaryContainer = secondaryContainerLightContrast,
    onSecondaryContainer = onSecondaryContainerLightContrast,
    tertiary = tertiaryLightContrast,
    onTertiary = onTertiaryLightContrast,
    tertiaryContainer = tertiaryContainerLightContrast,
    onTertiaryContainer = onTertiaryContainerLightContrast,
    error = errorLightContrast,
    onError = onErrorLightContrast,
    errorContainer = errorContainerLightContrast,
    onErrorContainer = onErrorContainerLightContrast,
    background = backgroundLightContrast,
    onBackground = onBackgroundLightContrast,
    surface = surfaceLightContrast,
    onSurface = onSurfaceLightContrast,
    surfaceVariant = surfaceVariantLightContrast,
    onSurfaceVariant = onSurfaceVariantLightContrast,
    outline = outlineLightContrast,
    outlineVariant = outlineVariantLightContrast,
    scrim = scrimLightContrast,
    inverseSurface = inverseSurfaceLightContrast,
    inverseOnSurface = inverseOnSurfaceLightContrast,
    inversePrimary = inversePrimaryLightContrast,

)

@Composable
fun ProjekAkhirPAMTheme(
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