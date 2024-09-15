package com.example.appmasacorporal


import DatabaseHelper
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.appmasacorporal.AboutActivity
import com.example.appmasacorporal.MainActivity
import com.example.appmasacorporal.R
import com.example.appmasacorporal.RegisterActivity

class LoginActivity : AppCompatActivity() {
    lateinit var databaseHelper:
            DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializar la base de datos
        databaseHelper = DatabaseHelper(this)

        // Referenciar componentes
        val loginButton = findViewById<Button>(R.id.loginButton)
        val usernameField = findViewById<EditText>(R.id.usernameField)
        val passwordField = findViewById<EditText>(R.id.passwordField)
        val aboutButton = findViewById<Button>(R.id.aboutButton)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val dbHelper = DatabaseHelper(this)

        val users = dbHelper.getAllUsers()

        for (user in users) {
            println("ID: ${user["id"]}, Username: ${user["username"]}, Password: ${user["password"]}")
        }






        // Abrir la actividad "Acerca de"
        aboutButton.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }

        // Ejecución del botón una vez se le da click
        loginButton.setOnClickListener {
            // Obtener los valores ingresados por el usuario
            val enteredUsername = usernameField.text.toString()
            val enteredPassword = passwordField.text.toString()

            // Validar que el nombre de usuario y la contraseña no estén vacíos
            if (enteredUsername.isNotEmpty() && enteredPassword.isNotEmpty()) {
                // Validación de usuario y contraseña en la base de datos
                if (databaseHelper.validateUser(enteredUsername, enteredPassword)) {
                    // Si la validación es correcta, abrir MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Cerrar LoginActivity
                } else {
                    // Si la validación es incorrecta, mostrar un Toast
                    Toast.makeText(
                        this,
                        getString(R.string.incorrect_credentials),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show()
            }
        }

        registerButton.setOnClickListener {
            // Abrir la actividad de registro
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
