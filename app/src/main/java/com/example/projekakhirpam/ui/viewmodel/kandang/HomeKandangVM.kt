package com.example.projekakhirpam.ui.viewmodel.kandang

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhirpam.model.Hewan
import com.example.projekakhirpam.model.Kandang
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
sealed class HomeKandangUiState {
    data class Success(val kandangWithHewanList: List<KandangWithHewan>) : HomeKandangUiState()
    object Error : HomeKandangUiState()
    object Loading : HomeKandangUiState()
}

// Data gabungan Kandang dan Hewan
data class KandangWithHewan(
    val kandang: Kandang,
    val hewan: Hewan?
)

// ViewModel untuk Kandang
class HomeKandangVM(private val repo: KebunRepository) : ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _dataSearch = MutableStateFlow<List<KandangWithHewan>>(emptyList())

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
                    it.kandang.lokasi.contains(text, ignoreCase = true) ||
                            it.hewan?.namaHewan?.contains(text, ignoreCase = true) == true
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
    var kandangUiState: HomeKandangUiState by mutableStateOf(HomeKandangUiState.Loading)
        private set

    // Inisialisasi data
    init {
        getData()
    }

    // Mengambil data dari repository dan menggabungkan Kandang dengan Hewan
    fun getData() {
        viewModelScope.launch {
            kandangUiState = HomeKandangUiState.Loading
            kandangUiState = try {
                val kandangList = repo.getKandang()
                val hewanList = repo.getHewan()

                // Gabungkan data kandang dan hewan berdasarkan idHewan
                val combinedList = kandangList.map { kandang ->
                    val hewan = hewanList.find { it.idHewan == kandang.idHewan }
                    KandangWithHewan(kandang, hewan)
                }

                _dataSearch.value = combinedList
                HomeKandangUiState.Success(combinedList)
            } catch (e: IOException) {
                HomeKandangUiState.Error
            } catch (e: HttpException) {
                HomeKandangUiState.Error
            }
        }
    }
}
