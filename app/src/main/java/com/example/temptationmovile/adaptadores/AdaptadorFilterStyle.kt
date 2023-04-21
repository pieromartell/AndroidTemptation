package com.example.temptationmovile.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.temptationmovile.R
import com.example.temptationmovile.clases.Style

class AdaptadorFilterStyle(contex: Context?, private val listastyle:List<Style>?): BaseAdapter() {

    private val layoutInflater: LayoutInflater
    private var listaFiltrada: List<Style>?

    init {
        layoutInflater= LayoutInflater.from(contex)
        listaFiltrada=listastyle
    }

    override fun getCount(): Int {
        return listaFiltrada!!.size
    }

    override fun getItem(p0: Int): Any {
        return listaFiltrada!![p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    fun filter(texto:String){
        listaFiltrada = if (texto.isEmpty()){
            listastyle
        }else{
            listastyle?.filter {
                it.name_sty!!.toString().lowercase().contains(texto.lowercase()) || it.idstyles!!.toString().contains(texto)
            }
        }
        notifyDataSetChanged()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var vista = p1
        val objstyle = listaFiltrada!![p0]
        if(vista == null){
            vista=layoutInflater.inflate(R.layout.elemento_lista_style,p2,false)
        }

            //creamos los controles
            val lstidstyle = vista!!.findViewById<TextView>(R.id.lstidstyle)
            val lstname_style = vista!!.findViewById<TextView>(R.id.lstname_style)
            val lststate_style = vista!!.findViewById<TextView>(R.id.lststate_style)

            //agregamos los valores a los controladores
            lstidstyle.text = ""+objstyle.idstyles
            lstname_style.text = ""+objstyle.name_sty
            if(objstyle.state == 1)
            {
                lststate_style.text = "Habilitado"
            }else{
                lststate_style.text = "Deshabilitado"
            }


        return vista!!
    }
}