package com.example.projekakhirpam.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.projekakhirpam.R

@Composable
fun BottomNavigationBar(
    selectedTab: Int,
    navigateHewan: () -> Unit,
    navigateKandang: () -> Unit,
    navigateMonitoring: () -> Unit,
    navigatePetugas: () -> Unit,
) {
    BottomAppBar(
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.paw),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Hewan") },
            selected = selectedTab == 0,
            onClick = {
                navigateHewan()
            }
        )

        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.cage),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Kandang") },
            selected = selectedTab == 1,
            onClick = {
                navigateKandang()
            }
        )

        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.monitoring),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Monitoring") },
            selected = selectedTab == 2,
            onClick = {
                navigateMonitoring()
            }
        )

        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.petugas),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Petugas") },
            selected = selectedTab == 3,
            onClick = {
                navigatePetugas()
            }
        )
    }
}
