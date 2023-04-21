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
import com.example.temptationmovile.adaptadores.AdaptadorBusquedaDetailIncome
import com.example.temptationmovile.adaptadores.AdaptadorBusquedaIncome
import com.example.temptationmovile.clases.DetailIncome
import com.example.temptationmovile.clases.Income
import com.example.temptationmovile.databinding.FragmentBuscarDetailIncomeBinding
import com.example.temptationmovile.databinding.FragmentBuscarIncomeBinding
import com.example.temptationmovile.remoto.ApiUtil
import com.example.temptationmovile.servicios.DetailIncomeService
import com.example.temptationmovile.servicios.IncomeService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentBuscarDetailIncome.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentBuscarDetailIncome : Fragment() {

    private var _binding: FragmentBuscarDetailIncomeBinding?=null
    private val binding get() = _binding!!
    private var fila:Int?=null
    private var detailincomeService: DetailIncomeService?=null
    private var registroDetailIncome:List<DetailIncome>?=null
    private var objDetailIncome= DetailIncome()
    var ft: FragmentTransaction?=null
    private var dialogo: AlertDialog.Builder?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentBuscarDetailIncomeBinding.inflate(inflater, container, false)
        registroDetailIncome=ArrayList()
        detailincomeService= ApiUtil.detailincomeService
        mostrarUsers()
        binding.txtBusDetailIncome.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                (binding.lstBusDetailIncome.adapter as AdaptadorBusquedaDetailIncome).filter(newText ?: "")
                return true
            }
        })

        binding.lstBusDetailIncome.setOnItemClickListener(
            { adapterView, view,i, id ->
                fila = i
                objDetailIncome.iddetincome=(registroDetailIncome as ArrayList<DetailIncome>).get(fila!!).iddetincome
                objDetailIncome.idincome=(registroDetailIncome as ArrayList<DetailIncome>).get(fila!!).idincome
                objDetailIncome.idproduc=(registroDetailIncome as ArrayList<DetailIncome>).get(fila!!).idproduc
                objDetailIncome.price_buy=(registroDetailIncome as ArrayList<DetailIncome>).get(fila!!).price_buy
                objDetailIncome.quantity=(registroDetailIncome as ArrayList<DetailIncome>).get(fila!!).quantity
                objDetailIncome.igv=(registroDetailIncome as ArrayList<DetailIncome>).get(fila!!).igv
                //asignamos los valores a cada control
                binding.txtBusIdDetailIncome.setText(objDetailIncome.iddetincome.toString())

            }
        )
        /*binding.btnStateDetailIncome.setOnClickListener {
            if (binding.txtBusIdDetailIncome.text.toString().length>0){
                if (objDetailIncome.state==1){
                    EliminarUsuario(objDetailIncome.idprovider.toLong())
                    DialogoCRUD("Usuario","Se deshabilitó el usuario "+objIncome.dateinco,FragmentBuscarProvider())
                }else{
                    objIncome.state=1
                    ActualizarUsuario(objIncome,objIncome.idprovider.toLong())
                    DialogoCRUD("Usuario","Se habilitó el usuario "+objIncome.dateinco,FragmentBuscarProvider())
                }
            }else{
                Toast.makeText(binding.root.context,"Debe Seleccionar un usuario", Toast.LENGTH_SHORT).show()
            }
        }*/

        return binding.root
    }

    fun mostrarUsers(){
        val call = detailincomeService!!.MostrarDetailIncomes()
        call!!.enqueue(object : Callback<List<DetailIncome>> {
            override fun onResponse(call: Call<List<DetailIncome>>, response: Response<List<DetailIncome>>) {
                if(response.isSuccessful){
                    println("Correcto")
                    registroDetailIncome = response.body()
                    binding.lstBusDetailIncome.adapter = AdaptadorBusquedaDetailIncome(binding.root.context,registroDetailIncome)
                }else{
                    println("Error")
                }
            }
            override fun onFailure(call: Call<List<DetailIncome>>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })
    }

    fun ActualizarUsuario(p: DetailIncome, id: Long ){
        val call = detailincomeService!!.ActualizarDetailIncome(id,p)
        call!!.enqueue(object : Callback<List<DetailIncome>?> {
            override fun onResponse(call: Call<List<DetailIncome>?>, response: Response<List<DetailIncome>?>) {
                if(response.isSuccessful){
                    Log.e("Mensaje", "Se actualizo correctamente el Usuario")
                }
            }
            override fun onFailure(call: Call<List<DetailIncome>?>, t: Throwable) {
                Log.e("Error: ",t.message!!)
            }
        })
    }

    fun EliminarUsuario(id: Long){
        val call = detailincomeService!!.EliminarDetailIncome(id)
        call!!.enqueue(object: Callback<List<DetailIncome>?> {
            override fun onResponse(call: Call<List<DetailIncome>?>, response: Response<List<DetailIncome>?>) {
                if(response.isSuccessful){
                    Log.e("mensaje","Se elimino correctamente")
                }
            }
            override fun onFailure(call: Call<List<DetailIncome>?>, t: Throwable) {
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