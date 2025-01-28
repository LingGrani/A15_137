package com.example.projekakhirpam.ui.viewmodel.hewan

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhirpam.model.Hewan
import com.example.projekakhirpam.repo.KebunRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class InsertHewanVM(private val repository: KebunRepository) : ViewModel() {
    var uiEvent by mutableStateOf(InsertHewanUiState())
        private set
    var uiState: FormStateHewan by mutableStateOf(FormStateHewan.Idle)
        private set

    fun updateInsertDataState(insertUiEvent: InsertHewanUiEvent) {
        uiEvent = uiEvent.copy(insertHewanUiEvent = insertUiEvent)
    }

    fun validateFields(): Boolean {
        val event = uiEvent.insertHewanUiEvent
        val state = uiState as? FormStateHewan.Success ?: return false
        val error = ErrorHewanFormState(
            namaHewan = when {
                event.namaHewan.isBlank() -> "Nama Hewan tidak boleh kosong"
                state.hewanList.any { it.namaHewan.equals(event.namaHewan, ignoreCase = true) } -> "Nama Hewan sudah ada"
                else -> null
            },
            tipePakan = if (event.tipePakan.isBlank()) "Tipe Pakan tidak boleh kosong" else null,
            populasi = when {
                event.populasi.isBlank() -> "Populasi tidak boleh kosong"
                event.populasi.toIntOrNull() == null -> "Populasi harus berupa angka"
                else -> null
            },
            zonaWilayah = if (event.zonaWilayah.isBlank()) "Zona Wilayah tidak boleh kosong" else null
        )
        uiEvent = uiEvent.copy(error = error)
        return error.isValid()
    }
    fun insertHewan() {
        if (validateFields()) {
            uiState = FormStateHewan.Loading
            viewModelScope.launch {
                try {
                    repository.insertHewan(uiEvent.insertHewanUiEvent.toData())
                    uiState = FormStateHewan.Success("Berhasil menambahkan Hewan")
                } catch (e:Exception){
                    uiState = FormStateHewan.Error("Gagal Menambahkan Hewan")
                    e.printStackTrace()
                }
            }
        } else {
            uiEvent = InsertHewanUiState(error = ErrorHewanFormState())
        }
    }
    init {
        getHewan()
    }

    fun getHewan() {
        viewModelScope.launch {
            uiState = FormStateHewan.Loading
            uiState = try {
                val hewanList = repository.getHewan()
                FormStateHewan.Success("Berhasil", hewanList)
            } catch (e: IOException) {
                FormStateHewan.Error("Gagal Mengambil Data Hewan")
            } catch (e: HttpException) {
                FormStateHewan.Error("Gagal Mengambil Data Hewan")
            }
        }
    }
}

data class InsertHewanUiState(
    val insertHewanUiEvent: InsertHewanUiEvent = InsertHewanUiEvent(),
    val error: ErrorHewanFormState = ErrorHewanFormState()
)

sealed class FormStateHewan {
    object Idle : FormStateHewan()
    object Loading : FormStateHewan()
    data class Success(
        val message: String,
        val hewanList: List<Hewan> = emptyList()
    ) : FormStateHewan()
    data class Error(val message: String) : FormStateHewan()
}

data class InsertHewanUiEvent(
    val idHewan: String = "",
    val namaHewan: String = "",
    val tipePakan: String = "",
    val populasi: String = "",
    val zonaWilayah: String = "",
)

fun InsertHewanUiEvent.toData(): Hewan = Hewan(
    idHewan = idHewan.toIntOrNull() ?: 0,
    namaHewan = namaHewan,
    tipePakan = tipePakan,
    populasi = populasi.toIntOrNull() ?: 0,
    zonaWilayah = zonaWilayah
)

data class ErrorHewanFormState (
    val namaHewan: String? = null,
    val tipePakan: String? = null,
    val populasi: String? = null,
    val zonaWilayah: String? = null
) {
    fun isValid(): Boolean {
        return namaHewan == null && tipePakan == null && populasi == null && zonaWilayah == null
    }
}

