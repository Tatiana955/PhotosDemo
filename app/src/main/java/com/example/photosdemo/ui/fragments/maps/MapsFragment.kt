package com.example.photosdemo.ui.fragments.maps

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.photosdemo.R
import com.example.photosdemo.data.models.image.ImageDtoOut
import com.example.photosdemo.databinding.FragmentMapsBinding
import com.example.photosdemo.viewmodel.PhotosViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MapsFragment : Fragment(), OnMapReadyCallback  {
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var googleMap: GoogleMap
    private lateinit var viewModel: PhotosViewModel
    private val photos = mutableListOf<ImageDtoOut?>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(requireActivity())[PhotosViewModel::class.java]
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        viewModel.photoLive.observe(viewLifecycleOwner, {
            photos.clear()
            if (it != null) {
                photos.addAll(it)
            }
        })
    }

    override fun onMapReady(gMap: GoogleMap) {
        googleMap = gMap

        for (i in photos) {
            val latLng = LatLng(i?.lat!!, i.lng!!)
            googleMap.addMarker(MarkerOptions().position(latLng))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}