package com.example.bancodesemillas.Admin.GuardasAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.bancodesemillas.Adaptador.Adaptador;
import com.example.bancodesemillas.Adaptador.AdaptadorSemillasGuardas;
import com.example.bancodesemillas.Modelo.Guardian;
import com.example.bancodesemillas.Modelo.Semilla;
import com.example.bancodesemillas.Modelo.Guarda;
import com.example.bancodesemillas.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListarGuardasAbiertasSemillaAdmin extends AppCompatActivity {

    RecyclerView Guardas_RecylerView;
    AdaptadorSemillasGuardas adaptador;
    List<Guarda> guardas;

    List <Semilla> semillas;
    String mailUser;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase mFirebaseDataBase;
    DatabaseReference mReference;

    DatabaseReference ReferencesSemillas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_guardas_abiertas_semilla_admin);

        Guardas_RecylerView = findViewById(R.id.GuardasAbiertasAdmin_RecylerView);
        Guardas_RecylerView.setHasFixedSize(true);
        Guardas_RecylerView.setLayoutManager(new LinearLayoutManager(this));
        guardas = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDataBase = FirebaseDatabase.getInstance();
        mReference = mFirebaseDataBase.getReference("GUARDAS");


        ReferencesSemillas = mFirebaseDataBase.getReference("semillas");

        Bundle intent = getIntent().getExtras();

        if ( intent != null) {
            //Recuperar los datos de la actividad anterior

            mailUser = (String) intent.getSerializable("MailUser");
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

                    String emailGuarda = guarda.getMailGuardian();

                    if(Objects.equals(emailGuarda, mailUser)){

                        guardas.add(guarda);
                    }

                }
                semillas = new ArrayList<>();

                List<Integer> idSemillas = new ArrayList<>();

                for (int i = 0 ; i< guardas.size(); i++){
                    int idSemilla = guardas.get(i).getIdSemilla();


                    if (idSemillas.isEmpty()){
                        idSemillas.add(idSemilla);
                    }else if(!idSemillas.contains(idSemilla)){
                        idSemillas.add(idSemilla);
                    }

                }


                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("semillas");
                reference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot1) {
                        int numeroSemillas = 0;

                        for (DataSnapshot ds1 : snapshot1.getChildren()) {
                            Semilla sem = ds1.getValue(Semilla.class);

                            int idSemilla1 = sem.getIdSemilla();

                            for (int i = 0; i < idSemillas.size(); i++) {

                                if (idSemilla1 == idSemillas.get(i)) {

                                    semillas.add(sem);
                                }

                            }


                        }


                        //                  }
                        adaptador = new AdaptadorSemillasGuardas(getApplication(), semillas);
                        Guardas_RecylerView.setAdapter(adaptador);
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