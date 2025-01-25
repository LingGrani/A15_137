package com.example.projekakhirpam.repo

import android.util.Log
import com.example.projekakhirpam.model.Hewan
import com.example.projekakhirpam.model.Kandang
import com.example.projekakhirpam.model.Monitoring
import com.example.projekakhirpam.model.Petugas
import com.example.projekakhirpam.service.KebunBinatangService
import java.io.IOException

interface KebunRepository {
    suspend fun getHewan(): List<Hewan>
    suspend fun insertHewan(hewan: Hewan)
    suspend fun updateHewan(idHewan: Int, hewan: Hewan)
    suspend fun deleteHewan(idHewan: Int)
    suspend fun getHewanById(idHewan: Int): Hewan

    suspend fun getKandang(): List<Kandang>
    suspend fun insertKandang(kandang: Kandang)
    suspend fun updateKandang(idKandang: Int, kandang: Kandang)
    suspend fun deleteKandang(idKandang: Int)
    suspend fun getKandangById(idKandang: Int): Kandang

    suspend fun getMonitoring(): List<Monitoring>
    suspend fun insertMonitoring(monitoring: Monitoring)
    suspend fun updateMonitoring(idMonitoring: Int, monitoring: Monitoring)
    suspend fun deleteMonitoring(idMonitoring: Int)
    suspend fun getMonitoringById(idMonitoring: Int): Monitoring

    suspend fun getPetugas(): List<Petugas>
    suspend fun insertPetugas(petugas: Petugas)
    suspend fun updatePetugas(idPetugas: Int, petugas: Petugas)
    suspend fun deletePetugas(idPetugas: Int)
    suspend fun getPetugasById(idPetugas: Int): Petugas
}

class NetworkKebunRepository(private val kebunBinatangService: KebunBinatangService) : KebunRepository {

    // Hewan /////////////////////////////////////////////////////////////////////
    override suspend fun insertHewan(hewan: Hewan) {
        Log.d("Hasil", hewan.toString())
        kebunBinatangService.insertHewan(hewan)
    }
    override suspend fun updateHewan(idHewan: Int, hewan: Hewan) {
        kebunBinatangService.updateHewan(idHewan, hewan)
    }
    override suspend fun deleteHewan(idHewan: Int) {
        try {
            val response = kebunBinatangService.deleteHewan(idHewan)
            if (!response.isSuccessful) {
                throw IOException("Delete failed. HTTP Status Code: ${response.code()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }
    override suspend fun getHewan(): List<Hewan> = kebunBinatangService.getHewan()
    override suspend fun getHewanById(idHewan: Int): Hewan {
        return kebunBinatangService.getHewanById(idHewan)
    }

    // Kandang /////////////////////////////////////////////////////////////////////
    override suspend fun insertKandang(kandang: Kandang) {
        kebunBinatangService.insertKandang(kandang)
    }
    override suspend fun updateKandang(idKandang: Int, kandang: Kandang) {
        kebunBinatangService.updateKandang(idKandang, kandang)
    }
    override suspend fun deleteKandang(idKandang: Int) {
        try {
            val response = kebunBinatangService.deleteKandang(idKandang)
            if (!response.isSuccessful) {
                throw IOException("Delete failed. HTTP Status Code: ${response.code()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }
    override suspend fun getKandang(): List<Kandang> = kebunBinatangService.getKandang()
    override suspend fun getKandangById(idKandang: Int): Kandang {
        return kebunBinatangService.getKandangById(idKandang)
    }

    // Monitoring /////////////////////////////////////////////////////////////////////
    override suspend fun insertMonitoring(monitoring: Monitoring) {
        kebunBinatangService.insertMonitoring(monitoring)
    }
    override suspend fun updateMonitoring(idMonitoring: Int, monitoring: Monitoring) {
        kebunBinatangService.updateMonitoring(idMonitoring, monitoring)
    }
    override suspend fun deleteMonitoring(idMonitoring: Int) {
        try {
            val response = kebunBinatangService.deleteMonitoring(idMonitoring)
            if (!response.isSuccessful) {
                throw IOException("Delete failed. HTTP Status Code: ${response.code()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getMonitoring(): List<Monitoring> = kebunBinatangService.getMonitoring()
    override suspend fun getMonitoringById(idMonitoring: Int): Monitoring {
        return kebunBinatangService.getMonitoringById(idMonitoring)
    }

    // Petugas /////////////////////////////////////////////////////////////////////
    override suspend fun insertPetugas(petugas: Petugas) {
        kebunBinatangService.insertPetugas(petugas)
    }
    override suspend fun updatePetugas(idPetugas: Int, petugas: Petugas) {
        kebunBinatangService.updatePetugas(idPetugas, petugas)
    }
    override suspend fun deletePetugas(idPetugas: Int) {
        try {
            val response = kebunBinatangService.deletePetugas(idPetugas)
            if (!response.isSuccessful) {
                throw IOException("Delete failed. HTTP Status Code: ${response.code()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }
    override suspend fun getPetugas(): List<Petugas> = kebunBinatangService.getPetugas()
    override suspend fun getPetugasById(idPetugas: Int): Petugas {
        return kebunBinatangService.getPetugasById(idPetugas)
    }
}
