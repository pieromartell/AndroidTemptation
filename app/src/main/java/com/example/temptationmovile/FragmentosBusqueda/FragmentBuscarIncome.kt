package com.example.temptationmovile.FragmentosBusqueda

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.temptationmovile.R
import com.example.temptationmovile.adaptadores.AdaptadorBusquedaIncome
import com.example.temptationmovile.adaptadores.AdaptadorBusquedaProvider
import com.example.temptationmovile.clases.Income
import com.example.temptationmovile.clases.Provider
import com.example.temptationmovile.databinding.FragmentBuscarIncomeBinding
import com.example.temptationmovile.databinding.FragmentBuscarProviderBinding
import com.example.temptationmovile.remoto.ApiUtil
import com.example.temptationmovile.servicios.IncomeService
import com.example.temptationmovile.servicios.ProviderService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentBuscarIncome.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentBuscarIncome : Fragment() {

    private var _binding: FragmentBuscarIncomeBinding?=null
    private val binding get() = _binding!!
    private var fila:Int?=null
    private var incomeService: IncomeService?=null
    private var registroIncome:List<Income>?=null
    private var objIncome= Income()
    var ft: FragmentTransaction?=null
    private var dialogo: AlertDialog.Builder?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentBuscarIncomeBinding.inflate(inflater, container, false)
        registroIncome=ArrayList()
        incomeService= ApiUtil.incomeService
        mostrarUsers()
        binding.txtBusIncome.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                (binding.lstBusIncome.adapter as AdaptadorBusquedaIncome).filter(newText ?: "")
                return true
            }
        })

        binding.lstBusIncome.setOnItemClickListener(
            { adapterView, view,i, id ->
                fila = i
                objIncome.idincome=(registroIncome as ArrayList<Income>).get(fila!!).idincome
                objIncome.idprovider=(registroIncome as ArrayList<Income>).get(fila!!).idprovider
                objIncome.dateinco=(registroIncome as ArrayList<Income>).get(fila!!).dateinco
                objIncome.state=(registroIncome as ArrayList<Income>).get(fila!!).state
                //asignamos los valores a cada control
                binding.txtBusIdIncome.setText(objIncome.idincome.toString())
                if(objIncome.state != 0){
                    binding.btnStateIncome.setText("Deshabilitar")
                }else{
                    binding.btnStateIncome.setText("Habilitar")
                }
            }
        )
        binding.btnStateIncome.setOnClickListener {
            if (binding.txtBusIdIncome.text.toString().length>0){
                if (objIncome.state==1){
                    EliminarUsuario(objIncome.idprovider.toLong())
                    DialogoCRUD("Usuario","Se deshabilitó el usuario "+objIncome.state,FragmentBuscarIncome())
                }else{
                    objIncome.state=1
                    ActualizarUsuario(objIncome,objIncome.idprovider.toLong())
                    DialogoCRUD("Usuario","Se habilitó el usuario "+objIncome.state,FragmentBuscarIncome())
                }
            }else{
                Toast.makeText(binding.root.context,"Debe Seleccionar un usuario", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    fun mostrarUsers(){
        val call = incomeService!!.MostrarIncomes()
        call!!.enqueue(object : Callback<List<Income>> {
            override fun onResponse(call: Call<List<Income>>, response: Response<List<Income>>) {
                if(response.isSuccessful){
                    println("Correcto")
                    registroIncome = response.body()
                    binding.lstBusIncome.adapter = AdaptadorBusquedaIncome(binding.root.context,registroIncome)
                }else{
                    println("Error")
                }
            }
            override fun onFailure(call: Call<List<Income>>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })
    }

    fun ActualizarUsuario(p: Income, id: Long ){
        val call = incomeService!!.ActualizarIncome(id,p)
        call!!.enqueue(object : Callback<List<Income>?> {
            override fun onResponse(call: Call<List<Income>?>, response: Response<List<Income>?>) {
                if(response.isSuccessful){
                    Log.e("Mensaje", "Se actualizo correctamente el Usuario")
                }
            }
            override fun onFailure(call: Call<List<Income>?>, t: Throwable) {
                Log.e("Error: ",t.message!!)
            }
        })
    }

    fun EliminarUsuario(id: Long){
        val call = incomeService!!.EliminarIncome(id)
        call!!.enqueue(object: Callback<List<Income>?> {
            override fun onResponse(call: Call<List<Income>?>, response: Response<List<Income>?>) {
                if(response.isSuccessful){
                    Log.e("mensaje","Se elimino correctamente")
                }
            }
            override fun onFailure(call: Call<List<Income>?>, t: Throwable) {
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



}