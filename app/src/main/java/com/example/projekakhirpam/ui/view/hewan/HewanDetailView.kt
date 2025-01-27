package com.example.projekakhirpam.ui.view.hewan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projekakhirpam.R
import com.example.projekakhirpam.model.Hewan
import com.example.projekakhirpam.ui.component.CustomTopAppBar
import com.example.projekakhirpam.ui.component.DeleteConfirmationDialog
import com.example.projekakhirpam.ui.theme.herbivora
import com.example.projekakhirpam.ui.theme.karnivora
import com.example.projekakhirpam.ui.theme.omnivora
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
                Column (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    DetailBody (
                        data = detailUiState.hewan,
                        modifier = modifier,
                    )
                    editDelete(
                        edit = { onEditClick(detailUiState.hewan.idHewan.toString()) },
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
                            pesan = "Apakah anda ingin mengahapus data?"
                        )
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
    val (warna, logoPakan) = when (data.tipePakan) {
        "Herbivora" -> herbivora to painterResource(R.drawable.herbivora)
        "Karnivora" -> karnivora to painterResource(R.drawable.karnivora)
        "Omnivora" -> omnivora to painterResource(R.drawable.omnivora)
        else -> Color.LightGray to painterResource(R.drawable.question)
    }
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = modifier)
        Column (
            modifier = Modifier
                .background (color = warna, shape = RoundedCornerShape(10))
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Icon(
                painter = logoPakan,
                contentDescription = null,
                modifier = Modifier
                    .padding(32.dp)
                    .size(128.dp)
                    .background(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(100))
                    .padding(16.dp),
                tint = warna
            )
            Column (
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(20))
                    .padding(16.dp)
            ){
                InfoRow("ID Hewan", data.idHewan.toString())
                InfoRow("Nama Hewan", data.namaHewan)
                InfoRow("Tipe Pakan", data.tipePakan)
                InfoRow("Populasi", data.populasi.toString())
                InfoRow("Zona Wilayah", data.zonaWilayah)
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row (
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text("$label: ", fontWeight = FontWeight.Bold)
        Text(value)
    }
}
