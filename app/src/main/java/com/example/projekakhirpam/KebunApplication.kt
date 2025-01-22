package com.example.projekakhirpam

import android.app.Application
import com.example.projekakhirpam.repo.AppContainer
import com.example.projekakhirpam.repo.KebunContainer

class KebunApplications: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = KebunContainer()
    }
}