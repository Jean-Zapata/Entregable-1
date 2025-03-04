package com.empire.entregable_1.views

import android.app.DatePickerDialog
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.empire.entregable_1.MainActivity
import com.empire.entregable_1.R
import com.empire.entregable_1.models.Administrador
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class RegisterReservation : AppCompatActivity() {
    private lateinit var etFecha: TextInputEditText
    private lateinit var etCosto: TextInputEditText
    private lateinit var etDuracion: TextInputEditText
    private lateinit var actvCliente: AutoCompleteTextView
    private lateinit var actvSitio: AutoCompleteTextView
    private lateinit var btnRegistrar: MaterialButton
    private lateinit var btnConsultar: MaterialButton
    private lateinit var btnRetornar: MaterialButton
    private lateinit var dbHelper: Administrador

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_reservation)

        dbHelper = Administrador(this)

        iniciarComponentes()
        configurarListeners()
        cargarClientesYSitios()
    }

    private fun iniciarComponentes() {
        etFecha = findViewById(R.id.etFecha)
        etCosto = findViewById(R.id.etCosto)
        etDuracion = findViewById(R.id.etDuracion)
        actvCliente = findViewById(R.id.actvCliente)
        actvSitio = findViewById(R.id.actvSitio)
        btnRegistrar = findViewById(R.id.btnRegistrar)
        btnConsultar = findViewById(R.id.btnConsultar)
        btnRetornar = findViewById(R.id.btnRetornar)
    }

    private fun configurarListeners() {
        btnRegistrar.setOnClickListener { registrarReserva() }
        btnConsultar.setOnClickListener { consultarReserva() }
        btnRetornar.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        etFecha.setOnClickListener { mostrarSelectorFecha() }
    }

    private fun cargarClientesYSitios() {
        val clientes = obtenerListaClientes()
        val sitios = obtenerListaSitios()

        actvCliente.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, clientes))
        actvSitio.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, sitios))
    }

    private fun obtenerListaClientes(): List<String> {
        val lista = mutableListOf<String>()
        val cursor: Cursor? = dbHelper.readableDatabase.rawQuery("SELECT id, nombre FROM clients", null)
        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getInt(0)
                val nombre = it.getString(1)
                lista.add("$id - $nombre")
            }
        }
        return lista
    }

    private fun obtenerListaSitios(): List<String> {
        val lista = mutableListOf<String>()
        val cursor: Cursor? = dbHelper.readableDatabase.rawQuery("SELECT id, nombre FROM sites", null)
        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getInt(0)
                val nombre = it.getString(1)
                lista.add("$id - $nombre")
            }
        }
        return lista
    }

    private fun registrarReserva() {
        if (!validarCampos()) return

        val fecha = etFecha.text.toString().trim()
        val costo = etCosto.text.toString().trim().toDouble()
        val duracion = etDuracion.text.toString().trim().toInt()
        val clienteId = actvCliente.text.toString().split(" - ")[0].toInt()
        val sitioId = actvSitio.text.toString().split(" - ")[0].toInt()

        val resultado = dbHelper.insertarReserva(fecha, costo, duracion, clienteId, sitioId)
        if (resultado != -1L) {
            Toast.makeText(this, "Reserva guardada correctamente", Toast.LENGTH_SHORT).show()
            limpiarCampos()
        } else {
            Toast.makeText(this, "Error al guardar la reserva", Toast.LENGTH_SHORT).show()
        }
    }

    private fun consultarReserva() {
        // Implementar lógica de consulta similar al método de inserción
    }

    private fun mostrarSelectorFecha() {
        val calendario = Calendar.getInstance()
        val anio = calendario[Calendar.YEAR]
        val mes = calendario[Calendar.MONTH]
        val dia = calendario[Calendar.DAY_OF_MONTH]

        val selectorFecha = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            etFecha.setText("%02d/%02d/%04d".format(dayOfMonth, month + 1, year))
        }, anio, mes, dia)

        selectorFecha.show()
    }

    private fun limpiarCampos() {
        etFecha.setText("")
        etCosto.setText("")
        etDuracion.setText("")
        actvCliente.setText("")
        actvSitio.setText("")
    }

    private fun validarCampos(): Boolean {
        return if (etFecha.text.toString().trim().isEmpty() ||
            etCosto.text.toString().trim().isEmpty() ||
            etDuracion.text.toString().trim().isEmpty() ||
            actvCliente.text.toString().trim().isEmpty() ||
            actvSitio.text.toString().trim().isEmpty()
        ) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }
}