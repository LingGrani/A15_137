package com.example.projekakhirpam

import androidx.compose.runtime.Composable
import com.example.projekakhirpam.ui.navigation.PengelolaHalaman

@Composable
fun KebunBinatangApp(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
){
    PengelolaHalaman(
        isDarkTheme = isDarkTheme,
        onThemeChange = onThemeChange
    )
}