/*
 * Copyright 2020 Sergio Belda
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sergiobelda.androidtodometer.persistence

import androidx.room.*
import com.sergiobelda.androidtodometer.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(task: Task)

    @Query("SELECT * FROM task_table ORDER BY taskId ASC")
    fun getTasks(): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE taskId = :id")
    fun getTask(id: Int): Flow<Task>

    @Query("DELETE FROM task_table WHERE taskId = :id")
    suspend fun deleteTask(id: Int)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("DELETE FROM task_table")
    suspend fun deleteTasks()

    @Update
    suspend fun updateTask(task: Task)

    @Query("UPDATE task_table SET taskState = 1 WHERE taskId = :id")
    suspend fun setTaskDone(id: Int)

    @Query("UPDATE task_table SET taskState = 0 WHERE taskId = :id")
    suspend fun setTaskDoing(id: Int)
}