package com.evans.technologies.usuario.fragments

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.*
import android.graphics.Camera
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.LinearInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.liveData
import androidx.lifecycle.observe
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.evans.technologies.usuario.Activities.MainActivity
import com.evans.technologies.usuario.R
import com.evans.technologies.usuario.Retrofit.RetrofitClient
import com.evans.technologies.usuario.Retrofit.RetrofitClientMaps
import com.evans.technologies.usuario.Utils.*
import com.evans.technologies.usuario.Utils.Adapters.adapter_spinner_pay_tipe
import com.evans.technologies.usuario.Utils.DirectionsHelper.DirectionsJSONParser
import com.evans.technologies.usuario.Utils.DirectionsHelper.TaskLoadedCallback
import com.evans.technologies.usuario.Utils.Services.cronometro
import com.evans.technologies.usuario.Utils.constans.AppConstants.*
import com.evans.technologies.usuario.Utils.timeCallback.ComunicateFrag
import com.evans.technologies.usuario.Utils.timeCallback.updateListenerNotifications
import com.evans.technologies.usuario.fragments.change_password.set_codigo
import com.evans.technologies.usuario.model.*
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.dialog_fin_viaje_dialog.*
import kotlinx.android.synthetic.main.dialog_nuevo_comentario.*
import kotlinx.android.synthetic.main.dialog_origin_dest_position_marker.*
import kotlinx.android.synthetic.main.dialog_precio_layout.*
import kotlinx.android.synthetic.main.dialog_transcurso_viaje_layout.*
import kotlinx.android.synthetic.main.fragment_mapa_inicio.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendAtomicCancellableCoroutine
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.List
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.math.abs
import kotlin.math.atan

/**
 * A simple {@link Fragment} subclass.
 */
@ExperimentalStdlibApi
class mapaInicio : Fragment(), com.google.android.gms.maps.OnMapReadyCallback,TaskLoadedCallback {
//, PermissionsListener
    val TAG:String = "Mapa Inicio"
    lateinit var  viewModel :viewModelMain
    //Bots
//    lateinit var bot1:Marker
//    lateinit var  bot2:Marker
//    lateinit var  bot3:Marker
//    lateinit var  bot4:Marker
    val  GPS:Int = 51
    //Datos de lpos Precios

//    val camera=false
    //Bindeos Mapa principal//////////////////////////////////////////////////////////////////

//    lateinit var marcador:ImageView
    //Bindeos dialog destinos//////////////////////////////////////////////////////////////////

    //  @BindView(R.id.dialog_fin_viaje_spinner)
    // Spinner dfv_spinner;

    lateinit var countDownTimer:CountDownTimer
//    lateinit var tarea:Runnable
    val uno=true
    //Bindeos Dialog Precio///////////////////////////////////////////////////////////////////////
    lateinit var segundos:String
    //markers
    lateinit var geo:Geocoder
     var origen: Marker?=null
     var destino:Marker?=null
    lateinit var fusedLocationProviderClient:FusedLocationProviderClient //Ultima Ubicacion
//    lateinit var location:Location
//    lateinit var locationCallback:LocationCallback //ACtualizar posicion
//    lateinit var dato:LatLng
    lateinit var refConexionDriverCoor:DatabaseReference
    var adres:List<Address>?=null
    lateinit var conexionDriver:ValueEventListener
    //Geo Fire

    //Line

    //Mapa Viajes
    //Boton View Comnet
    lateinit var bottomSheetBehavior:BottomSheetBehavior<View>
    //Viajes transcurso
    //Precio
    //Fin viaje

     var DriverOptions:Marker?=null
    //Place autocomplete Fragment
    /*
    @BindView(R.id.autocomplete_frament_text)
    PlaceAutocompleteFragment autocomplete_frament_text;
    autocomplete_frament_text.setOnPlaceSelectedListener

    destination= plac.getAdrdreess().toString
    */

    //Mapbox
    //lateinit var  mapboxMap:MapboxMap
    // variables for adding location layer
//    lateinit var  permissionsManager:PermissionsManager
//    lateinit var   locationComponent:LocationComponent
//    lateinit var   currentRoute:DirectionsRoute
//    lateinit var   routeCoordinateList:List<Point>
//    lateinit var   routeCoordinateList2:List<Point>
//    lateinit var   routeCoordinateList3:List<Point>
//    lateinit var   routeCoordinateList4:List<Point>
//    var   markerLinePointList : ArrayList<Point> = ArrayList<Point>()
//    var   pointSource:GeoJsonSource
    var   currentAnimator:Animator?=null
     var   routeIndex:Int=0
    // private GeoJsonSource lineSource;

//    var  navigationMapRoute:NavigationMapRoute?=null
//
//    lateinit var  geocoder:Geocoder
    //lateinit var  address:List<Address>
    //Mapbox
//    val valor:Int=1
    val  DOT_SOURCE_ID1:String = "dot-source-id1"
//     val count:Int = 0
//    lateinit var  handler:Handler
//    lateinit var  cordenadasBots_Runable:Runnable
//    var  coordenadasBots:Handler= Handler()
    var coordenadasDriver:Handler= Handler()
//    var handler_internet:Handler =  Handler()
    val  TIEMPO:Int=4000

    //Dta
//    lateinit var startAdress:String
//    lateinit var endAdress:String
    lateinit var travelPrecio:String
    lateinit var receiver:BroadcastReceiver
    lateinit var datadriver:SharedPreferences
    lateinit var prefs:SharedPreferences
//    lateinit var iconFactory:IconFactory
//    lateinit var actualiza_cada_cierto_tiempo_Coordenadas:LatLng

    lateinit var comunicateFrag:updateListenerNotifications
    var cordenadasDriver_Runable:Runnable?=null


    var originLatlng : LatLng?=null
    var destinationLatlng :LatLng?=null
    var status_btn_origin:Boolean=false
    var status_btn_dest:Boolean=false
    val  imagenes_Spinner= listOf(R.drawable.money,R.drawable.sale)
    val nombres_Spinner= listOf("Pago en efectivo","Pago con cupón")
//    public mapaInicio(String id,String token) {
//        this.id=id;
//        this.token=token;
//    }
    lateinit var googleMap: GoogleMap
    private var currentPoline:Polyline?=null
    private var isMoving:Boolean=false
    private var circleRadius:Int?=null
    //Bots
    var v:Float?=null
    var handler:Handler?=null
    var startPositionB:LatLng?=null
    var endPositionB:LatLng?=null
    var currentPositionB:LatLng?=null
    var index:Int?=null
    var next:Int?=null
    private var botMarker:Marker?=null
    var drawPathRunnable:Runnable?=null
    var blackPolilyne:Polyline?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Mapbox.getInstance(requireContext() , getString(R.string.access_token))

        return inflater.inflate(R.layout.fragment_mapa_inicio, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = requireActivity().run {
            ViewModelProviders.of(this)[viewModelMain::class.java]
        }
        bottomSheetBehavior= BottomSheetBehavior.from( crear_comentario_layout_dialog )
        datadriver = requireContext().getSharedPreferences("datadriver", Context.MODE_PRIVATE)
        prefs = requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        setClaseActual(prefs, mapaInicio().toString())

        try{
            val vista = requireActivity().currentFocus
            vista!!.clearFocus()
            if (view != null) {
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }catch ( e:Exception){

        }

        mi_imgbtn_next_step.setOnClickListener{
            fmi_progressbar.visibility = View.VISIBLE
            funcionObtenerPrecio()
        }

        //iconFactory = IconFactory.getInstance(requireContext())
        //Maps google
        mapView.onCreate( savedInstanceState )
        mapView.onResume()
        mapView.getMapAsync(this)
        dialog_fin_viaje_imgbtn_message.setOnClickListener{
            dialog_fin_viaje_imgbtn_message_notify.visibility = View.GONE
            comunicateFrag.updateNotificatones(true)
        }
        dialog_fin_viaje_imgbtn_delete.setOnClickListener{
            deleteOnclickTrip()
        }
        dialog_precio_select.setOnClickListener{
            fmi_progressbar.visibility = View.VISIBLE
            dialog_precio_select.isEnabled = false
            val date =  Date()
            val hourFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
//            Log.e("calles",'\n'+getUserId_Prefs(prefs)+'\n'+getOrigenLat(datadriver)+'\n'+getOrigenLong(datadriver)
//                    +'\n'+getDestinoLat(datadriver)+'\n'+getDestinoLong(datadriver)+'\n'+
//                    getStartAddress(datadriver).replace(" ","-")+'\n'+getEndAddress(datadriver).replace(" ","-")
//                    +'\n'+hourFormat.format(date)+'\n'+getPriceShared(datadriver)+'\n'+"Puno")

            val call = RetrofitClient.getInstance()
                .api.requestDriver(getUserId_Prefs(prefs)!!,getOrigenLat(datadriver)!!,getOrigenLong(datadriver)!!
                    ,getDestinoLat(datadriver)!!,getDestinoLong(datadriver)!!,getStartAddress(datadriver)!!.replace(" ","-"),getEndAddress(datadriver)!!.replace(" ","-"),hourFormat.format(date)+"",
                    ( getPriceShared(datadriver)!!.toDouble()-getPriceSharedDiscount(datadriver)!!.toDouble()).toString(),getPriceSharedDiscount(datadriver)!!,"PUNO")
            call.enqueue(object: Callback<user> {
                @Override
                override fun onResponse( call:Call<user>,  response:Response<user>) {
                    if(response.isSuccessful){
                        setViajeId(datadriver,response.body()!!.viajeId)
                        ////fmi_time.setVisibility(View.VISIBLE);
                        // ejecutarCronometro();
                        search_imagen.visibility = View.VISIBLE
                        setEstadoViews(datadriver,4)
                        cuarto_estado()
                        change_priceSpinner()
                        fmi_progressbar.visibility = View.GONE
                        dialog_precio_select.text = "Esperando evans"
                        try {
                            requireContext().stopService( Intent(requireContext(), cronometro::class.java))
                        }catch ( e:Exception){

                        }
                        try {
                            requireContext().startService( Intent(requireContext(), cronometro::class.java))
                        }catch ( e:Exception){

                        }
                        Log.e("paso prueba","Felicidades"+response.body()!!.viajeId)

                    }else{
                        dialog_precio_select.isEnabled = true
                        fmi_progressbar.visibility = View.GONE
                        activity!!.toastLong("Las movilidades cercanas se encuentran ocupadas.")
                        Log.e("paso prueba","${response.code()}  ${response.message()}")
                    }

                }

                @Override
                override fun onFailure( call:Call<user>,  t:Throwable) {
                    Log.e("errorFaire",t.message)
                    dialog_precio_select.isEnabled = true
                    fmi_progressbar.visibility = View.GONE
                    activity!!.toastLong("Solicite nuevamente el viaje")
                }
            })


        }

        //marcador=view.findViewById(R.id.mapa_marker_center)
        //bindeos dialog pedido

        dodpm_imgbtn_status_dest.setOnClickListener{
            dodpm_imgbtn_status_dest.isSelected = !it.isSelected
            if (dodpm_imgbtn_status_dest.isSelected){
                val center = googleMap.cameraPosition.target
                status_btn_dest=true
                try {
                    adres = geo.getFromLocation(center.latitude,center.longitude,1)  as List<Address>
                    val direccion3 = (adres!!.get(0).getAddressLine(0)).split(",")
                    dodpm_edtxt_dest.setText(direccion3[0])
                    dodpm_edtxt_dest.isEnabled = false
                    dodpm_edtxt_dest.setTextColor(requireContext().getColor(R.color.plomo))

                } catch ( e:IOException) {
                    e.printStackTrace()
                    Log.e("recoger View",e.message)
                }
                setDestino(datadriver,center.latitude.toString(),center.longitude.toString())

//                destino=googleMap.addMarker( MarkerOptions()
//                    .setIcon(iconFactory.fromResource(R.drawable.logo22))
//                    .setPosition( LatLng(getDestinoLat(datadriver)!!.toDouble(),getDestinoLong(datadriver)!!.toDouble()))
//                    .title("Destino"))
                destino=googleMap.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.logo22))
                    .title("Destino")
                    .position( LatLng(getDestinoLat(datadriver)!!.toDouble(),getDestinoLong(datadriver)!!.toDouble())))
                if (status_btn_origin){
                    mi_imgbtn_next_step.isEnabled = true
                    mi_imgbtn_next_step.setBackgroundColor(requireActivity().resources.getColor(R.color.black))
                    mi_imgbtn_next_step.text = "CALCULAR TARIFA"

                }
            } else{
                mi_imgbtn_next_step.isEnabled = false
                mi_imgbtn_next_step.setBackgroundColor(Color.GRAY)
                mi_imgbtn_next_step.text = "CALCULAR TARIFA"
                dodpm_edtxt_dest.isEnabled = true
                dodpm_edtxt_dest.setTextColor(requireContext().getColor(R.color.black))
                status_btn_dest=false
                destino!!.remove()
            }


        }
        dodpm_imgbtn_status_origin.setOnClickListener{
            dodpm_imgbtn_status_origin.isSelected = !it.isSelected
            if (dodpm_imgbtn_status_origin.isSelected){
                val center = googleMap.cameraPosition.target
                status_btn_origin=true
                try {
                    adres = geo.getFromLocation(center.latitude,center.longitude,1) as List<Address>
                    val direccion3 = (adres!!.get(0).getAddressLine(0)).split(",")
                    dodpm_edtxt_origin.setText(direccion3[0])
                    dodpm_edtxt_origin.isEnabled = false
                    dodpm_edtxt_origin.setTextColor(requireContext().getColor(R.color.plomo))
                } catch ( e:IOException) {
                    e.printStackTrace()
                    Log.e("recoger View",e.message)
                }
                setOrigen(datadriver,center.latitude.toString() ,center.longitude.toString())
                origen=googleMap.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.logo33))
                    .title("Origen")
                    .position( LatLng(getOrigenLat(datadriver)!!.toDouble(),getOrigenLong(datadriver)!!.toDouble())))
