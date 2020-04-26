package com.evans.technologies.usuario.Activities.cupon

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import com.evans.technologies.usuario.R
import com.evans.technologies.usuario.Retrofit.RetrofitClient
import com.evans.technologies.usuario.Utils.getUserId_Prefs
import com.evans.technologies.usuario.Utils.toastLong
import com.evans.technologies.usuario.model.getPrice
import kotlinx.android.synthetic.main.activity_add_code_promocional.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddCodePromocional() : Fragment() {

    var prefs: SharedPreferences? = null
    var dato: String? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            val inputManager = requireActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            val focusedView =  requireActivity().currentFocus
            if (focusedView != null) {
                inputManager.hideSoftInputFromWindow(
                    focusedView.windowToken,
                    InputMethodManager.SHOW_FORCED
                )
            }
        } catch (e: Exception) {
            Log.e("error", e.message)
        }
        prefs =  requireActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE)

        aacp_btn_add!!.setOnClickListener(View.OnClickListener {
            dato = aacp_edtxt_codep!!.text.toString().trim()
            if (ComprobarCode()) {
                funcionAumentarCode()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_add_code_promocional, container, false)
    }
    private fun funcionAumentarCode() {
        progressBar.visibility=View.VISIBLE
        Log.e("Agregarc", getUserId_Prefs((prefs)!!) + dato)
        val code: Call<getPrice> =
            RetrofitClient.getInstance().api.setCuponUser((getUserId_Prefs((prefs)!!))!!, (dato)!!)
        code.enqueue(object : Callback<getPrice> {
            override fun onResponse(
                call: Call<getPrice>?,
                response: Response<getPrice>
            ) {
                Log.e("Agregarc", "" + response.code())
                progressBar.visibility=View.GONE
                if (response.isSuccessful) {
                    activity!!.toastLong("Se agrego el cúpon correctamente")
//                    startActivity(
//                        Intent(
//                            this@AddCodePromocional,
//                            PagosActivity::class.java
//                        ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                    )
                } else {
                    activity!!.toastLong("Hubo problemas al agregar el cúpon")
                }
            }

            override fun onFailure(
                call: Call<getPrice>?,
                t: Throwable
            ) {
                progressBar.visibility=View.GONE
                activity!!.toastLong("Intentalo mas tarde")
            }
        })
    }

    override fun onStart() {
        super.onStart()
    }

    private fun ComprobarCode(): Boolean {
        if (dato!!.trim { it <= ' ' }.isEmpty()) {
            aacp_edtxt_codep!!.error = "El campo no puede estar vacio"
            return false
        }
        if (aacp_edtxt_codep!!.text.toString().trim { it <= ' ' }.length < 6) {
            aacp_edtxt_codep!!.error = "Codigo incorrecto"
            return false
        }
        return true
    }
}