package br.com.fiap.challenge.ecorodoviasapp.service

import br.com.fiap.challenge.ecorodoviasapp.domain.UserLocation
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LocationService {

    @POST("locations")
    fun save(@Body location: UserLocation): Call<Void>
}