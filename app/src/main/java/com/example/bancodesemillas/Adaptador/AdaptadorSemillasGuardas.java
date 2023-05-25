package com.example.bancodesemillas.Adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bancodesemillas.Modelo.Guardian;
import com.example.bancodesemillas.Modelo.Semilla;
import com.example.bancodesemillas.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorSemillasGuardas extends RecyclerView.Adapter<AdaptadorSemillasGuardas.MyHolder> {

    private Context context;
    private List<Semilla> mSemillas;

    public AdaptadorSemillasGuardas(Context context, List<Semilla> mSemillas) {
        this.context = context;
        this.mSemillas = mSemillas;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //INflar el guarda_abierta_admin_layout
        View view = LayoutInflater.from(context).inflate(R.layout.guarda_abierta_admin_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        //Obtenemos los datos del modelo
        int IDSEMILLA = mSemillas.get(position).getIdSemilla();
        String IMAGEN = mSemillas.get(position).getImagenSemilla();
        String NOMBRECIENTIFICO = mSemillas.get(position).getNombreCientificoSemilla();
        String NOMBREVULGAR = mSemillas.get(position).getNombreVulgarSemilla();
        String VARIEDAD = mSemillas.get(position).getVariedadSemilla();

        //Seteo de datos
        holder.NombreVulgarVariedadItem.setText(NOMBRECIENTIFICO);
        holder.NombreCientificoItem.setText(NOMBREVULGAR+VARIEDAD);

        try {
            //Si existe la imagen en la BD
            Picasso.get().load(IMAGEN).placeholder(R.drawable.android).into(holder.FotoGuardaSemilla);
        } catch (Exception e) {
            //Si no existe la imagen en la BD
            Picasso.get().load(R.drawable.android).into(holder.FotoGuardaSemilla);
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
        return mSemillas.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        //Declaramos las vistas
        CircleImageView FotoGuardaSemilla;
        TextView NombreCientificoItem, NombreVulgarVariedadItem;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            FotoGuardaSemilla = itemView.findViewById(R.id.FotoSemillaGuarda);
            NombreCientificoItem = itemView.findViewById(R.id.NombreCientificoItem);
            NombreVulgarVariedadItem = itemView.findViewById(R.id.NombreVulgarVariedadItem);

        }
    }
}
