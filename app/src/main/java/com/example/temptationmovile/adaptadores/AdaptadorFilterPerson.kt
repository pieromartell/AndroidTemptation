package com.example.temptationmovile.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.temptationmovile.R
import com.example.temptationmovile.clases.Person

class AdaptadorFilterPerson (context: Context?, private val listaPerson: List<Person>?): BaseAdapter() {
    private val layoutInflater: LayoutInflater
    private var listaFiltrada:List<Person>?=null
    init {
        layoutInflater = LayoutInflater.from(context)
        listaFiltrada=listaPerson
    }
    override fun getCount(): Int {
        return  listaFiltrada!!.size
    }

    override fun getItem(p0: Int): Any {
        return listaFiltrada!![p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var vista = p1
        val objperson = listaFiltrada!![p0]
        if(vista == null) {
            vista = layoutInflater.inflate(R.layout.elemento_lista_person, p2, false)
        }
        val txtdniPerson = vista!!.findViewById<TextView>(R.id.txtdniPerson)
        val txtnamelPerson = vista.findViewById<TextView>(R.id.txtnamelPerson)
        val txtRolPerson = vista.findViewById<TextView>(R.id.txtRolPerson)
        val txtUserPerson = vista.findViewById<TextView>(R.id.txtUserPerson)
        val txtEstadoPerson = vista.findViewById<TextView>(R.id.txtEstadoPerson)
        txtdniPerson.text = ""+objperson.dni.trim()
        txtnamelPerson.text = ""+objperson.name.trim()+" "+objperson.lastname.trim()
        txtRolPerson.text = ""+objperson.idrol
        txtUserPerson.text = ""+objperson.username.trim()
        if(objperson.state == 1)
        {
            txtEstadoPerson.text = "Habilitado"
        }else{
            txtEstadoPerson.text = "Deshabilitado"
        }

        return vista!!
    }

    fun filter(texto: String) {
        var p=texto.lowercase()
        listaFiltrada = if (texto.isEmpty()) {
            listaPerson
        } else {
            listaPerson?.filter {
                it.name!!.toString().lowercase().contains(p)
                        ||it.dni.toString().contains(p)||
                        it.lastname.lowercase().contains(p)||it.idrol.toString().contains(p) ||
                        it.username.lowercase().contains(p)
            }
        }
        notifyDataSetChanged()
    }

}