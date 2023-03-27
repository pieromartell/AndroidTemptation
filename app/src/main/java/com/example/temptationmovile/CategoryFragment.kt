package com.example.temptationmovile

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentTransaction
import com.example.temptationmovile.adaptadores.AdaptadorCategory
import com.example.temptationmovile.clases.Category
import com.example.temptationmovile.databinding.CategoryFragmentBinding
import com.example.temptationmovile.remoto.ApiUtil
import com.example.temptationmovile.servicios.CategoryService
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
 * Use the [CategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CategoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var txtcat: EditText
    private lateinit var chbestado: CheckBox
    private lateinit var lblidcat: TextView
    private lateinit var btnregistrar_cat: Button
    private lateinit var btnactualizar_cat: Button
    private lateinit var btneliminar_cat: Button
    private lateinit var lstcat: ListView

    val objcategory = Category()

    //declaramos variables
    private var cod = 0
    private var nom = ""
    private var est = 1
    private var fila = -1

    //
    private var categoryService: CategoryService?=null
    //
    private var registroCategory:List<Category>?=null

    var objutilidad = Util()

    //creamos transicion para fragmento
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
        val raiz = inflater.inflate(R.layout.category_fragment, container, false)
        //
        txtcat = raiz.findViewById(R.id.txtcat)
        chbestado = raiz.findViewById(R.id.chkStateCategory)
        lblidcat = raiz.findViewById(R.id.lblidcat)
        btnregistrar_cat = raiz.findViewById(R.id.btnregistrar_cat)
        btnactualizar_cat = raiz.findViewById(R.id.btnactualizar_cat)
        btneliminar_cat = raiz.findViewById(R.id.btneliminar_cat)
        lstcat = raiz.findViewById(R.id.lstcat)

        registroCategory = ArrayList()

        categoryService = ApiUtil.categoryService

        mostrarCategory(raiz.context)

        btnregistrar_cat.setOnClickListener {
            if(txtcat.getText().toString()==""){
                objutilidad.MensajeToast(raiz.context,"Ingrese el color")
                txtcat.requestFocus()
            }else{
                nom = txtcat.getText().toString()
                est = if(chbestado.isChecked){
                    1
                }else{
                    0
                }
                objcategory.name_cat = nom
                objcategory.state = est
                registrarCategory(raiz.context,objcategory)
                //
                val fcategory = CategoryFragment()
                ft = fragmentManager?.beginTransaction()
                ft?.replace(R.id.contenedor,fcategory,null)
                ft?.addToBackStack(null)
                ft?.commit()
            }
        }

        lstcat.setOnItemClickListener(
            { adapterView, view,i, id ->
                fila = i
                //asignamos los valores a cada control
                lblidcat.setText(""+(registroCategory as ArrayList<Category>).get(fila).idcat)
                txtcat.setText(""+(registroCategory as ArrayList<Category>).get(fila).name_cat)
                if((registroCategory as ArrayList<Category>).get(fila).state != 0){
                    chbestado.setChecked(true)
                }else{
                    chbestado.setChecked(false)
                }

            }
        )

        return raiz
    }


    fun mostrarCategory(contex:Context){
        val call = categoryService!!.MostrarCategory()
        call!!.enqueue(object : Callback<List<Category>?>{
            override fun onResponse(
                call: Call<List<Category>?>,
                response: Response<List<Category>?>
            ) {
                if(response.isSuccessful){
                    registroCategory = response.body()
                    lstcat.adapter = AdaptadorCategory(contex,registroCategory)
                }
            }

            override fun onFailure(call: Call<List<Category>?>, t: Throwable) {
                Log.e("Error: ", t.message.toString())
            }

        })

    }

    fun registrarCategory(context: Context,ca:Category){
        val call = categoryService!!.RegistrarCategory(ca)
        call!!.enqueue(object :Callback<Category?>{
            override fun onResponse(call: Call<Category?>, response: Response<Category?>) {
                if(response.isSuccessful){
                    objutilidad.MensajeToast(context,"Se registro la categoria")
                }
            }

            override fun onFailure(call: Call<Category?>, t: Throwable) {
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