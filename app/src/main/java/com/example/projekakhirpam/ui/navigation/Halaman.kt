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
import com.example.projekakhirpam.ui.view.monitoring.MonitoringDetailView
import com.example.projekakhirpam.ui.view.monitoring.MonitoringHomeView
import com.example.projekakhirpam.ui.view.monitoring.MonitoringInsertView
import com.example.projekakhirpam.ui.view.monitoring.MonitoringUpdateView
import com.example.projekakhirpam.ui.view.petugas.PetugasDetailView
import com.example.projekakhirpam.ui.view.petugas.PetugasHomeView
import com.example.projekakhirpam.ui.view.petugas.PetugasInsertView
import com.example.projekakhirpam.ui.view.petugas.PetugasUpdateView


@Composable
fun PengelolaHalaman(
    navController: NavHostController = rememberNavController(),
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = DestinasiMaster.route,
        modifier = Modifier
    ) {
        composable(DestinasiMaster.route) {
            MasterView(
                navigateHewan = { navController.navigate(DestinasiHewan.route) },
                navigateKandang = { navController.navigate(DestinasiKandang.route) },
                navigateMonitoring = { navController.navigate(DestinasiMonitoring.route) },
                navigatePetugas = { navController.navigate(DestinasiPetugas.route) },
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange,
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
                },
                navigateHewan = { navController.navigate(DestinasiHewan.route) },
                navigateKandang = { navController.navigate(DestinasiKandang.route) },
                navigateMonitoring = { navController.navigate(DestinasiMonitoring.route) },
                navigatePetugas = { navController.navigate(DestinasiPetugas.route) }
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
                },
                navigateHewan = { navController.navigate(DestinasiHewan.route) },
                navigateKandang = { navController.navigate(DestinasiKandang.route) },
                navigateMonitoring = { navController.navigate(DestinasiMonitoring.route) },
                navigatePetugas = { navController.navigate(DestinasiPetugas.route) }
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
                    },
                    onHewanClick = { it->
                        navController.navigate("${DestinasiHewanDetail.route}/$it")
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

        // MONITORING MONITORING MONITORING MONITORING MONITORING MONITORING MONITORING MONITORING MONITORING //

        composable(DestinasiMonitoring.route){
            MonitoringHomeView(
                onDetailClick = { id ->
                    navController.navigate("${DestinasiMonitoringDetail.route}/$id")
                },
                onAddClick = {
                    navController.navigate(DestinasiMonitoringInsert.route)
                },
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange,
                onBack = {
                    navController.navigate(DestinasiMaster.route)
                },
                navigateHewan = { navController.navigate(DestinasiHewan.route) },
                navigateKandang = { navController.navigate(DestinasiKandang.route) },
                navigateMonitoring = { navController.navigate(DestinasiMonitoring.route) },
                navigatePetugas = { navController.navigate(DestinasiPetugas.route) }
            )
        }
        composable(
            DestinasiMonitoringDetail.routeWithArgs,
            arguments = listOf(
                navArgument(DestinasiMonitoringDetail.idArg){
                    type = NavType.StringType
                }
            )
        ){
            val id = it.arguments?.getString(DestinasiMonitoringDetail.idArg)
            id?.let { id ->
                MonitoringDetailView(
                    onBack = {
                        navController.navigate(DestinasiMonitoring.route)
                    },
                    isDarkTheme = isDarkTheme,
                    onThemeChange = onThemeChange,
                    onEditClick = {
                        navController.navigate("${DestinasiMonitoringUpdate.route}/$id")
                    }
                )
            }
        }

        composable(DestinasiMonitoringInsert.route){
            MonitoringInsertView(
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange,
                onBack = {
                    navController.navigate(DestinasiMonitoring.route)
                }
            )
        }

        composable(
            DestinasiMonitoringUpdate.routeWithArgs,
            arguments = listOf(
                navArgument(DestinasiMonitoringUpdate.idArg){
                    type = NavType.StringType
                }
            )
        ){
            val id = it.arguments?.getString(DestinasiMonitoringUpdate.idArg)
            id?.let { id ->
                MonitoringUpdateView(
                    onBack = {
                        navController.navigate(DestinasiMonitoring.route)
                    },
                    isDarkTheme = isDarkTheme,
                    onThemeChange = onThemeChange,
                )
            }
        }

        // PETUGAS PETUGAS PETUGAS PETUGAS PETUGAS PETUGAS PETUGAS PETUGAS PETUGAS PETUGAS PETUGAS PETUGAS PETUGAS //
        composable(DestinasiPetugas.route){
            PetugasHomeView(
                onDetailClick = { id ->
                    navController.navigate("${DestinasiPetugasDetail.route}/$id")
                },
                onAddClick = {
                    navController.navigate(DestinasiPetugasInsert.route)
                },
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange,
                onBack = {
                    navController.navigate(DestinasiMaster.route)
                },
                navigateHewan = { navController.navigate(DestinasiHewan.route) },
                navigateKandang = { navController.navigate(DestinasiKandang.route) },
                navigateMonitoring = { navController.navigate(DestinasiMonitoring.route) },
                navigatePetugas = { navController.navigate(DestinasiPetugas.route) }
            )
        }
        composable(
            DestinasiPetugasDetail.routeWithArgs,
            arguments = listOf(
                navArgument(DestinasiPetugasDetail.idArg){
                    type = NavType.StringType
                }
            )
        ){
            val id = it.arguments?.getString(DestinasiPetugasDetail.idArg)
            id?.let { id ->
                PetugasDetailView(
                    onBack = {
                        navController.navigate(DestinasiPetugas.route)
                    },
                    isDarkTheme = isDarkTheme,
                    onThemeChange = onThemeChange,
                    onEditClick = {
                        navController.navigate("${DestinasiPetugasUpdate.route}/$id")
                    }
                )
            }
        }
        composable(
            DestinasiPetugasUpdate.routeWithArgs,
            arguments = listOf(
                navArgument(DestinasiPetugasUpdate.idArg){
                    type = NavType.StringType
                }
            )
        ){
            val id = it.arguments?.getString(DestinasiPetugasUpdate.idArg)
            id?.let { id ->
                PetugasUpdateView(
                    onBack = {
                        navController.navigate(DestinasiPetugas.route)
                    },
                    isDarkTheme = isDarkTheme,
                    onThemeChange = onThemeChange,
                )
            }
        }
        composable(DestinasiPetugasInsert.route){
            PetugasInsertView(
                onBack = {
                    navController.navigate(DestinasiPetugas.route)
                },
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange,
            )
        }
    }
}