package com.example.projekakhirpam.ui.view.kandang

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projekakhirpam.ui.component.CustomTopAppBar
import com.example.projekakhirpam.ui.component.DeleteConfirmationDialog
import com.example.projekakhirpam.ui.viewmodel.PenyediaViewModel
import com.example.projekakhirpam.ui.viewmodel.hewan.DetailHewanUiState
import com.example.projekakhirpam.ui.viewmodel.kandang.DetailKandangUiState
import com.example.projekakhirpam.ui.viewmodel.kandang.DetailKandangVM
import com.example.projekakhirpam.ui.viewmodel.kandang.KandangWithHewan


@Composable
fun KandangDetailView (
    viewModel: DetailKandangVM = viewModel(factory = PenyediaViewModel.Factory),
    onBack: () -> Unit,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    onEditClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            CustomTopAppBar(
                judul = "Detail Kandang",
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
                    (it as DetailKandangUiState.Success).kandangWithHewan.kandang.idKandang
                })
                onBack()
            }
        )
    }
}

@Composable
private fun DetailStatus (
    detailUiState: DetailKandangUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onEditClick: (String) -> Unit = {},
    onDeleteClick: () -> Unit
){
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
    when (detailUiState) {
        is DetailKandangUiState.Loading -> com.example.projekakhirpam.ui.view.hewan.OnLoading(modifier = modifier.fillMaxSize())
        is DetailKandangUiState.Success ->
            if (detailUiState.kandangWithHewan.kandang.idKandang == 0) {
                return Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Tidak ada data")
                }
            } else {
                Column {
                    DetailBody(
                        data = detailUiState.kandangWithHewan,
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
                        onClick = { onEditClick(detailUiState.kandangWithHewan.kandang.idKandang.toString()) }
                    ) {
                        Text(text = "Edit")
                    }
                }
            }
        is DetailKandangUiState.Error -> com.example.projekakhirpam.ui.view.hewan.OnError(
            retryAction,
            modifier = modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun DetailBody(
    modifier: Modifier,
    data: KandangWithHewan
) {
    Row (
        modifier = modifier
    ){
        Column (
        ){
            Text("IDKandang")
            Text(data.kandang.idKandang.toString())
            Text("IDHewan")
            Text(data.kandang.idHewan.toString())
            Text("Kapasitas")
            Text(data.kandang.kapasitas.toString())
            Text("Lokasi")
            Text(data.kandang.lokasi)
        }
        Column {
            Text("IDHewan")
            Text(data.hewan?.idHewan.toString())
            Text("Nama Hewan")
            data.hewan?.let { Text(it.namaHewan) }
            Text("Tipe Pakan")
            data.hewan?.let { Text(it.tipePakan) }
            Text("Populasi")
            Text(data.hewan?.populasi.toString())
        }
    }
}