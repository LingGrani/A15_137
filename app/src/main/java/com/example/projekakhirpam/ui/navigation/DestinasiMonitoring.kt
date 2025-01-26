package com.example.projekakhirpam.ui.navigation

object DestinasiMonitoring : DestinasiNavigasi {
    override val route = "Monitoring"
    override val titleRes = "Monitoring"
}

object DestinasiMonitoringDetail: DestinasiNavigasi {
    override val route: String = "detailMonitoring"
    override val titleRes: String = "Detail Monitoring"
    const val idArg = "id"
    val routeWithArgs = "$route/{$idArg}"
}

object DestinasiMonitoringUpdate: DestinasiNavigasi {
    override val route: String = "updateMonitoring"
    override val titleRes: String = "Update Monitoring"
    const val idArg = "id"
    val routeWithArgs = "$route/{$idArg}"
}

object DestinasiMonitoringInsert : DestinasiNavigasi {
    override val route = "Monitoring Insert"
    override val titleRes = "Monitoring Insert"
}