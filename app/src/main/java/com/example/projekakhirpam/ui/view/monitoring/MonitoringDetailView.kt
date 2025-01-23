package com.example.projekakhirpam.ui.view.monitoring

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MonitoringDetailView () {

}

@Preview(showBackground = true)
@Composable
private fun DetailBody() {
    Column {
        Text("ID Monitoring")
        Text("ID Petugas")
        Text("ID Kandang")
        Text("Tanggal")
        Text("Hewan Sakit")
        Text("Hewan Sehat")
        Text("Status")
    }
}