package com.example.projekakhirpam.ui.view.kandang

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.projekakhirpam.ui.component.SelectedTextField

@Composable
fun KandangInsertView(){

}

@Preview(showBackground = true)
@Composable
private fun Insert(
    modifier: Modifier = Modifier,
    enable: Boolean = false,
) {
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("ID Kandang") },
            modifier = Modifier.fillMaxWidth()
        )
        SelectedTextField(
            selectedValue = "",
            options = listOf("Nama Hewan", "Nama Hewan", "Blm Ada"),
            label = "Hewan",
            onValueChangedEvent = {},
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Kapasitas") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Lokasi") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}