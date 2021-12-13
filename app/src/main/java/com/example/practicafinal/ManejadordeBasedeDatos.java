package com.example.practicafinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ManejadordeBasedeDatos extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="preguntas.db";
    private static final String COLUMN_ID="ID";
    private static final String COLUMN_PREGUNTA="PREGUNTA";
    private static final String COLUMN_RESPUESTA_CORRECTA="CORRECTO";
    private static final String COLUMN_RESPUESTA_INCORRECTA1="INCORRECTO1";
    private static final String COLUMN_RESPUESTA_INCORRECTA2="INCORRECTO2";
    private static final String COLUMN_HORA_FECHA="HORA_FECHA";
    private static final String COLUMN_PUNTUACION="PUNTUACION";
    private static final String COLUMN_BATERIA="BATERIA";
    private static final String COLUMN_GPS="LATITUD";
    private static final String COLUMN_GPS2="LONGITUD";
    private static final String TABLE_NAME="PREGUNTAS";
    private static final String TABLE_NAME2="LOGROS";
    private static final String TABLE_NAME3="ENTRADAS";

    public ManejadordeBasedeDatos(Context context){
        super(context,DATABASE_NAME,null,1);
    }
    public ManejadordeBasedeDatos(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_NAME+" ("+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                COLUMN_PREGUNTA+" TEXT,"+COLUMN_RESPUESTA_CORRECTA+" TEXT,"+COLUMN_RESPUESTA_INCORRECTA1+" TEXT,"+COLUMN_RESPUESTA_INCORRECTA2+" TEXT"+")");
        sqLiteDatabase.execSQL("INSERT INTO "+TABLE_NAME+" ("+COLUMN_PREGUNTA+", "+COLUMN_RESPUESTA_CORRECTA+", "+COLUMN_RESPUESTA_INCORRECTA1+", "+COLUMN_RESPUESTA_INCORRECTA2+")"+" VALUES ("+"'¿Que es eso del cielo?'"+", "+"'Superman'"+", "+"'Pajaro'"+", "+"'Avion'"+")");
        sqLiteDatabase.execSQL("INSERT INTO "+TABLE_NAME+" ("+COLUMN_PREGUNTA+", "+COLUMN_RESPUESTA_CORRECTA+", "+COLUMN_RESPUESTA_INCORRECTA1+", "+COLUMN_RESPUESTA_INCORRECTA2+")"+" VALUES ("+"'¿Cuál iba a ser el nombre de Spider-man?'"+", "+"'The Fly'"+", "+"'Bugman'"+", "+"'Spider-man'"+")");
        sqLiteDatabase.execSQL("INSERT INTO "+TABLE_NAME+" ("+COLUMN_PREGUNTA+", "+COLUMN_RESPUESTA_CORRECTA+", "+COLUMN_RESPUESTA_INCORRECTA1+", "+COLUMN_RESPUESTA_INCORRECTA2+")"+" VALUES ("+"'¿Cual es el videojuego mas vendido de la historia?'"+", "+"'Minecraft'"+", "+"'Tetris'"+", "+"'Super Mario Bros'"+")");
        sqLiteDatabase.execSQL("INSERT INTO "+TABLE_NAME+" ("+COLUMN_PREGUNTA+", "+COLUMN_RESPUESTA_CORRECTA+", "+COLUMN_RESPUESTA_INCORRECTA1+", "+COLUMN_RESPUESTA_INCORRECTA2+")"+" VALUES ("+"'¿Que libro es el 2º libro mas vendido de la historia solo por debajo de la Biblia?'"+", "+"'One Piece'"+", "+"'Codigo Da Vinci'"+", "+"'Nande Koko ni Sensei ga'"+")");
        sqLiteDatabase.execSQL("INSERT INTO "+TABLE_NAME+" ("+COLUMN_PREGUNTA+", "+COLUMN_RESPUESTA_CORRECTA+", "+COLUMN_RESPUESTA_INCORRECTA1+", "+COLUMN_RESPUESTA_INCORRECTA2+")"+" VALUES ("+"'¿En que año salió la pelicula Titanic?'"+", "+"'1997'"+", "+"'1999'"+", "+"'1993'"+")");
        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_NAME2+" ("+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+COLUMN_HORA_FECHA+" DATE,"+COLUMN_PUNTUACION+" INTEGER"+")");
        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_NAME3+" ("+COLUMN_HORA_FECHA+" DATE,"+COLUMN_BATERIA+" FLOAT,"+COLUMN_GPS+" TEXT,"+COLUMN_GPS2+" TEXT"+")");

    }
    public boolean Insertar(String pregunta,String correcta,String incorrecta1,String incorrecta2){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(COLUMN_PREGUNTA,pregunta);
        contentValues.put(COLUMN_RESPUESTA_CORRECTA,correcta);
        contentValues.put(COLUMN_RESPUESTA_INCORRECTA1,incorrecta1);
        contentValues.put(COLUMN_RESPUESTA_INCORRECTA2,incorrecta2);

        long resultado=db.insert(TABLE_NAME,null,contentValues);
        db.close();
        return (resultado!=-1);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean InsertarLogros(int puntuacion){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        contentValues.put(COLUMN_HORA_FECHA, String.valueOf(sdf.format(new Date())));
        contentValues.put(COLUMN_PUNTUACION, puntuacion);

        long resultado=db.insert(TABLE_NAME2,null,contentValues);
        db.close();
        return (resultado!=-1);
    }
    public boolean InsertarEntradas(float bateria,String latitud,String longitud){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        contentValues.put(COLUMN_HORA_FECHA, String.valueOf(sdf.format(new Date())));
        contentValues.put(COLUMN_BATERIA, bateria);
        contentValues.put(COLUMN_GPS, latitud);
        contentValues.put(COLUMN_GPS2, longitud);

        long resultado=db.insert(TABLE_NAME3,null,contentValues);
        db.close();
        return (resultado!=-1);
    }
    public boolean Borrar(String id){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        int borrados=sqLiteDatabase.delete(TABLE_NAME,COLUMN_ID+"=?",new String[]{id});
        sqLiteDatabase.close();
        return (borrados>0);
    }
    public boolean Borrar2(){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        int borrados=sqLiteDatabase.delete(TABLE_NAME2,null,null);
        sqLiteDatabase.close();
        return (borrados>0);
    }
    public boolean Borrar3(){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        int borrados=sqLiteDatabase.delete(TABLE_NAME3,null,null);
        sqLiteDatabase.close();
        return (borrados>0);
    }
    public Cursor listarLogros(){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME2,null);
        return  cursor;
    }
    public Cursor listar(){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        return  cursor;
    }
    public Cursor listar2(String id){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+COLUMN_ID+" = "+id,null);
        return  cursor;
    }
    public Double listar3(){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Double lat=0.0,log=0.0,D=0.0;
        Location location,location2;
        location=new Location("Point A");
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT "+COLUMN_GPS+", "+COLUMN_GPS2+" FROM "+TABLE_NAME3,null);
        if(cursor!=null&&cursor.getCount()>0){
            while(cursor.moveToNext()){
                lat=Double.parseDouble(cursor.getString(0));
                log=Double.parseDouble(cursor.getString(1));
                if(cursor.isFirst()){
                    location.setLatitude(lat);
                    location.setLongitude(log);
                }
                else{
                    location2=new Location("Point B");
                    location2.setLatitude(lat);
                    location2.setLongitude(log);
                    D+=location2.distanceTo(location);
                    location=location2;
                }
            }
        }
        return D;
    }
    public boolean Modificar(String id,String pregunta,String correcta,String incorrecta1,String incorrecta2){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COLUMN_ID,id);
        contentValues.put(COLUMN_PREGUNTA,pregunta);
        contentValues.put(COLUMN_RESPUESTA_CORRECTA,correcta);
        contentValues.put(COLUMN_RESPUESTA_INCORRECTA1,incorrecta1);
        contentValues.put(COLUMN_RESPUESTA_INCORRECTA2,incorrecta2);
        long resultado=sqLiteDatabase.update(TABLE_NAME,contentValues,COLUMN_ID+"=?",new String[]{id});
        sqLiteDatabase.close();

        return(resultado>0);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
