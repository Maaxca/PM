package com.example.practicafinal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PantallaConfiguracion extends AppCompatActivity {
    private static final String ID_CANAL = "El nombre de mi canal";
    private static final int CODIGO_RESPUESTA = 1;
    private static final int CODIGO_ALARMA = 666;
    Switch switch1,switch2;
    Button btnLogros,btnBorrarLogros;
    FloatingActionButton btnCompartir;
    EditText txtMinutos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_configuracion);
        switch1=findViewById(R.id.switch1);
        switch2=findViewById(R.id.switch2);
        btnLogros=findViewById(R.id.btnLogros);
        btnBorrarLogros=findViewById(R.id.btnBorrarLogros);
        btnCompartir=findViewById(R.id.btnCompartir);
        txtMinutos=findViewById(R.id.txtMinutos);
        ManejadordeBasedeDatos manejadordeBasedeDatos=new ManejadordeBasedeDatos(this);
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        if(globalVariable.getAlgo().compareTo("ON")==0){
            switch1.setChecked(true);
            switch1.setText("ON");
        }
        else{
            switch1.setChecked(false);
            switch1.setText("OFF");
        }
        if(globalVariable.getAlgo2().compareTo("ON")==0){
            switch2.setChecked(true);
            switch2.setText("ON");
            txtMinutos.setEnabled(true);
        }
        else{
            switch2.setChecked(false);
            switch2.setText("OFF");
            txtMinutos.setEnabled(false);
        }
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                    globalVariable.setAlgo("ON");
                    switch1.setText("ON");
                }
                else{
                    final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                    globalVariable.setAlgo("OFF");
                    switch1.setText("OFF");
                }
            }
        });
        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                    globalVariable.setAlgo2("ON");
                    switch2.setText("ON");
                    txtMinutos.setEnabled(true);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(getApplicationContext(), Alarma.class);

                    PendingIntent pendingIntent =  PendingIntent.getBroadcast(getApplicationContext(), CODIGO_ALARMA,intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),Integer.parseInt(txtMinutos.getText().toString())+000*60, pendingIntent);
                }
                else{
                    final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                    globalVariable.setAlgo2("OFF");
                    switch2.setText("OFF");
                    txtMinutos.setEnabled(false);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(getApplicationContext(), Alarma.class);
                    PendingIntent pendingIntent =  PendingIntent.getBroadcast(getApplicationContext(), CODIGO_ALARMA,intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.cancel(pendingIntent);
                }
            }
        });
        btnLogros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PantallaConfiguracion.this,Logros.class);
                startActivityForResult(intent,3);
            }
        });
        btnBorrarLogros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(PantallaConfiguracion.this);
                builder.setTitle("Cuidado");
                builder.setMessage("¿Estas seguro que deseas borrar los logros y las entradas?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        manejadordeBasedeDatos.Borrar2();
                        manejadordeBasedeDatos.Borrar3();
                        lanzarNotificacion();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });

        btnCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor=manejadordeBasedeDatos.listarLogros();
                cursor.moveToLast();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "He acertado "+cursor.getString(2)+" de 5 en el test de preguntas y tu ya has jugado?");
                intent.setPackage("com.whatsapp");
                startActivity(intent);

                if (getPackageManager().getLaunchIntentForPackage("com.whatsapp") != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(PantallaConfiguracion.this, "Instala whastsapp para que rule", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    private void lanzarNotificacion() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, ID_CANAL);

        builder.setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Atencion")
                .setAutoCancel(false).setContentText("Se han borrado los datos de la tabla de logros y entradas");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String idChannel = "Canal 1";
            String nombreCanal = "Mi canal favorito";
            NotificationChannel notificationChannel = new NotificationChannel(idChannel, nombreCanal, NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableVibration(true);
            notificationChannel.setShowBadge(true);
            builder.setChannelId(idChannel);
            notificationManager.createNotificationChannel(notificationChannel);

        } else {

            //Menor que oreo
            builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);
        }

        //Acción tras pulsar la notificación
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(getApplicationContext());
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("IDENTIFICADOR", "Notificación simple"); //Le paso mensaje
        taskStackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(CODIGO_RESPUESTA, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        notificationManager.notify(1, builder.build());
    }
}