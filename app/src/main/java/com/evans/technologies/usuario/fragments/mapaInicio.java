package com.evans.technologies.usuario.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.evans.technologies.usuario.Activities.MainActivity;
import com.evans.technologies.usuario.R;
import com.evans.technologies.usuario.Retrofit.RetrofitClient;
import com.evans.technologies.usuario.Utils.Adapters.adapter_spinner_pay_tipe;
import com.evans.technologies.usuario.Utils.Services.cronometro;
import com.evans.technologies.usuario.Utils.timeCallback.ComunicateFrag;
import com.evans.technologies.usuario.Utils.timeCallback.updateListenerNotifications;
import com.evans.technologies.usuario.fragments.change_password.set_codigo;
import com.evans.technologies.usuario.model.config;
import com.evans.technologies.usuario.model.data;
import com.evans.technologies.usuario.model.getPrice;
import com.evans.technologies.usuario.model.infoDriver;
import com.evans.technologies.usuario.model.user;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.utils.PolylineUtils;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.evans.technologies.usuario.Utils.UtilsKt.getAccountActivate;
import static com.evans.technologies.usuario.Utils.UtilsKt.getApiWebVersion;
import static com.evans.technologies.usuario.Utils.UtilsKt.getCronometroStop;
import static com.evans.technologies.usuario.Utils.UtilsKt.getDestinoLat;
import static com.evans.technologies.usuario.Utils.UtilsKt.getDestinoLong;
import static com.evans.technologies.usuario.Utils.UtilsKt.getDriverId;
import static com.evans.technologies.usuario.Utils.UtilsKt.getEndAddress;
import static com.evans.technologies.usuario.Utils.UtilsKt.getEstadoView;
import static com.evans.technologies.usuario.Utils.UtilsKt.getOrigenLat;
import static com.evans.technologies.usuario.Utils.UtilsKt.getOrigenLong;
import static com.evans.technologies.usuario.Utils.UtilsKt.getPriceShared;
import static com.evans.technologies.usuario.Utils.UtilsKt.getPriceSharedDiscount;
import static com.evans.technologies.usuario.Utils.UtilsKt.getStartAddress;
import static com.evans.technologies.usuario.Utils.UtilsKt.getUserEmail;
import static com.evans.technologies.usuario.Utils.UtilsKt.getUserId_Prefs;
import static com.evans.technologies.usuario.Utils.UtilsKt.getUserName;
import static com.evans.technologies.usuario.Utils.UtilsKt.getVersionApp;
import static com.evans.technologies.usuario.Utils.UtilsKt.getViajeId;
import static com.evans.technologies.usuario.Utils.UtilsKt.getViewUpdateVersion;
import static com.evans.technologies.usuario.Utils.UtilsKt.getbrandCarID;
import static com.evans.technologies.usuario.Utils.UtilsKt.getcolorCarID;
import static com.evans.technologies.usuario.Utils.UtilsKt.getdataNotification_noti;
import static com.evans.technologies.usuario.Utils.UtilsKt.getdriverImgID;
import static com.evans.technologies.usuario.Utils.UtilsKt.getlicenseCarID;
import static com.evans.technologies.usuario.Utils.UtilsKt.getnameID;
import static com.evans.technologies.usuario.Utils.UtilsKt.llaveChat;
import static com.evans.technologies.usuario.Utils.UtilsKt.ramdomNum;
import static com.evans.technologies.usuario.Utils.UtilsKt.setChatJson;
import static com.evans.technologies.usuario.Utils.UtilsKt.setClaseActual;
import static com.evans.technologies.usuario.Utils.UtilsKt.setCronometroStop;
import static com.evans.technologies.usuario.Utils.UtilsKt.setDestino;
import static com.evans.technologies.usuario.Utils.UtilsKt.setDriverId;
import static com.evans.technologies.usuario.Utils.UtilsKt.setEstadoViews;
import static com.evans.technologies.usuario.Utils.UtilsKt.setInfoDriver;
import static com.evans.technologies.usuario.Utils.UtilsKt.setOrigen;
import static com.evans.technologies.usuario.Utils.UtilsKt.setPriceAndAdrress;
import static com.evans.technologies.usuario.Utils.UtilsKt.setPriceSharedDiscount;
import static com.evans.technologies.usuario.Utils.UtilsKt.setViajeId;
import static com.evans.technologies.usuario.Utils.UtilsKt.toastLong;
import static com.evans.technologies.usuario.Utils.constans.AppConstants.SEND_NOTIFICATION_NOTIFICAR_LLEGADA;
import static com.evans.technologies.usuario.Utils.constans.AppConstants.SEND_NOTIFICATION_PAGO_EXITOSO;
import static com.evans.technologies.usuario.Utils.constans.AppConstants.SEND_NOTIFICATION_VIAJE_ACEPTADO;
import static com.evans.technologies.usuario.Utils.constans.AppConstants.SEND_NOTIFICATION_VIAJE_CANCELADO;
import static com.evans.technologies.usuario.Utils.constans.AppConstants.SEND_NOTIFICATION_VIAJE_INICIADO;
import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;

/**
 * A simple {@link Fragment} subclass.
 */
public class mapaInicio extends Fragment implements OnMapReadyCallback, View.OnClickListener, PermissionsListener {

    private static final String TAG = "Mapa Inicio";

    //Bots
    Marker bot1;
    Marker bot2;
    Marker bot3;
    Marker bot4;
    final int GPS = 51;
    private  String token;
    //Datos de lpos Precios

    boolean camera=false;
    //Bindeos Mapa principal//////////////////////////////////////////////////////////////////
    View view;
    ImageView gps;
    ImageView marcador;
    //Bindeos dialog destinos//////////////////////////////////////////////////////////////////

    @BindView(R.id.fmi_time)
    TextView fmi_time;
    @BindView(R.id.fmi_progressbar)
    ProgressBar progressBar;
    @BindView(R.id.mapa_recoger_cliente)
    View destinos_dialog;
    @BindView(R.id.dodpm_imgbtn_status_dest)
    ImageButton imgbtn_dest;
    @BindView(R.id.dodpm_imgbtn_status_origin)
    ImageButton imgbtn_origin;
    @BindView(R.id.dodpm_edtxt_origin)
    EditText edtxt_origin;
    @BindView(R.id.dodpm_edtxt_dest)
    EditText edtxt_dest;
    //  @BindView(R.id.dialog_fin_viaje_spinner)
    // Spinner dfv_spinner;


    @BindView(R.id.dialog_precio_img_btn_atras)
    ImageButton dialog_precio_img_btn_atras;
    CountDownTimer countDownTimer;
    Runnable tarea;
    boolean uno=true;
    @SuppressLint("NewApi")
    //Bindeos Dialog Precio///////////////////////////////////////////////////////////////////////
            String segundos;
    //markers
    Geocoder geo;
    Marker origen;
    Marker destino;
    FusedLocationProviderClient fusedLocationProviderClient; //Ultima Ubicacion
    Location location;
    LocationCallback locationCallback; //ACtualizar posicion
    LatLng dato;
    DatabaseReference refConexionDriverCoor;
    List<Address> adres;
    ValueEventListener conexionDriver;
    //Geo Fire
    String id;
    //Line
    LinearLayout linearLayout;
    //Mapa Viajes
    @BindView(R.id.mapa_precios)
    View  mapa_precios;
    @BindView(R.id.dialog_precio_select)
    Button dialog_precio_select;
    @BindView(R.id.dialog_precio_text_view_precio)
    TextView precio;
    @BindView(R.id.dialog_precio_spinner)
    Spinner spinner;
    //Boton View Comnet
    @BindView(R.id.crear_comentario_publicar)
    Button publicar_comentario;
    @BindView(R.id.crear_comentario_rating)
    RatingBar publicar_rating_rating;
    @BindView(R.id.crear_comentario_comentario)
    EditText publicar_edt_comentario;
    @BindView(R.id.dialog_fin_viaje_imgbtn_message_notify)
    ImageView dialog_fin_viaje_imgbtn_message_notify;
    BottomSheetBehavior bottomSheetBehavior;
    FloatingActionButton button_coment_hide;
    //Viajes transcurso
    @BindView(R.id.transcurso_viaje)
    View dialog_transcurso_viaje;
    @BindView(R.id.dialog_transcurso_destino_txt_name)
    TextView dialog_transcurso_txt_name;
    @BindView(R.id.dialog_transcurso_destino_txt_placa)
    TextView dialog_transcurso_txt_placa;
    @BindView(R.id.dialog_transcurso_destino_txt_marca)
    TextView dialog_transcurso_txt_marca;
    @BindView(R.id.dialog_transcurso_destino_txt_color)
    TextView dialog_transcurso_txt_color;
    @BindView(R.id.dialog_transcurso_destino_img_photo)
    ImageView dialog_transcurso_iv_photo;
    @BindView(R.id.dialog_transcurso_destino_ib_chat)
    ImageButton dialog_transcurso_ib_chat;
    @BindView(R.id.dialog_transcurso_destino_btn_cancelar)
    Button dialog_transcurso_btn_cancel;
    //Precio
    //Fin viaje
    @BindView(R.id.dialog_fin_viaje_txt_precio)
    TextView dialog_fin_viaje_txt_precio;
    @BindView(R.id.dialog_fin_viaje_txt_precio_coupon)
    TextView dialog_fin_viaje_txt_precio_coupon;
    @BindView(R.id.fin_viaje)
    View dialog_fin_viaje_dialog;
    @BindView(R.id.dialog_fin_viaje_txt_origen)
    TextView dialog_fin_viaje_txt_origen;
    @BindView(R.id.dialog_fin_viaje_txt_destino)
    TextView dialog_fin_viaje_txt_destino;
    @BindView(R.id.dialog_fin_viaje_txt_titulo)
    TextView dialog_fin_viaje_txt_titulo;
    @BindView(R.id.dialog_fin_viaje_imgbtn_settings)
    ImageButton dialog_fin_viaje_imgbtn_settings;
    @BindView(R.id.dialog_fin_viaje_imgbtn_message)
    ImageButton dialog_fin_viaje_imgbtn_message;
    @BindView(R.id.dialog_fin_viaje_imgbtn_delete)
    ImageButton dialog_fin_viaje_imgbtn_delete;

