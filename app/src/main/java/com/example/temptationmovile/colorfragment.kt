package com.example.temptationmovile

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.temptationmovile.databinding.ColorfragmentBinding
import androidx.fragment.app.FragmentTransaction
import com.example.temptationmovile.adaptadores.AdaptadorColor
import com.example.temptationmovile.clases.Brand
import com.example.temptationmovile.clases.Color
import com.example.temptationmovile.remoto.ApiUtil
import com.example.temptationmovile.servicios.ColorService
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
 * Use the [colorfragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class colorfragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var txtcolor: EditText
    private lateinit var chbestado: CheckBox
    private lateinit var lblidcolor: TextView
    private lateinit var btnregistrar_color: Button
    private lateinit var btnactualizar_color: Button
    private lateinit var btneliminar_color: Button
    private lateinit var btnsalir_color: Button
    private lateinit var lstcolor: ListView

    //creamos un objeto de la clase categoria
    val objcolor = Color()
    //declaramos variables
    private var cod = 0
    private var nom = ""
    private var est = 1
    private var fila = -1

    //declaramos el servicio
    private var colorservice :ColorService?=null

    //creamos arraylist de categoria
    private var registrocolor:List<Color>?=null

    //creamos un objeto de la clase utilidad
    var objutilidad = Util()

    //creamos transicion para fragmento
    var ft: FragmentTransaction?= null

    private var _binding: ColorfragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val raiz = inflater.inflate(R.layout.colorfragment, container, false)
        //
        txtcolor = raiz.findViewById(R.id.txtcolor)
        chbestado = raiz.findViewById(R.id.chbestado)
        lblidcolor = raiz.findViewById(R.id.lblidcolor)
        btnregistrar_color = raiz.findViewById(R.id.btnregistrar_color)
        btnactualizar_color = raiz.findViewById(R.id.btnactualizar_color)
        btneliminar_color = raiz.findViewById(R.id.btneliminar_color)
        btnsalir_color = raiz.findViewById(R.id.btnsalir_color)
        lstcolor = raiz.findViewById(R.id.lstcolor)

        //
        registrocolor = ArrayList()

        //
        colorservice =ApiUtil.colorservice

        //
        mostrarColor(raiz.context)

        //
        btnregistrar_color.setOnClickListener {
            if(txtcolor.getText().toString()==""){
                objutilidad.MensajeToast(raiz.context,"Ingrese el color")
                txtcolor.requestFocus()
            }else{
                //capturando valores
                nom = txtcolor.getText().toString()
                est = if(chbestado.isChecked){
                    1
                }else{
                    0
                }
                //enviamos los valores a la clase
                objcolor.name_col = nom
                objcolor.state = est
                registrarColor(raiz.context,objcolor)
                //actualizamos fragmento
                val fcolor = colorfragment()
                ft = fragmentManager?.beginTransaction()
                ft?.replace(R.id.contenedor,fcolor,null)
                ft?.addToBackStack(null)
                ft?.commit()
            }
        }

        lstcolor.setOnItemClickListener(
            { adapterView, view,i, id ->
                fila = i
                //asignamos los valores a cada control
                lblidcolor.setText(""+(registrocolor as ArrayList<Color>).get(fila).idcolor)
                txtcolor.setText(""+(registrocolor as ArrayList<Color>).get(fila).name_col)
                if((registrocolor as ArrayList<Color>).get(fila).state != 0){
                    chbestado.setChecked(true)
                }else{
                    chbestado.setChecked(false)
                }

            }
        )

        return raiz
    }


    fun mostrarColor(contex:Context){
        val call = colorservice!!.MostrarColor()
        call!!.enqueue(object :Callback<List<Color>?>{
            override fun onResponse(
                call: Call<List<Color>?>,
                response: Response<List<Color>?>)
            {
                if(response.isSuccessful){
                    registrocolor = response.body()
                    lstcolor.adapter = AdaptadorColor(contex,registrocolor)
                }
            }

            override fun onFailure(call: Call<List<Color>?>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })

    }

    fun registrarColor(context:Context,c: Color){
        val call = colorservice!!.RegistrarColor(c)
        call!!.enqueue(object:Callback<Color?>{
            override fun onResponse(call: Call<Color?>, response: Response<Color?>) {
                if(response.isSuccessful){
                    objutilidad.MensajeToast(context,"Se registro el Color")
                }
            }

            override fun onFailure(call: Call<Color?>, t: Throwable) {
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