package com.example.projekakhirpam.ui.view.hewan

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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
import com.example.projekakhirpam.model.Hewan
import com.example.projekakhirpam.ui.component.CustomTopAppBar
import com.example.projekakhirpam.ui.component.DeleteConfirmationDialog
import com.example.projekakhirpam.ui.viewmodel.PenyediaViewModel
import com.example.projekakhirpam.ui.viewmodel.hewan.DetailHewanUiState
import com.example.projekakhirpam.ui.viewmodel.hewan.DetailHewanVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HewanDetailView (
    onBack: () -> Unit,
    onEditClick: (String) -> Unit = {},
    viewModel: DetailHewanVM = viewModel(factory = PenyediaViewModel.Factory),
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            CustomTopAppBar(
                judul = "Detail Hewan",
                showBackButton = true,
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange,
                onBack = onBack
            )
        }
    ) { innerPadding ->
        DetailStatus (
            detailUiState = viewModel.detailUiState,
            retryAction = { viewModel.getDataBYID() },
            modifier = Modifier.padding(innerPadding),
            onBack = onBack,
            onEditClick = onEditClick,
            onDeleteClick = {
                viewModel.deleteHewan(viewModel.detailUiState.let {
                    (it as DetailHewanUiState.Success).hewan.idHewan
                })
                onBack()
            }
        )
    }
}

@Composable
private fun DetailStatus (
    detailUiState: DetailHewanUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onEditClick: (String) -> Unit = {},
    onDeleteClick: () -> Unit
) {
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
    when (detailUiState) {
        is DetailHewanUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
        is DetailHewanUiState.Success ->
            if (detailUiState.hewan.idHewan == 0) {
                return Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Tidak ada data")
                }
            } else {
                Column {
                    DetailBody (
                        data = detailUiState.hewan,
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
                            pesan = "Apakah anda ingin mengahapus data?"
                        )
                    }
                    Button(
                        onClick = { onEditClick(detailUiState.hewan.idHewan.toString()) }
                    ) {
                        Text(text = "Edit")
                    }
                }
            }
        is DetailHewanUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxWidth())
    }
}

@Composable
private fun DetailBody(
    data: Hewan,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
    ){
        Text(data.idHewan.toString())
        Text(data.namaHewan)
        Text(data.tipePakan)
        Row(
        ){
            Text("Populasi")
            Text(data.populasi.toString())
        }
        Text(data.zonaWilayah)
    }
}
