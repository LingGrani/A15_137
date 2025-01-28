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

class InsertMonitoringVM(private val repository: KebunRepository) : ViewModel() {
    var uiState: InsertMonitoringUiState by mutableStateOf(InsertMonitoringUiState.Loading)
        private set

    fun updateInsertDataState(insertUiEvent: InsertMonitoringUiEvent) {
        if (uiState is InsertMonitoringUiState.Success) {
            val currentState = uiState as InsertMonitoringUiState.Success
            uiState = currentState.copy(insertMonitoringUiEvent = insertUiEvent)
        }
    }

    init {
        getData()
    }

    fun getData() {

        viewModelScope.launch {
            uiState = InsertMonitoringUiState.Loading
            uiState = try {
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
        if (uiState is InsertMonitoringUiState.Success) {
            val currentState = uiState as InsertMonitoringUiState.Success
            viewModelScope.launch {
                try {
                    repository.insertMonitoring(currentState.insertMonitoringUiEvent.toData())
                } catch (e: Exception) {
                    e.printStackTrace()
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
        val insertMonitoringUiEvent: InsertMonitoringUiEvent = InsertMonitoringUiEvent()
    ) : InsertMonitoringUiState()
    object Error : InsertMonitoringUiState()
}

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