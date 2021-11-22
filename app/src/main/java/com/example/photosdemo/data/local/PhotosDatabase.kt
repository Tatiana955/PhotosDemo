package com.example.photosdemo.data.local

import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import com.example.photosdemo.data.models.image.ImageDtoOut

@Database(
    version = 2,
    exportSchema = true,
    entities = [
        ImageDtoOut::class
    ],
    autoMigrations = [
        AutoMigration(
            from = 1,
            to = 2,
            spec = PhotosDatabase.DeleteTokenMigration::class
        )
    ]
)

abstract class PhotosDatabase: RoomDatabase() {
    abstract fun photosDao(): PhotosDao

    @DeleteTable(tableName = "token")
    class DeleteTokenMigration : AutoMigrationSpec
}