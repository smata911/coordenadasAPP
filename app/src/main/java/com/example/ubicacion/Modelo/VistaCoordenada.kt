package com.example.ubicacion.Modelo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.basededatosejemplo.Modelo.HttpApiResponse
import com.example.basededatosejemplo.Modelo.estructura
import com.example.basededatosejemplo.Recyclerview.AdaptadorCustom

import com.example.basededatosejemplo.Recyclerview.AdaptadorCustomC
import com.example.basededatosejemplo.Recyclerview.LongClickListener
import com.example.basededatosejemplo.Recyclerview.clickListener
import com.example.basededatosejemplo.SQLITE.ClienteCRUD
import com.example.basededatosejemplo.Utilidades.HttpResponse
import com.example.basededatosejemplo.Utilidades.Network
import com.example.manejodemensajes.Mensajes.Errores
import com.example.manejodemensajes.Mensajes.Mensaje
import com.example.ubicacion.MainActivity
import com.example.ubicacion.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import java.lang.Exception

class VistaCoordenada : AppCompatActivity() {
    var lista: RecyclerView?=null
    var adaptador: AdaptadorCustomC?=null
    var layouManager: RecyclerView.LayoutManager?=null//administrar el layou ya se horizontal o verrtical o en pinteres
    var crud: ClienteCRUD?=null
    var Coordenadas:ArrayList<estructura.Coordenada>?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vista_coordenada)




        val fab = findViewById<FloatingActionButton>(R.id.fabC)
        val toolbar=findViewById<Toolbar>(R.id.toolbartoolbarVistaCoordenada)//ojo con las librerias
        setSupportActionBar(toolbar)
        title = getString(R.string.TITULO_VISTA_COORDENADA)
        lista = findViewById(R.id.listaC)
        lista?.setHasFixedSize(true)//para no sea lenta la listas
        layouManager = LinearLayoutManager(this)
        lista?.layoutManager = layouManager//aqui decimos que a la lista tendra el layoumager

        fab.setOnClickListener() {
            var valor:String?=null
            if(intent.hasExtra("ocultar")){//RECORDEMO QUE EN NUEVO NO SE ENVIA NINGUN PARAMERATRO
                var valor=intent.getStringExtra("ocultar")!!

            if (valor=="ocultar"){
                Toast.makeText(this, "sorr", Toast.LENGTH_SHORT).show()
            }else if (valor=="mostrar"){
            startActivity(Intent(this, VistaClientes::class.java))
            }

            }
        }
        //todo 3 main activitu
        val network = Network(this)//ojo
        val activity: Context = this.applicationContext
        val gson = Gson()

        crud = ClienteCRUD(this)
        Coordenadas = crud?.getCordenadas()//la consulta ala base



        //hacemos el adapatar
        //adaptador=AdaptadorCustom(this, platillos)//cuando no hay un evento click
        adaptador=AdaptadorCustomC(Coordenadas!!, object : clickListener {
            override fun onCick(
                vista: View,
                index: Int
            ) {//esta es la funcion que se creo exactamente en el interfaz de clickListener
                Toast.makeText(applicationContext, "Se hizo un clicK", Toast.LENGTH_SHORT).show()
                //val intent=Intent(applicationContext, MainActivity::class.java)
                //intent.putExtra("IDCLIENTE",Coordenadas!!.get(index).idCliente)
                //startActivity(intent)


            }


        }, object : LongClickListener {
            override fun longClick(vista: View, index: Int) {
                Toast.makeText(applicationContext, "Es un CLICK alargacdo", Toast.LENGTH_SHORT).show()


            }


        })
        lista?.adapter=adaptador






    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {//control+0. override que ya existe esa funcion
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.nube_coordenadas, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {//para utilizar los eventos de los items de menu simple
        return when (item.itemId) {
            R.id.opSubir -> {
                //validar net
                var network = Network(this)
                if (network.hayRed()) {
                    showAlertInternetEstable()//pregunta si cuenta internet estable y envia todos los datos al server hay dos fun
                }else{
                    Mensaje.mensajeError(applicationContext,Errores.NO_HAY_RED)
                }


              true

            }
            else -> return super.onOptionsItemSelected(item)
        }
    }




private  fun showAlertInternetEstable(){
    val dialogo=AlertDialog.Builder(this)
    dialogo.setTitle("Confirmacion")
    dialogo.setMessage(R.string.VEFIFICAR_INTERNET_STRING)
    dialogo.setPositiveButton("si"){DialogInterface,i->
       // Toast.makeText(applicationContext, "aqui va todo el procedimiento si selecciona si", Toast.LENGTH_SHORT).show()
        // TODO: 29/10/2020 aqui va la qery de eliminacion
        guardarNubeCoordenada()
    }
    dialogo.setNegativeButton("no"){DialogInterface,i->
        DialogInterface.dismiss()//desaparecer mensaje
    }
    dialogo.show()
    true
}


    private fun guardarNubeCoordenada() {
        try {


            val can: Int = Coordenadas?.count()!!

            for (x in 0..(can!!) - 1) {
                val context: Context = this.applicationContext
                var network = Network(this)
                val query: String = "?idCliente=" + Coordenadas!!.get(x).id + "&x=" + Coordenadas!!.get(x).x + "&y=" + Coordenadas!!.get(x).y
                // var url:String="http://192.168.1.52/apisCoordenadas/coordenasTerreno/nuevoalumno" +query
                var url: String = "https://ihcprueba2020.000webhostapp.com/apisCoordenadas/coordenasTerreno/nuevoalumno/index.php" + query
                network.httpRequest(context, url, object : HttpResponse {
                    //HTTPRESPONSE HACE REFERENCIA A NUESTRO INTERFAZ
                    override fun httpResponseSuccess(response: String) {
                        val gson = Gson()//PARA EL JSON
                        val message: HttpApiResponse = gson.fromJson(response, HttpApiResponse::class.java)//RESPONSE OBTIENE EL JSON Y LO MAPEA EN HTTPAPIRESPONSE DE TIPO ARRAY
                        Toast.makeText(context, message.response, Toast.LENGTH_SHORT).show()//MUESTRA EL MENSAJE DE NUESTRAS APIS
                        //startActivity(Intent(context, MainActivity::class.java))
                    }

                    override fun httpErrorResponse(response: String) {
                        Log.e("Error response", response)
                        // Toast.makeText(context, "hubo un problema al enviar la solicitu", Toast.LENGTH_SHORT).show()
                        Mensaje.mensajeError(applicationContext, Errores.ERROR_SOLICITU_HTTP)

                    }
                })
                if (x==(can!!) - 1){
                    crud!!.deleteCoordenada()
                    Toast.makeText(this, "listo", Toast.LENGTH_SHORT).show()
                }
            }
            //todo aqui eliminar todos los registros

            showAlertINFO()
        } catch (ex: Exception) {

        }

    }



    private fun showAlertINFO(){//AUN NO USAMOS ESTA ALERTA
        val builder= AlertDialog.Builder(this)
        builder.setTitle("INFORMACIÓN")
        builder.setMessage(R.string.SE_ENVIO_NUB_STRING)
        builder.setPositiveButton("ACEPTAR",null)
        val dialog: AlertDialog =builder.create()
        dialog.show()
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        //startActivity(Intent(this,VistaCoordenada::class.java))

        //finish()
    }
    }




