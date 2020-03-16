package com.evans.technologies.usuario.fragments

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.evans.technologies.usuario.R
import com.evans.technologies.usuario.Retrofit.RetrofitClient
import com.evans.technologies.usuario.Utils.*
import com.evans.technologies.usuario.fragments.change_password.set_codigo
import com.evans.technologies.usuario.model.user
import kotlinx.android.synthetic.main.fragment_fragment_perfil_user.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.support.v4.longToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

/**
 * A simple [Fragment] subclass.
 */
class Fragment_perfil_user : Fragment(),View.OnClickListener {
    private var  file: File? = null
    private var doc: Uri? = null
    private var capturePath: String? = null
    private var prefs: SharedPreferences? = null
    lateinit var vista:View
    lateinit var imagen: ImageView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        vista=inflater.inflate(R.layout.fragment_fragment_perfil_user, container, false)
        prefs = context!!.getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        setClaseActual(prefs!!, Fragment_perfil_user().toString())
        imagen=vista.findViewById<ImageView>(R.id.ftvg_user_imagen_perfil)
        imagen.setOnClickListener(this)

        if (!(getRutaImagen(prefs!!).equals("nulo"))){
            var file: File = File(getRutaImagen(prefs!!))

            if (file.exists()) {
                try{
                    val myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath())
                    Glide.with(context!!).asBitmap().load(getImageRotate(getRutaImagen(prefs!!)!!,myBitmap))
                        .apply(RequestOptions.circleCropTransform())
                        .into(imagen)
                    imagen.isEnabled=false
                }catch (e:Exception){

                }


            }
        }
        // activity!!.toastLong("sad")

        cargardatos()

        return vista
    }
    private fun cargardatos() {



        var ciudadPais=vista.findViewById<TextView>(R.id.ftvg_txt_ciudad_user)
        var ratingDriver=vista.findViewById<TextView>(R.id.ftvg_user_txt_rating)
        var nombreApellidos=vista.findViewById<TextView>(R.id.ftvg_user_text_name)
        var viajesTotales=vista.findViewById<TextView>(R.id.ftvg_user_text_viajes)
        var correoDriver=vista.findViewById<TextView>(R.id.ftvg_user_txt_correo)
        var cupones=vista.findViewById<TextView>(R.id.ftvg_user_txt_cupones)
        var celular=vista.findViewById<TextView>(R.id.ftvg_user_text_phone)
        var nombrePerfil=vista.findViewById<TextView>(R.id.ftvg_txt_nombre_user)
        var validarbtn=vista.findViewById<Button>(R.id.ftvg_user_button_validar_account)

        //Dar valores a la imagenes

        nombreApellidos.text= getUserName(prefs!!) +" "+ getUserSurname(prefs!!)
        correoDriver.text= getUserEmail(prefs!!)
        nombrePerfil.text= getUserName(prefs!!)
        ciudadPais.text= getcityUser(prefs!!) +", Perú"
        cupones.text= "0"
        celular.text= getcellphoneUser(prefs!!)
        // Log.e("status",""+getAccountActivate(prefs!!)?:false)
        try{
            if (getAccountActivate(prefs!!)){
                validarbtn.visibility=View.GONE
            }else{
                validarbtn.visibility=View.VISIBLE
            }
        }catch ( e:Exception){

        }

        validarbtn.setOnClickListener {
            var enviarCode: Call<user> = RetrofitClient.getInstance().api.enviarCorreo_validate(
                getUserEmail(prefs!!)!!)
            enviarCode.enqueue(object : Callback<user> {
                override fun onFailure(call: Call<user>, t: Throwable) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onResponse(call: Call<user>, response: Response<user>) {
                    if( response.isSuccessful){
                        activity!!.toastLong("Se envio un mensaje a tu correo")

                        val manager = activity!!.supportFragmentManager
                        manager.beginTransaction().replace(
                            R.id.main_layout_change_fragment,
                            set_codigo(true, false)
                        ).commit()
                    }
                }
            })

        }

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.ftvg_user_imagen_perfil->{
                val opciones = arrayOf<CharSequence>("Tomar Foto", "Cargar Imagen")
                val alertOpciones = AlertDialog.Builder(context!!)

                alertOpciones.setTitle("Seleccione una opción:")
                alertOpciones.setItems(opciones) { dialogInterface, i ->
                    if (opciones[i] == "Tomar Foto") {
                        file = null
                        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            val builder = StrictMode.VmPolicy.Builder()
                            StrictMode.setVmPolicy(builder.build())
                            val directoryPath = Environment.getExternalStorageDirectory().toString() + "/" + "evans" + "/"
                            val filePath = directoryPath + getUserId_Prefs(prefs!!) + ".jpeg"
                            val data = File(directoryPath)

                            if (!data.exists()) {
                                data.mkdirs()
                            }
                            file = File(filePath)
                            capturePath = filePath // you will process the image from this path if the capture goes well
                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(File(filePath)))
                            startActivityForResult(intent, 20)
                        } else {
                            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity!!,
                                    Manifest.permission.CAMERA)) {
                                ActivityCompat.requestPermissions(activity!!,
                                    arrayOf(Manifest.permission.CAMERA),
                                    20)
                            }
                        }
                    } else {
                        file = null
                        ActivityCompat.requestPermissions(activity!!,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            30)

                        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        intent.type = "image/"
                        startActivityForResult(Intent.createChooser(intent, "Seleccione la Aplicación"), 10)
                    }
                }

                alertOpciones.show()
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    30
                )
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(Manifest.permission.CAMERA),
                    20
                )
            }
            R.id.ftvg_user_button_guardar->{

                if (file!=null){
                    guardarFotoEnArchivo()
                }else{
                    longToast("Ingrese una imagen por favor")
                }

            }
        }
    }
    private fun guardarFotoEnArchivo() {

        //MediaType.parse("multipart/form-data")
        Log.e("subir_imagen", file!!.getPath() + "" + file!!.getParent() + "" + file!!.getAbsolutePath() + "" + file!!.getPath() + "")
        if (detectar_formato(file!!.getPath()).equals("ninguno")) {

        } else {
            val requestFile = RequestBody.create(
                MediaType.parse("image/" + detectar_formato(file!!.getPath())),
                file
            )
            Log.e("subir_imagen", "nombre" + file!!.getName())
            // MultipartBody.Part is used to send also the actual file name
            val body = MultipartBody.Part.createFormData("profile", file!!.getName(), requestFile)
            subir_datos(body)
        }

    }

    private fun getPath(uri: Uri?): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = activity!!.managedQuery(uri, projection, null, null, null)
        activity!!.startManagingCursor(cursor)
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    private fun subir_datos(body: MultipartBody.Part) {
        val progressDoalog: ProgressDialog
        progressDoalog = ProgressDialog(context)
        progressDoalog.max = 100
        progressDoalog.setMessage("Cargando...")
        progressDoalog.setTitle("Subiendo Datos")
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        // show it
        progressDoalog.show()
        val name = RequestBody.create(MediaType.parse("text/plain"), "profile")
        val subir_imagen = RetrofitClient.getInstance().getApi()
            .guardarImagenes(getUserId_Prefs(prefs!!)!!, body, name)
        subir_imagen.enqueue(object : Callback<user> {
            override fun onResponse(call: Call<user>, response: Response<user>) {
                if (response.isSuccessful()) {
                    Log.e("subir_imagen", response.code().toString() + "" + getUserId_Prefs(prefs!!))
                    progressDoalog.dismiss()
                    ftvg_user_button_guardar.visibility=View.GONE
                    activity!!.toastLong("Guardado Correctamente")
                    if (file!!.exists()) {
                        setRutaImagen(prefs!!, file!!.getPath())
                    }
                } else {
                    Log.e("subir_imagen", response.code().toString() + "")
                    progressDoalog.dismiss()
                    ftvg_user_button_guardar.visibility=View.GONE
                }

            }

            override fun onFailure(call: Call<user>, t: Throwable) {
                Log.e("subir_imagen", t.message)
                progressDoalog.dismiss()
                ftvg_user_button_guardar.visibility=View.GONE
            }
        })


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                10 //10 -> Seleccionar de la galería
                -> {
                    doc = data!!.data
                    Log.e("subir_imagen", doc.toString())
                    imagen.setImageURI(doc)

                    ftvg_user_button_guardar.visibility=View.VISIBLE
                    ftvg_user_button_guardar.setOnClickListener(this)
                    if (doc!=null) {
                        file = File(getPath( doc))
                    }
                }
                20 //20 -> Tomar nueva foto
                ->

                    // bitmap = (Bitmap) data.getExtras().get("data");
                    if (file!!.exists()) {

                        val myBitmap = BitmapFactory.decodeFile(file!!.getAbsolutePath())

                        imagen.setImageBitmap(myBitmap)
                        ftvg_user_button_guardar.visibility=View.VISIBLE
                        ftvg_user_button_guardar.setOnClickListener(this)
                    }
            }
        }

    }


}
