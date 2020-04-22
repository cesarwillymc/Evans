package com.evans.technologies.usuario.Activities.viajes

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.evans.technologies.usuario.R
import com.evans.technologies.usuario.Retrofit.RetrofitClientMaps
import com.evans.technologies.usuario.Utils.DirectionsHelper.DirectionsJSONParser
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.fragment_historial_viaje.*
import kotlinx.android.synthetic.main.fragment_mapa_inicio.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class historial_viaje : Fragment() , OnMapReadyCallback{



    lateinit var googleMap:GoogleMap
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_historial_viaje, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //
        //historial_viajeArgs().tripData
        mapa_historial.onCreate( savedInstanceState )
        mapa_historial.onResume()
        mapa_historial.getMapAsync(this)


    }
    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()

    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (outState!=null){
            mapView.onSaveInstanceState(outState)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
    private fun getDirectionsUrl(
        origin: LatLng,
        dest: LatLng
    ): String? { // Punto de origen
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
        return "$output?$parameters&key=AIzaSyAPTlJ_g6cCp5eCwQS_Lw-whyIm-JllzlY"
    }
    @SuppressLint("LogNotTimber")
    fun getRetrofitMap(
        origin: LatLng?,
        dest: LatLng?
    ) { /* Retrofit retrofit= new Retrofit.Builder().baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(ScalarsConverterFactory.create()).build();
        Api api= retrofit.create(Api.class);*/
        Log.e("Gson: ", "$origin  $dest" )
        val llamada: Call<String> =
            RetrofitClientMaps.getInstance().api.callMaps(getDirectionsUrl(origin!!, dest!!))
        llamada.enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {

                //Data= g.fromJson(response.body(),Example.class);
                Log.e("Gson: ", response.body())
                if (response.isSuccessful) {

//                    var routes: List<List<HashMap<String,String>>>?=null
                    try {
                        val jObject = JSONObject(response.body()!!)
                        val parser = DirectionsJSONParser()
                        val routes: MutableList<MutableList<java.util.HashMap<String, String>>>? = parser.parse(jObject)

                        dibujarLineas(routes!!)
//                            mapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(
//                                     LatLngBounds.Builder()
//                                            .include( LatLng(origin.latitude(), origin.longitude()))
//                                            .include( LatLng(destination.latitude(), destination.longitude()))
//                                            .build(), 50), 3)

                        val cameramove= LatLngBounds(origin,dest)
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(cameramove,50))
                        // googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cameramove.center,15f))

                    } catch (e: Exception) {
                        Log.e("Gson: ", e.message)
                        e.printStackTrace()
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Error al traer data: " + response.code(),
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }
            }

            override fun onFailure(
                call: Call<String>,
                t: Throwable
            ) {
            }
        })
    }
    fun dibujarLineas(result: MutableList<MutableList<java.util.HashMap<String, String>>>): PolylineOptions {

        var points: ArrayList<LatLng> = ArrayList()
        var lineOptions: PolylineOptions? = PolylineOptions()
        for (i in result) {
            Log.e("result.indices: ", "$i  ${i.size}")
            for (j in i) {
                val lat = j["lat"]!!.toDouble()
                val lng = j["lng"]!!.toDouble()
                val position =
                    LatLng(lat, lng)

                points.add(position)
                Log.e("path.indices: ", position.toString())
            }
            lineOptions!!.addAll(points)
            lineOptions.width(12f)
            lineOptions.color(Color.BLACK)
            lineOptions.geodesic(true)

        }
        googleMap.addPolyline(lineOptions!!)
        return lineOptions!!
    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap=p0!!
        googleMap.uiSettings.isTiltGesturesEnabled=false
        googleMap.uiSettings.isScrollGesturesEnabled=false
        googleMap.uiSettings.isScrollGesturesEnabledDuringRotateOrZoom=false
    }


}
