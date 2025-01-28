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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projekakhirpam.ui.component.CustomTopAppBar
import com.example.projekakhirpam.ui.component.SelectedTextField
import com.example.projekakhirpam.ui.viewmodel.PenyediaViewModel
import com.example.projekakhirpam.ui.viewmodel.hewan.ErrorHewanFormState
import com.example.projekakhirpam.ui.viewmodel.hewan.FormUpdateStateHewan
import com.example.projekakhirpam.ui.viewmodel.hewan.UpdateHewanUiEvent
import com.example.projekakhirpam.ui.viewmodel.hewan.UpdateHewanUiState
import com.example.projekakhirpam.ui.viewmodel.hewan.UpdateHewanVM
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HewanUpdateView(
    onBack: () -> Unit,
    viewModel: UpdateHewanVM = viewModel(factory = PenyediaViewModel.Factory),
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val uiState = viewModel.uiState
    val uiEvent = viewModel.uiEvent
    val coroutineScope = rememberCoroutineScope()
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
            uiState = uiEvent,
            onValueChange = viewModel::updateInsertMhsState,
            onSaveClick = {
                if (viewModel.validateFields()){
                    viewModel.updateData()
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
private fun EditBody(
    uiState: UpdateHewanUiState,
    onValueChange: (UpdateHewanUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier,
    form: FormUpdateStateHewan
) {
    Column (
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ){
        UpdateHewan(
            uiEvent = uiState.updateHewanUiState,
            onValueChange = onValueChange,
            errorState = uiState.error,
        )
        Button (
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth(),
            enabled = form !is FormUpdateStateHewan.Loading
        ) {
            if (form is FormUpdateStateHewan.Loading) {
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
private fun UpdateHewan(
    uiEvent: UpdateHewanUiEvent,
    onValueChange: (UpdateHewanUiEvent) -> Unit = {},
    errorState: ErrorHewanFormState = ErrorHewanFormState()
) {
    Column (
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("ID Hewan: ${uiEvent.idHewan}", fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = uiEvent.namaHewan,
            onValueChange = {onValueChange(uiEvent.copy(namaHewan = it))},
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
            selectedValue = uiEvent.tipePakan,
            options = listOf("Herbivora", "Karnivora", "Omnivora"),
            label = "Pakan",
            onValueChangedEvent = {onValueChange(uiEvent.copy(tipePakan = it))},
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
            value = uiEvent.populasi,
            onValueChange = {onValueChange(uiEvent.copy(populasi = it))},
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
            value = uiEvent.zonaWilayah,
            onValueChange = {onValueChange(uiEvent.copy(zonaWilayah = it))},
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