package com.example.appmasacorporal

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //Se crean variables y se las trae los componenetes den xml, mediante el id
        val editTextAltura: EditText = findViewById(R.id.editTextAltura)
        val editTextPeso: EditText = findViewById(R.id.editTextPeso)
        val btnCalcular: Button = findViewById(R.id.botonCalcular)
        val textViewResultado: TextView = findViewById(R.id.textViewResultado)
        val imageViewResultado: ImageView = findViewById(R.id.imageViewResult)


        btnCalcular.setOnClickListener {
            //Trae la altura en caso de que caso de que sea vacio, trae null, si lo anterior no es verdadero, es decir, no es null lo divido entre 100 de lo contrario le asigno 0
            //Llamada segura
            val height = editTextAltura.text.toString().toDoubleOrNull()?.let { it / 100 }
            val weight = editTextPeso.text.toString().toDoubleOrNull()
            if (weight == null || height == null) {
                textViewResultado.text="Uno de los datos se encuentra vació"
            } else {
                val bmi = weight / (height * height)
                Log.e("Error",""+bmi)
                // Cambia las dos varibles, teniendo en cuenta la condición y de acuerdo a eso, asigna a bmiCategoria, imageResoursce carga la imagén
                val (bmiCategory, imageResource) = when {
                    bmi < 18.49 -> "Adelgazamiento" to R.drawable.bajopeso
                    bmi in 18.49..24.9 -> "Peso Normal" to R.drawable.normalpeso
                    bmi in 24.9..29.9 -> "Sobrepeso" to R.drawable.sobrepeso
                    bmi >= 29.9-> "Obesidad" to R.drawable.obesidadpeso
                    else -> "Valor de IMC no válido" to 0
                }

                //%s: Indica que placeholder para una cadena de texto
                //%.2f: Esto es un placeholder que se usa para formatear un número de punto flotante (Float o Double) con dos decimales.
                textViewResultado.text = "Tu IMC es %.2f.\n Categoría: %s".format(bmi, bmiCategory)

                if (imageResource != 0) {
                    imageViewResultado.setImageResource(imageResource)
                } else {
                    // imageViewResultado.setImageDrawable(null)
                    textViewResultado.text = "Los datos ingresado son ncorrectos, lo sentimos :c"
                }
            }
        }
    }
}