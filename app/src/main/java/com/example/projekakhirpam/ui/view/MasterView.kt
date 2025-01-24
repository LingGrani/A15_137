package com.example.projekakhirpam.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projekakhirpam.model.Monitoring

@Composable
fun MasterView(
    modifier: Modifier = Modifier,
    navigateHewan: () -> Unit,
    navigateKandang: () -> Unit,
    navigateMonitoring: () -> Unit,
    navigatePetugas: () -> Unit,
){
    Scaffold (

    ){ innerPadding ->
        Column (
            modifier = Modifier.padding(innerPadding)
        ){
            Button(
                onClick = navigateHewan,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Hewan")
            }
            Button(
                onClick = navigateKandang,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Kandang")
            }
            Button(
                onClick = navigateMonitoring,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Monitoring")
            }
            Button(
                onClick = navigatePetugas,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Petugas")
            }
        }
    }
}