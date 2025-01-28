package com.example.projekakhirpam.ui.viewmodel.kandang

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhirpam.model.Hewan
import com.example.projekakhirpam.model.Kandang
import com.example.projekakhirpam.repo.KebunRepository
import com.example.projekakhirpam.ui.navigation.DestinasiKandangDetail
import kotlinx.coroutines.launch

class UpdateKandangVM(
    savedStateHandle: SavedStateHandle,
    private val repo: KebunRepository
) : ViewModel() {

    var uiState: UpdateKandangUiState by mutableStateOf(UpdateKandangUiState.Loading)
        private set

    fun updateDataState(updateUiEvent: UpdateKandangUiEvent) {
        if (uiState is UpdateKandangUiState.Success) {
            val currentState = uiState as UpdateKandangUiState.Success
            uiState = currentState.copy(updateKandangUiEvent = updateUiEvent)
        }
    }

    private val _id: String = checkNotNull(savedStateHandle[DestinasiKandangDetail.idArg])

    init {
        getDataById()
    }

    fun getDataById() {
        viewModelScope.launch {
            uiState = UpdateKandangUiState.Loading
            try {
                val kandang = repo.getKandangById(_id.toInt())
                uiState = UpdateKandangUiState.Success(
                    hewanList = repo.getHewan(),
                    updateKandangUiEvent = kandang.toUpdateUiEvent()
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun updateData() {
        val updateEvent = (uiState as UpdateKandangUiState.Success).updateKandangUiEvent
        viewModelScope.launch {
            try {
                Log.d("Hasil", updateEvent.toString())
                repo.updateKandang(_id.toInt(), updateEvent.todata())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

sealed class UpdateKandangUiState {
    object Loading : UpdateKandangUiState()
    data class Success(
        val hewanList: List<Hewan>,
        val updateKandangUiEvent: UpdateKandangUiEvent = UpdateKandangUiEvent()
    ) : UpdateKandangUiState()
    object Error : UpdateKandangUiState()
}


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