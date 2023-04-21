package com.example.temptationmovile.FragmentosBusqueda

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.SearchView
import androidx.fragment.app.FragmentTransaction
import com.example.temptationmovile.R
import com.example.temptationmovile.adaptadores.AdaptadorPerson
import com.example.temptationmovile.clases.Person
import com.example.temptationmovile.databinding.FragmentoBuscarPersonBinding
import com.example.temptationmovile.remoto.ApiUtil
import com.example.temptationmovile.servicios.PersonService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentoBuscarPerson : Fragment() {

    private var _binding:FragmentoBuscarPersonBinding?=null
    private val binding get() = _binding!!
    private var fila:Int?=null
    private var personService:PersonService?=null
    private var registroPerson:List<Person>?=null
    private var objPerson=Person()
    var ft: FragmentTransaction?=null
    private var dialogo: AlertDialog.Builder?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentoBuscarPersonBinding.inflate(inflater, container, false)
        registroPerson=ArrayList()
        personService=ApiUtil.personService
        mostrarUsers()
        binding.txtBusPerson.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                (binding.lstPersonBusqueda.adapter as AdaptadorPerson).filter(newText ?: "")
                return true
            }
        })

        binding.lstPersonBusqueda.setOnItemClickListener(
            { adapterView, view,i, id ->
                fila = i
                objPerson.idperson=(registroPerson as ArrayList<Person>).get(fila!!).idperson
                objPerson.name=(registroPerson as ArrayList<Person>).get(fila!!).name
                objPerson.dni=(registroPerson as ArrayList<Person>).get(fila!!).dni
                objPerson.address=(registroPerson as ArrayList<Person>).get(fila!!).address
                objPerson.dni=(registroPerson as ArrayList<Person>).get(fila!!).gender
                objPerson.date_b=(registroPerson as ArrayList<Person>).get(fila!!).date_b
                objPerson.username=(registroPerson as ArrayList<Person>).get(fila!!).username
                objPerson.lastname=(registroPerson as ArrayList<Person>).get(fila!!).lastname
                objPerson.password=(registroPerson as ArrayList<Person>).get(fila!!).username
                objPerson.key=(registroPerson as ArrayList<Person>).get(fila!!).key
                objPerson.state=(registroPerson as ArrayList<Person>).get(fila!!).state
                //asignamos los valores a cada control
                binding.txtIdPersonBus.setText(objPerson.idperson.toString())
                if(objPerson.state != 0){
                    binding.btnStatePerson.setText("Deshabilitar")
                }else{
                    binding.btnStatePerson.setText("Habilitar")
                }
            }
        )
        binding.btnStatePerson.setOnClickListener {
            if (binding.txtIdPersonBus.text.toString().length>0){
                if (objPerson.state==1){
                    EliminarUsuario(objPerson.idperson.toLong())
                    DialogoCRUD("Usuario","Se deshabilitó el usuario "+objPerson.username,FragmentoBuscarPerson())
                }else{
                    objPerson.state=1
                    ActualizarUsuario(objPerson,objPerson.idperson.toLong())
                    DialogoCRUD("Usuario","Se habilitó el usuario "+objPerson.username,FragmentoBuscarPerson())
                }
            }else{
                Toast.makeText(binding.root.context,"Debe Seleccionar un usuario",Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    fun mostrarUsers(){
        val call = personService!!.MostrarUsuarios()
        call!!.enqueue(object : Callback<List<Person>> {
            override fun onResponse(call: Call<List<Person>>, response: Response<List<Person>>) {
                if(response.isSuccessful){
                    println("Correcto")
                    registroPerson = response.body()
                    binding.lstPersonBusqueda.adapter = AdaptadorPerson(binding.root.context,registroPerson)
                }else{
                    println("Error")
                }
            }
            override fun onFailure(call: Call<List<Person>>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })
    }

    fun ActualizarUsuario(p: Person, id: Long ){
        val call = personService!!.ActualizarUsuario(id,p)
        call!!.enqueue(object : Callback<List<Person>?> {
            override fun onResponse(call: Call<List<Person>?>, response: Response<List<Person>?>) {
                if(response.isSuccessful){
                    Log.e("Mensaje", "Se actualizo correctamente el Usuario")
                }
            }
            override fun onFailure(call: Call<List<Person>?>, t: Throwable) {
                Log.e("Error: ",t.message!!)
            }
        })
    }

    fun EliminarUsuario(id: Long){
        val call = personService!!.EliminarUsuario(id)
        call!!.enqueue(object: Callback<List<Person>?> {
            override fun onResponse(call: Call<List<Person>?>, response: Response<List<Person>?>) {
                if(response.isSuccessful){
                    Log.e("mensaje","Se elimino correctamente")
                }
            }
            override fun onFailure(call: Call<List<Person>?>, t: Throwable) {
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