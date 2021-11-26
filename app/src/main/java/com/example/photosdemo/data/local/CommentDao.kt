package com.example.photosdemo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.photosdemo.data.models.comment.CommentDtoOut
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(list: MutableList<CommentDtoOut>)

    @Query("SELECT * FROM comments")
    fun getAllComments(): Flow<MutableList<CommentDtoOut>>

    @Query("DELETE from comments WHERE id IN (:id)")
    suspend fun deleteComment(id: Int)

    @Query("DELETE FROM comments")
    suspend fun deleteAllComments()
}