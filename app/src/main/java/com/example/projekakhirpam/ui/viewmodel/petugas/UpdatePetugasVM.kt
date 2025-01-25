package com.example.projekakhirpam.ui.viewmodel.petugas

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhirpam.model.Petugas
import com.example.projekakhirpam.repo.KebunRepository
import com.example.projekakhirpam.ui.navigation.DestinasiPetugasDetail
import kotlinx.coroutines.launch

class UpdatePetugasVM(
    savedStateHandle: SavedStateHandle,
    private val repo: KebunRepository
) : ViewModel() {

    var uiState by mutableStateOf(UpdatePetugasUiState())
        private set

    fun updateDataState(updateUiEvent: UpdatePetugasUiEvent) {
        uiState = UpdatePetugasUiState(updatePetugasUiEvent = updateUiEvent)
    }

    private val _id: String = checkNotNull(savedStateHandle[DestinasiPetugasDetail.idArg])

    init {
        getDataById(_id)
    }

    fun getDataById(id: String) {
        viewModelScope.launch {
            try {
                val petugas = repo.getPetugasById(id.toInt())
                uiState = UpdatePetugasUiState(updatePetugasUiEvent = petugas.toUpdateUiEvent())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun updateData() {
        viewModelScope.launch {
            try {
                repo.updatePetugas(_id.toInt(), uiState.updatePetugasUiEvent.todata())
                Log.d("UpdateViewModel", "updateMhs: Success")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

data class UpdatePetugasUiState(
    val updatePetugasUiEvent: UpdatePetugasUiEvent = UpdatePetugasUiEvent()
)

data class UpdatePetugasUiEvent(
    val idPetugas: String = "",
    val namaPetugas: String = "",
    val jabatan: String = ""
)

fun UpdatePetugasUiEvent.todata(): Petugas = Petugas(
    idPetugas = idPetugas.toInt(),
    namaPetugas = namaPetugas,
    jabatan = jabatan
)

fun Petugas.toUpdateUiEvent(): UpdatePetugasUiEvent = UpdatePetugasUiEvent(
    idPetugas = idPetugas.toString(),
    namaPetugas = namaPetugas,
    jabatan = jabatan
)