package com.example.projekakhirpam.model

import kotlinx.serialization.Serializable

@Serializable
data class Hewan(
    val idHewan: Int,
    val namaHewan: String,
    val tipePakan: String,
    val populasi: Int,
    val zonaWilayah: String
){
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingFields = listOf(
            idHewan.toString(),
            namaHewan,
            tipePakan,
            zonaWilayah,
            populasi.toString()
        )

        return matchingFields.any {
            it.contains(query, ignoreCase = true)
        }
    }
}