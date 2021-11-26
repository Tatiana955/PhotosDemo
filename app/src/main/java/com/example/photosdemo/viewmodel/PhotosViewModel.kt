package com.example.photosdemo.viewmodel

import androidx.lifecycle.*
import com.example.photosdemo.data.Repository
import com.example.photosdemo.data.models.image.ImageDtoIn
import com.example.photosdemo.data.models.image.ImageDtoOut
import com.example.photosdemo.data.models.security.SignUserDtoIn
import com.example.photosdemo.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PhotosViewModel(
    private val repository: Repository
    ): ViewModel() {

    private val scope = CoroutineScope(Dispatchers.IO)
    val userToken: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    private val pageSize: Int = 0
    var selectedImage: ImageDtoOut? = null

    fun postSignUp(userDto: SignUserDtoIn) {
        scope.launch {
            val data = repository.postSignUp(userDto)
            val token = data?.data?.token
            userToken.postValue(token)
        }
    }

    fun postSignIn(userDto: SignUserDtoIn) {
        scope.launch {
            val data = repository.postSignIn(userDto)
            val token = data?.data?.token
            userToken.postValue(token)
        }
    }

    fun getImages(token: String): LiveData<Resource<MutableList<ImageDtoOut>>> {
        val data = repository.getImageOut(token, pageSize)
        return data.asLiveData()
    }

    fun postImageOut(imageDtoIn: ImageDtoIn) {
        scope.launch {
            userToken.value?.let { repository.postImageOut(it, imageDtoIn, pageSize) }
        }
    }

    fun deleteImageOut(id: Int) {
        scope.launch {
            repository.deleteImageOut(userToken.value!!, id)
        }
    }
}
