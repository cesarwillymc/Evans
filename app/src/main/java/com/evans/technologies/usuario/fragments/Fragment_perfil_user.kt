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
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.evans.technologies.usuario.Activities.InicioActivity

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
class Fragment_perfil_user : Fragment() {
    private var  file: File? = null
    private var doc: Uri? = null
    private var capturePath: String? = null
    private var prefs: SharedPreferences? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_perfil_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        setClaseActual(prefs!!, Fragment_perfil_user().toString())



        if (!(getRutaImagen(prefs!!).equals("nulo"))){
            var file: File = File(getRutaImagen(prefs!!))

            if (file.exists()) {
                try{
                    val myBitmap = BitmapFactory.decodeFile(file.absolutePath)
                    Glide.with(requireContext()).asBitmap().load(getImageRotate(getRutaImagen(prefs!!)!!,myBitmap))
                        .apply(RequestOptions.circleCropTransform())
                        .into(ffpu_img_profile)
                }catch (e:Exception){

                }
            }
        }else if(getImgUrl(prefs!!)!!.isNotEmpty()){
            Glide.with(requireContext()).asBitmap().load(getImgUrl(prefs!!))
                .apply(RequestOptions.circleCropTransform())
                .into(ffpu_img_profile)
        }
        ffpu_txt_name.text=getUserName(prefs!!)
        ffpu_txt_lastname.text=getUserSurname(prefs!!)
        ffpu_txt_email.text=getUserEmail(prefs!!)
        ffpu_txt_dni.text= getNumberDocument(prefs!!)
        ffpu_txt_phone.text=getcellphoneUser(prefs!!)
        ffpu_btn_logout.setOnClickListener {
            var editor2 = requireContext().getSharedPreferences("datadriver", Context.MODE_PRIVATE).edit()
            editor2.clear()
            editor2.apply()
            val editor = prefs!!.edit()
            editor.clear()
            editor.apply()
            val intent = Intent(requireContext(), InicioActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        ffpu_img_profile.setOnClickListener {
            saveImg()
        }
        linearLayout3.setOnClickListener {
            findNavController().navigate(R.id.action_nav_cuenta_to_passActual)
        }
        // activity!!.toastLong("sad")

    }

    fun saveImg(){
        val opciones = arrayOf<CharSequence>("Tomar Foto", "Cargar Imagen")
        val alertOpciones = AlertDialog.Builder(requireContext())

        alertOpciones.setTitle("Seleccione una opción:")
        alertOpciones.setItems(opciones) { dialogInterface, i ->
            if (opciones[i] == "Tomar Foto") {
                file = null
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
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
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                            Manifest.permission.CAMERA)) {
                        ActivityCompat.requestPermissions(requireActivity(),
                            arrayOf(Manifest.permission.CAMERA),
                            20)
                    }
                }
            } else {
                file = null
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    30)

                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type = "image/"
                startActivityForResult(Intent.createChooser(intent, "Seleccione la Aplicación"), 10)
            }
        }

        alertOpciones.show()
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            30
        )
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA),
            20
        )

    }




    private fun guardarFotoEnArchivo() {

        //MediaType.parse("multipart/form-data")

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
        val cursor = requireActivity().managedQuery(uri, projection, null, null, null)
        requireActivity().startManagingCursor(cursor)
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    private fun subir_datos(body: MultipartBody.Part) {
        val progressDoalog: ProgressDialog = ProgressDialog(requireContext())
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
//                    ftvg_user_button_guardar.visibility=View.GONE
                    activity!!.toastLong("Se subio su nueva imagen de perfil")
                    if (file!!.exists()) {
                        setRutaImagen(prefs!!, file!!.getPath())
                    }
                } else {
                    activity!!.toastLong("La imagen no fue cargada para su perfil")
                    Log.e("subir_imagen", response.code().toString() + "")
                    progressDoalog.dismiss()
//                    ftvg_user_button_guardar.visibility=View.GONE
                }

            }

            override fun onFailure(call: Call<user>, t: Throwable) {
                activity!!.toastLong("Se presentaron problemas con la red")
                progressDoalog.dismiss()
//                ftvg_user_button_guardar.visibility=View.GONE
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
                    ffpu_img_profile.setImageURI(doc)

                    if (doc!=null) {
                        file = File(getPath( doc))
                        imgSaved()
                    }

                }
                20 //20 -> Tomar nueva foto
                ->

                    // bitmap = (Bitmap) data.getExtras().get("data");
                    if (file!!.exists()) {

                        val myBitmap = BitmapFactory.decodeFile(file!!.getAbsolutePath())

                        ffpu_img_profile.setImageBitmap(myBitmap)
                        imgSaved()
                    }
            }
        }

    }
    fun imgSaved() {
        if (file != null) {
            guardarFotoEnArchivo()
        } else {
            longToast("Ingrese una imagen por favor")
        }
    }

}
