package com.example.projekakhirpam.model

import kotlinx.serialization.Serializable

@Serializable
data class Hewan(
    val idHewan: Int,
    val namaHewan: String,
    val tipePakan: String,
    val populasi: Int,
    val zonaWilayah: String
)