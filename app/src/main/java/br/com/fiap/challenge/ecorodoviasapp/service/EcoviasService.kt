package br.com.fiap.challenge.ecorodoviasapp.service

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class EcoviasService {

    companion object {
        private const val API_BASE_URL = "http://packback.brazilsouth.cloudapp.azure.com:8081/ecovias/api/"
    }

    private val builder: Retrofit.Builder = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .addConverterFactory(JacksonConverterFactory.create())

    private var retrofit = builder.build()

    private val httpClient = OkHttpClient.Builder()

    fun <S : Any> create(serviceClass: Class<S>): S? {

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        httpClient.addInterceptor(loggingInterceptor)

        builder.client(httpClient.build())

        retrofit = builder.build()

        return retrofit?.create(serviceClass)
    }
}