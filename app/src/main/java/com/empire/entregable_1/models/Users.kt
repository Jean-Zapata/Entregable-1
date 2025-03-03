package com.empire.entregable_1.models

class Users {
    var uid: String = "" // Nuevo campo para el UID
    var gmail: String = ""
    var password: String = ""

    // Constructor primario
    constructor(uid: String, gmail: String, password: String) {
        this.uid = uid
        this.gmail = gmail
        this.password = password
    }

    // Constructor secundario (por defecto)
    constructor() : this("", "", "") // Inicializa con valores vacíos
}