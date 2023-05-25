package com.example.bancodesemillas.FragmentosAdministrador.Semillas;

import static com.example.bancodesemillas.R.*;
import static com.example.bancodesemillas.R.id.*;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bancodesemillas.Admin.SemillasAdmin.Semilla;
import com.example.bancodesemillas.MainActivityAdministrador;
import com.example.bancodesemillas.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

public class AgregarSemilla extends Fragment implements View.OnClickListener {

    EditText nombreCientificoSemilla, nombreVulgarSemilla, disponibilidaddSemilla, otrosDatosSemilla, descripcionSemilla, ComoConsumirSemilla,
            ManejoSemilla, MesRecogidaSemilla, MesRepartoSemilla, ViabilidadSemilla, GramosMinimosSemilla,
            ProcedenciaSemilla, IdSemilla, variedadSemilla;
    ImageView ImagenAgregarSemilla;
    Button AgregarSemilla;

    String RutaDeAlmacenamiento = "Semilla_Subida";
    String RutaDeBaseDeDatos = "semillas";
    Uri RutaArchivoURI;

    StorageReference mStorageReference;
    DatabaseReference databaseReference;

    ProgressDialog progressDialog;


    int CODIGO_DE_SOLICITUD_IMAGEN = 5;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(layout.fragment_agregar_semilla, container, false);

        ActionBar actionBar = getActivity().getActionBar();


        nombreCientificoSemilla = view.findViewById(R.id.NombreCientificoSemilla);

        nombreVulgarSemilla = view.findViewById(id.NombreVulgarSemilla);
        disponibilidaddSemilla = view.findViewById(id.DisponibilidaddSemilla);
        otrosDatosSemilla = view.findViewById(id.OtrosDatosSemilla);
        descripcionSemilla = view.findViewById(id.DescripcionSemilla);
        ComoConsumirSemilla = view.findViewById(id.ComoConsumirSemilla);
        ManejoSemilla = view.findViewById(id.ManejoSemilla);
        MesRecogidaSemilla = view.findViewById(R.id.MesRecogidaSemilla);
        MesRecogidaSemilla.setFocusable(false);
        MesRecogidaSemilla.setOnClickListener((View.OnClickListener) this);
        MesRepartoSemilla = view.findViewById(id.MesRepartoSemilla);
        ViabilidadSemilla = view.findViewById(id.ViabilidadSemilla);
        GramosMinimosSemilla = view.findViewById(id.GramosMinimosSemilla);
        ProcedenciaSemilla = view.findViewById(id.ProcedenciaSemilla);
        IdSemilla = view.findViewById(idSemilla);
        variedadSemilla = view.findViewById(id.VariedadSemilla);
        ImagenAgregarSemilla = view.findViewById(id.ImagenAgregarSemilla);
        Button AgregarSemilla = view.findViewById(id.AgregarSemilla);


        //Inicializamos firebase
        mStorageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(RutaDeBaseDeDatos);

        progressDialog = new ProgressDialog(getActivity());


        ImagenAgregarSemilla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                ObtenerImagenGaleria.launch(intent);
            }
        });

        AgregarSemilla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubirImagen();
            }
        });

        return view;
    }



    private void SubirImagen() {

        String sem = nombreCientificoSemilla.getText().toString();

        if (RutaArchivoURI != null && !nombreCientificoSemilla.getText().toString().equals("") && !nombreVulgarSemilla.getText().toString().equals("") &&
            !variedadSemilla.getText().toString().equals("") && IdSemilla.getText().toString().length() > 5   ) {
            progressDialog.setTitle("Espero");
            progressDialog.setMessage("Subiendo imagen...");
            progressDialog.show();
            progressDialog.setCancelable(false);
            StorageReference storageReference2 = mStorageReference.child(RutaDeAlmacenamiento
                    + System.currentTimeMillis() + "-" + ObtenerExtensionDelArchivo(RutaArchivoURI));
            storageReference2.putFile(RutaArchivoURI)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //Obtenemos la Uri de la imagen que acabamos de subir
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;

                            Uri downloadURI = uriTask.getResult();
                            String mNombreCientifico = nombreCientificoSemilla.getText().toString();
                            String mNombreVulgar = nombreVulgarSemilla.getText().toString();

                            String mDisponibilidaddSemilla = disponibilidaddSemilla.getText().toString();
                            String mOtrosDatosSemilla = otrosDatosSemilla.getText().toString();
                            String mDescripcionSemilla = descripcionSemilla.getText().toString();
                            String mComoConsumirSemilla = ComoConsumirSemilla.getText().toString();
                            String mManejoSemilla = ManejoSemilla.getText().toString();

                            String mMesRecogidaSemilla = MesRecogidaSemilla.getText().toString();
                            String mMesRepartoSemilla = MesRepartoSemilla.getText().toString();
                            String mViabilidadSemilla = ViabilidadSemilla.getText().toString();
                            Double mGramosMinimosSemilla = Double.parseDouble(GramosMinimosSemilla.getText().toString());
                            String mProcedenciaSemilla = ProcedenciaSemilla.getText().toString();
                            int mIdSemilla = Integer.parseInt(IdSemilla.getText().toString());
                            String mVariedadSemilla = variedadSemilla.getText().toString();



                                Semilla semilla = new Semilla(downloadURI.toString(), mNombreCientifico, mNombreVulgar,
                                        mVariedadSemilla, mIdSemilla, mProcedenciaSemilla, mGramosMinimosSemilla,
                                        mViabilidadSemilla, mMesRepartoSemilla, mMesRecogidaSemilla, mManejoSemilla, mComoConsumirSemilla,
                                        mDescripcionSemilla, mOtrosDatosSemilla, mDisponibilidaddSemilla);
                                String ID_IMAGEN = databaseReference.push().getKey();

                                Query query = databaseReference.orderByChild("idSemilla").equalTo(mIdSemilla);

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("semillas/" + mIdSemilla + "/idSemilla");
                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            Toast.makeText(getActivity(), "El id de  la Semilla ya está registrado", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getActivity(), MainActivityAdministrador.class));
                                            getActivity().finish();
                                        } else {
                                            databaseReference.child(String.valueOf(mIdSemilla)).setValue(semilla);

                                            progressDialog.dismiss();
                                            Toast.makeText(getActivity(), "Semilla Registrada", Toast.LENGTH_SHORT).show();

                                            startActivity(new Intent(getActivity(), MainActivityAdministrador.class));
                                            getActivity().finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getActivity(), "Cancelado", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }


                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            progressDialog.setTitle("Registrando");
                            progressDialog.setCancelable(false);
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "Faltan campos obligatorios por rellenar como Imagen, Id mayor a 5 numeros , Nombre o Variedad", Toast.LENGTH_SHORT).show();
        }
    }

    //Obtenemos extension archivo de imagen
    private String ObtenerExtensionDelArchivo(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    //Obtener imagen de galería
    private ActivityResultLauncher<Intent> ObtenerImagenGaleria = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //manejar el resultado de nuestro intent
                    if (result.getResultCode() == Activity.RESULT_OK){
                        //Selección de imagen
                        Intent data = result.getData();
                        //Obtener uri de imagen
                        RutaArchivoURI = data.getData();
                        ImagenAgregarSemilla.setImageURI(RutaArchivoURI);
                    }
                    else {
                        Toast.makeText(getActivity(), "Cancelado", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                // Aquí puedes actualizar el campo de texto con la fecha seleccionada
                String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDayOfMonth;
                MesRecogidaSemilla.setText(selectedDate);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.MesRecogidaSemilla) {
            showDatePickerDialog();
        }
    }
}

