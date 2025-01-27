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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projekakhirpam.model.Petugas
import com.example.projekakhirpam.ui.component.CustomTopAppBar
import com.example.projekakhirpam.ui.component.SelectedTextField
import com.example.projekakhirpam.ui.component.TimeDatePickerSQL
import com.example.projekakhirpam.ui.view.hewan.OnLoading
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
    data1: HomeKandangVM = viewModel(factory = PenyediaViewModel.Factory),
    data2: HomePetugasVM = viewModel(factory = PenyediaViewModel.Factory),
    onBack: () -> Unit,
    viewModel: UpdateMonitoringVM = viewModel(factory = PenyediaViewModel.Factory),
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
){
    val coroutineScope = rememberCoroutineScope()
    val kandangUiState = data1.kandangUiState
    val petugasUiState = data2.petugasUiState
    Scaffold (
        topBar = {
            CustomTopAppBar(
                judul = "Tambah Monitoring",
                showBackButton = true,
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange,
                onBack = onBack
            )
        }
    ){ padding ->
        UpdateBody(
            insertUiState = viewModel.uiState,
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
            data1 = kandangUiState,
            data2 = petugasUiState
        )

    }
}

@Composable
private fun UpdateBody(
    insertUiState: UpdateMonitoringUiState,
    onValueChange: (UpdateMonitoringUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier,
    data1: HomeKandangUiState,
    data2: HomePetugasUiState
){
    var nilaiValid by remember { mutableStateOf(false) }
    when(data1){
        is HomeKandangUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
        is HomeKandangUiState.Success -> {
            when(data2){
                is HomePetugasUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
                is HomePetugasUiState.Success -> {
                    Column (
                        verticalArrangement = Arrangement.spacedBy(18.dp),
                        modifier = modifier.padding(12.dp)
                    ){
                        val list1: List<KandangWithHewan> = when (data1) {
                            else -> data1.kandangWithHewanList
                        }
                        val list2: List<Petugas> = when (data2) {
                            else -> data2.list
                        }
                        Update(
                            insertUiEvent = insertUiState.updateMonitoringUiEvent,
                            onValueChange = onValueChange,
                            data1 = list1,
                            data2 = list2,
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
                is HomePetugasUiState.Error -> TODO()
            }
        }
        is HomeKandangUiState.Error -> TODO()
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

    Column(
        modifier = Modifier,
    ) {
        OutlinedTextField(
            value = insertUiEvent.idMonitoring,
            onValueChange = { nilai ->
                if (nilai.all { it.isDigit() }) {
                    onValueChange(insertUiEvent.copy(idMonitoring = nilai))
                }
            },
            label = { Text("ID Monitoring") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            enabled = false,
            readOnly = false
        )
        Row {
            SelectedTextField(
                selectedValue = insertUiEvent.idKandang,
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
                modifier = Modifier.weight(1f)
            )
            Text(
                selectedHewanKandang,
                modifier = Modifier.weight(2f)
            )
        }

        // Pilih Petugas
        Row {
            SelectedTextField(
                selectedValue = insertUiEvent.idPetugas,
                options = data2.map { it.namaPetugas },
                label = "Petugas",
                onValueChangedEvent = { selectedName ->
                    val selectedId = data2.find { it.namaPetugas == selectedName }?.idPetugas
                    if (selectedId != null) {
                        onValueChange(insertUiEvent.copy(idPetugas = selectedId.toString()))
                    }
                },
                modifier = Modifier.weight(1f)
            )
            Text(
                selectedNamaPetugas,
                modifier = Modifier.weight(2f)
            )
        }
        Text(selectedJabatanPetugas)
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
        Row {
            Text("Status: ${insertUiEvent.status}")
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
                }
            ) {
                Text("Update")
            }
        }
        Text(insertUiEvent.tanggalMonitoring)
        Text(selectedPopulasi.toString())
    }
}