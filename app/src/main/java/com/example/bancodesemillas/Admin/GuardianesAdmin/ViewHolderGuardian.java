package com.example.bancodesemillas.Admin.GuardianesAdmin;

import android.content.Context;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bancodesemillas.R;
import com.squareup.picasso.Picasso;

public class ViewHolderGuardian extends RecyclerView.ViewHolder {

    static View mView;

    private ViewHolderGuardian.ClickListener mClickListener;
    private ClickListener onClickListener;


    public static void SeteoGuardianes(Context context, String nombre,
                                     String correo,String imagen) {

        ImageView ImagenGuardian;
        TextView NombreImagenGuardian;
        TextView CorreoGuardian;


        //Conexion con el item
        ImagenGuardian = mView.findViewById(R.id.ImagenGuardian);
        NombreImagenGuardian = mView.findViewById(R.id.NombreImagenGuardian);
        CorreoGuardian = mView.findViewById(R.id.CorreoGuardian);

        NombreImagenGuardian.setText(nombre);
        CorreoGuardian.setText(correo);

        //Controlar posibles errores carga imagenes firebase y picasso
        try {
            //Imagen traida con exito
            Picasso.get().load(imagen).into(ImagenGuardian);

        }
        catch (Exception e){
            //Fallo Traida imgen
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    public void setOnClickListener(com.example.bancodesemillas.Admin.GuardianesAdmin.ViewHolderGuardian.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public com.example.bancodesemillas.Admin.GuardianesAdmin.ViewHolderGuardian.ClickListener getOnClickListener() {
        return onClickListener;
    }




    //Click va a detalles, Click Largo menu borrar o modificar
    public interface ClickListener {
        void onItemClick (View view, int position);
        void onItemLongClick (View view, int position);

    }


    public ViewHolderGuardian(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getBindingAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view, getBindingAdapterPosition());
                return true;
            }
        });
    }



}
