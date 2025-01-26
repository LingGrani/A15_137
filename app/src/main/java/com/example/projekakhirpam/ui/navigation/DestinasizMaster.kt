package com.example.projekakhirpam.ui.navigation

interface DestinasiNavigasi {
    val route: String
    val titleRes: String
}
object DestinasiMaster : DestinasiNavigasi {
    override val route = "Master"
    override val titleRes = "Master"
}