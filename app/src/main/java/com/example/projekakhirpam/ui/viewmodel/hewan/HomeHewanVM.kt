package com.example.projekakhirpam.ui.viewmodel.hewan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhirpam.model.Hewan
import com.example.projekakhirpam.repo.KebunRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class HomeHewanUiState {
    data class Success(val hewanList: List<Hewan>) : HomeHewanUiState()
    object Error : HomeHewanUiState()
    object Loading : HomeHewanUiState()
}

class HomeHewanVM(private val hewanRepository: KebunRepository) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _dataSearch = MutableStateFlow<List<Hewan>>(emptyList())

    // Filtered data search logic
    val datas = searchText
        .debounce(500L) // Mengurangi efek pencarian cepat
        .onEach { _isSearching.update { true } }
        .combine(_dataSearch) { text, datas ->
            if (text.isBlank()) {
                datas // Jika tidak ada input, tampilkan semua data
            } else {
                delay(300L) // Simulasi waktu proses pencarian
                datas.filter { it.doesMatchSearchQuery(text) }
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

    var hewanUiState: HomeHewanUiState by mutableStateOf(HomeHewanUiState.Loading)
        private set

    init {
        getHewan()
    }

    fun getHewan() {
        viewModelScope.launch {
            hewanUiState = HomeHewanUiState.Loading
            hewanUiState = try {
                val hewanList = hewanRepository.getHewan()
                _dataSearch.value = hewanList // Memperbarui data utama
                HomeHewanUiState.Success(hewanList)
            } catch (e: IOException) {
                HomeHewanUiState.Error
            } catch (e: HttpException) {
                HomeHewanUiState.Error
            }
        }
    }
}
