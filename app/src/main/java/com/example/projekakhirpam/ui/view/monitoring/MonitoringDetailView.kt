package com.example.projekakhirpam.ui.view.monitoring

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
import com.example.projekakhirpam.ui.component.CustomTopAppBar
import com.example.projekakhirpam.ui.component.DeleteConfirmationDialog
import com.example.projekakhirpam.ui.view.hewan.OnError
import com.example.projekakhirpam.ui.view.hewan.OnLoading
import com.example.projekakhirpam.ui.viewmodel.PenyediaViewModel
import com.example.projekakhirpam.ui.viewmodel.monitoring.DetailMonitoringUiState
import com.example.projekakhirpam.ui.viewmodel.monitoring.DetailMonitoringVM
import com.example.projekakhirpam.ui.viewmodel.monitoring.Join

@Composable
fun MonitoringDetailView (
    viewModel: DetailMonitoringVM = viewModel(factory = PenyediaViewModel.Factory),
    onBack: () -> Unit,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    onEditClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            CustomTopAppBar(
                judul = "Detail Monitoring",
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
                    (it as DetailMonitoringUiState.Success).join.monitoring.idMonitoring
                })
                onBack()
            }
        )
    }
}

@Composable
private fun DetailStatus (
    detailUiState: DetailMonitoringUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onEditClick: (String) -> Unit = {},
    onDeleteClick: () -> Unit
){
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
    when (detailUiState) {
        is DetailMonitoringUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
        is DetailMonitoringUiState.Success ->
            if (detailUiState.join.monitoring.idMonitoring == 0) {
                return Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Tidak ada data")
                }
            } else {
                Column {
                    DetailBody(
                        data = detailUiState.join,
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
                        onClick = { onEditClick(detailUiState.join.monitoring.idMonitoring.toString()) }
                    ) {
                        Text(text = "Edit")
                    }
                }
            }
        is DetailMonitoringUiState.Error -> OnError(
            retryAction,
            modifier = modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun DetailBody(
    modifier: Modifier = Modifier,
    data: Join
) {
    Column {
        Text("ID Monitoring ${data.monitoring.idMonitoring}")
        Text("ID Petugas ${data.monitoring.idPetugas}")
        Text("Tanggal Monitoring ${data.monitoring.tanggalMonitoring}")
        Text("Hewan Sakit ${data.monitoring.hewanSakit}")
        Text("Hewan Sehat ${data.monitoring.hewanSehat}")
        Text("Status ${data.monitoring.status}")
    }
}