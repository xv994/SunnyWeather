package com.example.sunnyweather.android.logic.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.example.sunnyweather.android.SunnyWeatherApplication

class dbHelper(val context: Context, name: String, val version: Int) : SQLiteOpenHelper(context, name, null, version) {

    private val createStarPlace = "create table StarPlace (" +
            "id integer primary key autoincrement," +
            "place_name text," +
            "lat text," +
            "lng text," +
            "address text" +
            ")"

    override fun onCreate(db: SQLiteDatabase?) {

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}