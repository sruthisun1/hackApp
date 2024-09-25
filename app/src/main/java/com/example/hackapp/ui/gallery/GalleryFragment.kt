package com.example.hackapp.ui.gallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.hackapp.R
import com.example.hackapp.databinding.FragmentGalleryBinding
import com.example.hackapp.viewmodel.EventViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLngBounds

class GalleryFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentGalleryBinding? = null //view binding, access views with id
    private val binding get() = _binding!! //ref to binding

    private lateinit var eventViewModel: EventViewModel //instance of EventViewModel
    private lateinit var map: GoogleMap

    override fun onCreateView( //binds layout
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        eventViewModel = ViewModelProvider(requireActivity()).get(EventViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //after view created, sets up map
        super.onViewCreated(view, savedInstanceState)
        Log.d("GalleryFragment", "onViewCreated called")

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        eventViewModel.loadEventsIfNeeded() //load event data that hasn't been loaded
    }

    override fun onMapReady(googleMap: GoogleMap) { //observes events and add markers
        map = googleMap

        // Add static marker for testing
//        val testLocation = LatLng(40.113807, -88.224932)
//        map.addMarker(MarkerOptions().position(testLocation).title("Test Location"))
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(testLocation, 15f))

        eventViewModel.events.observe(viewLifecycleOwner) { events ->
            Log.d("GalleryFragment", "Received ${events.size} events")
            if (events.isEmpty()) {
                Log.d("GalleryFragment", "No events to display.")
            }

            val boundsBuilder = LatLngBounds.Builder()

            events.forEach { event ->
                Log.d("GalleryFragment", "Processing event: ${event.name}")
                event.locations.forEach { location ->
                    Log.d("GalleryFragment", "Location: ${location.description}, Lat: ${location.latitude}, Lng: ${location.longitude}")
                    if (location.latitude != 0.0 && location.longitude != 0.0) {
                        val latLng = LatLng(location.latitude, location.longitude)
                        Log.d("GalleryFragment", "Adding marker for ${event.name} at lat=${location.latitude}, lng=${location.longitude}")
                        map.addMarker(
                            MarkerOptions()//adding marker based on LatLng(location.latitude, location.longitude)
                                .position(latLng)
                                .title(event.name)
                                .snippet(location.description)
                        )
                        boundsBuilder.include(latLng)
                    } else {
                        Log.d("GalleryFragment", "Skipping invalid location for ${event.name}")
                    }
                }
            }

            // Adjust camera to show all markers
            if (::map.isInitialized) {
                val bounds = boundsBuilder.build()
                val padding = 100 // Adjust padding value as needed
                map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
            } else {
                Log.d("GalleryFragment", "Map not initialized")
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
