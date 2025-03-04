package com.empire.entregable_1.views

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.empire.entregable_1.MainActivity
import com.empire.entregable_1.MapsActivity
import com.empire.entregable_1.R
import com.empire.entregable_1.models.Administrador
import com.google.android.material.button.MaterialButton

class CheckReservations : AppCompatActivity() {

    lateinit var etIdReserva: EditText
    lateinit var btnConsultar: MaterialButton
    lateinit var etNombreSitio: EditText
    lateinit var etNombreCliente: EditText
    lateinit var btnVerMapa: MaterialButton
    lateinit var btnRetornar: MaterialButton

    // Variables para almacenar las coordenadas
    private var latitud: Double = 0.0
    private var longitud: Double = 0.0
    private var sitioNombre: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_check_reservations)

        inicializarComponentes()
        configurarBotones()
    }

    private fun configurarBotones() {
        // Botón para consultar reserva
        btnConsultar.setOnClickListener {
            val idReserva = etIdReserva.text.toString()

            if (idReserva.isEmpty()) {
                Toast.makeText(this, "Por favor ingrese un ID de reserva", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            consultarReserva(idReserva)
        }

        // Botón para ver mapa
        btnVerMapa.setOnClickListener {
            if (latitud != 0.0 && longitud != 0.0) {
                // Crear intent para la actividad del mapa
                val intent = Intent(this, MapsActivity::class.java).apply {
                    putExtra("LATITUD", latitud)
                    putExtra("LONGITUD", longitud)
                    putExtra("SITIO_NOMBRE", sitioNombre)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Primero debe consultar una reserva válida", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón para retornar
        btnRetornar.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun consultarReserva(idReserva: String) {
        val admin = Administrador(this)
        val cursorReserva = admin.consultarReserva(idReserva)

        if (cursorReserva.moveToFirst()) {
            // Obtenemos los IDs del cliente y sitio
            val clienteId = cursorReserva.getInt(cursorReserva.getColumnIndexOrThrow("cliente_id"))
            val sitioId = cursorReserva.getInt(cursorReserva.getColumnIndexOrThrow("sitio_id"))

            // Consultamos el nombre del cliente
            val db = admin.readableDatabase
            val cursorCliente = db.rawQuery("SELECT nombre, apellido FROM clients WHERE id = ?", arrayOf(clienteId.toString()))

            if (cursorCliente.moveToFirst()) {
                val nombreCliente = cursorCliente.getString(cursorCliente.getColumnIndexOrThrow("nombre"))
                val apellidoCliente = cursorCliente.getString(cursorCliente.getColumnIndexOrThrow("apellido"))
                etNombreCliente.setText("$nombreCliente $apellidoCliente")
            } else {
                etNombreCliente.setText("Cliente no encontrado")
            }
            cursorCliente.close()

            // Consultamos el sitio
            val cursorSitio = db.rawQuery("SELECT nombre, latitud, longitud FROM sites WHERE id = ?", arrayOf(sitioId.toString()))

            if (cursorSitio.moveToFirst()) {
                sitioNombre = cursorSitio.getString(cursorSitio.getColumnIndexOrThrow("nombre"))
                latitud = cursorSitio.getDouble(cursorSitio.getColumnIndexOrThrow("latitud"))
                longitud = cursorSitio.getDouble(cursorSitio.getColumnIndexOrThrow("longitud"))

                etNombreSitio.setText(sitioNombre)

                // Habilitamos el botón para ver el mapa
                btnVerMapa.isEnabled = true
            } else {
                etNombreSitio.setText("Sitio no encontrado")
                btnVerMapa.isEnabled = false
            }
            cursorSitio.close()

        } else {
            Toast.makeText(this, "Reserva no encontrada", Toast.LENGTH_SHORT).show()
            etNombreCliente.setText("")
            etNombreSitio.setText("")
            btnVerMapa.isEnabled = false
        }

        cursorReserva.close()
    }

    fun inicializarComponentes() {
        etIdReserva = findViewById(R.id.etIdReserva)
        btnConsultar = findViewById(R.id.btnConsultar)
        etNombreSitio = findViewById(R.id.etNombreSitio)
        etNombreCliente = findViewById(R.id.etNombreCliente)
        btnVerMapa = findViewById(R.id.btnVerMapa)
        btnRetornar = findViewById(R.id.btnRetornar)

        // Deshabilitamos el botón de mapa inicialmente
        btnVerMapa.isEnabled = false
    }
}