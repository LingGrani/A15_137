package com.example.projekakhirpam.ui.view.kandang

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projekakhirpam.R
import com.example.projekakhirpam.ui.component.BottomNavigationBar
import com.example.projekakhirpam.ui.component.CustomTopAppBar
import com.example.projekakhirpam.ui.component.OnError
import com.example.projekakhirpam.ui.component.OnLoading
import com.example.projekakhirpam.ui.component.footer
import com.example.projekakhirpam.ui.theme.herbivora
import com.example.projekakhirpam.ui.theme.karnivora
import com.example.projekakhirpam.ui.theme.omnivora
import com.example.projekakhirpam.ui.viewmodel.PenyediaViewModel
import com.example.projekakhirpam.ui.viewmodel.kandang.HomeKandangUiState
import com.example.projekakhirpam.ui.viewmodel.kandang.HomeKandangVM
import com.example.projekakhirpam.ui.viewmodel.kandang.KandangWithHewan

@Composable
fun KandangHomeView(
    viewModel: HomeKandangVM = viewModel(factory = PenyediaViewModel.Factory),
    onDetailClick: (String) -> Unit = {},
    onAddClick: () -> Unit,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    onBack: () -> Unit,
    navigateHewan: () -> Unit,
    navigateKandang: () -> Unit,
    navigateMonitoring: () -> Unit,
    navigatePetugas: () -> Unit,
) {
    val searchText by viewModel.searchText.collectAsState()
    val datas by viewModel.datas.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()

    Scaffold (
        topBar = {
            CustomTopAppBar(
                judul = "Daftar Kandang",
                showBackButton = true,
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange,
                onBack = onBack
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedTab = 1,
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
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, end = 8.dp)
                ,
                verticalAlignment = Alignment.CenterVertically
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

            }
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
                    homeUiState = viewModel.kandangUiState,
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
    homeUiState: HomeKandangUiState,
    datas: List<KandangWithHewan>,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit = {},
) {
    when (homeUiState) {
        is HomeKandangUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
        is HomeKandangUiState.Success ->
            if (datas.isEmpty()) {
                return Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Tidak ada data")
                    OnLoading()
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
        is HomeKandangUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxWidth())
    }
}

@Composable
private fun Layout (
    item1: List<KandangWithHewan>,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit = {},
) {
    LazyColumn (
        modifier = modifier,
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        items(item1) { one ->
            val id = one.kandang.idKandang.toString()
            HomeCard(
                data = one,
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { onDetailClick(id) }
            )
        }
        item {
            footer()
        }
    }
}

@Composable
private fun HomeCard(
    data: KandangWithHewan,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    val warna = when (data.hewan?.tipePakan) {
        "Herbivora" -> herbivora
        "Karnivora" -> karnivora
        "Omnivora" -> omnivora
        else -> Color.DarkGray
    }
    Card(
        modifier = Modifier
            .padding(top = 6.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ){
        Row(
            modifier = Modifier
                .background(color = warna)
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column (
                modifier = Modifier
                    .padding(8.dp)
                    .weight(9f)
            ){
                data.hewan?.let { Text(
                    it.namaHewan,
                    color = MaterialTheme.colorScheme.background,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ) }
                Text(data.kandang.lokasi, color = MaterialTheme.colorScheme.background, fontWeight = FontWeight.Medium)
            }
            Icon(
                painter = painterResource(R.drawable.arrow_right),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .weight(1f),
                tint = MaterialTheme.colorScheme.background
            )
        }
    }
}
