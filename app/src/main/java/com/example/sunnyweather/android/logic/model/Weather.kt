package com.example.sunnyweather.android.logic.model

// Weather类，包含实时天气和未来天气
data class Weather(val realtime: RealtimeResponse.Realtime, val daily: DailyResponse.Daily)