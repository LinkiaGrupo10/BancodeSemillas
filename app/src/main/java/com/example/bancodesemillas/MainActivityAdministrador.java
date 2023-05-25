package com.example.bancodesemillas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.bancodesemillas.FragmentosAdministrador.InicioAdmin;
import com.example.bancodesemillas.FragmentosAdministrador.ListarGuardian;
import com.example.bancodesemillas.FragmentosAdministrador.PerfilGuardian;
import com.example.bancodesemillas.FragmentosAdministrador.RegistrarGuardian;
import com.example.bancodesemillas.FragmentosAdministrador.Semillas.AgregarSemilla;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivityAdministrador extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_administrador);

        Toolbar toolbar = findViewById(R.id.toolbarA);
        setSupportActionBar(toolbar);


        drawerLayout = findViewById(R.id.drawer_layout_A);

        NavigationView navigationView = findViewById(R.id.nav_viewA);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.navigation_drawen_open, R.string.navigation_drawen_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        //Fragmento por defecto
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerA,
                    new InicioAdmin()).commit();
            navigationView.setCheckedItem(R.id.InicioAdmin);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.InicioAdmin:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerA,
                        new InicioAdmin()).commit();
                break;
            case R.id.PerfilAdmin:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerA,
                        new PerfilGuardian()).commit();
                break;
            case R.id.RegistrarGuardian:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerA,
                        new RegistrarGuardian()).commit();
                break;
            case R.id.RegistrarSemilla:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerA,
                        new AgregarSemilla()).commit();
                break;
            case R.id.ListarGuardianes:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerA,
                        new ListarGuardian()).commit();
                break;
            case R.id.ListarSemillas:
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerA,
                  //      new ListarSemillasBanco()).commit()
                break;
            case R.id.Salir:
                CerrarSesion();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void CerrarSesion() {
        finish();
        startActivity(new Intent(MainActivityAdministrador.this, Login.class));
        Toast.makeText(this, "Cerraste sesión", Toast.LENGTH_SHORT).show();
    }
}