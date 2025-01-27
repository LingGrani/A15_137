package com.example.projekakhirpam.ui.view.petugas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projekakhirpam.ui.component.CustomTopAppBar
import com.example.projekakhirpam.ui.viewmodel.PenyediaViewModel
import com.example.projekakhirpam.ui.viewmodel.petugas.UpdatePetugasUiEvent
import com.example.projekakhirpam.ui.viewmodel.petugas.UpdatePetugasUiState
import com.example.projekakhirpam.ui.viewmodel.petugas.UpdatePetugasVM
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetugasUpdateView(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdatePetugasVM = viewModel(factory = PenyediaViewModel.Factory),
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
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
                .fillMaxWidth()
        )
    }
}

@Composable
private fun EditBody(
    uiState: UpdatePetugasUiState,
    onValueChange: (UpdatePetugasUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ){
        Update(
            uiEvent = uiState.updatePetugasUiEvent,
            onValueChange = onValueChange,
        )
        Button (
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = androidx.compose.ui.Modifier.fillMaxWidth()
        ) {
            Text("Simpan")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Update(
    uiEvent: UpdatePetugasUiEvent = UpdatePetugasUiEvent(),
    onValueChange: (UpdatePetugasUiEvent) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val list = listOf("Keeper", "Dokter Hewan", "Kurator")
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = uiEvent.idPetugas,
            onValueChange = { onValueChange(uiEvent.copy(idPetugas = it)) },
            label = { Text("ID") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            readOnly = true,
            enabled = false
        )
        OutlinedTextField(
            value = uiEvent.namaPetugas,
            onValueChange = { onValueChange(uiEvent.copy(namaPetugas = it)) },
            label = { Text("Nama") },
            modifier = Modifier.fillMaxWidth()
        )

        Column(modifier = Modifier.fillMaxWidth()) {
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
    }
}
