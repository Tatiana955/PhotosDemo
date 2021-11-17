package com.example.photosdemo.data.models.image

data class ImageDtoIn(
    var base64Image: String?,
    var date: Int?,
    var lat: Double?,
    var lng: Double?
)