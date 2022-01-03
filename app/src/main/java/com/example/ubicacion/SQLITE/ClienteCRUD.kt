package com.example.basededatosejemplo.SQLITE

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.widget.Toast
import com.example.basededatosejemplo.Modelo.estructura
import com.example.manejodemensajes.Mensajes.Errores
import com.example.manejodemensajes.Mensajes.Mensaje
import java.lang.Exception

//import com.example.basededatosejemplo.Modelo.Cliente

class ClienteCRUD(context:Context) {
    private var helper: DataBaseHelper?=null
    init {
        helper= DataBaseHelper(context)
    }
    fun newCliente(item: estructura.Cliente){
        //abrir
        val db=helper?.writableDatabase!!//para poder escribir en la base
        val values=ContentValues()//es clase para agrupar la informacion
        //mapeos de la colunan valor a insertar
        values.put(ClientesContract.Companion.Entrada.COLUMNA_IDCLIENTE,item.idCliente)//item.id es del la clase alumno
        values.put(ClientesContract.Companion.Entrada.COLUMNA_NOMBRE,item.nombre)//item.id es del la clase alumno
        values.put(ClientesContract.Companion.Entrada.COLUMNA_UBICACION,item.ubicacion)
        values.put(ClientesContract.Companion.Entrada.COLUMNNA_TELEFONO,item.telefono)
        values.put(ClientesContract.Companion.Entrada.COLUMNA_ESTADO,item.estado)
        //insertar una nueva columna
        val newRoWId=db.insert(ClientesContract.Companion.Entrada.NOMBRE_TABLA,null,values)//el values es la agrupacion
        db.close()
    }
    fun newCoordenadas(item: estructura.Coordenada){
        val db=helper?.writableDatabase!!//para poder escribir en la base
        val values=ContentValues()//es clase para agrupar la informacion
        //mapeos de la colunan valor a insertar
        values.put(ClientesContract.Companion.Cordenadas.COLUMNA_ID,item.id)//item.id es del la clase alumno
        values.put(ClientesContract.Companion.Cordenadas.COLUMNA_X,item.x)//item.id es del la clase alumno
        values.put(ClientesContract.Companion.Cordenadas.COLUMNA_Y,item.y)
        values.put(ClientesContract.Companion.Cordenadas.COLUMNA_FECHA,item.fecha)

        //insertar una nueva columna
        val newRoWId=db.insert(ClientesContract.Companion.Cordenadas.NOMBRE_TABLAc,null,values)//el values es la agrupacion
        db.close()
    }

