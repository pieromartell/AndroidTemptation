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
import com.example.temptationmovile.adaptadores.*
import com.example.temptationmovile.clases.*
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

   private lateinit var cbobrad: Spinner
   private lateinit var cbocolor: Spinner
   private lateinit var cbosize: Spinner
   private lateinit var cbocategory: Spinner
   private lateinit var cbostyle: Spinner
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
    private var registrocolor: List<Color>? = null
    private var registrostyle: List<Style>? = null
    private var registrosize: List<Size>? = null
    private var registrocategory: List<Category>? = null
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
        cbobrad = raiz.findViewById(R.id.cboBrand)
        cbocategory = raiz.findViewById(R.id.cboCategory)
        cbocolor = raiz.findViewById(R.id.cbocolor)
        cbosize = raiz.findViewById(R.id.cboSize)
        cbostyle = raiz.findViewById(R.id.cboStyle)
        lstPro = raiz.findViewById(R.id.lstPro)

        registroBrand = ArrayList()
        registroProducto = ArrayList()
        registrocolor = ArrayList()
        registrosize = ArrayList()
        registrostyle = ArrayList()
        registrocategory = ArrayList()

        brandService = ApiUtil.brandservice
        productService = ApiUtil.productService
        categoryService = ApiUtil.categoryService
        sizeService = ApiUtil.sizeService
        styleService = ApiUtil.styleService
        colorService = ApiUtil.colorservice

        mostrarComboBrand(raiz.context)
        mostrarproduct(raiz.context)
        mostrarComboStyle(raiz.context)
        mostrarComboColor(raiz.context)
        mostrarComboCategory(raiz.context)
        mostrarcomboSize(raiz.context)
        return  raiz
    }
    fun mostrarComboBrand(context: Context){
        val call = brandService!!.MostrarBrand()
        call!!.enqueue(object: Callback<List<Brand>?>{
            override fun onResponse(call: Call<List<Brand>?>, response: Response<List<Brand>?>) {
                if(response.isSuccessful){
                    registroBrand = response.body()
                    cbobrad.adapter = AdaptadorComboBrand(context, registroBrand)
                }
            }

            override fun onFailure(call: Call<List<Brand>?>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })
    }

    fun mostrarComboStyle(context: Context){
        val call = styleService!!.MostrarEstilo()
        call.enqueue(object : Callback<List<Style>?>{
            override fun onResponse(call: Call<List<Style>?>, response: Response<List<Style>?>) {
                if(response.isSuccessful){
                    registrostyle = response.body()
                    cbostyle.adapter = AdaptadorComboEstilo(context,registrostyle)
                }
            }

            override fun onFailure(call: Call<List<Style>?>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })
    }
    fun mostrarComboColor(context: Context){
        val call = colorService!!.MostrarColor()
        call.enqueue(object :Callback<List<Color>?>{
            override fun onResponse(call: Call<List<Color>?>, response: Response<List<Color>?>) {
                if(response.isSuccessful){
                    registrocolor = response.body()
                    cbocolor.adapter = AdaptadorComboColor(context,registrocolor)
                }
            }

            override fun onFailure(call: Call<List<Color>?>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })
    }

    fun mostrarComboCategory(context: Context){
        val call = categoryService!!.MostrarCategory()
        call.enqueue(object : Callback<List<Category>?>{
            override fun onResponse(
                call: Call<List<Category>?>,
                response: Response<List<Category>?>
            ) {
                if(response.isSuccessful){
                    registrocategory = response.body()
                    cbocategory.adapter = AdaptadorComboCategory(context,registrocategory)
                }
            }

            override fun onFailure(call: Call<List<Category>?>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })
    }

    fun mostrarcomboSize(context: Context){
        val call = sizeService!!.Mostrarsizes()
        call.enqueue(object : Callback<List<Size>?>{
            override fun onResponse(call: Call<List<Size>?>, response: Response<List<Size>?>) {
                if(response.isSuccessful){
                    registrosize = response.body()
                    cbosize.adapter = AdaptadorComboSize(context,registrosize)
                }
            }

            override fun onFailure(call: Call<List<Size>?>, t: Throwable) {
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