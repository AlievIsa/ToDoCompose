package com.example.todocompose.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface TaskDao {

    @Query("SELECT * FROM task_table")
    suspend fun getTasks(): List<Task>

    @Upsert
    suspend fun upsert(task: Task)

    @Delete
    suspend fun delete(task: Task)

}