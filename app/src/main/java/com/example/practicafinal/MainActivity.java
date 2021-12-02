package com.example.practicafinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
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
    private static final String NOMBRE = "ALGO";
    private static final String ETIQUETA_FOTO = "BUENAS";
    Boolean si=false;
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
                    editor.putString(ETIQUETA_FOTO, fichero.getAbsolutePath());
                    editor.apply();
                    Intent intent=new Intent(MainActivity.this,Pantalla2.class);
                    intent.putExtra("","");
                    startActivityForResult(intent,0);

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
    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(gallery, "Selecciona una galeria"),PICK_IMAGE);
    }
}