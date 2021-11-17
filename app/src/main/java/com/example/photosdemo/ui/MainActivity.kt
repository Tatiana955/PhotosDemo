package com.example.photosdemo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.photosdemo.R
import com.example.photosdemo.databinding.ActivityMainBinding
import com.example.photosdemo.viewmodel.PhotosViewModel
import com.example.photosdemo.viewmodel.PhotosViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var navController: NavController? = null
    private lateinit var viewModel: PhotosViewModel
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
    }
}