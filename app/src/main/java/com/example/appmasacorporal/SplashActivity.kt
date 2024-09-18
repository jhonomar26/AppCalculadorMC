package com.example.appmasacorporal

import DatabaseHelper
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logo = findViewById<ImageView>(R.id.oms_logo)
        val text = findViewById<TextView>(R.id.textView)
        //Opacidad de la imágen
        logo.alpha = 0f
        text.alpha = 0f
        //Campia la opacidad de la imagen de 0 a 1 en 2 segundos
        logo.animate().alpha(1f).setDuration(3000).start()
        text.animate().alpha(1f).setDuration(3000).start()
        Handler(Looper.getMainLooper()).postDelayed({
            databaseHelper = DatabaseHelper(this)
            // Verificar si el usuario está autenticado
            val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
            val userId = sharedPreferences.getInt(
                "user_id",
                -1
            )  // -1 es el valor por defecto si no se encuentra el ID
            if (userId != -1) {
                Log.e("","El usuario si esta autenticado")
                // Si está autenticado, dirigir a MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Log.e("","Usuario no autenticado")

                // Si no está autenticado, dirigir a LoginActivity
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            finish()
        }, 6000)
    }

}