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
import com.example.temptationmovile.adaptadores.AdapterFilterCategory
import com.example.temptationmovile.clases.Category
import com.example.temptationmovile.clases.Rol
import com.example.temptationmovile.databinding.FragmentoBuscarCategoriaBinding
import com.example.temptationmovile.remoto.ApiUtil
import com.example.temptationmovile.servicios.CategoryService
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
 * Use the [FragmentoBuscarCategoria.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentoBuscarCategoria : Fragment() {


    //declaramos los controles
    private lateinit var txtBusCat: SearchView
    private lateinit var lstCatBusqueda: ListView
    private lateinit var btnStateCat: Button
    private lateinit var txtIdCatBus: TextView

    private val objcategoria= Category()

    //creamos variables
    private var cod=0L
    private var fila=-1
    private var nom=""
    private var est=false

    private var categoryService:CategoryService?=null

    private var registrocategoria:List<Category>?=null

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
        val raiz=inflater.inflate(R.layout.fragmento_buscar_categoria,container,false)
        txtBusCat =raiz.findViewById(R.id.txtBusCat)
        lstCatBusqueda = raiz.findViewById(R.id.lstCatBusqueda)
        btnStateCat = raiz.findViewById(R.id.btnStateCat)
        txtIdCatBus = raiz.findViewById(R.id.txtIdCatBus)

        //creamos el registro categoria
        registrocategoria=ArrayList()
        //implementamos el servicio
        categoryService= ApiUtil.categoryService
        //mostramos las categorias
        MostrarCategory(raiz.context)
        txtBusCat.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                (lstCatBusqueda.adapter as AdapterFilterCategory).filter(newText ?: "")

                return true
            }
        })
        lstCatBusqueda.setOnItemClickListener { adapterView, view, i, l ->
            fila = i
            objcategoria.idcat=(registrocategoria as ArrayList<Category>).get(fila!!).idcat
            objcategoria.name_cat=(registrocategoria as ArrayList<Category>).get(fila!!).name_cat
            objcategoria.state=(registrocategoria as ArrayList<Category>).get(fila!!).state
            //asignamos los valores a cada control
            txtIdCatBus.setText(objcategoria.idcat.toString())
            if(objcategoria.state != 0){
                btnStateCat.setText("Deshabilitar")
            }else{
                btnStateCat.setText("Habilitar")
            }
        }
        btnStateCat.setOnClickListener {
            if (txtIdCatBus.text.toString().length>0){
                if (objcategoria.state==1){
                    EliminarCategory(objcategoria.idcat.toLong())
                    DialogoCRUD("Rol","Se deshabilitó el rol "+objcategoria.name_cat,FragmentoBuscarCategoria())
                }else{
                    objcategoria.state=1
                    ActualizarCategory(objcategoria,objcategoria.idcat.toLong())
                    DialogoCRUD("Rol","Se habilitó el rol "+objcategoria.name_cat,FragmentoBuscarCategoria())
                }
            }else{
                Toast.makeText(raiz.context,"Debe Seleccionar un rol", Toast.LENGTH_SHORT).show()
            }
        }

        return raiz
    }



    fun MostrarCategory(context:Context?){
      val call =categoryService!!.MostrarCategory()
       call!!.enqueue(object :Callback<List<Category>?>{
           override fun onResponse(
               call: Call<List<Category>?>,
               response: Response<List<Category>?>
           ) {
               if(response.isSuccessful){
                   registrocategoria=response.body()
                   lstCatBusqueda.adapter= AdapterFilterCategory(context,registrocategoria)
               }else{
                   Log.e("Error: ","NO SALE EL FILTRADO")
               }
           }

           override fun onFailure(call: Call<List<Category>?>, t: Throwable) {
               Log.e("Error: ", t.message!!)
           }

       })
    }

   fun ActualizarCategory(b:Category,id:Long){
       val call = categoryService!!.ActualizarCategory(id,b)
       call!!.enqueue(object :Callback<List<Category>?>{
           override fun onResponse(
               call: Call<List<Category>?>,
               response: Response<List<Category>?>
           ) {
               if(response.isSuccessful){
                   Log.e("Mensaje", "Se actualizo correctamente")
               }
           }
           override fun onFailure(call: Call<List<Category>?>, t: Throwable) {
               Log.e("Error: ",t.message!!)
           }

       })
   }

    fun EliminarCategory(id:Long){
        val call = categoryService!!.EliminarCategory(id)
        call!!.enqueue(object : Callback<List<Category>?>{
            override fun onResponse(
                call: Call<List<Category>?>,
                response: Response<List<Category>?>
            ) {
                if(response.isSuccessful){
                    Log.e("mensaje","Se elimino correctamente")
                }
            }

            override fun onFailure(call: Call<List<Category>?>, t: Throwable) {
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
         * @return A new instance of fragment FragmentoBuscarCategoria.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentoBuscarCategoria().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}