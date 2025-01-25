package com.example.projekakhirpam.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.projekakhirpam.ui.view.MasterView
import com.example.projekakhirpam.ui.view.hewan.HewanDetailView
import com.example.projekakhirpam.ui.view.hewan.HewanHomeView
import com.example.projekakhirpam.ui.view.hewan.HewanInsertView
import com.example.projekakhirpam.ui.view.hewan.HewanUpdateView
import com.example.projekakhirpam.ui.view.kandang.KandangHomeView
import com.example.projekakhirpam.ui.view.monitoring.MonitoringHomeView
import com.example.projekakhirpam.ui.view.petugas.PetugasHomeView




@Composable
fun PengelolaHalaman(
    navController: NavHostController = rememberNavController(),
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = DestinasiHewan.route,
        modifier = Modifier
    ) {
        composable(DestinasiMaster.route) {
            MasterView(
                navigateHewan = { navController.navigate(DestinasiHewan.route) },
                navigateKandang = { navController.navigate(DestinasiKandang.route) },
                navigateMonitoring = { navController.navigate(DestinasiMonitoring.route) },
                navigatePetugas = { navController.navigate(DestinasiPetugas.route) },
            )
        }
        // HEWAN HEWAN HEWAN HEWAN HEWAN HEWAN HEWAN HEWAN HEWAN HEWAN HEWAN HEWAN HEWAN HEWAN HEWAN //
        composable(DestinasiHewan.route){
            HewanHomeView(
                onDetailClick = { id ->
                    navController.navigate("${DestinasiHewanDetail.route}/$id")
                },
                onAddClick = {
                    navController.navigate(DestinasiHewanInsert.route)
                }
            )
        }

        composable(
            DestinasiHewanDetail.routeWithArgs,
            arguments = listOf(
                navArgument(DestinasiHewanDetail.idArg) {
                    type = NavType.StringType
                }
            )
        ){
            val id = it.arguments?.getString(DestinasiHewanDetail.idArg)
            id?.let { id ->
                HewanDetailView(
                    onBack = {
                        navController.navigate(DestinasiHewan.route)
                    },
                    onEditClick = {
                        navController.navigate("${DestinasiHewanUpdate.route}/$id")
                    }
                )
            }
        }

        composable(DestinasiHewanInsert.route){
            HewanInsertView(
                onBack = {
                    navController.navigate(DestinasiHewan.route)
                },
            )
        }

        composable(
            DestinasiHewanUpdate.routeWithArgs,
            arguments = listOf(
                navArgument(DestinasiHewanUpdate.idArg){
                    type = NavType.StringType
                }
            )
        ){
            val id = it.arguments?.getString(DestinasiHewanUpdate.idArg)
            id?.let { id ->
                HewanUpdateView(
                    onBack = {
                        navController.navigate(DestinasiHewan.route)
                    }
                )
            }
        }


        // KANDANG KANDANG KANDANG KANDANG KANDANG KANDANG KANDANG KANDANG KANDANG KANDANG KANDANG KANDANG KANDANG //

        composable(DestinasiKandang.route){
            KandangHomeView()
        }

        composable(DestinasiMonitoring.route){
            MonitoringHomeView(
            )
        }
        composable(DestinasiPetugas.route){
            PetugasHomeView()
        }
    }
}