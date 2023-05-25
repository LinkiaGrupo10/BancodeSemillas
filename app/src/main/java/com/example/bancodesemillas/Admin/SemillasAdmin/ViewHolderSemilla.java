package com.example.bancodesemillas.Admin.SemillasAdmin;

import android.content.Context;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bancodesemillas.R;
import com.squareup.picasso.Picasso;

public class ViewHolderSemilla extends RecyclerView.ViewHolder {

    static View mView;

    private ViewHolderSemilla.ClickListener mClickListener;
    private ClickListener onClickListener;

    public static void SeteoSemillas(Context context, String nombreCientifico,
                                     String nombreVulgar, String variedad, String imagen) {

        ImageView ImagenSemilla;
        TextView NombreImagenSemilla;
        TextView NombreCompletoSemilla;

        //Conexion con el item
        ImagenSemilla = mView.findViewById(R.id.ImagenSemilla);
        NombreImagenSemilla = mView.findViewById(R.id.NombreImagenSemilla);
        NombreCompletoSemilla = mView.findViewById(R.id.NombreCompletoSemilla);

        NombreImagenSemilla.setText(nombreCientifico);
        String nombreCompletoSemilla = nombreVulgar + " " + variedad;
        NombreCompletoSemilla.setText(nombreCompletoSemilla);

        //Controlar posibles errores carga imagenes firebase y picasso
        try {


                Picasso.get().load(imagen).into(ImagenSemilla);




        }
        catch (Exception e){
            //Fallo Traida imgen
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    public void setOnClickListener(ViewHolderSemilla.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public ClickListener getOnClickListener() {
        return onClickListener;
    }




    //Click va a detalles, Click Largo menu borrar o modificar
    public interface ClickListener {
        void onItemClick (View view, int position);
        void onItemLongClick (View view, int position);

    }


    public ViewHolderSemilla(@NonNull View itemView) {
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
