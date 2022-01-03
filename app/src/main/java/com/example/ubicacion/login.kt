package com.example.ubicacion

import android.content.Intent
import android.icu.util.TimeUnit.values
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import com.example.basededatosejemplo.Modelo.estructura
import com.example.basededatosejemplo.SQLITE.ClienteCRUD
import com.example.manejodemensajes.Mensajes.Errores
import com.example.manejodemensajes.Mensajes.Mensaje
import com.example.ubicacion.Modelo.VistaClientes
import com.example.ubicacion.Modelo.VistaCoordenada
import com.google.android.gms.common.config.GservicesValue.value
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import java.time.chrono.JapaneseEra.values
import kotlin.time.measureTimedValue

class login : AppCompatActivity() {
    var crud: ClienteCRUD?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        var index="1"
        //para la introduccion splash scer
        setTheme(R.style.splashtheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        var recuperar=findViewById<Button>(R.id.btnExportar)

        val toolbar=findViewById<Toolbar>(R.id.toolbarLicencia)//ojo con las librerias
        setSupportActionBar(toolbar)
        title = getString(R.string.TITULO_LOGIN_LICENCIA_STRING)
        crud = ClienteCRUD(this)
        //agregar analitic
        val analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val budle=Bundle()
        budle.putString("messsage","integracin de firebase completa")
        analytics.logEvent("InitScreen",budle)
        val url :String?=intent.getStringExtra("url")//url es de firebase es la clave
        url?.let { //si existe
            println("TU LICENCIAAAAAAAA:   $url")
        }

        if (url.isNullOrBlank()) {//sino se envia nada de licencia
            // crud?.newLicencia(estructura.licencia("1","hola" ))//
            val licencianube: String? = intent.getStringExtra("licencia")//si se envia algo desde  firebase
            licencianube?.let { //si existe
                println("TU LICENCIAAAAAAAA:   $licencianube")
                crud?.deletelicencia()
                recuperar.setVisibility(View.VISIBLE)

            }
            recuperar.setOnClickListener(){
                val intent = Intent(applicationContext, VistaCoordenada::class.java)
                var valor="ocultar"
                intent.putExtra("ocultar", valor)
                startActivity(intent)
                finish()

            }

                val Licencia = crud?.getLicencia(index!!)//la mejor forma
                // Clientes = crud?.getAlumn()//la consulta ala base
                var nombreLicencia = Licencia?.licencia
                var id = Licencia?.id

                println("$nombreLicencia licencia base local")


                // val alumno = crud?.getALumno(index!!)
                //identidad.setText(cliente?.idCliente, TextView.BufferType.EDITABLE)
                //TODO UNA VALIDACION, HACEMOS UN QRY A SQLYTE TABLA LICENCIA
                if (Licencia?.licencia == "barcelona911") {
                    //Toast.makeText(this, "entro", Toast.LENGTH_SHORT).show()
                        Mensaje.mensajeSuccess(this,Errores.BIENVENIDA)
                    startActivity(Intent(this, inicioMenu::class.java))
                    finish()
                } else {
                    //Toast.makeText(applicationContext, .", Toast.LENGTH_SHORT).show()
                    Mensaje.mensajeError(this,Errores.ERROR_ESPERAR)
                    recuperar.setVisibility(View.VISIBLE)
                }

            } else {
                //todo hacemos un insert de la variable url
                crud?.newLicencia(estructura.licencia(index, url!!))//

                // crud?.newCoordenadas(estructura.Coordenada(identidad.text.toString(), lati.text.toString(), long.text.toString(),fecha))

               showAlertlicencia()
                println("SE INSERTO LA LICENCIA:   $url")

            }
            //llamos la funcio de login
            setup()
            limpiar()
        ModificarBton()


    }

    private fun limpiar(){
        val edtUsuario=findViewById<EditText>(R.id.emailEditText)
        val edtPassword=findViewById<EditText>(R.id.passwordEdt)

        edtUsuario.setText("")
        edtPassword.setText("")
    }
    private fun showAlert(){
        val builder= AlertDialog.Builder(this)
        builder.setTitle("ERROR")
        builder.setMessage(R.string.ERROR_AUTENTICAR)
        builder.setPositiveButton("ACEPTAR",null)
        val dialog: AlertDialog =builder.create()
        dialog.show()
    }
    private fun showAlertlicencia(){
        val builder= AlertDialog.Builder(this)
        builder.setTitle("INFORMACIÓN")
        builder.setMessage(R.string.ERROR_INSERTARLICENCIA_STRING)
        builder.setPositiveButton("ACEPTAR",null)
        val dialog: AlertDialog =builder.create()
        dialog.show()
    }