//                origen=mapboxMap.addMarker( MarkerOptions()
//                    .setIcon(iconFactory.fromResource(R.drawable.logo33))
//                    .setPosition( LatLng(getOrigenLat(datadriver)!!.toDouble(),getOrigenLong(datadriver)!!.toDouble()))
//                    .title("Origen"))
                if (status_btn_dest){
                    mi_imgbtn_next_step.isEnabled = true

                    mi_imgbtn_next_step.setBackgroundColor(requireActivity().resources.getColor(R.color.black))
                    mi_imgbtn_next_step.text = "CALCULAR TARIFA"

                }
            } else{
                mi_imgbtn_next_step.isEnabled = false
                mi_imgbtn_next_step.setBackgroundColor(Color.GRAY)
                mi_imgbtn_next_step.text = "CALCULAR TARIFA"
                dodpm_edtxt_origin.isEnabled = true
                dodpm_edtxt_origin.setTextColor(requireContext().getColor(R.color.black))
                status_btn_origin=false
                origen!!.remove()
            }

        }
        dodpm_edtxt_dest.setOnClickListener{

        }
        dodpm_edtxt_origin.setOnClickListener{}
        dialog_transcurso_destino_ib_chat.setOnClickListener{
            dialog_fin_viaje_imgbtn_message_notify.visibility = View.GONE
            comunicateFrag.updateNotificatones(true)
        }
        //bindeos dialog precio




        //temporal=evanscarga;
        ////////////////////////////////////////////////////////


        //Dialog trancurso
        dialog_transcurso_destino_btn_cancelar.setOnClickListener{
            deleteOnclickTrip()
        }
        //Dialog Fin de Viaje
        crear_comentario_publicar.setOnClickListener{
            refConexionDriverCoor.removeEventListener(conexionDriver)
            if (crear_comentario_rating!=null|| (crear_comentario_rating.rating.toString().equals(0.0) ) ){
                val comentar=RetrofitClient.getInstance().api.calificarDriver(getDriverId(datadriver)!!,
                    crear_comentario_comentario.text.toString(),
                    crear_comentario_rating.rating.toString(),getUserId_Prefs(prefs)!!,getUserName(prefs)!!)
                comentar.enqueue(object: Callback<getPrice> {
                    @Override
                    override fun onResponse( call:Call<getPrice>,  response:Response<getPrice>) {
                        Log.e("rating","enviado"+response.code()+"  "+crear_comentario_rating.rating)
                        if (response.isSuccessful){
                            comunicateFrag.removeChatConexion()
                            if (!(getApiWebVersion(prefs).equals(requireContext().getVersionApp()))){
                                requireActivity().getViewUpdateVersion(requireContext())
                            }
                            crear_comentario_layout_dialog.visibility = View.GONE
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                            mapa_recoger_cliente.visibility = View.VISIBLE
                            aic_button_comentar_hide.visibility = View.GONE
                            Log.e("estado_delete","btn publicar")
                            borrarSharedPreferencesDataDriver(1)
                            primer_estado()
                        }

                    }

                    @Override
                    override fun onFailure( call:Call<getPrice>,  t:Throwable) {

                    }
                })

            }
        }
        //DIalog comentarios


        dialog_precio_img_btn_atras.setOnClickListener{
            Log.e("estado_delete","btn atras")
            Log.e("botonatras", getEstadoView(datadriver)!!.toString())
            if (getViajeId(datadriver).equals("nulo")){
                borrarSharedPreferencesDataDriver(1)
                primer_estado()
            }else{
                val comando=RetrofitClient.getInstance().api.puStatusTrip(getViajeId(datadriver)!!,true,false,false,false)
                comando.enqueue(object: Callback<user> {
                    @Override
                    override fun onResponse(call :Call<user> ,  response:Response<user>) {

                        borrarSharedPreferencesDataDriver(1)
                        primer_estado()
                        // if (countDownTimer!=null)
                        //    countDownTimer.onFinish();
                        requireContext().stopService( Intent(requireContext(), cronometro::class.java))

                    }

                    @Override
                    override fun onFailure( call:Call<user>,  t:Throwable) {

                    }
                })


            }
            originLatlng=null
            destinationLatlng=null
        }
        aic_button_comentar_hide.setOnClickListener{
            if (!(getApiWebVersion(prefs).equals(requireContext().getVersionApp()))){
                requireActivity().getViewUpdateVersion(requireContext())
            }
            comunicateFrag.removeChatConexion()
            crear_comentario_layout_dialog.visibility = View.GONE
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            mapa_recoger_cliente.visibility = View.VISIBLE
            aic_button_comentar_hide.visibility = View.GONE
            terminarTrip()
            Log.e("estado_delete","btn publicar")
            borrarSharedPreferencesDataDriver(1)
            primer_estado()
        }
        bottomSheetBehavior.setBottomSheetCallback( object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState)
                {
                     BottomSheetBehavior.STATE_HIDDEN->{
                         //                        linearLayout.setVisibility(View.GONE);
//                        bottomSheetBehavior.setState( BottomSheetBehavior.STATE_HIDDEN );
//                        mapa_recoger_cliente.setVisibility(View.VISIBLE);
//                        aic_button_comentar_hide.setVisibility( View.GONE );
//                        terminarTrip();
//                        Log.e("estado_delete","btn publicar");
//                        borrarSharedPreferencesDataDriver(1);
//                        primer_estado();
                     }

                     BottomSheetBehavior.STATE_COLLAPSED->{
                         if (!(getApiWebVersion(prefs).equals(requireContext().getVersionApp()))){
                             activity!!.getViewUpdateVersion(requireContext())
                         }
                         comunicateFrag.removeChatConexion()
                         refConexionDriverCoor.removeEventListener(conexionDriver)
                         crear_comentario_layout_dialog.visibility = View.GONE
                         bottomSheetBehavior.setState( BottomSheetBehavior.STATE_HIDDEN )
                         mapa_recoger_cliente.visibility = View.VISIBLE
                         aic_button_comentar_hide.visibility = View.GONE
                         terminarTrip()
                         Log.e("estado_delete","btn publicar")
                         borrarSharedPreferencesDataDriver(1)
                         primer_estado()
                     }

                }
            }

        } )

        gpsEnable()
        dialog_fin_viaje_btn_aceptar.setOnClickListener{
            when(getEstadoView(datadriver)!!){
                6->{
                    val llamada = RetrofitClient.getInstance().api
                        .userTOdriver(getUserId_Prefs(prefs)!!,getDriverId(datadriver)!!,"Pasajero-Esperando","Pasajero-Subio-abordo","subioabordo")
                    llamada.enqueue(object: Callback<user> {
                        @Override
                       override fun onResponse( call:Call<user>,  response:Response<user>) {
                            if (response.isSuccessful){
                                setEstadoViews(datadriver,7)
                                septimo_estado()
                            }else{
                                Log.e("dialog_fin_viaje",response.message()+response.code())
                            }
                        }

                        @Override
                        override fun onFailure( call:Call<user>,  t:Throwable) {

                        }
                    })
                }
                8->{
                    val termianrViaje = RetrofitClient.getInstance().api
                        .userTOdriver(getUserId_Prefs(prefs)!!,getDriverId(datadriver)!!
                            ,( getPriceShared(datadriver)!!.toDouble()- getPriceSharedDiscount(datadriver)!!.toDouble()).toString(),
                            getPriceSharedDiscount(datadriver)!!,"viajeterminado")
                    termianrViaje.enqueue(object: Callback<user> {
                        @Override
                        override fun onResponse( call:Call<user>,  response:Response<user>) {
                            if (response.isSuccessful) {
                                //  change_priceSpinner();
                                setEstadoViews(datadriver,9)
                                noveno_estado()
                            }
                        }

                        @Override
                        override fun  onFailure( call:Call<user>,  t:Throwable) {

                        }
                    })


                }
            }
        }
        mapa_icon_gpsaa.setOnClickListener {
            val location=this.googleMap.myLocation
            if (location!=null){
                val target=LatLng(location.latitude,location.longitude)
                val builder= CameraPosition.builder()
                builder.zoom(16f)
                builder.target(target)
                this.googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()))
            }
        }
        receiver =  object:BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                try {
                    val datassss=intent!!.extras!!.getString("data")

                    if (datassss!!.contains("{cronometroFinalizado}")){
                        search_imagen.visibility = View.GONE
                        tercer_estado()

                        val comando= RetrofitClient.getInstance().api.puStatusTrip(getViajeId(datadriver)!!,
                            tripCancell = true,
                            tripAccepted = false,
                            tripInitiated = false,
                            tripFinalized = false
                        )
                        comando.enqueue(object : Callback<user> {
                            override fun onFailure(call: Call<user>, t: Throwable) {

                            }

                            override fun onResponse(call: Call<user>, response: Response<user>) {

                            }

                        })

                    }else {
                        try{

                        }catch ( e:Exception){

                        }
                        ejecutar_tarea_notificaciones_data(datassss)
                    }

                } catch ( e:Exception) {
                    Log.e("data recibida ",e.message!!)
                    e.printStackTrace()
                }

            }
        }
        ( requireActivity() as MainActivity).updateApi(ComunicateFrag.mapa_inicio {
            dialog_fin_viaje_imgbtn_message_notify.visibility = View.VISIBLE
        })
        // if (segundos!="0")

        comprobarStatusTrip()
        /*SpinnerAdapter dfv_spinner_customAdapter=new adapter_spinner_pay_tipe(requireContext(),imagenes_Spinner,nombres_Spinner,R.color.white);
        dfv_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              //  ((TextView) parent.getChildAt(0)).setTextColor();
                ((TextView)view.findViewById(R.id.textView)).setTextColor(requireContext().getColor(R.color.white));

                switch (position){
                    case 0:
                        precio.setText("S./ "+ getPriceShared(datadriver));
                        setPriceSharedDiscount(datadriver,"0.0");
                        break;
                    case 1:

                        if (!getAccountActivate(prefs)){
                            dfv_spinner.setSelection(0);
                            dfv_spinner.setSelection(0,true);
                            if (DialogCreate("Para obtener y usar cupones es necesario activar su cuenta \n ¿Desea activar su cuenta?")){
                                enviarMensajeCorreo();
                            }

                        }else{
                            if (getPriceSharedDiscount(datadriver).equals("0.0")){
                                Log.e("cupon",getUserId_Prefs(prefs)+"     "+getPriceShared(datadriver));
                                Call<getPrice> discount=RetrofitClient.getInstance().getApi().getPriceDiscount(getUserId_Prefs(prefs),getPriceShared(datadriver));
                                discount.enqueue(new Callback<getPrice>() {
                                    @Override
                                    public void onResponse(Call<getPrice> call, Response<getPrice> response) {
                                        if (response.isSuccessful()){
                                            if (response.body()!!.getCashPrice().equals(getPriceShared(datadriver))){
                                                Log.e("cupon",response.body()!!.getCashPrice());
                                                activity!!.toastLong(getActivity(),"NO HAY CUPONES DISPONIBLES");
                                                setPriceSharedDiscount(datadriver,"0.0");
                                                dfv_spinner.setSelection(0);

                                            }else{
                                                setPriceSharedDiscount(datadriver,Double.parseDouble(getPriceShared(datadriver))-Double.parseDouble(response.body()!!.getCashPrice())+"");
                                                precio.setText("S./ "+  (Double.parseDouble(getPriceShared(datadriver))- Double.parseDouble(getPriceSharedDiscount(datadriver)))+"");
                                            }
                                        }else{

                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<getPrice> call, Throwable t) {
                                        activity!!.toastLong(getActivity(),"NO HAY CUPONES DISPONIBLES");
                                    }
                                });

                            }else{
                                if (getPriceSharedDiscount(datadriver).contains("0.0")){
                                    activity!!.toastLong(getActivity(),"NO HAY CUPONES DISPONIBLES");
                                }else{
                                    precio.setText("S./ "+ getPriceSharedDiscount(datadriver));
                                }

                            }
                        }


                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        // dfv_spinner.setBackgroundColor(requireContext().getColor(R.color.black));
        //  spinner.setOnItemSelectedListener(listener);
        // dfv_spinner.setAdapter(dfv_spinner_customAdapter);
        var spinner_customAdapter= adapter_spinner_pay_tipe(requireContext(),
            imagenes_Spinner, nombres_Spinner ,R.color.black)
        dialog_precio_spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position){
                    0->{
                        dialog_precio_text_view_precio.text = "S./ "+ getPriceShared(datadriver)
                        setPriceSharedDiscount(datadriver,"0.0")
                    }
                    1->{
                        if (!getAccountActivate(prefs)){
                            dialog_precio_spinner.setSelection(0)
                            dialog_precio_spinner.setSelection(0,true)

                            if (DialogCreate("Para usar cupones es necesario activar su cuenta \n ¿Desea activar su cuenta?")){

                            }

                        }else{
                            if (getPriceSharedDiscount(datadriver).equals("0.0")){
                                Log.e("cupon",getUserId_Prefs(prefs)+"     "+getPriceShared(datadriver))
                                val discount=RetrofitClient.getInstance().api.getPriceDiscount(getUserId_Prefs(prefs)!!,getPriceShared(datadriver)!!)
                                discount.enqueue(object: Callback<getPrice> {
                                    @Override
                                    override fun onResponse( call:Call<getPrice>,  response:Response<getPrice>) {
                                        if (response.isSuccessful){
                                            if (response.body()!!.cashPrice == getPriceShared(datadriver)){
                                                Log.e("cupon", response.body()!!.cashPrice)
                                                activity!!.toastLong("NO HAY CUPONES DISPONIBLES")
                                                setPriceSharedDiscount(datadriver,"0.0")
                                                dialog_precio_spinner.setSelection(0)
                                                if (!getAccountActivate(prefs)){
                                                    if (DialogCreate("Para obtener y usar cupones es necesario activar su cuenta \n ¿Desea activar su cuenta?")){
                                                        enviarMensajeCorreo()
                                                    }
                                                }
                                            }else{
                                                setPriceSharedDiscount(datadriver,(getPriceShared(datadriver)!!.toDouble()- response.body()!!.cashPrice.toDouble() ).toString())
                                                dialog_precio_text_view_precio.text = "S./ "+   (getPriceShared(datadriver)!!.toDouble()- getPriceSharedDiscount(datadriver)!!.toDouble())
                                            }
                                        }else{

                                        }

                                    }

                                    @Override
                                    override fun onFailure( call:Call<getPrice>,  t:Throwable) {
                                        activity!!.toastLong("NO HAY CUPONES DISPONIBLES")
                                    }
                                })

                            }else{
                                if (getPriceSharedDiscount(datadriver)!!.contains("nulo")){
                                    activity!!.toastLong("NO HAY CUPONES DISPONIBLES")
                                    dialog_precio_spinner.setSelection(0)
                                    if (!getAccountActivate(prefs)){
                                        if (DialogCreate("Para obtener y usar cupones es necesario activar su cuenta \n ¿Desea activar su cuenta?")){
                                            enviarMensajeCorreo()
                                        }
                                    }
                                }else{
                                    dialog_precio_text_view_precio.text = "S./ "+ getPriceSharedDiscount(datadriver)
                                }

                            }

                        }

                    }

                }
            }

        }

        dialog_precio_spinner.adapter = spinner_customAdapter
    }

    private fun deleteOnclickTrip() {
        if (DriverOptions!!!=null)
            DriverOptions!!.remove()
        originLatlng=null
        destinationLatlng=null
        val dialogClickListener: DialogInterface.OnClickListener =
           DialogInterface.OnClickListener { dialog, which ->
               when(which){
                   DialogInterface.BUTTON_POSITIVE->{
                       val comando=RetrofitClient.getInstance().api.puStatusTrip(getViajeId(datadriver)!!,true,false,false,false)
                       comando.enqueue(object: Callback<user> {
                           @Override
                           override fun onResponse( call:Call<user>,  response:Response<user>) {
                               if (response.isSuccessful){
                                   Log.e("Viaje aceptado",response.message())
                                   comunicateFrag.removeChatConexion()
                                   cancel_viaje_noti()
                               }
                               refConexionDriverCoor.removeEventListener(conexionDriver)
                               //Log.e("Viaje aceptado",response.code()+" "+getViajeId(datadriver)+" "+response.message())
                           }

                           @Override
                           override fun onFailure( call:Call<user>,  t:Throwable) {

                           }
                       })
                   }
               }

           }

        val builder:AlertDialog.Builder =  AlertDialog.Builder(requireContext())
        builder.setMessage("¿Esta seguro de querer cancelar el viaje?").setPositiveButton("Si", dialogClickListener)
            .setNegativeButton("No", dialogClickListener).show()


    }


    private fun enviarMensajeCorreo() {
        val enviarCode = RetrofitClient.getInstance().api.enviarCorreo_validate(
                getUserEmail(prefs)!!)
        enviarCode.enqueue(object: Callback<user> {
            @Override
            override fun onResponse( call:Call<user>,  response:Response<user>) {
                activity!!.toastLong( "error"+response.code())
                if( response.isSuccessful){
                    activity!!.toastLong("Se envio un mensaje a tu correo")
                    val manager = activity!!.supportFragmentManager
                    manager.beginTransaction().replace(
                            R.id.main_layout_change_fragment,
                            set_codigo(true, false)
                    ).commit()
                }
            }

            @Override
            override fun onFailure( call:Call<user>,  t:Throwable) {
                activity!!.toastLong( "error   f ${t.message}")
            }
        })
    }

    private fun comprobarStatusTrip() {
        if (getViajeId(datadriver)!! != "nulo"){
            val  statusTrip=RetrofitClient.getInstance().api.getStatusTrip(getViajeId(datadriver)!!)
            statusTrip.enqueue(object: Callback<infoDriver> {
                @Override
                override fun onResponse( call:Call<infoDriver>,  response:Response<infoDriver>) {
                    try{
                        if ( !(response.body()!!.isOk)){
                            borrarSharedPreferencesDataDriver(1)
                            primer_estado()
                            originLatlng=null
                            destinationLatlng=null

                            setChatJson(datadriver,"nulo")
                         /*   Intent intent = new Intent("subsUnsubs");
                            intent.putExtra("subs", "chat");
                            intent.putExtra("subsUnsubs",false);
                            LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(requireContext());
                            broadcaster.sendBroadcast(intent);*/
                        }
                    }catch( e:Exception){

                    }
                }
                @Override
                override fun onFailure(call:Call<infoDriver> ,  t:Throwable) {

                }
            })

        }

    }

    override fun onAttach(activity: Activity) {
        try {
            comunicateFrag =  requireActivity() as updateListenerNotifications
        } catch ( e:ClassCastException) {
            throw  ClassCastException(context.toString() + " must implement OnFragmentInteractionListener")
        }
        super.onAttach(activity)
    }

