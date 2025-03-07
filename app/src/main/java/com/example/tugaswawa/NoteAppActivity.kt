package com.example.tugaswawa

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NoteAppActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NoteAdapter
    private var notesList = mutableListOf<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_app)

        // Set identitas ke TextView
        findViewById<TextView>(R.id.tv_Identity).text =
            "Nama : Siti Salwa\nKelas : RPL 2"

        dbHelper = DatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = NoteAdapter(notesList,
            onEditClick = { note ->
                val intent = Intent(this, AddEditNoteActivity::class.java).apply {
                    putExtra("note_id", note.id)
                    putExtra("note_title", note.title)
                    putExtra("note_content", note.content)
                }
                startActivity(intent)
            },
            onDeleteClick = { note ->
                showDeleteConfirmationDialog(note)
            }
        )
        recyclerView.adapter = adapter

        // Tombol tambah catatan
        findViewById<FloatingActionButton>(R.id.add_note).setOnClickListener {
            startActivity(Intent(this, AddEditNoteActivity::class.java))
        }

        // Tombol kembali ke MainActivity
        findViewById<Button>(R.id.btn_back).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Menutup aktivitas saat ini agar tidak bisa kembali ke sini dengan tombol back
        }
    }

    override fun onResume() {
        super.onResume()
        loadNotes()
    }

    private fun loadNotes() {
        notesList.clear()
        notesList.addAll(dbHelper.getAllNotes())

        if (notesList.isEmpty()) {
            Toast.makeText(this, "Tidak ada catatan yang tersedia", Toast.LENGTH_SHORT).show()
        }

        adapter.notifyDataSetChanged()
    }

    private fun showDeleteConfirmationDialog(note: Note) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Konfirmasi Hapus")
        dialog.setMessage("Apakah Anda yakin ingin menghapus catatan ini?")
        dialog.setPositiveButton("Ya") { _, _ ->
            dbHelper.deleteNote(note.id) // Hapus dari database
            loadNotes() // Segarkan daftar catatan
            Toast.makeText(this, "Catatan berhasil dihapus", Toast.LENGTH_SHORT).show()
        }
        dialog.setNegativeButton("Batal", null)
        dialog.show()
    }
}