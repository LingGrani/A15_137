package com.example.projekakhirpam.ui.view.kandang

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun KandangDetailView () {

}

@Preview(showBackground = true)
@Composable
private fun DetailBody() {
    Column {
        Text("IDKandang")
        Row {
            Text("IDHewan")
            Text("Value IDHewan")
        }
        Text("Kapasitas")
        Text("Lokasi")
    }
}