    fun newLicencia(item: estructura.licencia){
        val db=helper?.writableDatabase!!//para poder escribir en la base
        val values=ContentValues()//es clase para agrupar la informacion
        //mapeos de la colunan valor a insertar
        values.put(ClientesContract.Companion.Licencia.COLUMNA_ID,item.id)//item.id es del la clase alumno
        values.put(ClientesContract.Companion.Licencia.COLUMNA_NOMBRE_LICENCIA,item.licencia)
        val newRoWId=db.insert(ClientesContract.Companion.Licencia.NOMBRE_TABLAL,null,values)//el values es la agrupacion
        db.close()
    }
    fun getAlumn():ArrayList<estructura.Cliente>{
        val items:ArrayList<estructura.Cliente> =ArrayList()
        //abrir base en modo lectura
        val db:SQLiteDatabase=helper?.readableDatabase!!//de tipo lectura
        //especificar las columnas
        val columnas= arrayOf(
            ClientesContract.Companion.Entrada.COLUMNA_IDCLIENTE,
            ClientesContract.Companion.Entrada.COLUMNA_NOMBRE,
                ClientesContract.Companion.Entrada.COLUMNA_UBICACION,
                ClientesContract.Companion.Entrada.COLUMNNA_TELEFONO,
                        ClientesContract.Companion.Entrada.COLUMNA_ESTADO
        )//ALMACENA EN UN ARREGLO LO QUE QUIERO CONSULTAR DE LAS COLUMNANS
        //CREAR UN CURSOS PARA RECORRER LA TABLA
        val c:Cursor =db.query(
            ClientesContract.Companion.Entrada.NOMBRE_TABLA,
           columnas,
          null,
          null,
          null,//agrupar
          null,
          null//order by
        )
        //hacer el recorrido en la tabla
        while (c.moveToNext()){
            items.add(
                    estructura.Cliente(
                            c.getString(c.getColumnIndexOrThrow(ClientesContract.Companion.Entrada.COLUMNA_IDCLIENTE)),//GETString porque es tipo string
                            c.getString(c.getColumnIndexOrThrow(ClientesContract.Companion.Entrada.COLUMNA_NOMBRE)),
                            c.getString(c.getColumnIndexOrThrow(ClientesContract.Companion.Entrada.COLUMNA_UBICACION)),
                            c.getString(c.getColumnIndexOrThrow(ClientesContract.Companion.Entrada.COLUMNNA_TELEFONO)),
                            c.getString(c.getColumnIndexOrThrow(ClientesContract.Companion.Entrada.COLUMNA_ESTADO))
                    )
            )
        }
        //cerrarr la base
        db.close()
        return items


    }
    fun getCordenadas():ArrayList<estructura.Coordenada>{
        val items:ArrayList<estructura.Coordenada> =ArrayList()
        //abrir base en modo lectura
        val db:SQLiteDatabase=helper?.readableDatabase!!//de tipo lectura
        //especificar las columnas
        val columnas= arrayOf(
                ClientesContract.Companion.Cordenadas.COLUMNA_ID,
                ClientesContract.Companion.Cordenadas.COLUMNA_X,
                ClientesContract.Companion.Cordenadas.COLUMNA_Y,
            ClientesContract.Companion.Cordenadas.COLUMNA_FECHA


        )//ALMACENA EN UN ARREGLO LO QUE QUIERO CONSULTAR DE LAS COLUMNANS
        //CREAR UN CURSOS PARA RECORRER LA TABLA
        val c:Cursor =db.query(
                ClientesContract.Companion.Cordenadas.NOMBRE_TABLAc,
                columnas,
                null,
                null,
                null,//agrupar
                null,
                null//order by
        )
        //hacer el recorrido en la tabla
        while (c.moveToNext()){
            items.add(
                    estructura.Coordenada(
                            c.getString(c.getColumnIndexOrThrow(ClientesContract.Companion.Cordenadas.COLUMNA_ID)),//GETString porque es tipo string
                            c.getString(c.getColumnIndexOrThrow(ClientesContract.Companion.Cordenadas.COLUMNA_Y)),
                            c.getString(c.getColumnIndexOrThrow(ClientesContract.Companion.Cordenadas.COLUMNA_X)),
                            c.getString(c.getColumnIndexOrThrow(ClientesContract.Companion.Cordenadas.COLUMNA_FECHA))
                    )
            )
        }
        //cerrarr la base
        db.close()
        return items


    }
    //POR ID
    fun getALumno(id:String): estructura.Cliente {
        var item: estructura.Cliente?=null
        val db:SQLiteDatabase=helper?.readableDatabase!!
        val columnas= arrayOf(
            ClientesContract.Companion.Entrada.COLUMNA_IDCLIENTE,
            ClientesContract.Companion.Entrada.COLUMNA_NOMBRE,
                ClientesContract.Companion.Entrada.COLUMNA_UBICACION,
                        ClientesContract.Companion.Entrada.COLUMNNA_TELEFONO,
                        ClientesContract.Companion.Entrada.COLUMNA_ESTADO
        )//ALMACENA EN UN ARREGLO LO QUE QUIERO CONSULTAR DE LAS COLUMNANS

        val c:Cursor =db.query(
            ClientesContract.Companion.Entrada.NOMBRE_TABLA,//nombre de la tabla
            columnas,//las columnas a mostrar
            "idCliente=?",//este es el filtro aqui se prepara y en el arrayof el parametro
            arrayOf(id),
            null,//agrupar
            null,
            null

        )
        while (c.moveToNext()){
            item= estructura.Cliente(c.getString(c.getColumnIndexOrThrow(ClientesContract.Companion.Entrada.COLUMNA_IDCLIENTE)),
                    c.getString(c.getColumnIndexOrThrow(ClientesContract.Companion.Entrada.COLUMNA_NOMBRE)),
                    c.getString(c.getColumnIndexOrThrow(ClientesContract.Companion.Entrada.COLUMNA_UBICACION)),
                    c.getString(c.getColumnIndexOrThrow(ClientesContract.Companion.Entrada.COLUMNNA_TELEFONO)),
                    c.getString(c.getColumnIndexOrThrow(ClientesContract.Companion.Entrada.COLUMNA_ESTADO))
            )
        }
        c.close()

        return  item!!


    }

