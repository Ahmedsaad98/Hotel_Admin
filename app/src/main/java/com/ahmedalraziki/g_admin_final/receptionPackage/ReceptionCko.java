package com.ahmedalraziki.g_admin_final.receptionPackage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ahmedalraziki.g_admin_final.Classes.Reservation;
import com.ahmedalraziki.g_admin_final.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ReceptionCko extends Fragment {

    ConstraintLayout rootLayout;

    EditText txtResId;
    Button btnCheck;
    Button   btnScan;
    Button   btnBack;

    Reservation thisRes = null;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    SharedPreferences prefs = null ;

    private static final String ARG_PARAM1 = "qrData";
    private static final String ARG_PARAM2 = "fromFrag";

    private String qrData;
    private String fromFrag;

    public ReceptionCko() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            qrData = getArguments().getString(ARG_PARAM1);
            fromFrag = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reception_cko, container, false);
        rootLayout = rootView.findViewById(R.id.frecko_view);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefs = view.getContext().getSharedPreferences("prefs1", Context.MODE_PRIVATE);
        Assigner(view);
        Clicker();
        getQrData();
    }

    private void Assigner(View view){
        txtResId = view.findViewById(R.id.frecko_etResId);
        btnCheck = view.findViewById(R.id.frecko_btnCheck);
        btnScan  = view.findViewById(R.id.frecko_btnScan);
        btnBack  = view.findViewById(R.id.frecko_btnBack);
    }

    private void Clicker(){
        btnCheck.setOnClickListener(v -> {
            getRes(txtResId.getText().toString());
        });

        btnScan.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(ARG_PARAM2,"cko");
            Fragment fragment = new Reception_Scanner();
            fragment.setArguments(bundle);
            FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.reception, fragment).commit();
        });

        btnBack.setOnClickListener(v -> {
            Fragment fragment = new ReceptionDash();
            FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.reception, fragment).commit();
        });
    }

    private void getRes(String ResID){
        reference.child("reservations").child("ci").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot d1 : snapshot.getChildren()){
                    for (DataSnapshot d2 : d1.getChildren()){
                        if(Objects.equals(d2.getKey(), ResID)){
                            String id = "";
                            String name = "";
                            String phone = "";
                            String email = "";
                            String nob = "";
                            String dci = "";
                            String dco = "";
                            String total = "";
                            String roomNo = "";
                            String staffCki = "";
                            for (DataSnapshot d3 : d2.getChildren()){
                                if(Objects.requireNonNull(d3.getKey()).equals("id")){ id = Objects.requireNonNull(d3.getValue()).toString();}
                                if(Objects.requireNonNull(d3.getKey()).equals("name")){ name = Objects.requireNonNull(d3.getValue()).toString();}
                                if(Objects.requireNonNull(d3.getKey()).equals("phone")){ phone = Objects.requireNonNull(d3.getValue()).toString();}
                                if(Objects.requireNonNull(d3.getKey()).equals("email")){ email = Objects.requireNonNull(d3.getValue()).toString();}
                                if(Objects.requireNonNull(d3.getKey()).equals("nob")){ nob = Objects.requireNonNull(d3.getValue()).toString();}
                                if(Objects.requireNonNull(d3.getKey()).equals("dci")){ dci = Objects.requireNonNull(d3.getValue()).toString();}
                                if(Objects.requireNonNull(d3.getKey()).equals("dco")){ dco = Objects.requireNonNull(d3.getValue()).toString();}
                                if(Objects.requireNonNull(d3.getKey()).equals("total")){ total = Objects.requireNonNull(d3.getValue()).toString();}
                                if(Objects.requireNonNull(d3.getKey()).equals("roomNo")){ roomNo = Objects.requireNonNull(d3.getValue()).toString();}
                                if(Objects.requireNonNull(d3.getKey()).equals("staffCki")){ staffCki = Objects.requireNonNull(d3.getValue()).toString();}
                            }
                            Reservation tempRes = new Reservation(id, name, phone, email, dci, dco, nob,
                                    total);
                            tempRes.setRoomNo(roomNo);
                            tempRes.setStaffCki(staffCki);
                            setThisRes(tempRes);
                        }
                    }
                    mfCItCO();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void setThisRes(Reservation res){
        thisRes = res;
    }

    private void mfCItCO(){
        // Move From NCI To CI
        String staffCko = prefs.getString("uid","error");
        if (staffCko.equals("error")){
            Snackbar.make(rootLayout, "Error Signing In :(", Snackbar.LENGTH_LONG).show();
        }
        else if(thisRes!= null){
            DatabaseReference thisResRefOld = reference.child("reservations").child("ci")
                    .child(thisRes.getPhone()).child(thisRes.getId());

            DatabaseReference thisResRefNew = reference.child("reservations").child("co")
                    .child(thisRes.getPhone()).child(thisRes.getId());

            thisResRefOld.removeValue();
            thisRes.setStaffCko(staffCko);

            thisResRefNew.setValue(thisRes);

            freeTheRoom(thisRes.getRoomNo());
            Snackbar.make(rootLayout, "Done !", Snackbar.LENGTH_LONG).show();
        }
    }
    private void getQrData(){
        if (qrData != null && prefs != null){
            getRes(qrData);
        }
    }

    private void freeTheRoom(String rn){

        String floor = "";
        int room  = Integer.parseInt(rn);

        if (room < 110){
            floor = "floor1";
        } else if (room < 210){
            floor = "floor2";
        } else if (room < 310){
            floor = "floor3";
        } else if (room < 410){
            floor = "floor4";
        } else if (room < 510){
            floor = "floor5";
        }

        reference.child("rooms").child(floor).child(rn).setValue(1);
    }

}