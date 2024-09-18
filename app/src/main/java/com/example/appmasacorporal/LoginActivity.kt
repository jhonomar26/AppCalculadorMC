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
        //Recorrido de todos los usuarios en la base de datos
        for (user in users) {
            println("ID: ${user["id"]}, Username: ${user["username"]}, Password: ${user["password"]}")
        }
        //dbHelper.deleteAllUsers()
        // Abrir la actividad "Acerca de"
        aboutButton.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }

        // En el onCreate de LoginActivity, dentro del listener del botón de login
        loginButton.setOnClickListener {
            // Obtener los valores ingresados por el usuario
            val enteredUsername = usernameField.text.toString()
            val enteredPassword = passwordField.text.toString()

            // Validar que el nombre de usuario y la contraseña no estén vacíos
            if (enteredUsername.isNotEmpty() && enteredPassword.isNotEmpty()) {
                // Obtener el ID del usuario validao
                val userId = databaseHelper.validateUser(enteredUsername, enteredPassword)
                if (userId != null) {
                    // Guardar el ID del usuario en SharedPreferences
                    val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putInt("user_id", userId)
                    editor.apply()
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

private fun Any.putInt(s: String, userId: Boolean) {


}
