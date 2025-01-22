package com.example.projekakhirpam.model

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Monitoring(
    val idMonitoring: Int,
    val idPetugas: Int,
    val idKandang: Int,
    val tanggalMonitoring: LocalDateTime,
    val hewanSakit: Int,
    val hewanSehat: Int,
    val kondisi: String
)