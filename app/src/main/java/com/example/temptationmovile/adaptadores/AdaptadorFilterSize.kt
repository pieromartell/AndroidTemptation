package com.example.temptationmovile.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.temptationmovile.R
import com.example.temptationmovile.clases.Rol
import com.example.temptationmovile.clases.Size

class AdaptadorFilterSize(context: Context?, private val listsize: List<Size>?,val onClickListener: (Size, Int)->Unit): BaseAdapter() {

    private val layoutInflater: LayoutInflater
    private var listaFiltrada:List<Size>?=null
    init {
        layoutInflater =  LayoutInflater.from(context)
        listaFiltrada=listsize
    }

    override fun getCount(): Int {
        return  listaFiltrada!!.size
    }

    override fun getItem(position: Int): Any {
        return listaFiltrada!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var vista = convertView
        val objsiz =  listaFiltrada!![position]
        if(vista == null) {
            vista = layoutInflater.inflate(R.layout.elemento_lista_size, parent, false);
        }
        //creamos los controladores
        val lstcodsiz = vista!!.findViewById<TextView>(R.id.lstcodsiz)
        val lstnamesiz = vista!!.findViewById<TextView>(R.id.lstnamesiz)
        val lststate_siz = vista!!.findViewById<TextView>(R.id.lststate_siz)

        lstcodsiz.text =""+objsiz.idsize
        lstnamesiz.text = ""+objsiz.name_size
        if (objsiz.state == 1){
            lststate_siz.text = "Habilitado"
        }else{
            lststate_siz.text = "Desabilitado"
        }
        vista.setOnClickListener { onClickListener(objsiz,position) }
        return vista!!;
    }

    fun filter(texto: String) {
        listaFiltrada = if (texto.isEmpty()) {
            listsize
        } else {
            listsize?.filter {
                it.idsize!!.toString().lowercase().contains(texto.lowercase()) || it.name_size!!.toString().lowercase().contains(texto.lowercase())
            }
        }
        notifyDataSetChanged()
    }

}