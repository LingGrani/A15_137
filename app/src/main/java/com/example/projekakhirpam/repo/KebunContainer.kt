package com.example.projekakhirpam.repo

interface AppContainer{
    val kebunRepository: KebunRepository
}
class KebunContainer: AppContainer{
    override val kebunRepository: KebunRepository
        get() = TODO("Not yet implemented")
}