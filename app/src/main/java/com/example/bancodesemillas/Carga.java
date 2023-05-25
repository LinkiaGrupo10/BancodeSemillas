package com.example.bancodesemillas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class Carga extends AppCompatActivity {

    TextView app_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carga);

        //CAMBIO DE LETRA
        String ubicacion = "fuentes/sans_negrita.ttf";
        Typeface tf = Typeface.createFromAsset(Carga.this.getAssets(),
                ubicacion);
        //CAMBIO DE LETRA

        app_name = findViewById(R.id.app_name);

        //Cargamos 3 segundos la animacion
        final int Duracion = 3000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Codigo que se ejecuta despues de la animación
                Intent intent = new Intent(Carga.this, Login.class);
                startActivity(intent);
                finish();
            }
        }, Duracion);

        //Asignamos el nuevo tipo de letra
        app_name.setTypeface(tf);
    }
}