package com.example.temptationmovile.FragmentosBusqueda

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentTransaction
import com.example.temptationmovile.R
import com.example.temptationmovile.adaptadores.*
import com.example.temptationmovile.clases.*
import com.example.temptationmovile.remoto.ApiUtil
import com.example.temptationmovile.servicios.*
import com.example.temptationmovile.utilidad.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentoBuscarProduct.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentoBuscarProduct : Fragment() {

    private lateinit var txtBuscarProduct: SearchView
    private lateinit var lstProductBuscar: ListView
    private lateinit var btnProductbuscar: Button
    private lateinit var txtIdProduct: TextView

    private val objBrand = Brand()
    private val objproducto = Product()
    private var cod = 0
    private var nom = ""
    private var descri = ""
    private var stock = 0
    private var price = 0.0
    private var idbramd = 0
    private var codbrand = 0
    private var idcolor = 0
    private var codcolor = 0
    private var idstyle = 0
    private var codstyle = 0
    private var idcategory = 0
    private var codcategory = 0
    private var idsize = 0
    private var codsize = 0
    private var state = 1
    private var fila = -1
    private var pos = -1
    val raiz = null

    private var indiceB = 0
    private var indiceC = 0
    private var indiceCo = 0
    private var indiceSy = 0
    private var indiceSi = 0


    private var productService: ProductService? = null

    private var registroProducto: List<Product>? = null


    var objutilidad =  Util()
    private var dialogo: AlertDialog.Builder? = null
    var ft: FragmentTransaction?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val raiz = inflater.inflate(R.layout.fragment_fragmenteo_buscar_product, container, false)
        txtBuscarProduct=raiz.findViewById(R.id.txtBuscarProduc)

        lstProductBuscar=raiz.findViewById(R.id.lstBuscarProduct)

        btnProductbuscar = raiz.findViewById(R.id.btnEstadoProduc)

        txtIdProduct = raiz.findViewById(R.id.txtIdProductBus)

        registroProducto = ArrayList()

        productService = ApiUtil.productService


        mostrarproduct(raiz.context)

        txtBuscarProduct.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                (lstProductBuscar.adapter as AdaptadorBuscarProduct).filter(newText ?: "")

                return true
            }
        })

        lstProductBuscar.setOnItemClickListener(
            { adapterView, view,i, id ->
                fila = i
                objproducto.idproduc=(registroProducto as ArrayList<Product>).get(fila!!).idproduc
                objproducto.idbrand = (registroProducto as ArrayList<Product>).get(fila!!).idbrand
                objproducto.idsize = (registroProducto as ArrayList<Product>).get(fila!!).idsize
                objproducto.idstyles = (registroProducto as ArrayList<Product>).get(fila!!).idstyles
                objproducto.idcolor = (registroProducto as ArrayList<Product>).get(fila!!).idcolor
                objproducto.idcat = (registroProducto as ArrayList<Product>).get(fila!!).idcat
                objproducto.name_p=(registroProducto as ArrayList<Product>).get(fila!!).name_p
                objproducto.price = (registroProducto as ArrayList<Product>).get(fila!!).price
                objproducto.stock = (registroProducto as ArrayList<Product>).get(fila!!).stock
                objproducto.state=(registroProducto as ArrayList<Product>).get(fila!!).state
                //asignamos los valores a cada control
                txtIdProduct.setText(objproducto.idproduc.toString())
                if(objproducto.state != 0){
                    btnProductbuscar.setText("Deshabilitar")
                }else{
                    btnProductbuscar.setText("Habilitar")
                }
            }
        )

        btnProductbuscar.setOnClickListener {
            if (txtIdProduct.text.toString().length>0){
                if (objproducto.state==1){
                    EliminarProduct(raiz.context,objproducto.idproduc.toLong())
                    DialogoCRUD("Producto","Se deshabilitó el producto "+objproducto.name_p,FragmentoBuscarProduct())
                }else{
                    objproducto.state=1
                    ActualizarProduct(raiz.context,objproducto,objproducto.idproduc.toLong())
                    DialogoCRUD("Producto","Se habilitó el producto "+objproducto.name_p,FragmentoBuscarProduct())
                }
            }else{
                Toast.makeText(raiz.context,"Debe Seleccionar una producto", Toast.LENGTH_SHORT).show()
            }
        }

        return raiz
    }

    fun mostrarproduct(context: Context){
        val call = productService!!.MostrarProduct()
        call!!.enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if(response.isSuccessful){
                    println("Correcto")
                    registroProducto = response.body()
                    lstProductBuscar.adapter = AdaptadorBuscarProduct(context,registroProducto)
                }else{
                    println("Error")
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }
        })
    }


    fun ActualizarProduct( context: Context,p: Product, id: Long ){
        val call = productService!!.ActualizarProduct(id,p)
        call!!.enqueue(object : Callback<List<Product>?> {
            override fun onResponse(call: Call<List<Product>?>, response: Response<List<Product>?>) {
                if(response.isSuccessful){
                    Log.e("Mensaje", "Se actualizo correctamente el Producto")
                }
            }

            override fun onFailure(call: Call<List<Product>?>, t: Throwable) {
                Log.e("Error: ",t.message!!)
            }

        })
    }

    fun EliminarProduct(context: Context, id: Long){
        val call = productService!!.EliminarProduct(id)
        call!!.enqueue(object: Callback<List<Product>?> {
            override fun onResponse(call: Call<List<Product>?>, response: Response<List<Product>?>) {
                if(response.isSuccessful){
                    Log.e("mensaje","Se elimino correctamente")
                }
            }

            override fun onFailure(call: Call<List<Product>?>, t: Throwable) {
                Log.e("Error",t.message!!)
            }

        })
    }

    fun DialogoCRUD(titulo: String, mensaje: String, fragment: Fragment) {
        dialogo = AlertDialog.Builder(context)
        dialogo!!.setTitle(titulo)
        dialogo!!.setMessage(mensaje)
        dialogo!!.setCancelable(false)
        dialogo!!.setPositiveButton("Ok") { dialogo, which ->
            val fra = fragment
            ft = fragmentManager?.beginTransaction()
            ft?.replace(R.id.contenedor, fra, null)
            ft?.addToBackStack(null)
            ft?.commit()
        }
        dialogo!!.show()
    }

    fun DialogoCRUDEliminar(titulo: String, mensaje: String, fragment: Fragment) {
        dialogo = AlertDialog.Builder(context)
        dialogo!!.setTitle(titulo)
        dialogo!!.setMessage(mensaje)
        dialogo!!.setCancelable(false)
        dialogo!!.setPositiveButton("Sí") { dialogo, which ->
            val fra = fragment
            ft = fragmentManager?.beginTransaction()
            ft?.replace(R.id.contenedor, fra, null)
            ft?.addToBackStack(null)
            ft?.commit()
        }
        dialogo!!.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }
        dialogo!!.show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmenteoBuscarProduct.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentoBuscarProduct().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}