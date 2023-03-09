package com.example.temptationmovile

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.temptationmovile.clases.Brand
import androidx.fragment.app.FragmentTransaction
import com.example.temptationmovile.adaptadores.Adaptadorbrand
import com.example.temptationmovile.databinding.BrandFragmentBinding
import com.example.temptationmovile.remoto.ApiUtil
import com.example.temptationmovile.servicios.BrandService
import com.example.temptationmovile.utilidad.util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [brandFragmene.newInstance] factory method to
 * create an instance of this fragment.
 */
class brandFragmene : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var txtNomb: EditText
    private lateinit var chbEst: CheckBox
    private lateinit var lblCodCat: TextView
    private lateinit var btnRegistra: Button
    private lateinit var btnActualizar: Button
    private lateinit var btnEliminar: Button
    private lateinit var btnsalir: Button
    private lateinit var lstbrand: ListView

    val objbrand = Brand()
    private var idbrand = 0
    private var name_brand =""
    private var state = 1
    private var fila =-1

    private lateinit var binding: BrandFragmentBinding
    private var brandservice: BrandService ? = null
    private var registrobrand: List<Brand>?=null
    var objutilidad =  util()

    //creamos transicion para fragmento
    var ft: FragmentTransaction?= null

    private var _binding: BrandFragmentBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val raiz=inflater.inflate(R.layout.brand_fragment,container,false)
        //creamos los controles
        txtNomb=raiz.findViewById(R.id.txtnombreBrand)
        chbEst=raiz.findViewById(R.id.chbestado)
        lblCodCat=raiz.findViewById(R.id.lblidbrand)
        btnRegistra=raiz.findViewById(R.id.btnregistrar)
        btnActualizar=raiz.findViewById(R.id.btnactualizar)
        btnEliminar=raiz.findViewById(R.id.btneliminar)
        lstbrand=raiz.findViewById(R.id.lstbrand)

        registrobrand = ArrayList()

        brandservice = ApiUtil.brandservice


        mostrarBrand(raiz.context)


        btnRegistra.setOnClickListener {
            if (txtNomb.getText().toString() == "") {
                objutilidad.MensajeToast(raiz.context, "Ingrese el Nombre")
                txtNomb.requestFocus()
            } else {
                name_brand = txtNomb.getText().toString()
                state = if (chbEst.isChecked) 1 else 0
                //envienadoo los valores
                objbrand.name_brand = name_brand
                objbrand.state = state
                Log.e(objbrand.name_brand, (objbrand.state).toString())
                registrar(raiz.context, objbrand)
                Log.e(objbrand.name_brand, (objbrand.state).toString())

                //actualizamos el brand
                val fbrand = brandFragmene()
                ft = fragmentManager?.beginTransaction()
                ft?.replace(R.id.contenedor,fbrand,null)
                ft?.addToBackStack(null)
                ft?.commit()
            }
        }

            lstbrand.setOnItemClickListener(
                { adapterView, view,i, id ->
                    fila = i
                    //asignamos los valores a cada control
                    lblCodCat.setText(""+(registrobrand as ArrayList<Brand>).get(fila).idbrand)
                    txtNomb.setText(""+(registrobrand as ArrayList<Brand>).get(fila).name_brand)
                    if((registrobrand as ArrayList<Brand>).get(fila).state != 0){
                        chbEst.setChecked(true)
                    }else{
                        chbEst.setChecked(false)
                    }

                }
            )
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
                    lstbrand.adapter = Adaptadorbrand(context,registrobrand)
                }
            }

            override fun onFailure(call: Call<List<Brand>?>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })
    }
    fun registrar(context: Context,c:Brand){
        val call = brandservice!!.RegistrarBrand(c)
        call!!.enqueue(object :Callback<Brand?>{
            override fun onResponse(call: Call<Brand?>, response: Response<Brand?>) {
                if(response.isSuccessful){
                    objutilidad.MensajeToast(context, "Se registro la marca")
                }
            }

            override fun onFailure(call: Call<Brand?>, t: Throwable) {
                Log.e("Error: ", t.message!!)
            }

        })
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}