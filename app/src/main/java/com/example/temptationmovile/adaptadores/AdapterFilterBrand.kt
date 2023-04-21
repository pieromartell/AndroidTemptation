package com.example.temptationmovile.adaptadores

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.temptationmovile.R
import com.example.temptationmovile.clases.Brand

class AdapterFilterBrand(context: Context?, private val listBrand: List<Brand>?): BaseAdapter() {
    private val layoutInflater: LayoutInflater
    private var listFilBrand: List<Brand>? = null

    init {
        layoutInflater = LayoutInflater.from(context)
        listFilBrand = listBrand
    }

    fun filter(texto: String){
        listFilBrand = if (texto.isEmpty()){
            Log.e("LoG","Entrada en VACIO")
            listBrand
        }else{
            listBrand?.filter {
                Log.e("LogAda","realizar filtrado")
                it.name_brand!!.lowercase().contains(texto.lowercase())
            }
        }
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return listFilBrand!!.size
    }

    override fun getItem(p0: Int): Any {
        return listBrand!![p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var vista = p1
        if(vista == null){
            vista = layoutInflater.inflate(R.layout.elemento_lista_brand,p2,false);
            val objbrand =  getItem(p0) as Brand
            //creamos los controladores
            val lstidbrand = vista!!.findViewById<TextView>(R.id.lstidband)
            val lstname_brand = vista!!.findViewById<TextView>(R.id.lstname_brand)
            val lststate = vista!!.findViewById<TextView>(R.id.lststate)

            lstidbrand.text =""+objbrand.idbrand
            lstname_brand.text = ""+objbrand.name_brand
            if (objbrand.state == 1){
                lststate.text = "Habilitado"
            }else{
                lststate.text = "Desabilitado"
            }
        }
        return vista!!;
    }
}