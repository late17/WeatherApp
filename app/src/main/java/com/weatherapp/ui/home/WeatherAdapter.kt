package com.weatherapp.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weatherapp.R
import com.weatherapp.data.model.ThreeHourModel


class WeatherAdapter(context: Context) :
    ListAdapter<ThreeHourModel, WeatherAdapter.WeatherViewHolder>(DiffWeatherCallback())
{
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        return WeatherViewHolder(layoutInflater.inflate(R.layout.weather_item, parent, false))
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class WeatherViewHolder(private val itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val hour : TextView = itemView.findViewById(R.id.hour)
        private val temperature : TextView = itemView.findViewById(R.id.temperature)
        private val weather : TextView = itemView.findViewById(R.id.weather)

        fun bind(model: ThreeHourModel) {
            hour.text = model.hour
            temperature.text = model.temperature
            weather.text = model.weather
        }
    }
}

class DiffWeatherCallback() : DiffUtil.ItemCallback<ThreeHourModel>() {

    override fun areItemsTheSame(oldItem: ThreeHourModel, newItem: ThreeHourModel): Boolean {
        return oldItem.areItemsTheSame(newItem)
    }

    override fun areContentsTheSame(oldItem: ThreeHourModel, newItem: ThreeHourModel): Boolean {
        return oldItem == newItem
    }

}