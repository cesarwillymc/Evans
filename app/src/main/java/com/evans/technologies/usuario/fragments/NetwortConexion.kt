package com.evans.technologies.usuario.fragments

import android.util.Log
import android.widget.Toast
import com.evans.technologies.usuario.Retrofit.RetrofitClientMaps
import com.evans.technologies.usuario.Utils.DirectionsHelper.DirectionsJSONParser
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.suspendAtomicCancellableCoroutine
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class NetwortConexion {
    suspend fun getStringPoline(  url:String
    ) : MutableList<MutableList<java.util.HashMap<String, String>>> = suspendAtomicCancellableCoroutine{
        val llamada: Call<String> =
            RetrofitClientMaps.getInstance().api.callMaps( url)
        llamada.enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                if (response.isSuccessful) {
                    try {
                        val jObject = JSONObject(response.body()!!)
                        val parser = DirectionsJSONParser()
                        val routes: MutableList<MutableList<java.util.HashMap<String, String>>>? = parser.parse(jObject)
                        it.resume(routes!!)


                    } catch (e: Exception) {
                        it.resumeWithException(e)
//                        emit(Resource.Failure(e))
                        Log.e("Gson: ", e.message)
                        e.printStackTrace()
                    }
                } else {

                    it.resumeWithException( Exception("No se encontraron rutas"))

                }
            }

            override fun onFailure(
                call: Call<String>,
                t: Throwable
            ) {
                it.resumeWithException( Exception("Error con la red"))

            }
        })
    }

}