//    private fun  init_bot_driver( ruta:String){
//        Log.e("rutabot",ruta)
//
//        // new LoadGeoJson(mapaInicio.this,ruta).execute();
//        /*mapboxMap.getStyle(new Style.OnStyleLoaded() {
//            @Override
//            public void onStyleLoaded(@NonNull Style style) {
//                DirectionsRoute route=DirectionsRoute.fromJson(ruta);
//                initData(style,FeatureCollection.fromFeature(
//                        Feature.fromGeometry(LineString.fromPolyline(route.geometry(), PRECISION_6))));
//            }
//        });*/
//
//        //
//        //List<Point> routeCoordinateList=PolylineUtils.decode((route.geometry()),6);
//        //initRunnable(routeCoordinateList);
//    }
//    private fun initData( fullyLoadedStyle:Style, @NonNull  featureCollection:FeatureCollection) {
//        if (featureCollection.features() != null) {
//            val lineString = ( featureCollection.features()!![0].geometry()) as LineString
//            routeCoordinateList = lineString.coordinates() as List<Point>
//            initSources(fullyLoadedStyle, featureCollection)
//            initSymbolLayer(fullyLoadedStyle)
//            //initDotLinePath(fullyLoadedStyle);
//            animate()
//        }
//    }

    /**
     * Set up the repeat logic for moving the icon along the route.
     */
   /* private void updatePositionMarquers(Point p1,Point p2,Point p3,Point p4){
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_directions_car_black_24dp, null);
        Bitmap mBitmap = BitmapUtils.getBitmapFromDrawable(drawable);
        if (bot1!=null){
            bot1.remove();
        }
        if (bot2!=null){
            bot2.remove();
        }
        if (bot3!=null){
            bot3.remove();
        }
        if (bot4!=null){
            bot4.remove();
        }
        cordenadasBots_Runable=new Runnable() {
            @Override
            public void run() {
                if (p1!=null){
                    iconFactory = IconFactory.getInstance(requireContext());
                    bot1 =mapboxMap.addMarker(new MarkerOptions()
                            .setIcon(iconFactory.fromResource(R.drawable.ic_directions_car_black_24dp))
                            .setPosition(new LatLng(p1.latitude(),p1.longitude()))
                            .title("Destino"));
                }
                if (p2!=null){
                    bot2=mapboxMap.addMarker(new MarkerOptions()
                            .setIcon(iconFactory.fromResource(R.drawable.ic_directions_car_black_24dp))
                            .setPosition(new LatLng(p2.latitude(),p2.longitude()))
                            .title("Destino"));

                }
                if (p3!=null){

                    bot3=mapboxMap.addMarker(new MarkerOptions()
                            .setIcon(iconFactory.fromResource(R.drawable.ic_directions_car_black_24dp))
                            .setPosition(new LatLng(p3.latitude(),p3.longitude()))
                            .title("Destino"));

                }
                if (p4!=null){
                    bot4=mapboxMap.addMarker(new MarkerOptions()
                            .setIcon(iconFactory.fromResource(R.drawable.ic_directions_car_black_24dp))
                            .setPosition(new LatLng(p4.latitude(),p4.longitude()))
                            .title("Destino"));
                }
                animarMarker();
                coordenadasBots.postDelayed(this,4000);
            }
        };
        coordenadasBots.postDelayed(cordenadasBots_Runable,0);


    }*/

    /*private void animarMarker(){
        Point p1 = null;
        Point p2 = null;
        Point p3 = null;
        Point p4 = null;

        if ((routeCoordinateList1.size() - 1 > routeIndex[0])) {
            p1 = routeCoordinateList1.get(routeIndex[0]);
            routeIndex[0]++;
        }else{
            bot1.remove();
        }
        if ((routeCoordinateList2.size() - 1 > routeIndex[1])) {
            p2 = routeCoordinateList2.get(routeIndex[1]);
            routeIndex[1]++;
        }else{
            bot2.remove();
        }
        if ((routeCoordinateList3.size() - 1 > routeIndex[2])) {
            p3 = routeCoordinateList3.get(routeIndex[2]);
            routeIndex[2]++;
        }else{
            bot3.remove();
        }
        if ((routeCoordinateList4.size() - 1 > routeIndex[3])) {
            p4 = routeCoordinateList4.get(routeIndex[3]);
            routeIndex[3]++;
        }else{
            bot4.remove();
        }
        if (p1==null&&p2==null&&p3==null&&p4==null){
            coordenadasBots.removeCallbacks(cordenadasBots_Runable);
        }else{
            updatePositionMarquers(p1,p2,p3,p4);
        }

    }*/
