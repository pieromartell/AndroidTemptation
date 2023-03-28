package com.example.temptationmovile

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
import com.example.temptationmovile.adaptadores.AdaptadorComboBrand
import com.example.temptationmovile.adaptadores.AdaptadorComboProducto
import com.example.temptationmovile.adaptadores.AdaptadorOutput
import com.example.temptationmovile.adaptadores.AdaptadorProduct
import com.example.temptationmovile.clases.Output
import com.example.temptationmovile.clases.Product
import com.example.temptationmovile.remoto.ApiUtil
import com.example.temptationmovile.servicios.OutputService
import com.example.temptationmovile.servicios.ProductService
import com.example.temptationmovile.utilidad.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OutputFragment : Fragment() {

    private lateinit var cboproducto: Spinner
    private lateinit var txtcant: EditText
    private lateinit var txtfecha: TextView
    private lateinit var txtdestino: EditText
    private lateinit var chbestado_out: CheckBox
    private lateinit var lblidout: TextView
    private lateinit var btnRegistrar_out: Button
    private lateinit var btnActualizar_out: Button
    private lateinit var lstout: ListView

    private val objoutput = Output()
    private var cod = 0
    private var nom = ""
    private var fecha = ""
    private var destino = ""
    private var cantidad = 0
    private var idproduc = 0
    private var codproduc = 0
    private var state = 1
    private var fila = -1
    private var pos = -1
    val raiz = null

    private var indiceProduct = 0

    private var productService: ProductService? = null
    private var outputService: OutputService? = null

    private var registroProducto: List<Product>? = null
    private var registroOutput: List<Output>? = null

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
        val raiz = inflater.inflate(R.layout.output_fragment, container, false)

        txtcant = raiz.findViewById(R.id.txtcant)
        txtfecha = raiz.findViewById(R.id.txtfecha)
        txtdestino = raiz.findViewById(R.id.txtdestino)
        chbestado_out = raiz.findViewById(R.id.chbestado_out)
        lblidout = raiz.findViewById(R.id.lblidout)
        lstout = raiz.findViewById(R.id.lstout)
        btnRegistrar_out = raiz.findViewById(R.id.btnRegistrar_out)
        btnActualizar_out = raiz.findViewById(R.id.btnActualizar_out)
        cboproducto = raiz.findViewById(R.id.cboproducto)

        registroProducto = ArrayList()
        registroOutput = ArrayList()

        productService = ApiUtil.productService
        outputService = ApiUtil.outputService

        mostrarComboProduc(raiz.context)



        return  raiz
    }
    fun mostrarComboProduc(context:Context){
        val call = productService!!.MostrarProduct()
        call.enqueue(object :Callback<List<Product>?>{
            override fun onResponse(
                call: Call<List<Product>?>,
                response: Response<List<Product>?>
            ) {
                if(response.isSuccessful){
                    registroProducto = response.body()
                    cboproducto.adapter = AdaptadorComboProducto(context, registroProducto)
                }
            }

            override fun onFailure(call: Call<List<Product>?>, t: Throwable) {
                Log.e("Error Combo: ", t.message.toString())
            }

        })
    }

    fun mostrarOutput(context:Context){
        val call = outputService!!.MostrarOutputs()
        call!!.enqueue(object: Callback<List<Output>>{
            override fun onResponse(call: Call<List<Output>>, response: Response<List<Output>>) {
                if(response.isSuccessful){
                    println("Correcto")
                    registroOutput = response.body()
                    lstout.adapter = AdaptadorOutput(context,registroOutput)
                }
            }

            override fun onFailure(call: Call<List<Output>>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })
    }




















}