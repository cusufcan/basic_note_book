package com.cusufcan.basicnotebook.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cusufcan.basicnotebook.databinding.NoteItemBinding
import com.cusufcan.basicnotebook.model.MyNote
import com.cusufcan.basicnotebook.view.CreateActivity

class NoteAdapter(private val notes: List<MyNote>) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notes[position])
        holder.itemView.setOnClickListener() {
            val intent = Intent(holder.itemView.context, CreateActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra("info", "old")
            intent.putExtra("id", notes[position].id.toString())
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    class NoteViewHolder(private val binding: NoteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(note: MyNote) {
            binding.noteItemTitle.text = note.title
            binding.noteItemContent.text = note.content
        }
    }
}