//    private fun  animate() {
//
//        if (getEstadoView(datadriver)!!<3){
//            if ((routeCoordinateList.size - 1 > routeIndex!!)) {
//                var indexPoint:Point = routeCoordinateList.get(routeIndex!!)
//                var newPoint:Point = Point.fromLngLat(indexPoint.longitude(), indexPoint.latitude())
//                currentAnimator = createLatLngAnimator(indexPoint, newPoint)
//                currentAnimator!!.start()
//                routeIndex++
//            }else{
//                mapboxMap.getStyle { style ->
//                    style.removeSource(DOT_SOURCE_ID1)
//                    style.removeLayer("symbol-layer-id")
//                    getRouteString()
//                }
//            }
//        }else{
//            mapboxMap.getStyle { style ->
//                style.removeSource(DOT_SOURCE_ID1)
//                style.removeLayer("symbol-layer-id")
//            }
//        }
//
//    }
//    class PointEvaluator:TypeEvaluator<Point>{
//        override fun evaluate(fraction: Float, startValue: Point?, endValue: Point?): Point {
//            return Point.fromLngLat(
//                startValue!!.longitude() + ((endValue!!.longitude() - startValue.longitude()) * fraction),
//                startValue.latitude() + ((endValue.latitude() - startValue.latitude()) * fraction)
//            )
//        }
//
//    }
//    private static class PointEvaluator implements TypeEvaluator<Point> {
//
//        @Override
//        public Point evaluate(float fraction, Point startValue, Point endValue) {
//            return
//        }
//    }

