package com.empire.entregable_1.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.empire.entregable_1.MainActivity
import com.empire.entregable_1.R
import com.empire.entregable_1.models.Administrador
import com.google.android.material.button.MaterialButton

class RegisterClients : AppCompatActivity() {

    private lateinit var etIdCliente: EditText
    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var etDni: EditText
    private lateinit var etTelefono: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnRegistrar: MaterialButton
    private lateinit var btnConsultar: MaterialButton
    private lateinit var btnRetornar: MaterialButton

    private lateinit var dbHelper: Administrador

    private val TAG = "RegisterClients"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_clients)

        iniciarComponentes()
        dbHelper = Administrador(this)

        btnRegistrar.setOnClickListener { registrarCliente() }
        btnConsultar.setOnClickListener { consultarCliente() }

        btnRetornar.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun iniciarComponentes() {
        etIdCliente = findViewById(R.id.etIdCliente)
        etNombre = findViewById(R.id.etNombre)
        etApellido = findViewById(R.id.etApellido)
        etDni = findViewById(R.id.etDni)
        etTelefono = findViewById(R.id.etTelefono)
        etEmail = findViewById(R.id.etEmail)
        btnRegistrar = findViewById(R.id.btnRegistrar)
        btnConsultar = findViewById(R.id.btnConsultar)
        btnRetornar = findViewById(R.id.btnRetornar)
    }

    private fun registrarCliente() {
        val nombre = etNombre.text.toString().trim()
        val apellido = etApellido.text.toString().trim()
        val dni = etDni.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()
        val email = etEmail.text.toString().trim()

        if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d(TAG, "Registrando cliente con DNI: $dni")

        val resultado = dbHelper.insertarCliente(nombre, apellido, dni, telefono, email)

        if (resultado != -1L) {
            Toast.makeText(this, "Cliente guardado correctamente", Toast.LENGTH_SHORT).show()
            limpiarCampos()
        } else {
            Toast.makeText(this, "Error al guardar el cliente", Toast.LENGTH_SHORT).show()
        }
    }

    private fun consultarCliente() {
        val dni = etDni.text.toString().trim()

        if (dni.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa un DNI", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d(TAG, "Consultando cliente con DNI: $dni")

        val cursor = dbHelper.consultarCliente(dni)
        if (cursor != null && cursor.moveToFirst()) {
            etNombre.setText(cursor.getString(cursor.getColumnIndexOrThrow("nombre")))
            etApellido.setText(cursor.getString(cursor.getColumnIndexOrThrow("apellido")))
            etTelefono.setText(cursor.getString(cursor.getColumnIndexOrThrow("telefono")))
            etEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow("email")))
            Toast.makeText(this, "Cliente encontrado", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Cliente no encontrado", Toast.LENGTH_SHORT).show()
        }
        cursor?.close()
    }

    private fun limpiarCampos() {
        etIdCliente.setText("")
        etNombre.setText("")
        etApellido.setText("")
        etDni.setText("")
        etTelefono.setText("")
        etEmail.setText("")
        etIdCliente.requestFocus()
    }
}
