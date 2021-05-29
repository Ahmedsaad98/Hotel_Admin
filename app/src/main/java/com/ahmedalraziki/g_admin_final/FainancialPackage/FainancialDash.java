package com.ahmedalraziki.g_admin_final.FainancialPackage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ahmedalraziki.g_admin_final.R;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class FainancialDash extends Fragment {

    Button btnFiAna;
    Button btnInAdd;
    Button btnOuAdd;

    public FainancialDash() { }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_fainancial_dash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Assigner(view);
        Clicker();
    }

    private void Assigner(View view){
        btnFiAna = view.findViewById(R.id.ffd_btnInAn);
        btnOuAdd = view.findViewById(R.id.ffd_btnOuAn);
        btnInAdd = view.findViewById(R.id.ffd_btnInAd);
    }

    private void Clicker(){
        btnFiAna.setOnClickListener(v -> {
            Fragment fragment = new FinancialAnalysis();
            FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fainancial, fragment).commit();
        });

        btnInAdd.setOnClickListener(v -> {
            Fragment fragment = new FainancialAddIncome();
            FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fainancial, fragment).commit();
        });

        btnOuAdd.setOnClickListener(v ->{
            Fragment fragment = new FainancialAddOutlay();
            FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fainancial, fragment).commit();
        });
    }


}