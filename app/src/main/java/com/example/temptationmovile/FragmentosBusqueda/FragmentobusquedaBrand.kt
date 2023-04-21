package com.example.temptationmovile.FragmentosBusqueda

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.temptationmovile.R
import com.example.temptationmovile.adaptadores.AdaptadorFilterBrand
import com.example.temptationmovile.clases.Brand
import com.example.temptationmovile.databinding.BrandFragmentBinding
import com.example.temptationmovile.remoto.ApiUtil
import com.example.temptationmovile.servicios.BrandService
import com.example.temptationmovile.utilidad.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentobusquedaBrand.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentobusquedaBrand : Fragment() {
    // TODO: Rename and change types of parameters

    private lateinit var txtBusquedaBrand: SearchView
    private lateinit var lstbrandBusqueda: ListView
    private lateinit var btnbuscarbrand :Button
    private lateinit var txtIdBrand: TextView

    val objbrand = Brand()
    private var idbrand = 0
    private var name_brand =""
    private var state = 1
    private var fila =-1

    private lateinit var binding: BrandFragmentBinding
    private var brandservice: BrandService? = null
    private var registrobrand: List<Brand>?=null
    var objutilidad = Util()

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

        btnbuscarbrand = raiz.findViewById(R.id.btnbuscarbrand)

        txtIdBrand = raiz.findViewById(R.id.txtIdBuscarbrand)

        registrobrand = ArrayList()

        brandservice = ApiUtil.brandservice


        mostrarBrand(raiz.context)

        txtBusquedaBrand.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                (lstbrandBusqueda.adapter as AdaptadorFilterBrand).filter(newText ?: "")

                return true
            }
        })

        lstbrandBusqueda.setOnItemClickListener(
            { adapterView, view,i, id ->
                fila = i
                objbrand.idbrand=(registrobrand as ArrayList<Brand>).get(fila!!).idbrand
                objbrand.name_brand=(registrobrand as ArrayList<Brand>).get(fila!!).name_brand
                objbrand.state=(registrobrand as ArrayList<Brand>).get(fila!!).state
                //asignamos los valores a cada control
                txtIdBrand.setText(objbrand.idbrand.toString())
                if(objbrand.state != 0){
                    btnbuscarbrand.setText("Deshabilitar")
                }else{
                    btnbuscarbrand.setText("Habilitar")
                }
            }
        )

        btnbuscarbrand.setOnClickListener {
            if (txtIdBrand.text.toString().length>0){
                if (objbrand.state==1){
                    EliminarBrand(objbrand.idbrand.toLong())
                    DialogoCRUD("Marca","Se deshabilitó la marca "+objbrand.name_brand,FragmentobusquedaBrand())
                }else{
                    objbrand.state=1
                    ActualizarBrand(objbrand,objbrand.idbrand.toLong())
                    DialogoCRUD("Marca","Se habilitó la marca "+objbrand.name_brand,FragmentobusquedaBrand())
                }
            }else{
                Toast.makeText(raiz.context,"Debe Seleccionar una marca", Toast.LENGTH_SHORT).show()
            }
        }

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
                    lstbrandBusqueda.adapter = AdaptadorFilterBrand(context, registrobrand)
                }
            }

            override fun onFailure(call: Call<List<Brand>?>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })
    }
    fun ActualizarBrand(b: Brand, id: Long ){
        val call = brandservice!!.ActualizarBrand(id,b)
        call!!.enqueue(object : Callback<List<Brand>?>{
            override fun onResponse(call: Call<List<Brand>?>, response: Response<List<Brand>?>) {
                if(response.isSuccessful){
                    Log.e("Mensaje", "Se actualizo correctamente")
                }
            }

            override fun onFailure(call: Call<List<Brand>?>, t: Throwable) {
                Log.e("Error: ",t.message!!)
            }

        })
    }
    fun EliminarBrand(id: Long){
        val call = brandservice!!.EliminarBrand(id)
        call!!.enqueue(object: Callback<List<Brand>?>{
            override fun onResponse(call: Call<List<Brand>?>, response: Response<List<Brand>?>) {
                if(response.isSuccessful){
                    Log.e("mensaje","Se elimino correctamente")
                }
            }

            override fun onFailure(call: Call<List<Brand>?>, t: Throwable) {
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}