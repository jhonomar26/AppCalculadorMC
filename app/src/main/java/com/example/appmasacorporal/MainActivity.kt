package com.example.appmasacorporal

import DatabaseHelper
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Habilita la opción de que la aplicación use el modo de pantalla completa, eliminando los bordes
        enableEdgeToEdge()

        // Define el layout que se va a utilizar para esta actividad, vinculado al archivo XML `activity_main`
        setContentView(R.layout.activity_main)

        // Enlaza los componentes de la interfaz de usuario definidos en el archivo XML mediante sus identificadores
        val editTextAltura: EditText =
            findViewById(R.id.editTextAltura)  // Campo de entrada para la altura
        val editTextPeso: EditText =
            findViewById(R.id.editTextPeso)      // Campo de entrada para el peso
        val btnCalcular: Button =
            findViewById(R.id.botonCalcular)        // Botón para realizar el cálculo del IMC
        val textViewResultado: TextView =
            findViewById(R.id.textViewResultado) // Texto para mostrar el resultado del IMC
        val imageViewResultado: ImageView =
            findViewById(R.id.imageViewResult)  // Imagen que cambia según la categoría del IMC

        databaseHelper = DatabaseHelper(this)

        // Botón de cerrar sesión
        val logoutButton = findViewById<Button>(R.id.botonCerrarSesion)
        logoutButton.setOnClickListener {
            // Cerrar sesión
            databaseHelper.clearAuthenticatedUser()
            // Redirigir a la pantalla de Login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Establece un listener para el botón, que responde al hacer clic
        btnCalcular.setOnClickListener {
            // Convierte el texto del campo de altura a un número (Double). Si está vacío o no es válido, devuelve `null`
            val height = editTextAltura.text.toString().toDoubleOrNull()
                ?.let { it / 100 } // Convierte de cm a metros
            // Convierte el texto del campo de peso a un número (Double). Si está vacío o no es válido, devuelve `null`
            val weight = editTextPeso.text.toString().toDoubleOrNull()

            // Verifica si alguno de los valores es nulo (vacío o no válido)
            if (weight == null || height == null) {
                // Si hay un error (por ejemplo, si alguno de los campos está vacío), muestra un mensaje de error
                showError("Uno de los datos se encuentra vacío")
                imageViewResultado.setImageDrawable(null)
                textViewResultado.text = ""

            } else {
                // Si ambos valores son válidos, calcula el IMC (Índice de Masa Corporal)
                val bmi = weight / (height * height)
                // Llama a la función `updateUI` para mostrar los resultados en la interfaz
                updateUI(bmi, textViewResultado, imageViewResultado)
                // Guardar el historial en la base de datos
                // Obtener la fecha y hora actual

                // Obtener la fecha y hora actual
                val currentDateTime =
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

                // Determinar el concepto según el IMC
                val concept = when {
                    bmi < 18.49 -> "Bajo peso"
                    bmi in 18.49..24.9 -> "Peso normal"
                    bmi in 24.9..29.9 -> "Sobrepeso"
                    else -> "Obesidad"
                }

                // Guardar el historial en la base de datos
                val dbHelper = DatabaseHelper(this)
                val userId = 1  // Aquí deberías obtener el ID del usuario autenticado
                dbHelper.insertIMCHistory(userId, currentDateTime, weight, height, bmi, concept)
            }
        }
    }

    // Función para actualizar la interfaz de usuario con el resultado del IMC y la categoría correspondiente
    private fun updateUI(bmi: Double, textViewResultado: TextView, imageViewResultado: ImageView) {
        // Evalúa el valor del IMC y determina la categoría correspondiente y la imagen asociada
        val (bmiCategory, imageResource) = when {
            bmi < 18.49 -> "Bajo peso" to R.drawable.bajopeso    // IMC menor a 18.49: categoría de bajo peso
            bmi in 18.49..24.9 -> "Peso normal" to R.drawable.normalpeso // IMC entre 18.49 y 24.9: peso normal
            bmi in 24.9..29.9 -> "Sobrepeso" to R.drawable.sobrepeso     // IMC entre 24.9 y 29.9: sobrepeso
            bmi >= 29.9 -> "Obesidad" to R.drawable.obesidadpeso         // IMC mayor o igual a 29.9: obesidad
            else -> "Valor de IMC no válido" to 0                        // Si el IMC no entra en ninguna categoría
        }

        // Actualiza el `TextView` con el resultado del IMC y su categoría.
        // El `%.2f` indica que se muestra el valor del IMC con dos decimales.
        textViewResultado.text = "Tu IMC es %.2f.\nCategoría: %s".format(bmi, bmiCategory)

        // Si la categoría del IMC tiene una imagen asociada, actualiza el `ImageView` con esa imagen
        if (imageResource != 0) {
            imageViewResultado.setImageResource(imageResource)

        } else {
            // Si el valor del IMC es inválido, elimina cualquier imagen previa y muestra un mensaje de error en el texto
            imageViewResultado.setImageDrawable(null)
            textViewResultado.text = "Los datos ingresados son incorrectos, lo sentimos :c"
        }
    }

    // Función para mostrar un mensaje de error en forma de un `Toast`, un mensaje emergente en pantalla
    private fun showError(message: String) {
        // Muestra un mensaje corto que indica el error ocurrido
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


}
