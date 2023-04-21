package com.example.temptationmovile.FragmentosBusqueda

import android.app.AlertDialog
import android.content.Context
import com.example.temptationmovile.clases.Output
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
import com.example.temptationmovile.adaptadores.AdaptadorFilterOutput
import com.example.temptationmovile.adaptadores.AdaptadorFilterRol
import com.example.temptationmovile.adaptadores.AdaptadorOutput
import com.example.temptationmovile.clases.Rol
import com.example.temptationmovile.databinding.FragmentoBuscarOutputBinding
import com.example.temptationmovile.remoto.ApiUtil
import com.example.temptationmovile.servicios.OutputService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentoBuscarOutput : Fragment() {

    private var _binding:FragmentoBuscarOutputBinding?=null
    private val binding get() = _binding!!
    private var fila:Int?=null
    private var outService:OutputService?=null
    private var registroOutput:List<Output>?=null
    private var objUt=Output()
    var ft: FragmentTransaction?=null
    private var dialogo: AlertDialog.Builder?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentoBuscarOutputBinding.inflate(inflater, container, false)
        registroOutput=ArrayList()
        outService=ApiUtil.outputService
        mostrarOutput()
        binding.txtBusOutput.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                (binding.lstOutputBusqueda.adapter as AdaptadorFilterOutput).filter(newText ?: "")
                return true
            }
        })

        binding.lstOutputBusqueda.setOnItemClickListener(
            { adapterView, view,i, id ->
                fila = i
                objUt.idout=(registroOutput as ArrayList<Output>).get(fila!!).idout
                objUt.idproduc=(registroOutput as ArrayList<Output>).get(fila!!).idproduc
                objUt.state=(registroOutput as ArrayList<Output>).get(fila!!).state
                objUt.destino=(registroOutput as ArrayList<Output>).get(fila!!).destino
                objUt.dateout=(registroOutput as ArrayList<Output>).get(fila!!).dateout
                objUt.quantity=(registroOutput as ArrayList<Output>).get(fila!!).quantity

                //asignamos los valores a cada control
                binding.txtIdOutputBus.setText(objUt.idout.toString())
                if(objUt.state != 0){
                    binding.btnStateOutput.setText("Deshabilitar")
                }else{
                    binding.btnStateOutput.setText("Habilitar")
                }
            }
        )
        binding.btnStateOutput.setOnClickListener {
            if (binding.txtIdOutputBus.text.toString().length>0){
                if (objUt.state==1){
                    eliminarOutput(objUt.idout.toLong())
                    DialogoCRUD("Salida","Se deshabilitó la salida "+objUt.idout,FragmentoBuscarOutput())
                }else{
                    objUt.state=1
                    actualizarOutput(objUt,objUt.idout.toLong())
                    DialogoCRUD("Salida","Se habilitó la salida "+objUt.idout,FragmentoBuscarOutput())
                }
            }else{
                Toast.makeText(binding.root.context,"Debe Seleccionar", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    fun mostrarOutput(){
        val call = outService!!.MostrarOutputs()
        call!!.enqueue(object: Callback<List<Output>> {
            override fun onResponse(call: Call<List<Output>>, response: Response<List<Output>>) {
                if(response.isSuccessful){
                    println("Correcto")
                    registroOutput = response.body()
                    binding.lstOutputBusqueda.adapter = AdaptadorFilterOutput(context,registroOutput)
                }
            }

            override fun onFailure(call: Call<List<Output>>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })
    }
    fun actualizarOutput(o:Output,id:Long){
        val call = outService!!.ActualizarProduct(id,o)
        call!!.enqueue(object :Callback<List<Output>?>{
            override fun onResponse(call: Call<List<Output>?>, response: Response<List<Output>?>) {
                if(response.isSuccessful){
                    Log.e("Mensaje", "Se actualizo correctamente la Salida")
                }
            }

            override fun onFailure(call: Call<List<Output>?>, t: Throwable) {
                Log.e("Error: ",t.message!!)
            }

        })
    }
    fun eliminarOutput(id:Long){
        val call=outService!!.EliminarOutput(id)
        call!!.enqueue(object :Callback<Output?>{
            override fun onResponse(call: Call<Output?>, response: Response<Output?>) {
                if (response.isSuccessful){
                    Log.i("mensaje","se elimino")
                }
            }

            override fun onFailure(call: Call<Output?>, t: Throwable) {
                Log.i("Error",t.message!!.toString())
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