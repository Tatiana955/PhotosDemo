package com.example.photosdemo.ui.fragments.photos

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.photosdemo.BuildConfig
import com.example.photosdemo.R
import com.example.photosdemo.data.models.image.ImageDtoIn
import com.example.photosdemo.data.models.image.ImageDtoOut
import com.example.photosdemo.databinding.FragmentPhotosBinding
import com.example.photosdemo.viewmodel.PhotosViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class PhotosFragment : Fragment(), LocationListener {
    private var _binding: FragmentPhotosBinding? = null
    private val binding get() = _binding!!
    private var navController: NavController? = null
    private lateinit var viewModel: PhotosViewModel
    private lateinit var adapter: PhotosAdapterAsync
    private val photos = mutableListOf<ImageDtoOut?>()

    private var latestTmpUri: Uri? = null
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    private var latitude: Double? = null
    private var longitude: Double? = null

    private var alertDialog: AlertDialog? = null

    private val takeImageResult = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            val timeStamp = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
            getLocation()
            latestTmpUri?.let { uri ->
                val image = ImageDtoIn(
                    encode(uri),
                    timeStamp.toInt(),
                    latitude,
                    longitude
                )
                viewModel.postImageOut(image)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotosBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(requireActivity())[PhotosViewModel::class.java]
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        viewModel.getImageOut()

        adapter = PhotosAdapterAsync(this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        viewModel.photoLive.observe(requireActivity(), {
            photos.clear()
            if (it != null) {
                photos.addAll(it)
                adapter.submitList(it)
            }
        })

        binding.floatingActionButton.setOnClickListener {
            takeImage()
        }
    }

    private fun encode(imageUri: Uri): String {
        val input = activity?.contentResolver?.openInputStream(imageUri)
        val image = BitmapFactory.decodeStream(input, null, null)
        val baos = ByteArrayOutputStream()
        image?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageBytes = baos.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp.", ".jpeg").apply {
            createNewFile()
            deleteOnExit()
        }
        return FileProvider.getUriForFile(requireContext(), "${BuildConfig.APPLICATION_ID}.provider", tmpFile)
    }

    private fun getLocation() {
        locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }

    override fun onLocationChanged(location: Location) {
        latitude = location.latitude
        longitude = location.longitude
    }

    override fun onLocationChanged(locations: MutableList<Location>) {
        super.onLocationChanged(locations)
    }

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    fun navToDetails(position: Int) {
        viewModel.selectedImage = photos[position]
        navController?.navigate(R.id.detailsFragment)
    }

    fun showDeleteDialog(position: Int) {
        val builder = AlertDialog.Builder(activity)
        builder.run {
            setTitle("Delete")
            setMessage("You want to delete photo?")
            setPositiveButton("Yes") { _, _ ->
                deleteImage(position)
            }
            setNegativeButton("Cancel") { _, _ ->
            }
        }
        alertDialog = builder.create()
        alertDialog?.show()
    }

    private fun deleteImage(position: Int) {
        viewModel.deleteImageOut(photos[position]!!.id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}