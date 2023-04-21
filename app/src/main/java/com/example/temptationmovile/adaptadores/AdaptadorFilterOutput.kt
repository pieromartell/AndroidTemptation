package com.example.temptationmovile.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.temptationmovile.R
import com.example.temptationmovile.clases.Output
import com.example.temptationmovile.clases.Person

class AdaptadorFilterOutput (context: Context?, private val listoutput: List<Output>?): BaseAdapter() {

    private val layoutInflater: LayoutInflater
    private var listFiltrada:List<Output>?=null
    init {
        layoutInflater = LayoutInflater.from(context)
        listFiltrada=listoutput
    }

    override fun getCount(): Int {
        return listFiltrada!!.size
    }

    override fun getItem(p0: Int): Any {
        return listFiltrada!![p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var vista = p1
        val objoutput = listFiltrada!![p0]
        if(vista == null) {
            vista = layoutInflater.inflate(R.layout.elemento_lista_output, p2, false)
        }
        val lblidoutput = vista!!.findViewById<TextView>(R.id.lblidoutput)
        val lblidprod = vista!!.findViewById<TextView>(R.id.lblidprod)
        val lblcant = vista!!.findViewById<TextView>(R.id.lblcant)
        val lbldestino = vista!!.findViewById<TextView>(R.id.lbldestino)
        val lblEstadoOut=vista!!.findViewById<TextView>(R.id.lblEstadoOut)
        lblidoutput.text = ""+objoutput.idout
        lblidprod.text = ""+objoutput.idproduc
        lblcant.text = ""+objoutput.quantity
        lbldestino.text = ""+objoutput.destino
        if (objoutput.state==1){
            lblEstadoOut.text="Habilitado"
        }else{
            lblEstadoOut.text="Deshabilitado"
        }

        return vista!!
    }

    fun filter(texto: String) {
        listFiltrada = if (texto.isEmpty()) {
            listoutput
        } else {
            listoutput?.filter {
                it.idout!!.toString().lowercase().contains(texto.lowercase()) ||
                        it.destino.toString().lowercase()!!.contains(texto.lowercase())||
                        it.idproduc.toString().lowercase().contains(texto.lowercase())||
                        //it.dateout.toString().lowercase().contains(texto.lowercase())||
                        it.idproduc.toString().lowercase().contains(texto.lowercase())||
                        it.quantity.toString().lowercase().contains(texto.lowercase())
            }
        }
        notifyDataSetChanged()
    }

}