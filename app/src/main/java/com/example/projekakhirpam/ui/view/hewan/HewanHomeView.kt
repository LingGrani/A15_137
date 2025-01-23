package com.example.projekakhirpam.ui.view.hewan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.projekakhirpam.R


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun HewanHomeView(
    modifier: Modifier = Modifier
) {
    Scaffold (
    ){ innerPadding ->
        Column (
            modifier = Modifier
                .padding(innerPadding)
                .padding(8.dp)
        ){
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(
                    onClick = { /*TODO*/ },
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
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("Search") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    }
                )
            }
            Column (
                modifier = Modifier
                    .padding(top = 8.dp)
                    .verticalScroll(rememberScrollState())
            ){
                homeCard(jenisPakan = "Herbivora")
                homeCard(jenisPakan = "Karnivora")
                homeCard(jenisPakan = "Omnivora")
                homeCard(jenisPakan = "Kanibal")
                homeCard(jenisPakan = "Herbivora")
                homeCard(jenisPakan = "Karnivora")
                homeCard(jenisPakan = "Omnivora")
                homeCard( jenisPakan = "Kanibal")
            }
        }
    }

}

@Composable
private fun homeCard(
    jenisPakan: String
){
    val warna:Color
    val logoPakan:Painter
    when (jenisPakan) {
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
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
                    Text("Nama Hewan")
                    Text("Pakan")
                    Text("Populasi")

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