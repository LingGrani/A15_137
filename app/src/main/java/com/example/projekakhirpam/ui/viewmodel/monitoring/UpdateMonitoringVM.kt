package com.example.projekakhirpam.ui.viewmodel.monitoring

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhirpam.model.Monitoring
import com.example.projekakhirpam.model.Petugas
import com.example.projekakhirpam.repo.KebunRepository
import com.example.projekakhirpam.ui.navigation.DestinasiMonitoringDetail
import com.example.projekakhirpam.ui.viewmodel.kandang.KandangWithHewan
import com.example.projekakhirpam.ui.viewmodel.kandang.UpdateKandangUiState
import com.example.projekakhirpam.ui.viewmodel.kandang.toUpdateUiEvent
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class UpdateMonitoringVM(
    savedStateHandle: SavedStateHandle,
    private val repo: KebunRepository
) : ViewModel() {

    var uiState: UpdateMonitoringUiState by mutableStateOf(UpdateMonitoringUiState.Loading)
        private set

    fun updateDataState(updateUiEvent: UpdateMonitoringUiEvent) {
        if (uiState is UpdateMonitoringUiState.Success) {
            val currentState = uiState as UpdateMonitoringUiState.Success
            uiState = currentState.copy(updateMonitoringUiEvent = updateUiEvent)
        }
    }

    private val _id: String = checkNotNull(savedStateHandle[DestinasiMonitoringDetail.idArg])

    init {
        getDataById()
    }

    fun getDataById() {
        uiState = UpdateMonitoringUiState.Loading
        viewModelScope.launch {
            uiState = try {
                val monitoring = repo.getMonitoringById(_id.toInt())
                val petugasList = repo.getPetugas()
                val kandangList = repo.getKandang()
                val hewanList = repo.getHewan()

                val combinedList = kandangList.map { kandang ->
                    val hewan = hewanList.find { it.idHewan == kandang.idHewan }
                    KandangWithHewan(kandang, hewan)
                }
                UpdateMonitoringUiState.Success(
                    petugasList = petugasList,
                    kandangHewanList = combinedList,
                    updateMonitoringUiEvent = monitoring.toUpdateUiEvent()
                )
            } catch (e: IOException) {
                UpdateMonitoringUiState.Error
            } catch (e: HttpException) {
                UpdateMonitoringUiState.Error
            }
        }
    }

    suspend fun updateData() {
        val updateEvent = (uiState as UpdateMonitoringUiState.Success).updateMonitoringUiEvent
        viewModelScope.launch {
            try {
                repo.updateMonitoring(_id.toInt(), updateEvent.todata())
                Log.d("UpdateViewModel", "updateMhs: Success")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

sealed class UpdateMonitoringUiState {
    object Loading : UpdateMonitoringUiState()
    data class Success(
        val petugasList: List<Petugas>,
        val kandangHewanList: List<KandangWithHewan>,
        val updateMonitoringUiEvent: UpdateMonitoringUiEvent = UpdateMonitoringUiEvent()
    ) : UpdateMonitoringUiState()
    object Error : UpdateMonitoringUiState()
}

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