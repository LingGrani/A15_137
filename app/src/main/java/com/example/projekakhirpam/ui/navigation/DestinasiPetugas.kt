package com.example.projekakhirpam.ui.navigation

object DestinasiPetugas : DestinasiNavigasi {
    override val route = "Petugas"
    override val titleRes = "Petugas"
}

object DestinasiPetugasDetail: DestinasiNavigasi {
    override val route: String = "detailPetugas"
    override val titleRes: String = "Detail Petugas"
    const val idArg = "id"
    val routeWithArgs = "$route/{$idArg}"
}

object DestinasiPetugasUpdate: DestinasiNavigasi {
    override val route: String = "updatePetugas"
    override val titleRes: String = "Update Petugas"
    const val idArg = "id"
    val routeWithArgs = "$route/{$idArg}"
}

object DestinasiPetugasInsert : DestinasiNavigasi {
    override val route = "Petugas Insert"
    override val titleRes = "Petugas Insert"
}
