package com.weatherapp.ui.map

import android.os.Bundle
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.weatherapp.R
import com.weatherapp.data.model.Coordinates
import com.weatherapp.databinding.FragmentMapsBinding
import com.weatherapp.databinding.FragmentMapsContainerBinding


class MapsFragment : Fragment() {

    private var _binding: FragmentMapsContainerBinding? = null
    private val binding get() = _binding!!

    private var marker: Marker? = null
    private lateinit var doneBtn : FloatingActionButton


    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        googleMap.setOnMapClickListener { clickOnMap ->
            if (marker!=null){
                marker?.remove()
            }
            marker = googleMap.addMarker(
                MarkerOptions()
                    .position(LatLng(clickOnMap.latitude, clickOnMap.longitude))
                    .title("Current position")
            )
        }

        doneBtn.setOnClickListener {
            if (marker != null){
                val coordinates =
                    Coordinates(marker!!.position.latitude, marker!!.position.longitude)
                val bundle = Bundle()
                bundle.putSerializable("coordinates", coordinates)
                view?.let { view ->
                    Navigation.findNavController(view).navigate(R.id.action_mapsFragment_to_homeFragment, bundle)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsContainerBinding.inflate(inflater, container, false)
        doneBtn = binding.doneBtn
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}