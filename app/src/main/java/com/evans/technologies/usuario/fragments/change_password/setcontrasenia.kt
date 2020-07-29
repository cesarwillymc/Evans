package com.evans.technologies.usuario.fragments.change_password

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.evans.technologies.usuario.Activities.MainActivity

import com.evans.technologies.usuario.R
import com.evans.technologies.usuario.Retrofit.RetrofitClient
import com.evans.technologies.usuario.Utils.*
import com.evans.technologies.usuario.model.ResponsesApi.LoginResponse
import com.evans.technologies.usuario.model.data
import com.evans.technologies.usuario.model.user
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.fragment_setcontrasenia.*
import org.jetbrains.anko.support.v4.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.jar.Manifest

/**
 * A simple [Fragment] subclass.
 */
class setcontrasenia : Fragment(), View.OnClickListener {
    private lateinit var prefs: SharedPreferences
    private lateinit var navFragment: SharedPreferences
    private lateinit var correo: String
    lateinit var data: user
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setcontrasenia, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (setcontraseniaArgs.fromBundle(requireArguments()).correo!!.isNotEmpty()){
            this.correo= setcontraseniaArgs.fromBundle(requireArguments()).correo!!
        }
        navFragment= requireContext().getSharedPreferences("navFragment", Context.MODE_PRIVATE)
        navFragment.edit().clear().apply()
        prefs =requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE)


        fsc_btn_next_signin.setOnClickListener(this)
        fsc_txt_forgot.setOnClickListener(this)
        super.onViewCreated(view, savedInstanceState)
    }


    @SuppressLint("MissingPermission", "HardwareIds")
    private fun userLogin() {

        if (!fsc_edtxt_contarseña.passwordvalido())
        {
            fsc_edtxt_contarseña.error = "Contraseña no valida"
            fsc_edtxt_contarseña.requestFocus()
            progressBar.visibility= View.GONE
            fsc_edtxt_contarseña.isEnabled=true
            fsc_edtxt_contarseña.isEnabled=true
            return
        }
        progressBar.visibility= View.VISIBLE
        fsc_btn_next_signin.isEnabled=false
        val call = RetrofitClient
            .getInstance()
            .api
            .loginUser(correo, fsc_edtxt_contarseña.text.toString())
        //  val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        //telephonyManager.getDeviceId()
        call.enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.e("datosenviados",correo +"  "+ fsc_edtxt_contarseña.text.toString())

                when(response.code()){
                    200->{
                        goToMain(response.body()!!.user.id, response.body()!!.user.token)
                    }
                    400->{
                        fsc_btn_next_signin.isEnabled=true
                        progressBar.visibility= View.GONE
                        activity!!.toastLong("Email o contraseña incorrectos")
                    }
                    500->{
                        fsc_btn_next_signin.isEnabled=true
                        progressBar.visibility= View.GONE
                        activity!!.toastLong("Error al realizar la petición.")
                    }
                    404->{
                        fsc_btn_next_signin.isEnabled=true
                        progressBar.visibility= View.GONE
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
                     progressBar.visibility=View.GONE
                     activity!!.toastLong(response.body()!!.getMessage()+response.code()+"  ")
                 }*/

            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

                fsc_btn_next_signin.isEnabled=true
                progressBar.visibility= View.GONE
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
            R.id.fsc_btn_next_signin -> {
                try{
                    var view = requireActivity().currentFocus
                    requireView().clearFocus()
                    if (view != null) {
                        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view.windowToken, 0)
                    }
                }catch (e:Exception){

                }

                userLogin()
            }
            R.id.fsc_txt_forgot->{
                val bundle= Bundle()
                bundle.putBoolean( "sendRecuperarContraseña",true)

                findNavController().navigate(R.id.action_setcontrasenia_to_correo,bundle)
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
                fsc_btn_next_signin.isEnabled=true
                progressBar.visibility= View.GONE
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
                                                    data.celphone?:"999999999", data.numDocument,data.isReferred?:false)
                                                var data_prueba= File("/storage/emulated/0/evans/evans"+ getUserId_Prefs(prefs) +".jpg")
                                                Log.e("imagen",data.imageProfile)
                                                if (data_prueba.exists()){
                                                    setImgUrl(prefs,"https://evans-img.s3.us-east-2.amazonaws.com/"+data.imageProfile)
                                                    setRutaImagen(prefs,data_prueba.path)
                                                    fsc_btn_next_signin.isEnabled=true
                                                    progressBar.visibility= View.GONE
                                                    startActivity<MainActivity>("tokensend" to token)
                                                    activity!!.finish()
                                                }else if (!data.imageProfile.contains("null") )
                                                    guardar_foto("https://evans-img.s3.us-east-2.amazonaws.com/"+data.imageProfile)
                                                else{
                                                    fsc_btn_next_signin.isEnabled=true
                                                    progressBar.visibility= View.GONE
                                                    startActivity<MainActivity>("tokensend" to token)
                                                    activity!!.finish()
                                                }
                                            }

                                        }
                                        override fun onFailure(call: Call<user>, t: Throwable) {
                                            fsc_btn_next_signin.isEnabled=true
                                            progressBar.visibility= View.GONE
                                            activity!!.toastLong("Revise su conexion a internet")
                                        }
                                    })

                                }else{
                                    fsc_btn_next_signin.isEnabled=true
                                    progressBar.visibility= View.GONE
                                    activity!!.toastLong("Error al obtener Instancia ${task.result}")
                                    Log.e("Hola", "${task.result} getInstanceId failed ${task.exception}")
                                }
                            }
                        )
                }else{
                    fsc_btn_next_signin.isEnabled=true
                    progressBar.visibility= View.GONE
                    activity!!.toastLong("Revise su conexion a internet")
                    return
                }
            }

        })



    }

//    private fun setCredentialIfExist(){
//        val email = getUserEmail(prefs)
//        val password = getUserPassword(prefs)
//        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
//            login_edit_text_usuario.setText(email)
//            fsc_btn_next_signin.setText(password)
//        }
//    }
    fun guardar_foto(url:String){
        setImgUrl(prefs,url)
        Glide.with(requireActivity())
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
                    fsc_btn_next_signin.isEnabled=true
                    progressBar.visibility= View.GONE
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
        editor.putString("password", fsc_btn_next_signin.text.toString())
        editor.putBoolean("isreferred", referred)
        editor.apply()
    }

}


