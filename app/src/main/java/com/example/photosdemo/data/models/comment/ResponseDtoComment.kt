package com.example.photosdemo.data.models.comment

data class ResponseDtoComment(
    val status: Int,
    val data: MutableList<CommentDtoOut>
)