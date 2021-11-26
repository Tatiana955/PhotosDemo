package com.example.photosdemo.data.models.comment

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class CommentDtoOut(
    @PrimaryKey val id: Int,
    val date: Int,
    val text: String
)