package com.example.practicafinal;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;

import java.util.Random;


public class MiServicioIntenso extends JobIntentService {
    private static final int ID = 1 ;
    public static final String ID_CANAL = "mi canal favorito" ;
    private static final int ID_TRABAJO = 666;
    private static final String NOMBRE3 = "ALGODON";
    LocationManager locationManager;
    LocationListener locationListener;
    String ETIQUETA = "SERVICIOINTENSO",latitud,altitud;
    int contador = 1;
    CuandoPasanCosas cuandoPasanCosas = new CuandoPasanCosas();


    public MiServicioIntenso() {


    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d(ETIQUETA, "Comenzamos a trabajar");

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        getBaseContext().registerReceiver(cuandoPasanCosas, intentFilter);


        while(true){

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


    static void encolarTrabajo(Context context, Intent trabajo){
        enqueueWork(context, MiServicioIntenso.class, ID_TRABAJO,  trabajo);
    }

    class CuandoPasanCosas extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
                        latitud= String.valueOf(location.getLatitude());
                        altitud=String.valueOf(location.getAltitude());
                    }
                };
                IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                Intent batteryStatus = registerReceiver(null, ifilter);

                int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

                float bateria = (level/(float)scale)*100;

                ManejadordeBasedeDatos manejadordeBasedeDatos = new ManejadordeBasedeDatos(getApplicationContext());
                SharedPreferences misDatos = getSharedPreferences(NOMBRE3, MODE_PRIVATE);
                manejadordeBasedeDatos.InsertarEntradas(bateria,misDatos.getString("LATITUD","--sin guardar--"),misDatos.getString("LONGITUD","--sin guardar--"));

            }
        }
    }

}