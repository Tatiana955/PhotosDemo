package com.example.photosdemo.data

import android.util.Log
import com.example.photosdemo.data.local.PhotosDatabase
import com.example.photosdemo.data.models.image.ImageDtoIn
import com.example.photosdemo.data.models.image.ImageDtoOut
import com.example.photosdemo.data.models.security.ResponseDto
import com.example.photosdemo.data.models.security.SignUserDtoIn
import com.example.photosdemo.data.models.security.SignUserOutDto
import com.example.photosdemo.data.remote.ApiService
import java.lang.Exception
import javax.inject.Inject

class Repository @Inject constructor(
    private val apiService: ApiService,
    private val database: PhotosDatabase
    )
{
    suspend fun postSignUp(userDto: SignUserDtoIn): ResponseDto? {
        return try {
            val data = apiService.postSignUp(userDto)
            insertToken(data?.data)
            data
        } catch (e: Exception) {
            Log.d("!!!e_postSignUp", e.toString())
            null
        }
    }

    suspend fun postSignIn(userDto: SignUserDtoIn): ResponseDto? {
        return try {
            val data = apiService.postSignIn(userDto)
            insertToken(data?.data)
            data
        } catch (e: Exception) {
            Log.d("!!!e_postSignIn", e.toString())
            null
        }
    }

    suspend fun getImageOut(access_token: String, page: Int): MutableList<ImageDtoOut> {
        return try {
            val data = apiService.getImageOut(access_token, page)
            data?.data?.let {
                database.photosDao().insertAllImageDtoOut(it)
            }
            val localData = database.photosDao().getImageDtoOutList()
            localData
        } catch (e: Exception){
            Log.d("!!!e_getImage", e.toString())
            mutableListOf()
        }
    }

    suspend fun postImageOut(access_token: String, imageDtoIn: ImageDtoIn) {
        try {
            apiService.postImageOut(access_token, imageDtoIn)
            getImageOut(access_token, 0)
        } catch (e: Exception){
            Log.d("!!!e_postImage", e.toString())
        }
    }

    suspend fun deleteImageOut(access_token: String, id: Int) {
        try {
            apiService.deleteImageOut(access_token, id)
            database.photosDao().deleteImageDtoOut(id)
            getImageOut(access_token, id)
        } catch (e: Exception) {
            Log.d("!!!e_deleteImage", e.toString())
        }
    }

    private suspend fun insertToken(token: SignUserOutDto?) {
        database.photosDao().insertToken(token)
    }

    suspend fun getToken(): String {
        return database.photosDao().getToken()
    }
}