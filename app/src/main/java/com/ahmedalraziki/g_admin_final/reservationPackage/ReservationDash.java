package com.ahmedalraziki.g_admin_final.reservationPackage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ahmedalraziki.g_admin_final.R;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ReservationDash extends Fragment {

    Button btnNci;
    Button btnCi;
    Button btnCo;
    private ReservationViewModel mViewModel;


    public ReservationDash() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reservation_dash, container, false);

    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(ReservationViewModel.class);
        Assigner();

        Clicker();
    }

    private void Assigner(){
        btnNci = Objects.requireNonNull(getActivity()).findViewById(R.id.frd_btnNci);
        btnCi = Objects.requireNonNull(getActivity()).findViewById(R.id.frd_btnCi);
        btnCo = Objects.requireNonNull(getActivity()).findViewById(R.id.frd_btnCo);
    }

    private void Clicker(){
        btnNci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("param1", 1);
                Fragment fragment = new ReservationList();
                fragment.setArguments(bundle);
                FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.reservations, fragment).commit();
            }
        });

        btnCi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("param1", 2);
                Fragment fragment = new ReservationList();
                fragment.setArguments(bundle);
                FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.reservations, fragment).commit();
            }
        });

        btnCo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("param1", 3);
                Fragment fragment = new ReservationList();
                fragment.setArguments(bundle);
                FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.reservations, fragment).commit();
            }
        });
    }

}