package com.example.bancodesemillas.Adaptador;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bancodesemillas.Modelo.Guardian;
import com.example.bancodesemillas.R;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorGuardas extends RecyclerView.Adapter<AdaptadorGuardas.MyHolder> {

    private Context context;
    private List<Guardian> mGuardianes;
    private boolean modoSeleccion;
    private SparseBooleanArray seleccionados;

    public AdaptadorGuardas(Context context, List<Guardian> mGuardianes) {
        this.context = context;
        this.mGuardianes = mGuardianes;
        seleccionados = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //INflar el GuardianGuardas_layout
        View view = LayoutInflater.from(context).inflate(R.layout.guardian_guardas_item, parent, false);
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
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
        //          @Override
        //        public void onClick(View v) {

        //      }
        //});
    }

    @Override
    public int getItemCount() {
        return mGuardianes.size();
    }

    /**Devuelve aquellos objetos marcados.*/
    public LinkedList<Guardian> obtenerSeleccionados() {
        LinkedList<Guardian> marcados = new LinkedList<>();
        for (int i = 0; i < mGuardianes.size(); i++) {
            if (seleccionados.get(i)){
                marcados.add(mGuardianes.get(i));
            }
        }
        return marcados;
    }


    public class MyHolder extends RecyclerView.ViewHolder {


        //Declaramos las vistas
        CircleImageView PerfilGuardian;
        TextView NombreGuardianItem, CorreoGuardianItem;

        private View item;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            this.item = itemView;
            PerfilGuardian = itemView.findViewById(R.id.PerfilGuardian);
            NombreGuardianItem = itemView.findViewById(R.id.NombreGuardianItem);
            CorreoGuardianItem = itemView.findViewById(R.id.CorreoGuardianItem);

            //Selecciona el objeto si estaba seleccionado
            if (seleccionados.get(getBindingAdapterPosition())){
                item.setSelected(true);
            } else
                item.setSelected(false);


            /**Activa el modo de selección*/
            item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!modoSeleccion){
                        modoSeleccion = true;
                        v.setSelected(true);
                        seleccionados.put(getBindingAdapterPosition(), true);
                    }
                    v.setBackgroundResource(R.color.selected);
                    return true;
                }
            });
            /**Selecciona/deselecciona un ítem si está activado el modo selección*/
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (modoSeleccion) {
                        if (!v.isSelected()) {
                            v.setSelected(true);
                            v.setBackgroundResource(R.color.selected);
                            seleccionados.put(getBindingAdapterPosition(), true);
                        } else {
                            v.setSelected(false);
                            v.setBackgroundResource(R.color.normal);
                            seleccionados.put(getBindingAdapterPosition(), false);
                            if (!haySeleccionados())
                                modoSeleccion = false;
                        }
                    }
                }
            });
        }


        public boolean haySeleccionados() {
            for (int i = 0; i <= mGuardianes.size(); i++) {
                if (seleccionados.get(i))
                    return true;
            }
            return false;
        }






    }
}







