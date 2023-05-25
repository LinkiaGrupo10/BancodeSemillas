package com.example.bancodesemillas.FragmentosGuardian;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.bancodesemillas.Admin.GuardasAdmin.GuardasAdmin;
import com.example.bancodesemillas.Admin.GuardianesAdmin.ListarGuardianesAdmin;
import com.example.bancodesemillas.Admin.RepartosAdmin.RepartosAdmin;
import com.example.bancodesemillas.Admin.SemillasAdmin.ListarSemillasBanco;
import com.example.bancodesemillas.FragmentosAdministrador.AcercaDe;
import com.example.bancodesemillas.Login;
import com.example.bancodesemillas.R;


public class InicioGuardian extends Fragment {

    Button btnGuardas_Cerradas, btnRepartos, btnSemillas, btnGuardas_Abiertas, btnAcercaDe, btnSalir;;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inicio_guardian, container, false);

        btnGuardas_Cerradas = view.findViewById(R.id.Btn_Guardas_Cerradas);
        btnRepartos = view.findViewById(R.id.Btn_Repartos);
        btnSemillas = view.findViewById(R.id.Btn_Semillas);
        btnGuardas_Abiertas = view.findViewById(R.id.Btn_Guardas_Abiertas);
        btnAcercaDe = view.findViewById(R.id.AcercaDe);
        btnSalir = view.findViewById(R.id.CerrarSesion);

        btnGuardas_Cerradas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getActivity(), ListarGuardasCerradasGuardian.class));
            }
        });

        btnSemillas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ListarSemillasBanco.class));
            }
        });

        btnGuardas_Abiertas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getActivity(), ListarGuardasAbiertasGuardian.class));
            }
        });

        btnRepartos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RepartosAdmin.class));
            }
        });

        btnAcercaDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AcercaDe.class));
            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Login.class));
                Toast.makeText(getActivity(), "Cerraste sesión exitosamente", Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }
}