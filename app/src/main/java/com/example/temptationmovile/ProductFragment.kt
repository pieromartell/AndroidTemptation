package com.example.temptationmovile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
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
   private lateinit var txtNomPro: EditText
   private lateinit var txtdescrip: EditText
   private lateinit var txtStock: EditText
   private lateinit var txtprice: EditText
   private lateinit var chkEstPro: CheckBox
   private lateinit var lblCodPro: TextView
   private lateinit var btnRegistrarProd: Button
   private lateinit var btnActualizarProd: Button
   private lateinit var btnEliminarProd: Button

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
        val raiz = inflater.inflate(R.layout.fragment_product, container, false)
        txtNomPro = raiz.findViewById(R.id.txtnombreProd)
        txtdescrip = raiz.findViewById(R.id.txtdescripcionProd)
        txtStock = raiz.findViewById(R.id.txtstockProd)
        txtprice =raiz.findViewById(R.id.txtpriceProd)
        lblCodPro = raiz.findViewById(R.id.codProduct)
        chkEstPro = raiz.findViewById(R.id.chkStateProd)
        btnRegistrarProd =raiz.findViewById(R.id.btnregistrarproduct)
        btnEliminarProd = raiz.findViewById(R.id.btneliminarproduct)
        btnActualizarProd =raiz.findViewById(R.id.btnactualizarproduct)

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


        btnRegistrarProd.setOnClickListener {
            if(txtNomPro.text.toString() ==""){
                objutilidad.MensajeToast(raiz.context,"Ingresa el Nombre")
                txtNomPro.requestFocus()
            }else if(txtdescrip.text.toString() == ""){
                objutilidad.MensajeToast(raiz.context,"Ingresa la descripcion")
                txtdescrip.requestFocus()
            }else if(txtprice.text.toString()==""){
                objutilidad.MensajeToast(raiz.context,"Ingrese un precio")
                txtprice.requestFocus()
            }else if(txtStock.text.toString()==""){
                objutilidad.MensajeToast(raiz.context,"Ingrese el stock")
                txtStock.requestFocus()
            }else if(cbobrad.selectedItemPosition==-1){
                objutilidad.MensajeToast(raiz.context,"Seleccionne una Marca")
                cbobrad.requestFocus()
            }else{
                nom =txtNomPro.text.toString()
                descri = txtdescrip.text.toString()
                price = txtprice.text.toString().toDouble()
                stock = txtStock.text.toString().toInt()
                idbramd = cbobrad.selectedItemPosition
                codbrand = (registroBrand as ArrayList<Brand>).get(idbramd).idbrand
                idcolor = cbocolor.selectedItemPosition
                codcolor = (registrocolor as ArrayList<Color>).get(idcolor).idcolor
                idcategory = cbocategory.selectedItemPosition
                codcategory = (registrocategory as ArrayList<Category>).get(idcategory).idcat
                idstyle = cbostyle.selectedItemPosition
                codstyle = (registrostyle as ArrayList<Style>).get(idstyle).idstyles
                idsize = cbosize.selectedItemPosition
                codsize = (registrosize as ArrayList<Size>).get(idsize).idsize
                state = if (chkEstPro.isChecked) 1 else 0



                objproducto.idcat = codcategory
                objproducto.idsize = codsize
                objproducto.idstyles = codstyle
                objproducto.idbrand = codbrand
                objproducto.idcolor = codcolor
                objproducto.name_p = nom
                objproducto.description = descri
                objproducto.price = price
                objproducto.stock = stock
                objproducto.image_front = 0
                objproducto.image_back = 0
                objproducto.image_using = 0
                objproducto.state = state

                registrarProducto(raiz.context,objproducto)
                val fproducto = ProductFragment()
                DialogoCRUDEliminar("Registro de Producto", "Se registro el Producto Correctamente",fproducto)

            }
        }
        lstPro.setOnItemClickListener { adapterView, view, i, l ->
            fila = i
            lblCodPro.text = (registroProducto as ArrayList<Product>).get(fila).idproduc.toString()
           txtNomPro.setText(""+ (registroProducto as ArrayList<Product>).get(fila).name_p.toString())
            txtdescrip.setText(""+ (registroProducto as ArrayList<Product>).get(fila).description.toString())
            txtprice.setText(""+ (registroProducto as ArrayList<Product>).get(fila).price.toString().toDouble())
           txtStock.setText(""+ (registroProducto as ArrayList<Product>).get(fila).stock.toString().toInt())
            for (x in (registroBrand as ArrayList<Brand>).indices){
                    if((registroBrand as ArrayList<Brand>).get(x).idbrand == (registroProducto as ArrayList<Product>).get(fila).idbrand){
                        indiceB = x
                    }
            }

            for (x in (registrocolor as ArrayList<Color>).indices){
                if((registrocolor as ArrayList<Color>).get(x).idcolor == (registroProducto as ArrayList<Product>).get(fila).idcolor){
                    indiceCo = x
                }
            }
            for (x in (registrocategory as ArrayList<Category>).indices){
                if((registrocategory as ArrayList<Category>).get(x).idcat == (registroProducto as ArrayList<Product>).get(fila).idcat){
                    indiceC = x
                }
            }
            for (x in (registrosize as ArrayList<Size>).indices){
                if((registrosize as ArrayList<Size>).get(x).idsize == (registroProducto as ArrayList<Product>).get(fila).idsize){
                    indiceSi = x
                }
            }

            for (x in (registrostyle as ArrayList<Style>).indices){
                if((registrostyle as ArrayList<Style>).get(x).idstyles == (registroProducto as ArrayList<Product>).get(fila).idstyles){
                    indiceSy = x
                }
            }
            cbobrad.setSelection(indiceB)
            cbocolor.setSelection(indiceCo)
            cbocategory.setSelection(indiceC)
            cbosize.setSelection(indiceSi)
            cbostyle.setSelection(indiceSy)
            if((registroProducto as ArrayList<Product>).get(fila).state != 0){
                chkEstPro.setChecked(true)
            }else{
                chkEstPro.setChecked(false)
            }
        }

        btnActualizarProd.setOnClickListener {
            if(fila >=0){
                cod = lblCodPro.text.toString().toInt()
                nom =txtNomPro.text.toString()
                descri = txtdescrip.text.toString()
                price = txtprice.text.toString().toDouble()
                stock = txtStock.text.toString().toInt()
                idbramd = cbobrad.selectedItemPosition
                codbrand = (registroBrand as ArrayList<Brand>).get(idbramd).idbrand
                idcolor = cbocolor.selectedItemPosition
                codcolor = (registrocolor as ArrayList<Color>).get(idcolor).idcolor
                idcategory = cbocategory.selectedItemPosition
                codcategory = (registrocategory as ArrayList<Category>).get(idcategory).idcat
                idstyle = cbostyle.selectedItemPosition
                codstyle = (registrostyle as ArrayList<Style>).get(idstyle).idstyles
                idsize = cbosize.selectedItemPosition
                codsize = (registrosize as ArrayList<Size>).get(idsize).idsize
                state = if (chkEstPro.isChecked) 1 else 0



                objproducto.idproduc = cod
                objproducto.idcat = codcategory
                objproducto.idsize = codsize
                objproducto.idstyles = codstyle
                objproducto.idbrand = codbrand
                objproducto.idcolor = codcolor
                objproducto.name_p = nom
                objproducto.description = descri
                objproducto.price = price
                objproducto.stock = stock
                objproducto.image_front = 0
                objproducto.image_back = 0
                objproducto.image_using = 0
                objproducto.state = state
                ActualizarProduct(raiz.context,objproducto,cod.toLong())
                val fproducto = ProductFragment()
                DialogoCRUDEliminar("Actualizacion de Producto", "Se Actualizo el Producto Correctamente",fproducto)
            }else{
                objutilidad.MensajeToast(raiz.context,"Seleccione un elemento de la lista")
                lstPro.requestFocus()
            }
        }
        btnEliminarProd.setOnClickListener {
            if(fila>=0){
                cod = lblCodPro.text.toString().toInt()


                EliminarProduct(raiz.context,cod.toLong())
                val fproducto = ProductFragment()
                DialogoCRUDEliminar("¿Eliminar el Producto?", "¿Desea Eliminar el Producto?",fproducto)

            }else{
                objutilidad.MensajeToast(raiz.context,"Seleccione un elemento de la lista")
                lstPro.requestFocus()
            }
        }



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

    fun registrarProducto(context: Context, p:Product){
        val call = productService!!.RegistrarProduct(p)
        call!!.enqueue(object :Callback<Product?>{
            override fun onResponse(call: Call<Product?>, response: Response<Product?>) {
                if(response.isSuccessful){
                    objutilidad.MensajeToast(context, "Se registro el producto")
                }
            }

            override fun onFailure(call: Call<Product?>, t: Throwable) {
                Log.e("Error: ", t.message!!)
            }

        })
    }
    fun ActualizarProduct(context: Context, p:Product, id: Long ){
        val call = productService!!.ActualizarProduct(id,p)
        call!!.enqueue(object : Callback<List<Product>?>{
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
        call!!.enqueue(object: Callback<List<Product>?>{
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