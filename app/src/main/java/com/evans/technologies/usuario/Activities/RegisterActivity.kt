package com.evans.technologies.usuario.Activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.evans.technologies.usuario.R
import com.evans.technologies.usuario.Retrofit.RetrofitClient
import com.evans.technologies.usuario.Utils.setIDrecuperar
import com.evans.technologies.usuario.Utils.toastLong
import com.evans.technologies.usuario.fragments.change_password.set_codigo
import com.evans.technologies.usuario.model.ResponsesApi.RegisterResponse
import com.evans.technologies.usuario.model.user
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var navFragment: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        navFragment = getSharedPreferences("navFragment", Context.MODE_PRIVATE)
        /*   checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
               prueba=isChecked
           }*/
        txt_view_terminos.setText(
            Html.fromHtml("Al continuar, confirmo que lei y acepto los " +
                    "<a href='www.google.com.pe'>Terminos y condiciones</a>"+" y la "+"<a href='www.google.com.pe'>Politica de privacidad</a>"))
        txt_view_terminos.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()

        register_image_view_atras.setOnClickListener(this@RegisterActivity)
        register_button_registrar.setOnClickListener(this@RegisterActivity)

    }

    ////registro de usuarios condiciones

    private fun userSignUp() {

        val name = register_edit_text_nombre.text.toString().trim()
        val surname = register_edit_text_apellido.text.toString().trim()
        val dni = register_edit_text_dni.text.toString().trim()
        val email = register_edit_text_correo.text.toString().trim()
        val celphone = register_edit_text_celular.text.toString().trim()
        val city = register_edit_text_ciudad.text.toString().trim()
        val password = register_edit_text_contraseña.text.toString().trim()
        val passwordconfirm = register_edit_text_verificar_contraseña.text.toString().trim()

        if (name.isEmpty())
        {
            register_edit_text_nombre.error = "Es necesario ingresar tu nombre."
            register_edit_text_nombre.requestFocus()
            return
        }
        if (surname.isEmpty())
        {
            register_edit_text_apellido.error = "Es necesario ingresar tus Apellidos."
            register_edit_text_apellido.requestFocus()
            return
        }
        if (dni.isEmpty())
        {
            register_edit_text_dni.error = "Ingresa tu número de DNI."
            register_edit_text_dni.requestFocus()
            return
        }
        if (email.isEmpty())
        {
            register_edit_text_correo.error = "Es necesario tu Email."
            register_edit_text_correo.requestFocus()
            return
        }
        if (celphone.isEmpty())
        {
            register_edit_text_celular.error = "Es necesario tu número de celular"
            register_edit_text_celular.requestFocus()
        }
        if (city.isEmpty())
        {
            register_edit_text_ciudad.error = "Ingresa tu ciudad"
            register_edit_text_ciudad.requestFocus()
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            register_edit_text_correo.error = "Email invalido"
            register_edit_text_correo.requestFocus()
            return
        }
        if (password.isEmpty())
        {
            register_edit_text_contraseña.error = "Ingresa una contraseña."
            register_edit_text_contraseña.requestFocus()
            return
        }
        if (password.length <= 8)
        {
            register_edit_text_contraseña.error = "La contraseña debe de tener al menos 8 caracteres"
            register_edit_text_contraseña.requestFocus()
            return
        }
        if (passwordconfirm.length<=8 || !(passwordconfirm.equals(password)))
        {
            register_edit_text_verificar_contraseña.error = "Contraseñas diferentes."
            register_edit_text_verificar_contraseña.requestFocus()
            return
        }
        register_progressbar.visibility= View.VISIBLE
        val call = RetrofitClient
            .getInstance()
            .api
            .createUser(name, surname, dni, email, celphone, city, password, passwordconfirm)

        call.enqueue(object: Callback<RegisterResponse> {

            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {

                when(response.code()){
                    200->{

                        setIDrecuperar(navFragment,response.body()!!.user?:"a123")
                        Log.e("idrecuperado",response.body()!!.user?:"no recibe nada"+"")
                        actions()

                        toastLong("Se registro exitosamente")
                    }
                    400->{
                        register_progressbar.visibility= View.GONE
                        toastLong("Contraseña no coincide")
                    }
                    401->{
                        register_progressbar.visibility= View.GONE
                        toastLong("El email ya esta en uso")
                    }
                    402->{
                        register_progressbar.visibility= View.GONE
                        toastLong("El nombre no debe exeder de los 40 caracteres")
                    }
                    403->{
                        register_progressbar.visibility= View.GONE
                        toastLong("Los apellidos no debe exeder de los 40 caracteres")
                    }
                    404->{
                        register_progressbar.visibility= View.GONE
                        toastLong("E-mail no debe exeder de los 40 caracteres o no es un e-mail valido")
                    }
                    405->{
                        register_progressbar.visibility= View.GONE
                        toastLong("El numero celular son solo digitos")
                    }
                    406->{
                        register_progressbar.visibility= View.GONE
                        toastLong("El numero dni son solo digitos")
                    }
                    407->{
                        register_progressbar.visibility= View.GONE
                        toastLong("La contraseña es invalida")
                    }
                    500->{
                        register_progressbar.visibility= View.GONE
                        toastLong("Error al registrarse")
                    }
                }

            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                register_progressbar.visibility= View.GONE
                toastLong("Revise su conexion a internet")
            }
        })
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.register_button_registrar-> {
                try{
                    var view = this.getCurrentFocus()
                    view!!.clearFocus()
                    if (view != null) {
                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view!!.getWindowToken(), 0)
                    }
                }catch (e:Exception){

                }
                userSignUp()

            }
            R.id.register_image_view_atras -> onBackPressed()
            R.id.txt_view_terminos->{
                val url = "http://www.proevans.com/es/general-terms-of-use"
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }
        }

    }
    fun Sfragmentdefault() {
        try{
            txt_view_terminos.isEnabled=false
            val manager = supportFragmentManager
            manager.beginTransaction().replace(R.id.frame, set_codigo(true,true)).commit()
        }catch (E:Exception){
            Log.e("error",E.message)
        }
    }

    private fun actions(){
        frame.visibility= View.VISIBLE
        var llamr: Call<user> = RetrofitClient.getInstance().api.enviarCorreo_validate(register_edit_text_correo.text.toString().trim())
        llamr.enqueue(object: Callback<user> {
            override fun onFailure(call: Call<user>, t: Throwable) {
                register_progressbar.visibility= View.GONE
                toastLong("Revise su conexion a internet")
            }

            override fun onResponse(call: Call<user>, response: Response<user>) {
                register_progressbar.visibility= View.GONE
                if (response.isSuccessful){
                    Sfragmentdefault()
                }else{
                    toastLong("Se registro correctamente")
                }

            }

        })

        // finish()
        //  startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
    }
}
