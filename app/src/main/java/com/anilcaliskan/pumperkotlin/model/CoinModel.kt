package com.anilcaliskan.pumperkotlin.model

import com.google.gson.annotations.SerializedName


data class CoinModel(
    @SerializedName("symbol")
    var coinName: String = "",

    val refPoint: Double = 0.0,

    @SerializedName("price")
    var currentPoint: Double = 0.0,

    val percentage: Double = 0.0


) {
}