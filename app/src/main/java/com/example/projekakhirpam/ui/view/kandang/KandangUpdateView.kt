package com.example.projekakhirpam.ui.view.kandang

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projekakhirpam.model.Hewan
import com.example.projekakhirpam.ui.component.CustomTopAppBar
import com.example.projekakhirpam.ui.component.OnError
import com.example.projekakhirpam.ui.component.OnLoading
import com.example.projekakhirpam.ui.component.SelectedTextField
import com.example.projekakhirpam.ui.viewmodel.PenyediaViewModel
import com.example.projekakhirpam.ui.viewmodel.kandang.UpdateKandangUiEvent
import com.example.projekakhirpam.ui.viewmodel.kandang.UpdateKandangUiState
import com.example.projekakhirpam.ui.viewmodel.kandang.UpdateKandangVM
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KandangUpdateView(
    onBack: () -> Unit,
    viewModel: UpdateKandangVM = viewModel(factory = PenyediaViewModel.Factory),
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
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
                .fillMaxWidth(),
            retryAction = viewModel::getDataById
        )
    }
}

@Composable
private fun EditBody(
    uiState: UpdateKandangUiState,
    onValueChange: (UpdateKandangUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    retryAction: () -> Unit
) {
    when(uiState){
        is UpdateKandangUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
        is UpdateKandangUiState.Success ->
            if (uiState.hewanList.isEmpty()) {
                return Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Tidak ada data")
                    OnLoading()
                }
            } else {
                Column (
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    modifier = modifier.padding(12.dp)
                ){
                    Update(
                        uiEvent = uiState.updateKandangUiEvent,
                        onValueChange = onValueChange,
                        data = uiState.hewanList,
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
        is UpdateKandangUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxWidth())
    }
}

@Composable
private fun Update(
    uiEvent: UpdateKandangUiEvent,
    onValueChange: (UpdateKandangUiEvent) -> Unit,
    data: List<Hewan>,
) {
    var namaHewan by remember {
        mutableStateOf(
            data.find { it.idHewan == uiEvent.idHewan.toIntOrNull() }?.namaHewan ?: ""
        )
    }
    Column (
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("ID Kandang: ${uiEvent.idKandang}", fontWeight = FontWeight.Bold)
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            SelectedTextField(
                selectedValue = namaHewan,
                options = data.map { it.namaHewan },
                label = "Hewan",
                onValueChangedEvent = { selectedName ->
                    val selectedId = data.find { it.namaHewan == selectedName }?.idHewan
                    if (selectedId != null) {
                        onValueChange(uiEvent.copy(idHewan = selectedId.toString()))
                        namaHewan = selectedName
                    }
                }
            )
        }
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