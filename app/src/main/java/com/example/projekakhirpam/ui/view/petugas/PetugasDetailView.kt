package com.example.projekakhirpam.ui.view.petugas

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projekakhirpam.model.Petugas
import com.example.projekakhirpam.ui.component.CustomTopAppBar
import com.example.projekakhirpam.ui.component.DeleteConfirmationDialog
import com.example.projekakhirpam.ui.viewmodel.PenyediaViewModel
import com.example.projekakhirpam.ui.viewmodel.petugas.DetailPetugasUiState
import com.example.projekakhirpam.ui.viewmodel.petugas.DetailPetugasVM

@Composable
fun PetugasDetailView (
    viewModel: DetailPetugasVM = viewModel(factory = PenyediaViewModel.Factory),
    onBack: () -> Unit,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    onEditClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            CustomTopAppBar(
                judul = "Detail Petugas",
                showBackButton = true,
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange,
                onBack = onBack
            )
        }
    ){ innerPadding ->
        DetailStatus(
            modifier = Modifier.padding(innerPadding),
            detailUiState = viewModel.detailUiState,
            retryAction = { viewModel.getDataBYID() },
            onBack = onBack,
            onEditClick = onEditClick,
            onDeleteClick = {
                viewModel.delete(viewModel.detailUiState.let {
                    (it as DetailPetugasUiState.Success).petugas.idPetugas
                })
                onBack()
            }
        )
    }
}

@Composable
private fun DetailStatus (
    detailUiState: DetailPetugasUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onEditClick: (String) -> Unit = {},
    onDeleteClick: () -> Unit
){
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
    when (detailUiState) {
        is DetailPetugasUiState.Loading -> com.example.projekakhirpam.ui.view.hewan.OnLoading(modifier = modifier.fillMaxSize())
        is DetailPetugasUiState.Success ->
            if (detailUiState.petugas.idPetugas == 0) {
                return Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Tidak ada data")
                }
            } else {
                Column {
                    DetailBody(
                        data = detailUiState.petugas,
                        modifier = modifier,
                    )
                    Button(
                        onClick = { deleteConfirmationRequired = true },
                    ) {
                        Text(text = "Delete")
                    }
                    if (deleteConfirmationRequired) {
                        DeleteConfirmationDialog(
                            onDeleteConfirm = {
                                deleteConfirmationRequired = false
                                onDeleteClick()
                            },
                            onDeleteCancel = { deleteConfirmationRequired = false },
                            modifier = Modifier.padding(8.dp),
                            pesan = "Apakah Anda Ingin Menghapus Data?"
                        )
                    }
                    Button(
                        onClick = { onEditClick(detailUiState.petugas.idPetugas.toString()) }
                    ) {
                        Text(text = "Edit")
                    }
                }
            }
        is DetailPetugasUiState.Error -> com.example.projekakhirpam.ui.view.hewan.OnError(
            retryAction,
            modifier = modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun DetailBody(
    modifier: Modifier = Modifier,
    data: Petugas
) {
    Column (
        modifier = modifier
    ){
        Text("ID Petugas")
        Text(data.idPetugas.toString())
        Text("Nama Petugas")
        Text(data.namaPetugas)
        Text("Jabatan")
        Text(data.jabatan)
    }
}