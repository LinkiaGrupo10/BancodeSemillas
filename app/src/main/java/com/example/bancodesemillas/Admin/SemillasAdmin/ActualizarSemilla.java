package com.example.bancodesemillas.Admin.SemillasAdmin;

import static com.google.firebase.FirebaseApp.getInstance;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class ActualizarSemilla extends AppCompatActivity {

    EditText NombreCientificoSemillaActualizar, NombreVulgarSemillaActualizar, DisponibilidaddSemillaActualizar,
            OtrosDatosSemillaActualizar, DescripcionSemillaActualizar, ComoConsumirSemillaActualizar,
            ManejoSemillaActualizar, MesRecogidaSemillaActualizar, MesRepartoSemillaActualizar,
            ViabilidadSemillaActualizar, GramosMinimosSemillaActualizar, ProcedenciaSemillaActualizar,
            IdSemillaActualizar, VariedadSemillaActualizar;
    ImageView ImagenAgregarSemillaActualizar;
    Button ActualizarSemilla;

    Semilla rSemilla;

    private Uri imagenURI;
    String imagenSemilla;

    String RutaDeAlmacenamiento = "Semilla_Subida";

    Uri RutaArchivoURI;

    StorageReference mStorageReference;
    DatabaseReference databaseReference;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_semilla);

        NombreCientificoSemillaActualizar = findViewById(R.id.NombreCientificoSemillaActualizar);
        NombreVulgarSemillaActualizar = findViewById(R.id.NombreVulgarSemillaActualizar);
        DisponibilidaddSemillaActualizar = findViewById(R.id.DisponibilidaddSemillaActualizar);
        OtrosDatosSemillaActualizar = findViewById(R.id.OtrosDatosSemillaActualizar);
        DescripcionSemillaActualizar = findViewById(R.id.DescripcionSemillaActualizar);
        ComoConsumirSemillaActualizar = findViewById(R.id.ComoConsumirSemillaActualizar);
        ManejoSemillaActualizar = findViewById(R.id.ManejoSemillaActualizar);
        MesRecogidaSemillaActualizar = findViewById(R.id.MesRecogidaSemillaActualizar);
        MesRepartoSemillaActualizar = findViewById(R.id.MesRepartoSemillaActualizar);
        ViabilidadSemillaActualizar = findViewById(R.id.ViabilidadSemillaActualizar);
        GramosMinimosSemillaActualizar = findViewById(R.id.GramosMinimosSemillaActualizar);
        ProcedenciaSemillaActualizar = findViewById(R.id.ProcedenciaSemillaActualizar);
        IdSemillaActualizar = findViewById(R.id.idSemillaActualizar);
        VariedadSemillaActualizar = findViewById(R.id.VariedadSemillaActualizar);
        ImagenAgregarSemillaActualizar = findViewById(R.id.ImagenAgregarSemillaActualizar);
        ActualizarSemilla = findViewById(R.id.ActualizarSemilla);

        progressDialog = new ProgressDialog(ActualizarSemilla.this);

        databaseReference = FirebaseDatabase.getInstance().getReference("semillas");

        mStorageReference = FirebaseStorage.getInstance().getReference();

        Bundle intent = getIntent().getExtras();
        if ( intent != null) {
            //Recuperar los datos de la actividad anterior

            rSemilla = (Semilla) intent.getSerializable("SemillaEnviada");
            //Settear

            Picasso.get().load(rSemilla.getImagenSemilla()).into(ImagenAgregarSemillaActualizar);
            NombreCientificoSemillaActualizar.setText(rSemilla.getNombreCientificoSemilla());
            NombreVulgarSemillaActualizar.setText(rSemilla.getNombreVulgarSemilla());
            DisponibilidaddSemillaActualizar.setText(rSemilla.getDisponibilidad());
            OtrosDatosSemillaActualizar.setText(rSemilla.getOtrosDatosSemilla());
            DescripcionSemillaActualizar.setText(rSemilla.getDescripcionSemilla());
            ComoConsumirSemillaActualizar.setText(rSemilla.getConsumirSemilla());
            MesRepartoSemillaActualizar.setText(rSemilla.getMesRepartoSemilla());
            MesRecogidaSemillaActualizar.setText(rSemilla.getMesDevolucionSemilla());
            ViabilidadSemillaActualizar.setText(rSemilla.getViavilidad());
            GramosMinimosSemillaActualizar.setText(String.valueOf(rSemilla.getGramosMinimosSemilla()));
            ProcedenciaSemillaActualizar.setText(rSemilla.getProcedenciaSemilla());
            VariedadSemillaActualizar.setText(rSemilla.getVariedadSemilla());


        }

        ImagenAgregarSemillaActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                ActualizarImagenSemilla();
            }
        });

        ActualizarSemilla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActSemilla();
            }
        });

    }

    private void ActualizarImagenSemilla() {
        String [] opcion ={"Cambiar foto de semilla"};
        //crear alertdialog
        AlertDialog.Builder builder = new AlertDialog.Builder(ActualizarSemilla.this);
        //Asignamos un titulo
        builder.setTitle("Elegir una opción");
        builder.setItems(opcion, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (i == 0) {

                    //imagenSemilla = "imagenSemilla";
                    imagenSemilla = rSemilla.getImagenSemilla();
                    ElegirFoto();
                }

            }
        });

        builder.create().show();
    }

    private void ActSemilla() {
        progressDialog.setTitle("Actualizando");
        progressDialog.setMessage("Espere...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ActualizarSemillaBD();
    }

    //Elegir de donde procede la imagen
    private void ElegirFoto() {
        String [] opciones = {"Cámara", "Galería"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar imagen de: ");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                //Seleccionar de Camara
                if (i == 0) {
                    if (ContextCompat.checkSelfPermission(ActualizarSemilla.this, Manifest.permission.CAMERA) ==
                        PackageManager.PERMISSION_GRANTED) {
                        ElegirDeCamara();
                    } else {
                        SolicitudPermisoCamara.launch(Manifest.permission.CAMERA);
                        }

                } else if (i == 1) {
                    if (ContextCompat.checkSelfPermission(ActualizarSemilla.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED) {
                        ElegirDeGaleria();
                    } else {
                        SolicitudPermisoGaleria.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                }
            }
        });
        builder.create().show();
    }

    private void ElegirDeGaleria() {
        Intent GaleriaIntent = new Intent(Intent.ACTION_PICK);
        GaleriaIntent.setType("image/*");
        ObtenerImagengALERIa.launch(GaleriaIntent);
    }

    private void ElegirDeCamara() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Foto Temporal");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Descripción Temporal");
        imagenURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //Actividad para abrir la camara
        Intent camaraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camaraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imagenURI);
        ObtenerImagenCamara.launch(camaraIntent);

    }

    private ActivityResultLauncher<Intent> ObtenerImagenCamara = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK) {
                        ActualizarImagenEnBD(imagenURI);
                            progressDialog.setTitle("Procesando");
                            progressDialog.setMessage("La imagen se está cambiando, espere...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                        } else {
                            Toast.makeText(ActualizarSemilla.this, "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );


        private ActivityResultLauncher<Intent> ObtenerImagengALERIa = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            imagenURI = data.getData();
                            ActualizarImagenEnBD(imagenURI);
                            progressDialog.setTitle("Procesando");
                            progressDialog.setMessage("La imagen se está cambiando, espere...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                        } else {
                            Toast.makeText(ActualizarSemilla.this, "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        private ActivityResultLauncher<String> SolicitudPermisoCamara =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
                    if (isGranted) {
                        ElegirDeCamara();
                    } else {
                        Toast.makeText(ActualizarSemilla.this, "Permiso denegado", Toast.LENGTH_SHORT).show();
                    }
                });

        private ActivityResultLauncher<String> SolicitudPermisoGaleria =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
                    if (isGranted) {
                        ElegirDeGaleria();
                    } else {
                        Toast.makeText(ActualizarSemilla.this, "Permiso denegado", Toast.LENGTH_SHORT).show();
                    }
                });

    private void ActualizarImagenEnBD (Uri uri) {


        String  RutaArchivoYNombre = imagenSemilla.substring(80,111);

        StorageReference storageReference2 = mStorageReference.child(RutaArchivoYNombre);

      storageReference2.putFile(uri)




                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task <Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        Uri dowonloadUri = uriTask.getResult();

                        if (uriTask.isSuccessful()) {

                            rSemilla.setImagenSemilla(dowonloadUri.toString());
                     System.out.println(rSemilla.getImagenSemilla().toString());
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("semillas/"+rSemilla.getIdSemilla());

                            reference.child(String.valueOf(rSemilla.getIdSemilla())).setValue(rSemilla);
                            progressDialog.dismiss();
                            Toast.makeText(ActualizarSemilla.this, "Actualización hecha", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ActualizarSemilla.this, ListarSemillasBanco.class));
                            finish();

                        } else {
                            Toast.makeText(ActualizarSemilla.this, "Ha habido algún problema", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ActualizarSemilla.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void ActualizarSemillaBD() {

        rSemilla.setNombreCientificoSemilla(NombreCientificoSemillaActualizar.getText().toString());
        rSemilla.setNombreVulgarSemilla(NombreVulgarSemillaActualizar.getText().toString());
        rSemilla.setDisponibilidad(DisponibilidaddSemillaActualizar.getText().toString());
        rSemilla.setOtrosDatosSemilla(OtrosDatosSemillaActualizar.getText().toString());
        rSemilla.setDescripcionSemilla(DescripcionSemillaActualizar.getText().toString());
        rSemilla.setConsumirSemilla(ComoConsumirSemillaActualizar.getText().toString());
        rSemilla.setMesRepartoSemilla(MesRepartoSemillaActualizar.getText().toString());
        rSemilla.setMesDevolucionSemilla(MesRecogidaSemillaActualizar.getText().toString());
        rSemilla.setViavilidad(ViabilidadSemillaActualizar.getText().toString());
        rSemilla.setGramosMinimosSemilla(Double.parseDouble (GramosMinimosSemillaActualizar.getText().toString()));
        rSemilla.setProcedenciaSemilla(ProcedenciaSemillaActualizar.getText().toString());
        rSemilla.setVariedadSemilla(VariedadSemillaActualizar.getText().toString());

        //Se añade a la base de datos

        databaseReference.child(String.valueOf(rSemilla.getIdSemilla())).setValue(rSemilla);
        progressDialog.dismiss();
        Toast.makeText(ActualizarSemilla.this, "Actualización hecha", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ActualizarSemilla.this, ListarSemillasBanco.class));
        finish();


    }


}