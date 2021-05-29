package com.ahmedalraziki.g_admin_final.receptionPackage;

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
import com.ahmedalraziki.g_admin_final.reservationPackage.ReservationList;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ReceptionDash extends Fragment {

    Button btnRoomSrv;
    Button btnCkiC;
    Button btnCkoC;
    Button btnFRL;

    public ReceptionDash() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reception_dash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Assigner(view);
        Clicker();
    }

    //Assign Views IDs.
    private void Assigner(View view){
        btnRoomSrv = view.findViewById(R.id.frecd_btnRomSrv);
        btnCkiC = view.findViewById(R.id.frecd_btnCki);
        btnCkoC = view.findViewById(R.id.frecd_btnCko);
        btnFRL = view.findViewById(R.id.frecd_btnFreRoms);
    }

    //Set On Click Listeners.
    private void Clicker(){
        //Go Check-In a client.
        btnCkiC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ReceptionCki();
                FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.reception, fragment).commit();
            }
        });

        btnCkoC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ReceptionCko();
                FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.reception, fragment).commit();
            }
        });

    }

}