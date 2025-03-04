package com.empire.entregable_1.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.empire.entregable_1.MapsActivity
import com.empire.entregable_1.database.DBHelper
import com.empire.entregable_1.databinding.ActivityCheckReservationsBinding
import com.empire.entregable_1.models.Reservation
import com.empire.entregable_1.models.Sites

class CheckReservations : AppCompatActivity() {

    private lateinit var binding: ActivityCheckReservationsBinding
    private lateinit var dbHelper: DBHelper
    private var currentSite: Sites? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckReservationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnConsultar.setOnClickListener {
            val reservationId = binding.etIdReserva.text.toString().trim()
            if (reservationId.isEmpty()) {
                showError("Por favor, ingrese un ID de reserva")
                return@setOnClickListener
            }

            consultarReserva(reservationId)
        }

        binding.btnVerMapa.setOnClickListener {
            currentSite?.let { site ->
                val intent = Intent(this, MapsActivity::class.java).apply {
                    putExtra("LATITUDE", site.latitud)
                    putExtra("LONGITUDE", site.longitud)
                }
                startActivity(intent)
            } ?: showError("No hay un sitio para ver en el mapa")
        }

        binding.btnRetornar.setOnClickListener {
            finish()
        }
    }

    private fun consultarReserva(reservationId: String) {
        val reservation = dbHelper.consultarReserva(reservationId)
        if (reservation != null) {
            fetchClientAndSiteInfo(reservation)
        } else {
            showError("No se encontró la reserva con ID: $reservationId")
        }
    }

    private fun fetchClientAndSiteInfo(reservation: Reservation) {
        val client = dbHelper.consultarCliente(reservation.cliente)
        val site = dbHelper.consultarSitio(reservation.sitio)

        binding.etNombreCliente.setText(client?.let { "${it.nombre} ${it.apellido}" } ?: "Cliente no encontrado")
        binding.etNombreSitio.setText(site?.nombre ?: "Sitio no encontrado")
        currentSite = site
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
