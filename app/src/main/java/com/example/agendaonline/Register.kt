package com.example.agendaonline

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class Register : AppCompatActivity() {
    //variables para los elementos de la vista
    lateinit var btnRegistrar: Button
    private lateinit var txtNombre: EditText
    lateinit var txtCorreo: EditText
    lateinit var txtPassword: EditText
    lateinit var txtConfirmPassword: EditText
    lateinit var btnTengoCuenta: TextView
    //creacion de variables para la autenticacion
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //creacion de getSupportActionBar para retroceder en la vista
        val actionBar = supportActionBar
        actionBar?.title = "Registro"
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)

        txtNombre = findViewById(R.id.txtNombre)
        txtCorreo = findViewById(R.id.txtCorreo)
        txtPassword = findViewById(R.id.txtPassword)
        txtConfirmPassword = findViewById(R.id.txtConfirmarPassword)
        //botones
        btnRegistrar = findViewById(R.id.btnRegistrar)
        btnTengoCuenta = findViewById(R.id.btnTengoCuenta)
        btnTengoCuenta.setOnClickListener {
            //regresamos a la vista anterior
            finish()
        }
        //inicializamos la autenticacion
        auth = FirebaseAuth.getInstance()
        val progressDialog = AlertDialog.Builder(this)
            .setView(R.layout.progress_dialog)
            .setCancelable(false)
            .create()

    }
    private fun ValidarDatos() {
        if (TextUtils.isEmpty(txtNombre.text.toString())){
            Toast.makeText(this, "Ingrese su nombre", Toast.LENGTH_SHORT).show()
        }
        if (TextUtils.isEmpty(txtCorreo.text.toString())){
            Toast.makeText(this, "Ingrese su correo", Toast.LENGTH_SHORT).show()
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(txtCorreo.text.toString()).matches()){
            Toast.makeText(this, "Correo no válido", Toast.LENGTH_SHORT).show()
        }
    }
}