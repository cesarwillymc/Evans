package com.evans.technologies.usuario.Utils.timeCallback

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.evans.technologies.usuario.Utils.setPrimerplano

class ArchLifecycleApp : Application(), LifecycleObserver {
    companion object{
        private lateinit var instance:ArchLifecycleApp
        fun getInstanceApp():ArchLifecycleApp = instance
        fun getContextApp(): Context =instance
        fun setInstance(instance:ArchLifecycleApp){
            this.instance=instance
        }

    }
    override fun onCreate() {
        super.onCreate()
        setInstance(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() { //App in background
        val prefs =
            getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        setPrimerplano(prefs, false)
        Log.e("LifecycleObserver", "segundoPlano")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        val prefs =
            getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        setPrimerplano(prefs, true)
        // App in foreground
/* try{
            if (!isMyServiceRunning(service_mqtt.class)){
                startService(new Intent(this, service_mqtt.class));
            }
        }catch (Exception e){
            Log.e("LifecycleObserver",e.getMessage());
        }*/Log.e("LifecycleObserver", "primerPLANO")
    }

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
}