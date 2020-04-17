package com.evans.technologies.usuario.fragments.change_password

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.evans.technologies.usuario.R
import com.evans.technologies.usuario.Retrofit.RetrofitClient
import com.evans.technologies.usuario.Utils.setCorreoNavFragment
import com.evans.technologies.usuario.Utils.setIDrecuperar
import com.evans.technologies.usuario.Utils.setNavFragment
import com.evans.technologies.usuario.model.user
import kotlinx.android.synthetic.main.fragment_correo.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class correo : Fragment() {

    lateinit var navFragment: SharedPreferences

    private var sendRecuperarContraseña=false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_correo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navFragment =
            context!!.getSharedPreferences("navFragment", Context.MODE_PRIVATE)
        navFragment.edit().clear().apply()
        val b = arguments
        if (b != null) {
            sendRecuperarContraseña = b.getBoolean("sendRecuperarContraseña")
        }
        fc_btn_correo_ic!!.setOnClickListener {
            val e1 = c_edtxt_email!!.text.toString().trim()
            val e2 = c_edtxt_email_confirm!!.text.toString().trim()
            if (comprobarcampos(e1,e2)) {
                Log.e("accountActivate",sendRecuperarContraseña.toString())
                if(sendRecuperarContraseña){
                    recuperarContraseña(e1)
                }else{
                    accountActivate(e1)

                }
            }
        }
    }
    fun recuperarContraseña(e1: String) {
        progressBar_correo!!.visibility = View.VISIBLE
        val sendCorreo =
            RetrofitClient.getInstance().api.sendCorreo_recuperar(e1!!)
        sendCorreo.enqueue(object : Callback<user?> {
            override fun onResponse(
                call: Call<user?>,
                response: Response<user?>
            ) {
                Log.e("correo_set", response.code().toString() + "")
                if (response.isSuccessful) {
                    progressBar_correo!!.visibility = View.GONE

                        Log.e("correo_set", response.body()!!.user)
                        setIDrecuperar(navFragment, response.body()!!.user)
                        setNavFragment(navFragment, correo().toString())
                        setCorreoNavFragment(navFragment, e1)
                        val bundle= Bundle()
                        bundle.putBoolean( "validar_account",false)
                        bundle.putBoolean("activity" ,false)

                        findNavController().navigate(R.id.action_correo_to_set_codigo,bundle)

                        //    findNavController().navigate(R.id.action_loginFragment_to_correo)

//                                val manager =
//                                    activity!!.supportFragmentManager
//                                manager.beginTransaction().replace(
//                                    R.id.recuperar_frag,
//                                    set_codigo(false, false)
//                                ).commit()

                } else {
                    progressBar_correo!!.visibility = View.GONE
                    activity!!.toast("El correo no existe")
                }
            }

            override fun onFailure(
                call: Call<user?>,
                t: Throwable
            ) {
                progressBar_correo!!.visibility = View.GONE
                activity!!.toast("Error al enviar el codigo.")
            }
        })

    }
    fun accountActivate(e1: String) {
        progressBar_correo!!.visibility = View.VISIBLE
        val sendCorreo =
            RetrofitClient.getInstance().api.sendEmail(e1);
        sendCorreo.enqueue(object: Callback<user>{
            override fun onFailure(call: Call<user>, t: Throwable) {
                progressBar_correo!!.visibility = View.GONE
                Log.e("accountActivate",t.message)
                activity!!.toast("Error con la red.")
            }

            override fun onResponse(call: Call<user>, response: Response<user>) {
                Log.e("accountActivate",response.code().toString())
                when {
                    response.code()==200 -> {
                       // setIDrecuperar(navFragment, response.body()!!.user)
                        setNavFragment(navFragment, correo().toString())
                        setCorreoNavFragment(navFragment, e1)
                        val bundle= Bundle()
                        bundle.putBoolean( "validar_account",true)
                        bundle.putBoolean("activity" ,true)
                        findNavController().navigate(R.id.action_correo_to_set_codigo,bundle)
                    }
                    response.code()==202 -> {
                        progressBar_correo!!.visibility = View.GONE
                        activity!!.toast("Correo ya esta siendo usado")
                    }
                    else -> {
                        progressBar_correo!!.visibility = View.GONE
                        activity!!.toast("Intentelo nuevamente")
                    }
                }
            }

        })

    }
    private fun comprobarcampos(e1: String, e2: String): Boolean {
        if (e1!!.isEmpty() || e2!!.isEmpty()) {
            c_edtxt_email!!.error = "Campos vacios"
            c_edtxt_email_confirm!!.error = "Campos vacios"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(e1).matches()) {
            c_edtxt_email!!.error = "No es un correo valido"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(e2).matches()) {
            c_edtxt_email_confirm!!.error = "No es un correo valido"
            return false
        }
        if (e1 != e2) {
            c_edtxt_email!!.error = "Los correos no coinciden"
            c_edtxt_email_confirm!!.error = "Los correos no coinciden"
            return false
        }
        return true
    }
}