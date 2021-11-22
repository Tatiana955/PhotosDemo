package com.example.photosdemo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.photosdemo.data.models.image.ImageDtoOut
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotosDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(list: MutableList<ImageDtoOut>)

    @Query("SELECT * FROM image")
    fun getAllImages(): Flow<MutableList<ImageDtoOut>>

    @Query("DELETE from image WHERE id IN (:id)")
    suspend fun deleteImageDtoOut(id: Int)

    @Query("DELETE FROM image")
    suspend fun deleteAllImages()
}