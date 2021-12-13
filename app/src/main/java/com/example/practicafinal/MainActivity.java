package com.example.practicafinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final String NOMBRE_FICHERO = "COSA";
    private static final String ID_CANAL2 = "El nombre de mi canal";
    private static final String NOMBRE = "ALGO";
    private static final String ETIQUETA_FOTO = "BUENAS";
    private static final String NOMBRE2 = "COSA2";
    private static final String ALGO = "NOMBRE";
    private static final int CODIGO_RESPUESTA = 1;
    private static final String NOMBRE3 = "ALGODON";
    Boolean si=false,entrar=false;
    Button buttonHacerFoto,buttonSeleccionar,buttonEntrar;
    ImageView imageView;
    EditText txtClave;
    private static final int VENGO_DE_CAMARA = 1;
    private static final int PEDI_PERMISO_ESCRITURA = 1;
    private static final int VENGO_DE_CAMARA_CON_CALIDAD = 2;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    Button btnHacerFoto,btnHacerFotoCalidad;
    private File fichero;
    private static final int PERMISO_GPS = 5;
    private static final long TIEMPO_REFRESCO = 500;
    String altitud,latitud;
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PEDI_PERMISO_ESCRITURA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                hacerLaFotoConCalidad();
            } else {
                Toast.makeText(this, "Sin permiso de escritura no hay foto de calidad", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == PERMISO_GPS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "No tienes permisos.", Toast.LENGTH_SHORT).show();
                } else {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIEMPO_REFRESCO, 0, locationListener);
                }

            } else {
                Toast.makeText(this, "Debes darme persmisos para continuar", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void hacerLaFotoConCalidad() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            fichero = crearFicheroFoto();
        } catch (IOException e) {
            e.printStackTrace();
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this,"com.example.practicafinal.fileprovider", fichero));

        if (intent.resolveActivity(getPackageManager()) != null) { //Debo permitir la consulta en el android manifest
            startActivityForResult(intent, VENGO_DE_CAMARA_CON_CALIDAD);
        } else {
            Toast.makeText(MainActivity.this, "Necesitas instalar o tener una cámara.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VENGO_DE_CAMARA && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        } else if (requestCode == VENGO_DE_CAMARA_CON_CALIDAD) {

            Bitmap rotacion=BitmapFactory.decodeFile(fichero.getAbsolutePath());
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(fichero.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            int grados=0;

            Matrix matrix= new Matrix();
            switch (orientation){
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.postRotate(90);
                    grados=90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.postRotate(180);
                    grados=180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.postRotate(270);
                    grados=270;
                    break;
                default:
                    break;
            }
            imageView.setImageBitmap(rotacion);
            imageView.setRotation(grados);
        }
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    private File crearFicheroFoto() throws IOException {
        String fechaYHora = new SimpleDateFormat("yyyyMMdd_HH_mm_ss_").format(new Date());
        String nombreFichero = "fotos_" + fechaYHora;
        File carpetaFotos = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        carpetaFotos.mkdirs();
        File imagenAltaResolucion = File.createTempFile(nombreFichero, ".jpg", carpetaFotos);
        return imagenAltaResolucion;
    }

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    latitud= String.valueOf(location.getLatitude());
                    altitud=String.valueOf(location.getLongitude());
                    SharedPreferences misDatos = getSharedPreferences(NOMBRE3, MODE_PRIVATE);
                    SharedPreferences.Editor editor = misDatos.edit();
                    editor.putString("LATITUD",latitud);
                    editor.putString("LONGITUD",altitud);
                    editor.apply();
                }
            };
            pedirPermisoGps();
        MiServicioIntenso.encolarTrabajo(getApplicationContext(), new Intent());
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        buttonHacerFoto=findViewById(R.id.buttonHacerFoto);
        buttonSeleccionar=findViewById(R.id.buttonSeleccionar);
        buttonEntrar=findViewById(R.id.buttonEntrar);
        imageView=findViewById(R.id.imageView);
        txtClave=findViewById(R.id.txtClave);
        SharedPreferences misDatos = getSharedPreferences(NOMBRE_FICHERO, MODE_PRIVATE);
            if(misDatos.getString(NOMBRE,"-- sin guardar --").compareTo("-- sin guardar --")==0){
                si=false;
            }
            else{
                si=true;

                Bitmap rotacion=BitmapFactory.decodeFile(misDatos.getString(ETIQUETA_FOTO,"-- sin guardar --"));
                ExifInterface exif = null;
                try {
                    exif = new ExifInterface(misDatos.getString(ETIQUETA_FOTO,"-- sin guardar --"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                int grados=0;

                Matrix matrix= new Matrix();
                switch (orientation){
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        matrix.postRotate(90);
                        grados=90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        matrix.postRotate(180);
                        grados=180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        matrix.postRotate(270);
                        grados=270;
                        break;
                    default:
                        break;
                }

                imageView.setImageBitmap(rotacion);
                imageView.setRotation(grados);
            }

        buttonHacerFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    pedirPermisoParaFoto();
                    entrar=true;
            }
        });

        buttonSeleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        buttonEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(si==false && txtClave.getText().toString().compareTo("")==0){
                    Toast.makeText(MainActivity.this, "No puedes dejar la clave en blanco", Toast.LENGTH_SHORT).show();
                }
                else if(si==true && misDatos.getString(NOMBRE,"-- sin guardar --").compareTo(txtClave.getText().toString())!=0){
                    Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
                    txtClave.startAnimation(animation);
                    buttonEntrar.startAnimation(animation);
                    Toast.makeText(MainActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }
                else{
                    SharedPreferences misDatos = getSharedPreferences(NOMBRE_FICHERO, MODE_PRIVATE);
                    SharedPreferences.Editor editor = misDatos.edit();
                    editor.putString(NOMBRE,txtClave.getText().toString());
                    if(entrar==false){
                        editor.putString(ETIQUETA_FOTO, misDatos.getString(ETIQUETA_FOTO,"-- sin guardar --"));
                    }
                    else{
                        editor.putString(ETIQUETA_FOTO, fichero.getAbsolutePath());
                    }
                    editor.apply();
                    Intent intent=new Intent(MainActivity.this,Pantalla2.class);
                    intent.putExtra("","");
                    startActivityForResult(intent,0);
                    SharedPreferences misDatos2 = getSharedPreferences(NOMBRE2, MODE_PRIVATE);
                    if(misDatos2.getString(ALGO,"-- sin guardar --").compareTo("-- sin guardar --")!=0){
                        lanzarNotificacion(misDatos2.getString(ALGO,"-- sin guardar --"));
                    }
                }
            }
        });
    }
    private void pedirPermisoParaFoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) { //No tengo permiso
            //Pedir permiso:
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PEDI_PERMISO_ESCRITURA);
            }

        } else {
            hacerLaFotoConCalidad();
        }

    }
    private void pedirPermisoGps(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISO_GPS);
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIEMPO_REFRESCO, 0, locationListener);
    }
    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(gallery, "Selecciona una galeria"),PICK_IMAGE);
    }
    private void lanzarNotificacion(String acertadas) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, ID_CANAL2);

        ManejadordeBasedeDatos manejadordeBasedeDatos = new ManejadordeBasedeDatos(getApplicationContext());
        builder.setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Puntuacion del ultimo examen y metros recorridos")
                .setAutoCancel(false).setContentText("Acertaste "+acertadas+" de 5"+" y recorriste "+Double.toString(manejadordeBasedeDatos.listar3())+" metros");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String idChannel = "Canal 1";
            String nombreCanal = "Mi canal favorito";
            NotificationChannel notificationChannel = new NotificationChannel(idChannel, nombreCanal, NotificationManager.IMPORTANCE_DEFAULT);

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