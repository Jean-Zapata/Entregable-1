package com.empire.entregable_1.models

data class Reservation(
    val id: String,
    val fecha: String,
    val costo: Double,
    val duracion: Int,
    val cliente: String, // Asegúrate de que sea tipo String
    val sitio: String    // Asegúrate de que sea tipo String
)
