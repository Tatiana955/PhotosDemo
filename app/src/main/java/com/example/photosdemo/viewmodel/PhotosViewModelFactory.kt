package com.example.photosdemo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.photosdemo.data.Repository

@Suppress("UNCHECKED_CAST")
class PhotosViewModelFactory(
    private val repository: Repository
    ): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PhotosViewModel(repository) as T
    }
}