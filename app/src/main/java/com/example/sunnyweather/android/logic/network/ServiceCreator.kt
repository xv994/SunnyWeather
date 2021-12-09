package com.example.sunnyweather.android.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {

    // API接口网址
    private const val BASE_URL = "https://api.caiyunapp.com"

    // 创建Retrofit对象，确立根网址和信息处理器Gson
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // 通过此方法可建立一个service动态代理对象
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    // 内联函数inline 编译器将代码直接传到调用处，可提高性能
    // 使具体化 关键字reified create(T::class.java) 等价于 create<T>()
    inline fun <reified T> create(): T = create(T::class.java)

}