package com.example.photosdemo.ui

import android.Manifest
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.photosdemo.R
import com.example.photosdemo.databinding.ActivityMainBinding
import com.example.photosdemo.viewmodel.PhotosViewModel
import com.example.photosdemo.viewmodel.PhotosViewModelFactory
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList
import java.util.HashMap
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var navController: NavController? = null
    private lateinit var viewModel: PhotosViewModel
    private val locationPermissionCode = 2
    @Inject
    lateinit var factory: PhotosViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbar)
        navController = supportFragmentManager.findFragmentById(R.id.navHost)?.findNavController()
        setupActionBarWithNavController(navController!!, binding.drawerLayout)
        binding.toolbar.setupWithNavController(navController!!, binding.drawerLayout)
        binding.navView.setupWithNavController(navController!!)

        viewModel = ViewModelProvider(this, factory)[PhotosViewModel::class.java]

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            checkAndAddPermission()
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun addPermission(permissionsList: MutableList<String>, permission: String): Boolean {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission)
            if (!shouldShowRequestPermissionRationale(permission)) return false
        }
        return true
    }

    private fun checkAndAddPermission() {
        val permissionsNeeded: MutableList<String> = ArrayList()
        val permissionsList: MutableList<String> = ArrayList()
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION)) permissionsNeeded.add("GPS")
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION)) permissionsNeeded.add("Coarse")
        if (permissionsList.size > 0) {
            if (permissionsNeeded.size > 0) {
                var message = "You need to grant access to " + permissionsNeeded[0]
                for (i in 1 until permissionsNeeded.size) message = message + ", " + permissionsNeeded[i]
                ActivityCompat.requestPermissions(this, permissionsList.toTypedArray(), locationPermissionCode)
            } else {
                ActivityCompat.requestPermissions(this, permissionsList.toTypedArray(), locationPermissionCode)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            locationPermissionCode -> {
                val perms: MutableMap<String, Int> = HashMap()
                perms[Manifest.permission.ACCESS_FINE_LOCATION] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.ACCESS_COARSE_LOCATION] = PackageManager.PERMISSION_GRANTED
                var i = 0
                while (i < permissions.size) {
                    perms[permissions[i]] = grantResults[i]
                    i++
                }
                if (perms[Manifest.permission.ACCESS_FINE_LOCATION] == PackageManager.PERMISSION_GRANTED
                    && perms[Manifest.permission.ACCESS_COARSE_LOCATION] == PackageManager.PERMISSION_GRANTED
                ) {
                    Log.d("!!!", "All permissions granted")
                } else {
                    Snackbar.make(
                        binding.navHost,
                        "Some Permission is Denied",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}