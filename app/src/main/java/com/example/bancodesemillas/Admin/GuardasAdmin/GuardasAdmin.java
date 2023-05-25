package com.example.bancodesemillas.Admin.GuardasAdmin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bancodesemillas.Adaptador.*;
import com.example.bancodesemillas.Modelo.Guarda;
import com.example.bancodesemillas.Modelo.Guardian;
import com.example.bancodesemillas.Modelo.Semilla;
import com.example.bancodesemillas.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;



public class GuardasAdmin extends AppCompatActivity {

    RecyclerView GuardianesGuardas_RecylerView;
    AdaptadorGuardas adaptadorGuardas;

    ArrayAdapter arrayAdapter;
    List<Guardian> guardianesList;
    List<Semilla> semillasList;

    LinkedList <Guardian> guardianesSeleccionados;

    List<String> semillasStrings;
    FirebaseAuth firebaseAuth;

    private Button btn_guarda;

    Spinner SemillaSp;

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardas_admin);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Guardas");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        GuardianesGuardas_RecylerView = findViewById(R.id.GuardianesGuardas_RecylerView);
        GuardianesGuardas_RecylerView.setHasFixedSize(true);
        GuardianesGuardas_RecylerView.setLayoutManager(new LinearLayoutManager(this));
        SemillaSp = findViewById(R.id.SemillaSp);
        btn_guarda = (Button) findViewById(R.id.btn_guarda);
        guardianesList = new ArrayList<>();
        semillasList = new ArrayList<>();
        semillasStrings = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();

        ObtenerLista();
        ObtenerListaSemillas();

        btn_guarda.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                String semillaGuarda = SemillaSp.getSelectedItem().toString();
                String [] aux = semillaGuarda.split(" ");
                int idSemillaGuarda = Integer.parseInt(aux[0]);
                LocalDate fecha = LocalDate.now();
                DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                String fechaGuarda = fecha.format(formatoFecha);
                guardianesSeleccionados = adaptadorGuardas.obtenerSeleccionados();
                for (Guardian g : guardianesSeleccionados) {
                    Guarda guarda = new Guarda(g.getCORREO(), idSemillaGuarda, fechaGuarda, false,"");

                    //Se añade a la base de datos
                    databaseReference.child("GUARDAS").child(g.getUID()+idSemillaGuarda+fechaGuarda).setValue(guarda);
                    Toast.makeText(GuardasAdmin.this, "Guarda creada correctamente", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                    //                assert user != null;
                    //              assert guardian != null;
                    if (!guardian.getUID().equals(user.getUid()) && !guardian.getCORREO().equals("adminlinkia@linkia.com")) {
                        guardianesList.add(guardian);
                    }
                }
                adaptadorGuardas = new AdaptadorGuardas(getApplicationContext(), guardianesList);
                GuardianesGuardas_RecylerView.setAdapter(adaptadorGuardas);
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void ObtenerListaSemillas() {

        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        //Se busca referencia de Persona en la base de datos
        databaseReference.child("semillas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                semillasList.clear();
                semillasStrings.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Semilla semilla = ds.getValue(Semilla.class);
                    String datosSemilla = semilla.getIdSemilla() + " " + semilla.getNombreVulgarSemilla() + " " + semilla.getVariedadSemilla();
                    semillasList.add(semilla);
                    semillasStrings.add(datosSemilla);
                }

                String [] arraySemillasStrings = new String[semillasStrings.size()];
                System.arraycopy(semillasStrings.toArray(), 0, arraySemillasStrings, 0, semillasStrings.size());

                arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, arraySemillasStrings);
                //arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.activity_list_item, semillasStrings);
                SemillaSp.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ListasActivity", "Error al leer la base de datos", error.toException());
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}


