package com.example.projekakhirpam

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.projekakhirpam.model.Monitoring
import com.example.projekakhirpam.ui.theme.ProjekAkhirPAMTheme
import com.example.projekakhirpam.ui.view.hewan.HewanHomeView
import com.example.projekakhirpam.ui.view.monitoring.MonitoringHomeView
import com.example.projekakhirpam.ui.view.monitoring.MonitoringInsertView
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }
            ProjekAkhirPAMTheme (
                darkTheme = isDarkTheme
            ){
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    KebunBinatangApp(
                        isDarkTheme = isDarkTheme,
                        onThemeChange = { isDarkTheme = it },
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val date = remember {
        LocalDateTime.of(2024,1,30,12,0,30)
    }
    Text(
        text = "Hello $date!",
        modifier = modifier
    )
    Log.d("kaj", date.toString())
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProjekAkhirPAMTheme {
        Greeting("Android")
    }
}