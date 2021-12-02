package com.example.practicafinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Pantalla2 extends AppCompatActivity {
Button buttonPreguntas,buttonAprender,buttonConfiguracion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla2);
        buttonPreguntas=findViewById(R.id.buttonPreguntas);
        buttonAprender=findViewById(R.id.buttonAprender);
        buttonConfiguracion=findViewById(R.id.buttonConfiguracion);

        buttonPreguntas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Pantalla2.this,PantallaPreguntas.class);
                startActivityForResult(intent,1);
            }
        });

        buttonAprender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        buttonConfiguracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}