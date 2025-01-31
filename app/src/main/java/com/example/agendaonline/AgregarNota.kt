package com.example.agendaonline

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AgregarNota : AppCompatActivity() {
    private lateinit var btnCalendario: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agregar_nota)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnCalendario = findViewById(R.id.btnCalendario)
        btnCalendario.setOnClickListener {
            // Lógica para abrir el selector de fecha
            //mostramos el selector de fecha y obtenemos la fecha seleccionada
            val datePicker = DatePickerDialog(this)
            datePicker.show()
            datePicker.setOnDateSetListener { _, year, month, dayOfMonth ->
                var day = dayOfMonth.toString()
                var monthFormatted = (month + 1).toString()
                if (dayOfMonth < 10) {
                    day = "0$dayOfMonth"
                }
                if (month < 10) {
                    monthFormatted = "0$monthFormatted"
                }
                val selectedDate = "$day/$monthFormatted/$year"
                btnCalendario.text = selectedDate
            }

        }

    }
}