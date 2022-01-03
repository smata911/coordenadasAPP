package com.example.basededatosejemplo.Utilidades

import android.content.Context
import android.net.ConnectivityManager
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError

import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.basededatosejemplo.Utilidades.HttpResponse
import com.example.manejodemensajes.Mensajes.Errores
import com.example.manejodemensajes.Mensajes.Mensaje

class Network(var activity: AppCompatActivity) {
    init {

    }


    fun hayRed():Boolean{
        val connetivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connetivityManager.activeNetworkInfo//esta obsoleto
        return networkInfo!=null && networkInfo.isConnected//esta obsoleto
    }


     fun httpRequest(context:Context,url: String,httpResponse: HttpResponse){
        if (hayRed()){
            val queue:RequestQueue= Volley.newRequestQueue(context)
            val solicitud= StringRequest(Request.Method.GET, url, { response: String ->
                httpResponse.httpResponseSuccess(response)
            }, { error: VolleyError ->
                Log.d("HTTP_REQUEST", error.message.toString())
                //Mensaje.mensajeError(context,Error.HTTP_ERROR)
                //todo 2
                // httpResponse.httpErrorResponse(error.message.toString())
                Mensaje.mensajeError(context,Errores.NO_HAY_RED)
            })
            queue.add(solicitud)
            }else{
              //  Mensaje.mensajeError(context,Error.NO_HAY_RED)
            Mensaje.mensajeError(context, Errores.ERROR_SOLICITU_HTTP)
            }
    }



    fun httpPOSTRequest(context: Context,url:String,httpResponse: HttpResponse){

        if (hayRed()){
            val queue= Volley.newRequestQueue(context)
            val solicitud= StringRequest(Request.Method.POST,url,Response.Listener <String>{
                response ->
                httpResponse.httpResponseSuccess(response)

            },Response.ErrorListener { error ->
                Log.d("http", error.message.toString())
                //Messaje.mensajeError(context, Errores.HTTP_ERROR)
            })
            queue.add(solicitud)

        }else{
           //Message.messajeError(context, Errores.HTTP_NO_HAY_RES)
        }
    }
}