    private  fun showHome(email:String){//OSEA DE TIPO PROVIDERTYPE PARA QUE SOLO ACEPTE EL ENUM QUE ESTA EN HOMEACTIVITY
        val homeIntent: Intent = Intent(this,inicioMenu::class.java).apply {
            //putExtra("provider",provider)
            putExtra("email",email)


        }
        startActivity(homeIntent)


    }


    private fun  setup(){//para autenticar por correo
       // title=(R.string.TITULO_AUTENTICAR_STRING.toString())

        val btonRegistrar=findViewById<Button>(R.id.btnRegistrar)
        val btnAcceder=findViewById<Button>(R.id.btnacceder)
        val edtUsuario=findViewById<EditText>(R.id.emailEditText)
        val edtPassword=findViewById<EditText>(R.id.passwordEdt)

        btonRegistrar.setOnClickListener {


            if (edtPassword.text.isNotEmpty() && edtUsuario.text.isNotEmpty()){
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(edtUsuario.text.toString(),edtPassword.text.toString())
                    .addOnCompleteListener{
                        if (it.isSuccessful){
                            Mensaje.mensajeSuccess(applicationContext,Errores.EXITO_SUCCES)
                            limpiar()
                            //showHome(it.result?.user!!.email!!)

                        }else{
                            showAlert()
                        }
                    }

            }
        }



        btnAcceder.setOnClickListener {
            //regstrar los errores

//si truena todo entonces envia el usuario https://console.firebase.google.com/project/fir-tutorial-6530d/crashlytics/app/android:com.example.firebase_android_login_mas/issues/95df8ef9bcacf31481ea3b3bf1e977e7?time=last-seven-days&sessionEventKey=5FD86C3C0392000123052B982D734ECA_1484834675640921649
            val usuario:String="sergiomata911@gmail.com"
            //para enviar a que usuario le trono
        //    FirebaseCrashlytics.getInstance().setUserId(usuario)
            //envia el log
          //  FirebaseCrashlytics.getInstance().log("se pulso el boton xx")

            //throw  RuntimeException("forzar errror")
            val cod="1"
            val licenciaLocal= "barcelona911"
            crud = ClienteCRUD(this)
            if (edtPassword.text.isNotEmpty() && edtUsuario.text.isNotEmpty()){
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(edtUsuario.text.toString(),edtPassword.text.toString()).addOnCompleteListener{
                        if (it.isSuccessful){
                            showHome(it.result?.user!!.email!!)
                           // Toast.makeText(this, "ingresaaste", Toast.LENGTH_SHORT).show()
                            crud?.newLicencia(estructura.licencia(cod!!, licenciaLocal))
                            crud?.newLicencia(estructura.licencia(cod!!, licenciaLocal!!))//
                            Mensaje.mensajeSuccess(applicationContext,Errores.BIENVENIDA)
                            finish()


                        }else if (it.isCanceled){
                            Toast.makeText(this, "cancelado", Toast.LENGTH_SHORT).show()
                        } else{
                            showAlert()
                        }
                    }

            }
        }


    }



    private fun ModificarBton(){

//todo modificar pormedio de parametros remoto
        //valores por defecto para luego editarlos remotamente
        val btnRegistrar=findViewById<Button>(R.id.btnRegistrar)
        btnRegistrar?.visibility= View.INVISIBLE//vuelve invisible los objetos

        val configSetting: FirebaseRemoteConfigSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds=60//este es el tiempo de cada ves que hace los cambios
        }
        val firebaseConfi: FirebaseRemoteConfig = Firebase.remoteConfig//la isntancia
        firebaseConfi.setConfigSettingsAsync(configSetting)//ya definimos el tiempo de los cambios
        firebaseConfi.setDefaultsAsync(mapOf("btnRegistrar" to false,"btnRegistrar_texto" to "forzar error" ))//valores por defecto
        //recumerar las configuraciions desde la consosla
        Firebase.remoteConfig.fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful){
                val showlogin_errorButon:Boolean= Firebase.remoteConfig.getBoolean("loginBtn")//nombre del parametro de la consola
                val showtexto_buton: String= Firebase.remoteConfig.getString("loginBtn_texto")
                //preguntamos si esta visible
                //if (btonAcceder.visibility==View.INVISIBLE){
                if (showlogin_errorButon) {//si hay parametros que modificar
                    btnRegistrar?.visibility= View.VISIBLE

                }
                btnRegistrar?.text=showtexto_buton//se puede mandar la licencia pormedio de este medio

            }

        }


    }
}