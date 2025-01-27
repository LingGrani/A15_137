package com.example.projekakhirpam.ui.view.monitoring

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projekakhirpam.R
import com.example.projekakhirpam.model.Petugas
import com.example.projekakhirpam.ui.component.CustomTopAppBar

import com.example.projekakhirpam.ui.component.SelectedTextField
import com.example.projekakhirpam.ui.component.TimeDatePickerSQL
import com.example.projekakhirpam.ui.view.hewan.OnLoading
import com.example.projekakhirpam.ui.viewmodel.PenyediaViewModel
import com.example.projekakhirpam.ui.viewmodel.hewan.HomeHewanUiState
import com.example.projekakhirpam.ui.viewmodel.hewan.HomeHewanVM
import com.example.projekakhirpam.ui.viewmodel.kandang.HomeKandangUiState
import com.example.projekakhirpam.ui.viewmodel.kandang.HomeKandangVM
import com.example.projekakhirpam.ui.viewmodel.kandang.KandangWithHewan
import com.example.projekakhirpam.ui.viewmodel.monitoring.InsertMonitoringUiEvent
import com.example.projekakhirpam.ui.viewmodel.monitoring.InsertMonitoringUiState
import com.example.projekakhirpam.ui.viewmodel.monitoring.InsertMonitoringVM
import com.example.projekakhirpam.ui.viewmodel.petugas.HomePetugasUiState
import com.example.projekakhirpam.ui.viewmodel.petugas.HomePetugasVM
import kotlinx.coroutines.launch
import java.time.LocalDateTime


@Composable
fun MonitoringInsertView(
    data1: HomeKandangVM = viewModel(factory = PenyediaViewModel.Factory),
    data2: HomePetugasVM = viewModel(factory = PenyediaViewModel.Factory),
    onBack: () -> Unit,
    viewModel: InsertMonitoringVM = viewModel(factory = PenyediaViewModel.Factory),
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
            data1 = kandangUiState,
            data2 = petugasUiState
        )

    }
}

@Composable
private fun EntryBody(
    insertUiState: InsertMonitoringUiState,
    onValueChange: (InsertMonitoringUiEvent) -> Unit,
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
                        Insert(
                            insertUiEvent = insertUiState.insertMonitoringUiEvent,
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
private fun Insert(
    insertUiEvent: InsertMonitoringUiEvent = InsertMonitoringUiEvent(),
    onValueChange: (InsertMonitoringUiEvent) -> Unit = {},
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
    onValueChange(insertUiEvent.copy(tanggal = LocalDateTime.now().toString()))

    Column(
        modifier = Modifier,
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
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
                modifier = Modifier.weight(2f).padding(16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }
        Text(
            "Populasi: $selectedPopulasi",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        // Pilih Petugas
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            SelectedTextField(
                selectedValue = insertUiEvent.idPetugas,
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
                modifier = Modifier.weight(1f)
            )
            Text(
                selectedNamaPetugas,
                modifier = Modifier.weight(2f).padding(16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }
        Text(
            selectedJabatanPetugas,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        // Pilih Waktu
        TimeDatePickerSQL(
            onValueChangedEvent = {
                onValueChange(insertUiEvent.copy(tanggal = it.toString()))
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

fun updateStatus(
    populasi: Int = 0,
    sakit: Int = 0,
    sehat: Int = 0,
    update: (String) -> Unit
) {
    if (populasi == 0) {
        update("Data Tidak Valid")
        return
    }
    if (populasi == (sakit + sehat)) {
        // Hitung status berdasarkan persentase hewan sakit
        val status = when {
            sakit < (populasi * 0.1) -> "Aman"
            sakit < (populasi * 0.5) -> "Waspada"
            else -> "Kritis"
        }
        update(status)
    } else {
        update("Data Tidak Valid")
    }
}


