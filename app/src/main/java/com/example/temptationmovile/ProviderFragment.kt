package com.example.temptationmovile

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentTransaction
import com.example.temptationmovile.adaptadores.AdaptadorProvider
import com.example.temptationmovile.clases.Brand
import com.example.temptationmovile.clases.Provider
import com.example.temptationmovile.databinding.BrandFragmentBinding
import com.example.temptationmovile.remoto.ApiUtil
import com.example.temptationmovile.servicios.ProviderService
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
 * Use the [ProviderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProviderFragment : Fragment() {
    private lateinit var txtNomb: EditText
    private lateinit var chbEst: CheckBox
    private lateinit var lblCodProv: TextView
    private lateinit var btnRegistrar: Button
    private lateinit var btnActualizar: Button
    private lateinit var btnEliminar: Button
    private lateinit var btnSalir: Button
    private lateinit var lstProvider: ListView

    val objprob = Provider()
    private var idprovider=0
    private var name_prov=""
    private var ruc=""
    private var company_name=""
    private var phone=0
    private var email=""
    private var description=""
    private var address=""
    private var state=1
    private var fila =-1

    private lateinit var binding: ProviderFragment
    private var providerservice: ProviderService? = null
    private var registroprovider: List<Provider>?=null
    var objutilidad =  Util()

    //creamos transicion para fragmento
    var ft: FragmentTransaction?= null
    private var _binding: BrandFragmentBinding? = null

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_provider, container, false)
        val raiz=inflater.inflate(R.layout.fragment_provider,container,false)
        //creamos los controles
        txtNomb=raiz.findViewById(R.id.txtnombreprov)
        chbEst=raiz.findViewById(R.id.chbprovider)
        lblCodProv=raiz.findViewById(R.id.lblidprovider)
        btnRegistrar=raiz.findViewById(R.id.btnregistrarprov)
        btnActualizar=raiz.findViewById(R.id.btnactualizarprov)
        btnEliminar=raiz.findViewById(R.id.btneliminarprov)
        lstProvider=raiz.findViewById(R.id.lstprovider)
        registroprovider = ArrayList()

        providerservice = ApiUtil.providerService


        mostrarProvider(raiz.context)

        btnRegistrar.setOnClickListener {
            if (txtNomb.getText().toString() == "") {
                objutilidad.MensajeToast(raiz.context, "Ingrese el Nombre")
                txtNomb.requestFocus()
            } else {
                name_prov = txtNomb.getText().toString()
                state = if (chbEst.isChecked) 1 else 0
                //envienadoo los valores
                objprob.name_prov = name_prov
                objprob.state = state
                Log.e(objprob.name_prov, (objprob.state).toString())
                registrar(raiz.context, objprob)
                Log.e(objprob.name_prov, (objprob.state).toString())

                //actualizamos el brand
                val fprov = ProviderFragment()
                ft = fragmentManager?.beginTransaction()
                ft?.replace(R.id.contenedor,fprov,null)
                ft?.addToBackStack(null)
                ft?.commit()
            }
        }

        lstProvider.setOnItemClickListener(
            { adapterView, view,i, id ->
                fila = i
                //asignamos los valores a cada control
                lblCodProv.setText(""+(registroprovider as ArrayList<Provider>).get(fila).idprovider)
                txtNomb.setText(""+(registroprovider as ArrayList<Provider>).get(fila).name_prov)
                if((registroprovider as ArrayList<Provider>).get(fila).state != 0){
                    chbEst.setChecked(true)
                }else{
                    chbEst.setChecked(false)
                }

            }
        )
        return raiz


    }

    //CREAMOS LA FUNCION PARA MOSTRAR LAS CATEGORIAS
    fun mostrarProvider(context: Context){
        val call = providerservice!!.MostrarProvider()
        call!!.enqueue(object : Callback<List<Provider>?> {
            override fun onResponse(
                call: Call<List<Provider>?>,
                response: Response<List<Provider>?>
            ) {
                if(response.isSuccessful){
                    registroprovider = response.body()
                    lstProvider.adapter = AdaptadorProvider(context,registroprovider)
                }
            }

            override fun onFailure(call: Call<List<Provider>?>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })
    }

    fun registrar(context: Context,c:Provider){
        val call = providerservice!!.RegistrarProvider(c)
        call!!.enqueue(object :Callback<Provider?>{
            override fun onResponse(call: Call<Provider?>, response: Response<Provider?>) {
                if(response.isSuccessful){
                    objutilidad.MensajeToast(context, "Se registro al proveedor")
                }
            }

            override fun onFailure(call: Call<Provider?>, t: Throwable) {
                Log.e("Error: ", t.message!!)
            }

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    /*companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProviderFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProviderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }*/
}