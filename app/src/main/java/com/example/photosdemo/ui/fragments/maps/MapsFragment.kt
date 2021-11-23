package com.example.photosdemo.ui.fragments.maps

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.photosdemo.R
import com.example.photosdemo.data.remote.SessionManager
import com.example.photosdemo.databinding.FragmentMapsBinding
import com.example.photosdemo.viewmodel.PhotosViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.launch

class MapsFragment : Fragment(), OnMapReadyCallback  {
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var googleMap: GoogleMap
    private lateinit var viewModel: PhotosViewModel
    private lateinit var sessionManager: SessionManager
    private var bmp: Bitmap? = null

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
        sessionManager = SessionManager(requireContext())
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(gMap: GoogleMap) {
        googleMap = gMap

        viewModel.getImages(sessionManager.fetchAuthToken()!!).observe(viewLifecycleOwner, {
            if (it.data != null) {
                for (i in it.data) {
                    val latLng = LatLng(i.lat!!, i.lng!!)
                    val marker = googleMap.addMarker(MarkerOptions().position(latLng))
                    lifecycleScope.launch {
                        bmp = i.url?.let { url ->
                            getBitmap(url)
                        }
                    }
                    val finalBmp = bmp?.let {
                            bitmap -> Bitmap.createScaledBitmap(bitmap, 100, 100, false)
                    }
                    marker?.setIcon(finalBmp?.let { bitmap ->
                        BitmapDescriptorFactory.fromBitmap(bitmap)
                    })
                }
            }
        })
    }

    private suspend fun getBitmap(url: String) : Bitmap? {
        val loading = ImageLoader(requireContext())
        val request = ImageRequest.Builder(requireContext())
            .data(url)
            .build()
        val result = (loading.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}