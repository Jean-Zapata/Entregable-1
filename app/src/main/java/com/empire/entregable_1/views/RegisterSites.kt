package com.empire.entregable_1.views

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.empire.entregable_1.MainActivity
import com.empire.entregable_1.R
import com.empire.entregable_1.models.Administrador
import com.empire.entregable_1.models.Sites
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class RegisterSites : AppCompatActivity() {
    private var etNombre: TextInputEditText? = null
    private var etLatitud: TextInputEditText? = null
    private var etLongitud: TextInputEditText? = null
    private var etDescripcion: TextInputEditText? = null
    private var btnRegistrar: MaterialButton? = null
    private var btnConsultar: MaterialButton? = null
    private var btnRetornar: MaterialButton? = null
    private var dbHelper: Administrador? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_sites)

        iniciarComponentes()
        dbHelper = Administrador(this)

        btnRegistrar!!.setOnClickListener { v: View? -> registrarSitio() }
        btnConsultar!!.setOnClickListener { v: View? -> consultarSitio() }
        btnRetornar!!.setOnClickListener { v: View? ->
            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )
        }
    }

    private fun iniciarComponentes() {
        etNombre = findViewById(R.id.etNombre)
        etLatitud = findViewById(R.id.etLatitud)
        etLongitud = findViewById(R.id.etLongitud)
        etDescripcion = findViewById(R.id.etDescripcion)
        btnRegistrar = findViewById(R.id.btnRegistrar)
        btnConsultar = findViewById(R.id.btnConsultar)
        btnRetornar = findViewById(R.id.btnRetornar)
    }

    private fun registrarSitio() {
        val nombre = etNombre!!.text.toString().trim { it <= ' ' }
        val latitudStr = etLatitud!!.text.toString().trim { it <= ' ' }
        val longitudStr = etLongitud!!.text.toString().trim { it <= ' ' }
        val descripcion = etDescripcion!!.text.toString().trim { it <= ' ' }

        if (nombre.isEmpty() || latitudStr.isEmpty() || longitudStr.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val latitud = latitudStr.toDouble()
            val longitud = longitudStr.toDouble()
            val resultado = dbHelper!!.insertarSitio(nombre, latitud, longitud, descripcion)

            if (resultado != -1L) {
                Toast.makeText(this, "Sitio registrado con éxito", Toast.LENGTH_SHORT).show()
                limpiarCampos()
            } else {
                Toast.makeText(this, "Error al registrar sitio", Toast.LENGTH_SHORT).show()
            }
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Latitud o longitud inválidas", Toast.LENGTH_SHORT).show()
        }
    }

    private fun consultarSitio() {
        val nombre = etNombre!!.text.toString().trim { it <= ' ' }
        if (nombre.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa el nombre del sitio", Toast.LENGTH_SHORT)
                .show()
            return
        }

         fun consultarSitio() {
            val nombre = etNombre!!.text.toString().trim()
            if (nombre.isEmpty()) {
                Toast.makeText(this, "Por favor, ingresa el nombre del sitio", Toast.LENGTH_SHORT)
                    .show()
                return
            }

            val sitioCursor = dbHelper?.consultarSitioPorNombre(nombre)

            sitioCursor?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val latitud = cursor.getDouble(cursor.getColumnIndexOrThrow("latitud"))
                    val longitud = cursor.getDouble(cursor.getColumnIndexOrThrow("longitud"))
                    val descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"))

                    etLatitud?.setText(latitud.toString())
                    etLongitud?.setText(longitud.toString())
                    etDescripcion?.setText(descripcion)

                    Toast.makeText(this, "Sitio encontrado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Sitio no encontrado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


        private fun limpiarCampos() {
        etNombre!!.setText("")
        etLatitud!!.setText("")
        etLongitud!!.setText("")
        etDescripcion!!.setText("")
        etNombre!!.requestFocus()
    }
}