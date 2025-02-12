package com.example.tugaswawa

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inisialisasi komponen
        val usernameField: EditText = findViewById(R.id.etUsername)
        val passwordField: EditText = findViewById(R.id.etPassword)
        val loginButton: Button = findViewById(R.id.btnLogin)

        // Username dan Password yang valid
        val validUsername = "salwa"
        val validPassword = "salwa"

        // Fungsi ketika tombol login diklik
        loginButton.setOnClickListener {
            val inputUsername = usernameField.text.toString().trim()
            val inputPassword = passwordField.text.toString().trim()

            // Validasi input
            if (inputUsername.isEmpty()) {
                usernameField.error = "Username tidak boleh kosong"
                return@setOnClickListener
            }

            if (inputPassword.isEmpty()) {
                passwordField.error = "Password tidak boleh kosong"
                return@setOnClickListener
            }

            // Validasi username dan password
            if (inputUsername == validUsername && inputPassword == validPassword) {
                // Jika login berhasil, pindah ke halaman utama
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Tutup LoginActivity
            } else {
                // Jika login gagal, tampilkan pesan error
                Toast.makeText(this, "Username atau Password salah", Toast.LENGTH_SHORT).show()
            }
        }
    }
}