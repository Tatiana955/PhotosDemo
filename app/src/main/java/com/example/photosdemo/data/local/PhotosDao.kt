package com.example.photosdemo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.photosdemo.data.models.image.ImageDtoOut
import com.example.photosdemo.data.models.security.SignUserOutDto

@Dao
interface PhotosDao {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImageDtoOut(image: ImageDtoOut)

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllImageDtoOut(image: MutableList<ImageDtoOut?>)

    @Query("SELECT * FROM image")
    suspend fun getImageDtoOutList(): MutableList<ImageDtoOut>

    @Query ("SELECT * FROM image WHERE id = :id")
    suspend fun getImageDtoOutById(id: Int): ImageDtoOut

    @Query("DELETE FROM image")
    suspend fun clearImageDtoOut()

    @Query("DELETE from image WHERE id IN (:id)")
    suspend fun deleteImageDtoOut(id: Int)


    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToken(token: SignUserOutDto?)

    @Query ("SELECT token FROM token")
    suspend fun getToken(): String
}