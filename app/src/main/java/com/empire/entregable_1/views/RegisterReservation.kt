package com.empire.entregable_1.views

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.empire.entregable_1.R
import com.empire.entregable_1.database.DBHelper
import com.empire.entregable_1.database.DatabaseHelper

import com.empire.entregable_1.models.Reservation
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class RegisterReservation : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper

    // UI Components
    private lateinit var etIdReserva: TextInputEditText
    private lateinit var etFecha: TextInputEditText
    private lateinit var etCosto: TextInputEditText
    private lateinit var etDuracion: TextInputEditText
    private lateinit var actvCliente: AutoCompleteTextView
    private lateinit var actvSitio: AutoCompleteTextView
    private lateinit var btnRegistrar: MaterialButton
    private lateinit var btnConsultar: MaterialButton

    // Selected values
    private var selectedClienteId: String? = null
    private var selectedSitioId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_reservation)

        dbHelper = DBHelper(this)

        // Insert test data if needed


        // Initialize UI components
        initializeUIComponents()

        // Set up listeners
        setupListeners()

        // Load data for dropdowns
        cargarClientes()
        cargarSitios()
    }



    private fun initializeUIComponents() {
        etIdReserva = findViewById(R.id.etIdReserva)
        etFecha = findViewById(R.id.etFecha)
        etCosto = findViewById(R.id.etCosto)
        etDuracion = findViewById(R.id.etDuracion)
        actvCliente = findViewById(R.id.actvCliente)
        actvSitio = findViewById(R.id.actvSitio)
        btnRegistrar = findViewById(R.id.btnRegistrar)
        btnConsultar = findViewById(R.id.btnConsultar)
    }

    private fun setupListeners() {
        // Date picker for the date field
        setupDatePicker()

        // Client selection listener
        actvCliente.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedItem = actvCliente.adapter.getItem(position).toString()
            // Extract ID from the format "id - nombre apellido"
            selectedClienteId = selectedItem.split(" - ")[0]
            Log.d("SELECTION", "Cliente seleccionado ID: $selectedClienteId")
        }

        // Site selection listener
        actvSitio.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedItem = actvSitio.adapter.getItem(position).toString()
            // Extract ID from the format "id - nombre"
            selectedSitioId = selectedItem.split(" - ")[0]
            Log.d("SELECTION", "Sitio seleccionado ID: $selectedSitioId")
        }

        // Register button listener
        btnRegistrar.setOnClickListener {
            registrarReservacion()
        }

        // Consult button listener
        btnConsultar.setOnClickListener {
            consultarReservacion()
        }

        // Return button listener
        findViewById<MaterialButton>(R.id.btnRetornar).setOnClickListener {
            finish()
        }
    }

    private fun setupDatePicker() {
        etFecha.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(selectedYear, selectedMonth, selectedDay)
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    etFecha.setText(dateFormat.format(selectedDate.time))
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
    }

    private fun cargarClientes() {
        val clientes = dbHelper.obtenerClientes()
        if (clientes.isEmpty()) {
            Log.d("DB_CHECK", "No hay clientes en la base de datos.")
            Toast.makeText(this, "No hay clientes registrados", Toast.LENGTH_SHORT).show()
        } else {
            Log.d("DB_CHECK", "Clientes cargados: $clientes")
            val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, clientes)
            actvCliente.setAdapter(adapter)
        }
    }

    private fun cargarSitios() {
        val sitios = dbHelper.obtenerSitios()
        if (sitios.isEmpty()) {
            Log.d("DB_CHECK", "No hay sitios en la base de datos.")
            Toast.makeText(this, "No hay sitios registrados", Toast.LENGTH_SHORT).show()
        } else {
            Log.d("DB_CHECK", "Sitios cargados: $sitios")
            val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, sitios)
            actvSitio.setAdapter(adapter)
        }
    }

    private fun registrarReservacion() {
        try {
            // Validate fields
            val id = etIdReserva.text.toString().trim()
            val fecha = etFecha.text.toString().trim()
            val costoText = etCosto.text.toString().trim()
            val duracionText = etDuracion.text.toString().trim()

            if (id.isEmpty() || fecha.isEmpty() || costoText.isEmpty() || duracionText.isEmpty() ||
                selectedClienteId == null || selectedSitioId == null) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return
            }

            val costo = costoText.toDoubleOrNull()
            if (costo == null) {
                Toast.makeText(this, "El costo debe ser un número válido", Toast.LENGTH_SHORT).show()
                return
            }

            val duracion = duracionText.toIntOrNull()
            if (duracion == null) {
                Toast.makeText(this, "La duración debe ser un número entero", Toast.LENGTH_SHORT).show()
                return
            }

            // Create reservation object
            val reservacion = Reservation(
                id = id,
                fecha = fecha,
                costo = costo,
                duracion = duracion,
                cliente = selectedClienteId!!,
                sitio = selectedSitioId!!
            )

            // Insert into database
            val resultado = dbHelper.insertarReserva(reservacion)

            if (resultado) {
                Toast.makeText(this, "Reservación registrada con éxito", Toast.LENGTH_SHORT).show()
                limpiarCampos()
            } else {
                Toast.makeText(this, "Error al registrar la reservación", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            Log.e("REGISTER_ERROR", "Error al registrar reservación", e)
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun consultarReservacion() {
        val id = etIdReserva.text.toString().trim()

        if (id.isEmpty()) {
            Toast.makeText(this, "Ingrese un ID para consultar", Toast.LENGTH_SHORT).show()
            return
        }

        val reservacion = dbHelper.consultarReserva(id)

        if (reservacion != null) {
            // Set fields with reservation data
            etFecha.setText(reservacion.fecha)
            etCosto.setText(reservacion.costo.toString())
            etDuracion.setText(reservacion.duracion.toString())

            // Need to find the client and site in the lists
            selectedClienteId = reservacion.cliente
            selectedSitioId = reservacion.sitio

            // Find the client in the adapter
            val clientes = dbHelper.obtenerClientes()
            for (i in clientes.indices) {
                if (clientes[i].startsWith(reservacion.cliente)) {
                    actvCliente.setText(clientes[i], false)
                    break
                }
            }

            // Find the site in the adapter
            val sitios = dbHelper.obtenerSitios()
            for (i in sitios.indices) {
                if (sitios[i].startsWith(reservacion.sitio)) {
                    actvSitio.setText(sitios[i], false)
                    break
                }
            }

            Toast.makeText(this, "Reservación encontrada", Toast.LENGTH_SHORT).show()
        } else {
            AlertDialog.Builder(this)
                .setTitle("Reservación no encontrada")
                .setMessage("No existe una reservación con el ID: $id")
                .setPositiveButton("Aceptar", null)
                .show()
        }
    }

    private fun limpiarCampos() {
        etIdReserva.setText("")
        etFecha.setText("")
        etCosto.setText("")
        etDuracion.setText("")
        actvCliente.setText("", false)
        actvSitio.setText("", false)
        selectedClienteId = null
        selectedSitioId = null
    }
}