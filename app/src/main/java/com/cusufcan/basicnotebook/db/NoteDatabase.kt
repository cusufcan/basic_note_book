package com.cusufcan.basicnotebook.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cusufcan.basicnotebook.model.MyNote

@Database(entities = [MyNote::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun dao(): NoteDao
}