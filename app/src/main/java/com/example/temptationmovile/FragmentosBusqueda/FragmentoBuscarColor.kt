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
import com.example.temptationmovile.adaptadores.AdaptadorFilterColor
import com.example.temptationmovile.adaptadores.AdapterFilterCategory
import com.example.temptationmovile.clases.Category
import com.example.temptationmovile.clases.Color
import com.example.temptationmovile.remoto.ApiUtil
import com.example.temptationmovile.servicios.CategoryService
import com.example.temptationmovile.servicios.ColorService
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
 * Use the [FragmentoBuscarColor.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentoBuscarColor : Fragment() {

    //declaramos los controles
    private lateinit var txtBusColor: SearchView
    private lateinit var lstColorBusqueda: ListView
    private lateinit var btnStateColor: Button
    private lateinit var txtIdColorBus: TextView

    private val objcolor= Color()

    //creamos variables
    private var cod=0L
    private var fila=-1
    private var nom=""
    private var est=false

    private var colorService: ColorService?=null

    private var registrocolor:List<Color>?=null

    private val objutilidad= Util()

    var ft: FragmentTransaction?=null

    private var dialogo: AlertDialog.Builder?=null











    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val raiz=inflater.inflate(R.layout.fragmento_buscar_color,container,false)
        txtBusColor =raiz.findViewById(R.id.txtBusColor)
        lstColorBusqueda = raiz.findViewById(R.id.lstColorBusqueda)
        btnStateColor = raiz.findViewById(R.id.btnStateColor)
        txtIdColorBus = raiz.findViewById(R.id.txtIdColorBus)

        registrocolor =ArrayList()

        colorService = ApiUtil.colorservice
        MostrarColor(raiz.context)
        txtBusColor.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                (lstColorBusqueda.adapter as AdaptadorFilterColor).filter(newText ?: "")

                return true
            }
        })

        lstColorBusqueda.setOnItemClickListener { adapterView, view, i, l ->
            fila = i
            objcolor.idcolor=(registrocolor as ArrayList<Color>).get(fila!!).idcolor
            objcolor.name_col=(registrocolor as ArrayList<Color>).get(fila!!).name_col
            objcolor.state=(registrocolor as ArrayList<Color>).get(fila!!).state
            //asignamos los valores a cada control
            txtIdColorBus.setText(objcolor.idcolor.toString())
            if(objcolor.state != 0){
                btnStateColor.setText("Deshabilitar")
            }else{
                btnStateColor.setText("Habilitar")
            }
        }

        btnStateColor.setOnClickListener {
            if (txtIdColorBus.text.toString().length>0){
                if (objcolor.state==1){
                    EliminarColor(objcolor.idcolor.toLong())
                    DialogoCRUD("Rol","Se deshabilitó el rol "+objcolor.name_col,FragmentoBuscarColor())
                }else{
                    objcolor.state=1
                    ActualizarColor(objcolor,objcolor.idcolor.toLong())
                    DialogoCRUD("Rol","Se habilitó el rol "+objcolor.name_col,FragmentoBuscarColor())
                }
            }else{
                Toast.makeText(raiz.context,"Debe Seleccionar un rol", Toast.LENGTH_SHORT).show()
            }
        }

        return raiz

    }

    fun MostrarColor(context: Context?){
        val call = colorService!!.MostrarColor()
        call!!.enqueue(object : Callback<List<Color>?>{
            override fun onResponse(call: Call<List<Color>?>, response: Response<List<Color>?>) {
                if(response.isSuccessful){
                    registrocolor=response.body()
                    lstColorBusqueda.adapter= AdaptadorFilterColor(context,registrocolor)
                }else{
                    Log.e("Error: ","NO SALE EL FILTRADO")
                }
            }

            override fun onFailure(call: Call<List<Color>?>, t: Throwable) {
                Log.e("Error: ", t.message!!)
            }

        })
    }


    fun ActualizarColor(b:Color,id:Long){
        val call = colorService!!.ActualizarColor(id,b)
        call!!.enqueue(object :Callback<List<Color>?>{
            override fun onResponse(call: Call<List<Color>?>, response: Response<List<Color>?>) {
                if(response.isSuccessful){
                    Log.e("Mensaje", "Se actualizo correctamente")
                }
            }
            override fun onFailure(call: Call<List<Color>?>, t: Throwable) {
                Log.e("Error: ",t.message!!)
            }

        })
    }

    fun EliminarColor(id:Long){
        val call = colorService!!.EliminarColor(id)
        call!!.enqueue(object :Callback<List<Color>?>{
            override fun onResponse(call: Call<List<Color>?>, response: Response<List<Color>?>) {
                if(response.isSuccessful){
                    Log.e("mensaje","Se elimino correctamente")
                }
            }

            override fun onFailure(call: Call<List<Color>?>, t: Throwable) {
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














    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentoBuscarColor.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentoBuscarColor().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}