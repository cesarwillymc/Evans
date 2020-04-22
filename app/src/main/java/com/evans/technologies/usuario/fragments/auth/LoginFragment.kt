package com.evans.technologies.usuario.fragments.auth

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.evans.technologies.usuario.Activities.MainActivity
import com.evans.technologies.usuario.Activities.recuperar_contra
import com.evans.technologies.usuario.R
import com.evans.technologies.usuario.Retrofit.RetrofitClient
import com.evans.technologies.usuario.Utils.*
import com.evans.technologies.usuario.model.ResponsesApi.LoginResponse
import com.evans.technologies.usuario.model.user
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.support.v4.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class LoginFragment : Fragment(), View.OnClickListener {

    private lateinit var prefs: SharedPreferences
    private lateinit var navFragment: SharedPreferences
    lateinit var data: user
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_login, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navFragment= context!!.getSharedPreferences("navFragment", Context.MODE_PRIVATE)
        navFragment.edit().clear().apply()
        prefs =context!!. getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        setCredentialIfExist()

        login_button_registrar.isEnabled=true
        login_button_logeo.setOnClickListener(this)
        login_button_registrar.setOnClickListener(this)
        al_txt_olvidaste.setOnClickListener(this)
        ActivityCompat.requestPermissions(activity!!,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            30)
        super.onViewCreated(view, savedInstanceState)
    }


    @SuppressLint("MissingPermission", "HardwareIds")
    private fun userLogin() {

        if (!login_edit_text_usuario.userValido())
        {
            login_edit_text_usuario.error = "Complete el campo con un correo"
            login_edit_text_usuario.requestFocus()
            login_progressbar.visibility= View.GONE
            login_button_logeo.isEnabled=true
            login_button_registrar.isEnabled=true
            return
        }
        if (!login_edit_text_contraseña.passwordvalido())
        {
            login_edit_text_contraseña.error = "Contraseña no valida"
            login_edit_text_contraseña.requestFocus()
            login_progressbar.visibility= View.GONE
            login_button_logeo.isEnabled=true
            login_button_registrar.isEnabled=true
            return
        }
        login_progressbar.visibility= View.VISIBLE
        login_button_logeo.isEnabled=false
        login_button_registrar.isEnabled=false
        val call = RetrofitClient
            .getInstance()
            .api
            .loginUser(login_edit_text_usuario.text.toString(), login_edit_text_contraseña.text.toString())
        //  val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        //telephonyManager.getDeviceId()
        call.enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.e("datosenviados",login_edit_text_usuario.text.toString() +"  "+ login_edit_text_contraseña.text.toString())

                when(response.code()){
                    200->{
                        goToMain(response.body()!!.user.id, response.body()!!.user.token)
                    }
                    400->{
                        login_button_logeo.isEnabled=true
                        login_button_registrar.isEnabled=true
                        login_progressbar.visibility= View.GONE
                        activity!!.toastLong("Email o contraseña incorrectos")
                    }
                    500->{
                        login_button_logeo.isEnabled=true
                        login_button_registrar.isEnabled=true
                        login_progressbar.visibility= View.GONE
                        activity!!.toastLong("Error al realizar la petición.")
                    }
                    404->{
                        login_button_logeo.isEnabled=true
                        login_button_registrar.isEnabled=true
                        login_progressbar.visibility= View.GONE
                        activity!!.toastLong("El usuario no existe.")
                    }
                }

                /* if (response.body()!=null){
                     if (response.body()!!.isSuccess){
                         Log.e("token",response.body()!!.user.id +"  "+ response.body()!!.user.token)

                     }else{


                     }
                 }else{
                     login_button_logeo.isEnabled=true
                     login_button_registrar.isEnabled=true
                     login_progressbar.visibility=View.GONE
                     activity!!.toastLong(response.body()!!.getMessage()+response.code()+"  ")
                 }*/

            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                login_button_logeo.isEnabled=true
                login_button_registrar.isEnabled=true
                login_progressbar.visibility= View.GONE
                activity!!.toastLong("Revise su conexion a internet")
            }
        })
    }

    private fun saveOnPreferencesDataToken(id: String?, token: String?) {
        val editor = prefs.edit()
        editor.putString("id",id)
        editor.putString("token",token)
        editor.apply()

    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.login_button_registrar -> {
                findNavController().navigate(R.id.action_loginFragment_to_correo)
//                login_button_registrar.isEnabled=false
//                startActivity(Intent(activity!!, RegisterFragment::class.java))
//                login_button_registrar.isEnabled=true
            }
            R.id.login_button_logeo -> {
                try{
                    var view = activity!!.currentFocus
                    view!!.clearFocus()
                    if (view != null) {
                        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view.windowToken, 0)
                    }
                }catch (e:Exception){

                }

                userLogin()
            }
            R.id.al_txt_olvidaste->{
                val bundle= Bundle()
                bundle.putBoolean( "sendRecuperarContraseña",true)

                findNavController().navigate(R.id.action_loginFragment_to_correo)
//                startActivity(Intent(activity!!,
//                    recuperar_contra::class.java))
            }
        }
    }

    fun goToMain(id:String,token: String) {
        val getInfo=RetrofitClient.getInstance()
            .api.getUserInfo(token,id)
        Log.e("datosenviados",token+"  "+ id)

        getInfo.enqueue(object: Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                login_button_logeo.isEnabled=true
                login_button_registrar.isEnabled=true
                login_progressbar.visibility= View.GONE
                activity!!.toastLong("Revise su conexion a internet")
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){

                    data= response.body()!!.user

                    FirebaseInstanceId.getInstance().instanceId
                        .addOnCompleteListener(
                            OnCompleteListener {
                                    task ->
                                if (task.isSuccessful){
                                    val Firebasetoken = task.result!!.token
                                    val user = user(Firebasetoken)
                                    val call = RetrofitClient.getInstance()
                                        .api.tokenFCM(token,id,token,Firebasetoken)
                                    Log.e("tokenid",Firebasetoken)
                                    call.enqueue(object: Callback<user> {
                                        override fun onResponse(call: Call<user>, response: Response<user>) {
                                            Log.e("httprequest",call.isExecuted.toString())
                                            Log.e("httprequest",call.request().body().toString())
                                            Log.e("httprequest",response.code().toString())
                                            Log.e("httprequest","  "+data.accountActivate)
                                            Log.e("httprequest",response.message())
                                            if (response.isSuccessful){

                                                saveOnPreferences(id,token,Firebasetoken,
                                                    data.email?:"Desconocido",data.accountActivate?:false,data.name?:"Non",
                                                    data.surname?:"Desc", data.city?:"Puno",
                                                    data.celphone?:"999999999", data.numDocument,data.isReferred)
                                                var data_prueba= File("/storage/emulated/0/evans/evans"+ getUserId_Prefs(prefs) +".jpg")
                                                if (data_prueba.exists()){
                                                    setRutaImagen(prefs,data_prueba.path)
                                                    login_button_logeo.isEnabled=true
                                                    login_button_registrar.isEnabled=true
                                                    login_progressbar.visibility= View.GONE
                                                    startActivity<MainActivity>("tokensend" to token)
                                                    activity!!.finish()
                                                }else if (data.imageProfile!=null&&!(data.imageProfile.equals("nulo")))
                                                    guardar_foto("https://evans-img.s3.us-east-2.amazonaws.com/"+data.imageProfile)
                                                else{
                                                    login_button_logeo.isEnabled=true
                                                    login_button_registrar.isEnabled=true
                                                    login_progressbar.visibility= View.GONE
                                                    startActivity<MainActivity>("tokensend" to token)
                                                    activity!!.finish()
                                                }
                                            }

                                        }
                                        override fun onFailure(call: Call<user>, t: Throwable) {
                                            login_button_logeo.isEnabled=true
                                            login_button_registrar.isEnabled=true
                                            login_progressbar.visibility= View.GONE
                                            activity!!.toastLong("Revise su conexion a internet")
                                        }
                                    })

                                }else{
                                    login_button_logeo.isEnabled=true
                                    login_button_registrar.isEnabled=true
                                    login_progressbar.visibility= View.GONE
                                    activity!!.toastLong("Error al obtener Instancia ${task.result}")
                                    Log.e("Hola", "${task.result} getInstanceId failed ${task.exception}")
                                }
                            }
                        )
                }else{
                    login_button_logeo.isEnabled=true
                    login_button_registrar.isEnabled=true
                    login_progressbar.visibility= View.GONE
                    activity!!.toastLong("Revise su conexion a internet")
                    return
                }
            }

        })



    }

    private fun setCredentialIfExist(){
        val email = getUserEmail(prefs)
        val password = getUserPassword(prefs)
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            login_edit_text_usuario.setText(email)
            login_edit_text_contraseña.setText(password)
        }
    }
    fun guardar_foto(url:String){
        Glide.with(activity!!)
            .asBitmap()
            .load(url)
            // .fitCenter()
            .into(object : SimpleTarget<Bitmap>(100, 100) {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                ) {
                    /*try {
                        val path =  Environment.getExternalStorageDirectory().path+ "/" + "evans" + "/"

                        val file =
                                File(path, "evansimageprofile" + getUserId_Prefs(prefs) + ".jpg")
                        val out = FileOutputStream(file)
                        resource.compress(Bitmap.CompressFormat.JPEG, 85, out)
                        out.flush()
                        out.close()
                        if(file.exists()){
                            setRutaImagen(prefs,file.path)
                        }
                    }
                    catch (e: IOException) {
                        // handle exception
                    }*/
                    var archivo= File(saveToInternalStorage(resource, getUserId_Prefs(prefs)!!))
                    if(archivo.exists()){
                        setRutaImagen(prefs,archivo.path)
                        Log.e("imagen","guardado")
                    }
                    login_button_logeo.isEnabled=true
                    login_button_registrar.isEnabled=true
                    login_progressbar.visibility= View.GONE
                    activity!!.toastLong("guardado correctamente")
                    startActivity<MainActivity>()

                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    Log.e("imagen","Error al descargar imagne")
                    activity!!.toastLong("guardado correctamente")
                    startActivity<MainActivity>()
                    activity!!.finish()
                }
            })

    }
    private fun saveOnPreferences(id: String, token: String,accesToken:String,email:String, accountActivate:Boolean, name:String, surname:String, city:String, cellphone:String, dni:String,referred:Boolean){
        val editor = prefs.edit()
        editor.putString("id",id)
        editor.putString("token",token)
        editor.putString("email",email)
        editor.putString("accesToken",accesToken)
        editor.putBoolean("accountActivate",accountActivate)
        editor.putString("name",name)
        editor.putString("surname",surname)
        editor.putString("city",city)
        editor.putString("cellphone",cellphone)
        editor.putString("dni",dni)
        editor.putString("password", login_edit_text_contraseña.text.toString())
        editor.putBoolean("isreferred", referred)
        editor.apply()
    }

}
