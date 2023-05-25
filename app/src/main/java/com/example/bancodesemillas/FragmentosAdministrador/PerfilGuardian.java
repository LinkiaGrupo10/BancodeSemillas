package com.example.bancodesemillas.FragmentosAdministrador;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.Manifest.permission;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bancodesemillas.MainActivityAdministrador;
import com.example.bancodesemillas.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;


public class PerfilGuardian extends Fragment {


    //FIREBASE
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    DatabaseReference GUARDIANES;

    StorageReference storageReference;
    String RutaAlmacenamiento = "FOTO_PERFIL_GUARDIANES/*";


    private Uri imagenURI;
    private String imagenPerfil;
    private ProgressDialog progressDialog;



    //VISTAS
    ImageView FotoPerfilGuardian;
    TextView NombreGardian, CorreoGuardian, TelefonoGuardian, DireccionGuardian, MunicipioGuardian, AsambleaGaurdian;
    Button ActualizarPass, ActualizarDatos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil_guardian, container, false);

        FotoPerfilGuardian = view.findViewById(R.id.FotoPerfilGuardian);
        NombreGardian = view.findViewById(R.id.NombreGardian);
        CorreoGuardian = view.findViewById(R.id.CorreoGuardian);
        TelefonoGuardian = view.findViewById(R.id.TelefonoGuardian);
        DireccionGuardian = view.findViewById(R.id.DireccionGuardian);
        MunicipioGuardian = view.findViewById(R.id.MunicipioGuardian);
        AsambleaGaurdian = view.findViewById(R.id.AsambleaGaurdian);
        ActualizarPass = view.findViewById(R.id.ActualizarPass);
        ActualizarDatos = view.findViewById(R.id.ActualizarDatos);

        //Inicializamos Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();


        progressDialog = new ProgressDialog(getActivity());

        GUARDIANES = FirebaseDatabase.getInstance().getReference("GUARDIANES");

        GUARDIANES.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    //Obtenemos los datos
                    String UID = "" + snapshot.child("uid").getValue();
                    String nombre = "" + snapshot.child("nombre").getValue();
                    String correo = "" + snapshot.child("correo").getValue();
                    String pass = "" + snapshot.child("password").getValue();
                    String telefono = "" + snapshot.child("telefono").getValue();
                    String direccion = "" + snapshot.child("direccion").getValue();
                    String municipio = "" + snapshot.child("municipio").getValue();
                    String imagen = "" + snapshot.child("imagen").getValue();

                    String asamblea = "" + snapshot.child("asamblea").getValue();


                    NombreGardian.setText(nombre);
                    CorreoGuardian.setText(correo);
                    TelefonoGuardian.setText(telefono);
                    DireccionGuardian.setText(direccion);
                    MunicipioGuardian.setText(municipio);
                    if(asamblea.equals("true")) {
                        AsambleaGaurdian.setText("Sí");
                    } else {
                        AsambleaGaurdian.setText("No");
                    }

                    try {
                        //Si Existe la imagen
                        Picasso.get().load(imagen).placeholder(R.drawable.perfil).into(FotoPerfilGuardian);

                    } catch (Exception e) {
                        //No existe imagen en firebase
                        Picasso.get().load(R.drawable.perfil).into(FotoPerfilGuardian);


                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Nos dirige a la activity Cambio_pass
        ActualizarPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Cambio_Pass.class));
                getActivity().finish();
            }
        });


        ActualizarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditarDatos();
            }
        });


        FotoPerfilGuardian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CambiarImagenPerfilGuardian();
            }
        });


        return view;
    }

    private void EditarDatos() {
        String [] opciones = {"Editar Nombre", "Editar Telefono", "Editar Direccion", "Editar Municipio", "Editar Asamblea"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Elegir opción; ");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (i == 0) {
                    //Editar Nombre
                    EditarNombre();
                } else if (i == 1) {
                    //Editar Telefono
                    EditarTelefono();
                } else if (i == 2) {
                    //Editar Direccion
                    EditarDireccion();
                }if (i == 3) {
                    //Editar Municipio
                    EditarMunicipio();
                } else if (i == 4) {
                    //Editar Asamblea
                    EditarAsamblea();
                }
            }
        });

        builder.create().show();
    }

    private void EditarAsamblea() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Actualizar información:");
        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(getActivity());
        linearLayoutCompat.setOrientation(LinearLayoutCompat.VERTICAL);
        linearLayoutCompat.setPadding(10, 10, 10, 10);
        EditText editText = new EditText(getActivity());
        editText.setHint("Ingrese nuevo dato...");
        editText.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
        linearLayoutCompat.addView(editText);
        builder.setView(linearLayoutCompat);
        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                String nuevoDato = editText.getText().toString().trim();
                if (!nuevoDato.equals("")) {
                    HashMap<String, Object> resultado = new HashMap<>();
                    resultado.put("asamblea", nuevoDato);
                    GUARDIANES.child(user.getUid()).updateChildren(resultado)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getActivity(), "Dato actualizado", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(getActivity(), "Campo vacío", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
            }
        });

        builder.create().show();
    }

    private void EditarMunicipio() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Actualizar información:");
        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(getActivity());
        linearLayoutCompat.setOrientation(LinearLayoutCompat.VERTICAL);
        linearLayoutCompat.setPadding(10, 10, 10, 10);
        EditText editText = new EditText(getActivity());
        editText.setHint("Ingrese nuevo dato...");
        editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
        linearLayoutCompat.addView(editText);
        builder.setView(linearLayoutCompat);
        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                String nuevoDato = editText.getText().toString().trim();
                if (!nuevoDato.equals("")) {
                    HashMap<String, Object> resultado = new HashMap<>();
                    resultado.put("municipio", nuevoDato);
                    GUARDIANES.child(user.getUid()).updateChildren(resultado)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getActivity(), "Dato actualizado", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(getActivity(), "Campo vacío", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
            }
        });

        builder.create().show();
    }

    private void EditarDireccion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Actualizar información:");
        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(getActivity());
        linearLayoutCompat.setOrientation(LinearLayoutCompat.VERTICAL);
        linearLayoutCompat.setPadding(10, 10, 10, 10);
        EditText editText = new EditText(getActivity());
        editText.setHint("Ingrese nuevo dato...");
        editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
        linearLayoutCompat.addView(editText);
        builder.setView(linearLayoutCompat);
        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                String nuevoDato = editText.getText().toString().trim();
                if (!nuevoDato.equals("")) {
                    HashMap<String, Object> resultado = new HashMap<>();
                    resultado.put("direccion", nuevoDato);
                    GUARDIANES.child(user.getUid()).updateChildren(resultado)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getActivity(), "Dato actualizado", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(getActivity(), "Campo vacío", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
            }
        });

        builder.create().show();
    }

    private void EditarTelefono() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Actualizar información:");
        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(getActivity());
        linearLayoutCompat.setOrientation(LinearLayoutCompat.VERTICAL);
        linearLayoutCompat.setPadding(10, 10, 10, 10);
        EditText editText = new EditText(getActivity());
        editText.setHint("Ingrese nuevo dato...");
        editText.setInputType(InputType.TYPE_CLASS_PHONE);
        linearLayoutCompat.addView(editText);
        builder.setView(linearLayoutCompat);
        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                String nuevoDato = editText.getText().toString().trim();
                if (!nuevoDato.equals("")) {
                    HashMap<String, Object> resultado = new HashMap<>();
                    resultado.put("telefono", nuevoDato);
                    GUARDIANES.child(user.getUid()).updateChildren(resultado)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getActivity(), "Dato actualizado", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(getActivity(), "Campo vacío", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
            }
        });

        builder.create().show();
    }

    private void EditarNombre() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Actualizar información:");
        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(getActivity());
        linearLayoutCompat.setOrientation(LinearLayoutCompat.VERTICAL);
        linearLayoutCompat.setPadding(10, 10, 10, 10);
        EditText editText = new EditText(getActivity());
        editText.setHint("Ingrese nuevo dato...");
        editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
        linearLayoutCompat.addView(editText);
        builder.setView(linearLayoutCompat);
        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                String nuevoDato = editText.getText().toString().trim();
                if (!nuevoDato.equals("")) {
                    HashMap<String, Object> resultado = new HashMap<>();
                    resultado.put("nombre", nuevoDato);
                    GUARDIANES.child(user.getUid()).updateChildren(resultado)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getActivity(), "Dato actualizado", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(getActivity(), "Campo vacío", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
            }
        });

        builder.create().show();
    }

    private void CambiarImagenPerfilGuardian() {
        String [] opcion ={"Cambiar foto de perfil"};
        //crear alertdialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //Asignamos un titulo
        builder.setTitle("Elegir una opción");
        builder.setItems(opcion, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (i == 0) {
                    imagenPerfil = "imagen";
                    ElegirFoto();
                }

            }
        });

        builder.create().show();
    }




    private boolean ComprobarPermisosCamara() {
        boolean resultado1 = ContextCompat.checkSelfPermission(getActivity(), permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);
        boolean resultado2 = ContextCompat.checkSelfPermission(getActivity(), permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return resultado1 && resultado2;

    }



    //Elegir de donde procede la imagen
    private void ElegirFoto() {
        String [] opciones = {"Cámara", "Galería"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Seleccionar imagen de: ");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                //Seleccionar de Camara
                if (i == 0) {
                    if (ContextCompat.checkSelfPermission(getActivity(), permission.CAMERA) ==
                            PackageManager.PERMISSION_GRANTED) {
                        ElegirDeCamara();
                    } else {
                        SolicitudPermisoCamara.launch(permission.CAMERA);
                    }

                } else if (i == 1) {
                    if (ContextCompat.checkSelfPermission(getActivity(), permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_GRANTED) {
                        ElegirDeGaleria();
                    } else {
                        SolicitudPermisoGaleria.launch(permission.WRITE_EXTERNAL_STORAGE);
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
        imagenURI = (requireActivity()).getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
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
                        Toast.makeText(getActivity(), "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private ActivityResultLauncher<String> SolicitudPermisoCamara =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
                if (isGranted) {
                    ElegirDeCamara();
                } else {
                    Toast.makeText(getActivity(), "Permiso denegado", Toast.LENGTH_SHORT).show();
                }
            });

    private ActivityResultLauncher<String> SolicitudPermisoGaleria =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
                if (isGranted) {
                    ElegirDeGaleria();
                } else {
                    Toast.makeText(getActivity(), "Permiso denegado", Toast.LENGTH_SHORT).show();
                }
            });



    private void ActualizarImagenEnBD (Uri uri) {
        String RutaArchivoYNombre = RutaAlmacenamiento + "" + imagenPerfil + "-" + user.getUid();
        StorageReference storageReference2 = storageReference.child(RutaArchivoYNombre);
        storageReference2.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task <Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        Uri dowonloadUri = uriTask.getResult();

                        if (uriTask.isSuccessful()) {
                            HashMap<String, Object> results = new HashMap<>();
                            results.put(imagenPerfil, dowonloadUri.toString());
                            GUARDIANES.child(user.getUid()).updateChildren(results)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            startActivity(new Intent(getActivity(), MainActivityAdministrador.class));
                                            getActivity().finish();
                                            Toast.makeText(getActivity(), "Imagen Actualizada", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(getActivity(), "Ha habido algún problema", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }



}