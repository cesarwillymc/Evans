package com.evans.technologies.usuario.fragments.change_password

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController

import com.evans.technologies.usuario.R
import com.evans.technologies.usuario.Retrofit.RetrofitClient
import com.evans.technologies.usuario.Utils.getToken
import com.evans.technologies.usuario.Utils.getUserId_Prefs
import com.evans.technologies.usuario.model.modelTrip.trip
import kotlinx.android.synthetic.main.fragment_pass_actual.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class passActual : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pass_actual, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs = requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        next_paso_pass.setOnClickListener {
            val pass=pass_Actual.text.toString().trim()
            verificarcontraseña(getUserId_Prefs(prefs)!!,pass)
        }
    }

    private fun verificarcontraseña(token:String,pass: String) {
        progressBar3.visibility=View.VISIBLE
        if(pass.isEmpty()){
            pass_Actual.error="Esta vacio"
            return
        }
        RetrofitClient.getInstance().api.equalsPassword(pass,token).enqueue(object: Callback<trip> {
            override fun onFailure(call: Call<trip>, t: Throwable) {
                progressBar3.visibility=View.GONE
                Log.e("passChange",t.message)
                Toast.makeText(requireContext(),"Error de red",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<trip>, response: Response<trip>) {
                Log.e("passChange",response.code().toString())
               if (response.isSuccessful){

                   progressBar3.visibility=View.GONE
                   val dato=passActualDirections.actionPassActualToChangepassword2(token,false)
                    findNavController().navigate(dato)
               }else{
                   progressBar3.visibility=View.GONE
                   Toast.makeText(requireContext(),"Contraseña incorrecta",Toast.LENGTH_LONG).show()
               }
            }

        })
    }

}
