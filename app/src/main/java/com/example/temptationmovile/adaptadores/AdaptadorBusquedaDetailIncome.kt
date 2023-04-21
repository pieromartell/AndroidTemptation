package com.example.temptationmovile.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.temptationmovile.R
import com.example.temptationmovile.clases.DetailIncome
import com.example.temptationmovile.clases.Income

class AdaptadorBusquedaDetailIncome (context: Context?, private val listbusdetailinco: List<DetailIncome>?): BaseAdapter() {

    private val layoutInflater: LayoutInflater
    private var listaFiltrada: List<DetailIncome>? = null

    init{
        layoutInflater= LayoutInflater.from(context)
        listaFiltrada = listbusdetailinco
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
        val objdetailincome = listaFiltrada!![position]

        if(vista==null) {
            vista=layoutInflater.inflate(R.layout.elemento_lista_detailincome,parent,false)
        }

        //creamos los controles
        val lbliddetailincome= vista!!.findViewById<TextView>(R.id.lstiddetailincomeE)
        val lblidincome= vista!!.findViewById<TextView>(R.id.lstidincomeE)
        val lblidproduct= vista!!.findViewById<TextView>(R.id.lstidproductE)
        val lblprice= vista!!.findViewById<TextView>(R.id.lstprice_buyE)
        val lblquantity= vista!!.findViewById<TextView>(R.id.lstquantityE)
        //agregamos los valores a la lista
        lbliddetailincome.text=""+objdetailincome.iddetincome
        lblidincome.text=""+objdetailincome.idincome
        lblidproduct.text=""+objdetailincome.idproduc
        lblprice.text=""+objdetailincome.price_buy
        lblquantity.text=""+objdetailincome.quantity
        /*if(objincome.state==1){
            lblstateprov.text="Habilitado"
        }else{
            lblstateprov.text="Deshabilitado"
        }*/
        return vista!!
    }

    fun filter(texto: String) {
        listaFiltrada = if (texto.isEmpty()) {
            listbusdetailinco
        } else {
            listbusdetailinco?.filter {
                it.iddetincome!!.toString().lowercase().contains(texto.lowercase()) ||
                it.idincome!!.toString().lowercase().contains(texto.lowercase()) ||
                        it.idproduc!!.toString().lowercase().contains(texto.lowercase()) ||
                        it.price_buy!!.toString().lowercase().contains(texto.lowercase()) ||
                        it.quantity!!.toString().lowercase().contains(texto.lowercase())
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