//    private fun createLatLngAnimator( currentPosition:Point,  targetPosition:Point):Animator {
//        val latLngAnimator:ValueAnimator = ValueAnimator.ofObject( PointEvaluator(), currentPosition, targetPosition)
//        // latLngAnimator.setDuration((long) TurfMeasurement.distance(currentPosition, targetPosition, "meters"));
//        latLngAnimator.duration = 5000
//
//        latLngAnimator.interpolator = LinearInterpolator()
//        latLngAnimator.addListener(object: AnimatorListenerAdapter() {
//            @Override
//            override fun onAnimationEnd( animation:Animator) {
//                super.onAnimationEnd(animation)
//                animate()
//            }
//        })
//        latLngAnimator.addUpdateListener { animation ->
//            val point:Point =  animation.animatedValue as Point
//
//           // pointSource.setGeoJson(point)
//            markerLinePointList.add(point)
//            // lineSource.setGeoJson(Feature.fromGeometry(LineString.fromLngLats(markerLinePointList)));
//        }
//
//        return latLngAnimator
//    }
//    private fun  initSources(@NonNull  loadedMapStyle:Style, @NonNull  featureCollection:FeatureCollection) {
//        mapboxMap.getStyle { style ->
//            if (style.getSource(DOT_SOURCE_ID1)==null){
//                loadedMapStyle.addSource(  GeoJsonSource(DOT_SOURCE_ID1, featureCollection))
//            }
//        }
//
//
//        //  loadedMapStyle.addSource(lineSource = new GeoJsonSource(LINE_SOURCE_ID));
//    }
//    private fun initSymbolLayer(@NonNull  loadedMapStyle:Style) {
//        mapboxMap.getStyle { style ->
//            if (style.getLayer("symbol-layer-id")==null){
//                val drawable: Drawable? = ResourcesCompat.getDrawable(resources, R.drawable.ic_directions_car_black_24dp, null)
//                val mBitmap: Bitmap? = BitmapUtils.getBitmapFromDrawable(drawable)
//                loadedMapStyle.addImage("moving-red-marker", mBitmap!!)
//                loadedMapStyle.addLayer( SymbolLayer("symbol-layer-id", DOT_SOURCE_ID1).withProperties(
//                    iconImage("moving-red-marker"),
//                    iconSize(1f),
//                    iconIgnorePlacement(true),
//                    iconAllowOverlap(true)
//                ))
//                //  iconOffset(listOf(5f, 0f) as Array<out Float>),
//
//            }
//        }
//
//    }

    /**
     * Add the LineLayer for the marker icon's travel route. Adding it under the "road-label" layer, so that the
     * this LineLayer doesn't block the street name.
     */

    private fun ejecutar_tarea_notificaciones_data( datassss:String) {
        Log.e("data_recibida"," mapa inicio"+datassss)
        val gson =  Gson()
        var topic:data?=null
        try{

            topic = gson.fromJson(datassss, data::class.java)
            if (topic.response.contains(SEND_NOTIFICATION_VIAJE_ACEPTADO)){
                if (getViajeId(datadriver).equals("nulo")){
                    setDriverId(datadriver,topic.getDriverId())
                    cancel_viaje_noti()
                }else{
                    // if (countDownTimer!=null)
                    //    countDownTimer.onFinish();
                    requireContext().stopService( Intent(requireContext(), cronometro::class.java))


                    search_imagen.visibility = View.GONE
                    ////fmi_time.setVisibility(View.GONE);
                    val  getInfoDriver = RetrofitClient.getInstance()
                            .api.getInfoDriver(topic.getDriverId())
                    getInfoDriver.enqueue(object: Callback<infoDriver> {
                        @Override
                        override fun onResponse( call:Call<infoDriver>,  response:Response<infoDriver>) {
                            if (response.isSuccessful){

                                setInfoDriver(datadriver,"https://evans-img.s3.us-east-2.amazonaws.com/"+response.body()!!.information.driverImg,response.body()!!.information.surname
                                    ,response.body()!!.information.name,
                                        response.body()!!.information.licenseCar,response.body()!!.information.brandCar, response.body()!!.information.modelCar,
                                        response.body()!!.information.colorCar
                                )
                                setDriverId(datadriver,topic.driverId)
                                setEstadoViews(datadriver,5)
                                llaveChat(datadriver, topic.chatId)
                                comunicateFrag.createConexionChat()
                           /* Intent intent = new Intent("subsUnsubs")
                            intent.putExtra("subs", "chat");
                            intent.putExtra("subsUnsubs",true);
                            LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(requireContext());
                            broadcaster.sendBroadcast(intent);*/
                                //ejecutarTarea_ActualizarCordenadasDriver();
                                cargarPosicionDriver()
                                quinto_estado()

//                                mapboxMap.getStyle { style ->
//                                    style.removeSource(DOT_SOURCE_ID1)
//                                    style.removeLayer("symbol-layer-id")
//                                }
                            }
                            else{
                                activity!!.toastLong( "El conductor no tiene informacion, se cancelara el viaje")

                            }
                           // Log.e("error Retrofit22",response.code()+"")
                        }

                        @Override
                        override fun onFailure( call:Call<infoDriver>,  t:Throwable) {

                        }
                    })
                }
            }else if (topic.getResponse().contains(SEND_NOTIFICATION_NOTIFICAR_LLEGADA)){
                sexto_estado()
                setEstadoViews(datadriver,6)


            }else if (topic.getResponse().contains(SEND_NOTIFICATION_VIAJE_INICIADO)){
                setEstadoViews(datadriver,8)
                octavo_estado()
            }else if (topic.getResponse().contains(SEND_NOTIFICATION_PAGO_EXITOSO)){
                comunicateFrag.removeChatConexion()
                setEstadoViews(datadriver,10)
                decimo_estado()
                originLatlng=null
                destinationLatlng=null
            }else if(topic.getResponse() == SEND_NOTIFICATION_VIAJE_CANCELADO){
                comunicateFrag.removeChatConexion()
                refConexionDriverCoor.removeEventListener(conexionDriver)
                Log.e("estado_delete","cancelado estado")
                if (DriverOptions!!!=null)
                    DriverOptions!!.remove()
                setChatJson(datadriver,"nulo")
               /* Intent intent = new Intent("subsUnsubs")
                intent.putExtra("subs", "chat");
                intent.putExtra("subsUnsubs",false);
                LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(requireContext());
                broadcaster.sendBroadcast(intent);*/
                borrarSharedPreferencesDataDriver(1)
                originLatlng=null
                destinationLatlng=null
                primer_estado()

            }else{

            }
        }catch ( e:Exception){
            Log.e("errorconvertgson"," mapa inicio"+datassss)
        }

        //  if (topic.getTitle()!=null&&!(topic.getTitle().equals(""))&&
        //         topic.getBody()!!!=null&&!(topic.getBody()!!.equals(""))) {
        //  sendNotification(requireContext(), topic.getTitle().replace("-", " "), topic.getBody()!!.replace("-", " "));
        //  }

    }

    private fun cargarPosicionDriver() {
        refConexionDriverCoor= FirebaseDatabase.getInstance().reference.child("coordenadaUpdate").child(getDriverId(datadriver)!!)
        conexionDriver=refConexionDriverCoor.addValueEventListener(object: ValueEventListener {
            @Override
            override fun onDataChange(@NonNull  dataSnapshot:DataSnapshot) {
                Log.e("updateCoor"," entro  "+dataSnapshot.ref)
                if(dataSnapshot.exists()){
                    val configuraciones:config = dataSnapshot.getValue( config::class.java)!!
                    Log.e("updateCoor","${configuraciones.lat}  ${configuraciones.log}")
                    if (DriverOptions!!!=null)
                        DriverOptions!!.remove()
                    DriverOptions=googleMap.addMarker( MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.car64dp))
                            .position( LatLng(configuraciones.lat,configuraciones.log))
                            .title("Driver"))
                }
            }

            @Override
            override fun onCancelled(@NonNull  databaseError:DatabaseError) {

            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            GPS->{
                if (resultCode == RESULT_OK) {

                    getDeviceLocation()

                } else {
                    Toast.makeText(requireContext(), "Error de gps", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

//    private fun getRouteString(){
//        val origin = Point.fromLngLat( ramdomNum(false).toDouble(),ramdomNum(true).toDouble())
//        val destination = Point.fromLngLat( ramdomNum(false).toDouble(),ramdomNum(true).toDouble())
//
//        NavigationRoute.builder(requireContext())
//                .accessToken(getString(R.string.access_token))
//                .origin(origin)
//                .destination(destination)
//                .build()
//                .getRoute(object: Callback<DirectionsResponse> {
//                    @Override
//                    override fun onResponse( call:Call<DirectionsResponse>,  response:Response<DirectionsResponse>) {
//                        if (response.isSuccessful){
//                            if (response.body()!!.routes().size < 1){
//
//                                activity!!.toastLong("No se encontro la direccion")
//                            }else{
//                                val f1= FeatureCollection.fromFeature(
//                                        Feature.fromGeometry(LineString.fromPolyline(response.body()!!.routes()[0].geometry()!!, PRECISION_6)))
//                                // LineString ls1 = ((LineString) f1.features().get(0).geometry());
//                                mapboxMap.getStyle {style->
//                                    initData(style,f1)
//                                }
//
//                                Log.e("ruta", "currentRoute: " + response.body()!!.routes().get(0))
//
//                            }
//                        }
//                    }
//
//                    @Override
//                    override fun onFailure( call:Call<DirectionsResponse>,  throwable:Throwable) {
//                        Log.e(TAG, "Error: " + throwable.message)
//                    }
//                })
//
//
//        //button.setVisibility(View.VISIBLE);
//    }
//    private fun getRoute( origin:Point,  destination:Point) {
//        Log.e("origin","$origin")
//        Log.e("destination","$destination ")
//        NavigationRoute.builder(requireContext())
//                .accessToken(getString(R.string.access_token))
//                .origin(origin)
//                .destination(destination)
//                .build()
//                .getRoute(object: Callback<DirectionsResponse> {
//                    @Override
//                    override fun onResponse( call:Call<DirectionsResponse>,  response:Response<DirectionsResponse>) {
//
//                        if (response.body()!! == null) {
//                            Log.e(TAG, "No routes found, make sure you set the right user and access token.")
//                            return
//                        } else if (response.body()!!.routes().size < 1) {
//                            Log.e(TAG, "No routes found")
//                            return
//                        }
//
//                        Log.d(TAG, "currentRoute"+  PolylineUtils.decode(response.body()!!.routes()[0].geometry()!!,6))
//                        currentRoute = response.body()!!.routes()[0]
//                        //new LoadGeoJson(mapaInicio.this).execute()
//                        Log.d(TAG, "currentRoute: " + response.body()!!)
//                        Log.d(TAG, "currentRoute: " + response.body()!!.routes()[0])
//                        Log.d(TAG, "currentRoute"+ response.body()!!.routes()[0].geometry())
//
//                        if (navigationMapRoute != null) {
//                            navigationMapRoute!!.removeRoute()
//                        } else {
//                            try{
//                                navigationMapRoute =  NavigationMapRoute(null, mapView, mapboxMap, R.style.navegation)
//                            }catch ( e:Exception){
//
//                            }
//
//                        }
//                        try{
//                            navigationMapRoute!!.addRoute(currentRoute)
//                            mapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(
//                                     LatLngBounds.Builder()
//                                            .include( LatLng(origin.latitude(), origin.longitude()))
//                                            .include( LatLng(destination.latitude(), destination.longitude()))
//                                            .build(), 50), 3)
//                        }catch ( e:Exception){
//
//                        }
//                    }
//
//                    @Override
//                    override fun onFailure( call:Call<DirectionsResponse>,  throwable:Throwable) {
//                        Log.e(TAG, "Error: " + throwable.message)
//                    }
//                })
//        //button.setVisibility(View.VISIBLE);
//    }
//    @SuppressLint("MissingPermission")

private  fun getDeviceLocation() {
    fusedLocationProviderClient.lastLocation
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                var location = task.result
                if (location != null) {
                    googleMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                location.latitude,
                                location.longitude
                            ),
                            18f
                        )
                    )
                } else {
                    val locationRequest = LocationRequest.create()
                    locationRequest.interval = 10000
                    locationRequest.fastestInterval = 5000
                    locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    val locationCallback = object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            super.onLocationResult(locationResult)
                            if (locationResult == null) return
                            location = locationResult.lastLocation
                            googleMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        location!!.latitude,
                                        location!!.longitude
                                    ),
                                    15f
                                )
                            )
                            fusedLocationProviderClient.removeLocationUpdates(this)
                        }
                    }
                    fusedLocationProviderClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        null
                    )
                }
            } else {
                try {
                    Toast.makeText(
                        context,
                        "Unable to get last lcoation",
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: java.lang.Exception) {
                }
            }
        }
}
//    private fun getDeviceLocation() {
//        try {
//            val lastKnownLocation = googleMap.locationComponent.lastKnownLocation
//
//            val log=lastKnownLocation!!.longitude
//            val lat= lastKnownLocation.latitude
//            val position =  CameraPosition.Builder()
//                    .target( LatLng(lat,log )) // Sets the new camera position
//                    .zoom(15.toDouble())
//                    .build()// Creates a CameraPosition from the builder
//
//            mapboxMap.animateCamera(CameraUpdateFactory
//                    .newCameraPosition(position), 1000)
//
//        }catch ( e:Exception){
//
//            Log.e("deviceLocation", e.message)
//        }
//    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver((receiver),
             IntentFilter("clase")
        )
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        //  if (handler != null && runnable != null) {
        //     handler.post(runnable);
        // }
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()

        // if (handler != null && runnable != null) {
        //    handler.removeCallbacksAndMessages(null);
        // }
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(receiver)
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
//        mapboxMap.getStyle { style ->
//            style.removeSource(DOT_SOURCE_ID1)
//            style.removeLayer("symbol-layer-id")
//        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
//    private fun enableLocationComponent( style:Style) {
//
//        // Check if permissions are enabled and if not request
//        if (PermissionsManager.areLocationPermissionsGranted(requireContext())) {
//
//            // Get an instance of the component
//            locationComponent = mapboxMap.locationComponent
//
//            // Activate with a built LocationComponentActivationOptions object
//            locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(requireContext(), style).build())
//
//            // Enable to make component visible
//            locationComponent.isLocationComponentEnabled = true
//
//            // Set the component's camera mode
//            locationComponent.cameraMode = CameraMode.TRACKING
//
//            // Set the component's render mode
//            locationComponent.renderMode = RenderMode.COMPASS
//
//        } else {
//
//            permissionsManager =  PermissionsManager(this)
//
//            permissionsManager.requestLocationPermissions(requireActivity())
//
//        }
//    }
    private fun gpsEnable() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        val locationRequest = LocationRequest.create()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder:LocationSettingsRequest.Builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient:SettingsClient = LocationServices.getSettingsClient(requireActivity())
        val task:Task<LocationSettingsResponse> = settingsClient.checkLocationSettings(builder.build())
        task.addOnSuccessListener { getDeviceLocation() }
        //instanceof
        task.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                val resolvableApiException = e
                try {
                    resolvableApiException.startResolutionForResult(requireActivity(), GPS)
                } catch ( e1:IntentSender.SendIntentException) {
                    e1.printStackTrace()
                }
            }
        }
    }

    private fun change_priceSpinner() {
       //Log.e("discount","${getPriceShared(datadriver)!!} ${getPriceSharedDiscount(datadriver)!!}")
        val cambios=RetrofitClient.getInstance().api.changePriceTrip(getViajeId(datadriver)!!,getPriceShared(datadriver)!!,
                getPriceSharedDiscount(datadriver)!!)
        cambios.enqueue(object: Callback<user> {
            @Override
            override fun onResponse( call:Call<user>,  response:Response<user>) {
               // Log.e("cambiosP",response.code()+"")
                if (response.isSuccessful){

                }
            }

            @Override
            override fun onFailure( call:Call<user>,  t:Throwable) {
                Log.e("cambiosP",t.message)
            }
        })
    }

    private fun DialogCreate( mensaje:String):Boolean{
        var error = false
        val dialogClickListener:DialogInterface.OnClickListener  =
            DialogInterface.OnClickListener { dialog, which ->
                when(which){
                    DialogInterface.BUTTON_POSITIVE->{
                        enviarMensajeCorreo()
                    }
                    DialogInterface.BUTTON_NEGATIVE->{
                        error=false
                    }


                }
            }
        val builder:AlertDialog.Builder =  AlertDialog.Builder(requireContext())
        builder.setMessage(mensaje).setPositiveButton("Si", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show()
        return error
    }
    private fun terminarTrip() {
        val terminado = RetrofitClient.getInstance().api
                .puStatusTrip(getViajeId(datadriver)!!,false,false,false,true)
        terminado.enqueue(object: Callback<user> {
            @Override
            override fun onResponse( call:Call<user>,  response:Response<user>) {

            }

            @Override
            override fun onFailure( call:Call<user>,  t:Throwable) {

            }
        })
    }

    private fun funcionObtenerPrecio() {
        val call = RetrofitClient
                .getInstance()
                .api
                .travel(getToken(prefs)!!, getOrigenLat(datadriver)!!, getOrigenLong(datadriver)!!,
                        getDestinoLat(datadriver)!!, getDestinoLong(datadriver)!!)
//        Log.e("codigo",getOrigenLat(datadriver)+"   "+ getOrigenLong(datadriver)+"   "+
//                getDestinoLat(datadriver)+"   "+  getDestinoLong(datadriver));
        call.enqueue(object: Callback<getPrice> {
            @Override
            override fun onResponse( call:Call<getPrice>,  response:Response<getPrice>) {
               // Log.e("codigo",response.code()+"")
                if (response.isSuccessful){

                    travelPrecio=response.body()!!.travelRate.ecoEvans
                    setPriceAndAdrress(datadriver,dodpm_edtxt_origin.text.toString(),
                            dodpm_edtxt_dest.text.toString(),travelPrecio)
                    setEstadoViews(datadriver,3)
                    tercer_estado()

                    fmi_progressbar.visibility = View.GONE


                }else{
                    requireActivity().toastLong("Seleccione nuevamente el destino")
                    fmi_progressbar.visibility = View.GONE
                }
            }

            @Override
            override fun onFailure( call:Call<getPrice>,  t:Throwable) {
                fmi_progressbar.visibility = View.GONE
              //  Toast.makeText(requireContext(),"Seleccione nuevamente el destino"+t.getMessage(),Toast.LENGTH_LONG).show();
            }
        })
    }



    private fun cancel_viaje_noti() {
        val cancelarViaje=RetrofitClient.getInstance().api.userTOdriver(getUserId_Prefs(prefs)!!,getDriverId(datadriver)!!,"Viaje-Cancelado","El-Usuario-cancelo-el-viaje","viajecancelado")
        cancelarViaje.enqueue(object: Callback<user> {
            @Override
            override fun onResponse( call:Call<user>,  response:Response<user>) {
                if (response.isSuccessful){
                    if (!(getApiWebVersion(prefs).equals(requireContext().getVersionApp()))){
                        requireActivity().getViewUpdateVersion(requireContext())
                    }
                    Log.e("estado_delete","btn cancelar")
                    borrarSharedPreferencesDataDriver(1)
                    primer_estado()
                    setChatJson(datadriver,"nulo")
                    /*Intent intent = new Intent("subsUnsubs");
                    intent.putExtra("subs", "chat");
                    intent.putExtra("subsUnsubs",false);
                    LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(requireContext());
                    broadcaster.sendBroadcast(intent);*/

                }
              //  Log.e("estado_delete",response.code()+" "+response.message())
            }

            @Override
            override fun onFailure( call:Call<user>,  t:Throwable) {
                Log.e("estado_delete",t.message)
            }
        })

    }
    private fun ejecutarCronometro() {
        countDownTimer = object: CountDownTimer(25000,1000) {
            override fun onTick( millisUntilFinished:Long) {
                setCronometroStop(datadriver,true)
                segundos=String.format(Locale.getDefault(), "%d", millisUntilFinished / 1000L)
                //fmi_time.text = segundos
                Log.e("tiempocountDownTimer",segundos)
            }

            override fun onFinish() {

                if (Integer.parseInt(segundos)>0){
                    countDownTimer.cancel()
                }else {
                   // Log.d("MainActivity22","finnn"+segundos+getCronometroStop(datadriver))
                    if (getCronometroStop(datadriver)!!){

                    }else {
                        setEstadoViews(datadriver,3)
                        tercer_estado()
                        //fmi_time.visibility = View.GONE
                        val comando=RetrofitClient.getInstance().api.puStatusTrip(getViajeId(datadriver)!!,true,false,false,false)
                        comando.enqueue(object: Callback<user> {
                            @Override
                            override fun onResponse( call:Call<user>, response:Response<user> ) {
                                if (response.isSuccessful){
                                    Log.e("Viaje aceptado",response.message())

                                }
                              //  Log.e("Viaje aceptado",response.code()+"")
                            }

                            @Override
                            override fun onFailure( call:Call<user>,  t:Throwable) {

                            }
                        })
                    }



                }


            }

        }.start()
    }
//    @SuppressLint("LogNotTimber")
//    fun getRetrofitMap(
//        origin: LatLng?,
//        dest: LatLng?
//    ) : ArrayList<LatLng>? {
//        Log.e("Gson: ", "$origin  $dest" )
////        emit(Resource.Loading())
//        var dato  : ArrayList<LatLng>?=null
//        val llamada: Call<String> =
//            RetrofitClientMaps.getInstance().api.callMaps(getDirectionsUrl(origin!!, dest!!))
//        llamada.enqueue(object : Callback<String> {
//            override fun onResponse(
//                call: Call<String>,
//                response: Response<String>
//            ) {
//
//                //Data= g.fromJson(response.body(),Example.class);
//                //Log.e("Gson: ", response.body())
//                if (response.isSuccessful) {
//
////                    var routes: List<List<HashMap<String,String>>>?=null
//                    try {
//                        val jObject = JSONObject(response.body()!!)
//                        val parser = DirectionsJSONParser()
//                        val routes: MutableList<MutableList<java.util.HashMap<String, String>>>? = parser.parse(jObject)
////                        val points= dibujarLineas(routes!!)
//                        var points: ArrayList<LatLng> = ArrayList()
//
//                        for (i in routes!!) {
//                            //Log.e("result.indices: ", "$i  ${i.size}")
//                            for (j in i) {
//                                val lat = j["lat"]!!.toDouble()
//                                val lng = j["lng"]!!.toDouble()
//                                val position =
//                                    LatLng(lat, lng)
//                                points.add(position)
//                            }
//                        }
//                        dato=points
////                        emit(Resource.Success(points))
////                            mapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(
////                                     LatLngBounds.Builder()
////                                            .include( LatLng(origin.latitude(), origin.longitude()))
////                                            .include( LatLng(destination.latitude(), destination.longitude()))
////                                            .build(), 50), 3)
//                       // googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cameramove.center,15f))
//
//                    } catch (e: Exception) {
//                        throw e
////                        emit(Resource.Failure(e))
//                        Log.e("Gson: ", e.message)
//                        e.printStackTrace()
//                    }
//                } else {
//                    throw Exception("No se encontraron rutas")
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
//                throw Exception("Error con la red")
//            }
//        })
//        return dato
//    }
    override fun onMapReady(p0: GoogleMap?) {
        this.googleMap=p0!!
        googleMap.isMyLocationEnabled=true
        googleMap.uiSettings.isMyLocationButtonEnabled=false
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        geo =  Geocoder(requireContext(), Locale.getDefault())
        googleMap.setOnCameraMoveStartedListener {i->
            isMoving = true
//            img_change.visibility = View.GONE
//            profile_loader.visibility = View.GONE
//            val mDrawable= requireContext().resources.getDrawable(R.drawable.circle_background_moving, null)
//            mapa_marker_center.background = mDrawable
//            resizeLayout(false)

        }
        googleMap.setOnCameraIdleListener {
            isMoving = false
//            img_change.visibility = View.INVISIBLE
//            profile_loader.visibility = View.VISIBLE
//            resizeLayout(true)
            Handler().postDelayed(Runnable {
                val mDrawable =
                    requireContext().resources.getDrawable(R.drawable.circle_background, null)

                if (!isMoving) {
//                    mapa_marker_center.background = mDrawable
//                    img_change.visibility = View.VISIBLE
//                    profile_loader.visibility = View.GONE

                    try {
                        val center = googleMap.cameraPosition.target
                        adres = geo.getFromLocation(center.latitude,center.longitude,1) as List<Address>
                        val  direccion2= (adres!!.get(0).getAddressLine(0)).split(",")

                        if (!status_btn_origin){
                            dodpm_edtxt_origin.setText(direccion2[0])
                        }else{
                            dodpm_edtxt_dest.setText(direccion2[0])
                        }

                    }catch ( e:IOException) {
                        e.printStackTrace()
                    }
                }
            },1500)
        }
        MapsInitializer.initialize(requireContext())
        moveMapCamera()
        comprobarestadoViews()
    }
     private fun moveMapCamera() {

         var center = CameraUpdateFactory.newLatLng( LatLng(-15.834, -70.019))
        var zoom = CameraUpdateFactory.zoomTo(15f)

        googleMap.moveCamera(center)
         googleMap.animateCamera(zoom)
    }
//    private fun resizeLayout( backToNormalSize:Boolean) {
//        val params =  mapa_marker_center.layoutParams as FrameLayout.LayoutParams
//
//        var vto = mapa_marker_center.viewTreeObserver
//        vto.addOnGlobalLayoutListener {
//            mapa_marker_center.viewTreeObserver.removeGlobalOnLayoutListener { this }
//            circleRadius = mapa_marker_center.measuredWidth
//        }
//
//
//        if (backToNormalSize) {
//            params.width = WRAP_CONTENT
//            params.height = WRAP_CONTENT
//            params.topMargin = 0
//
//        } else {
//            if(circleRadius!=null){
//
//                params.topMargin =  (circleRadius!! * 0.3).toInt()
//                params.height = circleRadius!! - circleRadius!! / 3
//                params.width = circleRadius!! - circleRadius!! / 3
//            }
//        }
//
//        mapa_marker_center.layoutParams = params
//    }
//    fun dibujarLineas(result: MutableList<MutableList<java.util.HashMap<String, String>>>):ArrayList<LatLng> {
//
//        var points: ArrayList<LatLng> = ArrayList()
//
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
//        }
//        return points
//    }
    override fun onTaskDone(vararg values: Any?) {
        if (currentPoline!=null){
            currentPoline!!.remove()
        }
        currentPoline= googleMap.addPolyline(values[0] as PolylineOptions)
    }
//    @SuppressLint("MissingPermission")
//    @Override
//    override fun  onMapReady(@NonNull mapboxMap:MapboxMap) {
//
//        this.mapboxMap = mapboxMap
//
//        geo =  Geocoder(requireContext(), Locale.getDefault())
//        mapboxMap.uiSettings.isCompassEnabled = true
//        mapboxMap.setStyle(getString(R.string.navigation_guidance_day), object: Style.OnStyleLoaded {
//            @Override
//            override fun  onStyleLoaded(@NonNull  style:Style) {
//                enableLocationComponent(style)
//                // addDestinationIconSymbolLayer(style)
//            }
//
//        })
//        mapboxMap.addOnCameraIdleListener(object: MapboxMap.OnCameraIdleListener {
//            @Override
//            override fun onCameraIdle() {
//                var center = mapboxMap.cameraPosition.target
//                if ((center.latitude.toString()!="0.0")&&(center.longitude.toString()!="0.0")){
//                    // new Handler().postDelayed(new Runnable() {
//                    //   @Override
//                    //  public void run() {
//                    try {
//                        adres = geo.getFromLocation(center.latitude,center.longitude,1) as List<Address>
//                        val  direccion2= (adres!!.get(0).getAddressLine(0)).split(",")
//
//                        if (!status_btn_origin){
//                            dodpm_edtxt_origin.setText(direccion2[0])
//                        }else{
//                            dodpm_edtxt_dest.setText(direccion2[0])
//                        }
//
//                    }catch ( e:IOException) {
//                        e.printStackTrace()
//                    }
//                    //   }
//                    //},2000);
//
//
//                }
//            }
//        })
//        mapa_icon_gps.setOnClickListener{
//            try {
//                val lastKnownLocation = mapboxMap.locationComponent.lastKnownLocation
//
//                val log=lastKnownLocation!!.longitude
//                val lat= lastKnownLocation!!.latitude
//                val position =  CameraPosition.Builder()
//                    .target( com.mapbox.mapboxsdk.geometry.LatLng(lat,log )) // Sets the new camera position
//                .zoom(15.toDouble())
//                    .build() // Creates a CameraPosition from the builder
//
//                mapboxMap.animateCamera(CameraUpdateFactory
//                    .newCameraPosition(position), 1000)
//            }catch ( e:Exception){
//
//                Log.e("deviceLocation", e.message)
//                val intent=  Intent(requireContext(), MainActivity::class.java)
//                startActivity(intent)
//            }
//        }
//
//        comprobarestadoViews()
//        if (!getdataNotification_noti(datadriver).equals("nulo")){
//            search_imagen.visibility = View.GONE
//            Log.d("MainActivity22222","ejecuto")
//
//            if (getdataNotification_noti(datadriver)!!.contains("{cronometroFinalizado}")){
//                search_imagen.visibility = View.GONE
//                val comando= RetrofitClient.getInstance().api.puStatusTrip(getViajeId(datadriver)!!,true,false,false,false)
//                comando.enqueue(object: Callback<user> {
//                    @Override
//                    override fun onResponse( call:Call<user>,  response:Response<user>) {
//                    }
//
//                    @Override
//                    override fun onFailure( call:Call<user>,  t:Throwable) {
//                    }
//                })
//
//            }else {
//                ejecutar_tarea_notificaciones_data(getdataNotification_noti(datadriver)!!)
//            }
//        }
//        if (getEstadoView(datadriver)!!>=5){
//            cargarPosicionDriver()
//        }
//      /*
//      *
//        DirectionsRoute ruta2=getRouteString();
//        FeatureCollection f2= FeatureCollection.fromFeature(
//                Feature.fromGeometry(LineString.fromPolyline(ruta2.geometry(), PRECISION_6)));
//        LineString ls2 = ((LineString) f2.features().get(0).geometry());
//        if (ls2 != null) {
//            routeCoordinateList2= ls2.coordinates();
//        }
//        DirectionsRoute ruta3=getRouteString();
//        FeatureCollection f3= FeatureCollection.fromFeature(
//                Feature.fromGeometry(LineString.fromPolyline(ruta3.geometry(), PRECISION_6)));
//        LineString ls3 = ((LineString) f3.features().get(0).geometry());
//        if (ls3 != null) {
//            routeCoordinateList3 = ls3.coordinates();
//        }
//       DirectionsRoute ruta4= getRouteString();
//        FeatureCollection f4= FeatureCollection.fromFeature(
//                Feature.fromGeometry(LineString.fromPolyline(ruta4.geometry(), PRECISION_6)));
//        LineString ls4 = ((LineString) f4.features().get(0).geometry());
//        if (ls4 != null) {
//            routeCoordinateList4 = ls4.coordinates();
//        }
//      * */
//
//
//    }
//
//



    /**
     * Method is used to interpolate the SymbolLayer icon animation.
     */

//    @Override
//    override fun  onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
//        Toast.makeText(requireContext(),"This app needs location permissions to show its functionality.", Toast.LENGTH_LONG).show()
//    }

//    @Override
//    override fun  onPermissionResult( granted:Boolean) {
//        if (granted) {
//            enableLocationComponent(mapboxMap.style!!)
//        } else {
//            Toast.makeText(requireContext(), "You didn\\'t grant location permissions.", Toast.LENGTH_LONG).show()
//            //mapView.finish();
//        }
//    }
    private fun borrarSharedPreferencesDataDriver( estado:Int){
        //limpiarNotify(getActivity());
       // Log.e("estado_delete",getEstadoView(datadriver)+"");
        val editor = datadriver.edit()
        editor.clear()
        editor.apply()
        setEstadoViews(datadriver,estado)
    }

    @ExperimentalStdlibApi
    @SuppressLint("RestrictedApi")
    private fun primer_estado(){

        try {
            if (refConexionDriverCoor!=null&&conexionDriver!=null)
                refConexionDriverCoor.removeEventListener(conexionDriver)
        }catch ( e:Exception){

        }

        Log.e("vistas","entra la primera vista")
        if (cordenadasDriver_Runable!=null){
            coordenadasDriver.removeCallbacks(cordenadasDriver_Runable)
        }
//        mapboxMap.getStyle { style ->
//            style.removeSource(DOT_SOURCE_ID1)
//            style.removeLayer("symbol-layer-id")
//        }
//        try{
//            mapboxMap.clear()
//            mapboxMap.removeAnnotations()
//            navigationMapRoute!!.removeRoute()
//            mapboxMap.removeMarker(origen!!)
//            mapboxMap.removeMarker(destino!!)
//
//        }catch ( e:Exception){
//
//        }

        // dfv_spinner.setVisibility(View.GONE);
        //   dfv_spinner.setEnabled(false);
        dialog_precio_spinner.isEnabled = true
        mi_imgbtn_next_step.isEnabled = false
        mi_imgbtn_next_step.setBackgroundColor(Color.GRAY)
        mi_imgbtn_next_step.text = "CALCULAR TARIFA"
        if (currentPoline!=null){
            currentPoline!!.remove()
        }
        if(origen!=null){
            origen!!.remove()
        }
        if(destino!=null){
            destino!!.remove()
        }
        dodpm_edtxt_dest.setText("")
        dodpm_edtxt_origin.setText("")
        dodpm_edtxt_dest.setTextColor(requireContext().getColor(R.color.black))
        dodpm_edtxt_origin.setTextColor(requireContext().getColor(R.color.black))
        dodpm_edtxt_dest.isEnabled = true
        dodpm_edtxt_origin.isEnabled = true
        dodpm_imgbtn_status_dest.isSelected = false
        dodpm_imgbtn_status_origin.isSelected = false
        status_btn_dest=false
        status_btn_origin=false
        fmi_progressbar.visibility = View.GONE
        search_imagen.visibility = View.GONE
        crear_comentario_layout_dialog.visibility = View.GONE
        mapa_marker_center.visibility = View.VISIBLE
        //fmi_time.visibility = View.GONE
        mapa_recoger_cliente.visibility = View.VISIBLE
        mapa_precios.visibility = View.GONE
        transcurso_viaje.visibility = View.GONE
        fin_viaje.visibility = View.GONE
        bottomSheetBehavior.setState( BottomSheetBehavior.STATE_HIDDEN )
        aic_button_comentar_hide.visibility = View.GONE
        try{
            //getRouteString()
        }catch ( e:Exception){

        }
        if (DriverOptions!=null)
            DriverOptions!!.remove()
        dibujarlineas()

    }
    @SuppressLint("RestrictedApi")
    private fun segundo_estado(){
        mapa_recoger_cliente.visibility = View.VISIBLE
        mapa_precios.visibility = View.GONE
        transcurso_viaje.visibility = View.GONE
        fin_viaje.visibility = View.GONE
        mapa_marker_center.visibility = View.VISIBLE
        bottomSheetBehavior.setState( BottomSheetBehavior.STATE_HIDDEN )
        aic_button_comentar_hide.visibility = View.GONE
       // Log.e("estado 2: ",getOrigenLat(datadriver)+"   "+getOrigenLong(datadriver)+"  "+valor);
    }
    @SuppressLint("RestrictedApi")
    private fun tercer_estado(){
//        mapboxMap.getStyle(object: Style.OnStyleLoaded {
//            @Override
//            override fun onStyleLoaded(@NonNull  style:Style) {
//                style.removeSource(DOT_SOURCE_ID1)
//                style.removeLayer("symbol-layer-id")
//            }
//        })
        if (botMarker!=null){
            botMarker!!.remove()
            googleMap.clear()
            handler!!.removeCallbacks(drawPathRunnable)
        }
        dibujarlineas()
        dialog_precio_spinner.isEnabled = true
        dialog_precio_spinner.setSelection(0)

      /*  if (tarea!=null&&handler_internet!=null)
            handler_internet.removeCallbacks(tarea);*/
        mapa_recoger_cliente.visibility = View.GONE
        mapa_precios.visibility = View.VISIBLE
        dialog_precio_select.isEnabled = true
        search_imagen.visibility = View.GONE
        //fmi_time.visibility = View.GONE
        transcurso_viaje.visibility = View.GONE
        fin_viaje.visibility = View.GONE
        bottomSheetBehavior.setState( BottomSheetBehavior.STATE_HIDDEN )
        aic_button_comentar_hide.visibility = View.GONE
        dialog_precio_text_view_precio.text = "S./ "+ getPriceShared(datadriver)!!
        mapa_marker_center.visibility = View.GONE
        dialog_precio_select.text = "PEDIR ECO EVANS"
       // Log.e("estado 3: ",getDestinoLat(datadriver)+"   "+getDestinoLong(datadriver)+"  "+valor)

    }
    @SuppressLint("RestrictedApi")
    private fun cuarto_estado(){
        dialog_precio_select.isEnabled = false
        mapa_marker_center.visibility = View.GONE
        mapa_recoger_cliente.visibility = View.GONE
        mapa_precios.visibility = View.VISIBLE
        transcurso_viaje.visibility = View.GONE
        fin_viaje.visibility = View.GONE
        bottomSheetBehavior.setState( BottomSheetBehavior.STATE_HIDDEN )
        aic_button_comentar_hide.visibility = View.GONE
        dialog_precio_spinner.isEnabled = false
        dialog_precio_text_view_precio.text = "S./ "+ getPriceShared(datadriver)!!
        dialog_precio_select.text = "Esperando evans"
        //Log.e("estado 4: ",getDestinoLat(datadriver)+"   "+getDestinoLong(datadriver)+"  "+valor);
        dibujarlineas()

    }

    @ExperimentalStdlibApi
    private fun dibujarlineas() {
        try {
            var tempO: LatLng?
            var tempD: LatLng?
            if (3>getEstadoView(datadriver)!!){
                val datos=ramdomNumForLat(googleMap.cameraPosition.target)
                val dato2=ramdomNumForLat(LatLng(-15.834, -70.019))
                tempO = dato2
                tempD = datos
            }else{
                if (botMarker!=null){
                    botMarker!!.remove()
                }
                if (handler!=null&&drawPathRunnable!=null){
                    handler!!.removeCallbacks(drawPathRunnable)
                }
                originLatlng = LatLng(getOrigenLat(datadriver)!!.toDouble(),getOrigenLong(datadriver)!!.toDouble())
                destinationLatlng = LatLng( getDestinoLat(datadriver)!!.toDouble(),getDestinoLong(datadriver)!!.toDouble())
                tempO=originLatlng
                tempD=destinationLatlng
            }
            Log.e("viewModel","$tempO  $tempD")
            viewModel.getRutaMapa(tempO!!,tempD!!).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                when(it){
                    is Resource.Loading->{
                        Log.e("viewModel","Loaging")
                    }
                    is Resource.Success->{
                        if (3>getEstadoView(datadriver)!!){
                            Log.e("viewModel",it.data.toString())
                            Gneratebots(it.data)
                        }else{
                            val points=it.data
                            Log.e("lineasMapbox",points.toString())
                                var lineOptions: PolylineOptions? = PolylineOptions()
                                lineOptions!!.addAll(points!!)
                                lineOptions.width(10f)
                                lineOptions.color(Color.BLACK)
                                lineOptions.geodesic(true)
                                currentPoline=googleMap.addPolyline(lineOptions)
                                val cameramove= LatLngBounds(originLatlng,destinationLatlng)
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(cameramove,50))
                        }

                    }
                    is Resource.Failure->{
                        Log.e("viewModel",it.exception.message)
                    }
                }
            })

        }catch ( e:Exception){

        }


        if (destino!=null){
            destino!!.remove()
        }
        if (origen!=null){
            origen!!.remove()
        }
        try {
            destino=googleMap.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.logo22))
                .title("Destino")
                .position( LatLng(getDestinoLat(datadriver)!!.toDouble(),getDestinoLong(datadriver)!!.toDouble())))


            origen=googleMap.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.logo33))
                .title("Origen")
                .position( LatLng(getOrigenLat(datadriver)!!.toDouble(),getOrigenLong(datadriver)!!.toDouble())))
        }catch ( e:Exception){
         /*   Log.d("entrando","a transacion"+e.getMessage());
            Fragment frg = new mapaInicio(getUserId_Prefs(prefs),getToken(prefs));
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.detach(frg);
            ft.attach(frg);
            ft.commit();*/
        }

    }
    fun Gneratebots(points:ArrayList<LatLng>){
//        var polilineanimator= ValueAnimator.ofInt(0,100)
//        polilineanimator.duration = 2000
//        polilineanimator.interpolator = LinearInterpolator()
//        polilineanimator.addUpdateListener {
//            var percentValue= it.animatedValue as Int
//            var size = points.size
//            var newPoints = (size*(percentValue/100.0f)).toInt()
//            var p= points.subList(0,newPoints)
//            Log.e("viewModel",1.toString())
//        }
//        polilineanimator.start()
        botMarker= googleMap.addMarker(MarkerOptions().position(points[0])
            .flat(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.car64dp)))
        Log.e("viewModel",2.toString())
        handler= Handler()
        index=-1
        next=1
        drawPathRunnable= object:Runnable {
            override fun run() {
                if(index!! < (points.size-1)){
                    index=index!!+1
                    next=index!!+1
                }
                //else{
                //                    botMarker!!.remove()
                //                    handler!!.removeCallbacks(drawPathRunnable)
                //                    dibujarlineas()
                //                }
                if(index!! < (points.size-1)){
                    startPositionB= points[index!!]
                    endPositionB= points[next!!]
                }
                var animator= ValueAnimator.ofFloat(0f,1f)
                animator.duration=3000
                animator.interpolator=LinearInterpolator()
                animator.addUpdateListener {
                    if (startPositionB!=null&&endPositionB!=null){
                        v = it.animatedFraction
                        val lngB=v!!*endPositionB!!.longitude+(1-v!!) * startPositionB!!.longitude
                        val latB=v!!*endPositionB!!.latitude+(1-v!!) * startPositionB!!.latitude
                        var newPosaa = LatLng(latB,lngB)
                        botMarker!!.position=newPosaa
                        botMarker!!.setAnchor(0.5F,0.5F)
                        botMarker!!.rotation= getBearing(startPositionB!!,newPosaa)
                    }else{
                        botMarker!!.remove()
                        handler!!.removeCallbacks(drawPathRunnable)
                        dibujarlineas()

                    }


                }
                animator.start()
                handler!!.postDelayed(this,3000)
            }
        }
        handler!!.postDelayed(drawPathRunnable,3000)

    }
    fun getBearing(startPosition:LatLng,endPosition:LatLng):Float{
        val lat= abs(startPosition.latitude-endPosition.latitude)
        val log= abs(startPosition.longitude-endPosition.longitude)

        if (startPosition.latitude<endPosition.latitude&&startPosition.longitude<endPosition.longitude){
            return Math.toDegrees(atan(log/lat)).toFloat()
        }else if(startPosition.latitude>=endPosition.latitude&&startPosition.longitude<endPosition.longitude){
            return (90-Math.toDegrees(atan(log/lat))+90).toFloat()
        }else if(startPosition.latitude>=endPosition.latitude&&startPosition.longitude>=endPosition.longitude){
            return (Math.toDegrees(atan(log/lat))+180).toFloat()
        }else if(startPosition.latitude<endPosition.latitude&&startPosition.longitude>=endPosition.longitude){
            return (90-Math.toDegrees(atan(log/lat))+270).toFloat()
        }
        return -1f
    }
    @SuppressLint("RestrictedApi")
    private fun quinto_estado(){
        mapa_marker_center.visibility = View.GONE
        mapa_recoger_cliente.visibility = View.GONE
        mapa_precios.visibility = View.GONE
        transcurso_viaje.visibility = View.VISIBLE
        fin_viaje.visibility = View.GONE
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        aic_button_comentar_hide.visibility = View.GONE
        dialog_transcurso_destino_txt_name.text = getnameID(datadriver)
        dialog_transcurso_destino_txt_placa.text = getlicenseCarID(datadriver)
        dialog_transcurso_destino_txt_marca.text = getbrandCarID(datadriver)
        dialog_transcurso_destino_txt_color.text = getcolorCarID(datadriver)
        try {
            val options =  RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round)
            Glide.with(this).load(getdriverImgID(datadriver)).apply(options).into(dialog_transcurso_destino_img_photo)

        }catch ( e:Exception){
           // Log.e("ErrorcargarImg",e.getMessage());
        }

        //dialog_fin_viaje_imgbtn_delete.setVisibility(View.GONE);
        // dialog_fin_viaje_imgbtn_message.setVisibility(View.VISIBLE);
    //    Log.e("estado 5: ",getDestinoLat(datadriver)+"   "+getDestinoLong(datadriver)+"  "+valor);
        dibujarlineas()
    }
    @SuppressLint("RestrictedApi")
    private fun sexto_estado(){
       // Log.e("estado 6: ",getDestinoLat(datadriver)+"   "+getDestinoLong(datadriver)+"  "+valor);

        dialog_fin_viaje_txt_precio_coupon.text = "${getPriceSharedDiscount(datadriver)!!} PEN"
        dialog_fin_viaje_txt_precio.text = " ${( getPriceShared(datadriver)!!.toDouble()-getPriceSharedDiscount(datadriver)!!.toDouble())} PEN"
        mapa_recoger_cliente.visibility = View.GONE
        mapa_precios.visibility = View.GONE
        mapa_marker_center.visibility = View.GONE
        transcurso_viaje.visibility = View.GONE
        fin_viaje.visibility = View.VISIBLE
        bottomSheetBehavior.setState( BottomSheetBehavior.STATE_HIDDEN )
        aic_button_comentar_hide.visibility = View.GONE
        dialog_fin_viaje_btn_aceptar.isEnabled = true
        dialog_fin_viaje_txt_titulo.text = "CONDUCTOR LLEGÓ AL ORIGEN"
        dialog_fin_viaje_btn_aceptar.text = "INICIO"
        dialog_fin_viaje_txt_origen.text = "Origen: "+getStartAddress(datadriver)!!
        dialog_fin_viaje_txt_destino.text = "Destino: "+getEndAddress(datadriver)!!
        //dialog_fin_viaje_imgbtn_delete.setVisibility(View.VISIBLE)
        //dialog_fin_viaje_imgbtn_message.setVisibility(View.GONE);
        dibujarlineas()
    }
    @SuppressLint("RestrictedApi")
    private  fun septimo_estado(){
   //     Log.e("estado 7: ",getDestinoLat(datadriver)+"   "+getDestinoLong(datadriver)+"  "+valor)


        mapa_recoger_cliente.visibility = View.GONE
        mapa_precios.visibility = View.GONE
        transcurso_viaje.visibility = View.GONE
        fin_viaje.visibility = View.VISIBLE
        aic_button_comentar_hide.visibility = View.GONE
        mapa_marker_center.visibility = View.GONE
        bottomSheetBehavior.setState( BottomSheetBehavior.STATE_HIDDEN )
        dialog_fin_viaje_txt_titulo.text = "Esperando que Inicie el Viaje"
        dialog_fin_viaje_btn_aceptar.text = "Esperando.."
        dialog_fin_viaje_btn_aceptar.isEnabled = false
        dialog_fin_viaje_txt_origen.text = "Origen: "+getStartAddress(datadriver)!!
        dialog_fin_viaje_txt_destino.text = "Destino: "+getEndAddress(datadriver)!!
        //  dialog_fin_viaje_imgbtn_delete.setVisibility(View.VISIBLE);
        //dialog_fin_viaje_imgbtn_message.setVisibility(View.GONE);
        dibujarlineas()
    }
    @SuppressLint("RestrictedApi")
    private fun octavo_estado(){
      //  Log.e("estado 8: ",getDestinoLat(datadriver)+"   "+getDestinoLong(datadriver)+"  "+valor);
        mapa_recoger_cliente.visibility = View.GONE
        mapa_precios.visibility = View.GONE
        transcurso_viaje.visibility = View.GONE
        fin_viaje.visibility = View.VISIBLE
        aic_button_comentar_hide.visibility = View.GONE
        bottomSheetBehavior.setState( BottomSheetBehavior.STATE_HIDDEN )
        dialog_fin_viaje_txt_origen.text = "Origen: "+getStartAddress(datadriver)!!
        dialog_fin_viaje_txt_destino.text = "Destino: "+getEndAddress(datadriver)!!


        /*dfv_spinner.setVisibility(View.VISIBLE);
        dfv_spinner.setEnabled(true);
        if (  getPriceSharedDiscount(datadriver).equals("0.0")){
            dfv_spinner.setSelection(0);
        }else{
            dfv_spinner.setSelection(1);
        }*/
        dialog_fin_viaje_txt_titulo.text = "Confirmar terminar Viaje"
        dialog_fin_viaje_btn_aceptar.text = "Terminar Viaje"
        dialog_fin_viaje_btn_aceptar.isEnabled = true
        mapa_marker_center.visibility = View.GONE
        // dialog_fin_viaje_imgbtn_delete.setVisibility(View.VISIBLE);
        // dialog_fin_viaje_imgbtn_message.setVisibility(View.GONE);
        dibujarlineas()
    }
    @SuppressLint("RestrictedApi")
    private fun noveno_estado(){
     //   Log.e("estado 9: ",getDestinoLat(datadriver)+"   "+getDestinoLong(datadriver)+"  "+valor);

        dibujarlineas()

        mapa_recoger_cliente.visibility = View.GONE
        mapa_precios.visibility = View.GONE
        transcurso_viaje.visibility = View.GONE
        fin_viaje.visibility = View.VISIBLE
        bottomSheetBehavior.setState( BottomSheetBehavior.STATE_HIDDEN )
        aic_button_comentar_hide.visibility = View.GONE
        mapa_marker_center.visibility = View.GONE
        dialog_fin_viaje_txt_origen.text = "Origen: "+getStartAddress(datadriver)!!
        dialog_fin_viaje_txt_destino.text = "Destino: "+getEndAddress(datadriver)!!
        dialog_fin_viaje_txt_titulo.text = "Conductor esperando Pago"
        dialog_fin_viaje_btn_aceptar.text = "Esperando.."
        dialog_fin_viaje_btn_aceptar.isEnabled = false
        //dialog_fin_viaje_imgbtn_delete.setVisibility(View.VISIBLE);
        // dialog_fin_viaje_imgbtn_message.setVisibility(View.GONE);
    }
    @SuppressLint("RestrictedApi")
    private fun decimo_estado(){
        if (currentPoline!=null){
            currentPoline!!.remove()
            destino!!.remove()
            origen!!.remove()
        }
       // Log.e("estado 10: ",getDestinoLat(datadriver)+"   "+getDestinoLong(datadriver)+"  "+valor)

        mapa_recoger_cliente.visibility = View.GONE
        mapa_precios.visibility = View.GONE
        transcurso_viaje.visibility = View.GONE
        fin_viaje.visibility = View.GONE
        aic_button_comentar_hide.visibility = View.VISIBLE
        crear_comentario_layout_dialog.visibility = View.VISIBLE
        mapa_marker_center.visibility = View.GONE
        bottomSheetBehavior.setState( BottomSheetBehavior.STATE_EXPANDED )
        Log.e("estado_delete","decimo estado")


    }
    fun ejecutarTarea_ActualizarCordenadasDriver(){

        cordenadasDriver_Runable= object:Runnable {
            @Override
            override fun run() {
                try {

                    val call = RetrofitClient.getInstance()
                            .api.updateCoordenadasDriver(getDriverId(datadriver)!!)

                    call.enqueue(object: Callback<infoDriver> {
                        @Override
                        override fun onResponse( call:Call<infoDriver>,  response:Response<infoDriver>) {
                            if (response.isSuccessful){
                                Log.e("updateDrive","Cordenate"+response.body()!!.latitude)
                                try {

                                }catch ( e:Exception){
                                    Log.e("updateDrive","Cordenate"+e.message)
                                }

                            }
                            Log.e("updateDrive","Cordenate"+response.message()+response.code())
                        }

                        @Override
                        override fun  onFailure(call:Call<infoDriver> ,  t:Throwable) {

                        }
                    })
                }catch ( e:Exception){
                    Log.e("Update","Cordenadas "+e.message)
                }
                coordenadasDriver.postDelayed(this, TIEMPO.toLong())

            }
        }
        coordenadasDriver.postDelayed(cordenadasDriver_Runable, TIEMPO.toLong())
    }
    private fun comprobarestadoViews() {
        when (getEstadoView(datadriver)!!){
            1->{
                Log.e("estado_delete","dswitch comprobar vista")
                borrarSharedPreferencesDataDriver(1)
                primer_estado()
            }
             2->{
                 Log.e("estado_delete","dswitch comprobar vista")
                 borrarSharedPreferencesDataDriver(1)
                 primer_estado()
             }
             3->{
                 tercer_estado()
             }
             4->{
                 tercer_estado()
             }
             5->{
                 quinto_estado()
             }
             6->{
                 sexto_estado()
             }
             7->{
                 septimo_estado()
             }
             8->{
                 octavo_estado()
             }
             9->{
                 noveno_estado()
             }
             10->{
                 decimo_estado()
             }
        }
    }

    //pRUEBAS DE INTERNET
    private fun isNetDisponible():Boolean {
        try {
            val connectivityManager:ConnectivityManager =
                    requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val actNetInfo:NetworkInfo = connectivityManager.activeNetworkInfo

            return (actNetInfo != null && actNetInfo.isConnected)
        }catch ( e:Exception){
            Log.e("error isnetDisponible: ",e.message)
            return true
        }

    }
    fun isOnlineNet():Boolean {

        try {
            val p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.pe")

             val   dato        = p.waitFor()
            val reachable = (dato == 0)
            return reachable

        } catch ( e:Exception) {

            e.printStackTrace()
            return false
        }

    }

}
