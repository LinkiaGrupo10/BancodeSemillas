package com.example.bancodesemillas.Admin.GuardasAdmin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bancodesemillas.MainActivityAdministrador;
import com.example.bancodesemillas.Modelo.Guarda;
import com.example.bancodesemillas.Modelo.Guardian;
import com.example.bancodesemillas.Modelo.Historial;
import com.example.bancodesemillas.Modelo.Semilla;
import com.example.bancodesemillas.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DetallesHistorial extends AppCompatActivity {

    String idGuardian;
    int idSemilla;

    TextView nombreSemilla,nombreGuardian, observaciones;


    Button eliminar, inicio;


    FirebaseDatabase mFirebaseDataBase2;
    DatabaseReference ReferenceSemillas;
    DatabaseReference ReferencesGuardianes;
    DatabaseReference ReferencesGuardas;
    DatabaseReference ReferencesHistorial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_historial);

        Bundle intent = getIntent().getExtras();


        nombreGuardian = findViewById(R.id.textGuardian);
        nombreSemilla = findViewById(R.id.text_Semilla);
        observaciones = findViewById(R.id.TextObservaciones);
        inicio = findViewById(R.id.inicio);
        eliminar = findViewById(R.id.eliminar);

        mFirebaseDataBase2 = FirebaseDatabase.getInstance();
        ReferencesGuardas = mFirebaseDataBase2.getReference("GUARDAS");
        ReferenceSemillas = mFirebaseDataBase2.getReference("semillas");
        ReferencesGuardianes = mFirebaseDataBase2.getReference("GUARDIANES");
        ReferencesHistorial = mFirebaseDataBase2.getReference("HISTORIAL");


        if ( intent != null) {
            //Recuperar los datos de la actividad anterior

            idGuardian = (String) intent.getSerializable("idGuardian");
            idSemilla = (int) intent.getSerializable("idSemilla");

            RellenarCampos();

        }



    }

    private void RellenarCampos(){

        ReferencesGuardianes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {

                    Guardian guardian = ds.getValue(Guardian.class);
                    if (guardian.getUID().equals(idGuardian)){
                        nombreGuardian.setText(guardian.getNOMBRE());

                        ReferenceSemillas.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot1) {

                                for (DataSnapshot ds1 : snapshot1.getChildren()){
                                    Semilla semilla = ds1.getValue(Semilla.class);
                                    if (semilla.getIdSemilla() == idSemilla){

                                        nombreSemilla.setText(semilla.getNombreVulgarSemilla());

                                        ReferencesHistorial.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot2) {


                                                for (DataSnapshot ds2 : snapshot2.getChildren()){

                                                    Historial historial = ds2.getValue(Historial.class);

                                                    if (historial.getIdSemilla() == semilla.getIdSemilla() && historial.getMailGuardian().equals(guardian.getCORREO())){

                                                            observaciones.setText(historial.getObservaciones());



                                                        inicio.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                Intent intent = new Intent(getApplicationContext(), MainActivityAdministrador.class);
                                                                getApplicationContext().startActivity(intent);
                                                            }

                                                        });


                                                        eliminar.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                ReferencesHistorial.child(guardian.getUID()+historial.getIdSemilla()+historial.getFechaGuarda()).removeValue();
                                                                Toast.makeText(DetallesHistorial.this, "Se ha eliminado la guarda!", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(getApplicationContext(), MainActivityAdministrador.class);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                getApplicationContext().startActivity(intent);



                                                            }
                                                        });


                                                    }

                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }



}