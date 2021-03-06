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

import androidx.room.TypeConverter
import com.sergiobelda.androidtodometer.model.TaskState
import com.sergiobelda.androidtodometer.model.Tag

// NOTE: each conversion must have two functions to convert A to B and B to A
// i.e. Tag to Int and Int to Tag
class Converters {
    @TypeConverter
    fun toInt(tag: Tag?): Int? {
        return tag?.ordinal
    }

    @TypeConverter
    fun toTag(ordinal: Int): Tag? {
        return enumValues<Tag>()[ordinal]
    }

    @TypeConverter
    fun toInt(taskState: TaskState?): Int? {
        return taskState?.ordinal
    }

    @TypeConverter
    fun toTaskState(ordinal: Int): TaskState? {
        return enumValues<TaskState>()[ordinal]
    }
}