package com.example.projekakhirpam.ui.viewmodel.hewan

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhirpam.model.Hewan
import com.example.projekakhirpam.repo.KebunRepository
import com.example.projekakhirpam.ui.navigation.DestinasiHewanDetail
import kotlinx.coroutines.launch

class UpdateHewanVM(
    savedStateHandle: SavedStateHandle,
    private val repo: KebunRepository
) : ViewModel() {

    var uiState by mutableStateOf(UpdateHewanUiState())
        private set

    fun updateUpdateMhsState(updateUiEvent: UpdateHewanUiEvent) {
        uiState = UpdateHewanUiState(updateHewanUiEvent = updateUiEvent)
    }

    private val _id: String = checkNotNull(savedStateHandle[DestinasiHewanDetail.idArg])

    init {
        getDataById(_id)
    }

    fun getDataById(id: String) {
        viewModelScope.launch {
            try {
                val hewan = repo.getHewanById(id.toInt())
                uiState = UpdateHewanUiState(updateHewanUiEvent = hewan.toUpdateUiEvent())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun updateData() {
        viewModelScope.launch {
            try {
                repo.updateHewan(_id.toInt(), uiState.updateHewanUiEvent.todata())
                Log.d("UpdateViewModel", "updateMhs: Success")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

data class UpdateHewanUiState(
    val updateHewanUiEvent: UpdateHewanUiEvent = UpdateHewanUiEvent()
)

data class UpdateHewanUiEvent(
    val idHewan: String = "",
    val namaHewan: String = "",
    val tipePakan: String = "",
    val populasi: String = "",
    val zonaWilayah: String = ""
)

fun UpdateHewanUiEvent.todata(): Hewan = Hewan(
    idHewan = idHewan.toInt(),
    namaHewan = namaHewan,
    tipePakan = tipePakan,
    populasi = populasi.toInt(),
    zonaWilayah = zonaWilayah
)

fun Hewan.toUpdateUiEvent(): UpdateHewanUiEvent = UpdateHewanUiEvent(
    idHewan = idHewan.toString(),
    namaHewan = namaHewan,
    tipePakan = tipePakan,
    populasi = populasi.toString(),
    zonaWilayah = zonaWilayah
)