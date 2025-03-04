package com.empire.entregable_1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.empire.entregable_1.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var latitud: Double = 0.0
    private var longitud: Double = 0.0
    private var sitioNombre: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtenemos las coordenadas desde el intent
        latitud = intent.getDoubleExtra("LATITUD", 0.0)
        longitud = intent.getDoubleExtra("LONGITUD", 0.0)
        sitioNombre = intent.getStringExtra("SITIO_NOMBRE") ?: "Sitio"

        // Obtenemos el fragmento del mapa
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Añadimos un marcador en la ubicación del sitio
        val sitioUbicacion = LatLng(latitud, longitud)
        mMap.addMarker(MarkerOptions().position(sitioUbicacion).title(sitioNombre))

        // Movemos la cámara a la ubicación del sitio con un zoom adecuado
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sitioUbicacion, 15f))
    }
}