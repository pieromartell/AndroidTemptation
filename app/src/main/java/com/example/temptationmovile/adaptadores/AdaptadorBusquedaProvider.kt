package com.example.temptationmovile.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.temptationmovile.R
import android.widget.TextView
import com.example.temptationmovile.clases.Provider

class AdaptadorBusquedaProvider(context: Context?, private val listbusprov: List<Provider>?): BaseAdapter() {

    private val layoutInflater: LayoutInflater
    private var listaFiltrada: List<Provider>? = null

    init{
        layoutInflater= LayoutInflater.from(context)
        listaFiltrada = listbusprov
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
        val objprovider = listaFiltrada!![position]

        if(vista==null) {
            vista=layoutInflater.inflate(R.layout.elemento_lista_provider,parent,false)
        }

        //creamos los controles
        val lblcodprov= vista!!.findViewById<TextView>(R.id.lstidprov)
        val lblnameprov= vista!!.findViewById<TextView>(R.id.lstname_prov)
        val lblstateprov= vista!!.findViewById<TextView>(R.id.lststate_pro)
        //agregamos los valores a la lista
        lblcodprov.text=""+objprovider.idprovider
        lblnameprov.text=""+objprovider.name_prov
        if(objprovider.state==1){
            lblstateprov.text="Habilitado"
        }else{
            lblstateprov.text="Deshabilitado"
        }
        return vista!!
    }

    fun filter(texto: String) {
        listaFiltrada = if (texto.isEmpty()) {
            listbusprov
        } else {
            listbusprov?.filter {
                it.idprovider!!.toString().lowercase().contains(texto.lowercase()) ||
                        it.name_prov!!.lowercase().contains(texto.lowercase())
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
