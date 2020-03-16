package com.evans.technologies.usuario.Utils.timeCallback;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;


import static com.evans.technologies.usuario.Utils.UtilsKt.setPrimerplano;

public class ArchLifecycleApp extends Application implements LifecycleObserver {

    @Override
    public void onCreate() {
        super.onCreate();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppBackgrounded() {
        //App in background
        SharedPreferences prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setPrimerplano(prefs,false);
        Log.e("LifecycleObserver","segundoPlano");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onAppForegrounded() {
        SharedPreferences prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setPrimerplano(prefs,true);
        // App in foreground
       /* try{
            if (!isMyServiceRunning(service_mqtt.class)){
                startService(new Intent(this, service_mqtt.class));
            }
        }catch (Exception e){
            Log.e("LifecycleObserver",e.getMessage());
        }*/

        Log.e("LifecycleObserver","primerPLANO");
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}