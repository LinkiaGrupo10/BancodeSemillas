package com.example.bancodesemillas.FragmentosGuardian;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.example.bancodesemillas.Adaptador.Adaptador;
import com.example.bancodesemillas.Adaptador.AdaptadorGuardasSemilla;
import com.example.bancodesemillas.Admin.SemillasAdmin.ActualizarSemilla;
import com.example.bancodesemillas.Admin.SemillasAdmin.InfoSemilla;
import com.example.bancodesemillas.Admin.SemillasAdmin.ListarSemillasBanco;
import com.example.bancodesemillas.Admin.SemillasAdmin.Semilla;
import com.example.bancodesemillas.Admin.SemillasAdmin.ViewHolderSemilla;
import com.example.bancodesemillas.Modelo.Guarda;
import com.example.bancodesemillas.Modelo.Guardian;
import com.example.bancodesemillas.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;


public class ListarSemillasGuardian extends Fragment {



    RecyclerView recyclerViewSemilla;
    FirebaseDatabase mFirebaseDataBase;
    DatabaseReference mReference;

    FirebaseAuth firebaseAuth;
    DatabaseReference ReferencesSemillas;

    //Usa un detector de eventos para monitorear los cambios en la consulta de firebase
    FirebaseRecyclerAdapter<Semilla, ViewHolderSemilla> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Semilla> options;

    AdaptadorGuardasSemilla adaptador;
    List<Guarda> guardas;

    List <Semilla> semillas;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_listar_semillas_guardian, container, false);

        recyclerViewSemilla = view.findViewById(R.id.recyclerViewSemillaGuardian);


        //Se adapta si se agrega o elimina algun elemento
        recyclerViewSemilla.setHasFixedSize(true);

        mFirebaseDataBase = FirebaseDatabase.getInstance();
        mReference = mFirebaseDataBase.getReference("GUARDAS");


        ReferencesSemillas = mFirebaseDataBase.getReference("semillas");


        guardas = new ArrayList<>();

        obtenerListaSemillas();


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(firebaseRecyclerAdapter!=null) {
            firebaseRecyclerAdapter.startListening();
        }
    }


    //Lista de semillas del guardian
    private void obtenerListaSemillas() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String emailUser = user.getEmail();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GUARDAS");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                guardas.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Guarda guarda = ds.getValue(Guarda.class);
                    //Comprobamos el correo
                    String emailGuarda = guarda.getMailGuardian();

                    if(Objects.equals(emailGuarda, emailUser)){

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

                          int idSemilla1 =  sem.getIdSemilla() ;

                          for ( int i = 0; i < idSemillas.size() ; i++){

                             if(idSemilla1 == idSemillas.get(i)){

                                 semillas.add(sem);
                             }

                          }


                        }



                        Context context1 = getContext();

                        LinearLayoutManager manager = new LinearLayoutManager(context1);
                        recyclerViewSemilla.setLayoutManager(manager);
                        adaptador = new AdaptadorGuardasSemilla(context1, semillas);
                        recyclerViewSemilla.setAdapter(adaptador);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }


                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error.getCode());
                System.out.println("*******************************************aaaaaaaaaaaaaa****************");
            }
        });



    }


    public List<Semilla> obtenerSemilla(int Id){

        List<Semilla> semEncontradas = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("semillas");

        reference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                  Semilla  sem= ds.getValue(Semilla.class);

                  int idSemilla = sem.getIdSemilla();
                    if (idSemilla == Id){

                         semEncontradas.add(sem);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

        return semEncontradas;
    }

}