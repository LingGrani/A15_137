package com.example.projekakhirpam.ui.view.hewan

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projekakhirpam.R
import com.example.projekakhirpam.model.Hewan
import com.example.projekakhirpam.ui.viewmodel.PenyediaViewModel
import com.example.projekakhirpam.ui.viewmodel.hewan.HomeHewanUiState
import com.example.projekakhirpam.ui.viewmodel.hewan.HomeHewanVM

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun HewanHomeView(
    modifier: Modifier = Modifier,
    viewModel: HomeHewanVM = viewModel(factory = PenyediaViewModel.Factory),
    onDetailClick: (String) -> Unit = {},
    onAddClick: () -> Unit
) {
    val searchText by viewModel.searchText.collectAsState()
    val datas by viewModel.datas.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    Scaffold (
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
                IconButton(
                    onClick = onAddClick,
                    modifier = Modifier.padding(4.dp)
                        .background(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(100))
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(Modifier.padding(8.dp))
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
                    homeUiState = viewModel.hewanUiState,
                    retryAction = viewModel::getHewan,
                    modifier = Modifier.fillMaxSize(),
                    onDetailClick = onDetailClick,
                    datas = datas
                )
            }
        }
    }

}
@Composable
private fun HomeStatus (
    homeUiState: HomeHewanUiState,
    datas: List<Hewan>,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit = {},
) {
    when (homeUiState) {
        is HomeHewanUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
        is HomeHewanUiState.Success ->
            if (datas.isEmpty()) {
                return Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Tidak ada data kontak")
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
        is HomeHewanUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxWidth())
    }
}

@Composable
fun OnLoading(modifier: Modifier = Modifier) {
    Image (
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.question),
        contentDescription = null
    )
}

@Composable
fun OnError(retryAction:() -> Unit, modifier: Modifier = Modifier) {
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = ""
        )
        Button(onClick = retryAction) {
            Text("Coba lagi")
        }
    }
}

@Composable
private fun Layout (
    item1: List<Hewan>,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit = {},
) {
    LazyColumn (
        modifier = modifier,
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        items(item1) { one ->
            val id = one.idHewan.toString()
            HomeCard(
                hewan = one,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDetailClick(id) }
            )
        }
    }
}
@Composable
private fun HomeCard(
    hewan: Hewan,
    modifier: Modifier = Modifier,
){
    val warna:Color
    val logoPakan:Painter
    when (hewan.tipePakan) {
        "Herbivora" -> {
            warna = Color.Green
            logoPakan = painterResource(R.drawable.herbivora)
        }
        "Karnivora" -> {
            warna = Color.Red
            logoPakan = painterResource(R.drawable.karnivora)
        }
        "Omnivora" -> {
            warna = Color.Cyan
            logoPakan = painterResource(R.drawable.omnivora)
        }
        else -> {
            warna = Color.LightGray
            logoPakan = painterResource(R.drawable.question)
        }
    }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(3.dp, color = warna , shape = RoundedCornerShape(12.dp))
    ){
        Column (
            modifier = Modifier
                .background(warna, shape = RoundedCornerShape(12.dp))
                .fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ){
            Icon(
                painter = logoPakan,
                contentDescription = null,
                Modifier.padding(8.dp)
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column (
                    modifier = Modifier
                        .weight(9f),
                ){
                    Text(hewan.namaHewan)
                    Text(hewan.tipePakan)
                    Row {
                        Text("Populasi: ")
                        Text(hewan.populasi.toString())
                    }
                }
                Icon(
                    painter = painterResource(R.drawable.arrow_right),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .weight(1f),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}