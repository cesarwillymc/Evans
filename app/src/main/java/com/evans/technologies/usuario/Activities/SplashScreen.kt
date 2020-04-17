package com.evans.technologies.usuario.Activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import androidx.core.app.ActivityCompat
import com.evans.technologies.usuario.R
import com.evans.technologies.usuario.Utils.getUserEmail
import com.evans.technologies.usuario.Utils.getUserPassword
import com.evans.technologies.usuario.fragments.auth.LoginFragment

class SplashScreen : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Handler().postDelayed({
            initSesion()
        }, 1000)


    }
    fun initSesion(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            30
        )
        var valor="hola"
        // var dataDriver = getSharedPreferences("datadriver", Context.MODE_PRIVATE)
        //setdataNotification_noti(dataDriver,"nulo","")
        //{latitudeOrigen=-15.839086694816755, startAddress=sadsad, userId=5e1c067278f2bb00170b8232, response=requestDriver, longitudeOrigen=-70.01764614355434, longitudeDestino=-70.0222960003151, viajeId=5e277e8475a2db00175fc7d5, travelRate=20, destinationAddress=sdsadsadsadsadsad, latitudeDestino=-15.83869473760434}
        intent.extras?.let {
            Log.e("NotificationReceiver",it.getString("data")+" splash")
            try{
                valor=it.getString("data")!!
            }catch (e:Exception){

            }

        }

        if (!valor.contains("hola"))
            Log.d("MainActivity22",valor )
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE)

        val intentLogin = Intent(this@SplashScreen, InicioActivity::class.java)
        val intentMain = Intent(this@SplashScreen, MainActivity::class.java)
        Log.e("data", getUserEmail(prefs) + "  y " + getUserPassword(prefs))
        if(!TextUtils.isEmpty(getUserEmail(prefs)) &&
            !TextUtils.isEmpty(getUserPassword(prefs))){
            if (valor.contains("hola")){

                startActivity(intentMain)
                finish()
            }
            else{
                if (valor.contains("{")){
                    // setdataNotification_noti(dataDriver,valor)
                    // setEstadoViews(dataDriver,3)
                }
                startActivity(intentMain)
                finish()
            }
        }else{

            startActivity(intentLogin)
            finish()
        }

    }
}
