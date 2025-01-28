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
import retrofit2.HttpException
import java.io.IOException

class UpdateHewanVM(
    savedStateHandle: SavedStateHandle,
    private val repo: KebunRepository
) : ViewModel() {

    var uiEvent by mutableStateOf(UpdateHewanUiState())
        private set
    var uiState: FormUpdateStateHewan by mutableStateOf(FormUpdateStateHewan.Idle)
        private set

    fun updateInsertMhsState(updateUiState: UpdateHewanUiEvent) {
        uiEvent = uiEvent.copy(updateHewanUiState = updateUiState)
    }

    fun validateFields(): Boolean {
        val event = uiEvent.updateHewanUiState
        val state = uiState as? FormUpdateStateHewan.Success ?: return false
        val error = ErrorHewanFormState(
            namaHewan = when {
                event.namaHewan.isBlank() -> "Nama Hewan tidak boleh kosong"
                event.namaHewan != event.originalNamaHewan && state.hewanList.any { it.namaHewan.equals(event.namaHewan, ignoreCase = true) } ->
                    "Nama Hewan sudah ada"
                else -> null
            },
            tipePakan = if (event.tipePakan.isBlank()) "Tipe Pakan tidak boleh kosong" else null,
            populasi = if (event.populasi.isBlank()) "Populasi tidak boleh kosong" else null,
            zonaWilayah = if (event.zonaWilayah.isBlank()) "Zona Wilayah tidak boleh kosong" else null
        )
        uiEvent = uiEvent.copy(error = error)
        return error.isValid()
    }

    private val _id: String = checkNotNull(savedStateHandle[DestinasiHewanDetail.idArg])

    init {
        getDataById()
        getHewan()
    }

    fun getDataById() {
        viewModelScope.launch {
            try {
                val hewan = repo.getHewanById(_id.toInt())
                uiEvent = uiEvent.copy(
                    updateHewanUiState = hewan.toUpdateUiEvent().copy(
                        originalNamaHewan = hewan.namaHewan
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateData() {
        if (validateFields()){
            uiState = FormUpdateStateHewan.Loading
            viewModelScope.launch {
                try {
                    repo.updateHewan(_id.toInt(), uiEvent.updateHewanUiState.todata())
                    uiState = FormUpdateStateHewan.Success("Berhasil Mengupdate Hewan")
                } catch (e: Exception) {
                    uiState = FormUpdateStateHewan.Error("Gagal Mengupdate Hewan")
                    e.printStackTrace()
                }
            }
        } else {
            uiEvent = UpdateHewanUiState(error = ErrorHewanFormState())
        }
    }

    fun getHewan() {
        viewModelScope.launch {
            uiState = FormUpdateStateHewan.Loading
            uiState = try {
                val hewanList = repo.getHewan()
                FormUpdateStateHewan.Success("Berhasil", hewanList)
            } catch (e: IOException) {
                FormUpdateStateHewan.Error("Gagal Mengambil Data Hewan")
            } catch (e: HttpException) {
                FormUpdateStateHewan.Error("Gagal Mengambil Data Hewan")
            }
        }
    }
}
data class UpdateHewanUiState(
    val updateHewanUiState: UpdateHewanUiEvent = UpdateHewanUiEvent(),
    val error: ErrorHewanFormState = ErrorHewanFormState()
)

sealed class FormUpdateStateHewan {
    object Idle : FormUpdateStateHewan()
    object Loading : FormUpdateStateHewan()
    data class Success(
        val message: String,
        val hewanList: List<Hewan> = emptyList()
    ) : FormUpdateStateHewan()
    data class Error(val message: String) : FormUpdateStateHewan()
}


data class UpdateHewanUiEvent(
    val idHewan: String = "",
    val namaHewan: String = "",
    val originalNamaHewan: String = "",
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