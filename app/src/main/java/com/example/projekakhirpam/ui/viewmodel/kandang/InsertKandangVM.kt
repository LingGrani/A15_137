package com.example.projekakhirpam.ui.viewmodel.kandang

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhirpam.model.Hewan
import com.example.projekakhirpam.model.Kandang
import com.example.projekakhirpam.repo.KebunRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class InsertKandangVM(private val repository: KebunRepository) : ViewModel() {
    var uiState: InsertKandangUiState by mutableStateOf(InsertKandangUiState.Loading)
        private set

    init {
        getData()
    }

    fun updateInsertDataState(insertUiEvent: InsertKandangUiEvent) {
        if (uiState is InsertKandangUiState.Success) {
            val currentState = uiState as InsertKandangUiState.Success
            uiState = currentState.copy(insertKandangUiEvent = insertUiEvent)
        }
    }

    fun getData() {
        viewModelScope.launch {
            uiState = InsertKandangUiState.Loading
            uiState = try {
                val hewanList = repository.getHewan()
                InsertKandangUiState.Success(hewanList = hewanList)
            } catch (e: IOException) {
                InsertKandangUiState.Error
            } catch (e: HttpException) {
                InsertKandangUiState.Error
            }
        }
    }

    fun insertHewan() {
        if (uiState is InsertKandangUiState.Success) {
            val currentState = uiState as InsertKandangUiState.Success
            viewModelScope.launch {
                try {
                    repository.insertKandang(currentState.insertKandangUiEvent.toData())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}

sealed class InsertKandangUiState {
    object Loading : InsertKandangUiState()
    data class Success(
        val hewanList: List<Hewan>,
        val insertKandangUiEvent: InsertKandangUiEvent = InsertKandangUiEvent()
    ) : InsertKandangUiState()
    object Error : InsertKandangUiState()
}

data class InsertKandangUiEvent(
    val idKandang: String = "",
    val idHewan: String = "",
    val kapasitas: String = "",
    val lokasi: String = "",
)

fun InsertKandangUiEvent.toData(): Kandang = Kandang(
    idKandang = idKandang.toIntOrNull() ?: 0,
    idHewan = idHewan.toIntOrNull() ?: 0,
    kapasitas = kapasitas.toIntOrNull() ?: 0,
    lokasi = lokasi
)
