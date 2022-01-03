package com.example.basededatosejemplo.SQLITE

import android.os.Build
import android.provider.BaseColumns
//aqui definimmos la estructura de la base de datos es decir el nombre de la base de datos y las columnas.
class ClientesContract {

    companion object{//globales
    val VERSION=1
        class Entrada:BaseColumns{//es nuestra clase a permitir mapear nuestras columnas en nuestras tablas
            companion object{
                  val NOMBRE_TABLA="clientes"
                  var COLUMNA_IDCLIENTE="idCliente"
                   var COLUMNA_NOMBRE="nombre"
            var COLUMNA_UBICACION="ubicacion"
            var COLUMNNA_TELEFONO="telefono"
            var COLUMNA_ESTADO="estado"
            }

        }
        class Cordenadas:BaseColumns{
            companion object{
                val COLUMNA_ID="idCoordenada"
                val COLUMNA_X="x"
                val COLUMNA_Y="y"
               val COLUMNA_FECHA="fecha"
                val NOMBRE_TABLAc="coordenadas"
            }
        }

        class Licencia:BaseColumns{
            companion object{
                val COLUMNA_ID="id"
                val COLUMNA_NOMBRE_LICENCIA="Nombre_licencia"
                val NOMBRE_TABLAL="licencia"
            }
        }
    }
}