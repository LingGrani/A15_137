package com.example.projekakhirpam.ui.navigation

object DestinasiHewan : DestinasiNavigasi {
    override val route = "Hewan"
    override val titleRes = "Hewan"
}

object DestinasiHewanDetail: DestinasiNavigasi {
    override val route: String = "detailHewan"
    override val titleRes: String = "Detail Hewan"
    const val idArg = "id"
    val routeWithArgs = "$route/{$idArg}"
}

object DestinasiHewanUpdate: DestinasiNavigasi {
    override val route: String = "updateHewan"
    override val titleRes: String = "Update Hewan"
    const val idArg = "id"
    val routeWithArgs = "$route/{$idArg}"
}

object DestinasiHewanInsert : DestinasiNavigasi {
    override val route = "Hewan Insert"
    override val titleRes = "Hewan Insert"
}