package com.example.projekakhirpam.ui.view.monitoring

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MonitoringHomeView(
    modifier: Modifier
){
    Scaffold { innerPadding ->
        HomeCard()
    }

}

@Preview(showBackground = true)
@Composable
private fun HomeCard(

){
    Card(

    ) {
        Column {
            Text("ID Kandang")
            Text("ID Monitoring")
            Text("Tanggal")
            Text("Status")
        }
    }
}