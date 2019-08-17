package com.mavenusrs.weather.presentation.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mavenusrs.weather.R
import com.mavenusrs.weather.common.getDegreeWithCelsiusSympol
import com.mavenusrs.weather.common.toDayOfTheWeek
import com.mavenusrs.weather.model.ForecastdayViewModel
import java.text.SimpleDateFormat
import java.util.*

class WeatherAdapter(private val forcastDays: List<ForecastdayViewModel>) : RecyclerView.Adapter<WeatherAdapter.WeatherVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sheet_item, parent, false)
        return WeatherVH(view)
    }

    override fun getItemCount(): Int {
        forcastDays.apply {
            return this.size
        }
    }

    override fun onBindViewHolder(holder: WeatherVH, position: Int) {
        forcastDays.apply {
            holder.bind(this[position])
        }
    }


    class WeatherVH(viewItem: View) : RecyclerView.ViewHolder(viewItem) {

        val dayOfWeekTV: TextView = viewItem.findViewById(R.id.dayOfWeekTV)
        val degreeTV: TextView = viewItem.findViewById(R.id.degreeTV)


        fun bind(forecastday: ForecastdayViewModel) {
            dayOfWeekTV.text = forecastday.date.toDayOfTheWeek()
            degreeTV.text = forecastday.dayAvgtempC.getDegreeWithCelsiusSympol()

        }


    }
}

