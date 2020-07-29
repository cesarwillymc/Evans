package com.evans.technologies.usuario.Utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import com.evans.technologies.usuario.R
import com.evans.technologies.usuario.fragments.change_password.changepassword
import com.evans.technologies.usuario.fragments.change_password.correo
import com.evans.technologies.usuario.fragments.change_password.set_codigo
import com.google.firebase.messaging.FirebaseMessagingService
import java.io.File
import java.io.FileOutputStream
import java.math.RoundingMode
import java.text.DecimalFormat
fun getIsReferred(prefs: SharedPreferences): Boolean? {
    return prefs.getBoolean("isreferred", false)
}
fun setReferido(prefs: SharedPreferences,refirio:Boolean){
    val editor = prefs.edit()
    editor.putBoolean("isreferred",refirio)
    editor.apply()
}
fun getCorreoNavFragment(prefs: SharedPreferences): String? {
    return prefs.getString("setCorreoNavFragment", "")
}
fun setCorreoNavFragment(prefs: SharedPreferences,refirio:String){
    val editor = prefs.edit()
    editor.putString("setCorreoNavFragment",refirio)
    editor.apply()
}
fun settokenrecuperar(navFragment: SharedPreferences, id:String){
    val editor = navFragment.edit()
    editor.putString("settokenrecuperar",id)
    editor.apply()
}
fun gettokenrecuperar(navFragment: SharedPreferences): String? {
    return navFragment.getString("settokenrecuperar","")
}
fun setIDrecuperar(navFragment: SharedPreferences, id:String){
    val editor = navFragment.edit()
    editor.putString("setIDrecuperar",id)
    editor.apply()
}
fun getIDrecuperar(navFragment: SharedPreferences): String? {
    return navFragment.getString("setIDrecuperar","")
}
fun setNavFragment(navFragment: SharedPreferences, Frag:String){
    val editor = navFragment.edit()
    editor.putString("NavFragment",Frag)
    editor.apply()
}
fun getBeforeNavFragment(navFragment: SharedPreferences): Fragment? {
    val data=navFragment.getString("NavFragment","")
    if (data.toString().contains("changepassword")){
        return changepassword()
    }
    if (data.toString().contains("correo")){
        return correo()
    }
    if (data.toString().contains("set_codigo")){
        return set_codigo(false,false)
    }
    return null
}
fun Activity.toastShort(mensaje:String){
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show()
}
fun Activity.toastLong(mensaje:String){
    Toast.makeText(this,mensaje,Toast.LENGTH_LONG).show()
}
fun ViewGroup.inflate(layoutId: Int): View {
    return LayoutInflater.from(context).inflate(layoutId,this,false)
}
fun getUserEmail(prefs: SharedPreferences): String? {
    return prefs.getString("email", "")
}

fun getUserPassword(prefs: SharedPreferences): String? {
    return prefs.getString("password", "")
}

fun getUserName(prefs: SharedPreferences): String? {
    return prefs.getString("name", "")
}
fun getcityUser(prefs: SharedPreferences): String? {
    return prefs.getString("city", "")
}
fun getcellphoneUser(prefs: SharedPreferences): String? {
    return prefs.getString("cellphone", "")
}
fun getUserId_Prefs(prefs: SharedPreferences): String? {
    return prefs.getString("id", "")
}
fun getToken(prefs: SharedPreferences): String? {
    return prefs.getString("token", "")
}

fun getUserSurname(prefs: SharedPreferences): String? {
    return prefs.getString("surname", "")
}

fun getUserCellphone(prefs: SharedPreferences): String? {
    return prefs.getString("cellphone", "")
}
fun SharedPreferences.edit(func: SharedPreferences.Editor.() -> Unit){
    val editor= edit()
    editor.func()
    editor.apply()
}
fun EditText.passwordvalido() =this.text.toString().isNotEmpty()&&
            this.text.toString().length.compareTo(6)==1
fun EditText.userValido() = this.text.toString().isNotEmpty()&&
            this.text.toString().contains('@')
