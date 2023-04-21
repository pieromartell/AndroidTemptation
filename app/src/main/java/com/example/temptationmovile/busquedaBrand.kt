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
import com.example.temptationmovile.adaptadores.Adaptadorbrand
import com.example.temptationmovile.adaptadores.AdapterFilterBrand
import com.example.temptationmovile.clases.Brand
import com.example.temptationmovile.databinding.BrandFragmentBinding
import com.example.temptationmovile.remoto.ApiUtil
import com.example.temptationmovile.servicios.BrandService
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
 * Use the [busquedaBrand.newInstance] factory method to
 * create an instance of this fragment.
 */
class busquedaBrand : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var txtBusquedaBrand: SearchView
    private lateinit var lstbrandBusqueda: ListView

    val objbrand = Brand()
    private var idbrand = 0
    private var name_brand =""
    private var state = 1
    private var fila =-1

    private lateinit var binding: BrandFragmentBinding
    private var brandservice: BrandService? = null
    private var registrobrand: List<Brand>?=null
    var objutilidad =  Util()

    //creamos transicion para fragmento
    var ft: FragmentTransaction?= null


    private var dialogo: AlertDialog.Builder? = null


    private var _binding: BrandFragmentBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val raiz=inflater.inflate(R.layout.fragment_busqueda_brand,container,false)
        //creamos los controles
        txtBusquedaBrand=raiz.findViewById(R.id.txtbusquedabrand)

        lstbrandBusqueda=raiz.findViewById(R.id.lstBrandBuscar)

        registrobrand = ArrayList()

        brandservice = ApiUtil.brandservice


        mostrarBrand(raiz.context)

        txtBusquedaBrand.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                (lstbrandBusqueda.adapter as AdapterFilterBrand).filter(newText ?: "")

                return true
            }
        })

        return raiz
    }

    //CREAMOS LA FUNCION PARA MOSTRAR LAS CATEGORIAS
    fun mostrarBrand(context: Context){
        val call = brandservice!!.MostrarBrand()
        call!!.enqueue(object : Callback<List<Brand>?> {
            override fun onResponse(
                call: Call<List<Brand>?>,
                response: Response<List<Brand>?>
            ) {
                if(response.isSuccessful){
                    registrobrand = response.body()
                    lstbrandBusqueda.adapter = AdapterFilterBrand(context,registrobrand)
                }
            }

            override fun onFailure(call: Call<List<Brand>?>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}