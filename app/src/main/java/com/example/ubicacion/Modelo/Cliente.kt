package com.example.basededatosejemplo.Modelo

class estructura() {
    class Cliente(idCliente: String, nombre: String, telefono: String, ubicacion: String, estado: String) {
        var idCliente: String? = null
        var nombre: String? = null
        var telefono: String? = null
        var ubicacion: String? = null
        var estado: String? = null

        init {
            this.idCliente = idCliente
            this.nombre = nombre
            this.telefono = telefono
            this.ubicacion = ubicacion
            this.estado = estado
        }
    }
    class Coordenada(id:String,X:String,Y:String,Fecha:String){
        var id:String?=null
        var x:String?=null
        var y :String?=null
        var fecha:String?=null

        init {
            this.id=id
            this.x=X
            this.y=Y
            this.fecha=Fecha
        }
    }
    class licencia(id: String,Licencia:String){
        var id:String?=null
        var licencia:String?=null
        init {
            this.id=id
            this.licencia=Licencia
        }
    }
}