package com.example.projekakhirpam.service

import com.example.projekakhirpam.model.Hewan
import com.example.projekakhirpam.model.Kandang
import com.example.projekakhirpam.model.Monitoring
import com.example.projekakhirpam.model.Petugas
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface KebunBinatangService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
//    @GET(".")
//    suspend fun getMahasiswa(): MahasiswaResponse
//
//    @GET("{nim}")
//    suspend fun getMahasiswaById(@Path("nim")nim:String): MahasiswaDetailResponse
//
//    @POST("/store")
//    suspend fun insertMahasiswa(@Body mahasiswa: Mahasiswa)
//
//    @PUT("{nim}")
//    suspend fun updateMahasiswa(@Path("nim")nim:String, @Body mahasiswa: Mahasiswa)
//
//    @DELETE("{nim}")
//    suspend fun deleteMahasiswa(@Path("nim")nim:String): Response<Void>
    @GET("hewan.php")
    suspend fun getHewan(): List<Hewan>

    @POST("hewan.php")
    suspend fun insertHewan(@Body hewan: Hewan): Hewan

    @PUT("hewan.php/{idHewan}")
    suspend fun updateHewan(@Path("idHewan") idHewan: Int, @Body hewan: Hewan): Hewan

    @DELETE("hewan.php/{idHewan}")
    suspend fun deleteHewan(@Path("idHewan") idHewan: Int): Response<Void>

    @GET("hewan.php/{idHewan}")
    suspend fun getHewanById(@Path("idHewan") idHewan: Int): Hewan

    ////////////////////////////////////////////////////////////////////////

    @GET("kandang.php")
    suspend fun getKandang(): List<Kandang>

    @POST("kandang.php")
    suspend fun insertKandang(@Body kandang: Kandang): Kandang

    @PUT("kandang.php/{idKandang}")
    suspend fun updateKandang(@Path("idKandang") idKandang: Int, @Body kandang: Kandang): Kandang

    @DELETE("kandang.php/{idKandang}")
    suspend fun deleteKandang(@Path("idKandang") idKandang: Int): Response<Void>

    @GET("kandang.php/{idKandang}")
    suspend fun getKandangById(@Path("idKandang") idKandang: Int): Kandang


    ////////////////////////////////////////////////////////////////////////

    @GET("monitoring.php")
    suspend fun getMonitoring(): List<Monitoring>

    @POST("monitoring.php")
    suspend fun insertMonitoring(@Body monitoring: Monitoring): Monitoring

    @PUT("monitoring.php/{idMonitoring}")
    suspend fun updateMonitoring(@Path("idMonitoring") idMonitoring: Int, @Body monitoring: Monitoring): Monitoring

    @DELETE("monitoring.php/{idMonitoring}")
    suspend fun deleteMonitoring(@Path("idMonitoring") idMonitoring: Int): Response<Void>

    @GET("monitoring.php/{idMonitoring}")
    suspend fun getMonitoringById(@Path("idMonitoring") idMonitoring: Int): Monitoring

    ////////////////////////////////////////////////////////////////////////

    @GET("petugas.php")
    suspend fun getPetugas(): List<Petugas>

    @POST("petugas.php")
    suspend fun insertPetugas(@Body petugas: Petugas): Petugas

    @PUT("petugas.php/{idPetugas}")
    suspend fun updatePetugas(@Path("idPetugas") idPetugas: Int, @Body petugas: Petugas): Petugas

    @DELETE("petugas.php/{idPetugas}")
    suspend fun deletePetugas(@Path("idPetugas") idPetugas: Int): Response<Void>

    @GET("petugas.php/{idPetugas}")
    suspend fun getPetugasById(@Path("idPetugas") idPetugas: Int): Petugas

}