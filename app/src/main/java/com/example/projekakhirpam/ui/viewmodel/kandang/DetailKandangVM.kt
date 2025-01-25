package com.example.projekakhirpam.ui.viewmodel.kandang

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhirpam.repo.KebunRepository
import com.example.projekakhirpam.ui.navigation.DestinasiKandangDetail
import com.example.projekakhirpam.ui.viewmodel.hewan.DetailHewanUiState
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class DetailKandangUiState {
    data class Success(val kandangWithHewan: KandangWithHewan) : DetailKandangUiState()
    object Error : DetailKandangUiState()
    object Loading : DetailKandangUiState()
}

class DetailKandangVM(
    savedStateHandle: SavedStateHandle,
    private val repo: KebunRepository
) : ViewModel() {
    private val _id: String = checkNotNull(savedStateHandle[DestinasiKandangDetail.idArg])

    var detailUiState: DetailKandangUiState by mutableStateOf(DetailKandangUiState.Loading)
        private set

    init {
        getDataBYID()
    }

    fun getDataBYID() {
        viewModelScope.launch {
            detailUiState = DetailKandangUiState.Loading
            detailUiState = try {
                val kandang = repo.getKandangById(_id.toInt())
                val hewan = repo.getHewanById(kandang.idHewan)
                val combined = KandangWithHewan(kandang, hewan)
                DetailKandangUiState.Success(combined)
            } catch (e: IOException) {
                DetailKandangUiState.Error
            } catch (e: HttpException) {
                DetailKandangUiState.Error
            }
        }
    }
    fun delete(id: Int) {
        viewModelScope.launch {
            try {
                repo.deleteKandang(id) // Menghapus data
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