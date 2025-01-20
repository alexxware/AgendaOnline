package com.example.agendaonline

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    lateinit var inputEmail: EditText
    lateinit var inputPassword: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        inputEmail = findViewById(R.id.usernameEditText)
        inputPassword = findViewById(R.id.userPasswordEt)

        val validado = ValidarDatos()
        if (validado) {
            IniciarSesion(
                correo = inputEmail.text.toString(),
                password = inputPassword.text.toString()
            )
        }

    }

    private fun ValidarDatos(): Boolean {
        if (inputEmail.text.toString().isEmpty()) {
            Toast.makeText(this, "Ingrese su correo", Toast.LENGTH_SHORT).show()
            return false
        }
        if (inputPassword.text.toString().isEmpty()) {
            Toast.makeText(this, "Ingrese una contraseña", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun IniciarSesion(correo: String, password: String) {
        //iniciamos la instancia para firebase
        val auth = FirebaseAuth.getInstance()

        auth.signInWithEmailAndPassword(correo, password)
            .addOnSuccessListener {
                Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}