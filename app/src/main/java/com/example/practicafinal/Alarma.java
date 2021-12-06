package com.example.practicafinal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class Alarma extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "La alarma se ha disparado", Toast.LENGTH_SHORT).show();
        Log.i("ALARMA", "La alarma se ha disparado");
    }
}