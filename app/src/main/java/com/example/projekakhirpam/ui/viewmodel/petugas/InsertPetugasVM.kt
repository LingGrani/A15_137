package com.example.projekakhirpam.ui.viewmodel.petugas

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhirpam.model.Petugas
import com.example.projekakhirpam.repo.KebunRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class InsertPetugasVM(private val repository: KebunRepository) : ViewModel() {
    var uiEvent by mutableStateOf(InsertPetugasUiState())
        private set

    var uiState: FormStatePetugas by mutableStateOf(FormStatePetugas.Idle)
        private set

    fun updateInsertDataState(insertUiEvent: InsertPetugasUiEvent) {
        uiEvent = uiEvent.copy(insertPetugasUiEvent = insertUiEvent)
    }

    fun validateFields(): Boolean {
        val event = uiEvent.insertPetugasUiEvent
        val state = uiState as? FormStatePetugas.Success ?: return false
        val error = ErrorPetugasFormState(
            namaPetugas = when {
                event.namaPetugas.isBlank() -> "Nama Petugas tidak boleh kosong"
                state.petugasist.any { it.namaPetugas.equals(event.namaPetugas, ignoreCase = true) } -> "Nama Petugas sudah ada"
                else -> null
            },
            jabatan = if (event.jabatan.isBlank()) "Jabatan tidak boleh kosong" else null
        )
        uiEvent = uiEvent.copy(error = error)
        return error.isValid()
    }
    suspend fun insertPetugas() {
        if (validateFields()) {
            uiState = FormStatePetugas.Loading
            viewModelScope.launch {
                try {
                    repository.insertPetugas(uiEvent.insertPetugasUiEvent.toData())
                    uiState = FormStatePetugas.Success("Berhasil")
                } catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }
    }
    init {
        getData()
    }

    fun getData() {
        viewModelScope.launch {
            uiState = FormStatePetugas.Loading
            uiState = try {
                val list = repository.getPetugas()
                FormStatePetugas.Success("Berhasil",list)
            } catch (e: IOException) {
                FormStatePetugas.Error("Gagal")
            } catch (e: HttpException) {
                FormStatePetugas.Error("Gagal")
            }
        }
    }
}

data class InsertPetugasUiState(
    val insertPetugasUiEvent: InsertPetugasUiEvent = InsertPetugasUiEvent(),
    val error: ErrorPetugasFormState = ErrorPetugasFormState()
)

sealed class FormStatePetugas {
    object Idle : FormStatePetugas()
    object Loading : FormStatePetugas()
    data class Success(
        val message: String,
        val petugasist: List<Petugas> = emptyList()
    ) : FormStatePetugas()
    data class Error(val message: String) : FormStatePetugas()
}

data class InsertPetugasUiEvent(
    val idPetugas: String = "",
    val namaPetugas: String = "",
    val jabatan: String = "",
)

fun InsertPetugasUiEvent.toData(): Petugas = Petugas(
    idPetugas = idPetugas.toIntOrNull() ?: 0,
    namaPetugas = namaPetugas,
    jabatan = jabatan
)

data class ErrorPetugasFormState (
    val namaPetugas: String? = null,
    val jabatan: String? = null
) {
    fun isValid(): Boolean {
        return namaPetugas == null && jabatan == null
    }
}