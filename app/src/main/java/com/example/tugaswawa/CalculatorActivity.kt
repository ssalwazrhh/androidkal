package com.example.tugaswawa

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tugaswawa.databinding.ActivityCalculatorBinding

class CalculatorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalculatorBinding
    private lateinit var tvResult: TextView
    private var isNewOperation = true

    private var mediaPlayer: MediaPlayer? = null
    private var mediaPlayerClear: MediaPlayer? = null
    private var mediaPlayerEquals: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tvResult = findViewById(R.id.tvResult)

        // Inisialisasi MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.button_click)
        mediaPlayerClear = MediaPlayer.create(this, R.raw.button_clear)
        mediaPlayerEquals = MediaPlayer.create(this, R.raw.button_sum)

        val numberButtons = arrayOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )

        numberButtons.forEachIndexed { index, buttonId ->
            findViewById<Button>(buttonId).setOnClickListener {
                playSound()
                onNumberClick(index.toString())
            }
        }

        findViewById<Button>(R.id.btnPlus).setOnClickListener { playSound(); onOperationClick("+") }
        findViewById<Button>(R.id.btnMinus).setOnClickListener { playSound(); onOperationClick("-") }
        findViewById<Button>(R.id.btnMultiply).setOnClickListener { playSound(); onOperationClick("*") }
        findViewById<Button>(R.id.btnDivide).setOnClickListener { playSound(); onOperationClick("/") }
        findViewById<Button>(R.id.btnEquals).setOnClickListener { playEqualsSound(); onEqualsClick() }
        findViewById<Button>(R.id.btnClear).setOnClickListener { playClearSound(); onClearClick() }

        binding.btnExit.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun onNumberClick(number: String) {
        if (tvResult.text == "0" || isNewOperation) {
            tvResult.text = number
        } else {
            tvResult.append(number)
        }
        isNewOperation = false
    }

    private fun onOperationClick(op: String) {
        val currentText = tvResult.text.toString()

        if (currentText.isEmpty() || currentText.last().toString() in arrayOf("+", "-", "*", "/")) {
            return
        }

        tvResult.append(" $op ")
        isNewOperation = false
    }

    private fun onEqualsClick() {
        try {
            val expression = tvResult.text.toString()
            val result = evaluateExpression(expression)

            tvResult.text = if (result.toLong().toDouble() == result) {
                result.toLong().toString()
            } else {
                result.toString()
            }

            isNewOperation = true
        } catch (e: Exception) {
            tvResult.text = "Error"
        }
    }

    private fun evaluateExpression(expression: String): Double {
        val tokens = expression.split(" ").toMutableList()

        // 1️⃣ Proses perkalian (*) dan pembagian (/) dulu
        var i = 0
        while (i < tokens.size) {
            if (tokens[i] == "*" || tokens[i] == "/") {
                val left = tokens[i - 1].toDouble()
                val right = tokens[i + 1].toDouble()
                val result = if (tokens[i] == "*") left * right else left / right

                // Ganti operator dan angka dengan hasilnya
                tokens[i - 1] = result.toString()
                tokens.removeAt(i) // Hapus operator
                tokens.removeAt(i) // Hapus angka di kanan operator
                i--
            }
            i++
        }

        // 2️⃣ Proses penjumlahan (+) dan pengurangan (-)
        i = 0
        while (i < tokens.size) {
            if (tokens[i] == "+" || tokens[i] == "-") {
                val left = tokens[i - 1].toDouble()
                val right = tokens[i + 1].toDouble()
                val result = if (tokens[i] == "+") left + right else left - right

                tokens[i - 1] = result.toString()
                tokens.removeAt(i) // Hapus operator
                tokens.removeAt(i) // Hapus angka di kanan operator
                i--
            }
            i++
        }

        return tokens[0].toDouble()
    }

    private fun onClearClick() {
        tvResult.text = "0"
        isNewOperation = true
    }

    private fun playSound() {
        mediaPlayer?.let {
            it.seekTo(0)
            it.start()
        }
    }

    private fun playClearSound() {
        mediaPlayerClear?.let {
            it.seekTo(0)
            it.start()
        }
    }

    private fun playEqualsSound() {
        mediaPlayerEquals?.let {
            it.seekTo(0)
            it.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayerClear?.release()
        mediaPlayerEquals?.release()
    }
}