package com.example.projekakhirpam.ui.view.hewan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.projekakhirpam.ui.component.SelectedTextField

@Composable
fun HewanInsertView(){

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
            label = { Text("Nama Hewan") },
            modifier = Modifier.fillMaxWidth()
        )
        SelectedTextField(
            selectedValue = "",
            options = listOf("Herbivora", "Karnivora", "Omnivora"),
            label = "Pakan",
            onValueChangedEvent = {},
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Populasi") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Zona Wilayah") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}