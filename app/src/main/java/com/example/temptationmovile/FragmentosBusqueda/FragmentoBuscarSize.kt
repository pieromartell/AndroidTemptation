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
import com.example.temptationmovile.adaptadores.AdaptadorRol
import com.example.temptationmovile.adaptadores.AdaptadorSize
import com.example.temptationmovile.clases.Rol
import com.example.temptationmovile.clases.Size
import com.example.temptationmovile.databinding.FragmentoBuscarSizeBinding
import com.example.temptationmovile.remoto.ApiUtil
import com.example.temptationmovile.servicios.SizeService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentoBuscarSize : Fragment() {

    private var _binding:FragmentoBuscarSizeBinding?=null
    private val binding get() = _binding!!
    private var fila:Int?=null
    private var sizeService:SizeService?=null
    private var registroSize:List<Size>?=null
    private var objSize=Size()
    var ft:FragmentTransaction?=null
    private var dialogo:AlertDialog.Builder?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentoBuscarSizeBinding.inflate(inflater, container, false)
        registroSize=ArrayList()
        sizeService=ApiUtil.sizeService
        mostrarSize()
        binding.txtBusSize.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                (binding.lstSizeBusqueda.adapter as AdaptadorSize).filter(newText ?: "")
                return true
            }
        })
        binding.lstSizeBusqueda.setOnItemClickListener(
            { adapterView, view,i, id ->
                fila = i
                objSize.idsize=(registroSize as ArrayList<Size>).get(fila!!).idsize
                objSize.name_size=(registroSize as ArrayList<Size>).get(fila!!).name_size
                objSize.state=(registroSize as ArrayList<Size>).get(fila!!).state
                //asignamos los valores a cada control
                binding.txtIdSizeBus.setText(objSize.idsize.toString())
                if(objSize.state != 0){
                    binding.btnStateSize.setText("Deshabilitar")
                }else{
                    binding.btnStateSize.setText("Habilitar")
                }
            }
        )
        binding.btnStateSize.setOnClickListener {
            if (binding.txtIdSizeBus.text.toString().length>0){
                if (objSize.state==1){
                    EliminarSize(objSize.idsize.toLong())
                    DialogoCRUD("Talla","Se deshabilitó la talla "+objSize.name_size,FragmentoBuscarSize())
                }else{
                    objSize.state=1
                    ActualizarSize(objSize,objSize.idsize.toLong())
                    DialogoCRUD("Talla","Se habilitó la talla "+objSize.name_size,FragmentoBuscarSize())
                }
             }else{
                Toast.makeText(binding.root.context,"Debe Seleccionar un usuario",Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    fun mostrarSize(){
        val call = sizeService!!.Mostrarsizes()
        call.enqueue(object: Callback<List<Size>?> {
            override fun onResponse(call: Call<List<Size>?>, response: Response<List<Size>?>) {
                if (response.isSuccessful){
                    registroSize=response.body()
                    binding.lstSizeBusqueda.adapter= AdaptadorSize(binding.root.context,registroSize)
                }
            }
            override fun onFailure(call: Call<List<Size>?>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })
    }
    fun ActualizarSize(b: Size, id: Long ){
        val call = sizeService!!.Actualizarsize(id,b)
        call!!.enqueue(object : Callback<List<Size>?>{
            override fun onResponse(call: Call<List<Size>?>, response: Response<List<Size>?>) {
                if(response.isSuccessful){
                    Log.e("Mensaje", "Se actualizo correctamente")
                }
            }
            override fun onFailure(call: Call<List<Size>?>, t: Throwable) {
                Log.e("Error: ",t.message!!)
            }

        })
    }
    fun EliminarSize(id: Long){
        val call = sizeService!!.Eliminarrsize(id)
        call!!.enqueue(object: Callback<List<Size>?>{
            override fun onResponse(call: Call<List<Size>?>, response: Response<List<Size>?>) {
                if(response.isSuccessful){
                    Log.e("mensaje","Se elimino correctamente")
                }
            }

            override fun onFailure(call: Call<List<Size>?>, t: Throwable) {
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