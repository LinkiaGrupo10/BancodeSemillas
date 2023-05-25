package com.example.bancodesemillas.Admin.GuardianesAdmin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bancodesemillas.Admin.GuardasAdmin.ListarGuardasAbiertasSemillaAdmin;
import com.example.bancodesemillas.Admin.SemillasAdmin.InfoSemilla;
import com.example.bancodesemillas.Admin.SemillasAdmin.Semilla;
import com.example.bancodesemillas.Modelo.Guardian;
import com.example.bancodesemillas.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class InfoGuardian extends AppCompatActivity {


    TextView Nombre, Correo, Telefono,
            Direccion, Asamblea, Municipio;


    ImageView ImagenGuardian;

    Button Volver, guardas;

    Guardian guardian;

    String mailUser;
    DatabaseReference databaseReference;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_guardian);

        Nombre = findViewById(R.id.tvNombre);
        Correo = findViewById(R.id.tvCorreo);
        Telefono = findViewById(R.id.tvTelefono);
        Direccion = findViewById(R.id.tvDireccion);
        Asamblea = findViewById(R.id.tvAsamblea);
        Municipio= findViewById(R.id.tvMunicipio);
        ImagenGuardian = findViewById(R.id.ImagenGuardianInfo);

        Volver = findViewById(R.id.Volver);
        guardas = findViewById(R.id.GuardasAbiertas);


        progressDialog = new ProgressDialog(InfoGuardian.this);

        databaseReference = FirebaseDatabase.getInstance().getReference("GUARDIANES");

        Bundle intent = getIntent().getExtras();

        if ( intent != null) {
            //Recuperar los datos de la actividad anterior

            guardian = (Guardian) intent.getSerializable("GuardianEnviado");
            //Settear

            Picasso.get().load(guardian.getIMAGEN()).into(ImagenGuardian);
            Nombre.setText("Nombre: " + guardian.getNOMBRE());
            Correo.setText("Correo: " + guardian.getCORREO());
            Telefono.setText("Telefono: " + guardian.getTELEFONO());
            Direccion.setText("Dirección: " + guardian.getDIRECCION());
            Asamblea.setText("Pertenece Asamblea: " + guardian.getASAMBLEA());
            Municipio.setText("Municipio: " + guardian.getMUNICIPIO());

            mailUser = guardian.getCORREO();
        }

        guardas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoGuardian.this, ListarGuardasAbiertasSemillaAdmin.class);
                intent.putExtra("MailUser",mailUser);
                startActivity(intent);
            }
        });

        Volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}