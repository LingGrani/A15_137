package com.example.projekakhirpam.ui.view.kandang

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projekakhirpam.ui.component.CustomTopAppBar
import com.example.projekakhirpam.ui.component.SelectedTextField
import com.example.projekakhirpam.ui.viewmodel.PenyediaViewModel
import com.example.projekakhirpam.ui.viewmodel.hewan.HomeHewanUiState
import com.example.projekakhirpam.ui.viewmodel.hewan.HomeHewanVM
import com.example.projekakhirpam.ui.viewmodel.kandang.InsertKandangUiEvent
import com.example.projekakhirpam.ui.viewmodel.kandang.UpdateKandangUiEvent
import com.example.projekakhirpam.ui.viewmodel.kandang.UpdateKandangUiState
import com.example.projekakhirpam.ui.viewmodel.kandang.UpdateKandangVM
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KandangUpdateView(
    data: HomeHewanVM = viewModel(factory = PenyediaViewModel.Factory),
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdateKandangVM = viewModel(factory = PenyediaViewModel.Factory),
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val hewanUiState = data.hewanUiState
    Scaffold (
        topBar = {
            CustomTopAppBar(
                judul = "Update Kandang",
                showBackButton = true,
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange,
                onBack = onBack
            )
        }
    ){ innerPadding ->
        EditBody(
            uiState = viewModel.uiState,
            onValueChange = viewModel::updateDataState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateData()
                    onBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
            data = hewanUiState
        )
    }
}

@Composable
private fun EditBody(
    uiState: UpdateKandangUiState,
    onValueChange: (UpdateKandangUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    data: HomeHewanUiState
) {
    val list: List<Pair<Int?, String?>> = when (data) {
        is HomeHewanUiState.Success -> data.hewanList.map { it.idHewan to it.namaHewan }
        else -> listOf(null to null)
    }
    Column (
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ){
        Update(
            uiEvent = uiState.updateKandangUiEvent,
            onValueChange = onValueChange,
            data = list
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
private fun Update(
    uiEvent: UpdateKandangUiEvent,
    onValueChange: (UpdateKandangUiEvent) -> Unit,
    data: List<Pair<Int?, String?>>
) {
    Column (
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = uiEvent.idKandang,
            onValueChange = {onValueChange(uiEvent.copy(idKandang = it))},
            label = { Text("ID Kandang") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )
        SelectedTextField(
            selectedValue = uiEvent.idHewan,
            options = data.map { it.second ?: "" },
            label = "Hewan",
            onValueChangedEvent = { selectedName ->
                val selectedId = data.find { it.second == selectedName }?.first
                if (selectedId != null) {
                    onValueChange(uiEvent.copy(idHewan = selectedId.toString()))
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = uiEvent.kapasitas,
            onValueChange = {onValueChange(uiEvent.copy(kapasitas = it))},
            label = { Text("Kapasitas") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = uiEvent.lokasi,
            onValueChange = {onValueChange(uiEvent.copy(lokasi = it))},
            label = { Text("Lokasi") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}