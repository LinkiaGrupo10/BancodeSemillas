package com.example.bancodesemillas.Admin.GuardianesAdmin;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bancodesemillas.Admin.GuardianesAdmin.ViewHolderGuardian;
import com.example.bancodesemillas.Admin.SemillasAdmin.ListarSemillasBanco;
import com.example.bancodesemillas.Modelo.Guardian;
import com.example.bancodesemillas.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;

public class ListarGuardianesAdmin extends AppCompatActivity {


    RecyclerView recyclerViewGuardian ;
    FirebaseDatabase mFirebaseDataBase;
    DatabaseReference mReference;

    //Usa un detector de eventos para monitorear los cambios en la consulta de firebase
    FirebaseRecyclerAdapter<Guardian, ViewHolderGuardian> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Guardian> options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_guardianes_admin);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Guardianes registrados");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        recyclerViewGuardian = findViewById(R.id.recyclerViewGuardian);
        //Se adapta si se agrega o elimina algun elemento
        recyclerViewGuardian.setHasFixedSize(true);

        mFirebaseDataBase = FirebaseDatabase.getInstance();
        mReference = mFirebaseDataBase.getReference("GUARDIANES");

        ListarImagenesGuardianes();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseRecyclerAdapter!=null) {
            firebaseRecyclerAdapter.startListening();
        }
    }


    private void ListarImagenesGuardianes() {
        options = new FirebaseRecyclerOptions.Builder<Guardian>().setQuery(mReference, Guardian.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Guardian, ViewHolderGuardian>(options) {


            @Override
            protected void onBindViewHolder(@NonNull ViewHolderGuardian holder, int position, @NonNull Guardian guardian) {
                ViewHolderGuardian.SeteoGuardianes(
                        getApplicationContext(),
                        guardian.getNOMBRE(),
                        guardian.getCORREO(),
                        guardian.getIMAGEN()

                );

            }

            @NonNull
            @Override
            public ViewHolderGuardian onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //Inflar el Item
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guardian, parent, false);

                ViewHolderGuardian viewHolderGuardian = new ViewHolderGuardian(itemView);

                viewHolderGuardian.setOnClickListener(new ViewHolderGuardian.ClickListener(){
                    @Override
                    public void onItemClick(View view, int position) {


                        final String id = getItem(position).getUID();
                        final String correo = getItem(position).getCORREO();
                        final String imagen = getItem(position).getIMAGEN();
                        final String nombre = getItem(position).getNOMBRE();
                        final String telefono = getItem(position).getTELEFONO();
                        final String direccion = getItem(position).getDIRECCION();
                        final String asamblea = getItem(position).getASAMBLEA();
                        final String municipio = getItem(position).getMUNICIPIO();
                        final String password= getItem(position).getPASSWORD();

                       Guardian guardian =new Guardian(correo,nombre,telefono,direccion,asamblea,imagen,id,municipio,password);


                        Intent intent = new Intent(ListarGuardianesAdmin.this, InfoGuardian.class);
                        intent.putExtra("GuardianEnviado",guardian);
                        startActivity(intent);

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                        final String id = getItem(position).getUID();
                        final String correo = getItem(position).getCORREO();
                        final String imagen = getItem(position).getIMAGEN();
                        final String nombre = getItem(position).getNOMBRE();
                        final String telefono = getItem(position).getTELEFONO();
                        final String direccion = getItem(position).getDIRECCION();
                        final String asamblea = getItem(position).getASAMBLEA();
                        final String municipio = getItem(position).getMUNICIPIO();
                        final String password= getItem(position).getPASSWORD();

                        AlertDialog.Builder builder = new AlertDialog.Builder(ListarGuardianesAdmin.this);


                        String [] opciones = {"Actualizar", "Eliminar"};
                        builder.setItems(opciones, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                //Actualizar
                                if ( i==0 ) {


                                    Guardian guardian =new Guardian(correo,nombre,telefono,direccion,asamblea,imagen,id,municipio,password);

                                    Intent intent = new Intent(ListarGuardianesAdmin.this, ActualizarGuardian.class);

                                    intent.putExtra("GuardianEnviado",guardian);
                                    startActivity(intent);


                                }
                                //Eliminar
                                if ( i==1 ) {
                                    EliminarDatos(id, imagen);
                                }
                            }
                        });
                        builder.create().show();

                    }

                });


                return viewHolderGuardian;


            }

        };

        recyclerViewGuardian.setLayoutManager(new GridLayoutManager(ListarGuardianesAdmin.this, 2));
        firebaseRecyclerAdapter.startListening();
        recyclerViewGuardian.setAdapter(firebaseRecyclerAdapter);
    }


    private void EliminarDatos(final String idBorrar, final String ImagenBorrar) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ListarGuardianesAdmin.this);
        builder.setTitle("Eliminar");
        builder.setMessage("¿Desea eliminar el guardian?");

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Eliminar de la BD
                Query query = mReference.orderByChild("uid").equalTo(idBorrar);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(ListarGuardianesAdmin.this, "El guardian se ha eliminado", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ListarGuardianesAdmin.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                //ELIMINAMOS LA IMAGEN DEL ALMACENAMIENTO
                StorageReference ImagenSeleccionada = getInstance().getReferenceFromUrl(ImagenBorrar);
                ImagenSeleccionada.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ListarGuardianesAdmin.this, "Eliminado", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ListarGuardianesAdmin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ListarGuardianesAdmin.this, "Cancelado por el Administrador", Toast.LENGTH_SHORT).show();
            }
        });

        //Miostramos el cuadro de dialogo
        builder.create().show();
    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}


