package com.example.projekakhirpam.ui.viewmodel.petugas

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhirpam.model.Hewan
import com.example.projekakhirpam.model.Petugas
import com.example.projekakhirpam.repo.KebunRepository
import com.example.projekakhirpam.ui.navigation.DestinasiPetugasDetail
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class UpdatePetugasVM(
    savedStateHandle: SavedStateHandle,
    private val repo: KebunRepository
) : ViewModel() {

    var uiEvent by mutableStateOf(UpdatePetugasUiState())
        private set

    var uiState: FormUpdateStatePetugas by mutableStateOf(FormUpdateStatePetugas.Idle)
        private set

    fun updateDataState(updateUiEvent: UpdatePetugasUiEvent) {
        uiEvent = uiEvent.copy(updatePetugasUiEvent = updateUiEvent)
    }

    fun validateFields(): Boolean {
        val event = uiEvent.updatePetugasUiEvent
        val state = uiState as? FormUpdateStatePetugas.Success ?: return false
        val error = ErrorPetugasFormState(
            namaPetugas = when {
                event.namaPetugas.isBlank() -> "Nama Petugas tidak boleh kosong"
                event.namaPetugas != event.originalNama && state.petugasList.any { it.namaPetugas.equals(event.namaPetugas, ignoreCase = true) } -> "Nama Petugas sudah ada"
                else -> null
            },
            jabatan = if (event.jabatan.isBlank()) "Jabatan tidak boleh kosong" else null
        )
        uiEvent = uiEvent.copy(error = error)
        return error.isValid()
    }

    private val _id: String = checkNotNull(savedStateHandle[DestinasiPetugasDetail.idArg])

    init {
        getDataById()
        getData()
    }

    fun getDataById() {
        viewModelScope.launch {
            try {
                val petugas = repo.getPetugasById(_id.toInt())
                uiEvent = uiEvent.copy(
                    updatePetugasUiEvent = petugas.toUpdateUiEvent().copy(
                        originalNama = petugas.namaPetugas
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateData() {
        if (validateFields()){
            uiState = FormUpdateStatePetugas.Loading
            viewModelScope.launch {
                try {
                    repo.updatePetugas(_id.toInt(), uiEvent.updatePetugasUiEvent.todata())
                    uiState = FormUpdateStatePetugas.Success("Berhasil Mengupdate Petugas")
                } catch (e: Exception) {
                    uiState = FormUpdateStatePetugas.Error("Gagal Mengupdate Petugas")
                    e.printStackTrace()
                }
            }
        }
    }

    fun getData() {
        viewModelScope.launch {
            uiState = FormUpdateStatePetugas.Loading
            uiState = try {
                val list = repo.getPetugas()
                FormUpdateStatePetugas.Success("Berhasil",list)
            } catch (e: IOException) {
                FormUpdateStatePetugas.Error("Gagal")
            } catch (e: HttpException) {
                FormUpdateStatePetugas.Error("Gagal")
            }
        }
    }
}

data class UpdatePetugasUiState(
    val updatePetugasUiEvent: UpdatePetugasUiEvent = UpdatePetugasUiEvent(),
    val error: ErrorPetugasFormState = ErrorPetugasFormState()
)

sealed class FormUpdateStatePetugas {
    object Idle : FormUpdateStatePetugas()
    object Loading : FormUpdateStatePetugas()
    data class Success(
        val message: String,
        val petugasList: List<Petugas> = emptyList()
    ) : FormUpdateStatePetugas()
    data class Error(val message: String) : FormUpdateStatePetugas()
}

data class UpdatePetugasUiEvent(
    val idPetugas: String = "",
    val namaPetugas: String = "",
    val originalNama: String = "",
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