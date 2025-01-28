package com.example.projekakhirpam.ui.viewmodel.kandang

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhirpam.model.Hewan
import com.example.projekakhirpam.model.Kandang
import com.example.projekakhirpam.model.Petugas
import com.example.projekakhirpam.repo.KebunRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class InsertKandangVM(private val repository: KebunRepository) : ViewModel() {
    var uiEvent: InsertKandangUiState by mutableStateOf(InsertKandangUiState.Loading)
        private set

    var uiState: FormStateKandang by mutableStateOf(FormStateKandang.Idle)
        private set

    init {
        getData()
    }

    fun validateFields(): Boolean {
        val currentUiEvent = uiEvent as? InsertKandangUiState.Success ?: return false
        val error = ErrorKandangFormState(
            idHewan = if (currentUiEvent.insertKandangUiEvent.idHewan.isBlank()) "Id Hewan tidak boleh kosong" else null,
            kapasitas = when {
                currentUiEvent.insertKandangUiEvent.kapasitas.isBlank() -> "Kapasitas tidak boleh kosong"
                currentUiEvent.insertKandangUiEvent.kapasitas.toIntOrNull() == null -> "Kapasitas harus berupa angka"
                else -> null
            },
            lokasi = if (currentUiEvent.insertKandangUiEvent.lokasi.isBlank()) "Lokasi tidak boleh kosong" else null
        )
        uiEvent = currentUiEvent.copy(error = error)
        return error.isValid()
    }


    fun updateInsertDataState(insertUiEvent: InsertKandangUiEvent) {
        if (uiEvent is InsertKandangUiState.Success) {
            val currentState = uiEvent as InsertKandangUiState.Success
            uiEvent = currentState.copy(insertKandangUiEvent = insertUiEvent)
        }
    }

    fun getData() {
        viewModelScope.launch {
            uiEvent = InsertKandangUiState.Loading
            uiEvent = try {
                val hewanList = repository.getHewan()
                InsertKandangUiState.Success(hewanList = hewanList)
            } catch (e: IOException) {
                InsertKandangUiState.Error
            } catch (e: HttpException) {
                InsertKandangUiState.Error
            }
        }
    }

    fun insertHewan() {
        if (validateFields()){
            uiState = FormStateKandang.Loading
            if (uiEvent is InsertKandangUiState.Success) {
                val currentState = uiEvent as InsertKandangUiState.Success
                viewModelScope.launch {
                    try {
                        repository.insertKandang(currentState.insertKandangUiEvent.toData())
                        uiState = FormStateKandang.Success("Berhasil")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}

sealed class InsertKandangUiState {
    object Loading : InsertKandangUiState()
    data class Success(
        val hewanList: List<Hewan>,
        val insertKandangUiEvent: InsertKandangUiEvent = InsertKandangUiEvent(),
        val error: ErrorKandangFormState = ErrorKandangFormState()
    ) : InsertKandangUiState()
    object Error : InsertKandangUiState()
}

sealed class FormStateKandang {
    object Idle : FormStateKandang()
    object Loading : FormStateKandang()
    data class Success(val message: String) : FormStateKandang()
    data class Error(val message: String) : FormStateKandang()
}

data class InsertKandangUiEvent(
    val idKandang: String = "",
    val idHewan: String = "",
    val kapasitas: String = "",
    val lokasi: String = "",
)

fun InsertKandangUiEvent.toData(): Kandang = Kandang(
    idKandang = idKandang.toIntOrNull() ?: 0,
    idHewan = idHewan.toIntOrNull() ?: 0,
    kapasitas = kapasitas.toIntOrNull() ?: 0,
    lokasi = lokasi
)

data class ErrorKandangFormState(
    val idHewan: String? = null,
    val kapasitas: String? = null,
    val lokasi: String? = null
) {
    fun isValid(): Boolean {
        return idHewan == null && kapasitas == null && lokasi == null
    }
}
