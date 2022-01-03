package com.example.ubicacion.Modelo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.basededatosejemplo.Modelo.Clientes
import com.example.basededatosejemplo.Modelo.estructura

import com.example.basededatosejemplo.SQLITE.ClienteCRUD
import com.example.basededatosejemplo.Utilidades.HttpResponse
import com.example.basededatosejemplo.Utilidades.Network
import com.example.ubicacion.MainActivity
import com.google.gson.Gson

class CargarData2 {
//TODO AUN NO ACTUALIZA LAS RECICLER VIEW
    fun cargarDataCliente(activityp: AppCompatActivity){
        var crud: ClienteCRUD?=null
        var Clientes:ArrayList<estructura.Cliente>?=null

        crud = ClienteCRUD(activityp)
        Clientes = crud?.getAlumn()
        //prueba de cargar todos los datos desde el inicio
        val network = Network(activityp)//ojo
       // var activity: AppCompatActivity
        val activity: Context = activityp.applicationContext
        val gson = Gson()

        crud = ClienteCRUD(activity)
        Clientes = crud?.getAlumn()//la consulta ala base
        //hacemos el adapatar
        //configurarAdaptador(Clientes!!)
        //usamos la api para el llenado de sqlite
        network.httpRequest(activity,
                "https://ihcprueba2020.000webhostapp.com/apisCoordenadas/clientes/obtenertodos/",//todo pendiente
                object : HttpResponse {
                    override fun httpResponseSuccess(response: String) {
                        Log.d("RESPONSEEEEE", response)
                        val clientesApi: ArrayList<estructura.Cliente>? = gson.fromJson(response, com.example.basededatosejemplo.Modelo.Clientes::class.java).items//mapear de tipo array alumno en el arerglo items
                        for (id: estructura.Cliente in Clientes!!) {//limpiar la base de datos local
                            crud?.deleteAlumno(id)
                        }
                        for (id: estructura.Cliente in clientesApi!!) {///crea nueva lista por medio de la api
                            crud?.newCliente(estructura.Cliente(id.idCliente!!, id.nombre!!, id.telefono!!, id.ubicacion!!, id.estado!!))
                        }
                        Clientes = crud?.getAlumn()//llamar a tods los alumnos de nuevo slite
                        //configurarAdaptador(Clientes!!)
                    }

                    override fun httpErrorResponse(response: String) {//esto ees cucando no hay comunicion con los server
                        if (response.isEmpty()) {
                            Log.d("RESPONSEEEEE", response)
                            println("errorrrrrrrrrr")
                            Toast.makeText(activity, "error hacer la solicitud htttp", Toast.LENGTH_SHORT).show()
                            Clientes = crud?.getAlumn()//llamar a tods los alumnos de nuevo slite
                          ///  configurarAdaptador(Clientes!!)


                        }


                    }



                }
        )


    }



}