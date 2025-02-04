package com.example.projekakhirpam.ui.view.petugas

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projekakhirpam.ui.component.CustomTopAppBar
import com.example.projekakhirpam.ui.viewmodel.PenyediaViewModel
import com.example.projekakhirpam.ui.viewmodel.petugas.ErrorPetugasFormState
import com.example.projekakhirpam.ui.viewmodel.petugas.FormUpdateStatePetugas
import com.example.projekakhirpam.ui.viewmodel.petugas.UpdatePetugasUiEvent
import com.example.projekakhirpam.ui.viewmodel.petugas.UpdatePetugasUiState
import com.example.projekakhirpam.ui.viewmodel.petugas.UpdatePetugasVM
import kotlinx.coroutines.launch
import okhttp3.internal.format

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetugasUpdateView(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdatePetugasVM = viewModel(factory = PenyediaViewModel.Factory),
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val uiState = viewModel.uiState
    val uiEvent = viewModel.uiEvent
    val coroutineScope = rememberCoroutineScope()
    Scaffold (
        topBar = {
            CustomTopAppBar(
                judul = "Update Petugas",
                showBackButton = true,
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange,
                onBack = onBack
            )
        }
    ){ innerPadding ->
        EditBody(
            uiState = uiEvent,
            onValueChange = viewModel::updateDataState,
            onSaveClick = {
                if (viewModel.validateFields()){
                    coroutineScope.launch {
                        viewModel.updateData()
                        onBack()
                    }
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
    uiState: UpdatePetugasUiState,
    onValueChange: (UpdatePetugasUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    form: FormUpdateStatePetugas
) {
    Column (
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ){
        Update(
            uiEvent = uiState.updatePetugasUiEvent,
            onValueChange = onValueChange,
            errorState = uiState.error
        )
        Button (
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth(),
            enabled = form !is FormUpdateStatePetugas.Loading
        ) {
            if (form is FormUpdateStatePetugas.Loading) {
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

@Preview(showBackground = true)
@Composable
private fun Update(
    uiEvent: UpdatePetugasUiEvent = UpdatePetugasUiEvent(),
    onValueChange: (UpdatePetugasUiEvent) -> Unit = {},
    modifier: Modifier = Modifier,
    errorState: ErrorPetugasFormState = ErrorPetugasFormState()
) {
    val list = listOf("Keeper", "Dokter Hewan", "Kurator")
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("ID Petugas: ${uiEvent.idPetugas}", fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = uiEvent.namaPetugas,
            onValueChange = { onValueChange(uiEvent.copy(namaPetugas = it)) },
            label = { Text("Nama") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorState.namaPetugas != null,
            supportingText = {
                Text(
                    text = errorState.namaPetugas ?: "",
                    color = MaterialTheme.colorScheme.error
                )
            }
        )

        Column(
            modifier = Modifier.fillMaxWidth()
            .border(
                width = 2.dp,
                color = if (errorState.jabatan != null) MaterialTheme.colorScheme.error else Color.Transparent,
            )
        ) {
            list.forEach { selected ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = uiEvent.jabatan == selected,
                        onClick = { onValueChange(uiEvent.copy(jabatan = selected)) }
                    )
                    Text(
                        text = selected,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
        Text(
            text = errorState.jabatan ?: "",
            color = MaterialTheme.colorScheme.error
        )
    }
}