    @BindView(R.id.dialog_fin_viaje_btn_aceptar)
    Button dialog_fin_viaje_btn_aceptar;
    Marker DriverOptions;
    //Place autocomplete Fragment
    /*
    @BindView(R.id.autocomplete_frament_text)
    PlaceAutocompleteFragment autocomplete_frament_text;
    autocomplete_frament_text.setOnPlaceSelectedListener

    destination= plac.getAdrdreess().toString
    */
    int inicio_viaje;
    //Mapbox
    private MapboxMap mapboxMap;
    // variables for adding location layer
    private PermissionsManager permissionsManager;
    private LocationComponent locationComponent;
    private DirectionsRoute currentRoute;
    private List<Point> routeCoordinateList;
    private List<Point> routeCoordinateList2;
    private List<Point> routeCoordinateList3;
    private List<Point> routeCoordinateList4;
    private List<Point> markerLinePointList = new ArrayList<>();
    private GeoJsonSource pointSource;
    private Animator currentAnimator;
    private int routeIndex;
    // private GeoJsonSource lineSource;

    private NavigationMapRoute navigationMapRoute;
    private MapView mapView;
    private Geocoder geocoder;
    private List<Address> address;
    //Mapbox
    int valor=1;
    private static final String DOT_SOURCE_ID1 = "dot-source-id1";
    private int count = 0;
    private Handler handler;
    Runnable cordenadasBots_Runable;
    Handler coordenadasBots=new Handler();
    Handler coordenadasDriver=new Handler();
    Handler handler_internet = new Handler();
    final int  TIEMPO=4000;

    int index,next;
    LatLng startPosition,endPosition;
    //Dta
    String startAdress,endAdress,travelPrecio;
    BroadcastReceiver receiver;
    SharedPreferences datadriver;
    SharedPreferences prefs;
    IconFactory iconFactory;
    @BindView(R.id.dialog_no_connect_internet)
    View dialog_no_connect_internet;
    LatLng actualiza_cada_cierto_tiempo_Coordenadas=null;

    updateListenerNotifications comunicateFrag;
    Runnable cordenadasDriver_Runable;
    @BindView(R.id.search_imagen)
    View search_imagen;
    @BindView(R.id.mi_imgbtn_next_step)
    Button mi_imgbtn_next_step;

