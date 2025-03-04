package com.empire.entregable_1.models

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Administrador(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_RESERVATIONS)
        db.execSQL(CREATE_TABLE_CLIENTS)
        db.execSQL(CREATE_TABLE_SITES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS reservations")
        db.execSQL("DROP TABLE IF EXISTS clients")
        db.execSQL("DROP TABLE IF EXISTS sites")
        onCreate(db)
    }

    // Métodos para Clients
    fun insertarCliente(nombre: String, apellido: String, dni: String, telefono: String, email: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("apellido", apellido)
            put("dni", dni)
            put("telefono", telefono)
            put("email", email)
        }
        return db.insert("clients", null, values)
    }

    fun consultarCliente(dni: String): Cursor? {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM clients WHERE dni = ?", arrayOf(dni))
    }

    fun actualizarCliente(id: Int, nombre: String, apellido: String, dni: String, telefono: String, email: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("apellido", apellido)
            put("dni", dni)
            put("telefono", telefono)
            put("email", email)
        }
        return db.update("clients", values, "id = ?", arrayOf(id.toString()))
    }

    fun eliminarCliente(id: Int): Int {
        val db = writableDatabase
        return db.delete("clients", "id = ?", arrayOf(id.toString()))
    }

    // Métodos para Reservations
    fun insertarReserva(fecha: String, costo: Double, duracion: Int, clienteId: Int, sitioId: Int): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("fecha", fecha)
            put("costo", costo)
            put("duracion", duracion)
            put("cliente_id", clienteId)
            put("sitio_id", sitioId)
        }
        return db.insert("reservations", null, values)
    }

    fun consultarReserva(id: Int): Cursor? {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM reservations WHERE id = ?", arrayOf(id.toString()))
    }

    fun eliminarReserva(id: Int): Int {
        val db = writableDatabase
        return db.delete("reservations", "id = ?", arrayOf(id.toString()))
    }

    // Métodos para Sites
    fun insertarSitio(nombre: String, latitud: Double, longitud: Double, descripcion: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("latitud", latitud)
            put("longitud", longitud)
            put("descripcion", descripcion)
        }
        return db.insert("sites", null, values)
    }

    fun consultarSitio(id: Int): Cursor? {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM sites WHERE id = ?", arrayOf(id.toString()))
    }

    fun eliminarSitio(id: Int): Int {
        val db = writableDatabase
        return db.delete("sites", "id = ?", arrayOf(id.toString()))
    }

    companion object {
        private const val DATABASE_NAME = "reservations.db"
        private const val DATABASE_VERSION = 1

        private const val CREATE_TABLE_RESERVATIONS = """
            CREATE TABLE reservations (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                fecha TEXT,
                costo REAL,
                duracion INTEGER,
                cliente_id INTEGER,
                sitio_id INTEGER,
                FOREIGN KEY (cliente_id) REFERENCES clients(id),
                FOREIGN KEY (sitio_id) REFERENCES sites(id)
            )
        """

        private const val CREATE_TABLE_CLIENTS = """
            CREATE TABLE clients (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT,
                apellido TEXT,
                dni TEXT UNIQUE,
                telefono TEXT,
                email TEXT
            )
        """

        private const val CREATE_TABLE_SITES = """
            CREATE TABLE sites (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT,
                latitud REAL,
                longitud REAL,
                descripcion TEXT
            )
        """
    }
}
