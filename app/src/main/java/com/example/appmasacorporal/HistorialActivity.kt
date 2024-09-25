package com.example.appmasacorporal

import DatabaseHelper
import android.os.Bundle
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HistorialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial)

        val tableLayout = findViewById<TableLayout>(R.id.tableLayoutHistorial)

        val dbHelper = DatabaseHelper(this)
        val userId = dbHelper.getAuthenticatedUserId(this)
        val historial = userId?.let { dbHelper.getIMCHistory(it) }

        if (historial.isNullOrEmpty()) {
            val noDataRow = TableRow(this)
            val noDataView = TextView(this).apply {
                text = getString(R.string.no_historial)
            }
            noDataRow.addView(noDataView)
            tableLayout.addView(noDataRow)
        } else {
            for (record in historial) {
                val fecha = record["date_time"] ?: "N/A"
                val peso =
                    record["weight"]?.toDoubleOrNull()?.let { String.format("%.2f", it) } ?: "N/A"
                val altura =
                    record["height"]?.toDoubleOrNull()?.let { String.format("%.2f", it) } ?: "N/A"

                val imc =
                    record["imc"]?.toDoubleOrNull()?.let { String.format("%.2f", it) } ?: "N/A"
                val concepto = record["concept"] ?: "N/A"

                // Crear nueva fila de la tabla
                val row = TableRow(this)

                // Añadir los TextViews con los datos a la fila
                val fechaView = TextView(this).apply {
                    text = fecha
                    setPadding(8, 8, 8, 8)
                }

                val pesoView = TextView(this).apply {
                    text = peso
                    setPadding(8, 8, 8, 8)
                }

                val alturaView = TextView(this).apply {
                    text = altura
                    setPadding(8, 8, 8, 8)
                }

                val imcView = TextView(this).apply {
                    text = imc
                    setPadding(8, 8, 8, 8)
                }

                val conceptoView = TextView(this).apply {
                    text = concepto
                    setPadding(8, 8, 8, 8)
                }

                // Añadir los TextViews a la fila
                row.addView(fechaView)
                row.addView(pesoView)
                row.addView(alturaView)
                row.addView(imcView)
                row.addView(conceptoView)
                // Añadir la fila a la tabla
                tableLayout.addView(row)
            }
        }
    }
}
