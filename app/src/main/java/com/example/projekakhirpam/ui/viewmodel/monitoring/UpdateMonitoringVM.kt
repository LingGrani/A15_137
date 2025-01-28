package com.example.projekakhirpam.ui.viewmodel.monitoring

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhirpam.model.Kandang
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

    var uiEvent: UpdateMonitoringUiState by mutableStateOf(UpdateMonitoringUiState.Loading)
        private set

    var uiState: FormUpdateStateMonitoring by mutableStateOf(FormUpdateStateMonitoring.Idle)
        private set

    fun updateDataState(updateUiEvent: UpdateMonitoringUiEvent) {
        if (uiEvent is UpdateMonitoringUiState.Success) {
            val currentState = uiEvent as UpdateMonitoringUiState.Success
            uiEvent = currentState.copy(updateMonitoringUiEvent = updateUiEvent)
        }
    }

    private val _id: String = checkNotNull(savedStateHandle[DestinasiMonitoringDetail.idArg])

    fun validateFields(): Boolean {
        val currentUiEvent = uiEvent as? UpdateMonitoringUiState.Success ?: return false

        val hewanSakit = currentUiEvent.updateMonitoringUiEvent.hewanSakit.toIntOrNull() ?: 0
        val hewanSehat = currentUiEvent.updateMonitoringUiEvent.hewanSehat.toIntOrNull() ?: 0
        val totalHewan = hewanSakit + hewanSehat

        val status = when {
            totalHewan == 0 -> "Data Tidak Valid"
            hewanSakit.toDouble() / totalHewan < 0.1 -> "Aman"
            hewanSakit.toDouble() / totalHewan < 0.5 -> "Waspada"
            else -> "Kritis"
        }

        val error = ErrorMonitoringFormState(
            idKandang = if (currentUiEvent.updateMonitoringUiEvent.idKandang.isBlank()) "Id Kandang tidak boleh kosong" else null,
            idPetugas = if (currentUiEvent.updateMonitoringUiEvent.idPetugas.isBlank()) "Id Petugas tidak boleh kosong" else null,
            status = if (status == "Data Tidak Valid") "Status tidak valid" else null,
            tanggal = if (currentUiEvent.updateMonitoringUiEvent.tanggalMonitoring.isBlank()) "Tanggal tidak boleh kosong" else null,
        )

        updateDataState(
            currentUiEvent.updateMonitoringUiEvent.copy(status = status)
        )

        uiEvent = currentUiEvent.copy(error = error)
        return error.isValid()
    }

    init {
        getDataById()
    }

    fun getDataById() {
        uiEvent = UpdateMonitoringUiState.Loading
        viewModelScope.launch {
            uiEvent = try {
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
        if (validateFields()){
            uiState = FormUpdateStateMonitoring.Loading
            val updateEvent = (uiEvent as UpdateMonitoringUiState.Success).updateMonitoringUiEvent
            viewModelScope.launch {
                try {
                    repo.updateMonitoring(_id.toInt(), updateEvent.todata())
                    Log.d("UpdateViewModel", "updateMhs: Success")
                    uiState = FormUpdateStateMonitoring.Success("Berhasil")
                } catch (e: Exception) {
                    uiState = FormUpdateStateMonitoring.Error("Gagal")
                    e.printStackTrace()
                }
            }
        }
    }
}

sealed class UpdateMonitoringUiState {
    object Loading : UpdateMonitoringUiState()
    data class Success(
        val petugasList: List<Petugas>,
        val kandangHewanList: List<KandangWithHewan>,
        val updateMonitoringUiEvent: UpdateMonitoringUiEvent = UpdateMonitoringUiEvent(),
        val error: ErrorMonitoringFormState = ErrorMonitoringFormState()
    ) : UpdateMonitoringUiState()
    object Error : UpdateMonitoringUiState()
}

sealed class FormUpdateStateMonitoring {
    object Idle : FormUpdateStateMonitoring()
    object Loading : FormUpdateStateMonitoring()
    data class Success(val message: String) : FormUpdateStateMonitoring()
    data class Error(val message: String) : FormUpdateStateMonitoring()
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
    hewanSakit = hewanSakit.toIntOrNull() ?: 0,
    hewanSehat = hewanSehat.toIntOrNull() ?: 0
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