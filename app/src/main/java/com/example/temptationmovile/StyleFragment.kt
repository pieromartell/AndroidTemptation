package com.example.temptationmovile

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentTransaction
import com.example.temptationmovile.adaptadores.AdaptadorStyle
import com.example.temptationmovile.clases.Style
import com.example.temptationmovile.databinding.CategoryFragmentBinding
import com.example.temptationmovile.remoto.ApiUtil
import com.example.temptationmovile.servicios.StyleService
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
 * Use the [StyleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StyleFragment : Fragment() {

    private lateinit var txtstyle: EditText
    private lateinit var chbestado: CheckBox
    private lateinit var lblidstyle: TextView
    private lateinit var btnregistrar_style: Button
    private lateinit var btnactualizar_style: Button
    private lateinit var btneliminar_style: Button
    private lateinit var lststyle: ListView

    val objstyle = Style()

    //declaramos variables
    private var cod = 0
    private var nom = ""
    private var est = 1
    private var fila = -1

    //
    private var styleService: StyleService?=null

    //
    private var registroStyle:List<Style>?=null

    var objutilidad = Util()

    private var dialogo: AlertDialog.Builder? = null
    //
    var ft: FragmentTransaction?= null

    private var _binding: CategoryFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val raiz =  inflater.inflate(R.layout.style_fragment, container, false)

        txtstyle = raiz.findViewById(R.id.txtstyle)
        chbestado = raiz.findViewById(R.id.chkStateStyle)
        lblidstyle = raiz.findViewById(R.id.lblidstyle)
        btnregistrar_style = raiz.findViewById(R.id.btnregistrar_style)
        btnactualizar_style = raiz.findViewById(R.id.btnactualizar_style)
        btneliminar_style = raiz.findViewById(R.id.btneliminar_style)
        lststyle = raiz.findViewById(R.id.lststyle)

        registroStyle =ArrayList()

        styleService = ApiUtil.styleService

        //
        mostrarStyle(raiz.context)

        btnregistrar_style.setOnClickListener {
            if(txtstyle.getText().toString()==""){
                objutilidad.MensajeToast(raiz.context,"Ingrese el Estilo")
                txtstyle.requestFocus()
            }else{
                nom = txtstyle.getText().toString()
                est = if(chbestado.isChecked){
                    1
                }else{
                    0
                }
                objstyle.name_sty = nom
                objstyle.state = est
                registrarStyle(raiz.context,objstyle)
                //
                DialogoCRUD("Registro de Estilos","Se registró el nuevo Estilo correctamente",
                    StyleFragment())
            }
        }

        btnactualizar_style.setOnClickListener {
            if (fila >= 0) {
                cod = lblidstyle.getText().toString().toInt()
                nom = txtstyle.getText().toString()
                est = if (chbestado.isChecked) {
                    1
                } else {
                    0
                }
                objstyle.idstyles = cod
                objstyle.name_sty = nom
                objstyle.state = est
                actualizarStyle(raiz.context, objstyle, cod.toLong())
                //objutilidad.limpiar(raiz.findViewById<View>(R.id.frmCategoria) as ViewGroup)
                val fstyle = StyleFragment()
                DialogoCRUD("Actualizacion de Estilos", "Se actualizo el estilo", fstyle)
            } else {
                objutilidad.MensajeToast(raiz.context, "Seleccione un elemento de la lista")
                lststyle.requestFocus()
            }
        }

        btneliminar_style.setOnClickListener {
            if (fila >= 0) {
                cod = lblidstyle.getText().toString().toInt()
                objstyle.idstyles = cod
                eliminarStyle(raiz.context, cod.toLong())
                //objutilidad.limpiar(raiz.findViewById<View>(R.id.frmCategoria) as ViewGroup)
                val fstyle = StyleFragment()
                DialogoCRUD("Eliminacion de Estilos", "Se elimino el Estilo", fstyle)
            } else {
                objutilidad.MensajeToast(raiz.context, "Seleccione un elemento de la lista")
                lststyle.requestFocus()
            }
        }

        lststyle.setOnItemClickListener(
            { adapterView, view,i, id ->
                fila = i
                //asignamos los valores a cada control
                lblidstyle.setText(""+(registroStyle as ArrayList<Style>).get(fila).idstyles)
                txtstyle.setText(""+(registroStyle as ArrayList<Style>).get(fila).name_sty)
                if((registroStyle as ArrayList<Style>).get(fila).state != 0){
                    chbestado.setChecked(true)
                }else{
                    chbestado.setChecked(false)
                }

            }
        )

        return raiz
    }
    fun mostrarStyle(context:Context){
        val call = styleService!!.MostrarEstilo()
        call!!.enqueue(object : Callback<List<Style>?>{
            override fun onResponse(
                call: Call<List<Style>?>,
                response: Response<List<Style>?>
            ) {
                if(response.isSuccessful){
                    registroStyle = response.body()
                    lststyle.adapter = AdaptadorStyle(context,registroStyle)
                }
            }
            override fun onFailure(call: Call<List<Style>?>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })
    }

    fun registrarStyle(context:Context,st:Style){
        val call = styleService!!.RegistrarEstilo(st)
        call!!.enqueue(object :Callback<Style?>{
            override fun onResponse(call: Call<Style?>, response: Response<Style?>) {
                if(response.isSuccessful){
                    objutilidad.MensajeToast(context,"Se registro el Estilo")
                }
            }

            override fun onFailure(call: Call<Style?>, t: Throwable) {
                Log.e("Error: ", t.message!!)
            }

        })
    }
    fun actualizarStyle(context:Context,st:Style,id:Long){
        val call = styleService!!.ActualizarEstilo(id,st)
        call!!.enqueue(object :Callback<List<Style>?>{
            override fun onResponse(call: Call<List<Style>?>, response: Response<List<Style>?>) {
                if(response.isSuccessful){
                    Log.e("mensaje", "Se actualizo correctamente")
                }
            }

            override fun onFailure(call: Call<List<Style>?>, t: Throwable) {
                Log.e("Error: ", t.message!!)
            }

        })
    }

    fun eliminarStyle(context:Context,id:Long){
        val call = styleService!!.EliminarEstilo(id)
        call!!.enqueue(object :Callback<List<Style>?>{
            override fun onResponse(call: Call<List<Style>?>, response: Response<List<Style>?>) {
                if(response.isSuccessful){
                    Log.e("mensaje","Se elimino correctamente")
                }
            }

            override fun onFailure(call: Call<List<Style>?>, t: Throwable) {
                Log.e("Error: ", t.message!!)
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}