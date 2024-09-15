package com.example.appmasacorporal

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
//Hereda de la clase AppCompatActivity
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //Referenciar componenetes
        val loginButton = findViewById<Button>(R.id.loginButton)
        val passwordField = findViewById<EditText>(R.id.passwordField)
        val aboutButton = findViewById<Button>(R.id.about_button)

        aboutButton.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }

        //Ejecución del boton una vez se le de click
        loginButton.setOnClickListener {
            //Se trae la contraseña ingresado por el usuario
            val enteredPassword = passwordField.text.toString()

            // Validación de contraseña
            if (enteredPassword == "12345") {
                // Si la contraseña es correcta, abrir MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()  // Cerrar LoginActivity
            } else {
                // Si la contraseña es incorrecta, mostrar un Toast
                Toast.makeText(this, getString(R.string.incorrect_password), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
