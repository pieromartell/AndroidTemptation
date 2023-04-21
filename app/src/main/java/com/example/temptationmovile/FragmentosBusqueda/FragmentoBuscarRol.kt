package com.example.temptationmovile.FragmentosBusqueda

import android.app.AlertDialog
import android.content.Context
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
import com.example.temptationmovile.adaptadores.AdaptadorFilterRol
import com.example.temptationmovile.clases.Rol
import com.example.temptationmovile.databinding.FragmentoBuscarRolBinding
import com.example.temptationmovile.remoto.ApiUtil
import com.example.temptationmovile.servicios.RolService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentoBuscarRol : Fragment() {
    private var _binding:FragmentoBuscarRolBinding?=null
    private val binding get() = _binding!!
    private var fila:Int?=null
    private var rolService:RolService?=null
    private var registroRol:List<Rol>?=null
    private var objRol=Rol()
    var ft: FragmentTransaction?=null
    private var dialogo: AlertDialog.Builder?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentoBuscarRolBinding.inflate(inflater, container, false)
        registroRol=ArrayList()
        rolService=ApiUtil.rolService
        mostrarRol()
        binding.txtBusRol.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                (binding.lstRolBusqueda.adapter as AdaptadorFilterRol).filter(newText ?: "")
                return true
            }
        })
        
        binding.btnStateRol.setOnClickListener {
            if (binding.txtIdRolBus.text.toString().length>0){
                if (objRol.state==1){
                    EliminarRol(objRol.idrol.toLong())
                    DialogoCRUD("Rol","Se deshabilitó el rol "+objRol.namerol,FragmentoBuscarRol())
                }else{
                    objRol.state=1
                    ActualizarRol(objRol,objRol.idrol.toLong())
                    DialogoCRUD("Rol","Se habilitó el rol "+objRol.namerol,FragmentoBuscarRol())
                }
                }else{
                Toast.makeText(binding.root.context,"Debe Seleccionar un rol", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    fun onClickListener(rol:Rol,pos:Int){
        binding.txtIdRolBus.setText(rol.idrol.toString())
        objRol.idrol=rol.idrol
        objRol.namerol=rol.namerol
        objRol.state=rol.state
        if(objRol.state != 0){
            binding.btnStateRol.setText("Deshabilitar")
        }else{
            binding.btnStateRol.setText("Habilitar")
        }
    }
    fun mostrarRol(){
        val call = rolService!!.MostrarRol()
        call!!.enqueue(object: Callback<List<Rol>?> {
            override fun onResponse(call: Call<List<Rol>?>, response: Response<List<Rol>?>) {
                if (response.isSuccessful){
                    registroRol=response.body()
                    binding.lstRolBusqueda.adapter= AdaptadorFilterRol(binding.root.context,registroRol,{rol,pos->onClickListener(rol,pos)})
                }
            }
            override fun onFailure(call: Call<List<Rol>?>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }
        })
    }
    fun ActualizarRol(b: Rol, id: Long ){
        val call = rolService!!.ActualizarRol(id,b)
        call!!.enqueue(object : Callback<List<Rol>?>{
            override fun onResponse(call: Call<List<Rol>?>, response: Response<List<Rol>?>) {
                if(response.isSuccessful){
                    Log.e("Mensaje", "Se actualizo correctamente")
                }
            }

            override fun onFailure(call: Call<List<Rol>?>, t: Throwable) {
                Log.e("Error: ",t.message!!)
            }

        })
    }
    fun EliminarRol(id: Long){
        val call = rolService!!.EliminarRol(id)
        call!!.enqueue(object: Callback<List<Rol>?>{
            override fun onResponse(call: Call<List<Rol>?>, response: Response<List<Rol>?>) {
                if(response.isSuccessful){
                    Log.e("mensaje","Se elimino correctamente")
                }
            }

            override fun onFailure(call: Call<List<Rol>?>, t: Throwable) {
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