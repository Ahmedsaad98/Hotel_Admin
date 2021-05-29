package com.ahmedalraziki.g_admin_final.reservationPackage;

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
import android.widget.TextView;

import com.ahmedalraziki.g_admin_final.Classes.Reservation;
import com.ahmedalraziki.g_admin_final.R;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ReservationDetails extends Fragment {

    Reservation thisRes;

    private static final String ARG_PARAM1 = "rdfId";
    private static final String ARG_PARAM2 = "rdfName";
    private static final String ARG_PARAM3 = "rdfPhone";
    private static final String ARG_PARAM4 = "rdfEmail";
    private static final String ARG_PARAM5 = "rdfDci";
    private static final String ARG_PARAM6 = "rdfDco";
    private static final String ARG_PARAM7 = "rdfNob";
    private static final String ARG_PARAM8 = "rdfTotal";
    private static final String ARG_PARAM9 = "roomNo";
    private static final String ARG_PARAM10 = "staffCki";
    private static final String ARG_PARAM11 = "staffCko";
    private static final String ARG_PARAM12 = "param1";

    TextView txtId;
    TextView txtName;
    TextView txtPhone;
    TextView txtEmail;
    TextView txtDci;
    TextView txtDco;
    TextView txtNob;
    TextView txtTot;
    TextView txtRooNo;
    TextView txtStaffCi;
    TextView txtStaffCo;

    Button back;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;
    private String mParam4;
    private String mParam5;
    private String mParam6;
    private String mParam7;
    private String mParam8;
    private String mParam9;
    private String mParam10;
    private String mParam11;
    private int    mParam12;

    public ReservationDetails() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
            mParam4 = getArguments().getString(ARG_PARAM4);
            mParam5 = getArguments().getString(ARG_PARAM5);
            mParam6 = getArguments().getString(ARG_PARAM6);
            mParam7 = getArguments().getString(ARG_PARAM7);
            mParam8 = getArguments().getString(ARG_PARAM8);
            mParam9 = getArguments().getString(ARG_PARAM9);
            mParam10 = getArguments().getString(ARG_PARAM10);
            mParam11 = getArguments().getString(ARG_PARAM11);
            mParam12 = getArguments().getInt(ARG_PARAM12);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thisRes = new Reservation
                (mParam1, mParam2, mParam3, mParam4, mParam5, mParam6, mParam7, mParam8);
        thisRes.setRoomNo(mParam9);
        thisRes.setStaffCki(mParam10);
        thisRes.setStaffCko(mParam11);
        return inflater.inflate(R.layout.fragment_reservation_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        Assigner(view);
        Binder();
        backBtn();
        super.onViewCreated(view, savedInstanceState);
    }

    private void Assigner(View view){
        txtId      = view.findViewById(R.id.frde_txtId);
        txtName    = view.findViewById(R.id.frde_txtNa);
        txtPhone   = view.findViewById(R.id.frde_txtPh);
        txtEmail   = view.findViewById(R.id.frde_txtEm);
        txtDci     = view.findViewById(R.id.frde_txtDi);
        txtDco     = view.findViewById(R.id.frde_txtDo);
        txtNob     = view.findViewById(R.id.frde_txtBn);
        txtTot     = view.findViewById(R.id.frde_txtTo);
        txtRooNo   = view.findViewById(R.id.frde_txtRn);
        txtStaffCi = view.findViewById(R.id.frde_txtSi);
        txtStaffCo = view.findViewById(R.id.frde_txtSo);
        back       = view.findViewById(R.id.frde_btnBack);
    }

    private void Binder(){
        txtId.setText(thisRes.getId());
        txtName.setText(thisRes.getName());
        txtPhone.setText(thisRes.getPhone());
        txtEmail.setText(thisRes.getEmail());
        txtDci.setText(thisRes.getDci());
        txtDco.setText(thisRes.getDco());
        txtNob.setText(thisRes.getNob());
        txtTot.setText(thisRes.getTotal());
        txtRooNo.setText(thisRes.getRoomNo());
        txtStaffCi.setText(thisRes.getStaffCki());
        txtStaffCo.setText(thisRes.getStaffCko());
    }

    private void backBtn(){
        back.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(ARG_PARAM12, mParam12);
            Fragment fragment = new ReservationList();
            fragment.setArguments(bundle);
            FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.reservations, fragment).commit();
        });
    }
}