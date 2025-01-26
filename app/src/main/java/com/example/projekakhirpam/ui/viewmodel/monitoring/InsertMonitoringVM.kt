package com.example.projekakhirpam.ui.viewmodel.monitoring

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhirpam.model.Monitoring
import com.example.projekakhirpam.repo.KebunRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class InsertMonitoringVM(private val repository: KebunRepository) : ViewModel() {
    var uiState by mutableStateOf(InsertMonitoringUiState())
        private set

    fun updateInsertDataState(insertUiEvent: InsertMonitoringUiEvent) {
        uiState = InsertMonitoringUiState(insertMonitoringUiEvent = insertUiEvent)
    }

    suspend fun insertHewan() {
        viewModelScope.launch {
            try {
                repository.insertMonitoring(uiState.insertMonitoringUiEvent.toData())
                Log.d("Hasil", uiState.insertMonitoringUiEvent.toString())
                Log.d("Hasil", "Data inserted successfully")
            } catch (e:Exception){
                e.printStackTrace()
                Log.d("Hasil", uiState.insertMonitoringUiEvent.toString())
                Log.d("Hasil", "Data failed to insert")
            }
        }
    }
}

data class InsertMonitoringUiState(
    val insertMonitoringUiEvent: InsertMonitoringUiEvent = InsertMonitoringUiEvent()
)

data class InsertMonitoringUiEvent(
    val idMonitoring: String = "",
    val idKandang: String = "",
    val idPetugas: String = "",
    val status: String = "",
    val tanggal: String = "",
    val hewanSakit: String = "0",
    val hewanSehat: String = "0",
)

fun InsertMonitoringUiEvent.toData(): Monitoring = Monitoring(
    idMonitoring = idMonitoring.toInt(),
    idKandang = idKandang.toInt(),
    idPetugas = idPetugas.toInt(),
    status = status,
    tanggalMonitoring = tanggal,
    hewanSakit = hewanSakit.toInt(),
    hewanSehat = hewanSehat.toInt()
)