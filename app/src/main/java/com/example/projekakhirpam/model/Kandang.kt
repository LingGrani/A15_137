package com.example.projekakhirpam.model

import kotlinx.serialization.Serializable

@Serializable
data class Kandang(
    val idKandang: Int,
    val idHewan: Int,
    val kapasitas: Int,
    val lokasi: String
)