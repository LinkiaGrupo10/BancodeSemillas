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

public class DetallesGuardas extends AppCompatActivity {
    String idGuardian;
    int idSemilla;

    TextView nombreSemilla,nombreGuardian;
    EditText editObservacion;

    Button guardar, cerrar, eliminar;


    FirebaseDatabase mFirebaseDataBase2;
    DatabaseReference ReferenceSemillas;
    DatabaseReference ReferencesGuardianes;
    DatabaseReference ReferencesGuardas;
    DatabaseReference ReferencesHistorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_guardas);

        Bundle intent = getIntent().getExtras();


        nombreGuardian = findViewById(R.id.textGuardian);
        nombreSemilla = findViewById(R.id.text_Semilla);
        editObservacion = findViewById(R.id.editTextGuardas);
        guardar = findViewById(R.id.guardar);
        cerrar = findViewById(R.id.cerrarGuarda);
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

                                        ReferencesGuardas.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot2) {


                                                for (DataSnapshot ds2 : snapshot2.getChildren()){

                                                    Guarda guarda = ds2.getValue(Guarda.class);

                                                    if (guarda.getIdSemilla() == semilla.getIdSemilla() && guarda.getMailGuardian().equals(guardian.getCORREO())){

                                                        if (!guarda.getObservaciones().isEmpty()){
                                                            editObservacion.setText(guarda.getObservaciones());
                                                        }


                                                        guardar.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                Guarda newGuarda = new Guarda();

                                                                newGuarda.setFechaGuarda(guarda.getFechaGuarda());
                                                                newGuarda.setGuardaCerrada(guarda.isGuardaCerrada());
                                                                newGuarda.setIdSemilla(guarda.getIdSemilla());
                                                                newGuarda.setMailGuardian(guarda.getMailGuardian());
                                                                newGuarda.setObservaciones(editObservacion.getText().toString());

                                                                ReferencesGuardas .child(guardian.getUID()+newGuarda.getIdSemilla()+newGuarda.getFechaGuarda()).setValue(newGuarda);
                                                                Toast.makeText(DetallesGuardas.this, "Se ha actualizado la guarda!", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(getApplicationContext(), MainActivityAdministrador.class);
                                                                getApplicationContext().startActivity(intent);
                                                            }

                                                        });

                                                        cerrar.setOnClickListener(new View.OnClickListener() {
                                                            @RequiresApi(api = Build.VERSION_CODES.O)
                                                            @Override
                                                            public void onClick(View v) {
                                                                Historial newHistorial = new Historial();

                                                                LocalDate fecha = LocalDate.now();
                                                                DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                                                                String fechaCierre = fecha.format(formatoFecha);

                                                                newHistorial.setFechaGuarda(guarda.getFechaGuarda());
                                                                newHistorial.setFechaCierre(fechaCierre);
                                                                newHistorial.setGuardaCerrada(true);
                                                                newHistorial.setIdSemilla(guarda.getIdSemilla());
                                                                newHistorial.setMailGuardian(guarda.getMailGuardian());
                                                                newHistorial.setObservaciones(editObservacion.getText().toString());

                                                                ReferencesHistorial .child(guardian.getUID()+newHistorial.getIdSemilla()+newHistorial.getFechaCierre()).setValue(newHistorial);
                                                                ReferencesGuardas.child(guardian.getUID()+guarda.getIdSemilla()+guarda.getFechaGuarda()).removeValue();
                                                                Toast.makeText(DetallesGuardas.this, "Se ha cerrado la guarda!", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(getApplicationContext(), MainActivityAdministrador.class);
                                                                getApplicationContext().startActivity(intent);
                                                            }
                                                        });

                                                        eliminar.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                ReferencesGuardas.child(guardian.getUID()+guarda.getIdSemilla()+guarda.getFechaGuarda()).removeValue();
                                                                Toast.makeText(DetallesGuardas.this, "Se ha eliminado la guarda!", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(getApplicationContext(), MainActivityAdministrador.class);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                getApplicationContext().startActivity(intent);



                                                                                      ///// AQUI METER EN HISTORIAL LA GUARDA /////
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