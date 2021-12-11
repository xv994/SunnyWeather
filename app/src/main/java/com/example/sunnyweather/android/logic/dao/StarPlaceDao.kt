package com.example.sunnyweather.android.logic.dao

import android.content.ContentValues
import com.example.sunnyweather.android.SunnyWeatherApplication
import com.example.sunnyweather.android.logic.model.Location
import com.example.sunnyweather.android.logic.model.Place
import org.jetbrains.annotations.TestOnly

object StarPlaceDao {
    private val db = dbHelper(SunnyWeatherApplication.context, "SunnyWeather.db", 2).writableDatabase

    fun insert(place: Place) {
        val values = ContentValues().apply {
            put("place_name", place.name)
            put("lng", place.location.lng)
            put("lat", place.location.lat)
            put("address", place.address)
        }
        db.insert("StarPlace", null, values)
    }

    fun delete(place: Place) {
        db.delete("StarPlace", "place_name = ?", arrayOf(place.name))
    }

    fun select() : ArrayList<Place>{
        val starPlaceList = ArrayList<Place>()

        val cursor = db.query("StarPlace", null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndex("place_name"))
                val lng = cursor.getString(cursor.getColumnIndex("lng"))
                val lat = cursor.getString(cursor.getColumnIndex("lat"))
                val address = cursor.getString(cursor.getColumnIndex("address"))
                starPlaceList.add(Place(name, Location(lng, lat), address))
            } while (cursor.moveToNext())
        }
        cursor.close()
        starPlaceList.remove(Place("BISTU", Location("0", "0"), "BISTU"))
        return starPlaceList
    }

    fun isStarPlace(place: Place): Boolean {
        val cursor = db.query("StarPlace", null, "lng = ? AND lat = ?", arrayOf(place.location.lng, place.location.lat), null, null, null)
        return cursor.moveToFirst()
    }

}