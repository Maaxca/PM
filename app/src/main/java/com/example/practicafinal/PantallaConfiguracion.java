package com.example.practicafinal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class PantallaConfiguracion extends AppCompatActivity {
    Switch switch1;
    Button btnLogros,btnBorrarLogros;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_configuracion);
        switch1=findViewById(R.id.switch1);
        btnLogros=findViewById(R.id.btnLogros);
        btnBorrarLogros=findViewById(R.id.btnBorrarLogros);
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
    }
}