package com.evans.technologies.usuario.fragments

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.evans.technologies.usuario.Utils.getUserId_Prefs
import com.evans.technologies.usuario.Utils.timeCallback.ArchLifecycleApp.Companion.getContextApp
import com.evans.technologies.usuario.model.modelTrip.HistorialAll
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import java.util.HashMap
import kotlin.coroutines.CoroutineContext

class viewModelMain:ViewModel(),CoroutineScope{
    val job= Job()
    override val coroutineContext: CoroutineContext
        get() =job+ Dispatchers.IO
    val network=NetwortConexion()
    val dataDriver=getContextApp().getSharedPreferences("datadriver", Context.MODE_PRIVATE)
    @ExperimentalStdlibApi
    fun getRutaMapa(origin:LatLng, dest:LatLng):LiveData<Resource<ArrayList<LatLng>>> = liveData(Dispatchers.IO){
        emit(Resource.Loading())
        try{
            runBlocking {
                val dato=network.getStringPoline(getDirectionsUrl(origin,dest))
                val convertData= getDataPoints(dato) as ArrayList<LatLng>
                emit(Resource.Success(convertData))
            }
        }catch (e:Exception){
            emit(Resource.Failure(e))
        }
    }

    @ExperimentalStdlibApi
    private suspend  fun getDataPoints(dato: MutableList<MutableList<HashMap<String, String>>>): List<LatLng> = buildList {
        //var points: ArrayList<LatLng> = ArrayList()

        for (i in dato) {
            for (j in i) {
                val lat = j["lat"]!!.toDouble()
                val lng = j["lng"]!!.toDouble()
                val position =
                    LatLng(lat, lng)
                add(position)
            }
        }
    }

    private fun getDirectionsUrl(
        origin: LatLng,
        dest: LatLng
    ): String { // Punto de origen
        val str_origin = "origin=" + origin.latitude + "," + origin.longitude
        // punto de destino
        val str_dest = "destination=" + dest.latitude + "," + dest.longitude
        // Sensor de modo drive
        val sensor = "sensor=false"
        val mode = "mode=driving"
        // Sensor
        val parameters = "$str_origin&$str_dest&$sensor&$mode"
        // Formato de salida
        val output = "json"
        // url
        // https://maps.googleapis.com/maps/api/directions/json?origin=-15.837974456285096,-70.02117622643709&destination=-15.837974456285096,-70.02117622643709&sensor=false&mode=driving&key=AIzaSyD7kwgqDzGW8voiXP7gAbxaKnGY_Fr4Cng
        return "$output?$parameters&key=AIzaSyBXQBe1pHpqQkclaoMEuAnZ6QVFbC860Yo"
    }
    @ExperimentalStdlibApi
    fun getDataTrip(id:String):LiveData<Resource<HistorialAll>> = liveData(Dispatchers.IO){
       emit(Resource.Loading())
        try{
            val dato=network.getDataTrip(id)
            runBlocking {
                val ruta=network.getStringPoline(
                    getDirectionsUrl(LatLng(dato.latitudeOrigen,dato.longitudeOrigen),LatLng(dato.latitudeDestino,dato.longitudeDestino)))
                val convertData= getDataPoints(ruta) as ArrayList<LatLng>
                dato.ruta=convertData
                emit(Resource.Success(dato))
            }

       }catch (e:Exception){
        emit(Resource.Failure(e))
       }
    }
    val chatValue = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            network.chatConexion( getUserId_Prefs(dataDriver)!!).collect {
                emit(it)
            }
        }catch (e:Exception){
            emit(Resource.Failure(e))
        }
    }
}