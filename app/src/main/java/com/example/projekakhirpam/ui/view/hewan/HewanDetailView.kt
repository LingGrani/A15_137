package com.example.projekakhirpam.ui.view.hewan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun HewanDetailView () {

}

@Preview(showBackground = true)
@Composable
private fun DetailBody() {
    Column {
        Text("IDHewan")
        Text("Nama Hewan")
        Text("Pakan")
        Row(

        ){
            Text("Populasi")
            Text("Number")
        }
        Text("Zona Wilayah")
    }
}