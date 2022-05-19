package com.weatherapp.ui.home

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener
import com.weatherapp.R
import com.weatherapp.app.location.FindLocationClass
import com.weatherapp.app.mappers.toThreeHourModel
import com.weatherapp.app.resource.Resource
import com.weatherapp.data.dto.FiveDayWeather
import com.weatherapp.data.model.Coordinates
import com.weatherapp.data.model.ThreeHourModel
import com.weatherapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var findLocationClass: FindLocationClass
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var city: TextView
    private lateinit var cityViewFlipper: ViewFlipper
    private lateinit var enterCity: EditText
    private lateinit var adapter: WeatherAdapter
    private lateinit var weatherList: RecyclerView
    private lateinit var connectionProblem: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        askPermissionAndTryToUpdateLocation()
        initViews()
        showContent()
        return binding.root
    }

    private fun initViews() {
        connectionProblem = binding.connectionProblemHome
        progressBar = binding.progressBarHome
        city = binding.showCity.city
        cityViewFlipper = binding.viewFlipper
        enterCity = binding.chooseCity.enterCity
        weatherList = binding.recyclerView
        activity?.let { adapter = WeatherAdapter(it) }
        weatherList.adapter = adapter
        binding.showCity.switchToSearchCity.setOnClickListener(this)
        binding.chooseCity.btnSwitchToShowCity.setOnClickListener(this)
        binding.chooseCity.searchCity.setOnClickListener(this)
        binding.chooseCity.enableGps.setOnClickListener(this)
    }

    private fun showContent() {
        lifecycleScope.launch() {
            viewModel.fiveDayWeather.collectLatest { it ->
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        it.data?.let { fiveDayWeather ->
                            showSuccess(fiveDayWeather)
                        }
                        connectionProblem.text = viewModel.errorText
                    }
                    Resource.Status.LOADING -> {
                        showLoading()
                        connectionProblem.text = viewModel.errorText
                    }
                    Resource.Status.FAILED -> {
                        showFailed()
                        connectionProblem.text = viewModel.errorText
                    }
                    Resource.Status.PENDING -> {
                        showPending()
                        connectionProblem.text = viewModel.errorText
                    }
                }
            }
        }

    }

    private fun showSuccess(fiveDayWeather: FiveDayWeather) {
        val listOfWeather = ArrayList<ThreeHourModel>()

        viewModel.fiveDayWeather.value.data?.list?.let { list ->
            for (model in list) {
                listOfWeather.add(model.toThreeHourModel())
            }
        }
        adapter.submitList(listOfWeather)
        city.text = fiveDayWeather.city.name
        if (cityViewFlipper.displayedChild == 1){
            cityViewFlipper.displayedChild = 0
        }

        connectionProblem.visibility = GONE
        progressBar.visibility = GONE

    }

    private fun showPending() {
        connectionProblem.visibility = VISIBLE
        progressBar.visibility = GONE
    }

    private fun showFailed() {
        connectionProblem.visibility = VISIBLE
        progressBar.visibility = GONE
    }

    private fun showLoading() {
        connectionProblem.visibility = GONE
        progressBar.visibility = VISIBLE
    }

    private fun showSnackBar(string: String) {
        view?.let {
            val snackBar =
                Snackbar.make(
                    it,
                    string,
                    Snackbar.LENGTH_SHORT
                )
            snackBar.show()
        }
    }
    private fun askPermissionAndTryToUpdateLocation() {
        val listener = DialogOnDeniedPermissionListener.Builder
            .withContext(activity)
            .withTitle("Location")
            .withMessage("We can autodetect your city")
            .build()

        Dexter.withContext(activity)
            .withPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            .withListener(listener)
            .check()

        takeLocationFromAndroid()
    }

    private fun takeLocationFromAndroid() {
        activity?.let {
            if (
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                == PermissionChecker.PERMISSION_GRANTED
            ) {
                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(it)
                val task = fusedLocationClient.lastLocation

                task.addOnSuccessListener { location ->
                    loadWeather(
                        Coordinates(
                            location.latitude.toInt().toString(),
                            location.longitude.toInt().toString()
                        )
                    )
                }

                task.addOnFailureListener {
                    showSnackBar("Please enter your city or enable GPS")
                }
            }
        }
    }

    private fun loadWeather(coordinates: Coordinates) {
        viewModel.updateCoordinates(
            coordinates
        )
        viewModel.loadData()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.switch_to_search_city -> cityViewFlipper.displayedChild = 1
            R.id.btn_switch_to_show_city -> cityViewFlipper.displayedChild = 0

            R.id.search_city -> activity?.let {
                try{
                    findLocationClass.search(enterCity.text.toString(),
                        it
                    )
                } catch (e : Exception){
                    e.message?.let { it1 -> showSnackBar(it1) }
                }
            }

            R.id.enable_gps -> askPermissionAndTryToUpdateLocation()
        }
    }

}