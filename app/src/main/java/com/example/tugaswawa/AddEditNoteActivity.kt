package com.example.tugaswawa

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddEditNoteActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private var noteId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)

        dbHelper = DatabaseHelper(this)
        val editTitle = findViewById<EditText>(R.id.EditTitle)
        val editContent = findViewById<EditText>(R.id.EditContent)
        val btnSave = findViewById<Button>(R.id.BtnSave)

        noteId = intent.getIntExtra("note_id", -1)
        if (noteId != -1) {
            editTitle.setText(intent.getStringExtra("note_title"))
            editContent.setText(intent.getStringExtra("note_content"))
        }

        btnSave.setOnClickListener {
            val title = editTitle.text.toString()
            val content = editContent.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                if (noteId == -1) {
                    dbHelper.insertNote(title, content)
                    Toast.makeText(this, "Catatan Ditambahkan", Toast.LENGTH_SHORT).show()
                } else {
                    dbHelper.updateNote(noteId!!, title, content)
                    Toast.makeText(this, "Catatan Diperbarui", Toast.LENGTH_SHORT).show()
                }
                finish()
            } else {
                Toast.makeText(this, "Judul & Isi tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }
}