package com.example.ubicacion


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.basededatosejemplo.Modelo.HttpApiResponse
import com.google.gson.Gson
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.basededatosejemplo.Modelo.estructura
import com.example.basededatosejemplo.SQLITE.ClienteCRUD
import com.example.basededatosejemplo.Utilidades.*
import com.example.manejodemensajes.Mensajes.Errores
import com.example.manejodemensajes.Mensajes.Mensaje
import com.example.ubicacion.Modelo.VistaClientes
import com.example.ubicacion.Modelo.VistaClientes.Companion.obtenerContacto


import com.example.ubicacion.Modelo.VistaCoordenada
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.analytics.FirebaseAnalytics
import java.text.DateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private val permisoFineLocation=android.Manifest.permission.ACCESS_FINE_LOCATION
    private val permisoCoarseLocation=android.Manifest.permission.ACCESS_COARSE_LOCATION
    private val CODIGO_SOLICITUD_PERMISO=100//pra identificar el permiso con un numero
   private var fusedLocationClient:FusedLocationProviderClient?=null//para capturar la ultima ubicacion
    private var locationRequest:LocationRequest?=null
    private var callback: LocationCallback?=null

    private var coordenadaX:String?=null
    private var coodenadaY:String?=null
    var index:String="-1"//pensaria editar iten0



    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient= FusedLocationProviderClient(this)
        iniciarlizarLocationRequest()






    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {//control+0. override que ya existe esa funcion
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_vistas, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {//para utilizar los eventos de los items de menu simple
        return when (item.itemId) {
            R.id.opVista -> {
                val intent = Intent(applicationContext, VistaCoordenada::class.java)
                var valor="mostrar"
                intent.putExtra("ocultar", valor)
                startActivity(intent)
                finish()


                true

            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    private fun iniciarlizarLocationRequest(){
        locationRequest= LocationRequest()
        locationRequest?.interval=10000
        locationRequest?.fastestInterval=5000//5segundo
        locationRequest?.priority=LocationRequest.PRIORITY_HIGH_ACCURACY//PRIORITY_HIGH_ACCURACY es la mas certera es ahi hay una para ahorro de energia



    }
    private fun validarPermisosUbicacion():Boolean{//validadr si ya tiene permiso en el manifiesto
        val hayUbicacionPrecisaa= ContextCompat.checkSelfPermission(this,permisoFineLocation)==PackageManager.PERMISSION_GRANTED/* comparar para saber si tiene permisos */
        val hayUbicacionOrdinaria=ContextCompat.checkSelfPermission(this,permisoCoarseLocation)==PackageManager.PERMISSION_GRANTED
       return  hayUbicacionPrecisaa && hayUbicacionOrdinaria//devuelve si hay o no permisos en boleano

    }
    @SuppressLint("MissingPermission")
    private fun obtenerUbicacion(){

        //no se puede usaar la funcion de validadar PERMISOS, PERO MEJOR ELIMINAMOS QUE PIDA PERMISO CON @SUPPRESSLINT
        /*
        val hayUbicacionPrecisaa= ContextCompat.checkSelfPermission(this,permisoFineLocation)==PackageManager.PERMISSION_GRANTED/* comparar para saber si tiene permisos */
        val hayUbicacionOrdinaria=ContextCompat.checkSelfPermission(this,permisoCoarseLocation)==PackageManager.PERMISSION_GRANTED

         */
        //este es para obtener la ultima ubicacion todo
        /*

        fusedLocationClient?.lastLocation?.addOnSuccessListener(this, object : OnSuccessListener<Location> {
            override fun onSuccess(location: Location?) {
                if (location != null) {//si si optiene la localizacion
                    Toast.makeText(applicationContext, location?.latitude.toString()+" "+ location?.longitude, Toast.LENGTH_SHORT).show()
                }

            }

        })

         */

         callback=object : LocationCallback(){
             override fun onLocationResult(locationResul: LocationResult?) {
            super.onLocationResult(locationResul)

            for (ubicacion in locationResul?.locations!!){

                Toast.makeText(applicationContext, ubicacion?.latitude.toString()+" "+ ubicacion?.longitude.toString(), Toast.LENGTH_SHORT).show()
                coordenadaX=ubicacion.latitude.toString()
                 coodenadaY=ubicacion.longitude.toString()
                //lati.setText("${ubicacion.latitude.toString()} lati")
               // long.setText("${ubicacion.longitude.toString()} log")

            }
                 /*
                 btonGuardar.setOnClickListener (){
                     val lati=findViewById<TextView>(R.id.tvlatitud)
                     val long=findViewById<TextView>(R.id.tvlongitud)
                     val identidad=findViewById<TextView>(R.id.edIdentidadCoordenada)
                     lati.setText("$coordenadaX")
                     long.setText("$coodenadaY")


                 }

                  */
        }
    }


        //monitorear ubicacion
        fusedLocationClient?.requestLocationUpdates(locationRequest,callback,null)

    }

    private fun pedirPermisos(){
        val deboProveerContexto=androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale(this,permisoFineLocation)//obtener valor boleado para enteder previaamente al usuario si dio permiso
        if (deboProveerContexto){
            //mandar mensaje con explicacion adicional
            solicitudPermiso()
        }else{
            solicitudPermiso()

        }

    }



    private fun solicitudPermiso(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(permisoFineLocation,permisoCoarseLocation),CODIGO_SOLICITUD_PERMISO)
        }




    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CODIGO_SOLICITUD_PERMISO -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //obtenr ubicacion
                    obtenerUbicacion()
                } else {
                    //Toast.makeText(this, "no diste los permisos", Toast.LENGTH_SHORT).show()
                    Mensaje.mensajeError(applicationContext,Errores.PERMISOS)
                }
            }
        }
    }

    private fun detenerActualizacionUbicacion(){
        fusedLocationClient?.removeLocationUpdates(callback)

    }

    override fun onResume() {
        super.onResume()
        title = getString(R.string.TITULO_COORDENADA_STRING)
    }

    override fun onStart() {
        super.onStart()

        val toolbar=findViewById<Toolbar>(R.id.toolbarMain)//ojo con las librerias
        // actionBar?.title="ESTAS EN PANTALLA PRINCIPAL"
        setSupportActionBar(toolbar)
        //title = "INGRESAR CLIENTE"

        var crud:ClienteCRUD?=null

        val lati=findViewById<TextView>(R.id.tvlatitud)
        val long=findViewById<TextView>(R.id.tvlongitud)
        if(validarPermisosUbicacion()){//si se aceptan os permisos
            obtenerUbicacion()
            val btnGuardar=findViewById<Button>(R.id.btnGuardar)
            val btnGuardaLocal=findViewById<Button>(R.id.btnLocal)
            val identidad=findViewById<EditText>(R.id.edIdentidadCoordenada)
            val nombre=findViewById<EditText>(R.id.edNombreCoordenada)

            //TRAER EL CLIENTE DE SQLITE
            //AQUI OBTENGO EL PARAMETRO DEL ACTIVITY ANTERIOR
            if(intent.hasExtra("IDCLIENTE")){//RECORDEMO QUE EN NUEVO NO SE ENVIA NINGUN PARAMERATRO
                 index=intent.getStringExtra("IDCLIENTE")!!
               // rellenarDatos(index)
                //para mandar a su respectivo objeto
                crud = ClienteCRUD(this)
                //todo recorda que aqui trabajamos con la copia del arreglo por eso hicimos una fun global
                var cliente=VistaClientes.obtenerContacto(index.toInt()!!)//la mejor forma
               // val alumno = crud?.getALumno(index!!)
                identidad.setText(cliente?.idCliente, TextView.BufferType.EDITABLE)//A SU RESPECTIVO TEXVIEW
                nombre.setText(cliente?.nombre, TextView.BufferType.EDITABLE)


            }else{//todo por aqui nunca va a pasar porque ya no se puede hacer modificaciones en los EDtext
                identidad.addTextChangedListener(){
                    crud = ClienteCRUD(this)
                    var alumno = crud?.getALumno(it.toString()!!)
                   // identidad.setText(alumno?.idCliente, TextView.BufferType.EDITABLE)//A SU RESPECTIVO TEXVIEW
                    if (alumno!!.nombre!!.isEmpty()){
                        Toast.makeText(this, "NO SE ENCUENTRA NINGUN CLIENTE", Toast.LENGTH_SHORT).show()
                    }else {
                        nombre.setText(alumno?.nombre, TextView.BufferType.EDITABLE)
                        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            //lo ocuparemos si es que hay algguna

            //evento eGUARDAR LOCAL
//            crud = ClienteCRUD(this)
            btnGuardaLocal.setOnClickListener {
                val fecha: String = DateFormat.getDateInstance().format(Date())
                lati.setText("$coordenadaX")
                long.setText("$coodenadaY")
               // Toast.makeText(this, "SE HA GUARDADO EN TU MOVIL", Toast.LENGTH_SHORT).show()
                Mensaje.mensajeSuccess(this,Errores.EXITO_SUCCES)
                crud?.newCoordenadas(estructura.Coordenada(identidad.text.toString(), lati.text.toString(), long.text.toString(),fecha))

            }


            //evento guardar---------------------------------------------------------------------------------------------------------------
            btnGuardar.setOnClickListener (){
                lati.setText("$coordenadaX")
                long.setText("$coodenadaY")
if (lati.toString()=="null"){
    Toast.makeText(this, "POR FAVOR ACTIVA EL GPS", Toast.LENGTH_SHORT).show()
}
                val context: Context =this.applicationContext
                var network= Network(this)
                val query:String="?idCliente=" +identidad.text.toString() +"&x=" + lati.text.toString()+"&y=" + long.text.toString()
               // var url:String="http://192.168.1.52/apisCoordenadas/coordenasTerreno/nuevoalumno" +query
                var url:String="https://ihcprueba2020.000webhostapp.com/apisCoordenadas/coordenasTerreno/nuevoalumno/index.php" +query

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
                        //Toast.makeText(context, "hubo un problema al enviar la solicitu", Toast.LENGTH_SHORT).show()
                        Mensaje.mensajeError(applicationContext,Errores.ERROR_SOLICITU_HTTP)
                        Toast.makeText(context, "problema", Toast.LENGTH_SHORT).show()


                    }


                })
            }
        }else{
            pedirPermisos()
        }

    }

    override fun onPause() {
        super.onPause()
        detenerActualizacionUbicacion()!!
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,VistaClientes::class.java))
        finish()

    }
}