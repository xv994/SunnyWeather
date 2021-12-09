package com.example.sunnyweather.android.logic.network

import com.example.sunnyweather.android.SunnyWeatherApplication
import com.example.sunnyweather.android.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {

    // 获取地址信息接口，传递参数 "token"&"lang"&"query" 其中"query"为地址名称，是可变参数，其余两个皆为固定不变的参数
    // 返回值为Call<PlaceResponse> 可自动拆箱为PlaceResponse类型
    @GET("v2/place?token=${SunnyWeatherApplication.TOKEN}&lang=zh_CN")
    fun searchPlaces(@Query("query") query: String) : Call<PlaceResponse>

}