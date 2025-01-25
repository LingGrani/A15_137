package com.example.projekakhirpam.ui.viewmodel.petugas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhirpam.model.Petugas
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

sealed class HomePetugasUiState {
    data class Success(val list: List<Petugas>) : HomePetugasUiState()
    object Error : HomePetugasUiState()
    object Loading : HomePetugasUiState()
}

class HomePetugasVM(private val hewanRepository: KebunRepository) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _dataSearch = MutableStateFlow<List<Petugas>>(emptyList())

    // Filtered data search logic
    val datas = searchText
        .debounce(500L) // Mengurangi efek pencarian cepat
        .onEach { _isSearching.update { true } }
        .combine(_dataSearch) { text, datas ->
            if (text.isBlank()) {
                datas
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

    var petugasUiState: HomePetugasUiState by mutableStateOf(HomePetugasUiState.Loading)
        private set

    init {
        getData()
    }

    fun getData() {
        viewModelScope.launch {
            petugasUiState = HomePetugasUiState.Loading
            petugasUiState = try {
                val list = hewanRepository.getPetugas()
                _dataSearch.value = list // Memperbarui data utama
                HomePetugasUiState.Success(list)
            } catch (e: IOException) {
                HomePetugasUiState.Error
            } catch (e: HttpException) {
                HomePetugasUiState.Error
            }
        }
    }
}