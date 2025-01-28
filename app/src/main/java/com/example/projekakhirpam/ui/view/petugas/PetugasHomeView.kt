package com.example.projekakhirpam.ui.view.petugas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projekakhirpam.R
import com.example.projekakhirpam.model.Petugas
import com.example.projekakhirpam.ui.component.BottomNavigationBar
import com.example.projekakhirpam.ui.component.CustomTopAppBar
import com.example.projekakhirpam.ui.component.OnError
import com.example.projekakhirpam.ui.component.OnLoading
import com.example.projekakhirpam.ui.component.footer
import com.example.projekakhirpam.ui.viewmodel.PenyediaViewModel
import com.example.projekakhirpam.ui.viewmodel.petugas.HomePetugasUiState
import com.example.projekakhirpam.ui.viewmodel.petugas.HomePetugasVM

@Composable
fun PetugasHomeView(
    viewModel: HomePetugasVM = viewModel(factory = PenyediaViewModel.Factory),
    onDetailClick: (String) -> Unit = {},
    onAddClick: () -> Unit,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    onBack: () -> Unit,
    navigateHewan: () -> Unit,
    navigateKandang: () -> Unit,
    navigateMonitoring: () -> Unit,
    navigatePetugas: () -> Unit,
){
    val searchText by viewModel.searchText.collectAsState()
    val datas by viewModel.datas.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()

    Scaffold (
        topBar = {
            CustomTopAppBar(
                judul = "Daftar Petugas",
                showBackButton = true,
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange,
                onBack = onBack
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedTab = 3,
                navigateHewan = navigateHewan,
                navigateKandang = navigateKandang,
                navigateMonitoring = navigateMonitoring,
                navigatePetugas = navigatePetugas
            )
        },
        floatingActionButton = {
            IconButton(
                onClick = onAddClick,
                modifier = Modifier.padding(4.dp)
                    .background(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(30))
                    .size(64.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ){ innerPadding ->
        Column (
            modifier = Modifier
                .padding(innerPadding)
                .padding(8.dp)
        ){
            OutlinedTextField(
                value = searchText,
                onValueChange = viewModel::onSearchTextChange,
                label = { Text("Search") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                }
            )
            Divider(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth(),
                thickness = 2.dp
            )
            if(isSearching) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            } else {
                HomeStatus(
                    homeUiState = viewModel.petugasUiState,
                    retryAction = viewModel::getData,
                    modifier = Modifier.fillMaxSize(),
                    onDetailClick = onDetailClick,
                    datas = datas
                )
            }
        }
    }
}

@Composable
private fun HomeStatus(
    homeUiState: HomePetugasUiState,
    datas: List<Petugas>,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit = {},
) {
    when (homeUiState) {
        is HomePetugasUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
        is HomePetugasUiState.Success ->
            if (datas.isEmpty()) {
                return Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Tidak ada data")
                }
            } else {
                Layout (
                    item1 = datas,
                    modifier = modifier.fillMaxSize(),
                    onDetailClick = {
                        onDetailClick(it)
                    }
                )
            }
        is HomePetugasUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxWidth())
    }
}

@Composable
private fun Layout (
    item1: List<Petugas>,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit = {},
) {
    LazyColumn (
        modifier = modifier,
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        items(item1) { one ->
            val id = one.idPetugas.toString()
            HomeCard(
                data = one,
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { onDetailClick(id) }
            )
        }
        item{
            footer()
        }
    }
}

@Composable
private fun HomeCard(
    data: Petugas,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val logoJabatan: Painter = when (data.jabatan) {
        "Keeper" -> painterResource(R.drawable.paw)
        "Dokter Hewan" -> painterResource(R.drawable.doctor)
        "Kurator" -> painterResource(R.drawable.curator)
        else -> painterResource(R.drawable.question)
    }

    Card(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = logoJabatan,
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .padding(end = 16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Column {
                    Text(
                        text = data.namaPetugas,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = data.jabatan,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Icon(
                painter = painterResource(R.drawable.arrow_right),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}