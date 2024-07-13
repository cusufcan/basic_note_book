package com.cusufcan.basicnotebook.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.cusufcan.basicnotebook.databinding.ActivityCreateBinding
import com.cusufcan.basicnotebook.db.NoteDao
import com.cusufcan.basicnotebook.db.NoteDatabase
import com.cusufcan.basicnotebook.model.MyNote
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class CreateActivity : AppCompatActivity() {
    private val disposable = CompositeDisposable()

    private lateinit var db: NoteDatabase
    private lateinit var dao: NoteDao

    private lateinit var binding: ActivityCreateBinding

    private var noteFromMain: MyNote? = null
    private var isNewNote = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.deleteButton.visibility = View.GONE

        initializeDb()
        checkOldOrNewNote()
    }

    private fun checkOldOrNewNote() {
        val dataFromIntent = intent.getStringExtra("info")
        dataFromIntent?.let { isNewNote = it == "new" }
        if (!isNewNote) getNoteById()
    }

    private fun getNoteById() {
        binding.deleteButton.visibility = View.VISIBLE
        binding.saveButton.text = "UPDATE"

        val noteId = intent.getStringExtra("id")
        disposable.add(dao.getById(noteId!!.toInt()).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe() { note ->
                noteFromMain = note
                binding.noteTitleText.setText(note.title)
                binding.noteContentText.setText(note.content)
            })
    }

    private fun initializeDb() {
        db = Room.databaseBuilder(
            applicationContext, NoteDatabase::class.java, "note_database"
        ).build()

        dao = db.dao()
    }

    private fun handleCompleteResponse() {
        val intent = Intent(this, ListActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    fun save(view: View) {
        val title = binding.noteTitleText.text.toString().trim()
        val content = binding.noteContentText.text.toString().trim()

        if (title.isNotEmpty() && content.isNotEmpty()) {
            val note = MyNote(title, content)
            if (isNewNote) {
                disposable.add(
                    dao.insert(note).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleCompleteResponse)
                )
            } else {
                val id = noteFromMain!!.id
                disposable.add(
                    dao.updateById(id, title, content).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleCompleteResponse)
                )
            }
        }
    }

    fun delete(view: View) {
        disposable.add(
            dao.delete(noteFromMain!!).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(this::handleCompleteResponse)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}