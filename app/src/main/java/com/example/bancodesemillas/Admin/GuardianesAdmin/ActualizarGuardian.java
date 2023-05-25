package com.example.bancodesemillas.Admin.GuardianesAdmin;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bancodesemillas.Admin.SemillasAdmin.ActualizarSemilla;
import com.example.bancodesemillas.Admin.SemillasAdmin.ListarSemillasBanco;
import com.example.bancodesemillas.Admin.SemillasAdmin.Semilla;
import com.example.bancodesemillas.Modelo.Guardian;
import com.example.bancodesemillas.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ActualizarGuardian extends AppCompatActivity {


    EditText NombreActualizar, CorreoActualizar, TelefonoActualizar,
            DireccionActualizar, AsambleaActualizar, MunicipioActualizar;


    ImageView ImagenGuardianActualizarse;

    Button ActualizarGuardian;

    Guardian guardian;


    private Uri imagenURI;

    String imagenGuardian;

    String RutaDeAlmacenamiento = "Guardian";


    StorageReference mStorageReference;

    DatabaseReference databaseReference;

    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_guardian);


        NombreActualizar = findViewById(R.id.etNombre);
        CorreoActualizar = findViewById(R.id.etCorreo);
        TelefonoActualizar = findViewById(R.id.etTelefono);
        DireccionActualizar = findViewById(R.id.etDireccion);
        AsambleaActualizar = findViewById(R.id.etAsamblea);
        MunicipioActualizar = findViewById(R.id.etMunicipio);
        ImagenGuardianActualizarse = findViewById(R.id.ImagenGuardianActualizar);

        ActualizarGuardian = findViewById(R.id.ActualizarGuardian);



        progressDialog = new ProgressDialog(ActualizarGuardian.this);

        databaseReference = FirebaseDatabase.getInstance().getReference("GUARDIANES");

        mStorageReference = FirebaseStorage.getInstance().getReference();

        Bundle intent = getIntent().getExtras();

        if ( intent != null) {
            //Recuperar los datos de la actividad anterior

            guardian = (Guardian) intent.getSerializable("GuardianEnviado");
            //Settear

            Picasso.get().load(guardian.getIMAGEN()).into(ImagenGuardianActualizarse);
            NombreActualizar.setText(guardian.getNOMBRE());
            CorreoActualizar.setText(guardian.getCORREO());
            TelefonoActualizar.setText(guardian.getTELEFONO());
            DireccionActualizar.setText(guardian.getDIRECCION());
            AsambleaActualizar.setText(guardian.getASAMBLEA());
            MunicipioActualizar.setText(guardian.getMUNICIPIO());




        }



        ImagenGuardianActualizarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                ActualizarImagenGuardian();
            }
        });



        ActualizarGuardian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActGuardian();
            }
        });

    }


    private void ActualizarImagenGuardian() {
        String [] opcion ={"Cambiar foto de guardian"};
        //crear alertdialog
        AlertDialog.Builder builder = new AlertDialog.Builder(ActualizarGuardian.this);
        //Asignamos un titulo
        builder.setTitle("Elegir una opción");
        builder.setItems(opcion, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (i == 0) {


                    imagenGuardian = guardian.getIMAGEN();
                    ElegirFoto();
                }

            }
        });

        builder.create().show();
    }


    private void ElegirFoto() {
        String [] opciones = {"Cámara", "Galería"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar imagen de: ");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                //Seleccionar de Camara
                if (i == 0) {
                    if (ContextCompat.checkSelfPermission(ActualizarGuardian.this, Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_GRANTED) {
                        ElegirDeCamara();
                    } else {
                        SolicitudPermisoCamara.launch(Manifest.permission.CAMERA);
                    }

                } else if (i == 1) {
                    if (ContextCompat.checkSelfPermission(ActualizarGuardian.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
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
        ObtenerImagenGaleria.launch(GaleriaIntent);
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
                        ActualizarImagenGuardianBD(imagenURI);
                        progressDialog.setTitle("Procesando");
                        progressDialog.setMessage("La imagen se está cambiando, espere...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                    } else {
                        Toast.makeText(ActualizarGuardian.this, "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );


    private ActivityResultLauncher<Intent> ObtenerImagenGaleria = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        imagenURI = data.getData();
                        ActualizarImagenGuardianBD(imagenURI);
                        progressDialog.setTitle("Procesando");
                        progressDialog.setMessage("La imagen se está cambiando, espere...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                    } else {
                        Toast.makeText(ActualizarGuardian.this, "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );


    private void ActualizarImagenGuardianBD (Uri uri) {


        String  RutaArchivoYNombre = imagenGuardian.substring(105,126);


        StorageReference storageReference2 = mStorageReference.child("FOTO_PERFIL_GUARDIANES").child(RutaArchivoYNombre);

        storageReference2.putFile(uri)

                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        Uri dowonloadUri = uriTask.getResult();

                        if (uriTask.isSuccessful()) {
//                            HashMap<String, Object> results = new HashMap<>();
//                            results.put(imagenSemilla, dowonloadUri.toString());
                            guardian.setIMAGEN(dowonloadUri.toString());
                            System.out.println(guardian.getIMAGEN().toString());
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GUARDIANES/"+guardian.getUID());

                            reference.child(String.valueOf(guardian.getUID())).setValue(guardian);
                            progressDialog.dismiss();
                            Toast.makeText(ActualizarGuardian.this, "Actualización hecha",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ActualizarGuardian.this, ListarGuardianesAdmin.class));
                            finish();

                        } else {
                            Toast.makeText(ActualizarGuardian.this, "Ha habido algún problema", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ActualizarGuardian.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void ActGuardian() {
        progressDialog.setTitle("Actualizando");
        progressDialog.setMessage("Espere...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ActualizarGuardianBD();
    }



    private void ActualizarGuardianBD() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        guardian.setNOMBRE(NombreActualizar.getText().toString());
        guardian.setCORREO(CorreoActualizar.getText().toString());
        guardian.setTELEFONO(TelefonoActualizar.getText().toString());
        guardian.setDIRECCION(DireccionActualizar.getText().toString());
        guardian.setASAMBLEA(AsambleaActualizar.getText().toString());
        guardian.setMUNICIPIO(MunicipioActualizar.getText().toString());


        //Se añade a la base de datos

        databaseReference.child(String.valueOf(guardian.getUID())).setValue(guardian);
        progressDialog.dismiss();
        Toast.makeText(ActualizarGuardian.this, "Actualización hecha", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ActualizarGuardian.this, ListarGuardianesAdmin.class));
        finish();


    }


    private ActivityResultLauncher<String> SolicitudPermisoCamara =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
                if (isGranted) {
                    ElegirDeCamara();
                } else {
                    Toast.makeText(ActualizarGuardian.this, "Permiso denegado", Toast.LENGTH_SHORT).show();
                }
            });

    private ActivityResultLauncher<String> SolicitudPermisoGaleria =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
                if (isGranted) {
                    ElegirDeGaleria();
                } else {
                    Toast.makeText(ActualizarGuardian.this, "Permiso denegado", Toast.LENGTH_SHORT).show();
                }
            });
}