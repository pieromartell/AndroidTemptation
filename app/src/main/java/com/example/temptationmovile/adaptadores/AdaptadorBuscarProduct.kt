package com.example.temptationmovile.adaptadores

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.temptationmovile.R
import com.example.temptationmovile.clases.Product

class AdaptadorBuscarProduct(context: Context?, private val listProduct: List<Product>?): BaseAdapter() {

    private val layoutInflater: LayoutInflater
    private var listFilterProduct : List<Product>? = null
    init {
        layoutInflater = LayoutInflater.from(context)
        listFilterProduct = listProduct

    }

    fun filter(texto: String){
        listFilterProduct = if (texto.isEmpty()){
            Log.e("LoG","Entrada en VACIO")
            listProduct
        }else{
            listProduct?.filter {
                Log.e("LogAda","realizar filtrado")
                it.name_p!!.lowercase().contains(texto.lowercase()) || it.idproduc!!.toString().contains(texto)
            }
        }
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return  listFilterProduct!!.size
    }

    override fun getItem(p0: Int): Any {
        return listFilterProduct!![p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var vista = p1
        val objproduct = listFilterProduct!![p0]
        if(vista == null){
            vista = layoutInflater.inflate(R.layout.elemento_lista_producto,p2,false)
        }
            val lblidprode = vista!!.findViewById<TextView>(R.id.lblidproducE)
            val lblname_p = vista!!.findViewById<TextView>(R.id.lblname_p_E)
            val lblpriceE = vista!!.findViewById<TextView>(R.id.lblpriceE)
            val lblstockE = vista!!.findViewById<TextView>(R.id.lblstockE)
            val lblstateE = vista!!.findViewById<TextView>(R.id.lblstateE)



            lblidprode.text = ""+objproduct.idproduc
            lblname_p.text = ""+objproduct.name_p
            lblpriceE.text = ""+objproduct.price
            lblstockE.text = ""+objproduct.stock
            if(objproduct.state == 1)
            {
                lblstateE.text = "Habilitado"
            }else {
                lblstateE.text = "Deshabilitado"
            }
        return vista!!
    }
}