package com.evans.technologies.usuario.fragments

import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.evans.technologies.usuario.R
import com.evans.technologies.usuario.Retrofit.RetrofitClient
import com.evans.technologies.usuario.Utils.getIsReferred
import com.evans.technologies.usuario.Utils.getUserId_Prefs
import com.evans.technologies.usuario.Utils.setReferido
import com.evans.technologies.usuario.Utils.toastLong
import com.evans.technologies.usuario.model.Referido
import com.evans.technologies.usuario.model.infoDriver
import kotlinx.android.synthetic.main.referidos_dialog_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class ReferidosDialogFragment:Fragment() {
    val facebook="com.facebook.katana"
    val twitter="com.twitter.android"
    val instagram="com.instagram.android"
    val whatsapp="com.whatsapp"
    lateinit var referido: Referido

    private lateinit var prefs: SharedPreferences
    //open dialog
    //val dialog= ReferidosDialogFragment()
    //dialog.show(getSupportFragmentManager(),"")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //activity!!.setStyle(DialogFragment.STYLE_NORMAL,R.style.fullScreenDialog)
        prefs = requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        val view= inflater.inflate(R.layout.referidos_dialog_fragment,container,false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(getIsReferred(prefs)!!){
            rdf_cons.visibility=View.GONE
        }

        var getReferido= RetrofitClient.getInstance().api.getCodeReferido(getUserId_Prefs(prefs)!!)
        getReferido.enqueue(object : Callback<infoDriver> {
            override fun onFailure(call: Call<infoDriver>, t: Throwable) {
                Log.e("error", t.message)
                Toast.makeText(context,"No se pudo referir, revise su conexion",Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<infoDriver>, response: Response<infoDriver>) {
                if ( (response.isSuccessful)){
                    if (response.body()!=null){
                        referido=response.body()!!.referido
                        try{
                            txt_codigo.text= referido.key
                        }catch (e:Exception){

                        }
                    }else{
                        Toast.makeText(context,"No se pudo traer correctamente los datos, null",Toast.LENGTH_LONG).show()
                    }
                }else{
                    Toast.makeText(context,"No se pudo referir, revise su conexion ${response.code()}",Toast.LENGTH_LONG).show()
                }

            }

        })

        txt_codigo.setOnLongClickListener {
            val mensaje= txt_codigo.text.toString()
            copyText(mensaje)
        }
        rdf_copiar.setOnClickListener {
            val mensaje= txt_codigo.text.toString()
            copyText(mensaje)
        }
//        rdf_imgbtn_close.setOnClickListener {
//            this.dismiss()
//        }
        rdf_imgbtn_facebook.setOnClickListener {
            sharingtoSocialMedia(facebook)
        }
        rdf_imgbtn_msg.setOnClickListener {
            sharingtoSocialMedia(twitter)
        }
        rdf_imgbtn_wsp.setOnClickListener {
            sharingtoSocialMedia(whatsapp)
        }
        rdf_imgbtn_send_code.setOnClickListener {
            val dato= rdf_edtxt_code.text.toString().trim()
            if (dato.isNotEmpty()){
                registrarCode(dato)
            }else{
                rdf_edtxt_code.error="El codigo esta vacio"
            }


        }
        rdf_info.setOnClickListener {
            findNavController().navigate(R.id.action_nav_trips_free_to_referidosInfo)
        }
    }
    private fun registrarCode(dato: String) {
        val id= getUserId_Prefs(prefs)!!
        val statusTrip= RetrofitClient.getInstance().api.putReferido(id,dato)
        statusTrip.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if ( response.isSuccessful){
                    setReferido(prefs,true)
                    rdf_cons.visibility=View.GONE
                    Toast.makeText(context,"Refirio correctamente..",Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(context,"No se pudo referir, revise su conexion ${response.code()}",Toast.LENGTH_LONG).show()
                }

            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("register", t.message +"  "+id+"  "+dato)
                Toast.makeText(context,"No se pudo referir, revise su conexion",Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun copyText(mensaje: String): Boolean {
        val dato:ClipboardManager= requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("text",mensaje)
        dato.setPrimaryClip(clip)
        requireActivity().toastLong("Texto Copiado Correctamente")
        return false
    }

    fun sharingtoSocialMedia(paquete:String){
        val intent= Intent()
        intent.action = Intent.ACTION_SEND
        intent.type= "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Evans disfruta benefios en tus viajes registrate con este codigo ${referido.key}y gana un cupon de 1 PEN para tu siguiente viaje. http://www.proevans.com")
        val installed= checkAppInstalled(paquete)
        if (installed){
            intent.setPackage(paquete)
            startActivity(intent)
        }else{
            Toast.makeText(context,"La aplicacion no esta instalada.",Toast.LENGTH_LONG).show()
        }
    }

    private fun checkAppInstalled(paquete: String): Boolean {
        val pm= requireContext().packageManager
        return try{
            pm.getPackageInfo(paquete,PackageManager.GET_ACTIVITIES)
            true
        }catch (e:Exception){
            false
        }
    }
}