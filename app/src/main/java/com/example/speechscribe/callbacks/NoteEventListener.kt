package com.example.speechscribe.callbacks

import com.example.speechscribe.entity.Note


/**
 * Created by Adarsh Mohan
 */
interface NoteEventListener {
    /**
     * call wen note clicked.
     *
     * @param note: note item
     */
    fun onNoteClick(note: Note)

    /**
     * call wen long Click to note.
     *
     * @param note : item
     */
    fun onNoteLongClick(note: Note)
}
