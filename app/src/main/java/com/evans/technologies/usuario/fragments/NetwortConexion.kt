package com.evans.technologies.usuario.fragments

import android.util.Log
import android.widget.Toast
import com.evans.technologies.usuario.Retrofit.RetrofitClient
import com.evans.technologies.usuario.Retrofit.RetrofitClientMaps
import com.evans.technologies.usuario.Utils.DirectionsHelper.DirectionsJSONParser
import com.evans.technologies.usuario.model.modelTrip.HistorialAll
import com.evans.technologies.usuario.model.modelTrip.trip
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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
    suspend fun getDataTrip(id:String):HistorialAll= suspendAtomicCancellableCoroutine {
        RetrofitClient.getInstance().api.getHistorialTripById(id).enqueue(object : Callback<trip>{
            override fun onFailure(call: Call<trip>, t: Throwable) {
                it.resumeWithException(Exception("${t.message} Error con la conexion"))
            }

            override fun onResponse(call: Call<trip>, response: Response<trip>) {
                if (response.isSuccessful){
                    it.resume(response.body()!!.data)
                }else{
                    it.resumeWithException(Exception("${response.code()} Error al traer datos"))
                    Log.e("historial",response.code().toString())
                }
            }

        })
    }

    suspend fun chatConexion(id:String): Flow<Resource<DataSnapshot>> = callbackFlow {
        val conexion=FirebaseDatabase.getInstance().reference.child("chatsFirebase").child(id)
        conexion.limitToLast(15).addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                channel.close(Exception(p0.message))
            }
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    offer(Resource.Success(p0))
                }else{
                    channel.close(Exception("No existen Datos"))
                }
            }
        })
        awaitClose { conexion.removeValue() }
    }


}