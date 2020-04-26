package com.evans.technologies.usuario.Activities.viajes

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.evans.technologies.usuario.R
import com.evans.technologies.usuario.Retrofit.RetrofitClientMaps
import com.evans.technologies.usuario.Utils.DirectionsHelper.DirectionsJSONParser
import com.evans.technologies.usuario.Utils.getDestinoLat
import com.evans.technologies.usuario.Utils.getDestinoLong
import com.evans.technologies.usuario.Utils.getOrigenLat
import com.evans.technologies.usuario.Utils.getOrigenLong
import com.evans.technologies.usuario.fragments.Resource
import com.evans.technologies.usuario.fragments.viewModelMain
import com.evans.technologies.usuario.model.modelTrip.HistorialAll
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.fragment_historial_viaje.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
@ExperimentalStdlibApi
class historial_viaje : Fragment() , OnMapReadyCallback{
    private var currentPoline: Polyline?=null
    lateinit var prefs:SharedPreferences
    var data:HistorialAll?=null
    lateinit var  viewModel : viewModelMain
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
        viewModel = requireActivity().run {
            ViewModelProviders.of(this)[viewModelMain::class.java]
        }
        val id=historial_viajeArgs.fromBundle(requireArguments()).id

        prefs = requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        //historial_viajeArgs().tripData
        mapa_historial.onCreate( savedInstanceState )
        mapa_historial.onResume()
        mapa_historial.getMapAsync(this)

