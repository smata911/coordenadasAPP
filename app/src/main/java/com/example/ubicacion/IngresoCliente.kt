package com.example.ubicacion

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import com.example.basededatosejemplo.Modelo.estructura
import com.example.basededatosejemplo.Modelo.HttpApiResponse
import com.example.basededatosejemplo.SQLITE.ClienteCRUD
import com.example.basededatosejemplo.Utilidades.HttpResponse
import com.example.basededatosejemplo.Utilidades.Network
import com.example.manejodemensajes.Mensajes.Errores
import com.example.manejodemensajes.Mensajes.Mensaje

import com.example.ubicacion.Modelo.VistaClientes
import com.google.gson.Gson
import kotlin.math.min

class IngresoCliente : AppCompatActivity() {
   var crud: ClienteCRUD?=null
    val listEstado= arrayOf("ACTIVO","INACTIVO")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingreso_cliente)

        val toolbar=findViewById<Toolbar>(R.id.toolbarCliente)//ojo con las librerias
       // actionBar?.title="ESTAS EN PANTALLA PRINCIPAL"

        setSupportActionBar(toolbar)
        title = getString(R.string.TITULO_INGRESAR_CLIENTE_STRING)

val idCliente=findViewById<EditText>(R.id.edIdentidad)

       val nombre=findViewById<EditText>(R.id.edNombre)
       val telefono=findViewById<EditText>(R.id.edTelefono)
       val ubicacion=findViewById<EditText>(R.id.edUbicacion)
       val estado=findViewById<TextView>(R.id.edEstado)
       val btnGuardar=findViewById<Button>(R.id.btnGuardarCliente)
        val btnGuardaLocal=findViewById<Button>(R.id.btnGuardarLocal)
        /*todo aun falta esta validadcion
idCliente.addTextChangedListener() {

val valor:Int=13
    var IT=it
    var valor2=IT!!.length
    if (idCliente.length().toInt() < valor && valor2>2) {


        var TOTAL= (valor.toInt())-(valor2)
      // // Toast.makeText(this, "AUN TE FALTAN CARACTERES. $TOTAL", Toast.LENGTH_SHORT).show()
        Toast.makeText(this, R.string.ERROR_FALTACARACTERES_STRING +TOTAL, Toast.LENGTH_SHORT).show()
        idCliente.requestFocus()

    }
}

         */

//TODO EVENTO ESTADO
        estado.setOnClickListener(){
     val dfechas=AlertDialog.Builder(this)
    // dfechas.setMessage("SELECCIONA UN EL ESTADO")
            dfechas.setItems(listEstado){_,pos->
                val fecha=listEstado[pos]
                estado.setText(fecha)


            }
     val alert=dfechas.create()
     alert.setTitle(R.string.TITULO_ESTADO_STRING)
     alert.show()
        }
    //________________________________________________________________________________________
       crud = ClienteCRUD(this)
        btnGuardaLocal.setOnClickListener {
            showAlertINFO()
            //todo PENDIENTE. NO FUNCIONA PORQUE SIEMPRE SE ACTUALIZA LOS DATOS DESDE LA NUBE
            /*Mensaje.mensajeSuccess(applicationContext,Errores.EXITO_SUCCES)
            crud?.newCliente(estructura.Cliente(idCliente.text.toString(), nombre.text.toString(), telefono.text.toString(), ubicacion.text.toString(), estado.text.toString()))
            startActivity(Intent(this,VistaClientes::class.java))
            finish()

             */
        }
       //crud = AlumnoCRUD(this)
       btnGuardar.setOnClickListener () {
           //validar
           var campos = ArrayList<String>()
           campos.add(idCliente.text.toString())
           campos.add(nombre.text.toString())
           campos.add(telefono.text.toString())
           campos.add(ubicacion.text.toString())
           campos.add(estado.text.toString())
           var permitir = 0

           for (campo in campos) {
               if (campo.isNullOrEmpty())//si esta vacio uno o varios
                   permitir++
           }
           if (permitir > 0) {
               //Toast.makeText(this, "llena todos los campos", Toast.LENGTH_SHORT).show()
               Mensaje.mensajeError(applicationContext, Errores.CAMPO_VACIO)
           } else {

               val context: Context = this.applicationContext
               var network = Network(this)
               if (network.hayRed()) {
                   val query: String = "?idCliente=" + idCliente.text.toString() + "&nombre=" + nombre.text.toString() + "&ubicacion=" + ubicacion.text.toString() + "&telefono=" + telefono.text.toString() + "&estado=" + estado.text.toString()
                   // var url:String="http://192.168.1.52/apisCoordenadas/clientes/nuevoalumno" +query
                   var url: String = "https://ihcprueba2020.000webhostapp.com/apisCoordenadas/clientes/nuevoalumno/index.php" + query
                   network.httpRequest(context, url, object : HttpResponse {

                       //HTTPRESPONSE HACE REFERENCIA A NUESTRO INTERFAZ
                       override fun httpResponseSuccess(response: String) {
                           val gson = Gson()//PARA EL JSON
                           val message: HttpApiResponse = gson.fromJson(
                                   response,
                                   HttpApiResponse::class.java
                           )//RESPONSE OBTIENE EL JSON Y LO MAPEA EN HTTPAPIRESPONSE DE TIPO ARRAY

                         if (message.response=="Error al registrar alumno") {//RECOMIENDO QUE LA VALIDACION SEA ALREVES EL MENJA
                             Toast.makeText(context, message.response+". VERIFICA EL ID", Toast.LENGTH_SHORT).show()//MUESTRA EL MENSAJE DE NUESTRAS APIS
                         }else {
                             idCliente.setText("")
                             nombre.setText("")
                             telefono.setText("")
                             ubicacion.setText("")
                             estado.setText("")

                             //Mensaje.mensajeSuccess(context, Errores.EXITO_SUCCES)
                             startActivity(Intent(context, VistaClientes::class.java))
                         }
                       }

                       override fun httpErrorResponse(response: String) {

                           Log.e("Error response", response)
                           Mensaje.mensajeError(applicationContext, Errores.ERROR_SOLICITU_HTTP)
                          // Toast.makeText(applicationContext, response, Toast.LENGTH_SHORT).show()


                       }


                   })
                   //limpiar()

               }else{

                   Mensaje.mensajeError(context,Errores.NO_HAY_RED)
               }
           }
       }
    }



    private fun showAlertINFO(){//AUN NO USAMOS ESTA ALERTA
        val builder= AlertDialog.Builder(this)
        builder.setTitle("INFORMACIÓN")
        builder.setMessage(R.string.ERROR_MANTENIMIENTO_STRING)
        builder.setPositiveButton("ACEPTAR",null)
        val dialog: AlertDialog =builder.create()
        dialog.show()
    }


    override fun onBackPressed() {
        super.onBackPressed()

        startActivity(Intent(this,inicioMenu::class.java))
        finish()

    }
}