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
import retrofit2.http.Query

interface KebunBinatangService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
//    @GET(".")
//    suspend fun getMahasiswa(): MahasiswaResponse
//
//    @GET("{nim}")
//    suspend fun getMahasiswaById(@Query("nim")nim:String): MahasiswaDetailResponse
//
//    @POST("/store")
//    suspend fun insertMahasiswa(@Body mahasiswa: Mahasiswa)
//
//    @PUT("{nim}")
//    suspend fun updateMahasiswa(@Query("nim")nim:String, @Body mahasiswa: Mahasiswa)
//
//    @DELETE("{nim}")
//    suspend fun deleteMahasiswa(@Query("nim")nim:String): Response<Void>
    @GET("hewan.php")
    suspend fun getHewan(): List<Hewan>

    @POST("hewan.php")
    suspend fun insertHewan(@Body hewan: Hewan)

    @PUT("hewan.php/{idHewan}")
    suspend fun updateHewan(@Query("idHewan") idHewan: Int, @Body hewan: Hewan)

    @DELETE("hewan.php/{idHewan}")
    suspend fun deleteHewan(@Query("idHewan") idHewan: Int): Response<Void>

    @GET("hewan.php/{idHewan}")
    suspend fun getHewanById(@Query("idHewan") idHewan: Int): Hewan


    ////////////////////////////////////////////////////////////////////////

    @GET("kandang.php")
    suspend fun getKandang(): List<Kandang>

    @POST("kandang.php")
    suspend fun insertKandang(@Body kandang: Kandang)

    @PUT("kandang.php/{idKandang}")
    suspend fun updateKandang(@Query("idKandang") idKandang: Int, @Body kandang: Kandang): Kandang

    @DELETE("kandang.php/{idKandang}")
    suspend fun deleteKandang(@Query("idKandang") idKandang: Int): Response<Void>

    @GET("kandang.php/{idKandang}")
    suspend fun getKandangById(@Query("idKandang") idKandang: Int): Kandang


    ////////////////////////////////////////////////////////////////////////

    @GET("monitoring.php")
    suspend fun getMonitoring(): List<Monitoring>

    @POST("monitoring.php")
    suspend fun insertMonitoring(@Body monitoring: Monitoring)

    @PUT("monitoring.php/{idMonitoring}")
    suspend fun updateMonitoring(@Query("idMonitoring") idMonitoring: Int, @Body monitoring: Monitoring): Monitoring

    @DELETE("monitoring.php/{idMonitoring}")
    suspend fun deleteMonitoring(@Query("idMonitoring") idMonitoring: Int): Response<Void>

    @GET("monitoring.php/{idMonitoring}")
    suspend fun getMonitoringById(@Query("idMonitoring") idMonitoring: Int): Monitoring

    ////////////////////////////////////////////////////////////////////////

    @GET("petugas.php")
    suspend fun getPetugas(): List<Petugas>

    @POST("petugas.php")
    suspend fun insertPetugas(@Body petugas: Petugas)

    @PUT("petugas.php/{idPetugas}")
    suspend fun updatePetugas(@Query("idPetugas") idPetugas: Int, @Body petugas: Petugas): Petugas

    @DELETE("petugas.php/{idPetugas}")
    suspend fun deletePetugas(@Query("idPetugas") idPetugas: Int): Response<Void>

    @GET("petugas.php/{idPetugas}")
    suspend fun getPetugasById(@Query("idPetugas") idPetugas: Int): Petugas

}