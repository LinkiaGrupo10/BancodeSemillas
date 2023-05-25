package com.example.bancodesemillas.Admin.GuardasAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.bancodesemillas.Adaptador.Adaptador;
import com.example.bancodesemillas.Adaptador.AdaptadorSemillasGuardas;
import com.example.bancodesemillas.Adaptador.AdaptadorVerGuardianesSemillas;
import com.example.bancodesemillas.Modelo.Guarda;
import com.example.bancodesemillas.Modelo.Guardian;
import com.example.bancodesemillas.Modelo.Semilla;
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

public class ListarGuardasSemillasGuardianes extends AppCompatActivity {

    RecyclerView Guardianes_RecylerView;
    AdaptadorVerGuardianesSemillas adaptador;
    List<Guardian> guardianesList;
    FirebaseAuth firebaseAuth;
    List<Guarda> guardas;
    int idSemilla;
    FirebaseDatabase mFirebaseDataBase;
    FirebaseDatabase mFirebaseDataBase2;
    DatabaseReference mReference;

    DatabaseReference ReferencesGuardianes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_guardas_semillas_guardianes);

        Guardianes_RecylerView = findViewById(R.id.recyclerViewGuardian);
        Guardianes_RecylerView.setHasFixedSize(true);
        Guardianes_RecylerView.setLayoutManager(new LinearLayoutManager(this));

        guardas = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDataBase = FirebaseDatabase.getInstance();
        mFirebaseDataBase2 = FirebaseDatabase.getInstance();
        mReference = mFirebaseDataBase.getReference("GUARDAS");


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
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GUARDAS");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                guardas.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Guarda guarda = ds.getValue(Guarda.class);

                    int idGuardaSemilla = guarda.getIdSemilla();

                    if(Objects.equals(idGuardaSemilla, idSemilla)){

                        guardas.add(guarda);
                    }

                }
                guardianesList = new ArrayList<>();

                List<String> mailsGuardian = new ArrayList<>();

                for (int i = 0 ; i< guardas.size(); i++){
                    String emailGuardian = guardas.get(i).getMailGuardian();


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
                        adaptador = new AdaptadorVerGuardianesSemillas(getApplication(), guardianesList,idSemilla, 1);
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