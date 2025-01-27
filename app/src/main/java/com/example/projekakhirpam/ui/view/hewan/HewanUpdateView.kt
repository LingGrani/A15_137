package com.example.projekakhirpam.ui.view.hewan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projekakhirpam.ui.component.CustomTopAppBar
import com.example.projekakhirpam.ui.component.SelectedTextField
import com.example.projekakhirpam.ui.viewmodel.PenyediaViewModel
import com.example.projekakhirpam.ui.viewmodel.hewan.InsertHewanUiEvent
import com.example.projekakhirpam.ui.viewmodel.hewan.InsertHewanUiState
import com.example.projekakhirpam.ui.viewmodel.hewan.InsertHewanVM
import com.example.projekakhirpam.ui.viewmodel.hewan.UpdateHewanUiEvent
import com.example.projekakhirpam.ui.viewmodel.hewan.UpdateHewanUiState
import com.example.projekakhirpam.ui.viewmodel.hewan.UpdateHewanVM
import kotlinx.coroutines.launch
import java.lang.reflect.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HewanUpdateView(
    onBack: () -> Unit,
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier,
    viewModel: UpdateHewanVM = viewModel(factory = PenyediaViewModel.Factory),
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold (
        topBar = {
            CustomTopAppBar(
                judul = "Update Hewan",
                showBackButton = true,
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange,
                onBack = onBack
            )
        }
    ){ innerPadding ->
        EditBody(
            uiState = viewModel.uiState,
            onValueChange = viewModel::updateUpdateMhsState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateData()
                    onBack()
                }
            },
            modifier = androidx.compose.ui.Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun EditBody(
    uiState: UpdateHewanUiState,
    onValueChange: (UpdateHewanUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier
) {
    Column (
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ){
        UpdateHewan(
            uiEvent = uiState.updateHewanUiEvent,
            onValueChange = onValueChange,
        )
        Button (
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = androidx.compose.ui.Modifier.fillMaxWidth()
        ) {
            Text("Simpan")
        }
    }
}

@Composable
private fun UpdateHewan(
    uiEvent: UpdateHewanUiEvent,
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier,
    onValueChange: (UpdateHewanUiEvent) -> Unit = {},
    enable: Boolean = true
) {
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = uiEvent.idHewan,
            onValueChange = {onValueChange(uiEvent.copy(idHewan = it))},
            label = { Text("ID") },
            modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            readOnly = true,
            enabled = false
        )
        OutlinedTextField(
            value = uiEvent.namaHewan,
            onValueChange = {onValueChange(uiEvent.copy(namaHewan = it))},
            label = { Text("Nama Hewan") },
            modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
        )
        SelectedTextField(
            selectedValue = uiEvent.tipePakan,
            options = listOf("Herbivora", "Karnivora", "Omnivora"),
            label = "Pakan",
            onValueChangedEvent = {onValueChange(uiEvent.copy(tipePakan = it))},
            modifier = androidx.compose.ui.Modifier.fillMaxWidth(),

            )
        OutlinedTextField(
            value = uiEvent.populasi,
            onValueChange = {onValueChange(uiEvent.copy(populasi = it))},
            label = { Text("Populasi") },
            modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

            )
        OutlinedTextField(
            value = uiEvent.zonaWilayah,
            onValueChange = {onValueChange(uiEvent.copy(zonaWilayah = it))},
            label = { Text("Zona Wilayah") },
            modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
        )
    }
}