package com.example.sunnyweather.android.ui.star_place

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sunnyweather.R
import com.example.sunnyweather.android.logic.Repository
import com.example.sunnyweather.android.ui.weather.WeatherViewModel
import kotlinx.android.synthetic.main.fragment_star_place.*

class StarPlaceFragment : Fragment() {

    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }
    private lateinit var adapter: StarPlaceAdapter
//    val starPlaceSizeInFragment = MutableLiveData(viewModel.starPlaceList.size)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_star_place, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val layoutManager = LinearLayoutManager(activity)
        recyclerViewFromStarPlace.layoutManager = layoutManager
        adapter = StarPlaceAdapter(this, viewModel.starPlaceList)
        recyclerViewFromStarPlace.adapter = adapter

    }

    override fun onResume() {
        super.onResume()
        adapter = StarPlaceAdapter(this, Repository.getStarPlaceList())
        recyclerViewFromStarPlace.adapter = adapter
        Log.d("fragment", "resume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("fragment", "pause")
    }

}