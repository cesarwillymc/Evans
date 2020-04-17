package com.evans.technologies.usuario.fragments.change_password

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.evans.technologies.usuario.R
import com.evans.technologies.usuario.Retrofit.RetrofitClient
import com.evans.technologies.usuario.Utils.*
import com.evans.technologies.usuario.fragments.Fragment_perfil_user
import com.evans.technologies.usuario.model.user
import kotlinx.android.synthetic.main.fragment_set_codigo.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class set_codigo(var validar: Boolean=false,var actividad: Boolean=false) :
    Fragment() {
//    public fun set_codigo( validar: Boolean,  actividad: Boolean){
//        this.validar=validar
//        this.actividad=actividad
//    }
    lateinit var navFragment: SharedPreferences
    lateinit var prefs: SharedPreferences
    lateinit var code:String
    lateinit var id:String
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navFragment =
            context!!.getSharedPreferences("navFragment", Context.MODE_PRIVATE)
        prefs =
            context!!.getSharedPreferences("Preferences", Context.MODE_PRIVATE)

        val b = arguments
        if (b != null) {
            validar = b.getBoolean("validar_account")
            actividad = b.getBoolean("activity")
            Log.e(
                "codigo_setva", "$validar actividad $actividad"
            )
        }
        if (validar!! && actividad!!) {
            fsc_validar_tarde!!.visibility = View.VISIBLE
            id = getIDrecuperar(navFragment)!!
        } else {
            id = getUserId_Prefs(prefs)!!
        }
        fsc_validar_tarde!!.setOnClickListener(View.OnClickListener {
            if (actividad!!) {
                activity!!.finish()
            }
        })
        fsc_btn_next!!.setOnClickListener{
                code = fsc_edtxt_codigo!!.text.toString().trim()
                if (comprobarcampos()) {
                    progressBar_codigo!!.visibility = View.VISIBLE
                    if (validar) {
                        validarAccount()
                    } else {
                        cambiarContraseña()
                    }
                }

        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_set_codigo, container, false)
    }

    private fun validarAccount() {
        Log.e(
            "codigo_setva", ("\n" +
                    id + "\n" +
                    code + "\n"+
                    getCorreoNavFragment(navFragment)!!.toString())
        )
        val sendAccount = RetrofitClient.getInstance().api
            .validateEmail(getCorreoNavFragment(navFragment)!!.toString(), code!!)
        sendAccount.enqueue(object : Callback<user?> {
            override fun onResponse(
                call: Call<user?>,
                response: Response<user?>
            ) {
                if (response.body() != null) {
                    Log.e("codigo_set", "tiene cuerpo")
                }
                try {
                    Log.e(
                        "codigo_set", (response.code().toString() + "\n" +
                                id + "\n" +
                                code + "\n")
                    )
                } catch (e: Exception) {
                }
                if (response.isSuccessful) {
                    progressBar_codigo!!.visibility = View.GONE
                    activity!!.toastLong("Su cuenta se valido correctamente")
                    if (actividad!!) {
                        findNavController().navigate(R.id.action_set_codigo_to_registerFragment)
                    } else {
                        setAccountActivate((prefs)!!, true)
                        val manager =
                            activity!!.supportFragmentManager
                        manager.beginTransaction().replace(
                            R.id.main_layout_change_fragment,
                            Fragment_perfil_user()
                        ).commit()
                    }
                } else {
                    progressBar_codigo!!.visibility = View.GONE
                    activity!!.toast("El codigo no es valido")
                }
            }

            override fun onFailure(
                call: Call<user?>,
                t: Throwable
            ) {
                progressBar_codigo!!.visibility = View.GONE
                Log.e(
                    "codigo_setva", "\n" +
                            t.message
                )
            }
        })
    }

    private fun cambiarContraseña() {
        Log.e(
            "codigo_setchange", ("\n" +
                    id + "\n" +
                    code + "\n"+
                    getCorreoNavFragment(navFragment)!!.toString())
        )
        val sendCorreo = RetrofitClient.getInstance().api
            .sendCodigo_recuperar((getIDrecuperar((navFragment)!!))!!, (code)!!)
        sendCorreo.enqueue(object : Callback<user?> {
            override fun onResponse(
                call: Call<user?>,
                response: Response<user?>
            ) {
                if (response.body() != null) {
                    Log.e("codigo_set", "tiene cuerpo")
                }
                try {
                    Log.e(
                        "codigo_set", (response.code().toString() + "\n" +
                                getIDrecuperar((navFragment)!!) + "\n" +
                                code + "\n")
                    )
                } catch (e: Exception) {
                }
                if (response.isSuccessful) {
                    progressBar_codigo!!.visibility = View.GONE
                    setNavFragment((navFragment)!!, set_codigo(true, false).toString())
                    settokenrecuperar((navFragment)!!, (code)!!)
                    findNavController().navigate(R.id.action_set_codigo_to_changepassword)
                } else {
                    progressBar_codigo!!.visibility = View.GONE
                    activity!!.toast("El codigo no es valido")
                }
            }

            override fun onFailure(
                call: Call<user?>,
                t: Throwable
            ) {
                progressBar_codigo!!.visibility = View.GONE
            }
        })
    }

    private fun comprobarcampos(): Boolean {
        if (code!!.isEmpty()) {
            fsc_edtxt_codigo!!.error = "El campo esta vacio"
            return false
        }
        if (code!!.length != 5) {
            fsc_edtxt_codigo!!.error = "Codigo invalido el codigo es mayor de 5 digitos"
            return false
        }
        return true
    }

}