        setData(id)
    }


    private fun setData(id:String) {
        viewModel.getDataTrip(id).observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading->{
                    progressBar2.visibility=View.VISIBLE
                }
                is Resource.Success->{
                    progressBar2.visibility=View.GONE
                    setDataFindView(it.data)
                    if (it.data.tripFinalized) {
                        fhv_txt_estado.text="Estado: Viaje Finalizado"
                    }else{
                        fhv_txt_estado.text="Estado: Viaje Cancelado"
                    }
                    fhv_txt_fecha.text=it.data.dateTrip
                    fhv_txt_hora.text=""
                    fhv_txt_origen.text="DE: ${it.data.startAddress}"
                    fhv_txt_destino.text="A: ${it.data.destinationAddress}"
                    fhv_txt_carro.text= "${it.data.modelCar}  ${it.data.brandCar}"
                    fhv_txt_conductor_name.text="${it.data.driver}"
                    fhv_txt_placa.text="PLACA: ${it.data.licenseCar}"
                    fhv_txt_money_cupon.text="${it.data.travelRateDiscount}"
                    fhv_txt_money_cash.text="${it.data.travelRate}"
                    fhv_txt_money_all.text= " ${(it.data.travelRateDiscount.toFloat()+ it.data.travelRate.toFloat())}"

                }
                is Resource.Failure->{
                    progressBar2.visibility=View.GONE
                    Toast.makeText(requireContext(),it.exception.message,Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun setDataFindView(data: HistorialAll) {
        val origen = LatLng(data.latitudeOrigen,data.longitudeOrigen)
        val destino=LatLng(data.latitudeDestino,data.longitudeDestino)
        googleMap.addMarker(
            MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.logo22))
                .title("Destino")
                .position(destino))
        googleMap!!.addMarker(
            MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.logo33))
                .title("Origen")
                .position(origen ))
        try {
            val cameramove= LatLngBounds(destino,origen)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(cameramove,50))
            Log.e("latLong","origen")
        }catch (e:Exception){
            val cameramove= LatLngBounds(origen,destino)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(cameramove,50))
            Log.e("latLong",e.message)
        }
        var lineOptions: PolylineOptions? = PolylineOptions()
        lineOptions!!.addAll(data.ruta!!)
        lineOptions.width(10f)
        lineOptions.color(Color.BLACK)
        lineOptions.geodesic(true)
        currentPoline=googleMap!!.addPolyline(lineOptions)
        Log.e("latLong","origen: ${LatLng(data.latitudeOrigen,data.longitudeOrigen)} destino: ${LatLng(data.latitudeDestino,data.longitudeDestino)}")
        googleMap.uiSettings.isTiltGesturesEnabled=false
        googleMap.uiSettings.isScrollGesturesEnabled=false
        googleMap.uiSettings.isScrollGesturesEnabledDuringRotateOrZoom=false
        googleMap.uiSettings.isZoomControlsEnabled=false
    }


    override fun onStart() {
        super.onStart()
        mapa_historial.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapa_historial.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapa_historial.onPause()

    }

    override fun onStop() {
        super.onStop()
        mapa_historial.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (outState!=null){
            mapa_historial.onSaveInstanceState(outState)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(mapa_historial!=null)
        mapa_historial.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapa_historial.onLowMemory()
    }
//    private fun getDirectionsUrl(
//        origin: LatLng,
//        dest: LatLng
//    ): String? { // Punto de origen
//        val str_origin = "origin=" + origin.latitude + "," + origin.longitude
//        // punto de destino
//        val str_dest = "destination=" + dest.latitude + "," + dest.longitude
//        // Sensor de modo drive
//        val sensor = "sensor=false"
//        val mode = "mode=driving"
//        // Sensor
//        val parameters = "$str_origin&$str_dest&$sensor&$mode"
//        // Formato de salida
//        val output = "json"
//        // url
//        // https://maps.googleapis.com/maps/api/directions/json?origin=-15.837974456285096,-70.02117622643709&destination=-15.837974456285096,-70.02117622643709&sensor=false&mode=driving&key=AIzaSyD7kwgqDzGW8voiXP7gAbxaKnGY_Fr4Cng
//        return "$output?$parameters&key=AIzaSyAPTlJ_g6cCp5eCwQS_Lw-whyIm-JllzlY"
//    }
//    @SuppressLint("LogNotTimber")
//    fun getRetrofitMap(
//        origin: LatLng?,
//        dest: LatLng?
//    ) { /* Retrofit retrofit= new Retrofit.Builder().baseUrl("https://maps.googleapis.com/")
//                .addConverterFactory(ScalarsConverterFactory.create()).build();
//        Api api= retrofit.create(Api.class);*/
//        Log.e("Gson: ", "$origin  $dest" )
//        val llamada: Call<String> =
//            RetrofitClientMaps.getInstance().api.callMaps(getDirectionsUrl(origin!!, dest!!))
//        llamada.enqueue(object : Callback<String> {
//            override fun onResponse(
//                call: Call<String>,
//                response: Response<String>
//            ) {
//
//                //Data= g.fromJson(response.body(),Example.class);
//                Log.e("Gson: ", response.body())
//                if (response.isSuccessful) {
//
////                    var routes: List<List<HashMap<String,String>>>?=null
//                    try {
//                        val jObject = JSONObject(response.body()!!)
//                        val parser = DirectionsJSONParser()
//                        val routes: MutableList<MutableList<java.util.HashMap<String, String>>>? = parser.parse(jObject)
//
//                        dibujarLineas(routes!!)
////                            mapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(
////                                     LatLngBounds.Builder()
////                                            .include( LatLng(origin.latitude(), origin.longitude()))
////                                            .include( LatLng(destination.latitude(), destination.longitude()))
////                                            .build(), 50), 3)
//
//                        val cameramove= LatLngBounds(origin,dest)
//                        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(cameramove,50))
//                        // googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cameramove.center,15f))
//
//                    } catch (e: Exception) {
//                        Log.e("Gson: ", e.message)
//                        e.printStackTrace()
//                    }
//                } else {
//                    Toast.makeText(
//                        context,
//                        "Error al traer data: " + response.code(),
//                        Toast.LENGTH_LONG
//                    ).show()
//                    return
//                }
//            }
//
//            override fun onFailure(
//                call: Call<String>,
//                t: Throwable
//            ) {
//            }
//        })
//    }
//    fun dibujarLineas(result: MutableList<MutableList<java.util.HashMap<String, String>>>): PolylineOptions {
//
//        var points: ArrayList<LatLng> = ArrayList()
//        var lineOptions: PolylineOptions? = PolylineOptions()
//        for (i in result) {
//            Log.e("result.indices: ", "$i  ${i.size}")
//            for (j in i) {
//                val lat = j["lat"]!!.toDouble()
//                val lng = j["lng"]!!.toDouble()
//                val position =
//                    LatLng(lat, lng)
//
//                points.add(position)
//                Log.e("path.indices: ", position.toString())
//            }
//            lineOptions!!.addAll(points)
//            lineOptions.width(12f)
//            lineOptions.color(Color.BLACK)
//            lineOptions.geodesic(true)
//
//        }
//        googleMap.addPolyline(lineOptions!!)
//        return lineOptions!!
//    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap=p0!!
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        googleMap!!.isMyLocationEnabled=true
        googleMap!!.uiSettings.isMyLocationButtonEnabled=false
        moveMapCamera()

    }
    private fun moveMapCamera() {

        val center = CameraUpdateFactory.newLatLng( LatLng(-15.834, -70.019))
        val zoom = CameraUpdateFactory.zoomTo(18f)

        googleMap!!.moveCamera(center)
        googleMap!!.animateCamera(zoom)
    }

}
