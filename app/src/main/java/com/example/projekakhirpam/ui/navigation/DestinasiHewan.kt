package com.example.projekakhirpam.ui.navigation

interface DestinasiNavigasi {
    val route: String
    val titleRes: String
}
object DestinasiMaster : DestinasiNavigasi {
    override val route = "Master"
    override val titleRes = "Master"
}

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


object DestinasiMonitoring : DestinasiNavigasi {
    override val route = "Monitoring"
    override val titleRes = "Monitoring"
}
object DestinasiPetugas : DestinasiNavigasi {
    override val route = "Petugas"
    override val titleRes = "Petugas"
}
