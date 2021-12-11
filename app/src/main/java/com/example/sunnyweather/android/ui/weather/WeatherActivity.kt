package com.example.sunnyweather.android.ui.weather

import android.content.Intent
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sunnyweather.MainActivity
import com.example.sunnyweather.R
import com.example.sunnyweather.android.logic.model.Location
import com.example.sunnyweather.android.logic.model.Place
import com.example.sunnyweather.android.logic.model.Weather
import com.example.sunnyweather.android.logic.model.getSky
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.forecast.*
import kotlinx.android.synthetic.main.life_index.*
import kotlinx.android.synthetic.main.now.*
import java.util.*

class WeatherActivity : AppCompatActivity() {

    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT


        setContentView(R.layout.activity_weather)

        // 查询有无存储的城市，若有，则直接显示
        if (viewModel.locationLng.isEmpty())
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""

        if (viewModel.locationLat.isEmpty())
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""

        if (viewModel.placeName.isEmpty())
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""

        if (viewModel.placeAddress.isEmpty())
            viewModel.placeAddress = intent.getStringExtra("place_address") ?: ""

        // 当weatherLiveData改变时，更新页面
        viewModel.weatherLiveData.observe(this, Observer { result ->
            val weather = result.getOrNull()
            if (weather != null) {
                showWeatherInfo(weather)
            } else {
                Toast.makeText(this, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
            // 刷新状态关闭
            swipRefresh.isRefreshing = false
        })

        swipRefresh.setColorSchemeResources(R.color.colorPrimary)
        // 打开Activity自动更新weather
        refreshWeather()

        // 下拉刷新
        swipRefresh.setOnRefreshListener {
            refreshWeather()
        }

        // 状态栏按钮
        navBtn.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        if (viewModel.isStarPlace()) {
            starBtn.background = resources.getDrawable(R.drawable.ic_star)
        }

        starBtn.setOnClickListener {
            if (starBtn.background.constantState!! == resources.getDrawable(R.drawable.ic_star).constantState) {
                starBtn.background = resources.getDrawable(R.drawable.ic_star_border)
                viewModel.deletePlace(Place(viewModel.placeName, Location(viewModel.locationLng, viewModel.locationLat), viewModel.placeAddress))
                Toast.makeText(this, "取消收藏", Toast.LENGTH_SHORT).show()
            } else {
                starBtn.background = resources.getDrawable(R.drawable.ic_star)
                viewModel.addStarPlace(Place(viewModel.placeName, Location(viewModel.locationLng, viewModel.locationLat), viewModel.placeAddress))
                Toast.makeText(this, "加入收藏", Toast.LENGTH_SHORT).show()
            }
            viewModel.starPlaceSize.value = viewModel.starPlaceList.size
        }

        searchBtn.setOnClickListener {
            viewModel.clearPlace()
            startActivity(Intent(this, MainActivity::class.java))
        }

        // 添加状态栏监听器
        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerOpened(drawerView: View) {
            }

            // 当关闭状态栏时，关闭屏幕输入法
            override fun onDrawerClosed(drawerView: View) {
                starPlaceFragment.onPause()
            }

            override fun onDrawerStateChanged(newState: Int) {
                starPlaceFragment.onResume()
            }

        })

        viewModel.starPlaceSize.observe(this, Observer {
            viewModel.refreshStarPlace()
        })

    }

    // 刷新weather
    fun refreshWeather() {
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
        // 刷新状态开启
        swipRefresh.isRefreshing = true
    }

    // 显示天气信息，此函数的操作为填充数据
    private fun showWeatherInfo(weather: Weather) {
        placeName.text = viewModel.placeName
        val realtime = weather.realtime
        val daily = weather.daily

        // 填充now.xml布局中的数据
        val currentTemperature = "${realtime.temperature}℃"
        currentTemp.text = currentTemperature
        currentSky.text = getSky(realtime.skycon).info
        val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
        currentAQI.text = currentPM25Text
        nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)

        // 填充forecast.xml布局中的数据
        forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]

            // 获取列表项布局
            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false)
            val dateInfo = view.findViewById(R.id.dateInfo) as TextView
            val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
            val skyInfo = view.findViewById(R.id.skyInfo) as TextView
            val temperatureInfo = view.findViewById(R.id.temperatureInfo) as TextView

            // 填充列表数据
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateInfo.text = simpleDateFormat.format(simpleDateFormat.parse(skycon.date))

            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            val tempText = "${temperature.min.toInt()}"
            temperatureInfo.text = tempText

            forecastLayout.addView(view)
        }

        // 填充life_index布局中的数据
        val lifeIndex = daily.lifeIndex
        coldRiskText.text = lifeIndex.coldRisk[0].desc
        dressingText.text = lifeIndex.dressing[0].desc
        ultravioletText.text = lifeIndex.ultraviolet[0].desc
        carWashingText.text = lifeIndex.carWashing[0].desc
        weatherLayout.visibility = View.VISIBLE

    }
}