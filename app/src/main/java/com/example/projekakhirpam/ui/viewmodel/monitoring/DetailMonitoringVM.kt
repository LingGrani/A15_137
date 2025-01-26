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
import com.example.projekakhirpam.ui.viewmodel.hewan.DetailHewanUiState
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class DetailMonitoringUiState {
    data class Success(val join: Join) : DetailMonitoringUiState()
    object Error : DetailMonitoringUiState()
    object Loading : DetailMonitoringUiState()
}

class DetailMonitoringVM(
    savedStateHandle: SavedStateHandle,
    private val repo: KebunRepository
) : ViewModel() {
    private val _id: String = checkNotNull(savedStateHandle[DestinasiMonitoringDetail.idArg])

    var detailUiState: DetailMonitoringUiState by mutableStateOf(DetailMonitoringUiState.Loading)
        private set

    init {
        getDataBYID()
    }

    fun getDataBYID() {
        viewModelScope.launch {
            detailUiState = DetailMonitoringUiState.Loading
            detailUiState = try {
                val monitoring = repo.getMonitoringById(_id.toInt())
                val kandang = repo.getKandangById(monitoring.idKandang)
                val petugas = repo.getPetugasById(monitoring.idPetugas)
                val hewan = repo.getHewanById(kandang.idHewan)
                val combined = Join(monitoring, kandang, hewan, petugas)
                DetailMonitoringUiState.Success(combined)
            } catch (e: IOException) {
                DetailMonitoringUiState.Error
            } catch (e: HttpException) {
                DetailMonitoringUiState.Error
            }
        }
    }
    fun delete(id: Int) {
        viewModelScope.launch {
            try {
                repo.deleteMonitoring(id)
                Log.d("hasil", "Berhasil")
            } catch (e: IOException) {
                DetailHewanUiState.Error
                Log.d("hasil", "Gagal")
            } catch (e: HttpException) {
                DetailHewanUiState.Error
                Log.d("hasil", "Gagal")
            }
        }
    }
}