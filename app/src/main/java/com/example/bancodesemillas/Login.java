package com.example.bancodesemillas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import android.app.ProgressDialog;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Login extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example..EXTRA_MESSAGE";

    FirebaseDatabase firebaseDatabase;
    // Se crea referencia a la base de datos
    DatabaseReference databaseReference;

    Button btn_login;
    EditText emailUser, passwordUser;

    // Se crea la FirebaseAuthentication
    FirebaseAuth mfireAuth;

    ProgressDialog progressDialog;

    // Validar los datos
    String correo = "" , password = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Se inicializa el firebase

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Login");
        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        // Se inicializa el fireAuth y se recoge la instancia.
        mfireAuth = FirebaseAuth.getInstance();

        emailUser = findViewById(R.id.CorreoLogin);
        passwordUser = findViewById(R.id.PassLogin);
        btn_login = findViewById(R.id.Btn_Logeo);


        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setTitle("Espere por favor");
        progressDialog.setCanceledOnTouchOutside(false);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidarDatos();
            }
        });





    }


    private void ValidarDatos() {

        correo = emailUser.getText().toString();
        password = passwordUser.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            Toast.makeText(this, "Correo inválido", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Ingrese contraseña", Toast.LENGTH_SHORT).show();
        }
        else {
            LoginDeUsuario();
        }

    }

    private void LoginDeUsuario() {
        progressDialog.setMessage("Iniciando sesión ...");
        progressDialog.show();
        mfireAuth.signInWithEmailAndPassword(correo,password)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser user = mfireAuth.getCurrentUser();
                            if (emailUser.getText().toString().equalsIgnoreCase("admin@linkia.com") ){

                                //Si el admin incia sesion lo lleva a su activity

                                Intent intent = new Intent(Login.this , MainActivityAdministrador.class);
                                intent.putExtra(EXTRA_MESSAGE,correo+" "+password);
                                startActivity(intent);


                            }else{

                                //si incia sesion guardian, se recoge su UID ,
                                // se pasa al extra message para enviarselo a la activity que lo va usar para recuperar sus datos
                                String id = mfireAuth.getCurrentUser().getUid();
                                Intent intent = new Intent(Login.this , MainActivityGuardian.class);
                                intent.putExtra(EXTRA_MESSAGE, id);
                                startActivity(intent);



                            }

                            Toast.makeText(Login.this, "Bienvenido(a): "+user.getEmail(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, "Verifique si el correo y contraseña son los correctos", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(Login.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}




