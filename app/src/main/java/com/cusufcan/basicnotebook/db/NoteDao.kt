package com.cusufcan.basicnotebook.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.cusufcan.basicnotebook.model.MyNote
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface NoteDao {
    @Query("SELECT * FROM MyNote")
    fun getAll(): Flowable<List<MyNote>>

    @Query("SELECT * FROM MyNote WHERE id = :id")
    fun getById(id: Int): Flowable<MyNote>

    @Query("UPDATE MyNote SET title = :title, content = :content WHERE id = :id")
    fun updateById(id: Int, title: String, content: String): Completable

    @Insert
    fun insert(note: MyNote): Completable

    @Delete
    fun delete(note: MyNote): Completable
}