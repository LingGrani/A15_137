package com.example.projekakhirpam.ui.viewmodel.petugas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhirpam.model.Petugas
import com.example.projekakhirpam.repo.KebunRepository
import kotlinx.coroutines.launch

class InsertPetugasVM(private val repository: KebunRepository) : ViewModel() {
    var uiState by mutableStateOf(InsertPetugasUiState())
        private set

    fun updateInsertMhsState(insertUiEvent: InsertPetugasUiEvent) {
        uiState = InsertPetugasUiState(insertPetugasUiEvent = insertUiEvent)
    }

    suspend fun insertHewan() {
        viewModelScope.launch {
            try {
                repository.insertPetugas(uiState.insertPetugasUiEvent.toData())
            } catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
}

data class InsertPetugasUiState(
    val insertPetugasUiEvent: InsertPetugasUiEvent = InsertPetugasUiEvent()
)

data class InsertPetugasUiEvent(
    val idPetugas: String = "",
    val namaPetugas: String = "",
    val jabatan: String = "",
)

fun InsertPetugasUiEvent.toData(): Petugas = Petugas(
    idPetugas = idPetugas.toInt(),
    namaPetugas = namaPetugas,
    jabatan = jabatan
)