package com.example.practicafinal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
       if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
           Log.d("MYRECEIVER", "Arranco mi servicio");
           MiServicioIntenso.encolarTrabajo(context, new Intent());
       }
    }
}