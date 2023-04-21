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
import com.example.temptationmovile.adaptadores.AdaptadorBusquedaProvider
import com.example.temptationmovile.adaptadores.AdaptadorColor
import com.example.temptationmovile.adaptadores.AdaptadorPerson
import com.example.temptationmovile.adaptadores.AdaptadorProvider
import com.example.temptationmovile.clases.Person
import com.example.temptationmovile.clases.Provider
import com.example.temptationmovile.databinding.FragmentBuscarProviderBinding
import com.example.temptationmovile.databinding.FragmentoBuscarPersonBinding
import com.example.temptationmovile.remoto.ApiUtil
import com.example.temptationmovile.servicios.PersonService
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
 * Use the [FragmentBuscarProvider.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentBuscarProvider : Fragment() {

    private var _binding: FragmentBuscarProviderBinding?=null
    private val binding get() = _binding!!
    private var fila:Int?=null
    private var providerService: ProviderService?=null
    private var registroProvider:List<Provider>?=null
    private var objProvider= Provider()
    var ft: FragmentTransaction?=null
    private var dialogo: AlertDialog.Builder?=null

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentBuscarProviderBinding.inflate(inflater, container, false)
        registroProvider=ArrayList()
        providerService= ApiUtil.providerService
        mostrarUsers()
        binding.txtBusProvider.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                (binding.lstBusProvider.adapter as AdaptadorBusquedaProvider).filter(newText ?: "")
                return true
            }
        })

        binding.lstBusProvider.setOnItemClickListener(
            { adapterView, view,i, id ->
                fila = i
                objProvider.idprovider=(registroProvider as ArrayList<Provider>).get(fila!!).idprovider
                objProvider.name_prov=(registroProvider as ArrayList<Provider>).get(fila!!).name_prov
                objProvider.ruc=(registroProvider as ArrayList<Provider>).get(fila!!).ruc
                objProvider.company_name=(registroProvider as ArrayList<Provider>).get(fila!!).company_name
                objProvider.phone=(registroProvider as ArrayList<Provider>).get(fila!!).phone
                objProvider.email=(registroProvider as ArrayList<Provider>).get(fila!!).email
                objProvider.description=(registroProvider as ArrayList<Provider>).get(fila!!).description
                objProvider.address=(registroProvider as ArrayList<Provider>).get(fila!!).address
                objProvider.state=(registroProvider as ArrayList<Provider>).get(fila!!).state
                //asignamos los valores a cada control
                binding.txtBusIdProvider.setText(objProvider.idprovider.toString())
                if(objProvider.state != 0){
                    binding.btnStateProvider.setText("Deshabilitar")
                }else{
                    binding.btnStateProvider.setText("Habilitar")
                }
            }
        )
        binding.btnStateProvider.setOnClickListener {
            if (binding.txtBusIdProvider.text.toString().length>0){
                if (objProvider.state==1){
                    EliminarUsuario(objProvider.idprovider.toLong())
                    DialogoCRUD("Usuario","Se deshabilitó el usuario "+objProvider.name_prov,FragmentBuscarProvider())
                }else{
                    objProvider.state=1
                    ActualizarUsuario(objProvider,objProvider.idprovider.toLong())
                    DialogoCRUD("Usuario","Se habilitó el usuario "+objProvider.name_prov,FragmentBuscarProvider())
                }
            }else{
                Toast.makeText(binding.root.context,"Debe Seleccionar un usuario", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root

    }

    fun mostrarUsers(){
        val call = providerService!!.MostrarProvider()
        call!!.enqueue(object : Callback<List<Provider>> {
            override fun onResponse(call: Call<List<Provider>>, response: Response<List<Provider>>) {
                if(response.isSuccessful){
                    println("Correcto")
                    registroProvider = response.body()
                    binding.lstBusProvider.adapter = AdaptadorBusquedaProvider(binding.root.context,registroProvider)
                }else{
                    println("Error")
                }
            }
            override fun onFailure(call: Call<List<Provider>>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })
    }

    fun ActualizarUsuario(p: Provider, id: Long ){
        val call = providerService!!.ActualizarProvider(id,p)
        call!!.enqueue(object : Callback<List<Provider>?> {
            override fun onResponse(call: Call<List<Provider>?>, response: Response<List<Provider>?>) {
                if(response.isSuccessful){
                    Log.e("Mensaje", "Se actualizo correctamente el Usuario")
                }
            }
            override fun onFailure(call: Call<List<Provider>?>, t: Throwable) {
                Log.e("Error: ",t.message!!)
            }
        })
    }

    fun EliminarUsuario(id: Long){
        val call = providerService!!.EliminarrProvider(id)
        call!!.enqueue(object: Callback<List<Provider>?> {
            override fun onResponse(call: Call<List<Provider>?>, response: Response<List<Provider>?>) {
                if(response.isSuccessful){
                    Log.e("mensaje","Se elimino correctamente")
                }
            }
            override fun onFailure(call: Call<List<Provider>?>, t: Throwable) {
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