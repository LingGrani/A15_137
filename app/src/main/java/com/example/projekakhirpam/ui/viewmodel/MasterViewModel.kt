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
    }
}