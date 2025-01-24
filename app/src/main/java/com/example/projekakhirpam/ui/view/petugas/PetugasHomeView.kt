package com.example.projekakhirpam.ui.view.petugas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.projekakhirpam.R

@Preview
@Composable
fun MonitoringHomeView(){
    Column (
        modifier = Modifier
            .fillMaxWidth()
    ){
        HomeCard("Keeper")
        HomeCard("Dokter Hewan")
        HomeCard("Kurator")
    }
}

@Composable
private fun HomeCard(
    jabatan: String
){
    val logoJabatan: Painter
    when (jabatan) {
        "Keeper" -> {
            logoJabatan = painterResource(R.drawable.paw)
        }
        "Dokter Hewan" -> {
            logoJabatan = painterResource(R.drawable.doctor)
        }
        "Kurator" -> {
            logoJabatan = painterResource(R.drawable.curator)
        }
        else -> {
            logoJabatan = painterResource(R.drawable.question)
        }
    }
    Card {
        Row {
            Icon(
                painter = logoJabatan,
                contentDescription = null
            )
            Text("Nama")
            Text("Jabatan")
        }
    }
}