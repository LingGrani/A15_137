package com.example.projekakhirpam.ui.view.kandang

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projekakhirpam.R
import com.example.projekakhirpam.ui.component.CustomTopAppBar
import com.example.projekakhirpam.ui.component.DeleteConfirmationDialog
import com.example.projekakhirpam.ui.component.OnError
import com.example.projekakhirpam.ui.component.OnLoading
import com.example.projekakhirpam.ui.view.hewan.editDelete
import com.example.projekakhirpam.ui.viewmodel.PenyediaViewModel
import com.example.projekakhirpam.ui.viewmodel.kandang.DetailKandangUiState
import com.example.projekakhirpam.ui.viewmodel.kandang.DetailKandangVM
import com.example.projekakhirpam.ui.viewmodel.kandang.KandangWithHewan


@Composable
fun KandangDetailView (
    viewModel: DetailKandangVM = viewModel(factory = PenyediaViewModel.Factory),
    onBack: () -> Unit,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    onEditClick: (String) -> Unit,
    onHewanClick: (String) -> Unit
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
            onEditClick = onEditClick,
            onDeleteClick = {
                viewModel.delete(viewModel.detailUiState.let {
                    (it as DetailKandangUiState.Success).kandangWithHewan.kandang.idKandang
                })
                onBack()
            },
            onHewanClick = onHewanClick
        )
    }
}

@Composable
private fun DetailStatus (
    detailUiState: DetailKandangUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onEditClick: (String) -> Unit = {},
    onDeleteClick: () -> Unit,
    onHewanClick: (String) -> Unit = {}
){
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
    when (detailUiState) {
        is DetailKandangUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
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
                        onHewanClick = onHewanClick
                    )
                    editDelete(
                        edit = { onEditClick(detailUiState.kandangWithHewan.kandang.idKandang.toString()) },
                        delete = { deleteConfirmationRequired = true }
                    )
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
                }
            }
        is DetailKandangUiState.Error -> OnError(
            retryAction,
            modifier = modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun DetailBody(
    modifier: Modifier,
    data: KandangWithHewan,
    onHewanClick: (String) -> Unit = {}
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {

        // Kandang Details
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("KANDANG INFO", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                InfoRow(label = "ID Kandang", value = data.kandang.idKandang.toString())
                InfoRow(label = "ID Hewan", value = data.kandang.idHewan.toString())
                InfoRow(label = "Kapasitas", value = data.kandang.kapasitas.toString())
                InfoRow(label = "Lokasi", value = data.kandang.lokasi)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hewan Details
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
        ) {
            Row (
                modifier = Modifier.fillMaxWidth().clickable {
                    onHewanClick(data.hewan?.idHewan.toString())
                },
                verticalAlignment = Alignment.CenterVertically
            ){
                Column(modifier = Modifier.padding(16.dp).weight(9f)) {
                    Text("HEWAN INFO", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    InfoRow(label = "ID Hewan", value = data.hewan?.idHewan?.toString() ?: "-")
                    InfoRow(label = "Nama Hewan", value = data.hewan?.namaHewan ?: "-")
                    InfoRow(label = "Tipe Pakan", value = data.hewan?.tipePakan ?: "-")
                    InfoRow(label = "Populasi", value = data.hewan?.populasi?.toString() ?: "-")
                }
                Icon(
                    painter = painterResource(R.drawable.arrow_right),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .weight(1f),
                    tint = MaterialTheme.colorScheme.inverseSurface
                )
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(2f)
        )
    }
}
