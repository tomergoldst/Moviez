package com.tomergoldst.moviez.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import com.google.gson.GsonBuilder
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory


object RetrofitClient {

    private const val BASE_URL = "https://api.themoviedb.org/3/"

    var retrofit: Retrofit? = null
        private set(value){
            field = value
        }

    init {
        val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        val client : OkHttpClient = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
        }.build()

        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .create()

        retrofit =  Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            //.client(client)
            .build()
    }

    fun getMoviesService(): MoviesService = retrofit!!.create(MoviesService::class.java)


}