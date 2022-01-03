package com.example.ubicacion.Modelo
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService :FirebaseMessagingService(){
    override fun onMessageReceived(p0: RemoteMessage) {//muestra notificacion en forma de alert
        Looper.prepare()
        Handler().post{
            Toast.makeText(baseContext, p0.notification?.title, Toast.LENGTH_SHORT).show()
        }
        Looper.loop()
    }

}