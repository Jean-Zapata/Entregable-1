package com.empire.entregable_1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.empire.entregable_1.access.AuthActivity
import com.empire.entregable_1.views.CheckReservations
import com.empire.entregable_1.views.RegisterClients
import com.empire.entregable_1.views.RegisterSites
import com.empire.entregable_1.views.RegisterReservation


class MainActivity : AppCompatActivity() {

    lateinit var btnRegistrarSitios: Button
    lateinit var btnRegistrarClientes: Button
    lateinit var btnRegistrarReservas: Button
    lateinit var btnConsultarReservas: Button
    lateinit var btnCerrarSesion: Button




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        inicializarComponentes()
        cerrarSesion()


        btnRegistrarSitios.setOnClickListener {
            val intent = Intent(this, RegisterSites::class.java)
            startActivity(intent)
        }
        btnRegistrarClientes.setOnClickListener {
            val intent = Intent(this, RegisterClients::class.java)
            startActivity(intent)
        }
        btnRegistrarReservas.setOnClickListener {
            val intent = Intent(this, RegisterReservation::class.java)
            startActivity(intent)
        }
        btnConsultarReservas.setOnClickListener {
            val intent = Intent(this, CheckReservations::class.java)
            startActivity(intent)
        }

    }


    fun inicializarComponentes(){
        btnRegistrarSitios = findViewById(R.id.btnRegistrarSitios)
        btnRegistrarClientes = findViewById(R.id.btnRegistrarClientes)
        btnRegistrarReservas = findViewById(R.id.btnRegistrarReservas)
        btnConsultarReservas = findViewById(R.id.btnConsultarReservas)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)

    }

    fun cerrarSesion(){
        btnCerrarSesion.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Cerrar Sesión")
            builder.setMessage("¿Está seguro de que desea cerrar sesión?")
            builder.setPositiveButton("Sí") { dialog, which ->
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
                finish()
            }
            builder.setNegativeButton("No") { dialog, which ->

            }
            val dialog = builder.create()
            dialog.show()
        }
    }

}