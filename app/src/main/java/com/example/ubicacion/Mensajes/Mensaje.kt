package com.example.manejodemensajes.Mensajes

import android.content.Context
import android.provider.Settings.Secure.getString


import android.util.Log
import android.widget.Toast

import com.example.manejodemensajes.Mensajes.Errores.*
import com.example.ubicacion.R

class Mensaje {
    companion object {
        fun mensajeSuccess(contex: Context, exito: Errores) {
            var mensaje: String? = null
            when (exito) {
                EXITO_SUCCES -> {
                    mensaje = contex.getString(R.string.EXITO_SUCCES_STRING)//en un activity normal se quita el contex
                }
                SE_ENVIO_NUB -> {
                    mensaje = contex.getString(R.string.ERROR_GUARDAR_STRING)
                }BIENVENIDA->{
                    mensaje=contex.getString(R.string.BIENVENIDA_AUTENTICAR_STRING)
                }ERROR_LICENCIAINSERTADA->{
                mensaje=contex.getString(R.string.ERROR_INSERTARLICENCIA_STRING)

            }
            }
                Toast.makeText(contex, mensaje, Toast.LENGTH_SHORT).show()


            }

            fun mensajeError(contex: Context, error: Errores) {
                var mensaje = ""
                when (error) {
                    NO_HAY_RED -> {
                        mensaje = contex.getString(R.string.NO_HAY_RED_STRING)
                    }
                    ERROR_GUARDAR -> {
                        mensaje = contex.getString(R.string.ERROR_GUARDAR_STRING)
                    }
                    CAMPO_VACIO -> {
                        mensaje = contex.getString(R.string.CAMPO_VACIO_STRING)
                    }
                    SE_ENVIO_NUB -> {
                        mensaje = contex.getString(R.string.SE_ENVIO_NUB_STRING)
                    }
                    ERROR_SOLICITU_HTTP -> {
                        mensaje = contex.getString(R.string.ERROR_SOLICITU_HTTP_STRING)

                    }PERMISOS->{
                        mensaje=contex.getString(R.string.PERMISOS_STRING)
                    }MANTENIMIENTO->{
                    mensaje=contex.getString(R.string.ERROR_MANTENIMIENTO_STRING)

                }ERROR_FALTACARACTERES->{
                    mensaje=contex.getString(R.string.ERROR_FALTACARACTERES_STRING)
                }ERROR_ESPERAR->{
                    mensaje=contex.getString(R.string.ERROR_ESPERAR_STRING)

                }
                }
                Toast.makeText(contex, mensaje, Toast.LENGTH_SHORT).show()
            }
        }
    }