fun setPriceAndAdrress(prefs: SharedPreferences,  startAddress:String,endAddress:String,price:String){
    val editor = prefs.edit()
    editor.putString("startAddress",startAddress)
    editor.putString("endAddress",endAddress)
    editor.putString("price",price)
    editor.apply()
}
fun setOrigen(prefs: SharedPreferences,origenLat:String,origenLog:String){
    val editor = prefs.edit()
    editor.putString("origenLat",origenLat)
    editor.putString("origenLog",origenLog)
    editor.apply()
}
fun setDestino(prefs: SharedPreferences,destLat:String,destLong:String){
    val editor = prefs.edit()
    editor.putString("destLat",destLat)
    editor.putString("destLong",destLong)
    editor.apply()
}
////Info user
fun setRutaImagen(prefs: SharedPreferences, ruta:String) {
    val editor = prefs.edit()
    editor.putString("rutaImg",ruta)
    editor.apply()
}
fun getRutaImagen(dataDriver: SharedPreferences):String?{
    return dataDriver.getString("rutaImg", "nulo")
}
fun setInfoDriver(dataDriver: SharedPreferences,driverImg:String,surname:String,name:String,licenseCar:String,
                  brandCar:String,modelCar:String,colorCar:String){
    val editor = dataDriver.edit()
    editor.putString("driverImgID",driverImg)
    editor.putString("surnameID",surname)
    editor.putString("nameID",name)
    editor.putString("licenseCarID",licenseCar)
    editor.putString("brandCarID",brandCar)
    editor.putString("modelCarID",modelCar)
    editor.putString("colorCarID",colorCar)
    editor.apply()
}
fun setdataNotification_noti(dataDriver: SharedPreferences,data:String,boleano:String){
    val editor = dataDriver.edit()
    editor.putString("dataNotification_noti",data+boleano)
    editor.apply()
}
fun getdataNotification_noti(dataDriver: SharedPreferences): String?{
    return dataDriver.getString("dataNotification_noti", "nulo")
}
fun getdriverImgID(prefs: SharedPreferences): String? {
    return prefs.getString("driverImgID", "")
}
fun getsurnameID(prefs: SharedPreferences): String? {
    return prefs.getString("surnameID", "")
}
fun getnameID(prefs: SharedPreferences): String? {
    return prefs.getString("nameID", "")
}
fun getlicenseCarID(prefs: SharedPreferences): String? {
    return prefs.getString("licenseCarID", "")
}
fun getbrandCarID(prefs: SharedPreferences): String? {
    return prefs.getString("brandCarID", "")
}
fun getmodelCarID(prefs: SharedPreferences): String? {
    return prefs.getString("modelCarID", "")
}
fun getcolorCarID(prefs: SharedPreferences): String? {
    return prefs.getString("colorCarID", "")
}
///////////////////////////////////////////////////////
//////////////
fun getOrigenLat(dataDriver: SharedPreferences): String? {
    return dataDriver.getString("origenLat", "")
}
fun getOrigenLong(dataDriver: SharedPreferences): String? {
    return dataDriver.getString("origenLog", "")
}
fun getDestinoLat(dataDriver: SharedPreferences): String? {
    return dataDriver.getString("destLat", "")
}
fun getDestinoLong(dataDriver: SharedPreferences): String? {
    return dataDriver.getString("destLong", "")
}
fun getPriceShared(dataDriver: SharedPreferences): String? {
    return dataDriver.getString("price", "0.0")
}
fun getPriceSharedDiscount(dataDriver: SharedPreferences): String? {
    return dataDriver.getString("getPriceSharedDiscount", "0.0")
}
fun setPriceSharedDiscount(dataDriver: SharedPreferences,money:String){
    val editor = dataDriver.edit()
    editor.putString("getPriceSharedDiscount",money)
    editor.apply()
}

fun getStartAddress(dataDriver: SharedPreferences): String? {
    return dataDriver.getString("startAddress", "")
}
fun getEndAddress(dataDriver: SharedPreferences): String? {
    return dataDriver.getString("endAddress", "")
}
fun setDriverViajeid(prefs: SharedPreferences,
                     driverId:String,viajeId:String){
    val editor = prefs.edit()

    editor.putString("driverId",driverId)
    editor.putString("viajeId",viajeId)
    editor.apply()
}
fun setiteracionActivity(prefs: SharedPreferences,iteracionActivity:Int){
    val editor = prefs.edit()
    editor.putInt("iteracionActivity",iteracionActivity)
    editor.apply()
}
fun getiteracionActivity(prefs: SharedPreferences): Int? {
    return prefs.getInt("iteracionActivity", 999)
}

fun getViajeId(dataDriver: SharedPreferences): String? {
    return dataDriver.getString("viajeId", "nulo")
}
fun setViajeId(dataDriver: SharedPreferences,setDriverId:String){
    val editor = dataDriver.edit()
    editor.putString("viajeId",setDriverId)
    editor.apply()
}
fun setDriverId(prefs: SharedPreferences,setDriverId:String){
    val editor = prefs.edit()
    editor.putString("driverId",setDriverId)
    editor.apply()
}
fun llaveChat(prefs: SharedPreferences,llaveChat:String){
    val editor = prefs.edit()
    editor.putString("llaveChat",llaveChat)
    editor.apply()
}
fun getLlaveChat(prefs: SharedPreferences): String? {
    return prefs.getString("llaveChat", "")
}

