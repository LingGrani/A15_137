package com.example.projekakhirpam.ui.viewmodel.monitoring

import com.example.projekakhirpam.model.Kandang
import com.example.projekakhirpam.model.Petugas
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhirpam.model.Hewan
import com.example.projekakhirpam.model.Monitoring
import com.example.projekakhirpam.repo.KebunRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

// State untuk UI
sealed class HomeMonitoringUiState {
    data class Success(val join: List<Join>) : HomeMonitoringUiState()
    object Error : HomeMonitoringUiState()
    object Loading : HomeMonitoringUiState()
}

// Data gabungan 
data class Join(
    val monitoring: Monitoring,
    val kandang: Kandang?,
    val hewan: Hewan?,
    val petugas: Petugas?
)

// ViewModel untuk Monitoring
class HomeMonitoringVM(private val repo: KebunRepository) : ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _dataSearch = MutableStateFlow<List<Join>>(emptyList())

    // Logika pencarian
    val datas = searchText
        .debounce(500L)
        .onEach { _isSearching.update { true } }
        .combine(_dataSearch) { text, combinedList ->
            if (text.isBlank()) {
                combinedList
            } else {
                delay(300L)
                combinedList.filter {
                    it.monitoring.status .contains(text, ignoreCase = true)
                            || it.hewan?.namaHewan?.contains(text, ignoreCase = true) == true
                            || it.kandang?.lokasi?.contains(text, ignoreCase = true) == true
                            || it.petugas?.namaPetugas?.contains(text, ignoreCase = true) == true
                }
            }
        }
        .onEach { _isSearching.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    // State untuk menyimpan data
    var monitoringUiState: HomeMonitoringUiState by mutableStateOf(HomeMonitoringUiState.Loading)
        private set

    // Inisialisasi data
    init {
        getData()
    }

    // Mengambil data dari repository dan menggabungkan Monitoring dengan Hewan
    fun getData() {
        viewModelScope.launch {
            monitoringUiState = HomeMonitoringUiState.Loading
            monitoringUiState = try {
                val monitoringList = repo.getMonitoring()
                val hewanList = repo.getHewan()
                val petugasList = repo.getPetugas()
                val kandangList = repo.getKandang()

                val combinedList = monitoringList.map { monitoring ->
                    val kandang = kandangList.find { it.idKandang == monitoring.idKandang }
                    val petugas = petugasList.find { it.idPetugas == monitoring.idPetugas }
                    val hewan = hewanList.find { it.idHewan ==  kandang?.idHewan}
                    Join(monitoring, kandang, hewan, petugas)
                }

                _dataSearch.value = combinedList
                HomeMonitoringUiState.Success(combinedList)
            } catch (e: IOException) {
                HomeMonitoringUiState.Error
            } catch (e: HttpException) {
                HomeMonitoringUiState.Error
            }
        }
    }
}
