package com.example.photosdemo.data.models.security

data class SignUserOutDto(
    var userId: Int?,
    var login: String?,
    var token: String?
    )