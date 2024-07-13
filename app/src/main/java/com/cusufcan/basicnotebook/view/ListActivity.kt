package com.cusufcan.basicnotebook.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.cusufcan.basicnotebook.R
import com.cusufcan.basicnotebook.adapter.NoteAdapter
import com.cusufcan.basicnotebook.databinding.ActivityListBinding
import com.cusufcan.basicnotebook.db.NoteDao
import com.cusufcan.basicnotebook.db.NoteDatabase
import com.cusufcan.basicnotebook.model.MyNote
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ListActivity : AppCompatActivity() {
    private val disposable = CompositeDisposable()

    private lateinit var db: NoteDatabase
    private lateinit var dao: NoteDao

    private lateinit var binding: ActivityListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeDb()
        loadNotes()
    }

    private fun initializeDb() {
        db = Room.databaseBuilder(
            applicationContext, NoteDatabase::class.java, "note_database"
        ).build()

        dao = db.dao()
    }

    private fun loadNotes() {
        disposable.add(
            dao.getAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleNotes)
        )
    }

    private fun handleNotes(notes: List<MyNote>) {
        val noteAdapter = NoteAdapter(notes)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = noteAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.app_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.actionCreate) {
            val intent = Intent(this, CreateActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra("info", "new")
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}