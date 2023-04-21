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
import com.example.temptationmovile.adaptadores.AdaptadorFilterStyle
import com.example.temptationmovile.clases.Color
import com.example.temptationmovile.clases.Style
import com.example.temptationmovile.remoto.ApiUtil
import com.example.temptationmovile.servicios.ColorService
import com.example.temptationmovile.servicios.StyleService
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
 * Use the [FragmentoBuscarStyle.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentoBuscarStyle : Fragment() {

    //declaramos los controles
    private lateinit var txtBusStyle: SearchView
    private lateinit var lstStyleBusqueda: ListView
    private lateinit var btnStateStyle: Button
    private lateinit var txtIdStyleBus: TextView

    private val objstyle= Style()

    //creamos variables
    private var cod=0L
    private var fila=-1
    private var nom=""
    private var est=false

    private var styleService: StyleService?=null

    private var registrostyle:List<Style>?=null

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
        val raiz=inflater.inflate(R.layout.fragmento_buscar_style,container,false)
        txtBusStyle =raiz.findViewById(R.id.txtBusStyle)
        lstStyleBusqueda = raiz.findViewById(R.id.lstStyleBusqueda)
        btnStateStyle = raiz.findViewById(R.id.btnStateStyle)
        txtIdStyleBus = raiz.findViewById(R.id.txtIdStyleBus)


        registrostyle = ArrayList()
        styleService = ApiUtil.styleService
        MostrarStyle(raiz.context)
        txtBusStyle.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                (lstStyleBusqueda.adapter as AdaptadorFilterStyle).filter(newText ?: "")

                return true
            }
        })

        lstStyleBusqueda.setOnItemClickListener { adapterView, view, i, l ->
            fila = i
            objstyle.idstyles=(registrostyle as ArrayList<Style>).get(fila!!).idstyles
            objstyle.name_sty=(registrostyle as ArrayList<Style>).get(fila!!).name_sty
            objstyle.state=(registrostyle as ArrayList<Style>).get(fila!!).state
            //asignamos los valores a cada control
            txtIdStyleBus.setText(objstyle.idstyles.toString())
            if(objstyle.state != 0){
                btnStateStyle.setText("Deshabilitar")
            }else{
                btnStateStyle.setText("Habilitar")
            }
        }

        btnStateStyle.setOnClickListener {
            if (txtIdStyleBus.text.toString().length>0){
                if (objstyle.state==1){
                    EliminarStyle(objstyle.idstyles.toLong())
                    DialogoCRUD("Rol","Se deshabilitó el rol "+objstyle.name_sty,FragmentoBuscarStyle())
                }else{
                    objstyle.state=1
                    ActualizarStyle(objstyle,objstyle.idstyles.toLong())
                    DialogoCRUD("Rol","Se habilitó el rol "+objstyle.name_sty,FragmentoBuscarStyle())
                }
            }else{
                Toast.makeText(raiz.context,"Debe Seleccionar un rol", Toast.LENGTH_SHORT).show()
            }
        }

        return raiz
    }

    fun MostrarStyle(context: Context?){
        val call = styleService!!.MostrarEstilo()
        call!!.enqueue(object :Callback<List<Style>?>{
            override fun onResponse(call: Call<List<Style>?>, response: Response<List<Style>?>) {
                if(response.isSuccessful){
                    registrostyle=response.body()
                    lstStyleBusqueda.adapter= AdaptadorFilterStyle(context,registrostyle)
                }else{
                    Log.e("Error: ","NO SALE EL FILTRADO")
                }
            }

            override fun onFailure(call: Call<List<Style>?>, t: Throwable) {
                Log.e("Error: ", t.message!!)
            }

        })
    }

    fun ActualizarStyle(b:Style, id:Long){
        val call = styleService!!.ActualizarEstilo(id,b)
        call!!.enqueue(object :Callback<List<Style>?>{
            override fun onResponse(call: Call<List<Style>?>, response: Response<List<Style>?>) {
                if(response.isSuccessful){
                    Log.e("Mensaje", "Se actualizo correctamente")
                }
            }

            override fun onFailure(call: Call<List<Style>?>, t: Throwable) {
                Log.e("Error: ",t.message!!)
            }

        })
    }

    fun EliminarStyle(id:Long){
        val call = styleService!!.EliminarEstilo(id)
        call!!.enqueue(object :Callback<List<Style>?>{
            override fun onResponse(call: Call<List<Style>?>, response: Response<List<Style>?>) {

                if(response.isSuccessful){
                    Log.e("mensaje","Se elimino correctamente")
                }
            }

            override fun onFailure(call: Call<List<Style>?>, t: Throwable) {
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
         * @return A new instance of fragment FragmentoBuscarStyle.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentoBuscarStyle().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}