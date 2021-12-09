package com.example.sunnyweather.android.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.sunnyweather.android.logic.Repository
import com.example.sunnyweather.android.logic.dao.PlaceDao
import com.example.sunnyweather.android.logic.model.Place

class PlaceViewModel : ViewModel() {

    private val searchLiveData = MutableLiveData<String>()

    val placeList = ArrayList<Place>()

    // 当searchLiveData改变时，调用 Repository.searchPlaces(query)
    // 返回一个LiveData<Place>对象，可供Activity观察
    // 这是因为Repository.searchPlaces(query) 返回的是一个新的LiveData对象无法观察其改变
    // Transformation.switchMap可返回一个可观察的LiveData对象
    val placeLiveData = Transformations.switchMap(searchLiveData) { query ->
        Repository.searchPlaces(query)
    }

    fun searchPlaces(query: String) {
        searchLiveData.value = query
    }

    fun savePlace(place: Place) = Repository.savePlace(place)

    fun getSavedPlace() = Repository.getSavedPlace()

    fun isPlaceSaved() = Repository.isPlaceSaved()

}