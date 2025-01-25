package com.example.projekakhirpam.ui.viewmodel.kandang

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhirpam.model.Kandang
import com.example.projekakhirpam.repo.KebunRepository
import kotlinx.coroutines.launch

class InsertKandangVM(private val repository: KebunRepository) : ViewModel() {
    var uiState by mutableStateOf(InsertKandangUiState())
        private set

    fun updateInsertDataState(insertUiEvent: InsertKandangUiEvent) {
        uiState = InsertKandangUiState(insertKandangUiEvent = insertUiEvent)
    }

    suspend fun insertHewan() {
        viewModelScope.launch {
            try {
                repository.insertKandang(uiState.insertKandangUiEvent.toData())
                Log.d("Hasil", "Data inserted successfully")
            } catch (e:Exception){
                e.printStackTrace()
                Log.d("Hasil", "Data failed to insert")
            }
        }
    }
}

data class InsertKandangUiState(
    val insertKandangUiEvent: InsertKandangUiEvent = InsertKandangUiEvent()
)

data class InsertKandangUiEvent(
    val idKandang: String = "",
    val idHewan: String = "",
    val kapasitas: String = "",
    val lokasi: String = "",
)

fun InsertKandangUiEvent.toData(): Kandang = Kandang(
    idKandang = idKandang.toInt(),
    idHewan = idHewan.toInt(),
    kapasitas = kapasitas.toInt(),
    lokasi = lokasi
)