package com.evans.technologies.usuario.Activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.evans.technologies.evansuser.Utils.settingsDevice.canDrawOverlayViews
import com.evans.technologies.evansuser.Utils.settingsDevice.dialogEmergente
import com.evans.technologies.evansuser.Utils.settingsDevice.isXiaomi
import com.evans.technologies.usuario.Activities.cupon.PagosActivity
import com.evans.technologies.usuario.R
import com.evans.technologies.usuario.Retrofit.RetrofitClient
import com.evans.technologies.usuario.Utils.*
import com.evans.technologies.usuario.model.config
import com.evans.technologies.usuario.model.infoDriver
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import org.jetbrains.anko.toggleButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class MainActivity : AppCompatActivity() {


    //Uidentificar al llamar al obgeto swicht

    private lateinit var  token:String
    private lateinit var id:String
    private lateinit var prefs: SharedPreferences
    private lateinit var datadriver: SharedPreferences
    var chatSnapshot: DataSnapshot? =null
//    lateinit var comunicateFrag: ComunicateFrag.mapa_inicio
//    lateinit var comunicateFragChat: ComunicateFrag.chat
    lateinit var  refConexionDriverCoor: DatabaseReference

    lateinit var  conexionDriver: ValueEventListener
    //////////////////////////////////////////////////////////////////////////
    //////AQUI ESTA EL CODIGO GRENERADO POR DEFECTO DE NAVIGATION_DRAWER//////
    //////////////////////////////////////////////////////////////////////////
    private fun fixGoogleMapBugTem(){
        val googleBug= getSharedPreferences("google_bug_154855417", Context.MODE_PRIVATE)
        if(!googleBug.contains("fixed")){
            val corruptedZoomTables= File(filesDir,"ZoomTables.data")
            corruptedZoomTables.delete()
            googleBug.edit().putBoolean("fixed",true).apply()
        }
    }
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        datadriver = getSharedPreferences("datadriver", Context.MODE_PRIVATE)
        val headerView = main_nav_view_inicio.getHeaderView(0)
        setInfoUser(headerView)
        /* try{
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                 startForegroundService(Intent(this@MainActivity, service_mqtt::class.java))
             } else {
                 startService(Intent(this@MainActivity, service_mqtt::class.java))
             }
         }catch (e:Exception){

         }*/

        try{
            val view = this.currentFocus
            view!!.clearFocus()
            if (view != null) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }

//
        setSupportActionBar(toolbar)
        fixGoogleMapBugTem()
        //
        val navController = findNavController(R.id.main_layout_change_fragment)
        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        hamburguesa.setOnClickListener {
            try {
                drawer_layout.openDrawer(Gravity.START)
            }catch (e:Exception){
                drawer_layout.closeDrawer(Gravity.END)
            }
        }
//        drawer_layout.toggleButton {
//            if (isChecked){
//                drawer_layout.isDrawerOpen(Gravity.START)
//            }else{
//                drawer_layout.isDrawerOpen(Gravity.END)
//            }
//        }
//        var status:Boolean=true
//        val toggle = ActionBarDrawerToggle(
//            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
//        drawer_layout.addDrawerListener(toggle)
//        toggle.syncState()

        setupActionBarWithNavController(navController,appBarConfiguration)
        main_nav_view_inicio.setupWithNavController(navController)
//        toggle.setToolbarNavigationClickListener {
//            Log.e("enytro","sadfsdasd")
//            if (!status){
//                onBackPressed()
//            }
//        }
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id){
                R.id.nav_travel->{
                    Log.e("entro", "entro aca2")
                    main_nav_view_inicio.visibility=View.VISIBLE
                    hamburguesa.visibility=View.VISIBLE
                   // status=true
                  // acv_imagebutton_back.visibility=View.GONE
                }
                else->{
                    Log.e("entro", "entro aca")
                    main_nav_view_inicio.visibility=View.GONE
                    hamburguesa.visibility=View.GONE
                 //   status=false
                   // acv_imagebutton_back.visibility=View.VISIBLE
                   // toolbar.navigationIcon = null
                }
            }
        }


        FirebaseDatabase.getInstance().reference.child("settingUser").child(getUserId_Prefs(prefs)!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.exists()){
                        var configuraciones: config = p0.getValue( config::class.java)!!
                        if (configuraciones.accountActivate!=null){
                            setAccountActivate(prefs,configuraciones.accountActivate)
                        }
                    }
                }
            })
        updateVersion()

        token= getToken(prefs)!!
        id= getUserId_Prefs(prefs)!!
        //comprobarStatusTrip()
