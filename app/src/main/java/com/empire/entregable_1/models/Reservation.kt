package com.empire.entregable_1.models

class Reservation {
    // Getters y Setters
    var id: String? = null
    var fecha: String? = null
    var costo: Double = 0.0
    var duracion: Int = 0
    var clienteId: String? = null
    var sitioId: String? = null

    constructor()

    constructor(
        id: String?,
        fecha: String?,
        costo: Double,
        duracion: Int,
        clienteId: String?,
        sitioId: String?
    ) {
        this.id = id
        this.fecha = fecha
        this.costo = costo
        this.duracion = duracion
        this.clienteId = clienteId
        this.sitioId = sitioId
    }
}
