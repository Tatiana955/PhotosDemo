package com.example.photosdemo.data.models.image

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image")
data class ImageDtoOut(
    @PrimaryKey var id: Int,
    var url: String?,
    var date: Long?,
    var lat: Double?,
    var lng: Double?
    )