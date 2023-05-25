package com.example.bancodesemillas.Adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bancodesemillas.Modelo.Guardian;
import com.example.bancodesemillas.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adaptador extends RecyclerView.Adapter<Adaptador.MyHolder> {

    private Context context;
    private List<Guardian> mGuardianes;

    public Adaptador(Context context, List<Guardian> mGuardianes) {
        this.context = context;
        this.mGuardianes = mGuardianes;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //INflar el Guardian_layout
        View view = LayoutInflater.from(context).inflate(R.layout.guardian_item, parent, false);
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


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            PerfilGuardian = itemView.findViewById(R.id.PerfilGuardian);
            NombreGuardianItem = itemView.findViewById(R.id.NombreGuardianItem);
            CorreoGuardianItem = itemView.findViewById(R.id.CorreoGuardianItem);

        }
    }
}
