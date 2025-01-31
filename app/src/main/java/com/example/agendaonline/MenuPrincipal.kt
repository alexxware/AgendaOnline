package com.example.agendaonline

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.agendaonline.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class MenuPrincipal : AppCompatActivity() {
    lateinit var btnCerrarSeison: Button
    private lateinit var tvNombreUsuario: TextView
    private lateinit var tvCorreoUsuario: TextView
    private lateinit var loadingData: ProgressBar
    private lateinit var btnAgregarNota: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_principal)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnCerrarSeison = findViewById(R.id.btnCerrarSesion)
        tvNombreUsuario = findViewById(R.id.txtNombreUsuario)
        tvCorreoUsuario = findViewById(R.id.txtCorreoUsuario)
        loadingData = findViewById(R.id.loadingData)
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            Toast.makeText(this, "No se ha iniciado sesion", Toast.LENGTH_SHORT).show()
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }

        cargarDatos();
        //creamos instancia de Firebase para saber si hay un usuario con sesion activa
        btnCerrarSeison.setOnClickListener {

            //validamos si hay un usuario con sesion activa
            if (auth.currentUser != null){
                auth.signOut()
                startActivity(Intent(this, MainActivity::class.java))
                //finalizamos la pila de actividades
                finishAffinity()
            }
        }

        //boton para ir a la interfaz de agregar nota
        btnAgregarNota = findViewById(R.id.btnAgregarNota)
        btnAgregarNota.setOnClickListener {
            startActivity(Intent(this, AgregarNota::class.java))
        }
    }
    private fun cargarDatos() {
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseDatabase.getInstance()
        val dbUsuarios = db.getReference("Usuarios")

        val currentUser = auth.currentUser
        if (currentUser != null) {
            dbUsuarios.child(currentUser.uid).get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        val userData = snapshot.getValue(User::class.java)
                        // Procesar los datos del usuario
                        loadingData.visibility = ProgressBar.GONE //ocultamos el progressbar
                        tvNombreUsuario.visibility = TextView.VISIBLE //mostramos el nombre del usuario
                        tvCorreoUsuario.visibility = TextView.VISIBLE //mostramos el correo del usuario
                        tvNombreUsuario.text = userData?.nombre
                        tvCorreoUsuario.text = userData?.correo
                    } else {
                        Log.w("CargarDatos", "No se encontraron datos para el usuario")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("CargarDatos", "Error al obtener datos", exception)
                    Toast.makeText(this, "Error al obtener los datos", Toast.LENGTH_SHORT).show()
                }
        } else {
            Log.w("CargarDatos", "El usuario no está autenticado")
            Toast.makeText(this, "El usuario no está autenticado", Toast.LENGTH_SHORT).show()
            //startActivity(Intent(this, MainActivity::class.java))
            //finalizamos la pila de actividades
            //finishAffinity()
        }
    }

}