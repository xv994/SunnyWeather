package com.example.sunnyweather.android.ui.weather

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.sunnyweather.android.logic.Repository
import com.example.sunnyweather.android.logic.model.Location
import com.example.sunnyweather.android.logic.model.Place

class WeatherViewModel : ViewModel() {

    private val locationLiveData = MutableLiveData<Location>()
    // 经度
    var locationLng = ""
    // 纬度
    var locationLat = ""

    var placeName = ""

    var placeAddress = ""

    var starPlaceList = Repository.getStarPlaceList()

    val starPlaceSize = MutableLiveData(starPlaceList.size)

    // 当locationLiveData的值改变时，调用 Repository.refreshWeather(location.lng, location.lat)
    // 同时返回一个LiveData<Weather>对象，可在Activity中观察
    val weatherLiveData = Transformations.switchMap(locationLiveData) {location ->
        Repository.refreshWeather(location.lng, location.lat)
    }

    fun refreshWeather(lng: String, lat: String) {
        locationLiveData.value = Location(lng, lat)
    }

    fun clearPlace() = Repository.clearPlace()

    fun addStarPlace(place: Place) {
        Repository.addStarPlace(place)

    }

    fun deletePlace(place: Place) = Repository.deletePlace(place)

    fun isStarPlace() = Repository.isStarPlace(Place(placeName, Location(locationLng, locationLat), placeAddress))

    fun refreshStarPlace() {
        starPlaceList = Repository.getStarPlaceList()
        Log.d("liveData", starPlaceSize.value.toString())
        Log.d("liveData", starPlaceSize.hasObservers().toString())
        Log.d("liveData", starPlaceSize.hasActiveObservers().toString())
    }

}