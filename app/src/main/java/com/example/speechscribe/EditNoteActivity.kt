package com.example.speechscribe

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.speechscribe.db.NotesDB
import com.example.speechscribe.db.NotesDao
import com.example.speechscribe.entity.Note
import com.example.speechscribe.utils.APP_PREFERENCES
import com.example.speechscribe.utils.THEME_Key
import kotlinx.android.synthetic.main.activity_edit_note.*
import java.util.*
import kotlin.math.max


class EditNoteActivity : AppCompatActivity() {

    private lateinit var inputNote: EditText
    private lateinit var dao: NotesDao
    private var temp: Note? = null


    companion object {
        val NOTE_EXTRA_Key = "note_id"
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        setupTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)
        setSupportActionBar(edit_note_activity_toolbar)
        inputNote = input_note
        dao = NotesDB.getInstance(this).notesDao()
        if (intent.extras != null) {
            val id = intent.extras!!.getInt(NOTE_EXTRA_Key, 0)
            temp = dao.getNoteById(id)
            temp?.let {
                inputNote.setText(it.noteText)
            }
        } else inputNote.isFocusable = true

        val mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        val mSpeechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        mSpeechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        mSpeechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            Locale.getDefault())
        mSpeechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(bundle: Bundle) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(v: Float) {}
            override fun onBufferReceived(bytes: ByteArray) {}
            override fun onEndOfSpeech() {}
            override fun onError(i: Int) {}
            override fun onResults(bundle: Bundle) {
                //getting all the matches
                val matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

                //displaying the first match
                if (matches != null )
                    inputNote.setText(matches[0])

            }

            override fun onPartialResults(bundle: Bundle) {}
            override fun onEvent(i: Int, bundle: Bundle) {}
        })
        findViewById<View>(R.id.button).setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_UP -> {
                    mSpeechRecognizer.stopListening()
                }
                MotionEvent.ACTION_DOWN -> {
                    mSpeechRecognizer.startListening(mSpeechRecognizerIntent)
                }
            }
            false
        }
    }

    private fun setupTheme() {
        // set theme
        val sharedPreferences =
            getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        val theme = sharedPreferences.getInt(THEME_Key, R.style.AppTheme)
        setTheme(theme)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edite_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == R.id.save_note) onSaveNote()
        return super.onOptionsItemSelected(item)
    }

    private fun onSaveNote() {
        val text = inputNote.toText()
        if (text.isNotEmpty()) {
            val date: Long = Date().time // get  system time
            // if  exist update els crete new
            if (temp == null) {
                temp = Note(0, text, date)
                dao.insertNote(temp!!) // create new note and inserted to database
            } else {
                temp!!.noteText = text
                temp!!.noteDate = date
                dao.updateNote(temp!!) // change text and date and update note on database
            }
            finish() // return to the MainActivity
        }
    }


    private fun EditText.toText(): String {
        return this.text.toString()
    }
}
