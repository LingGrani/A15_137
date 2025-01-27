package com.example.projekakhirpam.ui.view.kandang

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projekakhirpam.ui.component.CustomTopAppBar
import com.example.projekakhirpam.ui.component.SelectedTextField
import com.example.projekakhirpam.ui.view.hewan.OnError
import com.example.projekakhirpam.ui.view.hewan.OnLoading
import com.example.projekakhirpam.ui.viewmodel.PenyediaViewModel
import com.example.projekakhirpam.ui.viewmodel.hewan.HomeHewanUiState
import com.example.projekakhirpam.ui.viewmodel.hewan.HomeHewanVM
import com.example.projekakhirpam.ui.viewmodel.kandang.InsertKandangUiEvent
import com.example.projekakhirpam.ui.viewmodel.kandang.InsertKandangUiState
import com.example.projekakhirpam.ui.viewmodel.kandang.InsertKandangVM
import kotlinx.coroutines.launch

@Composable
fun KandangInsertView(
    data: HomeHewanVM = viewModel(factory = PenyediaViewModel.Factory),
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertKandangVM = viewModel(factory = PenyediaViewModel.Factory),
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
){
    val coroutineScope = rememberCoroutineScope()
    val hewanUiState = data.hewanUiState
    Scaffold (
        topBar = {
            CustomTopAppBar(
                judul = "Tambah Kandang",
                showBackButton = true,
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange,
                onBack = onBack
            )
        }
    ){ padding ->
        EntryBody(
            insertUiState = viewModel.uiState,
            onValueChange = viewModel::updateInsertDataState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.insertHewan()
                    onBack()
                }
            },
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth(),
            data = hewanUiState
        )

    }
}

@Composable
private fun EntryBody(
    insertUiState: InsertKandangUiState,
    onValueChange: (InsertKandangUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier,
    data: HomeHewanUiState
){
    Column (
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ){
        when(data){
            is HomeHewanUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
            is HomeHewanUiState.Success ->
                if (data.hewanList.isEmpty()) {
                    return Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Tidak ada data")
                        OnLoading()
                    }
                } else {
                    val list: List<Pair<Int?, String?>> = data.hewanList.map { it.idHewan to it.namaHewan }
                    Insert(
                        insertUiEvent = insertUiState.insertKandangUiEvent,
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
            is HomeHewanUiState.Error -> TODO()
        }
    }
}

@Composable
private fun Insert(
    insertUiEvent: InsertKandangUiEvent,
    onValueChange: (InsertKandangUiEvent) -> Unit,
    data: List<Pair<Int?, String?>>
) {
    var namaHewan by remember { mutableStateOf("") }
    Column (
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            SelectedTextField(
                selectedValue = insertUiEvent.idHewan,
                options = data.map { it.second ?: "" },
                label = "Hewan",
                onValueChangedEvent = { selectedName ->
                    val selectedId = data.find { it.second == selectedName }?.first
                    if (selectedId != null) {
                        onValueChange(insertUiEvent.copy(idHewan = selectedId.toString()))
                        namaHewan = selectedName
                    }
                },
                modifier = Modifier.fillMaxWidth().weight(4f)
            )
            Text(
                namaHewan,
                modifier = Modifier.weight(7f).padding(16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }
        OutlinedTextField(
            value = insertUiEvent.kapasitas,
            onValueChange = {onValueChange(insertUiEvent.copy(kapasitas = it))},
            label = { Text("Kapasitas") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = insertUiEvent.lokasi,
            onValueChange = {onValueChange(insertUiEvent.copy(lokasi = it))},
            label = { Text("Lokasi") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}