fun getDriverId(prefs: SharedPreferences): String? {
    return prefs.getString("driverId", "")
}


fun setEstadoViews(prefs: SharedPreferences,estado:Int){
    val editor = prefs.edit()
    editor.putInt("estadoView",estado)
    editor.apply()
}
fun getEstadoView(prefs: SharedPreferences): Int?{
    return prefs.getInt("estadoView", 1)
}
fun milisecondsToSeconds(miliseconds: Long): Int {
    return (miliseconds / 1000 % 60).toInt()
}
fun Activity.limpiarNotify(){
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.cancelAll()
}
//aea
fun saveToInternalStorage(bitmapImage: Bitmap, name:String):String?{
    /* var cw: ContextWrapper =  ContextWrapper(getApplicationContext())
     var directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
     var mypath=File(directory,"profile.jpg")*/

    val path =  Environment.getExternalStorageDirectory().path+ "/" + "evans" + "/"
    var data=File(path)
    if (!data.exists()) {
        data.mkdirs()
    }
    val file =
            File(path, "evans"  +name+ ".jpg")

    var fos: FileOutputStream
    try {
        fos =  FileOutputStream(file)
        // Use the compress method on the BitMap object to write image to the OutputStream
        bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.close()
    } catch ( e:Exception) {
        e.printStackTrace()
        Log.e("imagen",e.message)
    }
    return file.path
}
fun getCronometroStop(dataDriver: SharedPreferences): Boolean? {
    return dataDriver.getBoolean("CronometroStop", false)
}
fun setCronometroStop(dataDriver: SharedPreferences,stop:Boolean){
    val editor = dataDriver.edit()
    editor.putBoolean("CronometroStop",stop)
    editor.apply()
}
fun getClaseChat(prefs: SharedPreferences): Boolean? {
    if (prefs.getString("claseActual", "a")!!.contains("Fragment_chat")) {
        return true
    }
    return false
}
@SuppressLint("WrongConstant")
fun Context.sendNotification(titulo:String,body:String) {

    val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    val notificationBuilder = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.iconevans)
            .setContentTitle(titulo)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
//    //.setColor(Color.BLUE)
//            .setWhen(System.currentTimeMillis()*2)
//            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
    val notificationManager = getSystemService(FirebaseMessagingService.NOTIFICATION_SERVICE) as NotificationManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Create the NotificationChannel
        val name = getString(R.string.evans_name)
        val descriptionText = getString(R.string.evans_descripcion)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(getString(R.string.default_notification_channel_id), name, importance)
        mChannel.description = descriptionText
        mChannel.setShowBadge(true)
        mChannel.canShowBadge()
        mChannel.enableLights(true)
        mChannel.setLightColor(Color.RED);
        mChannel.enableVibration(true)
        mChannel.setVibrationPattern( longArrayOf(100, 200, 300, 400, 500))
        notificationManager.createNotificationChannel(mChannel)
    }
    notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
}
fun getCoordenadasDriver(dataDriver: SharedPreferences,nameActual:String): Boolean? {
    return dataDriver.getString("CoordenadasDrivername", "")==nameActual

}
fun setCoordenadasDriver(dataDriver: SharedPreferences,name:String){
    val editor = dataDriver.edit()
    editor.putString("CoordenadasDrivername",name)
    editor.apply()
}
fun getPrimerplano(prefs: SharedPreferences): Boolean? {
    return prefs.getBoolean("getPrimerplano", true)

}
fun setPrimerplano(prefs: SharedPreferences,primerPlano:Boolean){
    val editor = prefs.edit()
    editor.putBoolean("getPrimerplano",primerPlano)
    editor.apply()
}
fun Activity.getPath(uri: Uri): String {
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = managedQuery(uri, projection, null, null, null)
    startManagingCursor(cursor)
    val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
    cursor.moveToFirst()
    return cursor.getString(column_index)
}
fun detectar_formato(texto: String): String {
    return if (texto.contains("jpg")) {
        "jpg"
    } else if (texto.contains("jpeg")) {
        "jpeg"
    } else if (texto.contains("svg")) {
        "svg"
    } else if (texto.contains("png")) {
        "png"
    } else
        "ninguno"


}

