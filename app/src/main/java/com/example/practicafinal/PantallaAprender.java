package com.example.practicafinal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class PantallaAprender extends AppCompatActivity {
Button btnSiguiente;
ListView lista2;
TextView txtTitulo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_aprender);
        btnSiguiente=findViewById(R.id.btnSiguiente);
        lista2=findViewById(R.id.listview2);
        txtTitulo=findViewById(R.id.txtTitulo);
    }
}