package com.example.photosdemo.data

import android.util.Log
import androidx.room.withTransaction
import com.example.photosdemo.data.local.PhotosDatabase
import com.example.photosdemo.data.models.comment.CommentDtoIn
import com.example.photosdemo.data.models.image.ImageDtoIn
import com.example.photosdemo.data.models.security.ResponseDto
import com.example.photosdemo.data.models.security.SignUserDtoIn
import com.example.photosdemo.data.remote.ApiService
import com.example.photosdemo.util.networkBoundResource
import java.lang.Exception
import javax.inject.Inject

class Repository @Inject constructor(
    private val apiService: ApiService,
    private val database: PhotosDatabase
    )
{
    suspend fun postSignUp(userDto: SignUserDtoIn): ResponseDto? {
        return try {
            apiService.postSignUp(userDto)
        } catch (e: Exception) {
            Log.d("!!!e_postSignUp", e.toString())
            null
        }
    }

    suspend fun postSignIn(userDto: SignUserDtoIn): ResponseDto? {
        return try {
            apiService.postSignIn(userDto)
        } catch (e: Exception) {
            Log.d("!!!e_postSignIn", e.toString())
            null
        }
    }

    fun getImageOut(access_token: String, page: Int) = networkBoundResource(
        // Query to return the list of all images
        query = {
            database.photosDao().getAllImages()
        },
        fetch = {
            apiService.getImageOut(access_token, page)
        },
        // Save the results in the table.
        // If data exists, then delete it and then store.
        saveFetchResult = { list ->
            database.withTransaction {
                database.photosDao().deleteAllImages()
                database.photosDao().insertImages(list.data)
            }
        }
    )

    suspend fun postImageOut(access_token: String, imageDtoIn: ImageDtoIn, page: Int) {
        try {
            apiService.postImageOut(access_token, imageDtoIn)
            val data = apiService.getImageOut(access_token, page)
            database.withTransaction {
                database.photosDao().deleteAllImages()
                database.photosDao().insertImages(data.data)
            }
        } catch (e: Exception) {
            Log.d("!!!e_postImage", e.toString())
        }
    }

    suspend fun deleteImageOut(access_token: String, id: Int) {
        try {
            apiService.deleteImageOut(access_token, id)
            database.photosDao().deleteImageDtoOut(id)
        } catch (e: Exception) {
            Log.d("!!!e_deleteImage", e.toString())
        }
    }

    fun getComments(access_token: String, imageId: Int, page: Int) = networkBoundResource(
        // Query to return the list of all comments
        query = {
            database.commentDao().getAllComments()
        },
        fetch = {
            apiService.getComment(access_token, imageId, page)
        },
        // Save the results in the table.
        // If data exists, then delete it and then store.
        saveFetchResult = { list ->
            database.withTransaction {
                database.commentDao().deleteAllComments()
                database.commentDao().insertComment(list.data)
            }
        }
    )

    suspend fun postComment(access_token: String, commentDtoIn: CommentDtoIn, imageId: Int, page: Int) {
        try {
            apiService.postComment(access_token, commentDtoIn, imageId)
            val data = apiService.getComment(access_token, imageId, page)
            database.withTransaction {
                database.commentDao().deleteAllComments()
                database.commentDao().insertComment(data.data)
            }
        } catch (e: Exception) {
            Log.d("!!!e_postComment", e.toString())
        }
    }

    suspend fun deleteComment(access_token: String, commentId: Int, imageId: Int) {
        try {
            apiService.deleteComment(access_token, commentId, imageId)
            database.commentDao().deleteComment(commentId)
        } catch (e: Exception) {
            Log.d("!!!e_deleteComment", e.toString())
        }
    }
}