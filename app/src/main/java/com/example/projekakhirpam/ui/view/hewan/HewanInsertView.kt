package com.example.projekakhirpam.ui.view.hewan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projekakhirpam.ui.component.CustomTopAppBar
import com.example.projekakhirpam.ui.component.SelectedTextField
import com.example.projekakhirpam.ui.viewmodel.PenyediaViewModel
import com.example.projekakhirpam.ui.viewmodel.hewan.ErrorHewanFormState
import com.example.projekakhirpam.ui.viewmodel.hewan.FormStateHewan
import com.example.projekakhirpam.ui.viewmodel.hewan.InsertHewanUiEvent
import com.example.projekakhirpam.ui.viewmodel.hewan.InsertHewanUiState
import com.example.projekakhirpam.ui.viewmodel.hewan.InsertHewanVM
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HewanInsertView(
    onBack: () -> Unit,
    viewModel: InsertHewanVM = viewModel(factory = PenyediaViewModel.Factory),
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
) {
    val uiState = viewModel.uiState
    val uiEvent = viewModel.uiEvent
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
            insertUiState = uiEvent,
            onMhsValueChange = viewModel::updateInsertDataState,
            onSaveClick = {
                if (viewModel.validateFields()) {
                    viewModel.insertHewan()
                    onBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            form = uiState
        )
    }
}


@Composable
private fun EntryBody(
    insertUiState: InsertHewanUiState,
    onMhsValueChange: (InsertHewanUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    form: FormStateHewan
) {
    Column (
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ){
        InsertHewan(
            insertUiEvent = insertUiState.insertHewanUiEvent,
            onValueChange = onMhsValueChange,
            errorState = insertUiState.error
        )
        Button (
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth(),
            enabled = form !is FormStateHewan.Loading
        ) {
            if (form is FormStateHewan.Loading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier
                        .size(20.dp)
                        .padding(end = 8.dp)
                )
                Text("Loading")
            } else {
                Text("Simpan")
            }
        }
    }
}

@Composable
fun InsertHewan(
    insertUiEvent: InsertHewanUiEvent,
    errorState: ErrorHewanFormState = ErrorHewanFormState(),
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
            isError = errorState.namaHewan != null,
            supportingText = {
                Text (
                    text = errorState.namaHewan ?: "",
                    color = MaterialTheme.colorScheme.error
                )
            }
        )
        SelectedTextField(
            selectedValue = insertUiEvent.tipePakan,
            options = listOf("Herbivora", "Karnivora", "Omnivora"),
            label = "Pakan",
            onValueChangedEvent = {onValueChange(insertUiEvent.copy(tipePakan = it))},
            modifier = Modifier.fillMaxWidth(),
            isError = errorState.tipePakan != null,
            supportingText = {
                Text(
                    text = errorState.tipePakan ?: "",
                    color = MaterialTheme.colorScheme.error
                )
            }
        )
        OutlinedTextField(
            value = insertUiEvent.populasi,
            onValueChange = {onValueChange(insertUiEvent.copy(populasi = it))},
            label = { Text("Populasi") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = errorState.populasi != null,
            supportingText = {
                Text(
                    text = errorState.populasi ?: "",
                    color = MaterialTheme.colorScheme.error
                )
            }
        )
        OutlinedTextField(
            value = insertUiEvent.zonaWilayah,
            onValueChange = {onValueChange(insertUiEvent.copy(zonaWilayah = it))},
            label = { Text("Zona Wilayah") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorState.zonaWilayah != null,
            supportingText = {
                Text(
                    text = errorState.zonaWilayah ?: "",
                    color = MaterialTheme.colorScheme.error
                )
            }
        )
    }
}