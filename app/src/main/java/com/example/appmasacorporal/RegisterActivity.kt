// RegisterActivity.kt
package com.example.appmasacorporal
import DatabaseHelper
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        dbHelper = DatabaseHelper(this)

        // Referenciar componentes
        val usernameField = findViewById<EditText>(R.id.usernameField)
        val passwordField = findViewById<EditText>(R.id.passwordField)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val backToLogin = findViewById<TextView>(R.id.backToLogin)

        registerButton.setOnClickListener {
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                val result = dbHelper.insertUser(username, password)
                if (result != -1L) {
                    Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
                    // Volver a la pantalla de inicio de sesión
                    // Limpiar los campos del formulario
                    usernameField.text.clear()
                    passwordField.text.clear()
                    finish()
                } else {
                    Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Mostrar mensaje de que falta completar los campos
                Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show()
            }
        }

        backToLogin.setOnClickListener {
            // Volver a la pantalla de inicio de sesión
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