    Point originPoint ;
    Point destinationPoint ;
    private Boolean status_btn_origin=false,status_btn_dest=false;
    final int imagenes_Spinner[]={R.drawable.money,R.drawable.sale};
    final String nombres_Spinner[]={"Pago en efectivo","Pago con cupón"};
    public mapaInicio(String id,String token) {
        this.id=id;
        this.token=token;
    }
    public mapaInicio() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Mapbox.getInstance(getActivity(), getString(R.string.access_token));
        // Inflate the layout for this fragment
        //

        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        datadriver = getContext().getSharedPreferences("datadriver", Context.MODE_PRIVATE);
        prefs = getContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setClaseActual(prefs,new mapaInicio().toString());
        view = inflater.inflate(R.layout.fragment_mapa_inicio, container, false);
        try{
            View view = getActivity().getCurrentFocus();
            view.clearFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE) ;
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }catch (Exception e){

        }
        mapView = view.findViewById(R.id.mapView);
        ButterKnife.bind(this,view);
        mi_imgbtn_next_step.setOnClickListener(this);

        iconFactory = IconFactory.getInstance(getContext());
        if (mapView != null) {
            mapView.onCreate( savedInstanceState );
            mapView.onResume();
            mapView.getMapAsync( this );
        }
        dialog_fin_viaje_imgbtn_message.setOnClickListener(this);
        dialog_fin_viaje_imgbtn_delete.setOnClickListener(this);
        dialog_precio_select.setOnClickListener(this);
        gps = view.findViewById(R.id.mapa_icon_gps);
        marcador=view.findViewById(R.id.mapa_marker_center);
        //bindeos dialog pedido

        imgbtn_dest.setOnClickListener(this);
        imgbtn_origin.setOnClickListener(this);
        edtxt_dest.setOnClickListener(this);
        edtxt_origin.setOnClickListener(this);
        dialog_transcurso_ib_chat.setOnClickListener(this);
        //bindeos dialog precio




        //temporal=evanscarga;
        ////////////////////////////////////////////////////////


        //Dialog trancurso
        dialog_transcurso_btn_cancel.setOnClickListener(this);
        //Dialog Fin de Viaje
        publicar_comentario.setOnClickListener(this);
        //DIalog comentarios
        linearLayout=(LinearLayout) view.findViewById( R.id.crear_comentario_layout_dialog ) ;
        bottomSheetBehavior= BottomSheetBehavior.from( linearLayout );

        button_coment_hide=(FloatingActionButton) view.findViewById( R.id.aic_button_comentar_hide);
        dialog_precio_img_btn_atras.setOnClickListener( this );
        button_coment_hide.setOnClickListener( this );
        bottomSheetBehavior.setBottomSheetCallback( new BottomSheetBehavior.BottomSheetCallback() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onStateChanged(@androidx.annotation.NonNull View view, int i) {
                switch (i)
                {
                    case BottomSheetBehavior.STATE_HIDDEN:
//                        linearLayout.setVisibility(View.GONE);
//                        bottomSheetBehavior.setState( BottomSheetBehavior.STATE_HIDDEN );
//                        destinos_dialog.setVisibility(View.VISIBLE);
//                        button_coment_hide.setVisibility( View.GONE );
//                        terminarTrip();
//                        Log.e("estado_delete","btn publicar");
//                        borrarSharedPreferencesDataDriver(1);
//                        primer_estado();
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        if (!(getApiWebVersion(prefs).equals(getVersionApp(getContext())))){
                            getViewUpdateVersion(getActivity(),getContext());
                        }
                        comunicateFrag.removeChatConexion();
                        refConexionDriverCoor.removeEventListener(conexionDriver);
                        linearLayout.setVisibility(View.GONE);
                        bottomSheetBehavior.setState( BottomSheetBehavior.STATE_HIDDEN );
                        destinos_dialog.setVisibility(View.VISIBLE);
                        button_coment_hide.setVisibility( View.GONE );
                        terminarTrip();
                        Log.e("estado_delete","btn publicar");
                        borrarSharedPreferencesDataDriver(1);
                        primer_estado();

                        // button_coment_collapse.setVisibility( View.VISIBLE );
                        break;
                }
            }

            @Override
            public void onSlide(@androidx.annotation.NonNull View view, float v) {

            }
        } );

        gpsEnable();
        dialog_fin_viaje_btn_aceptar.setOnClickListener(this);
        receiver = new BroadcastReceiver() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    String datassss=intent.getExtras().getString("data");

                    if (datassss.contains("{cronometroFinalizado}")){
                        search_imagen.setVisibility(View.GONE);
                        tercer_estado();

                        Call comando= RetrofitClient.getInstance().getApi().puStatusTrip(getViajeId(datadriver),true,false,false,false);
                        comando.enqueue(new Callback() {
                            @Override
                            public void onResponse(Call call, Response response) {;
                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {
                            }
                        });

                    }else {
                        try{

                        }catch (Exception e){

                        }
                        ejecutar_tarea_notificaciones_data(datassss);
                    }

                } catch (Exception e) {
                    Log.e("data recibida ",e.getMessage());
                    e.printStackTrace();
                }

            }
        };
        ((MainActivity) Objects.requireNonNull(getActivity())).updateApi(new ComunicateFrag.mapa_inicio() {
            @Override
            public void mensajeGet() {
                dialog_fin_viaje_imgbtn_message_notify.setVisibility(View.VISIBLE);
            }
        });
        // if (segundos!="0")

        comprobarStatusTrip();
        /*SpinnerAdapter dfv_spinner_customAdapter=new adapter_spinner_pay_tipe(getContext(),imagenes_Spinner,nombres_Spinner,R.color.white);
        dfv_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              //  ((TextView) parent.getChildAt(0)).setTextColor();
                ((TextView)view.findViewById(R.id.textView)).setTextColor(getContext().getColor(R.color.white));

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
                                            if (response.body().getCashPrice().equals(getPriceShared(datadriver))){
                                                Log.e("cupon",response.body().getCashPrice());
                                                toastLong(getActivity(),"NO HAY CUPONES DISPONIBLES");
                                                setPriceSharedDiscount(datadriver,"0.0");
                                                dfv_spinner.setSelection(0);

                                            }else{
                                                setPriceSharedDiscount(datadriver,Double.parseDouble(getPriceShared(datadriver))-Double.parseDouble(response.body().getCashPrice())+"");
                                                precio.setText("S./ "+  (Double.parseDouble(getPriceShared(datadriver))- Double.parseDouble(getPriceSharedDiscount(datadriver)))+"");
                                            }
                                        }else{

                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<getPrice> call, Throwable t) {
                                        toastLong(getActivity(),"NO HAY CUPONES DISPONIBLES");
                                    }
                                });

                            }else{
                                if (getPriceSharedDiscount(datadriver).contains("0.0")){
                                    toastLong(getActivity(),"NO HAY CUPONES DISPONIBLES");
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
        // dfv_spinner.setBackgroundColor(getContext().getColor(R.color.black));
        //  spinner.setOnItemSelectedListener(listener);
        // dfv_spinner.setAdapter(dfv_spinner_customAdapter);
        SpinnerAdapter spinner_customAdapter=new adapter_spinner_pay_tipe(getContext(),imagenes_Spinner,nombres_Spinner,R.color.black);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        precio.setText("S./ "+ getPriceShared(datadriver));
                        setPriceSharedDiscount(datadriver,"0.0");
                        break;
                    case 1:
                        if (!getAccountActivate(prefs)){
                            spinner.setSelection(0);
                            spinner.setSelection(0,true);

                            if (DialogCreate("Para usar cupones es necesario activar su cuenta \n ¿Desea activar su cuenta?")){

                            }

                        }else{
                            if (getPriceSharedDiscount(datadriver).equals("0.0")){
                                Log.e("cupon",getUserId_Prefs(prefs)+"     "+getPriceShared(datadriver));
                                Call<getPrice> discount=RetrofitClient.getInstance().getApi().getPriceDiscount(getUserId_Prefs(prefs),getPriceShared(datadriver));
                                discount.enqueue(new Callback<getPrice>() {
                                    @Override
                                    public void onResponse(Call<getPrice> call, Response<getPrice> response) {
                                        if (response.isSuccessful()){
                                            if (response.body().getCashPrice().equals(getPriceShared(datadriver))){
                                                Log.e("cupon",response.body().getCashPrice());
                                                toastLong(getActivity(),"NO HAY CUPONES DISPONIBLES");
                                                setPriceSharedDiscount(datadriver,"0.0");
                                                spinner.setSelection(0);
                                                if (!getAccountActivate(prefs)){
                                                    if (DialogCreate("Para obtener y usar cupones es necesario activar su cuenta \n ¿Desea activar su cuenta?")){
                                                        enviarMensajeCorreo();
                                                    }
                                                }
                                            }else{
                                                setPriceSharedDiscount(datadriver,Double.parseDouble(getPriceShared(datadriver))-Double.parseDouble(response.body().getCashPrice())+"");
                                                precio.setText("S./ "+   (Double.parseDouble(getPriceShared(datadriver))- Double.parseDouble(getPriceSharedDiscount(datadriver)))+"" );
                                            }
                                        }else{

                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<getPrice> call, Throwable t) {
                                        toastLong(getActivity(),"NO HAY CUPONES DISPONIBLES");
                                    }
                                });

                            }else{
                                if (getPriceSharedDiscount(datadriver).contains("nulo")){
                                    toastLong(getActivity(),"NO HAY CUPONES DISPONIBLES");
                                    spinner.setSelection(0);
                                    if (!getAccountActivate(prefs)){
                                        if (DialogCreate("Para obtener y usar cupones es necesario activar su cuenta \n ¿Desea activar su cuenta?")){
                                            enviarMensajeCorreo();
                                        }
                                    }
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
        });

        spinner.setAdapter(spinner_customAdapter);
        return view;
    }

    private void enviarMensajeCorreo() {
        Call<user> enviarCode = RetrofitClient.getInstance().getApi().enviarCorreo_validate(
                getUserEmail(prefs));
        enviarCode.enqueue(new Callback<user>() {
            @Override
            public void onResponse(Call<user> call, Response<user> response) {
                toastLong(getActivity(), "error"+response.code());
                if( response.isSuccessful()){
                    toastLong(getActivity(),"Se envio un mensaje a tu correo");
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    manager.beginTransaction().replace(
                            R.id.main_layout_change_fragment,
                            new set_codigo(true, false)
                    ).commit();
                }
            }

            @Override
            public void onFailure(Call<user> call, Throwable t) {
                toastLong(getActivity(), "error   f"+t.getMessage());
            }
        });
    }

    private void comprobarStatusTrip() {
        if (!getViajeId(datadriver).equals("nulo")){
            Call<infoDriver> statusTrip=RetrofitClient.getInstance().getApi().getStatusTrip(getViajeId(datadriver));
            statusTrip.enqueue(new Callback<infoDriver>() {
                @Override
                public void onResponse(Call<infoDriver> call, Response<infoDriver> response) {
                    try{
                        if ( !(response.body().isOk())){
                            borrarSharedPreferencesDataDriver(1);
                            primer_estado();
                            originPoint=null;
                            destinationPoint=null;

                            setChatJson(datadriver,"nulo");
                         /*   Intent intent = new Intent("subsUnsubs");
                            intent.putExtra("subs", "chat");
                            intent.putExtra("subsUnsubs",false);
                            LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(getContext());
                            broadcaster.sendBroadcast(intent);*/
                        }
                    }catch(Exception e){

                    }
                }

                @Override
                public void onFailure(Call<infoDriver> call, Throwable t) {

                }
            });

        }

    }
    @Override
    public void onAttach(@NonNull Context context) {
        try {
            comunicateFrag = (updateListenerNotifications) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnFragmentInteractionListener");
        }
        super.onAttach(context);
    }
    private void init_bot_driver(String ruta){
        Log.e("rutabot",ruta);

        // new LoadGeoJson(mapaInicio.this,ruta).execute();
        /*mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                DirectionsRoute route=DirectionsRoute.fromJson(ruta);
                initData(style,FeatureCollection.fromFeature(
                        Feature.fromGeometry(LineString.fromPolyline(route.geometry(), PRECISION_6))));
            }
        });*/

        //
        //List<Point> routeCoordinateList=PolylineUtils.decode((route.geometry()),6);
        //initRunnable(routeCoordinateList);
    }
    private void initData(Style fullyLoadedStyle, @NonNull FeatureCollection featureCollection) {
        if (featureCollection.features() != null) {
            LineString lineString = ((LineString) featureCollection.features().get(0).geometry());
            if (lineString != null) {
                routeCoordinateList = lineString.coordinates();
                initSources(fullyLoadedStyle, featureCollection);
                initSymbolLayer(fullyLoadedStyle);
                //initDotLinePath(fullyLoadedStyle);
                animate();
            }
        }
    }

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
                    iconFactory = IconFactory.getInstance(getContext());
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
    private void animate() {

        if (getEstadoView(datadriver)<3){
            if ((routeCoordinateList.size() - 1 > routeIndex)) {
                Point indexPoint = routeCoordinateList.get(routeIndex);
                Point newPoint = Point.fromLngLat(indexPoint.longitude(), indexPoint.latitude());
                currentAnimator = createLatLngAnimator(indexPoint, newPoint);
                currentAnimator.start();
                routeIndex++;
            }else{
                mapboxMap.getStyle(new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        style.removeSource(DOT_SOURCE_ID1);
                        style.removeLayer("symbol-layer-id");
                        getRouteString();

                    }
                });
            }
        }else{
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    style.removeSource(DOT_SOURCE_ID1);
                    style.removeLayer("symbol-layer-id");
                }
            });
        }

    }

    private static class PointEvaluator implements TypeEvaluator<Point> {

        @Override
        public Point evaluate(float fraction, Point startValue, Point endValue) {
            return Point.fromLngLat(
                    startValue.longitude() + ((endValue.longitude() - startValue.longitude()) * fraction),
                    startValue.latitude() + ((endValue.latitude() - startValue.latitude()) * fraction)
            );
        }
    }

    private Animator createLatLngAnimator(Point currentPosition, Point targetPosition) {
        ValueAnimator latLngAnimator = ValueAnimator.ofObject(new PointEvaluator(), currentPosition, targetPosition);
        // latLngAnimator.setDuration((long) TurfMeasurement.distance(currentPosition, targetPosition, "meters"));
        latLngAnimator.setDuration(5000);

        latLngAnimator.setInterpolator(new LinearInterpolator());
        latLngAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animate();
            }
        });
        latLngAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Point point = (Point) animation.getAnimatedValue();
                pointSource.setGeoJson(point);
                markerLinePointList.add(point);
                // lineSource.setGeoJson(Feature.fromGeometry(LineString.fromLngLats(markerLinePointList)));
            }
        });

        return latLngAnimator;
    }
    private void initSources(@NonNull Style loadedMapStyle, @NonNull FeatureCollection featureCollection) {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                if (style.getSource(DOT_SOURCE_ID1)==null){
                    loadedMapStyle.addSource(pointSource = new GeoJsonSource(DOT_SOURCE_ID1, featureCollection));
                }
            }
        });


        //  loadedMapStyle.addSource(lineSource = new GeoJsonSource(LINE_SOURCE_ID));
    }
    private void initSymbolLayer(@NonNull Style loadedMapStyle) {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                if (style.getLayer("symbol-layer-id")==null){
                    Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_directions_car_black_24dp, null);
                    Bitmap mBitmap = BitmapUtils.getBitmapFromDrawable(drawable);
                    loadedMapStyle.addImage("moving-red-marker", mBitmap);
                    loadedMapStyle.addLayer(new SymbolLayer("symbol-layer-id", DOT_SOURCE_ID1).withProperties(
                            iconImage("moving-red-marker"),
                            iconSize(1f),
                            iconOffset(new Float[] {5f, 0f}),
                            iconIgnorePlacement(true),
                            iconAllowOverlap(true)
                    ));

                }
            }
        });

    }

    /**
     * Add the LineLayer for the marker icon's travel route. Adding it under the "road-label" layer, so that the
     * this LineLayer doesn't block the street name.
     */

    private void ejecutar_tarea_notificaciones_data(String datassss) {
        Log.e("data_recibida"," mapa inicio"+datassss);
        Gson gson = new Gson();
        data topic;
        try{

            topic = gson.fromJson(datassss, data.class);
            if (topic.getResponse().contains(SEND_NOTIFICATION_VIAJE_ACEPTADO)){
                if (getViajeId(datadriver).equals("nulo")){
                    setDriverId(datadriver,topic.getDriverId());
                    cancel_viaje_noti();
                }else{
                    // if (countDownTimer!=null)
                    //    countDownTimer.onFinish();
                    getContext().stopService(new Intent(getContext(), cronometro.class));


                    search_imagen.setVisibility(View.GONE);
                    //fmi_time.setVisibility(View.GONE);
                    Call<infoDriver>  getInfoDriver = RetrofitClient.getInstance()
                            .getApi().getInfoDriver(topic.getDriverId());
                    getInfoDriver.enqueue(new Callback<infoDriver>() {
                        @Override
                        public void onResponse(Call<infoDriver> call, Response<infoDriver> response) {
                            if (response.isSuccessful()){

                                setInfoDriver(datadriver,"https://evans-img.s3.us-east-2.amazonaws.com/"+response.body().getInformation().getDriverImg(),response.body().getInformation().getSurname(),response.body().getInformation().getName(),
                                        response.body().getInformation().getLicenseCar(),response.body().getInformation().getBrandCar(), response.body().getInformation().getModelCar(),
                                        response.body().getInformation().getColorCar());
                                setDriverId(datadriver,topic.getDriverId());
                                setEstadoViews(datadriver,5);
                                llaveChat(datadriver, topic.getChatId());
                                comunicateFrag.createConexionChat();
                           /* Intent intent = new Intent("subsUnsubs");
                            intent.putExtra("subs", "chat");
                            intent.putExtra("subsUnsubs",true);
                            LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(getContext());
                            broadcaster.sendBroadcast(intent);*/
                                //ejecutarTarea_ActualizarCordenadasDriver();
                                cargarPosicionDriver();
                                quinto_estado();

                                mapboxMap.getStyle(new Style.OnStyleLoaded() {
                                    @Override
                                    public void onStyleLoaded(@NonNull Style style) {
                                        style.removeSource(DOT_SOURCE_ID1);
                                        style.removeLayer("symbol-layer-id");
                                    }
                                });
                            }
                            else{
                                toastLong(getActivity(), "El conductor no tiene informacion, se cancelara el viaje");

                            }
                            Log.e("error Retrofit22",response.code()+"");
                        }

                        @Override
                        public void onFailure(Call<infoDriver> call, Throwable t) {

                        }
                    });
                }
            }else if (topic.getResponse().contains(SEND_NOTIFICATION_NOTIFICAR_LLEGADA)){
                sexto_estado();
                setEstadoViews(datadriver,6);


            }else if (topic.getResponse().contains(SEND_NOTIFICATION_VIAJE_INICIADO)){
                setEstadoViews(datadriver,8);
                octavo_estado();
            }else if (topic.getResponse().contains(SEND_NOTIFICATION_PAGO_EXITOSO)){
                comunicateFrag.removeChatConexion();
                setEstadoViews(datadriver,10);
                decimo_estado();
                originPoint=null;
                destinationPoint=null;
            }else if(topic.getResponse().equals(SEND_NOTIFICATION_VIAJE_CANCELADO)){
                comunicateFrag.removeChatConexion();
                refConexionDriverCoor.removeEventListener(conexionDriver);
                Log.e("estado_delete","cancelado estado");
                if (DriverOptions!=null)
                    DriverOptions.remove();
                setChatJson(datadriver,"nulo");
               /* Intent intent = new Intent("subsUnsubs");
                intent.putExtra("subs", "chat");
                intent.putExtra("subsUnsubs",false);
                LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(getContext());
                broadcaster.sendBroadcast(intent);*/
                borrarSharedPreferencesDataDriver(1);
                originPoint=null;
                destinationPoint=null;
                primer_estado();

            }else{

            }
        }catch (Exception e){
            Log.e("errorconvertgson"," mapa inicio"+datassss);
        }

        //  if (topic.getTitle()!=null&&!(topic.getTitle().equals(""))&&
        //         topic.getBody()!=null&&!(topic.getBody().equals(""))) {
        //  sendNotification(getContext(), topic.getTitle().replace("-", " "), topic.getBody().replace("-", " "));
        //  }

    }

    private void cargarPosicionDriver() {
        refConexionDriverCoor= FirebaseDatabase.getInstance().getReference().child("coordenadaUpdate").child(getDriverId(datadriver));
        conexionDriver=refConexionDriverCoor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("updateCoor"," entro  "+dataSnapshot.getRef());
                if(dataSnapshot.exists()){
                    config configuraciones = dataSnapshot.getValue( config.class);
                    Log.e("updateCoor",configuraciones.getLat()+"   "+configuraciones.getLog());
                    if (DriverOptions!=null)
                        DriverOptions.remove();
                    DriverOptions=mapboxMap.addMarker(new MarkerOptions()
                            .setIcon(iconFactory.fromResource(R.drawable.car64dp))
                            .setPosition(new LatLng(configuraciones.getLat(),configuraciones.getLog()))
                            .title("Driver"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GPS:
                if (resultCode == RESULT_OK) {

                    getDeviceLocation();

                } else {
                    Toast.makeText(getContext(), "Error de gps", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private void getRouteString(){
        Point origin = Point.fromLngLat( Double.parseDouble(ramdomNum(false)),Double.parseDouble(ramdomNum(true)));
        Point destination = Point.fromLngLat( Double.parseDouble(ramdomNum(false)),Double.parseDouble(ramdomNum(true)));

        NavigationRoute.builder(getContext())
                .accessToken(getString(R.string.access_token))
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        if (response.isSuccessful()){
                            if (response!=null){
                                if (response.body().routes().size() < 1){

                                    toastLong(getActivity(),"No se encontro la direccion");
                                }else{
                                    FeatureCollection f1= FeatureCollection.fromFeature(
                                            Feature.fromGeometry(LineString.fromPolyline(response.body().routes().get(0).geometry(), PRECISION_6)));
                                    // LineString ls1 = ((LineString) f1.features().get(0).geometry());
                                    mapboxMap.getStyle(new Style.OnStyleLoaded() {
                                        @Override
                                        public void onStyleLoaded(@NonNull Style style) {
                                            initData(style,f1);
                                        }
                                    });

                                    Log.e("ruta", "currentRoute: " + response.body().routes().get(0));

                                }
                            }else{

                                toastLong(getActivity(),"No se encontro la direccion, compruebe el token");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });



        //button.setVisibility(View.VISIBLE);
    }
    private void getRoute(Point origin, Point destination) {
        Log.e("origin",origin+ "  ");
        Log.e("destination",destination +"  ");
        NavigationRoute.builder(getContext())
                .accessToken(getString(R.string.access_token))
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {

                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;
                        }

                        Log.d(TAG, "currentRoute"+  PolylineUtils.decode(response.body().routes().get(0).geometry(),6));
                        currentRoute = response.body().routes().get(0);
                        //new LoadGeoJson(mapaInicio.this).execute();
                        Log.d(TAG, "currentRoute: " + response.body());
                        Log.d(TAG, "currentRoute: " + response.body().routes().get(0));
                        Log.d(TAG, "currentRoute"+ response.body().routes().get(0).geometry());

                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            try{
                                navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.navegation);
                            }catch (Exception e){

                            }

                        }
                        try{
                            navigationMapRoute.addRoute(currentRoute);
                            mapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(
                                    new LatLngBounds.Builder()
                                            .include(new LatLng(origin.latitude(), origin.longitude()))
                                            .include(new LatLng(destination.latitude(), destination.longitude()))
                                            .build(), 50), 3);
                        }catch (Exception e){

                        }
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
        //button.setVisibility(View.VISIBLE);
    }
    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        try {
            Location lastKnownLocation = mapboxMap.getLocationComponent().getLastKnownLocation();

            Double log=lastKnownLocation.getLongitude();
            Double lat= lastKnownLocation.getLatitude();
            CameraPosition position = new CameraPosition.Builder()
                    .target(new com.mapbox.mapboxsdk.geometry.LatLng(lat,log )) // Sets the new camera position
                    .zoom(15)
                    .build(); // Creates a CameraPosition from the builder

            mapboxMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(position), 1000);

        }catch (Exception e){

            Log.e("deviceLocation", e.getMessage());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((receiver),
                new IntentFilter("clase")
        );
        mapView.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        //  if (handler != null && runnable != null) {
        //     handler.post(runnable);
        // }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();

        // if (handler != null && runnable != null) {
        //    handler.removeCallbacksAndMessages(null);
        // }
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
        super.onStop();
        mapView.onStop();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(Style style) {

        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(getContext())) {

            // Get an instance of the component
            locationComponent = mapboxMap.getLocationComponent();

            // Activate with a built LocationComponentActivationOptions object
            locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(getContext(), style).build());

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);

        } else {

            permissionsManager = new PermissionsManager(this);

            permissionsManager.requestLocationPermissions(getActivity());

        }
    }
    private void getDataActivityOrigin_Dest(){

    }
    private void gpsEnable() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient settingsClient = LocationServices.getSettingsClient(getActivity());
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());
        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                    try {

                        resolvableApiException.startResolutionForResult(getActivity(), GPS);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"RestrictedApi", "ResourceAsColor"})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mapa_icon_gps:
                try {
                    Location lastKnownLocation = mapboxMap.getLocationComponent().getLastKnownLocation();

                    Double log=lastKnownLocation.getLongitude();
                    Double lat= lastKnownLocation.getLatitude();
                    CameraPosition position = new CameraPosition.Builder()
                            .target(new com.mapbox.mapboxsdk.geometry.LatLng(lat,log )) // Sets the new camera position
                            .zoom(15)
                            .build(); // Creates a CameraPosition from the builder

                    mapboxMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(position), 1000);
                }catch (Exception e){

                    Log.e("deviceLocation", e.getMessage());
                    Intent intent= new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                }


                break;
            case R.id.mi_imgbtn_next_step:
                progressBar.setVisibility(View.VISIBLE);
                funcionObtenerPrecio();
                break;
            case  R.id.dodpm_imgbtn_status_origin:

                imgbtn_origin.setSelected(!v.isSelected());
                if (imgbtn_origin.isSelected()){
                    LatLng center = mapboxMap.getCameraPosition().target;
                    status_btn_origin=true;
                    try {
                        adres = geo.getFromLocation(center.getLatitude(),center.getLongitude(),1);
                        String[] direccion3;
                        direccion3 = (adres.get(0).getAddressLine(0)).split(",");
                        edtxt_origin.setText(direccion3[0]);
                        edtxt_origin.setEnabled(false);
                        edtxt_origin.setTextColor(getContext().getColor(R.color.plomo));
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("recoger View",e.getMessage()+"");
                    }
                    setOrigen(datadriver,center.getLatitude()+"",center.getLongitude()+"");
                    origen=mapboxMap.addMarker(new MarkerOptions()
                            .setIcon(iconFactory.fromResource(R.drawable.logo33))
                            .setPosition(new LatLng(Double.parseDouble(getOrigenLat(datadriver)),Double.parseDouble(getOrigenLong(datadriver))))
                            .title("Origen"));
                    if (status_btn_dest){
                        mi_imgbtn_next_step.setEnabled(true);

                        mi_imgbtn_next_step.setBackgroundColor(getActivity().getResources().getColor(R.color.black));
                        mi_imgbtn_next_step.setText("CALCULAR TARIFA");

                    }
                } else{
                    mi_imgbtn_next_step.setEnabled(false);
                    mi_imgbtn_next_step.setBackgroundColor(Color.GRAY);
                    mi_imgbtn_next_step.setText("CALCULAR TARIFA");
                    edtxt_origin.setEnabled(true);
                    edtxt_origin.setTextColor(getContext().getColor(R.color.black));
                    status_btn_origin=false;
                    origen.remove();
                }


                break;
            case R.id.dodpm_edtxt_origin:
            case R.id.dodpm_edtxt_dest:
                //   startActivity(new Intent(getContext(), origin_dest_point_search.class));
                break;
            case  R.id.dodpm_imgbtn_status_dest:
                imgbtn_dest.setSelected(!v.isSelected());
                if (imgbtn_dest.isSelected()){
                    LatLng center = mapboxMap.getCameraPosition().target;
                    status_btn_dest=true;
                    try {
                        adres = geo.getFromLocation(center.getLatitude(),center.getLongitude(),1);
                        String[] direccion3;
                        direccion3 = (adres.get(0).getAddressLine(0)).split(",");
                        edtxt_dest.setText(direccion3[0]);
                        edtxt_dest.setEnabled(false);
                        edtxt_dest.setTextColor(getContext().getColor(R.color.plomo));

                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("recoger View",e.getMessage()+"");
                    }
                    setDestino(datadriver,center.getLatitude()+"",center.getLongitude()+"");
                    destino=mapboxMap.addMarker(new MarkerOptions()
                            .setIcon(iconFactory.fromResource(R.drawable.logo22))
                            .setPosition(new LatLng(Double.parseDouble(getDestinoLat(datadriver)),Double.parseDouble(getDestinoLong(datadriver))))
                            .title("Destino"));

                    if (status_btn_origin){
                        mi_imgbtn_next_step.setEnabled(true);
                        mi_imgbtn_next_step.setBackgroundColor(getActivity().getResources().getColor(R.color.black));
                        mi_imgbtn_next_step.setText("CALCULAR TARIFA");

                    }
                } else{
                    mi_imgbtn_next_step.setEnabled(false);
                    mi_imgbtn_next_step.setBackgroundColor(Color.GRAY);
                    mi_imgbtn_next_step.setText("CALCULAR TARIFA");
                    edtxt_dest.setEnabled(true);
                    edtxt_dest.setTextColor(getContext().getColor(R.color.black));
                    status_btn_dest=false;
                    destino.remove();
                }





                break;

            case R.id.dialog_precio_select:

                progressBar.setVisibility(View.VISIBLE);
                dialog_precio_select.setEnabled(false);
                Date date = new Date();
                DateFormat hourFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Log.e("calles",'\n'+getUserId_Prefs(prefs)+'\n'+getOrigenLat(datadriver)+'\n'+getOrigenLong(datadriver)
                        +'\n'+getDestinoLat(datadriver)+'\n'+getDestinoLong(datadriver)+'\n'+
                        getStartAddress(datadriver).replace(" ","-")+'\n'+getEndAddress(datadriver).replace(" ","-")
                        +'\n'+hourFormat.format(date)+'\n'+getPriceShared(datadriver)+'\n'+"Puno");

                Call<user> call = RetrofitClient.getInstance()
                        .getApi().requestDriver(getUserId_Prefs(prefs),getOrigenLat(datadriver),getOrigenLong(datadriver)
                                ,getDestinoLat(datadriver),getDestinoLong(datadriver),getStartAddress(datadriver).replace(" ","-"),getEndAddress(datadriver).replace(" ","-"),hourFormat.format(date)+"",
                                ( Double.parseDouble(getPriceShared(datadriver))-Double.parseDouble(getPriceSharedDiscount(datadriver)))+"",getPriceSharedDiscount(datadriver),"PUNO");
                call.enqueue(new Callback<user>() {
                    @Override
                    public void onResponse(Call<user> call, Response<user> response) {
                        if(response.isSuccessful()){
                            setViajeId(datadriver,response.body().getViajeId());
                            //fmi_time.setVisibility(View.VISIBLE);
                            // ejecutarCronometro();
                            search_imagen.setVisibility(View.VISIBLE);
                            setEstadoViews(datadriver,4);
                            cuarto_estado();
                            change_priceSpinner();
                            progressBar.setVisibility(View.GONE);
                            dialog_precio_select.setText("Esperando evans");
                            try {
                                getContext().stopService(new Intent(getContext(), cronometro.class));
                            }catch (Exception e){

                            }
                            try {
                                getContext().startService(new Intent(getContext(), cronometro.class));
                            }catch (Exception e){

                            }
                            Log.e("paso prueba","Felicidades"+response.body().getViajeId());

                        }else{
                            dialog_precio_select.setEnabled(true);
                            progressBar.setVisibility(View.GONE);
                            toastLong(getActivity(),"Las movilidades cercanas se encuentran ocupadas.");
                            Log.e("paso prueba",response.code()+" " +response.message());
                        }

                    }

                    @Override
                    public void onFailure(Call<user> call, Throwable t) {
                        Log.e("errorFaire",t.getMessage());
                        dialog_precio_select.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        toastLong(getActivity(),"Solicite nuevamente el viaje");
                    }
                });



                break;

            case R.id.dialog_fin_viaje_btn_aceptar:

                switch (getEstadoView(datadriver)){
                    case 6:
                        Call<user> llamada = RetrofitClient.getInstance().getApi()
                                .userTOdriver(getUserId_Prefs(prefs),getDriverId(datadriver),"Pasajero-Esperando","Pasajero-Subio-abordo","subioabordo");
                        llamada.enqueue(new Callback<user>() {
                            @Override
                            public void onResponse(Call<user> call, Response<user> response) {
                                if (response.isSuccessful()){
                                    setEstadoViews(datadriver,7);
                                    septimo_estado();
                                }else{
                                    Log.e("dialog_fin_viaje",response.message()+response.code());
                                }
                            }

                            @Override
                            public void onFailure(Call<user> call, Throwable t) {

                            }
                        });
                        break;
                    case 8:

                        Call<user> termianrViaje = RetrofitClient.getInstance().getApi()
                                .userTOdriver(getUserId_Prefs(prefs),getDriverId(datadriver),( Double.parseDouble(getPriceShared(datadriver))-Double.parseDouble(getPriceSharedDiscount(datadriver)))+"",getPriceSharedDiscount(datadriver),"viajeterminado");
                        termianrViaje.enqueue(new Callback<user>() {
                            @Override
                            public void onResponse(Call<user> call, Response<user> response) {
                                if (response.isSuccessful()) {
                                    //  change_priceSpinner();
                                    setEstadoViews(datadriver,9);
                                    noveno_estado();
                                }
                            }

                            @Override
                            public void onFailure(Call<user> call, Throwable t) {

                            }
                        });


                        break;
                    default:

                        break;
                }
                break;
            case R.id.aic_button_comentar_hide:
                if (!(getApiWebVersion(prefs).equals(getVersionApp(getContext())))){
                    getViewUpdateVersion(getActivity(),getContext());
                }
                comunicateFrag.removeChatConexion();
                linearLayout.setVisibility(View.GONE);
                bottomSheetBehavior.setState( BottomSheetBehavior.STATE_HIDDEN );
                destinos_dialog.setVisibility(View.VISIBLE);
                button_coment_hide.setVisibility( View.GONE );
                terminarTrip();
                Log.e("estado_delete","btn publicar");
                borrarSharedPreferencesDataDriver(1);
                primer_estado();
                break;
            case R.id.crear_comentario_publicar:
                refConexionDriverCoor.removeEventListener(conexionDriver);
                if (publicar_rating_rating!=null||publicar_rating_rating.getRating()!=0.0){
                    Call comentar=RetrofitClient.getInstance().getApi().calificarDriver(getDriverId(datadriver),
                            publicar_edt_comentario.getText().toString(),
                            publicar_rating_rating.getRating()+"",getUserId_Prefs(prefs),getUserName(prefs));
                    comentar.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            Log.e("rating","enviado"+response.code()+"  "+publicar_rating_rating.getRating());
                            if (response.isSuccessful()){
                                comunicateFrag.removeChatConexion();
                                if (!(getApiWebVersion(prefs).equals(getVersionApp(getContext())))){
                                    getViewUpdateVersion(getActivity(),getContext());
                                }
                                linearLayout.setVisibility(View.GONE);
                                bottomSheetBehavior.setState( BottomSheetBehavior.STATE_HIDDEN );
                                destinos_dialog.setVisibility(View.VISIBLE);
                                button_coment_hide.setVisibility( View.GONE );
                                Log.e("estado_delete","btn publicar");
                                borrarSharedPreferencesDataDriver(1);
                                primer_estado();
                            }

                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {

                        }
                    });

                }

                break;


            case R.id.dialog_precio_img_btn_atras:
                Log.e("estado_delete","btn atras");
                Log.e("botonatras", getEstadoView(datadriver)+"");
                if (getViajeId(datadriver).equals("nulo")){
                    borrarSharedPreferencesDataDriver(1);
                    primer_estado();
                }else{
                    Call comando=RetrofitClient.getInstance().getApi().puStatusTrip(getViajeId(datadriver),true,false,false,false);
                    comando.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {

                            borrarSharedPreferencesDataDriver(1);
                            primer_estado();
                            // if (countDownTimer!=null)
                            //    countDownTimer.onFinish();
                            getContext().stopService(new Intent(getContext(), cronometro.class));

                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {

                        }
                    });


                }
                originPoint=null;
                destinationPoint=null;






                break;

            case R.id.dialog_transcurso_destino_btn_cancelar:
            case R.id.dialog_fin_viaje_imgbtn_delete:
                if (DriverOptions!=null)
                    DriverOptions.remove();
                originPoint=null;
                destinationPoint=null;
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                Call comando=RetrofitClient.getInstance().getApi().puStatusTrip(getViajeId(datadriver),true,false,false,false);
                                comando.enqueue(new Callback() {
                                    @Override
                                    public void onResponse(Call call, Response response) {
                                        if (response.isSuccessful()){
                                            Log.e("Viaje aceptado",response.message());
                                            comunicateFrag.removeChatConexion();
                                            cancel_viaje_noti();
                                        }
                                        refConexionDriverCoor.removeEventListener(conexionDriver);
                                        Log.e("Viaje aceptado",response.code()+" "+getViajeId(datadriver)+" "+response.message());
                                    }

                                    @Override
                                    public void onFailure(Call call, Throwable t) {

                                    }
                                });


                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("¿Esta seguro de querer cancelar el viaje?").setPositiveButton("Si", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();


                break;
            case R.id.dialog_transcurso_destino_ib_chat:
            case R.id.dialog_fin_viaje_imgbtn_message:
                dialog_fin_viaje_imgbtn_message_notify.setVisibility(View.GONE);
                comunicateFrag.updateNotificatones(true);
                break;

        }
    }

    private void change_priceSpinner() {
        Log.e("discount",getPriceShared(datadriver)+ "   "+getPriceSharedDiscount(datadriver));
        Call<user > cambios=RetrofitClient.getInstance().getApi().changePriceTrip(getViajeId(datadriver),getPriceShared(datadriver),
                getPriceSharedDiscount(datadriver));
        cambios.enqueue(new Callback<user>() {
            @Override
            public void onResponse(Call<user> call, Response<user> response) {
                Log.e("cambiosP",response.code()+"");
                if (response.isSuccessful()){

                }
            }

            @Override
            public void onFailure(Call<user> call, Throwable t) {
                Log.e("cambiosP",t.getMessage()+"    error ");
            }
        });
    }

    private Boolean DialogCreate(String mensaje){
        final Boolean[] error = {false};
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        enviarMensajeCorreo();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        error[0] =false;
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(mensaje).setPositiveButton("Si", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
        return error[0] ;
    }
    private void terminarTrip() {
        Call terminado = RetrofitClient.getInstance().getApi()
                .puStatusTrip(getViajeId(datadriver),false,false,false,true);
        terminado.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    private void funcionObtenerPrecio() {
        Call<getPrice> call = RetrofitClient
                .getInstance()
                .getApi()
                .travel(token, getOrigenLat(datadriver), getOrigenLong(datadriver),
                        getDestinoLat(datadriver), getDestinoLong(datadriver));
        Log.e("codigo",getOrigenLat(datadriver)+"   "+ getOrigenLong(datadriver)+"   "+
                getDestinoLat(datadriver)+"   "+  getDestinoLong(datadriver));
        call.enqueue(new Callback<getPrice>() {
            @Override
            public void onResponse(Call<getPrice> call, Response<getPrice> response) {
                Log.e("codigo",response.code()+"");
                if (response.isSuccessful()){

                    travelPrecio=response.body().getTravelRate().getEcoEvans();
                    setPriceAndAdrress(datadriver,edtxt_origin.getText().toString(),
                            edtxt_dest.getText().toString(),travelPrecio);
                    setEstadoViews(datadriver,3);
                    tercer_estado();

                    progressBar.setVisibility(View.GONE);


                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(),"Seleccione nuevamente el destino",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<getPrice> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(),"Seleccione nuevamente el destino"+t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }



    private void cancel_viaje_noti() {
        Call cancelarViaje=RetrofitClient.getInstance().getApi().userTOdriver(getUserId_Prefs(prefs),getDriverId(datadriver),"Viaje-Cancelado","El-Usuario-cancelo-el-viaje","viajecancelado");
        cancelarViaje.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()){
                    if (!(getApiWebVersion(prefs).equals(getVersionApp(getContext())))){
                        getViewUpdateVersion(getActivity(),getContext());
                    }
                    Log.e("estado_delete","btn cancelar");
                    borrarSharedPreferencesDataDriver(1);
                    primer_estado();
                    setChatJson(datadriver,"nulo");
                    /*Intent intent = new Intent("subsUnsubs");
                    intent.putExtra("subs", "chat");
                    intent.putExtra("subsUnsubs",false);
                    LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(getContext());
                    broadcaster.sendBroadcast(intent);*/

                }
                Log.e("estado_delete",response.code()+" "+response.message());
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("estado_delete",t.getMessage()+"");
            }
        });

    }
    private void ejecutarCronometro() {
        countDownTimer = new CountDownTimer(25000,1000) {
            public void onTick(long millisUntilFinished) {
                setCronometroStop(datadriver,true);
                segundos=String.format(Locale.getDefault(), "%d", millisUntilFinished / 1000L);
                fmi_time.setText(segundos);
                Log.e("tiempocountDownTimer",segundos);
            }

            public void onFinish() {

                if (Integer.parseInt(segundos)>0){
                    countDownTimer.cancel();
                }else {
                    Log.d("MainActivity22","finnn"+segundos+getCronometroStop(datadriver));
                    if (getCronometroStop(datadriver)){

                    }else {
                        setEstadoViews(datadriver,3);
                        tercer_estado();
                        fmi_time.setVisibility(View.GONE);
                        Call comando=RetrofitClient.getInstance().getApi().puStatusTrip(getViajeId(datadriver),true,false,false,false);
                        comando.enqueue(new Callback() {
                            @Override
                            public void onResponse(Call call, Response response) {
                                if (response.isSuccessful()){
                                    Log.e("Viaje aceptado",response.message());

                                }
                                Log.e("Viaje aceptado",response.code()+"");
                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {

                            }
                        });
                    }



                }


            }

        }.start();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {

        this.mapboxMap = mapboxMap;

        geo = new Geocoder(getContext(), Locale.getDefault());
        mapboxMap.getUiSettings().setCompassEnabled(true);
        mapboxMap.setStyle(getString(R.string.navigation_guidance_day), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);
                // addDestinationIconSymbolLayer(style);
            }

        });
        mapboxMap.addOnCameraIdleListener(new MapboxMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng center = mapboxMap.getCameraPosition().target;
                if (center.getLatitude()!=0.0&&center.getLongitude()!=0.0){
                    // new Handler().postDelayed(new Runnable() {
                    //   @Override
                    //  public void run() {
                    try {
                        String[] direccion2;
                        adres = geo.getFromLocation(center.getLatitude(),center.getLongitude(),1);
                        direccion2 = (adres.get(0).getAddressLine(0)).split(",");
                        if (!status_btn_origin){
                            edtxt_origin.setText(direccion2[0]);
                        }else{
                            edtxt_dest.setText(direccion2[0]);
                        }

                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                    //   }
                    //},2000);


                }
            }
        });
        gps.setOnClickListener(this);

        comprobarestadoViews();
        if (!getdataNotification_noti(datadriver).equals("nulo")){
            search_imagen.setVisibility(View.GONE);
            Log.d("MainActivity22222","ejecuto");

            if (getdataNotification_noti(datadriver).contains("{cronometroFinalizado}")){
                search_imagen.setVisibility(View.GONE);
                Call comando= RetrofitClient.getInstance().getApi().puStatusTrip(getViajeId(datadriver),true,false,false,false);
                comando.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {;
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                    }
                });

            }else {
                ejecutar_tarea_notificaciones_data(getdataNotification_noti(datadriver));
            }
        }
        if (getEstadoView(datadriver)>=5){
            cargarPosicionDriver();
        }
      /*
      *
        DirectionsRoute ruta2=getRouteString();
        FeatureCollection f2= FeatureCollection.fromFeature(
                Feature.fromGeometry(LineString.fromPolyline(ruta2.geometry(), PRECISION_6)));
        LineString ls2 = ((LineString) f2.features().get(0).geometry());
        if (ls2 != null) {
            routeCoordinateList2= ls2.coordinates();
        }
        DirectionsRoute ruta3=getRouteString();
        FeatureCollection f3= FeatureCollection.fromFeature(
                Feature.fromGeometry(LineString.fromPolyline(ruta3.geometry(), PRECISION_6)));
        LineString ls3 = ((LineString) f3.features().get(0).geometry());
        if (ls3 != null) {
            routeCoordinateList3 = ls3.coordinates();
        }
       DirectionsRoute ruta4= getRouteString();
        FeatureCollection f4= FeatureCollection.fromFeature(
                Feature.fromGeometry(LineString.fromPolyline(ruta4.geometry(), PRECISION_6)));
        LineString ls4 = ((LineString) f4.features().get(0).geometry());
        if (ls4 != null) {
            routeCoordinateList4 = ls4.coordinates();
        }
      * */


    }





    /**
     * Method is used to interpolate the SymbolLayer icon animation.
     */

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(getContext(),"This app needs location permissions to show its functionality.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationComponent(mapboxMap.getStyle());
        } else {
            Toast.makeText(getContext(), "You didn\\'t grant location permissions.", Toast.LENGTH_LONG).show();
            //mapView.finish();
        }
    }
    private void borrarSharedPreferencesDataDriver(int estado){
        //limpiarNotify(getActivity());
        Log.e("estado_delete",getEstadoView(datadriver)+"");
        SharedPreferences.Editor editor = datadriver.edit();
        editor.clear();
        editor.apply();
        setEstadoViews(datadriver,estado);
    }
    @SuppressLint("RestrictedApi")
    private void primer_estado(){

        try {
            if (refConexionDriverCoor!=null&&conexionDriver!=null)
                refConexionDriverCoor.removeEventListener(conexionDriver);
        }catch (Exception e){

        }

        Log.e("vistas","entra la primera vista");
        if (cordenadasDriver_Runable!=null){
            coordenadasDriver.removeCallbacks(cordenadasDriver_Runable);
        }
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                style.removeSource(DOT_SOURCE_ID1);
                style.removeLayer("symbol-layer-id");
            }
        });
        try{
            mapboxMap.clear();
            mapboxMap.removeAnnotations();
            navigationMapRoute.removeRoute();
            mapboxMap.removeMarker(origen);
            mapboxMap.removeMarker(destino);

        }catch (Exception e){

        }

        // dfv_spinner.setVisibility(View.GONE);
        //   dfv_spinner.setEnabled(false);
        spinner.setEnabled(true);
        mi_imgbtn_next_step.setEnabled(false);
        mi_imgbtn_next_step.setBackgroundColor(Color.GRAY);
        mi_imgbtn_next_step.setText("CALCULAR TARIFA");
        if (navigationMapRoute!=null){
            navigationMapRoute.removeRoute();
        }
        if(origen!=null){
            mapboxMap.removeMarker(origen);
        }
        if(destino!=null){
            mapboxMap.removeMarker(destino);
        }
        edtxt_dest.setText("");
        edtxt_origin.setText("");
        edtxt_dest.setTextColor(getContext().getColor(R.color.black));
        edtxt_origin.setTextColor(getContext().getColor(R.color.black));
        edtxt_dest.setEnabled(true);
        edtxt_origin.setEnabled(true);
        imgbtn_dest.setSelected(false);
        imgbtn_origin.setSelected(false);
        status_btn_dest=false;
        status_btn_origin=false;
        progressBar.setVisibility(View.GONE);
        search_imagen.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
        marcador.setVisibility(View.VISIBLE);
        fmi_time.setVisibility(View.GONE);
        destinos_dialog.setVisibility(View.VISIBLE);
        mapa_precios.setVisibility(View.GONE);
        dialog_transcurso_viaje.setVisibility(View.GONE);
        dialog_fin_viaje_dialog.setVisibility(View.GONE);
        bottomSheetBehavior.setState( BottomSheetBehavior.STATE_HIDDEN );
        button_coment_hide.setVisibility( View.GONE );
        try{
            getRouteString();
        }catch (Exception e){

        }
        if (DriverOptions!=null)
            DriverOptions.remove();


    }
    @SuppressLint("RestrictedApi")
    private void segundo_estado(){
        destinos_dialog.setVisibility(View.VISIBLE);
        mapa_precios.setVisibility(View.GONE);
        dialog_transcurso_viaje.setVisibility(View.GONE);
        dialog_fin_viaje_dialog.setVisibility(View.GONE);
        marcador.setVisibility(View.VISIBLE);
        bottomSheetBehavior.setState( BottomSheetBehavior.STATE_HIDDEN );
        button_coment_hide.setVisibility( View.GONE );
        Log.e("estado 2: ",getOrigenLat(datadriver)+"   "+getOrigenLong(datadriver)+"  "+valor);
    }
    @SuppressLint("RestrictedApi")
    private void tercer_estado(){
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                style.removeSource(DOT_SOURCE_ID1);
                style.removeLayer("symbol-layer-id");
            }
        });
        spinner.setEnabled(true);
        spinner.setSelection(0);

      /*  if (tarea!=null&&handler_internet!=null)
            handler_internet.removeCallbacks(tarea);*/
        destinos_dialog.setVisibility(View.GONE);
        mapa_precios.setVisibility(View.VISIBLE);
        dialog_precio_select.setEnabled(true);
        search_imagen.setVisibility(View.GONE);
        fmi_time.setVisibility(View.GONE);
        dialog_transcurso_viaje.setVisibility(View.GONE);
        dialog_fin_viaje_dialog.setVisibility(View.GONE);
        bottomSheetBehavior.setState( BottomSheetBehavior.STATE_HIDDEN );
        button_coment_hide.setVisibility( View.GONE );
        precio.setText("S./ "+ getPriceShared(datadriver));
        marcador.setVisibility(View.GONE);
        dialog_precio_select.setText("PEDIR ECO EVANS");
        Log.e("estado 3: ",getDestinoLat(datadriver)+"   "+getDestinoLong(datadriver)+"  "+valor);
        dibujarlineas();




    }
    @SuppressLint("RestrictedApi")
    private void cuarto_estado(){
        dialog_precio_select.setEnabled(false);
        marcador.setVisibility(View.GONE);
        destinos_dialog.setVisibility(View.GONE);
        mapa_precios.setVisibility(View.VISIBLE);
        dialog_transcurso_viaje.setVisibility(View.GONE);
        dialog_fin_viaje_dialog.setVisibility(View.GONE);
        bottomSheetBehavior.setState( BottomSheetBehavior.STATE_HIDDEN );
        button_coment_hide.setVisibility( View.GONE );
        spinner.setEnabled(false);
        precio.setText("S./ "+ getPriceShared(datadriver));
        dialog_precio_select.setText("Esperando evans");
        Log.e("estado 4: ",getDestinoLat(datadriver)+"   "+getDestinoLong(datadriver)+"  "+valor);
        dibujarlineas();

    }

    private void dibujarlineas() {
        try {
            if(originPoint==null||destinationPoint==null){
                Log.e("lineasMapbox",""+originPoint+"  "+destinationPoint);
                originPoint = Point.fromLngLat( Double.parseDouble(getOrigenLong(datadriver)),Double.parseDouble(getOrigenLat(datadriver)));
                destinationPoint = Point.fromLngLat( Double.parseDouble(getDestinoLong(datadriver)),Double.parseDouble(getDestinoLat(datadriver)));
                getRoute(originPoint, destinationPoint);
            }
        }catch (Exception e){

        }


        if (destino!=null){
            destino.remove();
        }
        if (origen!=null){
            origen.remove();
        }
        try {
            destino=mapboxMap.addMarker(new MarkerOptions()
                    .setIcon(iconFactory.fromResource(R.drawable.logo22))
                    .setPosition(new com.mapbox.mapboxsdk.geometry.LatLng(Double.parseDouble(getDestinoLat(datadriver)),Double.parseDouble(getDestinoLong(datadriver))))
                    .title("Destino"));

            origen=mapboxMap.addMarker(new MarkerOptions()
                    .setIcon(iconFactory.fromResource(R.drawable.logo33))
                    .setPosition(new com.mapbox.mapboxsdk.geometry.LatLng(Double.parseDouble(getOrigenLat(datadriver)),Double.parseDouble(getOrigenLong(datadriver))))
                    .title("Origen"));
        }catch (Exception e){
         /*   Log.d("entrando","a transacion"+e.getMessage());
            Fragment frg = new mapaInicio(getUserId_Prefs(prefs),getToken(prefs));
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.detach(frg);
            ft.attach(frg);
            ft.commit();*/
        }

    }

    @SuppressLint("RestrictedApi")
    private void quinto_estado(){
        marcador.setVisibility(View.GONE);
        destinos_dialog.setVisibility(View.GONE);
        mapa_precios.setVisibility(View.GONE);
        dialog_transcurso_viaje.setVisibility(View.VISIBLE);
        dialog_fin_viaje_dialog.setVisibility(View.GONE);
        bottomSheetBehavior.setState( BottomSheetBehavior.STATE_HIDDEN );
        button_coment_hide.setVisibility( View.GONE );
        dialog_transcurso_txt_name.setText(getnameID(datadriver));
        dialog_transcurso_txt_placa.setText(getlicenseCarID(datadriver));
        dialog_transcurso_txt_marca.setText(getbrandCarID(datadriver));
        dialog_transcurso_txt_color.setText(getcolorCarID(datadriver));
        try {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);
            Glide.with(this).load(getdriverImgID(datadriver)).apply(options).into(dialog_transcurso_iv_photo);

        }catch (Exception e){
            Log.e("ErrorcargarImg",e.getMessage());
        }

        //dialog_fin_viaje_imgbtn_delete.setVisibility(View.GONE);
        // dialog_fin_viaje_imgbtn_message.setVisibility(View.VISIBLE);
        Log.e("estado 5: ",getDestinoLat(datadriver)+"   "+getDestinoLong(datadriver)+"  "+valor);
        dibujarlineas();
    }
    @SuppressLint("RestrictedApi")
    private void sexto_estado(){
        Log.e("estado 6: ",getDestinoLat(datadriver)+"   "+getDestinoLong(datadriver)+"  "+valor);

        dialog_fin_viaje_txt_precio_coupon.setText(getPriceSharedDiscount(datadriver)+"PEN");
        dialog_fin_viaje_txt_precio.setText(( Double.parseDouble(getPriceShared(datadriver))-Double.parseDouble(getPriceSharedDiscount(datadriver)))+"PEN");
        destinos_dialog.setVisibility(View.GONE);
        mapa_precios.setVisibility(View.GONE);
        marcador.setVisibility(View.GONE);
        dialog_transcurso_viaje.setVisibility(View.GONE);
        dialog_fin_viaje_dialog.setVisibility(View.VISIBLE);
        bottomSheetBehavior.setState( BottomSheetBehavior.STATE_HIDDEN );
        button_coment_hide.setVisibility( View.GONE );
        dialog_fin_viaje_btn_aceptar.setEnabled(true);
        dialog_fin_viaje_txt_titulo.setText("CONDUCTOR LLEGÓ AL ORIGEN");
        dialog_fin_viaje_btn_aceptar.setText("INICIO");
        dialog_fin_viaje_txt_origen.setText("Origen: "+getStartAddress(datadriver));
        dialog_fin_viaje_txt_destino.setText("Destino: "+getEndAddress(datadriver));
        //dialog_fin_viaje_imgbtn_delete.setVisibility(View.VISIBLE);
        //dialog_fin_viaje_imgbtn_message.setVisibility(View.GONE);
        dibujarlineas();
    }
    @SuppressLint("RestrictedApi")
    private  void septimo_estado(){
        Log.e("estado 7: ",getDestinoLat(datadriver)+"   "+getDestinoLong(datadriver)+"  "+valor);



        destinos_dialog.setVisibility(View.GONE);
        mapa_precios.setVisibility(View.GONE);
        dialog_transcurso_viaje.setVisibility(View.GONE);
        dialog_fin_viaje_dialog.setVisibility(View.VISIBLE);
        button_coment_hide.setVisibility( View.GONE );
        marcador.setVisibility(View.GONE);
        bottomSheetBehavior.setState( BottomSheetBehavior.STATE_HIDDEN );
        dialog_fin_viaje_txt_titulo.setText("Esperando que Inicie el Viaje");
        dialog_fin_viaje_btn_aceptar.setText("Esperando..");
        dialog_fin_viaje_btn_aceptar.setEnabled(false);
        dialog_fin_viaje_txt_origen.setText("Origen: "+getStartAddress(datadriver));
        dialog_fin_viaje_txt_destino.setText("Destino: "+getEndAddress(datadriver));
        //  dialog_fin_viaje_imgbtn_delete.setVisibility(View.VISIBLE);
        //dialog_fin_viaje_imgbtn_message.setVisibility(View.GONE);
        dibujarlineas();
    }
    @SuppressLint("RestrictedApi")
    private void octavo_estado(){
        Log.e("estado 8: ",getDestinoLat(datadriver)+"   "+getDestinoLong(datadriver)+"  "+valor);
        destinos_dialog.setVisibility(View.GONE);
        mapa_precios.setVisibility(View.GONE);
        dialog_transcurso_viaje.setVisibility(View.GONE);
        dialog_fin_viaje_dialog.setVisibility(View.VISIBLE);
        button_coment_hide.setVisibility( View.GONE );
        bottomSheetBehavior.setState( BottomSheetBehavior.STATE_HIDDEN );
        dialog_fin_viaje_txt_origen.setText("Origen: "+getStartAddress(datadriver));
        dialog_fin_viaje_txt_destino.setText("Destino: "+getEndAddress(datadriver));


        /*dfv_spinner.setVisibility(View.VISIBLE);
        dfv_spinner.setEnabled(true);
        if (  getPriceSharedDiscount(datadriver).equals("0.0")){
            dfv_spinner.setSelection(0);
        }else{
            dfv_spinner.setSelection(1);
        }*/
        dialog_fin_viaje_txt_titulo.setText("Confirmar terminar Viaje");
        dialog_fin_viaje_btn_aceptar.setText("Terminar Viaje");
        dialog_fin_viaje_btn_aceptar.setEnabled(true);
        marcador.setVisibility(View.GONE);
        // dialog_fin_viaje_imgbtn_delete.setVisibility(View.VISIBLE);
        // dialog_fin_viaje_imgbtn_message.setVisibility(View.GONE);
        dibujarlineas();
    }
    @SuppressLint("RestrictedApi")
    private void noveno_estado(){
        Log.e("estado 9: ",getDestinoLat(datadriver)+"   "+getDestinoLong(datadriver)+"  "+valor);

        dibujarlineas();

        destinos_dialog.setVisibility(View.GONE);
        mapa_precios.setVisibility(View.GONE);
        dialog_transcurso_viaje.setVisibility(View.GONE);
        dialog_fin_viaje_dialog.setVisibility(View.VISIBLE);
        bottomSheetBehavior.setState( BottomSheetBehavior.STATE_HIDDEN );
        button_coment_hide.setVisibility( View.GONE );
        marcador.setVisibility(View.GONE);
        dialog_fin_viaje_txt_origen.setText("Origen: "+getStartAddress(datadriver));
        dialog_fin_viaje_txt_destino.setText("Destino: "+getEndAddress(datadriver));
        dialog_fin_viaje_txt_titulo.setText("Conductor esperando Pago");
        dialog_fin_viaje_btn_aceptar.setText("Esperando..");
        dialog_fin_viaje_btn_aceptar.setEnabled(false);
        //dialog_fin_viaje_imgbtn_delete.setVisibility(View.VISIBLE);
        // dialog_fin_viaje_imgbtn_message.setVisibility(View.GONE);
    }
    @SuppressLint("RestrictedApi")
    private void decimo_estado(){
        if (navigationMapRoute!=null){
            navigationMapRoute.removeRoute();
            mapboxMap.removeMarker(destino);
            mapboxMap.removeMarker(origen);
        }
        Log.e("estado 10: ",getDestinoLat(datadriver)+"   "+getDestinoLong(datadriver)+"  "+valor);

        destinos_dialog.setVisibility(View.GONE);
        mapa_precios.setVisibility(View.GONE);
        dialog_transcurso_viaje.setVisibility(View.GONE);
        dialog_fin_viaje_dialog.setVisibility(View.GONE);
        button_coment_hide.setVisibility( View.VISIBLE );
        linearLayout.setVisibility(View.VISIBLE);
        marcador.setVisibility(View.GONE);
        bottomSheetBehavior.setState( BottomSheetBehavior.STATE_EXPANDED );
        Log.e("estado_delete","decimo estado");


    }
    public  void ejecutarTarea_ActualizarCordenadasDriver(){

        cordenadasDriver_Runable=new Runnable() {
            @Override
            public void run() {
                try {

                    Call<infoDriver> call = RetrofitClient.getInstance()
                            .getApi().updateCoordenadasDriver(getDriverId(datadriver));

                    call.enqueue(new Callback<infoDriver>() {
                        @Override
                        public void onResponse(Call<infoDriver> call, Response<infoDriver> response) {
                            if (response.isSuccessful()){
                                Log.e("updateDrive","Cordenate"+response.body().getLatitude());
                                try {

                                }catch (Exception e){
                                    Log.e("updateDrive","Cordenate"+e.getMessage());
                                }

                            }
                            Log.e("updateDrive","Cordenate"+response.message()+response.code());
                        }

                        @Override
                        public void onFailure(Call<infoDriver> call, Throwable t) {

                        }
                    });
                }catch (Exception e){
                    Log.e("Update","Cordenadas "+e.getMessage());
                }
                coordenadasDriver.postDelayed(this,TIEMPO);

            }
        };
        coordenadasDriver.postDelayed(cordenadasDriver_Runable,TIEMPO);
    }
    private void comprobarestadoViews() {
        switch (getEstadoView(datadriver)){
            case 1:
            case 2:
                Log.e("estado_delete","dswitch comprobar vista");
                borrarSharedPreferencesDataDriver(1);
                primer_estado();
                break;
            case 3:
            case 4:
                tercer_estado();
                break;
            case 5:
                quinto_estado();
                break;
            case 6:
                sexto_estado();
                break;
            case 7:
                septimo_estado();
                break;
            case 8:
                octavo_estado();
                break;
            case 9:
                noveno_estado();
                break;
            case 10:
                decimo_estado();
                break;
        }
    }

    //pRUEBAS DE INTERNET
    private boolean isNetDisponible() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();

            return (actNetInfo != null && actNetInfo.isConnected());
        }catch (Exception e){
            Log.e("error isnetDisponible: ",e.getMessage());
            return true;
        }

    }
    public Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.pe");

            int val           = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

    }

}
