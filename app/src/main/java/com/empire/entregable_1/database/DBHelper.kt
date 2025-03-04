package com.empire.entregable_1.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.empire.entregable_1.models.Clients
import com.empire.entregable_1.models.Reservation
import com.empire.entregable_1.models.Sites

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "reservas.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_RESERVAS = "reservas"
        private const val TABLE_CLIENTES = "clients"
        private const val TABLE_SITIOS = "sites"

        // Columnas de Reservas
        private const val COLUMN_ID = "id"
        private const val COLUMN_FECHA = "fecha"
        private const val COLUMN_COSTO = "costo"
        private const val COLUMN_DURACION = "duracion"
        private const val COLUMN_CLIENTE = "cliente"
        private const val COLUMN_SITIO = "sitio"

        // Columnas de Clientes
        private const val COLUMN_NOMBRE = "nombre"
        private const val COLUMN_APELLIDO = "apellido"

        // Columnas de Sitios
        private const val COLUMN_LATITUD = "latitud"
        private const val COLUMN_LONGITUD = "longitud"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createReservasTable = """
            CREATE TABLE $TABLE_RESERVAS (
                $COLUMN_ID TEXT PRIMARY KEY,
                $COLUMN_FECHA TEXT,
                $COLUMN_COSTO REAL,
                $COLUMN_DURACION INTEGER,
                $COLUMN_CLIENTE TEXT,
                $COLUMN_SITIO TEXT
            )
        """.trimIndent()

        val createClientesTable = """
            CREATE TABLE $TABLE_CLIENTES (
                $COLUMN_ID TEXT PRIMARY KEY,
                $COLUMN_NOMBRE TEXT,
                $COLUMN_APELLIDO TEXT
            )
        """.trimIndent()

        val createSitiosTable = """
            CREATE TABLE $TABLE_SITIOS (
                $COLUMN_ID TEXT PRIMARY KEY,
                $COLUMN_NOMBRE TEXT,
                $COLUMN_LATITUD REAL,
                $COLUMN_LONGITUD REAL
            )
        """.trimIndent()

        db.execSQL(createReservasTable)
        db.execSQL(createClientesTable)
        db.execSQL(createSitiosTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RESERVAS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CLIENTES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SITIOS")
        onCreate(db)
    }

    // Métodos para Reservas
    fun insertarReserva(reserva: Reservation): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, reserva.id)
            put(COLUMN_FECHA, reserva.fecha)
            put(COLUMN_COSTO, reserva.costo)
            put(COLUMN_DURACION, reserva.duracion)
            put(COLUMN_CLIENTE, reserva.cliente)
            put(COLUMN_SITIO, reserva.sitio)
        }
        val resultado = db.insert(TABLE_RESERVAS, null, values)
        db.close()
        return resultado != -1L
    }

    fun consultarReserva(id: String): Reservation? {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_RESERVAS WHERE $COLUMN_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(id))

        return if (cursor.moveToFirst()) {
            val reserva = Reservation(
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_COSTO)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DURACION)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENTE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SITIO))
            )
            cursor.close()
            db.close()
            reserva
        } else {
            cursor.close()
            db.close()
            null
        }
    }

    // Métodos para Clientes
    fun insertarCliente(client: Clients): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, client.id)
            put(COLUMN_NOMBRE, client.nombre)
            put(COLUMN_APELLIDO, client.apellido)
        }
        val resultado = db.insert(TABLE_CLIENTES, null, values)
        db.close()
        return resultado != -1L
    }

    fun consultarCliente(id: String): Clients? {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_CLIENTES WHERE $COLUMN_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(id))

        return if (cursor.moveToFirst()) {
            val cliente = Clients(
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_APELLIDO))
            )
            cursor.close()
            db.close()
            cliente
        } else {
            cursor.close()
            db.close()
            null
        }
    }

    // Métodos para Sitios
    fun insertarSitio(site: Sites): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, site.id)
            put(COLUMN_NOMBRE, site.nombre)
            put(COLUMN_LATITUD, site.latitud)
            put(COLUMN_LONGITUD, site.longitud)
        }
        val resultado = db.insert(TABLE_SITIOS, null, values)
        db.close()
        return resultado != -1L
    }

    fun consultarSitio(id: String): Sites? {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_SITIOS WHERE $COLUMN_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(id))

        return if (cursor.moveToFirst()) {
            val sitio = Sites(
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUD)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUD))
            )
            cursor.close()
            db.close()
            sitio
        } else {
            cursor.close()
            db.close()
            null
        }
    }

    // Métodos para obtener listas
    fun obtenerClientes(): List<String> {
        val clientesList = mutableListOf<String>()
        val db = readableDatabase

        val query = "SELECT $COLUMN_ID, $COLUMN_NOMBRE, $COLUMN_APELLIDO FROM $TABLE_CLIENTES"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE))
                val apellido = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_APELLIDO))
                clientesList.add("$id - $nombre $apellido")
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return clientesList
    }

    fun obtenerSitios(): List<String> {
        val sitiosList = mutableListOf<String>()
        val db = readableDatabase

        val query = "SELECT $COLUMN_ID, $COLUMN_NOMBRE FROM $TABLE_SITIOS"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE))
                sitiosList.add("$id - $nombre")
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return sitiosList
    }

    // Método para insertar datos de prueba

}