package com.example.projekakhirpam.ui.viewmodel.kandang

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhirpam.model.Kandang
import com.example.projekakhirpam.repo.KebunRepository
import com.example.projekakhirpam.ui.navigation.DestinasiKandangDetail
import kotlinx.coroutines.launch

class UpdateKandangVM(
    savedStateHandle: SavedStateHandle,
    private val repo: KebunRepository
) : ViewModel() {

    var uiState by mutableStateOf(UpdateKandangUiState())
        private set

    fun updateDataState(updateUiEvent: UpdateKandangUiEvent) {
        uiState = UpdateKandangUiState(updateKandangUiEvent = updateUiEvent)
    }

    private val _id: String = checkNotNull(savedStateHandle[DestinasiKandangDetail.idArg])

    init {
        getDataById(_id)
    }

    fun getDataById(id: String) {
        viewModelScope.launch {
            try {
                val kandang = repo.getKandangById(id.toInt())
                uiState = UpdateKandangUiState(updateKandangUiEvent = kandang.toUpdateUiEvent())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun updateData() {
        viewModelScope.launch {
            try {
                Log.d("Hasil", uiState.updateKandangUiEvent.toString())
                repo.updateKandang(_id.toInt(), uiState.updateKandangUiEvent.todata())
                Log.d("Hasil", "updateMhs: Success")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

data class UpdateKandangUiState(
    val updateKandangUiEvent: UpdateKandangUiEvent = UpdateKandangUiEvent()
)

data class UpdateKandangUiEvent(
    val idKandang: String = "",
    val idHewan: String = "",
    val kapasitas: String = "",
    val lokasi: String = ""
)

fun UpdateKandangUiEvent.todata(): Kandang = Kandang(
    idKandang = idKandang.toInt(),
    idHewan = idHewan.toInt(),
    kapasitas = kapasitas.toInt(),
    lokasi = lokasi
)

fun Kandang.toUpdateUiEvent(): UpdateKandangUiEvent = UpdateKandangUiEvent(
    idKandang = idKandang.toString(),
    idHewan = idHewan.toString(),
    kapasitas = kapasitas.toString(),
    lokasi = lokasi
)