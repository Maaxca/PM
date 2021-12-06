package com.example.practicafinal;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Logros extends AppCompatActivity {

    ListView listView;
    TextView txtMetros;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logros);
        listView=findViewById(R.id.listView);
        ManejadordeBasedeDatos manejadordeBasedeDatos=new ManejadordeBasedeDatos(this);
        Cursor cursor=manejadordeBasedeDatos.listarLogros();

        ArrayAdapter<String> arrayAdapter;
        List<String> list=new ArrayList<>();

        if(cursor!=null&&cursor.getCount()>0){
            while(cursor.moveToNext()){
                String fila="";
                fila+="      ID: "+cursor.getString(0);
                fila+=" Hora-Fecha: "+cursor.getString(1);
                fila+=" Puntuacion: "+cursor.getInt(2);
                list.add(fila);
            }
            arrayAdapter=new ArrayAdapter<>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,list);
            listView.setAdapter(arrayAdapter);
        }
        else{
            Toast.makeText(Logros.this, "No hay ningun logro insertado", Toast.LENGTH_SHORT).show();
        }
    }
}