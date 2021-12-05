package com.example.practicafinal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class PantallaConfiguracion extends AppCompatActivity {
    Switch switch1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_configuracion);
        switch1=findViewById(R.id.switch1);
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
    }
}