package com.example.temptationmovile

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.temptationmovile.adaptadores.AdaptadorComboProvider
import com.example.temptationmovile.adaptadores.AdaptadorIncome
import com.example.temptationmovile.clases.Income
import com.example.temptationmovile.clases.Provider
import com.example.temptationmovile.remoto.ApiUtil
import com.example.temptationmovile.servicios.IncomeService
import com.example.temptationmovile.servicios.ProviderService
import com.example.temptationmovile.utilidad.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [incomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class incomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var cboprovider : Spinner
    private lateinit var btnregistroinco : Button
    private lateinit var btnsalirincome : Button
    private lateinit var btndeta_income : Button
    private lateinit var txtfechainco : TextView
    private lateinit var lstinco : ListView

    private val objprovider = Provider()

    private val objIncome = Income()
    private var fechaincome = ""
    private var state = 1
    private var idProvider= 0
    private var codProvider= 0

    private var indiceP=0
    private var fila = -1

    private var providerService: ProviderService?=null
    private var incomeService: IncomeService?=null

    private var registroIncome: List<Income>? = null
    private var registroProvider: List<Provider>? = null

    private var fecha = Calendar.getInstance().time
    private val formatoFecha = SimpleDateFormat("dd/MM/yyyy")
    private var fechaAct = formatoFecha.format(fecha)
    private var fechita = fechaAct

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
        val raiz = inflater.inflate(R.layout.fragment_income, container, false)
        txtfechainco = raiz.findViewById(R.id.txtfechacomprainco)
        cboprovider = raiz.findViewById(R.id.cboprovidersincome)
        btnsalirincome = raiz.findViewById(R.id.btnsalirincome)
        btnregistroinco = raiz.findViewById(R.id.btnregistrarincome)
        btndeta_income = raiz.findViewById(R.id.btndeta_inco)
        lstinco = raiz.findViewById(R.id.lstincome)
        registroIncome = ArrayList()
        registroProvider = ArrayList()
        providerService = ApiUtil.providerService
        incomeService = ApiUtil.incomeService

        mostrarComboProvider(raiz.context)
        mostrarincome(raiz.context)

        txtfechainco.setText(fechaAct)

        btnregistroinco.setOnClickListener {
            if(cboprovider.selectedItemPosition==-1){
                objutilidad.MensajeToast(raiz.context,"Seleccionne un Proveedor")
                cboprovider.requestFocus()
            }else if(txtfechainco.text.toString() == "") {
                objutilidad.MensajeToast(raiz.context, "Ingresa la fecha")
                txtfechainco.requestFocus()
            }else{
                //fechaincome=txtfechainco.text.toString()
                fechita = fechaAct
                idProvider = cboprovider.selectedItemPosition
                codProvider = (registroProvider as ArrayList<Provider>).get(idProvider).idprovider

                objIncome.idprovider=codProvider
                objIncome.dateinco=fechita

                registroIncome(raiz.context,objIncome)
                val fincome = incomeFragment()
                DialogoCRUD("Registro de Producto", "Se registro la Compra Correctamente",fincome)
            }
        }

        lstinco.setOnItemClickListener { adapterView, view, i, l ->
            fila = i

            txtfechainco.setText(""+ (registroIncome as ArrayList<Income>).get(fila).dateinco.toString())
            for (x in (registroProvider as ArrayList<Provider>).indices){
                if((registroProvider as ArrayList<Provider>).get(x).idprovider == (registroIncome as ArrayList<Income>).get(fila).idprovider){
                    indiceP = x
                }
            }
            cboprovider.setSelection(indiceP)

        }

        /*btnregistroinco.setOnClickListener {
            if(fila>=0){
                fechaincome = txtfechainco.text.toString()

                objIncome.

            }
        }*/



        return raiz
        //return inflater.inflate(R.layout.fragment_income, container, false)
    }

    fun mostrarComboProvider(context: Context){
        val call = providerService!!.MostrarProvider()
        call!!.enqueue(object: Callback<List<Provider>?> {
            override fun onResponse(call: Call<List<Provider>?>, response: Response<List<Provider>?>) {
                if(response.isSuccessful){
                    registroProvider = response.body()
                    cboprovider.adapter = AdaptadorComboProvider(context, registroProvider)
                }
            }

            override fun onFailure(call: Call<List<Provider>?>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })
    }



    fun registroIncome(context: Context, p: Income){
        val call = incomeService!!.RegistrarIncome(p)
        call!!.enqueue(object :Callback<Income?>{
            override fun onResponse(call: Call<Income?>, response: Response<Income?>) {
                if(response.isSuccessful){
                    objutilidad.MensajeToast(context, "Se registro la Compra")
                }
            }

            override fun onFailure(call: Call<Income?>, t: Throwable) {
                Log.e("Error: ", t.message!!)
            }

        })
    }


    fun mostrarincome(context: Context){
        val call = incomeService!!.MostrarIncomes()
        call!!.enqueue(object :Callback<List<Income>>{
            override fun onResponse(call: Call<List<Income>>, response: Response<List<Income>>) {
                if(response.isSuccessful){
                    println("Correcto")
                    registroIncome = response.body()
                    lstinco.adapter = AdaptadorIncome(context, registroIncome!!)
                }else{
                    println("Error")
                }
            }

            override fun onFailure(call: Call<List<Income>>, t: Throwable) {
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
















    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment income.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            incomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}