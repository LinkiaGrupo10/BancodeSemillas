package com.example.bancodesemillas.Admin.SemillasAdmin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bancodesemillas.Admin.GuardasAdmin.ListarGuardasAbiertasSemillaAdmin;
import com.example.bancodesemillas.Admin.GuardasAdmin.ListarGuardasSemillaGuardianes;
import com.example.bancodesemillas.Admin.GuardasAdmin.ListarGuardasSemillasGuardianes;
import com.example.bancodesemillas.Admin.GuardasAdmin.ListarHistorialSemillasGuardianes;
import com.example.bancodesemillas.Admin.GuardianesAdmin.InfoGuardian;
import com.example.bancodesemillas.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class InfoSemilla extends AppCompatActivity {


    TextView NombreCientificoSemilla, NombreVulgarSemilla, DisponibilidaddSemilla,
            OtrosDatosSemilla, DescripcionSemilla, ComoConsumirSemilla,
            ManejoSemilla, MesRecogidaSemilla, MesRepartoSemilla,
            ViabilidadSemilla, GramosMinimosSemilla, ProcedenciaSemilla,
            IdSemilla, VariedadSemilla;

    ImageView ImagenAgregarSemilla;

    Button Volver, guardas, historial;

    Semilla semilla;

    int idSemilla;

    DatabaseReference databaseReference;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_semilla);

        NombreCientificoSemilla = findViewById(R.id.lblNombreCientificoSemilla);
        NombreVulgarSemilla = findViewById(R.id.lblNombreVulgarSemilla);
        DisponibilidaddSemilla = findViewById(R.id.lblDisponibilidaddSemilla);
        OtrosDatosSemilla = findViewById(R.id.lblOtrosDatosSemilla);
        DescripcionSemilla = findViewById(R.id.lblDescripcionSemilla);
        ComoConsumirSemilla = findViewById(R.id.lblComoConsumirSemilla);
        ManejoSemilla = findViewById(R.id.lblManejoSemilla);
        MesRecogidaSemilla = findViewById(R.id.lblMesRecogidaSemilla);
        MesRepartoSemilla = findViewById(R.id.lblMesRepartoSemilla);
        ViabilidadSemilla = findViewById(R.id.lblViabilidadSemilla);
        GramosMinimosSemilla = findViewById(R.id.lblGramosMinimosSemilla);
        ProcedenciaSemilla = findViewById(R.id.lblProcedenciaSemilla);
        IdSemilla = findViewById(R.id.lblidSemilla);
        VariedadSemilla = findViewById(R.id.lblVariedadSemilla);
        ImagenAgregarSemilla = findViewById(R.id.ImagenSemillaInfo);
        Volver = findViewById(R.id.ActualizarSemilla);
        guardas = findViewById(R.id.VerGuardas);
        historial = findViewById(R.id.Historial);

        progressDialog = new ProgressDialog(InfoSemilla.this);

        databaseReference = FirebaseDatabase.getInstance().getReference("SEMILLAS");


        Bundle intent = getIntent().getExtras();

        if ( intent != null) {
            //Recuperar los datos de la actividad anterior

            semilla = (Semilla) intent.getSerializable("SemillaEnviada");
            //Settear

            Picasso.get().load(semilla.getImagenSemilla()).into(ImagenAgregarSemilla);
            NombreCientificoSemilla.setText("Nombre científico: " + semilla.getNombreCientificoSemilla());
            NombreVulgarSemilla.setText("Nombre: " + semilla.getNombreVulgarSemilla());
            DisponibilidaddSemilla.setText("Disponibilidad: " + semilla.getDisponibilidad());
            OtrosDatosSemilla.setText("Otros datos: " + semilla.getOtrosDatosSemilla());
            DescripcionSemilla.setText("Descripción: " + semilla.getDescripcionSemilla());
            ComoConsumirSemilla.setText("Cómo consumir: " + semilla.getConsumirSemilla());
            MesRepartoSemilla.setText("Mes reparto: " + semilla.getMesRepartoSemilla());
            MesRecogidaSemilla.setText("Mes devolución: " + semilla.getMesDevolucionSemilla());
            ViabilidadSemilla.setText("Viabilidad: " + semilla.getViavilidad());
            GramosMinimosSemilla.setText(String.valueOf("Gramos mínimos: " + semilla.getGramosMinimosSemilla()));
            ProcedenciaSemilla.setText("Procedencia: " + semilla.getProcedenciaSemilla());
            VariedadSemilla.setText("Variedad: " + semilla.getVariedadSemilla());
            IdSemilla.setText("Id: " + semilla.getIdSemilla());
            idSemilla = semilla.getIdSemilla();

            guardas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(InfoSemilla.this, ListarGuardasSemillasGuardianes.class);
                    intent.putExtra("idSemilla",idSemilla);
                    startActivity(intent);
                }
            });

            historial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(InfoSemilla.this, ListarHistorialSemillasGuardianes.class);
                    intent.putExtra("idSemilla",idSemilla);
                    startActivity(intent);
                }
            });

        }



        Volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
}