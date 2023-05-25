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
import com.example.bancodesemillas.FragmentosGuardian.InicioGuardian;
import com.example.bancodesemillas.FragmentosGuardian.ListarSemillasGuardian;
import com.example.bancodesemillas.FragmentosGuardian.PerfilGuardianG;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivityGuardian extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_guardian);

        Toolbar toolbar = findViewById(R.id.toolbarG);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout_G);

        NavigationView navigationView = findViewById(R.id.nav_viewG);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.navigation_drawen_open, R.string.navigation_drawen_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        //Fragmento por defecto
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerG,
                    new InicioGuardian()).commit();
            navigationView.setCheckedItem(R.id.InicioGuardian);
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.InicioGuardian:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerG,
                        new InicioGuardian()).commit();
                break;
            case R.id.PerfilGuardian:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerG,
                        new PerfilGuardianG()).commit();
                break;
            case R.id.ListarSemillasGuardian:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerG,
                        new ListarSemillasGuardian()).commit();
                break;
            case R.id.ListarSemillasBanco:
               // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerG,
                 //       new ListarSemillasBanco()).commit();
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

        startActivity(new Intent(MainActivityGuardian.this, Login.class));

        Toast.makeText(this, "Cerraste sesión", Toast.LENGTH_SHORT).show();
    }
}