package com.example.photosdemo.viewmodel

import androidx.lifecycle.*
import com.example.photosdemo.data.Repository
import com.example.photosdemo.data.models.image.ImageDtoIn
import com.example.photosdemo.data.models.image.ImageDtoOut
import com.example.photosdemo.data.models.security.SignUserDtoIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PhotosViewModel(
    private val repository: Repository
    ): ViewModel() {

    private val scope = CoroutineScope(Dispatchers.IO)
    val photoLive: MutableLiveData<MutableList<ImageDtoOut?>?> by lazy {
        MutableLiveData<MutableList<ImageDtoOut?>?>()
    }
    val userToken: MutableLiveData<String?> by lazy {
        MutableLiveData<String?>()
    }
    private val pageSize: Int = 0
    var selectedImage: ImageDtoOut? = null

    fun postSignUp(userDto: SignUserDtoIn) {
        scope.launch {
            repository.postSignUp(userDto)
        }
    }

    fun postSignIn(userDto: SignUserDtoIn) {
        scope.launch {
            repository.postSignIn(userDto)
        }
    }

    fun getImageOut() {
        scope.launch {
            val data = userToken.value?.let {
                repository.getImageOut(
                    it,
                    pageSize
                )
            }
            photoLive.postValue(data?.toMutableList())
        }
    }

    fun postImageOut(imageDtoIn: ImageDtoIn) {
        scope.launch {
            repository.postImageOut(userToken.value!!, imageDtoIn)
            getImageOut()
        }
    }

    fun deleteImageOut(id: Int) {
        scope.launch {
            repository.deleteImageOut(userToken.value!!, id)
            getImageOut()
        }
    }

    fun getToken() {
        scope.launch {
             val data = repository.getToken()
            userToken.postValue(data)
        }
    }
}
