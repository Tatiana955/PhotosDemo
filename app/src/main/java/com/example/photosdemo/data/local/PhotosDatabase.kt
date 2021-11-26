package com.example.photosdemo.data.local

import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import com.example.photosdemo.data.models.comment.CommentDtoOut
import com.example.photosdemo.data.models.image.ImageDtoOut

@Database(
    version = 3,
    exportSchema = true,
    entities = [
        ImageDtoOut::class,
        CommentDtoOut::class
    ],
    autoMigrations = [
        AutoMigration(
            from = 1,
            to = 2,
            spec = PhotosDatabase.DeleteTokenMigration::class
        ),
        AutoMigration(
            from = 2,
            to = 3
        )
    ]
)

abstract class PhotosDatabase: RoomDatabase() {
    abstract fun photosDao(): PhotosDao
    abstract fun commentDao(): CommentDao

    @DeleteTable(tableName = "token")
    class DeleteTokenMigration : AutoMigrationSpec
}