package com.example.photosdemo.data.models.security

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "token")
data class SignUserOutDto(
    @PrimaryKey
    var userId: Int?,
    var login: String?,
    var token: String?
    )