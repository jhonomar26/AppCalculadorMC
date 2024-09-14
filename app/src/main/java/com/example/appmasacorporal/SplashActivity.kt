package com.example.appmasacorporal

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SplashActivity : AppCompatActivity() {
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
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 6000)
    }

}