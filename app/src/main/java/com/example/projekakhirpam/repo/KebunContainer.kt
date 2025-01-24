package com.example.projekakhirpam.repo

import com.example.projekakhirpam.service.KebunBinatangService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer{
    val kebunRepository: KebunRepository
}
class KebunContainer: AppContainer{
    private val baseURL = "http://10.0.2.2/kebunbinatang/"
    private val json = Json { ignoreUnknownKeys = true }
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseURL).build()
    private val kebunBinatangService: KebunBinatangService by lazy { retrofit.create(KebunBinatangService::class.java) }
    override val kebunRepository: KebunRepository by lazy { NetworkKebunRepository(kebunBinatangService) }
}