fun  rotateImageSouce(source:Bitmap, angle: Int):Bitmap {
    var matrix: Matrix =  Matrix()
    matrix.postRotate(angle.toFloat())
    return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
            matrix, true)
}
fun getImageRotate(path:String,bitmap:Bitmap):Bitmap{
    var ei: ExifInterface =  ExifInterface(path)
    var orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED)

    var rotatedBitmap:Bitmap = bitmap
    when(orientation) {

        ExifInterface.ORIENTATION_ROTATE_90->{
            rotatedBitmap = rotateImageSouce(bitmap, 90)
        }
        ExifInterface.ORIENTATION_ROTATE_180->{
            rotatedBitmap = rotateImageSouce(bitmap, 180)
        }
        ExifInterface.ORIENTATION_ROTATE_270->{
            rotatedBitmap = rotateImageSouce(bitmap, 270)
        }
        ExifInterface.ORIENTATION_NORMAL->{
            rotatedBitmap = bitmap
        }
    }
    return rotatedBitmap
}
fun getClaseMapaInicio(prefs: SharedPreferences): Boolean? {
    if (prefs.getString("claseActual", "a")!!.contains("mapaInicio")) {
        return true
    }
    return false
}
fun setClaseActual(prefs: SharedPreferences,clase:String){
    val editor = prefs.edit()
    editor.putString("claseActual",clase)
    editor.apply()

}
fun setChatJson(prefs: SharedPreferences,chatJson:String){
    val editor = prefs.edit()
    editor.putString("setChatJson",chatJson)
    editor.apply()
}
fun getChatJson(prefs: SharedPreferences): String? {
    return prefs.getString("setChatJson", "nulo")
}
fun getestaFragmentChat(prefs: SharedPreferences):Boolean{
    if (prefs.getString("claseActual", "a")!!.contains("Fragment_chat")) {
        return true
    }
    return false
}
fun setDataActivityOrigin(dataDriver: SharedPreferences,setDataActivityOrigin:Boolean){
    val editor = dataDriver.edit()
    editor.putBoolean("setDataActivityOrigin",setDataActivityOrigin)
    editor.apply()
}
fun getDataActivityOrigin(dataDriver: SharedPreferences): Boolean? {
    return dataDriver.getBoolean("setDataActivityOrigin", false)
}
fun ramdomNum(lat:Boolean):String{
    var num:Double;
    var df:DecimalFormat =  DecimalFormat("##.#######")
    df.setRoundingMode(RoundingMode.CEILING)
    num = if (lat){
        Math.random()*((-15.834)-(-15.845))+(-15.845)
    }else{
        Math.random()*((-70.019)-(-70.030))+(-70.030)
    }
    return df.format(num)



}
fun ramdomNumForLat(lat:Boolean,latIni:Double,logInit:Double):String{
    var num:Double;
    var df:DecimalFormat =  DecimalFormat("##.#######")
    df.setRoundingMode(RoundingMode.CEILING)
    var latFinal=latIni-0.011
    var logFinal=logInit-0.011
    num = if (lat){
        Math.random()*((latIni)-(latFinal))+(latFinal)
    }else{
        Math.random()*((logInit)-(logFinal))+(logFinal)
    }
    return df.format(num)



}
fun getAccountActivate(prefs: SharedPreferences):Boolean{
    return prefs.getBoolean("accountActivate",false)
}
fun setAccountActivate(prefs: SharedPreferences,valor:Boolean){
    var editor=prefs.edit()
    editor.putBoolean("accountActivate",valor)
    editor.apply()
}

fun getApiWebVersion(prefs: SharedPreferences): String? {
    return prefs.getString("getApiWebVersion", "0")
}
fun setApiWebVersion(prefs: SharedPreferences,version:String){
    val editor = prefs.edit()
    editor.putString("getApiWebVersion",version)
    editor.apply()
}
//AIzaSyBdIjp0e-pvzlNzej27mE|AAAAAAA|fpg0TCC-w-xgs    google key
//Mapbox KEY sk.eyJ1Ijoic3VwcG9ydGV2YW5zIiwiYSI6ImNrNGcyM2JnYzB1|AAAAAAA|aXozbW83cWhqZ2l0enMifQ.6qXlLzej2U7OXyRAmX7t0w
fun Activity.getViewUpdateVersion(context:Context){
    var dialogClickListener: DialogInterface.OnClickListener  =  DialogInterface.OnClickListener { dialog, which ->
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> {
                val appPackageName = context.packageName // getPackageName() from Context or Activity object
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
                } catch (anfe: android.content.ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
                }
                finish()

            }

        }
    }
    var builder: AlertDialog.Builder  =  AlertDialog.Builder(context)
    builder.setCancelable(false)
    builder.setMessage("Actualize la version por favor").setPositiveButton("Actualizar", dialogClickListener).show()
}


fun Context.getVersionApp():String{
    var version=0
    try {
        val pInfo = getPackageManager().getPackageInfo(packageName, 0)
        version = pInfo.versionCode
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return ""+version!!
}