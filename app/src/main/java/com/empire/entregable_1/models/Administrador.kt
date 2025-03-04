package com.empire.entregable_1.models

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Administrador(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
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
    fun insertarCliente(
        nombre: String?,
        apellido: String?,
        dni: String?,
        telefono: String?,
        email: String?
    ): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("nombre", nombre)
        values.put("apellido", apellido)
        values.put("dni", dni)
        values.put("telefono", telefono)
        values.put("email", email)
        val resultado = db.insert("clients", null, values)
        db.close()
        return resultado
    }

    fun consultarCliente(dni: String): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM clients WHERE dni = ?", arrayOf(dni))
    }

    fun eliminarCliente(id: Int): Int {
        val db = this.writableDatabase
        val resultado = db.delete("clients", "id = ?", arrayOf(id.toString()))
        db.close()
        return resultado
    }

    // Métodos para Reservations
    fun insertarReserva(
        fecha: String?,
        costo: Double,
        duracion: Int,
        clienteId: Int,
        sitioId: Int
    ): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("fecha", fecha)
        values.put("costo", costo)
        values.put("duracion", duracion)
        values.put("cliente_id", clienteId)
        values.put("sitio_id", sitioId)
        val resultado = db.insert("reservations", null, values)
        db.close()
        return resultado
    }

    fun consultarReserva(id: String): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM reservations WHERE id = ?", arrayOf(id.toString()))
    }

    fun eliminarReserva(id: Int): Int {
        val db = this.writableDatabase
        val resultado = db.delete("reservations", "id = ?", arrayOf(id.toString()))
        db.close()
        return resultado
    }

    // Métodos para Sites
    fun insertarSitio(
        nombre: String?,
        latitud: Double,
        longitud: Double,
        descripcion: String?
    ): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("nombre", nombre)
        values.put("latitud", latitud)
        values.put("longitud", longitud)
        values.put("descripcion", descripcion)
        val resultado = db.insert("sites", null, values)
        db.close()
        return resultado
    }

    fun consultarSitioPorNombre(nombre: String): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM sites WHERE nombre = ?", arrayOf(nombre))
    }


    fun eliminarSitio(id: Int): Int {
        val db = this.writableDatabase
        val resultado = db.delete("sites", "id = ?", arrayOf(id.toString()))
        db.close()
        return resultado
    }

    // Métodos auxiliares para obtener listas
    fun obtenerClientes(): List<String> {
        val clientes: MutableList<String> = ArrayList()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT id, nombre, apellido FROM clients", null)

        if (cursor.moveToFirst()) {
            do {
                clientes.add(
                    cursor.getInt(0)
                        .toString() + " - " + cursor.getString(1) + " " + cursor.getString(2)
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return clientes
    }

    fun obtenerSitios(): List<String> {
        val sitios: MutableList<String> = ArrayList()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT id, nombre FROM sites", null)

        if (cursor.moveToFirst()) {
            do {
                sitios.add(cursor.getInt(0).toString() + " - " + cursor.getString(1))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return sitios
    }

    companion object {
        private const val DATABASE_NAME = "reservations.db"
        private const val DATABASE_VERSION = 1

        private val CREATE_TABLE_RESERVATIONS = """
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
        """.trimIndent()

        private val CREATE_TABLE_CLIENTS = """
        CREATE TABLE clients (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre TEXT,
            apellido TEXT,
            dni TEXT UNIQUE,
            telefono TEXT,
            email TEXT
        )
        """.trimIndent()

        private val CREATE_TABLE_SITES = """
        CREATE TABLE sites (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre TEXT,
            latitud REAL,
            longitud REAL,
            descripcion TEXT
        )
        """.trimIndent()
    }
}