//        Sfragmentdefault()
//
        //INFORMACIÓN DEL USUARIO EN EL MENU DE NAVEGACIÓN

      //  acv_imagebutton_back.setOnClickListener (this)
        /*setSupportActionBar(toolbar)
        menu_evans.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)
        }*/






        //HACEMOS LA LLAMADA A SWITCH LAYOUT PARA MOSTRAR EL LOGO DE EVANS
//        val actionBar = supportActionBar
        // actionBar?.setCustomView(R.layout.switch_layout)
       // actionBar?.displayOptions = ActionBar.DISPLAY_HOME_AS_UP or ActionBar.DISPLAY_SHOW_CUSTOM

//        val toggle = ActionBarDrawerToggle(
//            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
//        drawer_layout.addDrawerListener(toggle)
//        toggle.syncState()
        //comprobar_data_getintent()
       // main_nav_view_inicio.setNavigationItemSelectedListener(this)
//        var menu=main_nav_view_inicio.menu
//        menu.findItem(R.id.nav_wallet).isEnabled = false
//        //menu.findItem(R.id.nav_banca).setEnabled(false)
//        menu.findItem(R.id.nav_wallet).isEnabled = false
//        //menu.findItem(R.id.nav_share).isEnabled = false
//        menu.findItem(R.id.nav_oferts).isEnabled = false
//        if (!isXiaomi()){
//            var menu=main_nav_view_inicio.menu
//            menu.findItem(R.id.nav_trips_free).setVisible(false)
//        }

//        headerView.nav_header_image_view_profile.setOnClickListener(this)
        /*(applicationContext as mapaInicio).updateApi(object :  updateListenerNotifications{
            override fun updateNotificatones(check: Boolean?) {
                if (check!!){
                    acv_imagebutton_back.visibility=View.VISIBLE
                    val manager = supportFragmentManager
                    manager.beginTransaction().replace(R.id.main_layout_change_fragment, ConversationView()).commit()
                }

            }

        })*/
//        if (!(getDriverId(datadriver).equals(""))){
//            Inicializarchat()
//        }
//        acv_imagebutton_back.setOnClickListener {
//            try {
//                findNavController(R.id.main_layout_change_fragment).navigateUp()
//            }catch (e:Exception){
//                findNavController(R.id.main_layout_change_fragment).navigate(R.id.nav_travel)
//
//            }
//
//        }

    }


//    fun updateApi( listener: ComunicateFrag.mapa_inicio) {
//        comunicateFrag = listener
//    }
//
//    fun updateComuChat( listener: ComunicateFrag.chat) {
//        comunicateFragChat = listener
//    }

//    override fun createConexionChat() {
//
//        Inicializarchat()
//    }

//    override fun updateNotificatones(check: Boolean?) {
//        if (check!!){
////            acv_imagebutton_back.visibility= View.VISIBLE
//            val manager = supportFragmentManager
//            manager.beginTransaction().replace(R.id.main_layout_change_fragment, Fragment_chat()).commit()
//            Handler().postDelayed({
//                comunicateFragChat.sendDtaMessage(chatSnapshot?:null)
//            }, 500)
//        }
//    }

