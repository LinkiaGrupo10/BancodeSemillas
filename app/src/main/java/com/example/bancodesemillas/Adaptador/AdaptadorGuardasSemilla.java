package com.example.bancodesemillas.Adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bancodesemillas.Admin.SemillasAdmin.Semilla;
import com.example.bancodesemillas.R;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdaptadorGuardasSemilla extends RecyclerView.Adapter<AdaptadorGuardasSemilla.MyHolder> {

    private Context context;
    private List<Semilla> mSemilla;

    public AdaptadorGuardasSemilla(Context context1, List<Semilla> semillas) {
        this.context = context1;
        this.mSemilla = semillas;

    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //INflar el Guardian_layout
        View view = LayoutInflater.from(context).inflate(R.layout.item_semilla, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        //Obtenemos los datos del modelo

        String IMAGEN = mSemilla.get(position).getImagenSemilla();
        String NOMBRE = mSemilla.get(position).getNombreVulgarSemilla();



        //Seteo de datos
        holder.NombreImagenSemilla.setText(NOMBRE);
        holder.NombreCompletoSemilla.setText(NOMBRE);

        try {
            //Si existe la imagen en la BD
            Picasso.get().load(IMAGEN).placeholder(R.drawable.android).into(holder.ImagenSemilla);
        } catch (Exception e) {
            //Si no existe la imagen en la BD
            Picasso.get().load(R.drawable.android).into(holder.ImagenSemilla);
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
        return mSemilla.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        //Declaramos las vistas
        ImageView ImagenSemilla;
        TextView NombreImagenSemilla, NombreCompletoSemilla;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            ImagenSemilla = itemView.findViewById(R.id.ImagenSemilla);
            NombreImagenSemilla= itemView.findViewById(R.id.NombreImagenSemilla);
            NombreCompletoSemilla = itemView.findViewById(R.id.NombreCompletoSemilla);

        }
    }
}
