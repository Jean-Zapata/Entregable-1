package com.empire.entregable_1.models

// Clase Cliente

class Clients {
    // Getters y Setters
    var id: String? = null
    var nombre: String? = null
    var apellido: String? = null
    var dui: String? = null

    constructor()

    constructor(id: String?, nombre: String?, apellido: String?, dui: String?) {
        this.id = id
        this.nombre = nombre
        this.apellido = apellido
        this.dui = dui
    }
}