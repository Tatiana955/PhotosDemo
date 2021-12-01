package com.example.photosdemo.ui.fragments.photos

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
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
import com.example.photosdemo.data.remote.SessionManager
import com.example.photosdemo.databinding.FragmentPhotosBinding
import com.example.photosdemo.viewmodel.PhotosViewModel
import java.io.ByteArrayOutputStream
import java.io.File

class PhotosFragment : Fragment(), LocationListener {
    private var _binding: FragmentPhotosBinding? = null
    private val binding get() = _binding!!
    private var navController: NavController? = null
    private lateinit var viewModel: PhotosViewModel
    private lateinit var sessionManager: SessionManager
    private var token: String? = null
    private lateinit var adapter: PhotosAdapter
    private val photos = mutableListOf<ImageDtoOut?>()

    private var latestTmpUri: Uri? = null
    private var latitude: Double? = null
    private var longitude: Double? = null

    private var alertDialog: AlertDialog? = null

    private val takeImageResult = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            val date = System.currentTimeMillis() / 1000
            latestTmpUri?.let { uri ->
                val image = ImageDtoIn(
                    encode(uri),
                    date,
                    latitude,
                    longitude
                )
                viewModel.postImageOut(image, token!!)
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
        sessionManager = SessionManager(requireContext())
        token = sessionManager.fetchAuthToken()
        adapter = PhotosAdapter(this)

        binding.apply {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

            if (token != null) {
                viewModel.getImages(token!!).observe(viewLifecycleOwner) { result ->
                    photos.clear()
                    result.data?.let { photos.addAll(it) }
                    adapter.submitList(result.data)
                }
            } else {
                navController?.navigate(R.id.loginFragment)
            }

            floatingActionButton.setOnClickListener {
                takeImage()
            }
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
        viewModel.deleteImageOut(photos[position]!!.id, token!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}