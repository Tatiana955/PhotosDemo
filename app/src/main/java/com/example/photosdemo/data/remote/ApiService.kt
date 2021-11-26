package com.example.photosdemo.data.remote

import com.example.photosdemo.data.models.comment.CommentDtoIn
import com.example.photosdemo.data.models.comment.ResponseDtoComment
import com.example.photosdemo.data.models.image.ImageDtoIn
import com.example.photosdemo.data.models.image.ResponseDtoImage
import com.example.photosdemo.data.models.security.ResponseDto
import com.example.photosdemo.data.models.security.SignUserDtoIn
import retrofit2.http.*

const val BASE_URL = "http://junior.balinasoft.com/"

interface ApiService {

    @POST("api/account/signup")
    suspend fun postSignUp(
        @Body userDto: SignUserDtoIn
    ): ResponseDto

    @POST("api/account/signin")
    suspend fun postSignIn(
        @Body userDto: SignUserDtoIn
    ): ResponseDto

    @GET("api/image")
    suspend fun getImageOut(
        @Header("Access-Token") access_token: String,
        @Query("page") page: Int
    ): ResponseDtoImage

    @POST("api/image")
    suspend fun postImageOut(
        @Header("Access-Token") access_token: String,
        @Body imageDtoIn: ImageDtoIn
    )

    @DELETE("api/image/{id}")
    suspend fun deleteImageOut(
        @Header("Access-Token") access_token: String,
        @Path("id") id: Int
    )

    @GET("/api/image/{imageId}/comment")
    suspend fun getComment(
        @Header("Access-Token") access_token: String,
        @Path("imageId") imageId: Int,
        @Query("page") page: Int
    ): ResponseDtoComment

    @POST("/api/image/{imageId}/comment")
    suspend fun postComment(
        @Header("Access-Token") access_token: String,
        @Body commentDtoIn: CommentDtoIn,
        @Path("imageId") imageId: Int
    )

    @DELETE("/api/image/{imageId}/comment/{commentId}")
    suspend fun deleteComment(
        @Header("Access-Token") access_token: String,
        @Path("commentId") commentId: Int,
        @Path("imageId") imageId: Int
    )
}