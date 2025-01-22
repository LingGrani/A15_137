package com.example.projekakhirpam.model

import kotlinx.serialization.Serializable

@Serializable
data class Petugas(
    val idPetugas: Int,
    val namaPetugas: String,
    val jabatan: String,
)