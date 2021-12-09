package com.example.sunnyweather.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class SunnyWeatherApplication : Application() {
    companion object {
        // 彩云天气API令牌
        const val TOKEN = "M0X7mVg5VchyILlH"

        // 便于代码编写时获取上下文
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}