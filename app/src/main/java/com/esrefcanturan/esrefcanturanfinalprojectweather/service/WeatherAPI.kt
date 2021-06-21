package com.esrefcanturan.esrefcanturanfinalprojectweather.service

import com.esrefcanturan.esrefcanturanfinalprojectweather.model.WeatherModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherAPI {

    @GET("data/2.5/weather?&units=metric&APPID=dd225def1721ac58ec0dec849cf05caf")
    fun getData(
        @Query("q") cityName: String
    ): Single<WeatherModel>

}