    fun getLicencia(id:String): estructura.licencia? {
        try {

        var item: estructura.licencia?=null
        val db:SQLiteDatabase=helper?.readableDatabase!!
        val columnas= arrayOf(
            ClientesContract.Companion.Licencia.COLUMNA_ID,
            ClientesContract.Companion.Licencia.COLUMNA_NOMBRE_LICENCIA
        )//ALMACENA EN UN ARREGLO LO QUE QUIERO CONSULTAR DE LAS COLUMNANS

     val c: Cursor = db.query(
        ClientesContract.Companion.Licencia.NOMBRE_TABLAL,//nombre de la tabla
        columnas,//las columnas a mostrar
        "id=?",//este es el filtro aqui se prepara y en el arrayof el parametro
        arrayOf(id),
        null,//agrupar
        null,
        null

    )
    while (c.moveToNext()) {
        item = estructura.licencia(
            c.getString(c.getColumnIndexOrThrow(ClientesContract.Companion.Licencia.COLUMNA_ID)),
            c.getString(c.getColumnIndexOrThrow(ClientesContract.Companion.Licencia.COLUMNA_NOMBRE_LICENCIA))
        )
    }
           c.close()
            return item!!
        }catch (ex:Exception){
            //Toast.makeText(context, "no se recupero la lista", Toast.LENGTH_SHORT).show()
            return null
        }



}

            //Toast.makeText(, "no se recupero la lista", Toast.LENGTH_SHORT).show()





    fun updateAlumno(item: estructura.Cliente){
        val db:SQLiteDatabase=helper?.writableDatabase!!
        val values=ContentValues()
        values.put(ClientesContract.Companion.Entrada.COLUMNA_IDCLIENTE,item.idCliente)
        values.put(ClientesContract.Companion.Entrada.COLUMNA_NOMBRE,item.nombre)
        values.put(ClientesContract.Companion.Entrada.COLUMNA_UBICACION,item.ubicacion)
        values.put(ClientesContract.Companion.Entrada.COLUMNNA_TELEFONO,item.telefono)
        values.put(ClientesContract.Companion.Entrada.COLUMNA_ESTADO,item.estado)
        db.update(
            ClientesContract.Companion.Entrada.NOMBRE_TABLA,
            values,
            "idCliente=?",
            arrayOf(item.idCliente)
        )
        db.close()


    }
    fun deleteAlumno(item: estructura.Cliente){
        val db:SQLiteDatabase=helper?.writableDatabase!!
        db.delete(
            ClientesContract.Companion.Entrada.NOMBRE_TABLA,
                  "idCliente=?",
                   arrayOf(item.idCliente))
        db.close()
    }

    fun deleteCoordenada(){
        val db:SQLiteDatabase=helper?.writableDatabase!!
        db.delete(
                ClientesContract.Companion.Cordenadas.NOMBRE_TABLAc,
                null,
                null)
        db.close()
    }

    fun deletelicencia(){
        val db:SQLiteDatabase=helper?.writableDatabase!!
        db.delete(
            ClientesContract.Companion.Licencia.NOMBRE_TABLAL,
            null,
            null)
        db.close()
    }
}