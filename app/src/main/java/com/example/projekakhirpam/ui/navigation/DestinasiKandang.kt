package com.example.projekakhirpam.ui.navigation

object DestinasiKandang : DestinasiNavigasi {
    override val route = "Kandang"
    override val titleRes = "Kandang"
}

object DestinasiKandangDetail: DestinasiNavigasi {
    override val route: String = "detailKandang"
    override val titleRes: String = "Detail Kandang"
    const val idArg = "id"
    val routeWithArgs = "$route/{$idArg}"
}

object DestinasiKandangUpdate: DestinasiNavigasi {
    override val route: String = "updateKandang"
    override val titleRes: String = "Update Kandang"
    const val idArg = "id"
    val routeWithArgs = "$route/{$idArg}"
}

object DestinasiKandangInsert : DestinasiNavigasi {
    override val route = "Kandang Insert"
    override val titleRes = "Kandang Insert"
}