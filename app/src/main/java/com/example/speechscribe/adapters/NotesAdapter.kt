package com.example.speechscribe.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.speechscribe.R
import com.example.speechscribe.adapters.NotesAdapter.NoteHolder
import com.example.speechscribe.callbacks.NoteEventListener
import com.example.speechscribe.entity.Note
import com.example.speechscribe.utils.NoteUtils
import kotlinx.android.synthetic.main.note_layout.view.*

/**
 * Created by Adarsh Mohan on 18/07/2020
 */
class NotesAdapter(
    private val context: Context,
    private var notes: List<Note>
) :
    RecyclerView.Adapter<NoteHolder>() {
    private var listener: NoteEventListener? = null
    private var multiCheckMode = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val v =
            LayoutInflater.from(context).inflate(R.layout.note_layout, parent, false)
        return NoteHolder(v)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val note: Note? = getNote(position)
        note?.let {
            holder.noteText.text = it.noteText
            holder.noteDate.text = NoteUtils.dateFromLong(it.noteDate)
            // init note click event
            holder.itemView.setOnClickListener { listener!!.onNoteClick(note) }
            // init note long click
            holder.itemView.setOnLongClickListener {
                listener!!.onNoteLongClick(note)
                false
            }
            // check checkBox if note selected
            if (multiCheckMode) {
                holder.checkBox.visibility = View.VISIBLE // show checkBox if multiMode on
                holder.checkBox.isChecked = note.checked
            } else holder.checkBox.visibility = View.GONE // hide checkBox if multiMode off
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    private fun getNote(position: Int): Note? {
        return notes[position]
    }


    fun getCheckedNotes(): List<Note> = notes.filter { note ->
        note.checked
    }


    inner class NoteHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var noteText: TextView
        var noteDate: TextView
        var checkBox: AppCompatCheckBox

        init {
            itemView.apply {
                noteText = note_text
                noteDate = note_date
                checkBox = check_Box
            }
        }


    }

    fun setListener(listener: NoteEventListener?) {
        this.listener = listener
    }

    fun setMultiCheckMode(multiCheckMode: Boolean) {
        this.multiCheckMode = multiCheckMode
        if (!multiCheckMode)
            notes.forEach {
                it.checked = false
            }
        notifyDataSetChanged()
    }

}