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
import com.example.projekakhirpam.ui.view.kandang.KandangDetailView
import com.example.projekakhirpam.ui.view.kandang.KandangHomeView
import com.example.projekakhirpam.ui.view.kandang.KandangInsertView
import com.example.projekakhirpam.ui.view.kandang.KandangUpdateView
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
        startDestination = DestinasiKandang.route,
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
                },
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange,
                onBack = {
                    navController.navigate(DestinasiMaster.route)
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
                    },
                    isDarkTheme = isDarkTheme,
                    onThemeChange = onThemeChange
                )
            }
        }

        composable(DestinasiHewanInsert.route){
            HewanInsertView(
                onBack = {
                    navController.navigate(DestinasiHewan.route)
                },
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange
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
                    },
                    isDarkTheme = isDarkTheme,
                    onThemeChange = onThemeChange
                )
            }
        }


        // KANDANG KANDANG KANDANG KANDANG KANDANG KANDANG KANDANG KANDANG KANDANG KANDANG KANDANG KANDANG KANDANG //

        composable(DestinasiKandang.route){
            KandangHomeView(
                onDetailClick = { id ->
                    navController.navigate("${DestinasiKandangDetail.route}/$id")
                },
                onAddClick = {
                    navController.navigate(DestinasiKandangInsert.route)
                },
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange,
                onBack = {
                    navController.navigate(DestinasiMaster.route)
                }
            )
        }

        composable(
            DestinasiKandangDetail.routeWithArgs,
            arguments = listOf(
                navArgument(DestinasiKandangDetail.idArg){
                    type = NavType.StringType
                }
            )
        ){
            val id = it.arguments?.getString(DestinasiKandangDetail.idArg)
            id?.let { id ->
                KandangDetailView(
                    onBack = {
                        navController.navigate(DestinasiKandang.route)
                    },
                    isDarkTheme = isDarkTheme,
                    onThemeChange = onThemeChange,
                    onEditClick = {
                        navController.navigate("${DestinasiKandangUpdate.route}/$id")
                    }
                )
            }
        }
        composable(DestinasiKandangInsert.route){
            KandangInsertView(
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange,
                onBack = {
                    navController.navigate(DestinasiKandang.route)
                }
            )
        }
        composable(
            DestinasiKandangUpdate.routeWithArgs,
            arguments = listOf(
                navArgument(DestinasiKandangUpdate.idArg){
                    type = NavType.StringType
                }
            )
        ){
            val id = it.arguments?.getString(DestinasiKandangUpdate.idArg)
            id?.let { id ->
                KandangUpdateView(
                    onBack = {
                        navController.navigate(DestinasiKandang.route)
                    },
                    isDarkTheme = isDarkTheme,
                    onThemeChange = onThemeChange,
                )
            }
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