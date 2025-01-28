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

    var uiEvent: UpdateKandangUiState by mutableStateOf(UpdateKandangUiState.Loading)
        private set

    var uiState: FormStateKandang by mutableStateOf(FormStateKandang.Idle)
        private set


    fun updateDataState(updateUiEvent: UpdateKandangUiEvent) {
        if (uiEvent is UpdateKandangUiState.Success) {
            val currentState = uiEvent as UpdateKandangUiState.Success
            uiEvent = currentState.copy(updateKandangUiEvent = updateUiEvent)
        }
    }

    fun validateFields(): Boolean {
        val currentUiEvent = uiEvent as? UpdateKandangUiState.Success ?: return false
        val error = ErrorKandangFormState(
            idHewan = if (currentUiEvent.updateKandangUiEvent.idHewan.isBlank()) "Id Hewan tidak boleh kosong" else null,
            kapasitas = when {
                currentUiEvent.updateKandangUiEvent.kapasitas.isBlank() -> "Kapasitas tidak boleh kosong"
                currentUiEvent.updateKandangUiEvent.kapasitas.toIntOrNull() == null -> "Kapasitas harus berupa angka"
                else -> null
            },
            lokasi = if (currentUiEvent.updateKandangUiEvent.lokasi.isBlank()) "Lokasi tidak boleh kosong" else null
        )
        uiEvent = currentUiEvent.copy(error = error)
        return error.isValid()
    }

    private val _id: String = checkNotNull(savedStateHandle[DestinasiKandangDetail.idArg])

    init {
        getDataById()
    }

    fun getDataById() {
        viewModelScope.launch {
            uiEvent = UpdateKandangUiState.Loading
            try {
                val kandang = repo.getKandangById(_id.toInt())
                uiEvent = UpdateKandangUiState.Success(
                    hewanList = repo.getHewan(),
                    updateKandangUiEvent = kandang.toUpdateUiEvent()
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun updateData() {
        if (validateFields()){
            uiState = FormStateKandang.Loading
            val updateEvent = (uiEvent as UpdateKandangUiState.Success).updateKandangUiEvent
            viewModelScope.launch {
                try {
                    Log.d("Hasil", updateEvent.toString())
                    repo.updateKandang(_id.toInt(), updateEvent.todata())
                    uiState = FormStateKandang.Success("Berhasil")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}

sealed class UpdateKandangUiState {
    object Loading : UpdateKandangUiState()
    data class Success(
        val hewanList: List<Hewan>,
        val updateKandangUiEvent: UpdateKandangUiEvent = UpdateKandangUiEvent(),
        val error: ErrorKandangFormState = ErrorKandangFormState()
    ) : UpdateKandangUiState()
    object Error : UpdateKandangUiState()
}

sealed class FormUpdateStateKandang {
    object Idle : FormUpdateStateKandang()
    object Loading : FormUpdateStateKandang()
    data class Success(
        val message: String,
    ) : FormUpdateStateKandang()
    data class Error(val message: String) : FormUpdateStateKandang()
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