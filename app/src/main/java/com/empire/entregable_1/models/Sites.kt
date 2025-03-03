package com.empire.entregable_1.models

class Sites {
    // Getters y Setters
    var id: String? = null
    var nombre: String? = null
    var latitud: Double = 0.0
    var longitud: Double = 0.0

    constructor()

    constructor(id: String?, nombre: String?, latitud: Double, longitud: Double) {
        this.id = id
        this.nombre = nombre
        this.latitud = latitud
        this.longitud = longitud
    }
}
