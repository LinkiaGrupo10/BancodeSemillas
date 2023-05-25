package com.example.bancodesemillas.Admin.GuardasAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.bancodesemillas.Adaptador.AdaptadorVerGuardianesSemillas;
import com.example.bancodesemillas.Modelo.Guarda;
import com.example.bancodesemillas.Modelo.Guardian;
import com.example.bancodesemillas.Modelo.Historial;
import com.example.bancodesemillas.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListarHistorialSemillasGuardianes extends AppCompatActivity {

    RecyclerView Guardianes_RecylerView;
    AdaptadorVerGuardianesSemillas adaptador;
    List<Guardian> guardianesList;
    FirebaseAuth firebaseAuth;
    List<Historial> historial;
    int idSemilla;
    FirebaseDatabase mFirebaseDataBase;
    FirebaseDatabase mFirebaseDataBase2;
    DatabaseReference mReference;

    DatabaseReference ReferencesGuardianes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_historial_semillas_guardianes);


        Guardianes_RecylerView = findViewById(R.id.recyclerViewGuardian);
        Guardianes_RecylerView.setHasFixedSize(true);
        Guardianes_RecylerView.setLayoutManager(new LinearLayoutManager(this));

        historial = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDataBase = FirebaseDatabase.getInstance();
        mFirebaseDataBase2 = FirebaseDatabase.getInstance();
        mReference = mFirebaseDataBase.getReference("HISTORIAL");


        ReferencesGuardianes = mFirebaseDataBase2.getReference("GUARDIANES");

        Bundle intent = getIntent().getExtras();

        if ( intent != null) {
            //Recuperar los datos de la actividad anterior

            idSemilla = (int) intent.getSerializable("idSemilla");
        }

        ObtenerLista();


    }

    private void ObtenerLista() {
        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //String emailUser = user.getEmail();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("HISTORIAL");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                historial.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Historial guarda = ds.getValue(Historial.class);

                    int idHistorialSemilla = guarda.getIdSemilla();

                    if(Objects.equals(idHistorialSemilla, idSemilla)){

                        historial.add(guarda);
                    }

                }
                guardianesList = new ArrayList<>();

                List<String> mailsGuardian = new ArrayList<>();

                for (int i = 0 ; i< historial.size(); i++){
                    String emailGuardian = historial.get(i).getMailGuardian();


                    if (mailsGuardian.isEmpty()){
                        mailsGuardian.add(emailGuardian);
                    }else if(!mailsGuardian.contains(emailGuardian)){
                        mailsGuardian.add(emailGuardian);
                    }

                }


                //  DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("GUARDIANES");
                ReferencesGuardianes.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot1) {


                        for (DataSnapshot ds1 : snapshot1.getChildren()) {

                            Guardian guar = ds1.getValue(Guardian.class);

                            String emailGuardian = guar.getCORREO();

                            for (int i = 0; i < mailsGuardian.size(); i++) {

                                if (emailGuardian.equals(mailsGuardian.get(i))) {

                                    guardianesList.add(guar);
                                }

                            }


                        }


                        //                  }
                        adaptador = new AdaptadorVerGuardianesSemillas(getApplication(), guardianesList,idSemilla, 2);
                        Guardianes_RecylerView.setAdapter(adaptador);

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }


                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}