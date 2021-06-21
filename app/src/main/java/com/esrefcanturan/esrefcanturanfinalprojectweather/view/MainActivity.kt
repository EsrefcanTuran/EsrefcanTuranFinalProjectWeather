package com.esrefcanturan.esrefcanturanfinalprojectweather.view


import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.esrefcanturan.esrefcanturanfinalprojectweather.R
import com.esrefcanturan.esrefcanturanfinalprojectweather.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var viewmodel: MainViewModel
    private lateinit var GET: SharedPreferences
    private lateinit var SET: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GET = getSharedPreferences(packageName, MODE_PRIVATE)
        SET = GET.edit()

        viewmodel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        val cName = GET.getString("cityName", "istanbul")?.lowercase()
        edt_city.setText(cName)
        viewmodel.refreshData(cName!!)

        getLiveData()

        swipeRefreshLayout.setOnRefreshListener {
            ll_data.visibility = View.GONE
            txt_error.visibility = View.GONE
            pb_loading.visibility = View.GONE

            val cityName = GET.getString("cityName", cName)?.lowercase()
            edt_city.setText(cityName)
            viewmodel.refreshData(cityName!!)
            swipeRefreshLayout.isRefreshing = false
        }

        img_search_city.setOnClickListener {
            val cityName = edt_city.text.toString()
            SET.putString("cityName", cityName)
            SET.apply()
            viewmodel.refreshData(cityName)
            getLiveData()
            Log.i(TAG, "onCreate: $cityName")
        }

    }

    private fun getLiveData() {

        viewmodel.weatherData.observe(this, { data ->
            data?.let {
                ll_data.visibility = View.VISIBLE

                txt_country.text = data.sys.country
                txt_city.text = data.name

                Glide.with(this)
                    .load("https://openweathermap.org/img/wn/" + data.weather[0].icon + "@2x.png")
                    .into(img_weather_pictures)

                txt_degree.text = data.main.temp.toString() + "°C"
                txt_desc.text = data.weather[0].description
                txt_lat.text = data.coord.lat.toString()
                txt_lon.text = data.coord.lon.toString()
                txt_humidity.text = data.main.humidity.toString() + "%"
                txt_windspeed.text = data.wind.speed.toString()

            }
        })

        viewmodel.weatherError.observe(this, { error ->
            error?.let {
                if (error) {
                    txt_error.visibility = View.VISIBLE
                    pb_loading.visibility = View.GONE
                    ll_data.visibility = View.GONE
                } else {
                    txt_error.visibility = View.GONE
                }
            }
        })

        viewmodel.weatherLoading.observe(this, { loading ->
            loading?.let {
                if (loading) {
                    pb_loading.visibility = View.VISIBLE
                    txt_error.visibility = View.GONE
                    ll_data.visibility = View.GONE
                } else {
                    pb_loading.visibility = View.GONE
                }
            }
        })

    }
}