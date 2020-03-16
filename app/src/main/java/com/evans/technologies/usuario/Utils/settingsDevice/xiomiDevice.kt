package com.evans.technologies.evansuser.Utils.settingsDevice

import android.app.AppOpsManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*




fun Context.onDisplayPopupPermission(): Intent?{
    if (!isXiaomi()) {
        return null
    }
    try {
        // MIUI 8
        val localIntent = Intent("miui.intent.action.APP_PERM_EDITOR")
        localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity")
        localIntent.putExtra("extra_pkgname", getPackageName())

        return localIntent
    } catch (ignore: Exception) {
    }

    try {
        // MIUI 5/6/7
        val localIntent = Intent("miui.intent.action.APP_PERM_EDITOR")
        localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity")
        localIntent.putExtra("extra_pkgname", getPackageName())

        return localIntent
    } catch (ignore: Exception) {
    }

    // Otherwise jump to application details
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", getPackageName(), null)
    intent.setData(uri)
   return intent
}


fun isMIUI(): Boolean {
    val device = Build.MANUFACTURER
    Log.e("isMIUI","device "+ device)
    if (device == "Xiaomi") {
        try {
            val prop = Properties()
            prop.load(FileInputStream(File(Environment.getRootDirectory(), "build.prop")))
            Log.e("isMIUI","prop "+ prop)
            return (prop.getProperty("ro.miui.ui.version.code", null) != null
                    || prop.getProperty("ro.miui.ui.version.name", null) != null
                    || prop.getProperty("ro.miui.internal.storage", null) != null)
        } catch (e: IOException) {
            Log.e("isMIUI","error "+ e.message)
        }

    }
    return false
}
fun Context.dialogEmergente(mensaje:String){
    var dialogClickListener:DialogInterface.OnClickListener  =  DialogInterface.OnClickListener { dialog, which ->
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> {
                if (onDisplayPopupPermission()!=null){
                    startActivity(onDisplayPopupPermission())
                }
            }
            DialogInterface.BUTTON_NEGATIVE -> {

            }

        }
    }
    var builder:AlertDialog.Builder  =  AlertDialog.Builder(this)
    builder.setMessage(mensaje).setPositiveButton("Si", dialogClickListener)
            .setNegativeButton("No", dialogClickListener).show()
}
fun Context.canDrawOverlayViews(): Boolean {
    if (Build.VERSION.SDK_INT < 21) {
        return true
    }
    try {
        return Settings.canDrawOverlays(this)
    } catch (e: NoSuchMethodError) {
        return canDrawOverlaysUsingReflection()
    }

}
fun isXiaomi(): Boolean {
    return "xiaomi".equals(Build.MANUFACTURER, ignoreCase = true)
}
fun Context.canDrawOverlaysUsingReflection(): Boolean {

    try {

        val manager = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val clazz = AppOpsManager::class.java
        val dispatchMethod = clazz.getMethod("checkOp", *arrayOf<Class<*>>(Int::class.javaPrimitiveType!!, Int::class.javaPrimitiveType!!, String::class.java))
        //AppOpsManager.OP_SYSTEM_ALERT_WINDOW = 24
        val mode = dispatchMethod.invoke(manager, *arrayOf(24, Binder.getCallingUid(), applicationContext.packageName)) as Int

        return AppOpsManager.MODE_ALLOWED == mode

    } catch (e: Exception) {
        return false
    }

}