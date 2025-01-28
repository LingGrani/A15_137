package com.example.projekakhirpam.ui.view.monitoring

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projekakhirpam.model.Petugas
import com.example.projekakhirpam.ui.component.CustomTopAppBar
import com.example.projekakhirpam.ui.component.OnError
import com.example.projekakhirpam.ui.component.OnLoading
import com.example.projekakhirpam.ui.component.SelectedTextField
import com.example.projekakhirpam.ui.component.TimeDatePickerSQL
import com.example.projekakhirpam.ui.viewmodel.PenyediaViewModel
import com.example.projekakhirpam.ui.viewmodel.kandang.HomeKandangUiState
import com.example.projekakhirpam.ui.viewmodel.kandang.HomeKandangVM
import com.example.projekakhirpam.ui.viewmodel.kandang.KandangWithHewan
import com.example.projekakhirpam.ui.viewmodel.monitoring.UpdateMonitoringUiEvent
import com.example.projekakhirpam.ui.viewmodel.monitoring.UpdateMonitoringUiState
import com.example.projekakhirpam.ui.viewmodel.monitoring.InsertMonitoringVM
import com.example.projekakhirpam.ui.viewmodel.monitoring.UpdateMonitoringVM
import com.example.projekakhirpam.ui.viewmodel.petugas.HomePetugasUiState
import com.example.projekakhirpam.ui.viewmodel.petugas.HomePetugasVM
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@Composable
fun MonitoringUpdateView(
    onBack: () -> Unit,
    viewModel: UpdateMonitoringVM = viewModel(factory = PenyediaViewModel.Factory),
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
){
    val coroutineScope = rememberCoroutineScope()

    Scaffold (
        topBar = {
            CustomTopAppBar(
                judul = "Update Monitoring",
                showBackButton = true,
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange,
                onBack = onBack
            )
        }
    ){ padding ->
        UpdateBody(
            uiState = viewModel.uiState,
            onValueChange = viewModel::updateDataState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateData()
                    onBack()
                }
            },
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth(),
            retryAction = viewModel::getDataById
        )

    }
}

@Composable
private fun UpdateBody(
    uiState: UpdateMonitoringUiState,
    onValueChange: (UpdateMonitoringUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier,
    retryAction: () -> Unit,
){
    var nilaiValid by remember { mutableStateOf(false) }
    when(uiState){
        is UpdateMonitoringUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
        is UpdateMonitoringUiState.Error -> OnError(retryAction, Modifier.fillMaxSize())
        is UpdateMonitoringUiState.Success -> {
            Column (
                verticalArrangement = Arrangement.spacedBy(18.dp),
                modifier = modifier.padding(12.dp)
            ){
                val kandangHewan = uiState.kandangHewanList
                val petugas = uiState.petugasList
                Update(
                    insertUiEvent = uiState.updateMonitoringUiEvent,
                    onValueChange = onValueChange,
                    data1 = kandangHewan,
                    data2 = petugas,
                    valid = { data -> nilaiValid = data }
                )
                Button (
                    onClick = onSaveClick,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = nilaiValid
                ) {
                    Text("Simpan")
                }
            }
        }
    }
}
@Composable
private fun Update(
    insertUiEvent: UpdateMonitoringUiEvent = UpdateMonitoringUiEvent(),
    onValueChange: (UpdateMonitoringUiEvent) -> Unit = {},
    data1: List<KandangWithHewan>,
    data2: List<Petugas>,
    valid: (Boolean) -> Unit
) {
    var selectedHewanKandang by remember { mutableStateOf("") }
    var selectedNamaPetugas by remember { mutableStateOf("") }
    var selectedJabatanPetugas by remember { mutableStateOf("") }
    var selectedPopulasi by remember { mutableIntStateOf(0) }
    var sakit by remember { mutableIntStateOf(0) }
    var sehat by remember { mutableIntStateOf(0) }
    onValueChange(insertUiEvent.copy(tanggalMonitoring = LocalDateTime.now().toString()))
    selectedHewanKandang = data1.find { it.kandang.idKandang.toString() == insertUiEvent.idKandang }?.hewan?.namaHewan ?: ""
    selectedNamaPetugas = data2.find { it.idPetugas.toString() == insertUiEvent.idPetugas }?.namaPetugas ?: ""
    selectedPopulasi = data1.find { it.hewan?.namaHewan == selectedHewanKandang }?.hewan?.populasi!!
    Column(
        modifier = Modifier,
    ) {
        Text("ID Monitoring: ${insertUiEvent.idMonitoring}", fontWeight = FontWeight.Bold)
        SelectedTextField(
            selectedValue = selectedHewanKandang,
            options = data1.map { it.hewan?.namaHewan ?: "" },
            label = "Hewan",
            onValueChangedEvent = { selectedName ->
                val selectedId = data1.find { it.hewan?.namaHewan == selectedName }?.kandang?.idKandang
                val populasi = data1.find { it.hewan?.namaHewan == selectedName }?.hewan?.populasi
                if (selectedId != null) {
                    onValueChange(insertUiEvent.copy(idKandang = selectedId.toString()))
                    selectedHewanKandang = selectedName
                }
                if (populasi != null) {
                    selectedPopulasi = populasi
                }
            },
        )
        Text(
            "Populasi: $selectedPopulasi",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        // Pilih Petugas
        SelectedTextField(
            selectedValue = selectedNamaPetugas,
            options = data2.map { it.namaPetugas },
            label = "Petugas",
            onValueChangedEvent = { selectedName ->
                val selectedId = data2.find { it.namaPetugas == selectedName }?.idPetugas
                if (selectedId != null) {
                    onValueChange(insertUiEvent.copy(idPetugas = selectedId.toString()))
                    selectedNamaPetugas = selectedName
                    selectedJabatanPetugas = data2.find { it.namaPetugas == selectedName }?.jabatan ?: ""
                }
            },
        )
        Text(
            selectedJabatanPetugas,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        // Pilih Waktu
        TimeDatePickerSQL(
            onValueChangedEvent = {
                onValueChange(insertUiEvent.copy(tanggalMonitoring = it.toString()))
            }
        )

        // Input kondisi hewan sehat dan sakit
        Row {
            OutlinedTextField(
                value = insertUiEvent.hewanSehat,
                onValueChange = { nilai ->
                    if (nilai.all { it.isDigit() }) {
                        onValueChange(insertUiEvent.copy(hewanSehat = if (nilai.isEmpty()) "0" else nilai))
                        sehat = if (nilai.isEmpty()) 0 else nilai.toInt()
                        valid(false)
                    }
                },
                label = { Text("Kondisi Sehat") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = insertUiEvent.hewanSakit,
                onValueChange = { nilai ->
                    if (nilai.all { it.isDigit() }) {
                        sakit = if (nilai.isEmpty()) 0 else nilai.toInt()
                        onValueChange(insertUiEvent.copy(hewanSakit = if (nilai.isEmpty()) "0" else nilai))
                        valid(false)
                    }
                },
                label = { Text("Kondisi Sakit") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text("Status: ${insertUiEvent.status}", modifier = Modifier.weight(3f), fontWeight = FontWeight.Bold)
            Button(
                onClick = {
                    updateStatus(
                        populasi = selectedPopulasi,
                        sakit = sakit,
                        sehat = sehat,
                        update = {
                                value -> onValueChange(insertUiEvent.copy(status = value))
                            valid(value != "Data Tidak Valid")
                        }
                    )
                },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.weight(1f)
            ) {
                Text("Update")
            }
        }
    }
}