package com.example.saludousuario

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class MainActivity : Activity() {

    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nameInput = findViewById<EditText>(R.id.nameInput)
        val birthDateInput = findViewById<EditText>(R.id.birthDateInput)
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val greetingResult = findViewById<TextView>(R.id.greetingResult)
        val greetingButton = findViewById<Button>(R.id.greetingButton)

        greetingButton.setOnClickListener {
            hideKeyboard()
            showGreeting(
                name = nameInput.text.toString().trim(),
                birthDateText = birthDateInput.text.toString().trim(),
                email = emailInput.text.toString().trim(),
                output = greetingResult,
            )
        }
    }

    private fun showGreeting(
        name: String,
        birthDateText: String,
        email: String,
        output: TextView,
    ) {
        if (name.isBlank()) {
            showError(output, getString(R.string.error_name))
            return
        }

        val birthDate = try {
            LocalDate.parse(birthDateText, dateFormatter)
        } catch (_: DateTimeParseException) {
            showError(output, getString(R.string.error_date))
            return
        }

        val today = LocalDate.now()
        if (birthDate.isAfter(today)) {
            showError(output, getString(R.string.error_future_date))
            return
        }

        val age = Period.between(birthDate, today).years
        output.setTextColor(Color.rgb(15, 76, 92))
        output.text = buildString {
            append(getString(R.string.greeting_format, name, age))
            if (email.isNotBlank()) {
                append("\n")
                append(getString(R.string.email_confirmation, email))
            }
        }
    }

    private fun showError(output: TextView, message: String) {
        output.setTextColor(Color.rgb(176, 42, 42))
        output.text = message
    }

    private fun hideKeyboard() {
        val manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        currentFocus?.clearFocus()
    }
}
