package com.example.todocompose.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM task_table")
    fun getTasks(): Flow<List<Task>>

    @Upsert
    suspend fun upsert(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * FROM task_table WHERE (id == :taskId)")
    fun getTaskById(taskId: Int): Flow<Task>

}