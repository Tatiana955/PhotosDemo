package com.example.photosdemo.viewmodel

import androidx.lifecycle.*
import com.example.photosdemo.data.Repository
import com.example.photosdemo.data.models.comment.CommentDtoIn
import com.example.photosdemo.data.models.comment.CommentDtoOut
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

    fun postImageOut(imageDtoIn: ImageDtoIn, token: String) {
        scope.launch {
            repository.postImageOut(token, imageDtoIn, pageSize)
        }
    }

    fun deleteImageOut(id: Int, token: String) {
        scope.launch {
            repository.deleteImageOut(token, id)
        }
    }

    fun getComments(token: String): LiveData<Resource<MutableList<CommentDtoOut>>> {
        val data = repository.getComments(token, selectedImage!!.id, pageSize)
        return data.asLiveData()
    }

    fun postComment(commentDtoIn: CommentDtoIn, token: String) {
        scope.launch {
            repository.postComment(token, commentDtoIn, selectedImage!!.id, pageSize)
        }
    }

    fun deleteComment(commentId: Int, token: String) {
        scope.launch {
            repository.deleteComment(token, commentId, selectedImage!!.id)
        }
    }
}
