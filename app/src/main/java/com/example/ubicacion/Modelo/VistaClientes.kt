package com.example.ubicacion.Modelo

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.SearchView
import android.widget.Toast


import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.basededatosejemplo.Modelo.estructura
import com.example.basededatosejemplo.Recyclerview.AdaptadorCustom
import com.example.basededatosejemplo.Recyclerview.LongClickListener
import com.example.basededatosejemplo.Recyclerview.clickListener
import com.example.basededatosejemplo.SQLITE.ClienteCRUD
import com.example.basededatosejemplo.Utilidades.HttpResponse
import com.example.basededatosejemplo.Utilidades.Network
import com.example.manejodemensajes.Mensajes.Errores
import com.example.manejodemensajes.Mensajes.Mensaje
import com.example.ubicacion.IngresoCliente
import com.example.ubicacion.MainActivity
import com.example.ubicacion.R
import com.example.ubicacion.inicioMenu


import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson

class VistaClientes : AppCompatActivity() {

    companion object{
        var adaptador:AdaptadorCustom?=null
        fun obtenerContacto(index:Int):estructura.Cliente{
            //return contactos?.get(index)!! SIN BUSQUEDAD
           return adaptador?.getItem(index) as estructura.Cliente//cuando hay un buscador
        }
    }
    var lista: RecyclerView?=null
    //var adaptador: AdaptadorCustom?=null
    var layouManager: RecyclerView.LayoutManager?=null//administrar el layou ya se horizontal o verrtical o en pinteres
    var crud: ClienteCRUD?=null
    var Clientes:ArrayList<estructura.Cliente>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vista_clientes)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        val toolbar=findViewById<Toolbar>(R.id.toolbarVistaCliente)//ojo con las librerias

       // var cargarData=CargarData()
        //cargarData.cargarDataCliente(this)
        title = getString(R.string.TITULO_VISTA_CLIENTE_STRING)
        setSupportActionBar(toolbar)
        lista = findViewById(R.id.lista)
        lista?.setHasFixedSize(true)//para no sea lenta la listas
        layouManager = LinearLayoutManager(this)
        lista?.layoutManager = layouManager//aqui decimos que a la lista tendra el layoumager

        crud = ClienteCRUD(this)
        Clientes = crud?.getAlumn()//la consulta ala base
        configurarAdaptador(Clientes!!)


        fab.setOnClickListener() {
            startActivity(Intent(this, IngresoCliente::class.java))
            finish()
        }
        //todo 3 main activitu
       val network = Network(this)//ojo

            val activity: Context = this.applicationContext
            val gson = Gson()

            crud = ClienteCRUD(this)
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
                            configurarAdaptador(Clientes!!)
                        }

                        override fun httpErrorResponse(response: String) {//esto ees cucando no hay comunicion con los server
                            if (response.isEmpty()) {
                                Log.d("RESPONSEEEEE", response)
                                println("errorrrrrrrrrr")
                                Toast.makeText(activity, "error hacer la solicitud htttp", Toast.LENGTH_SHORT).show()
                                Clientes = crud?.getAlumn()//llamar a tods los alumnos de nuevo slite
                                configurarAdaptador(Clientes!!)


                            }


                        }



                    }
            )



        Clientes = crud?.getAlumn()//la consulta ala base
        configurarAdaptador(Clientes!!)



    }
    fun configurarAdaptador(data: ArrayList<estructura.Cliente>) {
        adaptador = AdaptadorCustom(data!!, object : clickListener {
            override fun onCick(vista: View, index: Int) {//esta es la funcion que se creo exactamente en el interfaz de clickListener
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.putExtra("IDCLIENTE", index.toString())
                startActivity(intent)

                finish()
               // overridePendingTransition(R.animator.from_left,R.animator.to_right)se pone la pantalla en negro
            }
        }, object : LongClickListener {
            override fun longClick(vista: View, index: Int) {
                // Toast.makeText(applicationContext, "es un alargacdo", Toast.LENGTH_SHORT).show()
            }
        })
        this.lista?.adapter = adaptador


    }
    //____________________________________________________________________________________________________________________________________
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater =menuInflater
        inflater.inflate(R.menu.menu_vista_cliente,menu)

        //ocupamos la clase de adaptadorCustom
        val searchManager=getSystemService(Context.SEARCH_SERVICE) as SearchManager//para la busquedad
        val itemBusqueda=menu?.findItem(R.id.app_bar_search)//busque
        val seachView=itemBusqueda?.actionView as SearchView//OJO
        seachView.setSearchableInfo(searchManager.getSearchableInfo(componentName))//PARA ACTIVAAR LOS SERVICIOS
        seachView.queryHint=getString(R.string.ED_BUSCAR_STRING)
        //preparar los dots
        seachView.setOnQueryTextFocusChangeListener { v, hasFocus ->

        }
        //EVENTO para filtrar
        seachView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adaptador?.filtrar(newText!!)//lo que se escribe
                return true
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,inicioMenu::class.java))
        finish()

    }



    override fun onStart() {
        super.onStart()
      //  var cargarData=CargarData()
       // cargarData.cargarDataCliente(this)

        //adaptador?.notifyDataSetChanged()

        //lista = findViewById(R.id.lista)
        //lista?.setHasFixedSize(true)//para no sea lenta la listas
        ///layouManager = LinearLayoutManager(this)
        //lista?.layoutManager = layouManager//aqui decimos que a la lista tendra el layoumager
        //sino fuenciona borrar esto
        //crud = ClienteCRUD(this)
        ///Clientes = crud?.getAlumn()//la consulta ala base
        //configurarAdaptador(Clientes!!)

    }
}