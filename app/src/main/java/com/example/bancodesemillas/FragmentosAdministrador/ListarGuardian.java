package com.example.bancodesemillas.FragmentosAdministrador;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.bancodesemillas.Adaptador.Adaptador;
import com.example.bancodesemillas.Modelo.Guardian;
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
import java.util.zip.Inflater;

public class ListarGuardian extends Fragment {


    RecyclerView Guardianes_RecylerView;
    Adaptador adaptador;
    List<Guardian> guardianesList;
    FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listar_guardian, container, false);

        Guardianes_RecylerView = view.findViewById(R.id.Guardianes_RecylerView);
        Guardianes_RecylerView.setHasFixedSize(true);
        Guardianes_RecylerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        guardianesList = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();

        ObtenerLista();

        return view;
    }

    private void ObtenerLista() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GUARDIANES");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                guardianesList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Guardian guardian = ds.getValue(Guardian.class);

                    //Condicion para que en la lista se visualicen todos los guardianes menos el que hace la conulta y el admin
                    assert user != null;
                    assert guardian != null;
                    if(!guardian.getUID().equals(user.getUid()) && !guardian.getCORREO().equals("adminlinkia@linkia.com")) {
                        guardianesList.add(guardian);
                    }
                    adaptador = new Adaptador(getActivity(), guardianesList);
                    Guardianes_RecylerView.setAdapter(adaptador);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void BuscarGuardian(String consulta) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GUARDIANES");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                guardianesList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Guardian guardian = ds.getValue(Guardian.class);

                    //Condicion para que en la lista se visualicen todos los guardianes menos el que hace la conulta y el admin
                    assert user != null;
                    assert guardian != null;
                    if(!guardian.getUID().equals(user.getUid()) && !guardian.getCORREO().equals("adminlinkia@linkia.com")) {

                        if (guardian.getNOMBRE().toLowerCase().contains(consulta.toLowerCase()) ||
                                guardian.getCORREO().toLowerCase().contains(consulta.toLowerCase())) {
                            guardianesList.add(guardian);
                        }
                    }
                    adaptador = new Adaptador(getActivity(), guardianesList);
                    Guardianes_RecylerView.setAdapter(adaptador);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Creamos el menu
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_buscar, menu);
        MenuItem item = menu.findItem(R.id.buscar_guardian);

        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String consulta) {
                //Se llama cuando se llama la busqueda desde teclado
                if(!TextUtils.isEmpty(consulta.trim())) {
                    //Si el campo busqueda no esta vacío
                    BuscarGuardian(consulta);
                } else {
                    ObtenerLista();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String consulta) {

                if(!TextUtils.isEmpty(consulta.trim())) {
                    //la busqueda se actualiza sefún se escribe
                    BuscarGuardian(consulta);
                } else {
                    ObtenerLista();
                }

                return false;
            }
        });


        super.onCreateOptionsMenu(menu, inflater);
    }

    //Visualizamos el menu


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }
}