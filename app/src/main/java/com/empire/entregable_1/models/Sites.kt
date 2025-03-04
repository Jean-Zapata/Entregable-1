package com.empire.entregable_1.models

data class Sites(
    val idSitio: String, // Cambiado a Int en lugar de String
    val nombre: String,
    val latitud: Double,
    val longitud: Double,
    val descripcion: String
)
