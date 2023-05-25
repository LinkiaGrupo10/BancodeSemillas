package com.example.bancodesemillas.FragmentosAdministrador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bancodesemillas.Login;
import com.example.bancodesemillas.MainActivityAdministrador;
import com.example.bancodesemillas.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Cambio_Pass extends AppCompatActivity {


    EditText ActualPassET, NuevoPassET;
    Button CambiarPassBtn, IrInicioBtn;

    DatabaseReference GUARDIANES;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambio_pass);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Cambiar Contraseña");


        ActualPassET = findViewById(R.id.ActualPassET);
        NuevoPassET = findViewById(R.id.NuevoPassET);
        CambiarPassBtn = findViewById(R.id.CambiarPassBtn);
        IrInicioBtn = findViewById(R.id.IrInicioBtn);

        GUARDIANES = FirebaseDatabase.getInstance().getReference("GUARDIANES");
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        progressDialog = new ProgressDialog(Cambio_Pass.this);

        //Consulta de contraseña en la BBDD
        Query query = GUARDIANES.orderByChild("correo").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()) {
                    //Obtener la pass
                    String pass = ""+ds.child("password").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        CambiarPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String actualPass = ActualPassET.getText().toString().trim();
                String nuevoPass = NuevoPassET.getText().toString().trim();

                //Condiciones
                if(TextUtils.isEmpty(actualPass)) {
                    Toast.makeText(Cambio_Pass.this, "El Campo contraseña actual está vacío", Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(nuevoPass)) {
                    Toast.makeText(Cambio_Pass.this, "El Campo nueva contraseña está vacío", Toast.LENGTH_SHORT).show();
                }
                if (!actualPass.equals("") && !nuevoPass.equals("") && nuevoPass.length()>=6) {
                    Cambio_Pass(actualPass, nuevoPass);
                } else {
                    NuevoPassET.setError("La contraseña debe tener mínimo 6 caracteres");
                    NuevoPassET.setFocusable(true);
                }
            }
        });

        IrInicioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Cambio_Pass.this, MainActivityAdministrador.class));
            }
        });


    }

    private void Cambio_Pass(String actualPass, String nuevoPass) {
        progressDialog.show();
        progressDialog.setTitle("Actualizando");
        progressDialog.setMessage("Espere por favor");

        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), actualPass);
        user.reauthenticate(authCredential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        user.updatePassword(nuevoPass)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        progressDialog.dismiss();
                                        String passNuevo = NuevoPassET.getText().toString().trim();
                                        HashMap<String, Object> resultado = new HashMap<>();
                                        resultado.put("PASSWORD", passNuevo);

                                        //Actualizamos contraseña en BBDD
                                        GUARDIANES.child(user.getUid()).updateChildren(resultado)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(Cambio_Pass.this, "Contraseña actualizada", Toast.LENGTH_SHORT).show();
                                                        //Cerrar Sesion
                                                        firebaseAuth.signOut();
                                                        startActivity(new Intent(Cambio_Pass.this, Login.class));
                                                        finish();


                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(Cambio_Pass.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        progressDialog.dismiss();
                                                    }
                                                });


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Cambio_Pass.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();

                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Cambio_Pass.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}