package com.empire.entregable_1.models

data class Clients(
    var id: String = "",
    var nombre: String = "",
    var apellido: String = "",
    var dni: String = "",
    var telefono: String = "",
    var email: String = ""
) {
    constructor() : this("", "", "", "", "", "")
}
