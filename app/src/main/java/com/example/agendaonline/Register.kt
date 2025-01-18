package com.example.agendaonline

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

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

        //lisener para el boton de registrar
        btnRegistrar.setOnClickListener {
            val validar = validarDatos()
            if (validar) CrearCuenta(correo = txtCorreo.text.toString(), password = txtPassword.text.toString())
        }

    }
    private fun CrearCuenta(correo:String, password:String) {
        //mostramos el dialogo de progreso
        val progressDialog = AlertDialog.Builder(this)
            .setView(R.layout.progress_dialog)
            .setCancelable(false)
            .create()
        progressDialog.show()

        auth.createUserWithEmailAndPassword(correo, password)
            .addOnSuccessListener {
                guardarInformacion(progressDialog)
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Error registrar la cuenta: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun validarDatos(): Boolean {
        if (TextUtils.isEmpty(txtNombre.text.toString())){
            Toast.makeText(this, "Ingrese su nombre", Toast.LENGTH_SHORT).show()
            return false
        }
        if (TextUtils.isEmpty(txtCorreo.text.toString())){
            Toast.makeText(this, "Ingrese su correo", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(txtCorreo.text.toString()).matches()){
            Toast.makeText(this, "Correo no válido", Toast.LENGTH_SHORT).show()
            return false
        }
        if (TextUtils.isEmpty(txtPassword.text.toString())) {
            Toast.makeText(
                this,
                "Ingrese una contraseña",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        if (TextUtils.isEmpty(txtConfirmPassword.text.toString())) {
            Toast.makeText(
                this,
                "Por favor confirme su contraseña",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        if (txtPassword.text.toString() != txtConfirmPassword.text.toString()) {
             Toast.makeText(
                 this,
                 "Las contraseñas no coinciden",
                 Toast.LENGTH_SHORT
             ).show()
             return false
        }
        if (txtPassword.text.toString().length < 6) {
            Toast.makeText(
                this,
                "La contraseña debe tener al menos 6 caracteres",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true //retorna en caso de exito
    }
    private fun guardarInformacion(progressDialog: AlertDialog) {

        val uid: String = auth.uid.toString()
        val datos = hashMapOf<String, String>(
            "uid" to uid.toString(),
            "nombre" to txtNombre.text.toString(),
            "correo" to txtCorreo.text.toString()
        )

        val df: DatabaseReference = FirebaseDatabase.getInstance().getReference("Usuarios")
        df.child(uid).setValue(datos)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Cuenta creada correctamente", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MenuPrincipal::class.java))//redireccionamos al menu principal
                finish()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Error al intentar guardar la cuenta: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}