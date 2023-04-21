package com.example.temptationmovile.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.temptationmovile.R
import com.example.temptationmovile.clases.Income
import com.example.temptationmovile.clases.Provider

class AdaptadorBusquedaIncome(context: Context?, private val listbusincome: List<Income>?): BaseAdapter() {

    private val layoutInflater: LayoutInflater
    private var listaFiltrada: List<Income>? = null

    init{
        layoutInflater= LayoutInflater.from(context)
        listaFiltrada = listbusincome
    }

    override fun getCount(): Int {
        return listaFiltrada!!.size
    }

    override fun getItem(position: Int): Any {
        return listaFiltrada!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var vista=convertView
        val objincome = listaFiltrada!![position]

        if(vista==null) {
            vista=layoutInflater.inflate(R.layout.elemento_lista_income,parent,false)
        }

        //creamos los controles
        val lblidincome= vista!!.findViewById<TextView>(R.id.lstidincome)
        val lblidprovider= vista!!.findViewById<TextView>(R.id.lstnomproviders)
        val lbldateincome= vista!!.findViewById<TextView>(R.id.lstfechaincome)
        //agregamos los valores a la lista
        lblidincome.text=""+objincome.idincome
        lblidprovider.text=""+objincome.idprovider
        lbldateincome.text=""+objincome.dateinco
        /*if(objincome.state==1){
            lblstateprov.text="Habilitado"
        }else{
            lblstateprov.text="Deshabilitado"
        }*/
        return vista!!
    }

    fun filter(texto: String) {
        listaFiltrada = if (texto.isEmpty()) {
            listbusincome
        } else {
            listbusincome?.filter {
                it.idincome!!.toString().lowercase().contains(texto.lowercase()) ||
                it.idprovider!!.toString().lowercase().contains(texto.lowercase()) ||
                        it.dateinco!!.toString().lowercase().contains(texto.lowercase())
                //it.ruc!!.lowercase().contains(texto.lowercase()) ||
                //it.company_name!!.lowercase().contains(texto.lowercase()) ||
                //it.phone!!.toString().lowercase().contains(texto.lowercase()) ||
                //it.email!!.lowercase().contains(texto.lowercase()) ||
                //it.description!!.lowercase().contains(texto.lowercase())  ||
                //it.address!!.lowercase().contains(texto.lowercase())
                //e -> e.nombre!!.toLowerCase(Locale.getDefault()).contains(texto.toLowerCase(Locale.getDefault()))!!
                //|| e.estado.toString()!!.toLowerCase(Locale.getDefault())?.contains(texto.toLowerCase(Locale.getDefault()))!!
            }
        }
        notifyDataSetChanged()
    }

}