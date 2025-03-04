package com.empire.entregable_1.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.empire.entregable_1.MainActivity
import com.empire.entregable_1.R
import com.empire.entregable_1.models.Sites
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class RegisterSites : AppCompatActivity() {

    private lateinit var etIdSitio: TextInputEditText
    private lateinit var etNombre: TextInputEditText
    private lateinit var etLatitud: TextInputEditText
    private lateinit var etLongitud: TextInputEditText
    private lateinit var etDescripcion: TextInputEditText
    private lateinit var btnRegistrar: MaterialButton
    private lateinit var btnConsultar: MaterialButton
    private lateinit var btnRetornar: MaterialButton

    private lateinit var dbFirestore: FirebaseFirestore
    private lateinit var dbRealtime: DatabaseReference

    private val TAG = "RegisterSites"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_sites)

        iniciarComponentes()

        try {
            dbFirestore = FirebaseFirestore.getInstance()
            dbFirestore.firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()

            dbRealtime = FirebaseDatabase.getInstance().getReference("Sitios")

            Log.d(TAG, "Firebase inicializado correctamente")
        } catch (e: Exception) {
            Log.e(TAG, "Error al inicializar Firebase: ${e.message}")
            Toast.makeText(this, "Error al inicializar Firebase", Toast.LENGTH_LONG).show()
        }

        btnRegistrar.setOnClickListener { registrarSitio() }
        btnConsultar.setOnClickListener { consultarSitio() }

        btnRetornar.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun iniciarComponentes() {
        etIdSitio = findViewById(R.id.etIdSitio)
        etNombre = findViewById(R.id.etNombre)
        etLatitud = findViewById(R.id.etLatitud)
        etLongitud = findViewById(R.id.etLongitud)
        etDescripcion = findViewById(R.id.etDescripcion)
        btnRegistrar = findViewById(R.id.btnRegistrar)
        btnConsultar = findViewById(R.id.btnConsultar)
        btnRetornar = findViewById(R.id.btnRetornar)
    }

    private fun registrarSitio() {
        val idSitio = etIdSitio.text.toString().trim()
        val nombre = etNombre.text.toString().trim()
        val latitud = etLatitud.text.toString().trim()
        val longitud = etLongitud.text.toString().trim()
        val descripcion = etDescripcion.text.toString().trim()

        if (idSitio.isEmpty() || nombre.isEmpty() || latitud.isEmpty() || longitud.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d(TAG, "Registrando sitio con ID: $idSitio")

        val sitio = Sites(idSitio, nombre, latitud.toDouble(), longitud.toDouble(), descripcion)

        guardarSitio(sitio)
    }

    private fun guardarSitio(sitio: Sites) {
        val sitioMap = mapOf(
            "id" to sitio.id,
            "nombre" to sitio.nombre,
            "latitud" to sitio.latitud,
            "longitud" to sitio.longitud,
            "descripcion" to sitio.descripcion
        )

        dbFirestore.collection("Sitios").document(sitio.id)
            .set(sitioMap)
            .addOnSuccessListener {
                Log.d(TAG, "Sitio guardado en Firestore: $sitioMap")
                runOnUiThread {
                    Toast.makeText(this, "Sitio guardado en Firestore", Toast.LENGTH_SHORT).show()
                }
                guardarEnRealtimeDatabase(sitio)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error al guardar en Firestore: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this, "Error en Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun guardarEnRealtimeDatabase(sitio: Sites) {
        dbRealtime.child(sitio.id).setValue(sitio)
            .addOnSuccessListener {
                Log.d(TAG, "Sitio guardado en Realtime Database")
                runOnUiThread {
                    Toast.makeText(this, "Sitio guardado en Realtime Database", Toast.LENGTH_SHORT).show()
                    limpiarCampos()
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error en Realtime Database: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this, "Error en Realtime Database: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun consultarSitio() {
        val idSitio = etIdSitio.text.toString().trim()

        if (idSitio.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa un ID de Sitio", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d(TAG, "Consultando sitio con ID: $idSitio")

        dbFirestore.collection("Sitios").document(idSitio)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    etNombre.setText(document.getString("nombre"))
                    etLatitud.setText(document.getDouble("latitud")?.toString() ?: "")
                    etLongitud.setText(document.getDouble("longitud")?.toString() ?: "")
                    etDescripcion.setText(document.getString("descripcion"))
                    Toast.makeText(this, "Sitio encontrado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Sitio no encontrado", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error en la consulta: ${e.message}")
                Toast.makeText(this, "Error en la consulta: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun limpiarCampos() {
        etIdSitio.setText("")
        etNombre.setText("")
        etLatitud.setText("")
        etLongitud.setText("")
        etDescripcion.setText("")
        etIdSitio.requestFocus()
    }
}
