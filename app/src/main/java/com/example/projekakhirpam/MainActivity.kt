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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.projekakhirpam.model.Monitoring
import com.example.projekakhirpam.ui.theme.ProjekAkhirPAMTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjekAkhirPAMTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
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