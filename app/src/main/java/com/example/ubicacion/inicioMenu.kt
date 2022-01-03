package com.example.ubicacion

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.system.Os.close
import android.system.Os.open
import android.util.Log
import android.view.MenuItem
import android.widget.Toast

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.basededatosejemplo.Modelo.estructura
import com.example.basededatosejemplo.SQLITE.ClienteCRUD
import com.example.basededatosejemplo.Utilidades.HttpResponse
import com.example.basededatosejemplo.Utilidades.Network
import com.example.ubicacion.Modelo.CargarData2
import com.example.ubicacion.Modelo.VistaClientes
import com.example.ubicacion.Modelo.VistaClientes.Companion
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson


class inicioMenu : AppCompatActivity() {
    lateinit var  toggle: ActionBarDrawerToggle
    var navView: NavigationView?=null
    var drawerLayout: DrawerLayout?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_menu)
        title =getString(R.string.TITULO_INICIO_STRING)
        val toolbar=findViewById<Toolbar>(R.id.toolbarInicio)//ojo con las librerias
        // actionBar?.title="ESTAS EN PANTALLA PRINCIPAL"
        setSupportActionBar(toolbar)
        drawerLayout=findViewById(R.id.drawerLayout)

        navView=findViewById(R.id.navView)
        navView?.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.miItem1 -> {


                    startActivity(Intent(this,IngresoCliente::class.java))
                }
                R.id.miItem2 -> {
                    startActivity(Intent(this,MainActivity::class.java))

                }
                R.id.miItem3 -> {
                    startActivity(Intent(this,VistaClientes::class.java))
                }
            }
            finish()
            true
        }


        toggle= ActionBarDrawerToggle(this,drawerLayout,R.string.open, R.string.close)
        drawerLayout?.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

      // var cargarData=CargarData2()
        //cargarData.cargarDataCliente(this)
         var crud = ClienteCRUD(this)
        var Clientes = crud?.getAlumn()//la consulta ala base
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
                       // configurarAdaptador(Clientes!!)
                    }

                    override fun httpErrorResponse(response: String) {//esto ees cucando no hay comunicion con los server
                        if (response.isEmpty()) {
                            Log.d("RESPONSEEEEE", response)
                            println("errorrrrrrrrrr")
                            Toast.makeText(activity, "error hacer la solicitud htttp", Toast.LENGTH_SHORT).show()
                            Clientes = crud?.getAlumn()//llamar a tods los alumnos de nuevo slite
                         //   configurarAdaptador(Clientes!!)


                        }


                    }



                }
        )





    }








    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {//override fun onBackPressed() {
        if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            drawerLayout!!.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}