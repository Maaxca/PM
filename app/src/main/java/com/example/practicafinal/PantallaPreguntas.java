package com.example.practicafinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PantallaPreguntas extends AppCompatActivity {

    Button btnMostrar,btnAñadir,btnModificar,btnBorrar;
    FloatingActionButton btnAtras;
    EditText txtID,txtPregunta,txtCorrecta,txtIncorrecta1,txtIncorrecta2;
    ListView lista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_preguntas);
        btnMostrar=findViewById(R.id.btnMostrar);
        ManejadordeBasedeDatos manejadordeBasedeDatos=new ManejadordeBasedeDatos(this);
        lista=findViewById(R.id.listView);
        registerForContextMenu(lista);
        txtID=findViewById(R.id.txtID);
        txtPregunta=findViewById(R.id.txtPregunta);
        txtCorrecta=findViewById(R.id.txtCorrecta);
        txtIncorrecta1=findViewById(R.id.txtIncorrecta1);
        txtIncorrecta2=findViewById(R.id.txtIncorrecta2);
        btnAñadir=findViewById(R.id.btnAñadir);
        btnModificar=findViewById(R.id.btnModificar);
        btnBorrar=findViewById(R.id.btnBorrar);
        btnAtras=findViewById(R.id.btnAtras);
        btnMostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor=manejadordeBasedeDatos.listar();

                ArrayAdapter<String> arrayAdapter;
                List<String> list=new ArrayList<>();

                if(cursor!=null&&cursor.getCount()>0){
                    while(cursor.moveToNext()){
                        String fila="";
                        fila+="    ID: "+cursor.getString(0);
                        fila+=" Pregunta: "+cursor.getString(1);
                        list.add(fila);
                    }
                    arrayAdapter=new ArrayAdapter<>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,list);
                    lista.setAdapter(arrayAdapter);
                }
                else{
                    Toast.makeText(PantallaPreguntas.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PantallaPreguntas.this,Pantalla2.class);
                startActivityForResult(intent,1);
            }
        });


        btnAñadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean resultado=manejadordeBasedeDatos.Insertar(txtPregunta.getText().toString(),txtCorrecta.getText().toString(),txtIncorrecta1.getText().toString(),txtIncorrecta2.getText().toString());
                if(resultado){
                    Toast.makeText(PantallaPreguntas.this,"Insertada pregunta correctamente",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(PantallaPreguntas.this, "Error en la inserccion", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean resultado=manejadordeBasedeDatos.Modificar(txtID.getText().toString(),txtPregunta.getText().toString(),txtCorrecta.getText().toString(),txtIncorrecta1.getText().toString(),txtIncorrecta2.getText().toString());
                Toast.makeText(PantallaPreguntas.this,resultado ?"Actualizado hecho correctamente":"No se ha actualizado",Toast.LENGTH_SHORT).show();
            }
        });
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean borrado=manejadordeBasedeDatos.Borrar(txtID.getText().toString());
                Toast.makeText(PantallaPreguntas.this,borrado?"Borrado hecho correctamente":"No se ha borrado",Toast.LENGTH_SHORT).show();
            }
        });
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor=manejadordeBasedeDatos.listar2(Integer.toString(position+1));
                if(cursor!=null&&cursor.getCount()>0){
                    while(cursor.moveToNext()){
                        txtID.setText(cursor.getString(0));
                        txtPregunta.setText(cursor.getString(1));
                        txtCorrecta.setText(cursor.getString(2));
                        txtIncorrecta1.setText(cursor.getString(3));
                        txtIncorrecta2.setText(cursor.getString(4));
                    }
                }
                else{
                    Toast.makeText(PantallaPreguntas.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.add(0, v.getId(),0,"Mostrar");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        ManejadordeBasedeDatos manejadordeBasedeDatos=new ManejadordeBasedeDatos(this);
        int Posi = info.position;

        if (item.getTitle()=="Mostrar"){
            Cursor cursor=manejadordeBasedeDatos.listar2(Integer.toString(Posi+1));
            if(cursor!=null&&cursor.getCount()>0){
                while(cursor.moveToNext()){
                    AlertDialog.Builder builder=new AlertDialog.Builder(this);
                    builder.setTitle("Datos de la Pregunta");
                    builder.setMessage("ID: "+cursor.getString(0)+
                    "\n Pregunta: "+cursor.getString(1)+
                    "\n Respuesta Correcta: "+cursor.getString(2)+
                    "\n Respuesta Incorrecta1: "+cursor.getString(3)+
                    "\n Respuesta Incorrecta2: "+cursor.getString(4));
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
                }
            }
            else{
                Toast.makeText(PantallaPreguntas.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }
}