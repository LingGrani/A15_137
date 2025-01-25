package com.example.projekakhirpam.model

import kotlinx.serialization.Serializable

@Serializable
data class Petugas(
    val idPetugas: Int,
    val namaPetugas: String,
    val jabatan: String,
){
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingFields = listOf(
            idPetugas.toString(),
            namaPetugas,
            jabatan
        )

        return matchingFields.any {
            it.contains(query, ignoreCase = true)
        }
    }
}