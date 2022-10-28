package com.anilcaliskan.pumperkotlin

import com.anilcaliskan.pumperkotlin.model.CoinModel
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET

//https://api.binance.com/api/v3/ticker/price

interface CoinAPI {
    @GET("api/v3/ticker/price")
    fun getData(): Observable<List<CoinModel>>

}