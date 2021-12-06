package com.example.practicafinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PantallaAprender extends AppCompatActivity {
    private static final String ID_CANAL = "El nombre de mi canal";
    Button btnSiguiente;
    ListView lista2;
    TextView txtTitulo;
    Boolean Clickado=true;
    int aleatorio,alea,acertadas=0;
    ArrayAdapter<String> arrayAdapter;
    static int pregunta=0;
    List<String> list = new ArrayList<>();
    static int []preguntas=new int[5];

    static int Aleatorio(){
            int valorEntero =(int) Math.floor(Math.random()*(2-0+1)+0);
            return valorEntero;
    }
    static Boolean comprobar(int valor){
        for (int i=0;i<=pregunta;i++){
            if(valor==preguntas[i]){
                return true;
            }
        }
        return false;

    }
    static int Aleatorio2(int cont){
        int valorEntero=0;
        do {
             valorEntero=(int) Math.floor(Math.random() * (cont - 0 + 1) + 0);
        }while(comprobar(valorEntero));
        return valorEntero;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_aprender);
        aleatorio=Aleatorio();
        btnSiguiente = findViewById(R.id.btnSiguiente);
        lista2 = findViewById(R.id.listview2);
        txtTitulo = findViewById(R.id.txtTitulo);
        ManejadordeBasedeDatos manejadordeBasedeDatos = new ManejadordeBasedeDatos(this);
        Cursor cursor = manejadordeBasedeDatos.listar();
        alea=Aleatorio2(cursor.getCount()-1);

            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToPosition(alea)) {
                    txtTitulo.setText((pregunta+1)+ ". " + cursor.getString(1));
                    if(aleatorio==0) {
                        list.add("       " + cursor.getString(2));
                        list.add("       " + cursor.getString(3));
                        list.add("       " + cursor.getString(4));
                    }
                    else if(aleatorio==1){
                        list.add("       " + cursor.getString(3));
                        list.add("       " + cursor.getString(2));
                        list.add("       " + cursor.getString(4));
                    }
                    else{
                        list.add("       " + cursor.getString(4));
                        list.add("       " + cursor.getString(3));
                        list.add("       " + cursor.getString(2));
                    }
                    arrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, list);
                    lista2.setAdapter(arrayAdapter);
                    preguntas[pregunta]=alea;
                }
            }

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (Clickado==false) {
                    aleatorio = Aleatorio();
                    list.removeAll(list);
                    if (pregunta!=4) {
                        alea=Aleatorio2(cursor.getCount()-1);
                        if (cursor.moveToPosition(alea)) {
                            txtTitulo.setText((pregunta+2) + ". " + cursor.getString(1));
                            if (aleatorio == 0) {
                                list.add("       " + cursor.getString(2));
                                list.add("       " + cursor.getString(3));
                                list.add("       " + cursor.getString(4));
                            } else if (aleatorio == 1) {
                                list.add("       " + cursor.getString(3));
                                list.add("       " + cursor.getString(2));
                                list.add("       " + cursor.getString(4));
                            } else {
                                list.add("       " + cursor.getString(4));
                                list.add("       " + cursor.getString(3));
                                list.add("       " + cursor.getString(2));
                            }
                            arrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, list);
                            lista2.setAdapter(arrayAdapter);
                            Clickado = true;
                            pregunta++;
                            preguntas[pregunta]=alea;
                        }
                    }else {
                        lanzarNotificacionConFoto();
                        manejadordeBasedeDatos.InsertarLogros(acertadas);
                        AlertDialog.Builder builder=new AlertDialog.Builder(PantallaAprender.this);
                        builder.setTitle("Gracias por jugar");
                        builder.setMessage("Se ha enviado una notificacion con tu puntuacion");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent=new Intent(PantallaAprender.this,Pantalla2.class);
                                startActivityForResult(intent,1);
                            }
                        });
                        builder.show();
                        preguntas[0]=0;
                        preguntas[1]=0;
                        preguntas[2]=0;
                        preguntas[3]=0;
                        preguntas[4]=0;
                        pregunta=0;
                        Clickado=true;
                        acertadas=0;
                    }
                }
                else{
                    Toast.makeText(PantallaAprender.this, "Tienes que seleccionar una respuesta", Toast.LENGTH_SHORT).show();
                    Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
                    btnSiguiente.startAnimation(animation);
                }
            }
        });
        lista2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position2, long id) {
                    if(Clickado) {
                        if (aleatorio == position2) {
                            Toast.makeText(PantallaAprender.this, "Bien", Toast.LENGTH_SHORT).show();
                            ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, list) {
                                @NonNull
                                @Override
                                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                                    View view = super.getView(position, convertView, parent);
                                    if (position2 == position) {
                                        view.setBackgroundColor(Color.GREEN);
                                    }
                                    return view;
                                }
                            };
                            lista2.setAdapter(arrayAdapter1);
                            Clickado=false;
                            acertadas++;
                        } else {
                            Toast.makeText(PantallaAprender.this, "Mal", Toast.LENGTH_SHORT).show();
                            ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, list) {
                                @NonNull
                                @Override
                                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                                    View view = super.getView(position, convertView, parent);
                                    if (position2 == position) {
                                        view.setBackgroundColor(Color.RED);
                                    }
                                    if (position==aleatorio){
                                        view.setBackgroundColor(Color.GREEN);
                                    }
                                    return view;
                                }
                            };
                            lista2.setAdapter(arrayAdapter1);
                            Clickado=false;
                        }
                        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                        if(globalVariable.getAlgo().compareTo("ON")==0){
                            Toast.makeText(PantallaAprender.this, "Hola muy buenas tardes", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(PantallaAprender.this, "Ya has seleccionado una respuesta", Toast.LENGTH_SHORT).show();
                    }
                }
        });

    }
    private void lanzarNotificacionConFoto() {
        String idChannel = "Canal 4";
        String nombreCanal = "Mi canal con fotos";
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, ID_CANAL);

        builder.setSmallIcon(R.drawable.ic_launcher_background);

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ben);
        bigPictureStyle.bigPicture(bitmap);
        bigPictureStyle.setBigContentTitle("La puntuacion de tu examen");
        bigPictureStyle.setSummaryText("Tu puntuacion es de: "+acertadas+" de 5");

        builder.setStyle(bigPictureStyle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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

        notificationManager.notify(4, builder.build());
    }
}
