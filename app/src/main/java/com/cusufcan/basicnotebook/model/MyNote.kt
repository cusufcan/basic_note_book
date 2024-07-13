package com.cusufcan.basicnotebook.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class MyNote(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "content") val content: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}