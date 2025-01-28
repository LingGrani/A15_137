package com.example.projekakhirpam.ui.viewmodel.monitoring

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhirpam.model.Hewan
import com.example.projekakhirpam.model.Monitoring
import com.example.projekakhirpam.model.Petugas
import com.example.projekakhirpam.repo.KebunRepository
import com.example.projekakhirpam.ui.viewmodel.kandang.InsertKandangUiEvent
import com.example.projekakhirpam.ui.viewmodel.kandang.InsertKandangUiState
import com.example.projekakhirpam.ui.viewmodel.kandang.KandangWithHewan
import com.example.projekakhirpam.ui.viewmodel.kandang.toData
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.lang.Error

class InsertMonitoringVM(private val repository: KebunRepository) : ViewModel() {
    var uiEvent: InsertMonitoringUiState by mutableStateOf(InsertMonitoringUiState.Loading)
        private set

    var uiState: FormStateMonitoring by mutableStateOf(FormStateMonitoring.Idle)
        private set

    fun updateInsertDataState(insertUiEvent: InsertMonitoringUiEvent) {
        if (uiEvent is InsertMonitoringUiState.Success) {
            val currentState = uiEvent as InsertMonitoringUiState.Success
            uiEvent = currentState.copy(insertMonitoringUiEvent = insertUiEvent)
        }
    }

    fun validateFields(): Boolean {
        val currentUiEvent = uiEvent as? InsertMonitoringUiState.Success ?: return false

        val hewanSakit = currentUiEvent.insertMonitoringUiEvent.hewanSakit.toIntOrNull() ?: 0
        val hewanSehat = currentUiEvent.insertMonitoringUiEvent.hewanSehat.toIntOrNull() ?: 0
        val totalHewan = hewanSakit + hewanSehat

        val status = when {
            totalHewan == 0 -> "Data Tidak Valid"
            hewanSakit.toDouble() / totalHewan < 0.1 -> "Aman"
            hewanSakit.toDouble() / totalHewan < 0.5 -> "Waspada"
            else -> "Kritis"
        }

        val error = ErrorMonitoringFormState(
            idKandang = if (currentUiEvent.insertMonitoringUiEvent.idKandang.isBlank()) "Id Kandang tidak boleh kosong" else null,
            idPetugas = if (currentUiEvent.insertMonitoringUiEvent.idPetugas.isBlank()) "Id Petugas tidak boleh kosong" else null,
            status = if (status == "Data Tidak Valid") "Status tidak valid" else null,
            tanggal = if (currentUiEvent.insertMonitoringUiEvent.tanggal.isBlank()) "Tanggal tidak boleh kosong" else null,
        )

        // Update UI Event with calculated status
        updateInsertDataState(
            currentUiEvent.insertMonitoringUiEvent.copy(status = status)
        )

        uiEvent = currentUiEvent.copy(error = error)
        return error.isValid()
    }

    init {
        getData()
    }

    fun getData() {
        viewModelScope.launch {
            uiEvent = InsertMonitoringUiState.Loading
            uiEvent = try {
                val petugasList = repository.getPetugas()
                val kandangList = repository.getKandang()
                val hewanList = repository.getHewan()

                val combinedList = kandangList.map { kandang ->
                    val hewan = hewanList.find { it.idHewan == kandang.idHewan }
                    KandangWithHewan(kandang, hewan)
                }
                InsertMonitoringUiState.Success(
                    petugasList = petugasList,
                    kandangHewanList = combinedList
                )
            } catch (e: IOException) {
                InsertMonitoringUiState.Error
            } catch (e: HttpException) {
                InsertMonitoringUiState.Error
            }
        }
    }

    suspend fun insertMonitoring() {
        if (validateFields()){
            uiState = FormStateMonitoring.Loading
            if (uiEvent is InsertMonitoringUiState.Success) {
                val currentState = uiEvent as InsertMonitoringUiState.Success
                viewModelScope.launch {
                    try {
                        repository.insertMonitoring(currentState.insertMonitoringUiEvent.toData())
                        uiState = FormStateMonitoring.Success("Berhasil")
                    } catch (e: Exception) {
                        uiState = FormStateMonitoring.Error("Gagal")
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}

sealed class InsertMonitoringUiState {
    object Loading : InsertMonitoringUiState()
    data class Success(
        val petugasList: List<Petugas>,
        val kandangHewanList: List<KandangWithHewan>,
        val insertMonitoringUiEvent: InsertMonitoringUiEvent = InsertMonitoringUiEvent(),
        val error: ErrorMonitoringFormState = ErrorMonitoringFormState()
    ) : InsertMonitoringUiState()
    object Error : InsertMonitoringUiState()
}

sealed class FormStateMonitoring {
    object Idle : FormStateMonitoring()
    object Loading : FormStateMonitoring()
    data class Success(val message: String) : FormStateMonitoring()
    data class Error(val message: String) : FormStateMonitoring()
}

data class InsertMonitoringUiEvent(
    val idMonitoring: String = "",
    val idKandang: String = "",
    val idPetugas: String = "",
    val status: String = "",
    val tanggal: String = "",
    val hewanSakit: String = "",
    val hewanSehat: String = "",
)

fun InsertMonitoringUiEvent.toData(): Monitoring = Monitoring(
    idMonitoring = idMonitoring.toIntOrNull() ?: 0,
    idKandang = idKandang.toInt(),
    idPetugas = idPetugas.toInt(),
    status = status,
    tanggalMonitoring = tanggal,
    hewanSakit = hewanSakit.toIntOrNull() ?: 0,
    hewanSehat = hewanSehat.toIntOrNull() ?: 0
)

data class ErrorMonitoringFormState(
    val idKandang: String? = null,
    val idPetugas: String? = null,
    val status: String? = null,
    val tanggal: String? = null,
    val hewanSakit: String? = null,
    val hewanSehat: String? = null
) {
    fun isValid(): Boolean {
        return idKandang == null && idPetugas == null && status == null && tanggal == null && hewanSakit == null && hewanSehat == null
    }
}