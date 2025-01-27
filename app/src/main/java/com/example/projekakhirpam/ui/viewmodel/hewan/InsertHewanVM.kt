package com.example.projekakhirpam.ui.viewmodel.hewan

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhirpam.model.Hewan
import com.example.projekakhirpam.repo.KebunRepository
import kotlinx.coroutines.launch

class InsertHewanVM(private val repository: KebunRepository) : ViewModel() {
    var uiState by mutableStateOf(InsertHewanUiState())
        private set

    fun updateInsertMhsState(insertUiEvent: InsertHewanUiEvent) {
        uiState = InsertHewanUiState(insertHewanUiEvent = insertUiEvent)
    }

    suspend fun insertHewan() {
        viewModelScope.launch {
            try {
                repository.insertHewan(uiState.insertHewanUiEvent.toData())
                Log.d("Hasil", "Hewan berhasil ditambahkan")
                Log.d("Hasil", uiState.insertHewanUiEvent.toString())
            } catch (e:Exception){
                Log.d("Hasil", "Gagal")
                e.printStackTrace()
            }
        }
    }
}

data class InsertHewanUiState(
    val insertHewanUiEvent: InsertHewanUiEvent = InsertHewanUiEvent()
)

data class InsertHewanUiEvent(
    val idHewan: String = "0",
    val namaHewan: String = "",
    val tipePakan: String = "",
    val populasi: String = "",
    val zonaWilayah: String = "",
)

fun InsertHewanUiEvent.toData(): Hewan = Hewan(
    idHewan = idHewan.toInt(),
    namaHewan = namaHewan,
    tipePakan = tipePakan,
    populasi = populasi.toInt(),
    zonaWilayah = zonaWilayah
)