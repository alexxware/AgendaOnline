package com.example.agendaonline

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class MenuPrincipal : AppCompatActivity() {
    lateinit var btnCerrarSeison: Button
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

        //creamos instancia de Firebase para saber si hay un usuario con sesion activa
        btnCerrarSeison.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            //validamos si hay un usuario con sesion activa
            if (auth.currentUser != null){
                auth.signOut()
                startActivity(Intent(this, MainActivity::class.java))
                //finalizamos la pila de actividades
                finishAffinity()
            }
        }
    }
}