package com.example.photosdemo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.photosdemo.data.models.image.ImageDtoOut
import com.example.photosdemo.data.models.security.SignUserOutDto

@Database(
    entities = [
        ImageDtoOut::class,
        SignUserOutDto::class
               ],
    exportSchema = false,
    version = 1)
abstract class PhotosDatabase: RoomDatabase() {
    abstract fun photosDao(): PhotosDao
}