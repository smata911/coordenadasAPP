package com.example.basededatosejemplo.SQLITE

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseHelper(context:Context):SQLiteOpenHelper(context,
    ClientesContract.Companion.Entrada.NOMBRE_TABLA,null,
    ClientesContract.Companion.VERSION) {
   companion object{
       val CREATE_CLIENTES_TABALE="CREATE TABLE ${ClientesContract.Companion.Entrada.NOMBRE_TABLA}" +//cliente
               "(${ClientesContract.Companion.Entrada.COLUMNA_IDCLIENTE} TEXT PRIMARY KEY," +
               "${ClientesContract.Companion.Entrada.COLUMNA_NOMBRE} TEXT," +
               "${ClientesContract.Companion.Entrada.COLUMNA_UBICACION} TEXT," +
               "${ClientesContract.Companion.Entrada.COLUMNNA_TELEFONO} TEXT," +
               "${ClientesContract.Companion.Entrada.COLUMNA_ESTADO} TEXT ); "
       val REMOVE_CLIENTES_TABLA="DROP TABLE IF EXISTS ${ClientesContract.Companion.Entrada.NOMBRE_TABLA}"


       val CREATE_COORDENADAS ="CREATE TABLE ${ClientesContract.Companion.Cordenadas.NOMBRE_TABLAc}" +//Coordenadas
               "(nRegistro INTEGER PRIMARY KEY AUTOINCREMENT," +
               "${ClientesContract.Companion.Cordenadas.COLUMNA_ID} TEXT," +
               "${ClientesContract.Companion.Cordenadas.COLUMNA_X} TEXT," +
               "${ClientesContract.Companion.Cordenadas.COLUMNA_Y} TEXT,"+
               "${ClientesContract.Companion.Cordenadas.COLUMNA_FECHA} TEXT,"+

               " FOREIGN KEY(${ClientesContract.Companion.Cordenadas.COLUMNA_ID}) REFERENCES ${ClientesContract.Companion.Entrada.NOMBRE_TABLA}(${ClientesContract.Companion.Entrada.COLUMNA_IDCLIENTE}));"
       val REMOVE_COORDENADAS_TABLA="DROP TABLE IF EXISTS ${ClientesContract.Companion.Cordenadas.NOMBRE_TABLAc}"

       val CREATE_LICENCIA_TABLE="CREATE TABLE ${ClientesContract.Companion.Licencia.NOMBRE_TABLAL}" +//licencia
               "(${ClientesContract.Companion.Licencia.COLUMNA_ID} TEXT PRIMARY KEY," +
               "${ClientesContract.Companion.Licencia.COLUMNA_NOMBRE_LICENCIA} TEXT);"

       val REMOVE_LICENCIA_TABLA="DROP TABLE IF EXISTS ${ClientesContract.Companion.Licencia.NOMBRE_TABLAL}"


   }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_CLIENTES_TABALE)
        db?.execSQL(CREATE_COORDENADAS)
        db?.execSQL(CREATE_LICENCIA_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(REMOVE_CLIENTES_TABLA)//primero se elimina
        db?.execSQL(REMOVE_COORDENADAS_TABLA)
        db?.execSQL(REMOVE_LICENCIA_TABLA)
        onCreate(db)//crea la nueva

    }

}