//    override fun removeChatConexion() {
//        try{
//            refConexionDriverCoor.removeEventListener(conexionDriver)
//        }catch (e:Exception){
//
//        }
//
//    }

    fun updateVersion(){
        FirebaseDatabase.getInstance().reference.child("settinsApp").child("user").addValueEventListener(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()){
                        try{
                            val dato= p0.getValue(infoDriver::class.java)
                            setApiWebVersion(prefs,dato!!.version+"")
                            if ((dato.version.toInt()>(getVersionApp().toInt()))){
                                if (getEstadoView(datadriver)!! <4){
                                    getViewUpdateVersion(this@MainActivity)
                                }
                            }
                        }catch (e:Exception){
                            Log.e("error ",e.message)
                        }
                    }
                }


            })

    }

    private fun comprobarStatusTrip() {
        if (!getViajeId(datadriver).equals("nulo")){
            var statusTrip= RetrofitClient.getInstance().api.getStatusTrip(getViajeId(datadriver)!!)
            statusTrip.enqueue(object : Callback<infoDriver> {
                override fun onFailure(call: Call<infoDriver>, t: Throwable) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
                override fun onResponse(call: Call<infoDriver>, response: Response<infoDriver>) {
                    if (!response.body()!!.isOk){
                        removerData()
                    }

                }

            })
        }

    }

//    override fun onClick(v: View) {
//        when(v.id){
//            R.id.acv_imagebutton_back -> {
////                if (acv_imagebutton_back.visibility== View.VISIBLE){
////                    acv_imagebutton_back.visibility= View.GONE
////                 //   Sfragmentdefault()
////                }
//            }
//            R.id.nav_header_image_view_profile->{
//                var menu=main_nav_view_inicio.menu
//                drawer_layout.closeDrawer(GravityCompat.START)
//                menu.findItem(R.id.nav_travel).setChecked(true)
//                val manager = supportFragmentManager
//                manager.beginTransaction().replace(R.id.main_layout_change_fragment, Fragment_perfil_user()).commit()
//
//            }

//        }
//    }

//    override fun onDestroy() {
//        // stopService(Intent(this@MainActivity, service_mqtt::class.java))
//
//        super.onDestroy()
//    }
//    override fun onStart() {
//       // Log.e("entro", " onStart" + isMyServiceRunning(service_mqtt::class.java) + "")
//        /*  try {
//              if (!isMyServiceRunning(service_mqtt::class.java)) {
//                  startService(Intent(this, service_mqtt::class.java))
//              }
//              <service android:name="org.eclipse.paho.android.service.MqttService" />
//          <service android:name=".Utils.Services.service_mqtt" />
//          } catch (e: java.lang.Exception) {
//              Log.e("LifecycleObserver", e.message)
//          }*/
//        super.onStart()
//    }
    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager =
            getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }


    /////////////////////////////////////////////////////////////////////
    //////////////////////Fragmentos del menu de navegación//////////////
    /////////////////////////////////////////////////////////////////////

