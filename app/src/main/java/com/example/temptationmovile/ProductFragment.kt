package com.example.temptationmovile

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Spinner
import com.example.temptationmovile.adaptadores.AdaptadorComboBrand
import com.example.temptationmovile.adaptadores.AdaptadorProduct
import com.example.temptationmovile.clases.Brand
import com.example.temptationmovile.clases.Product
import com.example.temptationmovile.remoto.ApiUtil
import com.example.temptationmovile.servicios.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProductFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductFragment : Fragment() {
    // TODO: Rename and change types of parameters

   private lateinit var cbocat: Spinner
   private lateinit var lstPro: ListView

   private val objBrand = Brand()
    private var cod = 0
    private var nom = ""
    private var est = false
    private var fila = -1
    val raiz = null

    private var brandService: BrandService? = null
    private var colorService: ColorService? = null
    private var styleService: StyleService? = null
    private var sizeService: SizeService? = null
    private var categoryService: CategoryService? = null
    private var productService: ProductService? = null

    private var registroProducto: List<Product>? = null
    private var registroBrand: List<Brand>? = null


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
        val raiz = inflater.inflate(R.layout.fragment_product, container, false)
        cbocat = raiz.findViewById(R.id.cboBrand)
        lstPro = raiz.findViewById(R.id.lstPro)
        registroBrand = ArrayList()
        registroProducto = ArrayList()

        brandService = ApiUtil.brandservice
        productService = ApiUtil.productService

        mostrarComboBrand(raiz.context)
        mostrarproduct(raiz.context)
        return  raiz
    }
    fun mostrarComboBrand(context: Context){
        val call = brandService!!.MostrarBrand()
        call!!.enqueue(object: Callback<List<Brand>?>{
            override fun onResponse(call: Call<List<Brand>?>, response: Response<List<Brand>?>) {
                if(response.isSuccessful){
                    registroBrand = response.body()
                    cbocat.adapter = AdaptadorComboBrand(context, registroBrand)
                }
            }

            override fun onFailure(call: Call<List<Brand>?>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })
    }
    fun mostrarproduct(context: Context){
    val call = productService!!.MostrarProduct()
        call!!.enqueue(object :Callback<List<Product>>{
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if(response.isSuccessful){
                    println("Correcto")
                    registroProducto = response.body()
                    lstPro.adapter = AdaptadorProduct(context,registroProducto)
                }else{
                    println("Error")
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProductFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProductFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}