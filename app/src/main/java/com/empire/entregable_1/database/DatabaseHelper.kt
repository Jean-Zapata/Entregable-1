package com.empire.entregable_1.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.empire.entregable_1.models.Sites

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "SitiosDB"
        private const val DATABASE_VERSION = 1

        private const val TABLE_SITES = "Sitios"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NOMBRE = "nombre"
        private const val COLUMN_LATITUD = "latitud"
        private const val COLUMN_LONGITUD = "longitud"
        private const val COLUMN_DESCRIPCION = "descripcion"

        private const val TABLE_CLIENTS = "clients"
        private const val COLUMN_CLIENT_ID = "id"
        private const val COLUMN_CLIENT_NAME = "nombre"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createSitesTable = """
            CREATE TABLE $TABLE_SITES (
                $COLUMN_ID TEXT PRIMARY KEY,
                $COLUMN_NOMBRE TEXT,
                $COLUMN_LATITUD REAL,
                $COLUMN_LONGITUD REAL,
                $COLUMN_DESCRIPCION TEXT
            )
        """.trimIndent()

        val createClientsTable = """
            CREATE TABLE $TABLE_CLIENTS (
                $COLUMN_CLIENT_ID TEXT PRIMARY KEY,
                $COLUMN_CLIENT_NAME TEXT
            )
        """.trimIndent()

        db.execSQL(createSitesTable)
        db.execSQL(createClientsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SITES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CLIENTS")
        onCreate(db)
    }

    fun insertarSitio(sitio: Sites): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, sitio.id)
            put(COLUMN_NOMBRE, sitio.nombre)
            put(COLUMN_LATITUD, sitio.latitud)
            put(COLUMN_LONGITUD, sitio.longitud)
            put(COLUMN_DESCRIPCION, sitio.descripcion)
        }
        val result = db.insert(TABLE_SITES, null, values)
        db.close()
        return result != -1L
    }

    fun insertarCliente(id: String, nombre: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CLIENT_ID, id)
            put(COLUMN_CLIENT_NAME, nombre)
        }
        val result = db.insert(TABLE_CLIENTS, null, values)
        db.close()
        return result != -1L
    }

    fun obtenerClientes(): List<String> {
        val clientes = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_CLIENT_NAME FROM $TABLE_CLIENTS", null)

        while (cursor.moveToNext()) {
            clientes.add(cursor.getString(0))
        }
        cursor.close()
        db.close()
        return clientes
    }

    fun obtenerSitios(): List<String> {
        val sitios = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_NOMBRE FROM $TABLE_SITES", null)

        while (cursor.moveToNext()) {
            sitios.add(cursor.getString(0))
        }
        cursor.close()
        db.close()
        return sitios
    }
}
