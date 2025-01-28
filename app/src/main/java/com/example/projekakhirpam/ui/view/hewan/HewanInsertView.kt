package com.example.projekakhirpam.ui.view.hewan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projekakhirpam.ui.component.CustomTopAppBar
import com.example.projekakhirpam.ui.component.SelectedTextField
import com.example.projekakhirpam.ui.viewmodel.PenyediaViewModel
import com.example.projekakhirpam.ui.viewmodel.hewan.InsertHewanUiEvent
import com.example.projekakhirpam.ui.viewmodel.hewan.InsertHewanUiState
import com.example.projekakhirpam.ui.viewmodel.hewan.InsertHewanVM
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HewanInsertView(
    onBack: () -> Unit,
    viewModel: InsertHewanVM = viewModel(factory = PenyediaViewModel.Factory),
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold (
        topBar = {
            CustomTopAppBar(
                judul = "Tambah Hewan",
                showBackButton = true,
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange,
                onBack = onBack
            )
        }
    ){ innerPadding ->
        EntryBody(
            insertUiState = viewModel.uiState,
            onMhsValueChange = viewModel::updateInsertMhsState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.insertHewan()
                    onBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        )
    }
}


@Composable
private fun EntryBody(
    insertUiState: InsertHewanUiState,
    onMhsValueChange: (InsertHewanUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ){
        InsertHewan(
            insertUiEvent = insertUiState.insertHewanUiEvent,
            onValueChange = onMhsValueChange,
        )
        Button (
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simpan")
        }
    }
}

@Composable
fun InsertHewan(
    insertUiEvent: InsertHewanUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (InsertHewanUiEvent) -> Unit = {},
) {
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = insertUiEvent.namaHewan,
            onValueChange = {onValueChange(insertUiEvent.copy(namaHewan = it))},
            label = { Text("Nama Hewan") },
            modifier = Modifier.fillMaxWidth(),
        )
        SelectedTextField(
            selectedValue = insertUiEvent.tipePakan,
            options = listOf("Herbivora", "Karnivora", "Omnivora"),
            label = "Pakan",
            onValueChangedEvent = {onValueChange(insertUiEvent.copy(tipePakan = it))},
            modifier = Modifier.fillMaxWidth(),

        )
        OutlinedTextField(
            value = insertUiEvent.populasi,
            onValueChange = {onValueChange(insertUiEvent.copy(populasi = it))},
            label = { Text("Populasi") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

        )
        OutlinedTextField(
            value = insertUiEvent.zonaWilayah,
            onValueChange = {onValueChange(insertUiEvent.copy(zonaWilayah = it))},
            label = { Text("Zona Wilayah") },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}