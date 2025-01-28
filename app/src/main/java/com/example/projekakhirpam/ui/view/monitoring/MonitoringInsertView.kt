package com.example.projekakhirpam.ui.view.monitoring

import android.health.connect.datatypes.WheelchairPushesRecord
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
import com.example.projekakhirpam.ui.component.OnError
import com.example.projekakhirpam.ui.component.OnLoading
import com.example.projekakhirpam.ui.component.SelectedTextField
import com.example.projekakhirpam.ui.component.TimeDatePickerSQL
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
    onBack: () -> Unit,
    viewModel: InsertMonitoringVM = viewModel(factory = PenyediaViewModel.Factory),
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
){
    val coroutineScope = rememberCoroutineScope()
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
                    viewModel.insertMonitoring()
                    onBack()
                }
            },
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth(),
            retryAction = viewModel::getData
        )

    }
}

@Composable
private fun EntryBody(
    insertUiState: InsertMonitoringUiState,
    onValueChange: (InsertMonitoringUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier,
    retryAction: () -> Unit
){
    var nilaiValid by remember { mutableStateOf(false) }
    when(insertUiState){
        is InsertMonitoringUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
        is InsertMonitoringUiState.Error -> OnError(retryAction, Modifier.fillMaxSize())
        is InsertMonitoringUiState.Success -> {
            Column (
                verticalArrangement = Arrangement.spacedBy(18.dp),
                modifier = modifier.padding(12.dp)
            ){
                val list1: List<KandangWithHewan> = insertUiState.kandangHewanList
                val list2: List<Petugas> = insertUiState.petugasList
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


