package com.example.bancodesemillas.FragmentosAdministrador;

import static androidx.browser.customtabs.CustomTabsClient.getPackageName;

import static com.example.bancodesemillas.R.id.idSemilla;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
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
import com.example.bancodesemillas.Modelo.Guardian;
import com.example.bancodesemillas.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import java.util.HashMap;
import java.util.regex.Pattern;

public class RegistrarGuardian extends Fragment {

    EditText Correo, Password, Nombre, Telefono, Direccion, Municipio, Asamblea;
    Button Registrar;

    ImageView Imagen;
    String RutaDeAlmacenamiento = "Guardian";

    String RutaDeBaseDeDatos = "GUARDIANES";

    StorageReference mStorageReference;

    FirebaseAuth auth;
    ProgressDialog progressDialog;

    Uri uriImage;

    DatabaseReference databaseReference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registrar_guardian, container, false);

        ActionBar actionBar = getActivity().getActionBar();

        Correo = view.findViewById(R.id.Correo);
        Password = view.findViewById(R.id.Contraseña);
        Nombre = view.findViewById(R.id.Nombre);
        Telefono = view.findViewById(R.id.Telefono);
        Direccion = view.findViewById(R.id.Direccion);
        Municipio = view.findViewById(R.id.Municipio);
        Asamblea = view.findViewById(R.id.Asamblea);
        Imagen = view.findViewById(R.id.Imagen);

        Registrar = view.findViewById(R.id.Registrar);

         auth = FirebaseAuth.getInstance();






         uriImage = getUriToResource(getContext(),R.drawable.registro);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(RutaDeBaseDeDatos);

        progressDialog = new ProgressDialog(getActivity());


        Registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubirImagen();
            }
        });

        return view;

    }




    private void SubirImagen() {
        if (uriImage != null) {
            progressDialog.setTitle("Espero");
            progressDialog.setMessage("Registrando...");
            progressDialog.show();
            progressDialog.setCancelable(false);
            StorageReference storageReference2 = mStorageReference.child("FOTO_PERFIL_GUARDIANES").child(RutaDeAlmacenamiento
                    + System.currentTimeMillis());
            storageReference2.putFile(uriImage)

                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                              @Override
                                              public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                  //Obtenemos la Uri de la imagen que acabamos de subir
                                                  Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                                  while (!uriTask.isSuccessful()) ;

                                                  Uri downloadURI = uriTask.getResult();
                                                  String correo = Correo.getText().toString();
                                                  String password = Password.getText().toString();
                                                  String nombre = Nombre.getText().toString();
                                                  String telefono = Telefono.getText().toString();
                                                  String direccion = Direccion.getText().toString();
                                                  String municipio = Municipio.getText().toString();
                                                  String asamblea = Asamblea.getText().toString();
                                                  String uid = nombre+1;


                                                  if (Telefono.equals("") || Direccion.equals("") || Municipio.equals("")
                                                          || Asamblea.equals("") || uriImage.equals("")) {
                                                      telefono = " ";
                                                      direccion = " ";
                                                      municipio = " ";
                                                      asamblea = "false";
                                                  }

                                                  if (correo.equals("") || password.equals("") || nombre.equals("")) {
                                                      Toast.makeText(getActivity(), "Correo, Password y Nombre son Obligatorios", Toast.LENGTH_SHORT).show();

                                                  } else {


                                                      //Validacion del correo

                                                      if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                                                          Correo.setError("Correo No Válido");
                                                          Correo.setFocusable(true);
                                                      } else if (password.length() < 6) {
                                                          Password.setError("Contraseña mínimo 6 caracteres");
                                                          Password.setFocusable(true);
                                                      } else {

                                                          Guardian guardian = new Guardian(correo, nombre, telefono, direccion, asamblea, downloadURI.toString(), uid, municipio, password);
                                                          DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GUARDIANES/"+uid);

                                                          reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                              @Override
                                                              public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                  if (snapshot.exists()) {
                                                                      Toast.makeText(getActivity(), "El email de este guardian ya está registrado", Toast.LENGTH_SHORT).show();
                                                                      startActivity(new Intent(getActivity(), MainActivityAdministrador.class));
                                                                      getActivity().finish();
                                                                  } else {


                                                                      RegistroGuardianes(guardian);
                                                                      progressDialog.dismiss();
                                                                      Toast.makeText(getActivity(), "Guardian Registrado", Toast.LENGTH_SHORT).show();

                                                                      startActivity(new Intent(getActivity(), MainActivityAdministrador.class));
                                                                     // getActivity().finish();
                                                                  }
                                                              }


                                                              @Override
                                                              public void onCancelled(@NonNull DatabaseError error) {
                                                                  progressDialog.dismiss();
                                                                  Toast.makeText(getActivity(), "Cancelado", Toast.LENGTH_SHORT).show();
                                                              }
                                                          });

                                                      }

                                                      progressDialog.dismiss();



                                                  }
                                                  progressDialog.dismiss();
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
        }
    }




                                              //Obtenemos extension archivo de imagen
                                                  private String ObtenerExtensionDelArchivo(Uri uri) {
                                                  ContentResolver contentResolver = getActivity().getContentResolver();
                                                  MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                                                  return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
                                              }


                                              //Método para registrar guardianes

     private void RegistroGuardianes(Guardian guardian) {

                                                  progressDialog.show();
                                                  auth.createUserWithEmailAndPassword(guardian.getCORREO(), guardian.getPASSWORD())
                                                          .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                              @Override
                                                              public void onComplete(@NonNull Task<AuthResult> task) {
                                                                  //Si el guardian fue creado correctamente
                                                                  if (task.isSuccessful()) {
                                                                      progressDialog.dismiss();
                                                                      FirebaseUser user = auth.getCurrentUser();
                                                                      assert user != null; //afirmar que el guardian no es nulo

                                                                      guardian.setUID(user.getUid());

                                                                      //Inicializar FirebaseDataBase
                                                                      FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                                      DatabaseReference reference = database.getReference();
                                                                      reference.child("GUARDIANES").child(user.getUid()).setValue(guardian);
                                                                      Toast.makeText(getActivity(), "Registro Completado", Toast.LENGTH_SHORT).show();

                                                                  } else {
                                                                      progressDialog.dismiss();
                                                                      Toast.makeText(getActivity(), "Error en el Registro", Toast.LENGTH_SHORT).show();

                                                                  }
                                                              }
                                                          })
                                                          .addOnFailureListener(new OnFailureListener() {
                                                              @Override
                                                              public void onFailure(@NonNull Exception e) {
                                                                  Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                              }
                                                          });
                                              }


    public static final Uri getUriToResource(@NonNull Context context, @AnyRes int resId) throws Resources.NotFoundException {

        Resources res = context.getResources();

        Uri resUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + res.getResourcePackageName(resId)
                + '/' + res.getResourceTypeName(resId)
                + '/' + res.getResourceEntryName(resId));

        return resUri;
    }
}


