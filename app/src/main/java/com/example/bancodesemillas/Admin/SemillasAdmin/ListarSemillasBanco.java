package com.example.bancodesemillas.Admin.SemillasAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bancodesemillas.FragmentosAdministrador.Semillas.AgregarSemilla;
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

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class ListarSemillasBanco extends AppCompatActivity {

    RecyclerView recyclerViewSemilla;
    FirebaseDatabase mFirebaseDataBase;
    DatabaseReference mReference;

    //Usa un detector de eventos para monitorear los cambios en la consulta de firebase
    FirebaseRecyclerAdapter<Semilla, ViewHolderSemilla> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Semilla> options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_semillas_banco);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Semillas registradas");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        recyclerViewSemilla = findViewById(R.id.recyclerViewSemilla);
        //Se adapta si se agrega o elimina algun elemento
        recyclerViewSemilla.setHasFixedSize(true);

        mFirebaseDataBase = FirebaseDatabase.getInstance();
        mReference = mFirebaseDataBase.getReference("semillas");

        ListarImagenesSemillas();




    }


    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseRecyclerAdapter!=null) {
            firebaseRecyclerAdapter.startListening();
        }
    }

    private void ListarImagenesSemillas() {
        options = new FirebaseRecyclerOptions.Builder<Semilla>().setQuery(mReference, Semilla.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Semilla, ViewHolderSemilla>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderSemilla holder, int position, @NonNull Semilla semilla) {
                ViewHolderSemilla.SeteoSemillas(
                        getApplicationContext(),
                        semilla.getNombreCientificoSemilla(),
                        semilla.getNombreVulgarSemilla(),
                        semilla.getVariedadSemilla(),
                        semilla.getImagenSemilla()
                );

            }

            @NonNull
            @Override
            public ViewHolderSemilla onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //Inflar el Item
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_semilla, parent, false);

                ViewHolderSemilla viewHolderSemilla = new ViewHolderSemilla(itemView);

                viewHolderSemilla.setOnClickListener(new ViewHolderSemilla.ClickListener(){
                    @Override
                    public void onItemClick(View view, int position) {

                        final int id = getItem(position).getIdSemilla();
                        final String imagen = getItem(position).getImagenSemilla();
                        final String nombreCientifico = getItem(position).getNombreCientificoSemilla();
                        final String nombreVulgar = getItem(position).getNombreVulgarSemilla();
                        final String disponibilidad = getItem(position).getDisponibilidad();
                        final String otrosDatos = getItem(position).getOtrosDatosSemilla();
                        final String descripcion = getItem(position).getDescripcionSemilla();
                        final String comoConsumir = getItem(position).getConsumirSemilla();
                        final String manejo = getItem(position).getManejoSemilla();
                        final String mesRecogida = getItem(position).getMesDevolucionSemilla();
                        final String mesReparto = getItem(position).getMesRepartoSemilla();
                        final String viabilidad = getItem(position).getViavilidad();
                        final Double gramosMinimos = getItem(position).getGramosMinimosSemilla();
                        final String procedencia = getItem(position).getProcedenciaSemilla();
                        final String variedad = getItem(position).getVariedadSemilla();

                        Semilla semilla =new Semilla(imagen, nombreCientifico, nombreVulgar, variedad, id, procedencia, gramosMinimos, viabilidad, mesReparto,
                                mesRecogida, manejo, comoConsumir, descripcion, otrosDatos, disponibilidad);

                        Intent intent = new Intent(ListarSemillasBanco.this, InfoSemilla.class);
                        intent.putExtra("SemillaEnviada",semilla);
                        startActivity(intent);

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        final int id = getItem(position).getIdSemilla();
                        final String imagen = getItem(position).getImagenSemilla();
                        final String nombreCientifico = getItem(position).getNombreCientificoSemilla();
                        final String nombreVulgar = getItem(position).getNombreVulgarSemilla();
                        final String disponibilidad = getItem(position).getDisponibilidad();
                        final String otrosDatos = getItem(position).getOtrosDatosSemilla();
                        final String descripcion = getItem(position).getDescripcionSemilla();
                        final String comoConsumir = getItem(position).getConsumirSemilla();
                        final String manejo = getItem(position).getManejoSemilla();
                        final String mesRecogida = getItem(position).getMesDevolucionSemilla();
                        final String mesReparto = getItem(position).getMesRepartoSemilla();
                        final String viabilidad = getItem(position).getViavilidad();
                        final Double gramosMinimos = getItem(position).getGramosMinimosSemilla();
                        final String procedencia = getItem(position).getProcedenciaSemilla();
                        final String variedad = getItem(position).getVariedadSemilla();

                        AlertDialog.Builder builder = new AlertDialog.Builder(ListarSemillasBanco.this);

                        String [] opciones = {"Actualizar", "Eliminar"};
                        builder.setItems(opciones, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                //Actualizar
                                if ( i==0 ) {
                                    Semilla semilla =new Semilla(imagen, nombreCientifico, nombreVulgar, variedad, id, procedencia, gramosMinimos, viabilidad, mesReparto,
                                            mesRecogida, manejo, comoConsumir, descripcion, otrosDatos, disponibilidad);
                                    Intent intent = new Intent(ListarSemillasBanco.this, ActualizarSemilla.class);
                                    intent.putExtra("SemillaEnviada",semilla);
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

                return viewHolderSemilla;


            }
        };

        recyclerViewSemilla.setLayoutManager(new GridLayoutManager(ListarSemillasBanco.this, 2));
        firebaseRecyclerAdapter.startListening();
        recyclerViewSemilla.setAdapter(firebaseRecyclerAdapter);
    }


    private void EliminarDatos(final int idSemilla, final String ImagenActual) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ListarSemillasBanco.this);
        builder.setTitle("Eliminar");
        builder.setMessage("¿Desea eliminar la semilla?");

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Eliminar de la BD
                Query query = mReference.orderByChild("idSemilla").equalTo(idSemilla);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(ListarSemillasBanco.this, "La semilla ha sido eliminada", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ListarSemillasBanco.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                //ELIMINAMOS LA IMAGEN DEL ALMACENAMIENTO
                StorageReference ImagenSeleccionada = getInstance().getReferenceFromUrl(ImagenActual);
                ImagenSeleccionada.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ListarSemillasBanco.this, "Eliminado", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ListarSemillasBanco.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ListarSemillasBanco.this, "Cancelado por el Administrador", Toast.LENGTH_SHORT).show();
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
