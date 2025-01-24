package com.example.projekakhirpam.ui.viewmodel.hewan

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhirpam.model.Hewan
import com.example.projekakhirpam.repo.KebunRepository
import com.example.projekakhirpam.ui.navigation.DestinasiHewanDetail
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class DetailHewanUiState {
    data class Success(val hewan: Hewan) : DetailHewanUiState()
    object Error : DetailHewanUiState()
    object Loading : DetailHewanUiState()
}

class DetailHewanVM(
    savedStateHandle: SavedStateHandle,
    private val repo: KebunRepository,
) : ViewModel() {

    // Mendapatkan NIM dari SavedStateHandle
    private val _id: String = checkNotNull(savedStateHandle[DestinasiHewanDetail.idArg])

    var detailUiState: DetailHewanUiState by mutableStateOf(DetailHewanUiState.Loading)
        private set

    init {
        getDataBYID()
    }

    fun getDataBYID() {
        viewModelScope.launch {
            detailUiState = DetailHewanUiState.Loading
            detailUiState = try {
                DetailHewanUiState.Success(repo.getHewanById(_id.toInt()))
            } catch (e: IOException) {
                DetailHewanUiState.Error
            } catch (e: HttpException) {
                DetailHewanUiState.Error
            }
        }
    }
    fun deleteHewan(idHewan: Int) {
        viewModelScope.launch {
            try {
                repo.deleteHewan(idHewan) // Menghapus data
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

