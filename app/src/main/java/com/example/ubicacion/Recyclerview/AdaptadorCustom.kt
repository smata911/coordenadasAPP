package com.example.basededatosejemplo.Recyclerview

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.basededatosejemplo.Modelo.estructura

import com.example.ubicacion.R


class AdaptadorCustom(items: ArrayList<estructura.Cliente>, var listener: clickListener, var longClickListener: LongClickListener):
    RecyclerView.Adapter<AdaptadorCustom.ViewHolder> () {

    //class AdaptadorCustom( items:ArrayList<Alumno>,var listener:clickListener,var longClickListener: LongClickListener):RecyclerView.Adapter<AdaptadorCustom.ViewHolder> () {
    //varialble globales
    var items:ArrayList<estructura.Cliente>?=null
    var copiarItems:ArrayList<estructura.Cliente>?=null
    var multiSeleccion=false //es para el modo eliminar
    var itemSeleccionados:ArrayList<Int>?=null//para guardar los index los seleccionados
    var viewHolder: ViewHolder?=null
    //inicializar
    init {
        this.items=ArrayList(items)
        this.copiarItems=items
        itemSeleccionados= ArrayList()
    }
    //implemetnar los override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //creamos nuestra vista
        val vista= LayoutInflater.from(parent.context).inflate(R.layout.template_alumno,parent,false)
        viewHolder=
            ViewHolder(vista,listener,longClickListener)//si no ocupas el eveto click borra el listener y el parametro
        return viewHolder!!


    }
    //AQUI ES LO QUE SE VA MOSTRAR AL FINAL EN SU RESPECTIVO OBJETO
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=items?.get(position)

        holder.nombre?.text=item?.nombre
        holder.idCliente?.text=item?.idCliente
        holder.ubicacion?.text=item?.ubicacion
        holder.telefono?.text=item?.telefono
        holder.estado?.text=item?.estado

        //para que cambie de color cuando se selecciona un items del reciclerview
        if (itemSeleccionados?.contains(position)!!){
            holder.vista.setBackgroundColor(Color.LTGRAY)
        }else{
            holder.vista.setBackgroundColor(Color.WHITE)
        }


    }

    override fun getItemCount(): Int {
        return items?.count()!!

    }
    //todo funcion para el activar el modo de eliminar
    fun iniciarActionMode(){
        multiSeleccion=true

    }
    fun destruirActionMode(){
        multiSeleccion=false
        itemSeleccionados?.clear()
        notifyDataSetChanged()//actualiza cualquier datos graficos
    }
    fun TerminarActionMode(){
        //eliminar elemento seleccionado
        for (item in itemSeleccionados!!){
            itemSeleccionados?.remove(item)
        }
        multiSeleccion=false
        notifyDataSetChanged()
    }

    fun seleccionarItems(index:Int){
        if (multiSeleccion){//si es verdadero
            if (itemSeleccionados?.contains(index)!!){
                itemSeleccionados?.remove(index)

            }else{
                itemSeleccionados?.add(index)

            }
            notifyDataSetChanged()//actualiza

        }

    }
    fun obtenerNumeroElementosSeleccionados():Int{
        return itemSeleccionados!!.count()
    }
    fun eliminarSeleccionados(){
        if (itemSeleccionados?.count()!!>0){
            //se elimina en base en contenido ya que es un arreglo supongo
            var itemsEliminados=ArrayList<estructura.Cliente>()
            for (index in itemSeleccionados!!) {
                itemsEliminados.add(items?.get(index)!!)
            }
            items!!.removeAll(itemsEliminados)
            itemSeleccionados!!.clear()
        }
    }




    //el viwholder es para inicializar las variables
    class ViewHolder(vista: View, Listener: clickListener, LongClickListener: LongClickListener) : RecyclerView.ViewHolder(vista),
        View.OnClickListener, View.OnLongClickListener{//View.OnClickListener es heredada del interfaz clickListener


        var vista=vista

        var nombre: TextView?=null
        var idCliente: TextView?=null
        var ubicacion: TextView?=null
        var telefono: TextView?=null
        var estado: TextView?=null

        var Listener: clickListener?=null//hace referencia al interfaz de clickListener. se ocupa para el el evento click
        var longListener: LongClickListener?=null//hacer referenci al longclickliestener

        //inicializamos para usar el layout de template_Alumno
        init {

            nombre=vista.findViewById(R.id.tvnombre)
            idCliente=vista.findViewById(R.id.tvidCliente)
            ubicacion=vista.findViewById(R.id.tvUbicacion)
            telefono=vista.findViewById(R.id.tvTelefono)
            estado=vista.findViewById(R.id.tvEstado)

            this.Listener=Listener//para el evento click
            this.longListener=LongClickListener
            vista.setOnClickListener((this))//para el evento click
            vista.setOnLongClickListener((this))

        }
        //aqui va el evento click de reciclerview
        override fun onClick(v: View?) {
            this.Listener?.onCick(v!!,adapterPosition)//pasa los parametros de la vista y del adapter a nuestro interfaz clicklistener
        }

        override fun onLongClick(v: View?): Boolean {
            this.longListener?.longClick(v!!, adapterPosition)
            return true
        }


    }


    fun filtrar(str:String){
        items?.clear()
        if (str.isEmpty()){
            items=ArrayList(copiarItems)//crea una nueva lista por si borra todo el usuario
            notifyDataSetChanged()//actualiza
            return


        }
        var busqueda=str
        busqueda=busqueda.toLowerCase()//busquedad en minusculas
        for (item in copiarItems!!){
            val nombre=item.nombre?.toLowerCase()
            if (nombre!!.contains(busqueda)){
                items?.add(item)
            }
        }
        notifyDataSetChanged()

    }


    fun getItem(position: Int): Any {
        return this.items?.get(position)!!


    }


}