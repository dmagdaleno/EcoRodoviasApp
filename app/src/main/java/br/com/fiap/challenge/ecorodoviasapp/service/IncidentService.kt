package br.com.fiap.challenge.ecorodoviasapp.service

import br.com.fiap.challenge.ecorodoviasapp.domain.Incident
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface IncidentService {

    @POST("incidents")
    fun saveIncident(@Body incident: Incident): Call<Void>

    @GET("incidents/user/{userId}")
    fun getIncidentsByUserId(@Path("userId") userId: String): Call<List<Incident>>
}