package com.example.sunnyweather.android.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.example.sunnyweather.android.SunnyWeatherApplication
import com.example.sunnyweather.android.logic.model.Place
import com.google.gson.Gson

object PlaceDao {

    // 用Gson将地址信息转换为字符串形式，放入SharedPreferences保存
    fun savePlace(place: Place) {
        sharedPreferences().edit {
            putString("place", Gson().toJson(place))
        }
    }

    // 将保存的地址信息用Gson转化为Place类
    fun getSavedPlace() : Place {
        val placeJson = sharedPreferences().getString("place", "")
        return Gson().fromJson(placeJson, Place::class.java)
    }

    // 判断有无保存的地址信息
    fun isPlaceSaved() = sharedPreferences().contains("place")

    // 清除sharedPreferences
    fun clearPlace() = sharedPreferences().edit {
        this.clear()
    }

    // 获取SharedPreferences
    private fun sharedPreferences() = SunnyWeatherApplication.context.getSharedPreferences("sunny_weather", Context.MODE_PRIVATE)

}