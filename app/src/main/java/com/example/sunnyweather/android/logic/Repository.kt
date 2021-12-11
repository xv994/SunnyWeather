package com.example.sunnyweather.android.logic

import androidx.lifecycle.liveData
import com.example.sunnyweather.android.logic.dao.PlaceDao
import com.example.sunnyweather.android.logic.dao.StarPlaceDao
import com.example.sunnyweather.android.logic.model.Place
import com.example.sunnyweather.android.logic.model.Weather
import com.example.sunnyweather.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

object Repository {

    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        // 返回一个PlaceResponse对象，即接口返回的信息，下同
        val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
        if (placeResponse.status == "ok") {
            val places = placeResponse.places
            Result.success(places)
        } else {
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }
    }

    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO) {
        coroutineScope {
            // async 异步执行 可节省时间
            val deferredRealtime = async {
                SunnyWeatherNetwork.getRealtimeWeather(lng, lat)
            }

            val derredDaily = async {
                SunnyWeatherNetwork.getDailyWeather(lng, lat)
            }

            val realtimeResponse = deferredRealtime.await()
            val dailyResponse = derredDaily.await()

            // 在实时天气信息和未来天气信息都获取成功后
            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                val weather = Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                Result.success(weather)
            } else {
                Result.failure(
                    RuntimeException(
                        "realtime response status is ${realtimeResponse.status}" +
                        "daily response status is ${dailyResponse.status}"
                    )
                )
            }
        }
    }

    // 为了便于抓取异常且代码优雅，利用泛型编写fire函数，接受context并发方式，block协程函数作为参数，返回Result<T>
    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }

    // 调用Dao包，存储或获取地址信息
    fun savePlace(place: Place) = PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()

    fun clearPlace() = PlaceDao.clearPlace()

    // 操作关注地址
    fun addStarPlace(place: Place) = StarPlaceDao.insert(place)

    fun deletePlace(place: Place) = StarPlaceDao.delete(place)

    fun getStarPlaceList() : ArrayList<Place> = StarPlaceDao.select()

    fun isStarPlace(place: Place) : Boolean = StarPlaceDao.isStarPlace(place)

}