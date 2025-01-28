package com.example.projekakhirpam.ui.view.kandang

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projekakhirpam.model.Hewan
import com.example.projekakhirpam.ui.component.CustomTopAppBar
import com.example.projekakhirpam.ui.component.OnError
import com.example.projekakhirpam.ui.component.OnLoading
import com.example.projekakhirpam.ui.component.SelectedTextField
import com.example.projekakhirpam.ui.viewmodel.PenyediaViewModel
import com.example.projekakhirpam.ui.viewmodel.kandang.ErrorKandangFormState
import com.example.projekakhirpam.ui.viewmodel.kandang.FormStateKandang
import com.example.projekakhirpam.ui.viewmodel.kandang.InsertKandangUiEvent
import com.example.projekakhirpam.ui.viewmodel.kandang.InsertKandangUiState
import com.example.projekakhirpam.ui.viewmodel.kandang.InsertKandangVM
import com.example.projekakhirpam.ui.viewmodel.petugas.FormStatePetugas
import kotlinx.coroutines.launch

@Composable
fun KandangInsertView(
    onBack: () -> Unit,
    viewModel: InsertKandangVM = viewModel(factory = PenyediaViewModel.Factory),
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
){
    val uiState = viewModel.uiState
    val uiEvent = viewModel.uiEvent
    val coroutineScope = rememberCoroutineScope()

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
            insertUiState = uiEvent,
            onValueChange = viewModel::updateInsertDataState,
            onSaveClick = {
                if (viewModel.validateFields()){
                    coroutineScope.launch {
                        viewModel.insertHewan()
                        onBack()
                    }
                }
            },
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth(),
            retryAction = viewModel::getData,
            form = uiState
        )

    }
}

@Composable
private fun EntryBody(
    insertUiState: InsertKandangUiState,
    onValueChange: (InsertKandangUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier,
    retryAction: () -> Unit,
    form: FormStateKandang
){
    Column (
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ){
        when(insertUiState){
            InsertKandangUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
            is InsertKandangUiState.Success ->
                if (insertUiState.hewanList.isEmpty()){
                    return Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Tidak ada data hewan")
                        OnLoading()
                    }
                } else {
                    Column {
                        Insert(
                            insertUiEvent = insertUiState.insertKandangUiEvent,
                            onValueChange = onValueChange,
                            data = insertUiState.hewanList,
                            errorState = insertUiState.error
                        )
                        Button (
                            onClick = onSaveClick,
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            if (form is FormStateKandang.Loading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .padding(end = 8.dp)
                                )
                                Text("Loading")
                            }
                            Text("Simpan")
                        }
                    }
                }
            InsertKandangUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun Insert(
    insertUiEvent: InsertKandangUiEvent,
    onValueChange: (InsertKandangUiEvent) -> Unit,
    data: List<Hewan>,
    errorState: ErrorKandangFormState
) {
    var namaHewan by remember { mutableStateOf("") }
    Column (
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SelectedTextField(
            selectedValue = namaHewan,
            options = data.map { it.namaHewan },
            label = "Hewan",
            onValueChangedEvent = { selectedName ->
                val selectedId = data.find { it.namaHewan == selectedName }?.idHewan
                if (selectedId != null) {
                    onValueChange(insertUiEvent.copy(idHewan = selectedId.toString()))
                    namaHewan = selectedName
                }
            },
            isError = errorState.idHewan != null,
            supportingText = {
                if (errorState.idHewan != null) {
                    Text(
                        text = errorState.idHewan,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )
        OutlinedTextField(
            value = insertUiEvent.kapasitas,
            onValueChange = {onValueChange(insertUiEvent.copy(kapasitas = it))},
            label = { Text("Kapasitas") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = errorState.kapasitas != null,
            supportingText = {
                Text(
                    text = errorState.kapasitas ?: "",
                    color = MaterialTheme.colorScheme.error
                )
            }
        )
        OutlinedTextField(
            value = insertUiEvent.lokasi,
            onValueChange = {onValueChange(insertUiEvent.copy(lokasi = it))},
            label = { Text("Lokasi") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorState.lokasi != null,
            supportingText = {
                Text(
                    text = errorState.lokasi ?: "",
                    color = MaterialTheme.colorScheme.error
                )
            }
        )
    }
}