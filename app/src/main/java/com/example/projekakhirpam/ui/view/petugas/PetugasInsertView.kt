package com.example.projekakhirpam.ui.view.petugas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import com.example.projekakhirpam.ui.component.SelectedTextField
import com.example.projekakhirpam.ui.viewmodel.PenyediaViewModel
import com.example.projekakhirpam.ui.viewmodel.petugas.InsertPetugasUiEvent
import com.example.projekakhirpam.ui.viewmodel.petugas.InsertPetugasUiState
import com.example.projekakhirpam.ui.viewmodel.petugas.InsertPetugasVM
import kotlinx.coroutines.launch

@Composable
fun PetugasInsertView(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertPetugasVM = viewModel(factory = PenyediaViewModel.Factory),
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
){
    val coroutineScope = rememberCoroutineScope()
    Scaffold (
        topBar = {
            CustomTopAppBar(
                judul = "Tambah Petugas",
                showBackButton = true,
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange,
                onBack = onBack
            )
        }
    ){ innerPadding ->
        EntryBody(
            insertUiState = viewModel.uiState,
            onMhsValueChange = viewModel::updateInsertMhsState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.insertHewan()
                    onBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@Composable
private fun EntryBody(
    insertUiState: InsertPetugasUiState,
    onMhsValueChange: (InsertPetugasUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ){
        Insert(
            insertUiEvent = insertUiState.insertPetugasUiEvent,
            onValueChange = onMhsValueChange,
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

@Preview(showBackground = true)
@Composable
private fun Insert(
    insertUiEvent: InsertPetugasUiEvent = InsertPetugasUiEvent(),
    onValueChange: (InsertPetugasUiEvent) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val list = listOf("Keeper", "Dokter Hewan", "Kurator")
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = insertUiEvent.idPetugas,
            onValueChange = {onValueChange(insertUiEvent.copy(idPetugas = it))},
            label = { Text("ID") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = insertUiEvent.namaPetugas,
            onValueChange = {onValueChange(insertUiEvent.copy(namaPetugas = it))},
            label = { Text("Nama") },
            modifier = Modifier.fillMaxWidth()
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            list.forEach { selected ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = insertUiEvent.jabatan == selected,
                        onClick = { onValueChange(insertUiEvent.copy(jabatan = selected)) }
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