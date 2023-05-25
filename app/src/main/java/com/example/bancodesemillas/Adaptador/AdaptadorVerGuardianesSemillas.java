package com.example.bancodesemillas.Adaptador;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bancodesemillas.Admin.GuardasAdmin.DetallesGuardas;
import com.example.bancodesemillas.Admin.GuardasAdmin.DetallesHistorial;
import com.example.bancodesemillas.Admin.GuardasAdmin.ListarGuardasAbiertasSemillaAdmin;
import com.example.bancodesemillas.Admin.GuardianesAdmin.InfoGuardian;
import com.example.bancodesemillas.Modelo.Guardian;
import com.example.bancodesemillas.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;



public class AdaptadorVerGuardianesSemillas  extends RecyclerView.Adapter<AdaptadorVerGuardianesSemillas.MyHolder> {

    private Context context;
    private List<Guardian> mGuardianes;

    private int idSemilla, deta;


    public AdaptadorVerGuardianesSemillas(Context context, List<Guardian> mGuardianes, int idSemilla, int deta) {
        this.context = context;
        this.mGuardianes = mGuardianes;
        this.idSemilla = idSemilla;
        this.deta = deta;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //INflar el Guardian_layout
        View view = LayoutInflater.from(context).inflate(R.layout.item_guardian_guardas, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        //Obtenemos los datos del modelo
        String UID = mGuardianes.get(position).getUID();
        String IMAGEN = mGuardianes.get(position).getIMAGEN();
        String NOMBRE = mGuardianes.get(position).getNOMBRE();
        String CORREO = mGuardianes.get(position).getCORREO();
        String TELEFONO = mGuardianes.get(position).getTELEFONO();
        String DIRECCION = mGuardianes.get(position).getDIRECCION();
        String ASAMBLEA = mGuardianes.get(position).getASAMBLEA();

        //Seteo de datos
        holder.NombreGuardianItem.setText(NOMBRE);
        holder.CorreoGuardianItem.setText(CORREO);

        try {
            //Si existe la imagen en la BD
            Picasso.get().load(IMAGEN).placeholder(R.drawable.android).into(holder.PerfilGuardian);
        } catch (Exception e) {
            //Si no existe la imagen en la BD
            Picasso.get().load(R.drawable.android).into(holder.PerfilGuardian);
        }
        //Al hacer click en un guardian
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mGuardianes.size();
    }



    public class MyHolder extends RecyclerView.ViewHolder {

        //Declaramos las vistas
        CircleImageView PerfilGuardian;
        TextView NombreGuardianItem, CorreoGuardianItem;

        Button detalles;


        public MyHolder(@NonNull View itemView) {
            super(itemView);


            PerfilGuardian = itemView.findViewById(R.id.PerfilGuardian);
            NombreGuardianItem = itemView.findViewById(R.id.NombreGuardianItem);
            CorreoGuardianItem = itemView.findViewById(R.id.CorreoGuardianItem);
            detalles = itemView.findViewById(R.id.detalles);


            detalles.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (deta == 1) {

                        String idGuardian = mGuardianes.get(getBindingAdapterPosition()).getUID();
                        Intent intent = new Intent(context.getApplicationContext(), DetallesGuardas.class);
                        intent.putExtra("idGuardian", idGuardian);
                        intent.putExtra("idSemilla", idSemilla);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(intent);
                    }
                    if (deta == 2) {

                        String idGuardian = mGuardianes.get(getBindingAdapterPosition()).getUID();
                        Intent intent = new Intent(context.getApplicationContext(), DetallesHistorial.class);
                        intent.putExtra("idGuardian", idGuardian);
                        intent.putExtra("idSemilla", idSemilla);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(intent);
                    }

                    //Toast.makeText(context.getApplicationContext(), mGuardianes.get(getBindingAdapterPosition()).getUID(), Toast.LENGTH_SHORT).show();
                }
            });




        }
    }
}
