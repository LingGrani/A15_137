package com.example.projekakhirpam.ui.viewmodel.petugas

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekakhirpam.model.Petugas
import com.example.projekakhirpam.repo.KebunRepository
import com.example.projekakhirpam.ui.navigation.DestinasiHewanDetail
import com.example.projekakhirpam.ui.navigation.DestinasiPetugasDetail
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class DetailPetugasUiState {
    data class Success(val petugas: Petugas) : DetailPetugasUiState()
    object Error : DetailPetugasUiState()
    object Loading : DetailPetugasUiState()
}

class DetailPetugasVM(
    savedStateHandle: SavedStateHandle,
    private val repo: KebunRepository,
) : ViewModel() {

    private val _id: String = checkNotNull(savedStateHandle[DestinasiPetugasDetail.idArg])

    var detailUiState: DetailPetugasUiState by mutableStateOf(DetailPetugasUiState.Loading)
        private set

    init {
        getDataBYID()
    }

    fun getDataBYID() {
        viewModelScope.launch {
            detailUiState = DetailPetugasUiState.Loading
            detailUiState = try {
                DetailPetugasUiState.Success(repo.getPetugasById(_id.toInt()))
            } catch (e: IOException) {
                DetailPetugasUiState.Error
            } catch (e: HttpException) {
                DetailPetugasUiState.Error
            }
        }
    }
    fun delete(id: Int) {
        viewModelScope.launch {
            try {
                repo.deleteHewan(id)
                Log.d("hasil", "Berhasil")
            } catch (e: IOException) {
                DetailPetugasUiState.Error
                Log.d("hasil", "Gagal")
            } catch (e: HttpException) {
                DetailPetugasUiState.Error
                Log.d("hasil", "Gagal")
            }
        }
    }
}

