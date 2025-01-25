package com.example.projekakhirpam.ui.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.projekakhirpam.KebunApplications
import com.example.projekakhirpam.ui.viewmodel.hewan.DetailHewanVM
import com.example.projekakhirpam.ui.viewmodel.hewan.HomeHewanVM
import com.example.projekakhirpam.ui.viewmodel.hewan.InsertHewanVM
import com.example.projekakhirpam.ui.viewmodel.hewan.UpdateHewanVM
import com.example.projekakhirpam.ui.viewmodel.kandang.DetailKandangVM
import com.example.projekakhirpam.ui.viewmodel.kandang.HomeKandangVM
import com.example.projekakhirpam.ui.viewmodel.kandang.InsertKandangVM
import com.example.projekakhirpam.ui.viewmodel.kandang.UpdateKandangVM
import com.example.projekakhirpam.ui.viewmodel.petugas.DetailPetugasVM
import com.example.projekakhirpam.ui.viewmodel.petugas.HomePetugasVM
import com.example.projekakhirpam.ui.viewmodel.petugas.InsertPetugasVM
import com.example.projekakhirpam.ui.viewmodel.petugas.UpdatePetugasVM

fun CreationExtras.aplikasiKebunBinatang(): KebunApplications =
(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as KebunApplications)

object PenyediaViewModel{
    val Factory = viewModelFactory {
        initializer { HomeHewanVM(aplikasiKebunBinatang().container.kebunRepository)}
        initializer {
            DetailHewanVM(
                createSavedStateHandle(),
                aplikasiKebunBinatang().container.kebunRepository
            )
        }
        initializer { InsertHewanVM(aplikasiKebunBinatang().container.kebunRepository) }
        initializer { UpdateHewanVM(createSavedStateHandle(), aplikasiKebunBinatang().container.kebunRepository) }

        initializer { HomeKandangVM(aplikasiKebunBinatang().container.kebunRepository) }
        initializer {
            DetailKandangVM(
                createSavedStateHandle(),
                aplikasiKebunBinatang().container.kebunRepository
            )
        }
        initializer { InsertKandangVM(aplikasiKebunBinatang().container.kebunRepository) }
        initializer { UpdateKandangVM(createSavedStateHandle(), aplikasiKebunBinatang().container.kebunRepository) }

        initializer { HomePetugasVM(aplikasiKebunBinatang().container.kebunRepository) }
        initializer {
            DetailPetugasVM(
                createSavedStateHandle(),
                aplikasiKebunBinatang().container.kebunRepository
            )
        }
        initializer { InsertPetugasVM(aplikasiKebunBinatang().container.kebunRepository) }
        initializer { UpdatePetugasVM(createSavedStateHandle(), aplikasiKebunBinatang().container.kebunRepository) }
    }
}