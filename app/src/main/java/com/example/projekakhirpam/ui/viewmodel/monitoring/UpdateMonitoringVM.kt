package com.example.projekakhirpam.ui.viewmodel.monitoring

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhirpam.model.Monitoring
import com.example.projekakhirpam.repo.KebunRepository
import com.example.projekakhirpam.ui.navigation.DestinasiMonitoringDetail
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class UpdateMonitoringVM(
    savedStateHandle: SavedStateHandle,
    private val repo: KebunRepository
) : ViewModel() {

    var uiState by mutableStateOf(UpdateMonitoringUiState())
        private set

    fun updateDataState(updateUiEvent: UpdateMonitoringUiEvent) {
        uiState = UpdateMonitoringUiState(updateMonitoringUiEvent = updateUiEvent)
    }

    private val _id: String = checkNotNull(savedStateHandle[DestinasiMonitoringDetail.idArg])

    init {
        getDataById(_id)
    }

    fun getDataById(id: String) {
        viewModelScope.launch {
            try {
                val petugas = repo.getMonitoringById(id.toInt())
                uiState = UpdateMonitoringUiState(updateMonitoringUiEvent = petugas.toUpdateUiEvent())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun updateData() {
        viewModelScope.launch {
            try {
                repo.updateMonitoring(_id.toInt(), uiState.updateMonitoringUiEvent.todata())
                Log.d("UpdateViewModel", "updateMhs: Success")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

data class UpdateMonitoringUiState(
    val updateMonitoringUiEvent: UpdateMonitoringUiEvent = UpdateMonitoringUiEvent()
)

data class UpdateMonitoringUiEvent(
    val idMonitoring: String = "",
    val idKandang: String = "",
    val idPetugas: String = "",
    val status: String = "",
    val tanggalMonitoring: String = "",
    val hewanSakit: String = "",
    val hewanSehat: String = "",
)

fun UpdateMonitoringUiEvent.todata(): Monitoring = Monitoring(
    idMonitoring = idMonitoring.toInt(),
    idKandang = idKandang.toInt(),
    idPetugas = idPetugas.toInt(),
    status = status,
    tanggalMonitoring = tanggalMonitoring,
    hewanSakit = hewanSakit.toInt(),
    hewanSehat = hewanSehat.toInt()
)

fun Monitoring.toUpdateUiEvent(): UpdateMonitoringUiEvent = UpdateMonitoringUiEvent(
    idMonitoring = idMonitoring.toString(),
    idKandang = idKandang.toString(),
    idPetugas = idPetugas.toString(),
    status = status,
    tanggalMonitoring = tanggalMonitoring,
    hewanSakit = hewanSakit.toString(),
    hewanSehat = hewanSehat.toString()
)