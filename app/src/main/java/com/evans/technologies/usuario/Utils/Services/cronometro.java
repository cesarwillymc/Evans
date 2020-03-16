package com.evans.technologies.usuario.Utils.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.evans.technologies.usuario.R;

import java.util.Locale;

import static com.evans.technologies.usuario.Utils.UtilsKt.getPrimerplano;
import static com.evans.technologies.usuario.Utils.UtilsKt.setEstadoViews;


public class cronometro extends Service {
    Context context=this;
    CountDownTimer countDownTimer;
    String segundos;
    SharedPreferences datadriver;
    MediaPlayer mp;
    @Override
    public void onCreate() {
        datadriver = context.getSharedPreferences("datadriver", Context.MODE_PRIVATE);
        SharedPreferences prefs = context.getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        countDownTimer = new CountDownTimer(25000, 1000) {
            public void onTick(long millisUntilFinished) {

                segundos=String.format(Locale.getDefault(), "%d", millisUntilFinished / 1000L);
                Log.e("tiempocountDownTimer",segundos);
            }

            public void onFinish() {
                Log.e("cronometro","entro true");

                setEstadoViews(datadriver,3);

                if (getPrimerplano(prefs)){
                      Log.e("NotificationReceiver", "is open");
                    Intent intent = new Intent("clase");
                    intent.putExtra("data", "{cronometroFinalizado}");
                    LocalBroadcastManager broadcaster= LocalBroadcastManager.getInstance(context);
                    broadcaster.sendBroadcast(intent);
                    context.stopService(new Intent(context, cronometro.class));
                }else{

                                setEstadoViews(datadriver,3);
                                Intent launchIntent = context.getPackageManager().
                                        getLaunchIntentForPackage("com.evans.technologies.evansuser");
                                launchIntent.setFlags(
                                        Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

                                launchIntent.putExtra("data","{cronometroFinalizado}");
                                context.startActivity(launchIntent);
                                context.stopService(new Intent(context, cronometro.class));
                 }
                           // if (SystemUtils.isAppAlive(context,"com.user.evans.app.evanstechnologiesuser")){

                /*Intent launchIntent = context.getPackageManager().
                        getLaunchIntentForPackage("com.user.evans.app.evanstechnologiesuser");
                launchIntent.setFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

                launchIntent.putExtra("data","cronometroFinalizado");
                context.startActivity(launchIntent);*/

                            //}else{
                              /*  Log.e("cronometro","entro false"+getViajeId(datadriver));
                                countDownTimer.cancel();
                                mp.stop();
                                setEstadoViews(datadriver,3);
                                LocalBroadcastManager broadcaster= LocalBroadcastManager.getInstance(context);
                                Intent intent = new Intent("clase");

                                intent.putExtra("data", "cronometroFinalizado");
                                broadcaster.sendBroadcast(intent);*/

                          //  }




            }




        }.start();
         mp = MediaPlayer.create(this, R.raw.sound);
        mp.setLooping(true);
        mp.start();


        super.onCreate();
    }

    @Override
    public void onDestroy() {
        countDownTimer.cancel();
        mp.stop();

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