//    @SuppressLint("WrongConstant")
//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        // Handle navigation view item clicks here.
//
//        var mifragment: Fragment? = null
//        var fragmentSeleccionado = false
//
//        when (item.itemId) {
//            R.id.nav_travel -> {
//                mifragment = mapaInicio(id, token)
//                fragmentSeleccionado = true
//            }
//            R.id.nav_wallet -> {
//                /*mifragment = TusViajesGratisFragment()
//                fragmentSeleccionado = true*/
//            }
//            R.id.nav_cuenta -> {
//                mifragment = Fragment_perfil_user()
//                fragmentSeleccionado = true
//            }
//            R.id.nav_trips -> {
//                startActivity(Intent(this@MainActivity, PagosActivity::class.java))
//                /*  mifragment = PagoFragment()
//                  fragmentSeleccionado = true*/
//            }
//            R.id.nav_trips_free -> {
//                if (!canDrawOverlayViews() && isXiaomi()) {
//                    //permission not granted
//                    Log.e("canDrawOverlayViews", "-No-");
//                    dialogEmergente("ACTIVAR: !!Mostrar ventanas emergentes mientras se ejecuta en segundo plano")
//                    //write above answered permission code for MIUI here
//                }else {
//
//                }
//            }
//            R.id.nav_oferts -> {
//                /* mifragment = AyudaFragment()
//                 fragmentSeleccionado = true*/
//            }
//            R.id.nav_share -> {
//                 val dialog= ReferidosDialogFragment()
//                 dialog.show(getSupportFragmentManager(),"")
//            }
//            R.id.nav_logout->{
//                var editor = datadriver.edit()
//                editor.clear()
//                editor.apply()
//                setEstadoViews(datadriver,1)
//                removerData()
//                removeSharedPreferences()
//                logOut()
//               // stopService(Intent(this@MainActivity, service_mqtt::class.java))
//                //stopService( Intent(this, cronometro.class))
//            }
//            R.id.nav_exit -> {
//                var editor = datadriver.edit()
//                editor.clear()
//                editor.apply()
//                removerData()
//                setEstadoViews(datadriver,1)
//                Log.e("datos enviados","Saliendo ")
//                finish()
//
//
//            }
//        }
//
//        //// ESTA CONDICION DIBUJA EN EL FRAGMENTO EL ITEM SELECCIONADO
//        if (fragmentSeleccionado) {
//            val manager = supportFragmentManager
//            if (mifragment != null) {
//                manager.beginTransaction().replace(R.id.main_layout_change_fragment, mifragment).commit()
//            }
//        }
//
//        drawer_layout.closeDrawer(GravityCompat.START)
//        return true
//    }
    private fun removerData(){
        val editor2 = datadriver.edit()
        editor2.clear()
        editor2.apply()
        setEstadoViews(datadriver,1)

    }

//    fun Sfragmentdefault() {
//        try{
//
//            val manager = supportFragmentManager
//            manager.beginTransaction().replace(R.id.main_layout_change_fragment, mapaInicio(
//                getUserId_Prefs(prefs), getToken(prefs)
//            )).commit()
//        }catch (E:Exception){
//            Log.e("error",E.message)
//        }
//    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
    private fun logOut(){
        val intent = Intent(this, InicioActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun removeSharedPreferences(){
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

    private fun setInfoUser(headerView: View) {
        Log.e("datatext", "${getUserSurname(prefs) }  datos")
        Log.e("imagen", "${getImgUrl(prefs)}  ruta = ${getRutaImagen(prefs)}")
        val nombres = getUserName(prefs)
        val apellidos = getUserSurname(prefs)

        if (!TextUtils.isEmpty(nombres) && !TextUtils.isEmpty(apellidos)){
            headerView.header_nav_text_view_nombre?.text = nombres
            headerView.header_nav_text_view_apellido?.text = apellidos
        }

        if (!getRutaImagen(prefs).equals("nulo")){
            Log.e("datatext", "entra a primara imagen")
            val file = File(getRutaImagen(prefs))
            if (file.exists()) {
                try{
                    Glide.with(this@MainActivity).load(file)
                        .apply(RequestOptions.circleCropTransform())
                        .into(headerView.nav_header_image_view_profile)
                }catch (e:Exception){

                }

            }
        }else{
//            Glide.with(this@MainActivity).load(getImgUrl(prefs))
//                .into(headerView.nav_header_image_view_profile)
            if(getImgUrl(prefs)!=""){
                Glide.with(this@MainActivity).load(getImgUrl(prefs))
                    .apply(RequestOptions.circleCropTransform()).into(headerView.nav_header_image_view_profile)
            }

        }
        Log.e("datatext", getUserName(prefs))

    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                val permission = permissions[0]
                val result = grantResults[0]
                if (permission == Manifest.permission.ACCESS_FINE_LOCATION) {
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this@MainActivity, "Permiso Activado", Toast.LENGTH_LONG)

                    } else {
                        Toast.makeText(this@MainActivity, "Permiso Denegado", Toast.LENGTH_LONG)
                    }
                }
            }

            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

}

