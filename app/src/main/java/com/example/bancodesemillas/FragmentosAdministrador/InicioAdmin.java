package com.example.bancodesemillas.FragmentosAdministrador;

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
import com.example.bancodesemillas.Login;
import com.example.bancodesemillas.R;


public class InicioAdmin extends Fragment {

    Button btnGuardas, btnRepartos, btnSemillas, btnGuardianes, btnAcercaDe, btnSalir;;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inicio_admin, container, false);

        btnGuardas = view.findViewById(R.id.Btn_Guardas);
        btnRepartos = view.findViewById(R.id.Btn_Repartos);
        btnSemillas = view.findViewById(R.id.Btn_Semillas);
        btnGuardianes = view.findViewById(R.id.Btn_Guardianes);
        btnAcercaDe = view.findViewById(R.id.AcercaDe);
        btnSalir = view.findViewById(R.id.CerrarSesion);

        btnGuardas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), GuardasAdmin.class));
            }
        });

        btnSemillas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ListarSemillasBanco.class));
            }
        });

        btnGuardianes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ListarGuardianesAdmin.class));
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
                startActivity(new Intent(getActivity(),AcercaDe.class));
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