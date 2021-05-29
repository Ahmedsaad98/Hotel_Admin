package com.ahmedalraziki.g_admin_final.reservationPackage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ahmedalraziki.g_admin_final.Classes.FListResAdapter;
import com.ahmedalraziki.g_admin_final.Classes.Reservation;
import com.ahmedalraziki.g_admin_final.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ReservationList extends Fragment implements FListResAdapter.OnItemListener{


    private static final String ARG_PARAM1 = "param1";
    private int lisType;
    RecyclerView recyclerView;
    List<Reservation> reservations = new ArrayList<>();
    RecyclerView.Adapter adapter;
    Button btd;

    DatabaseReference mainRef = FirebaseDatabase.getInstance().getReference();

    public ReservationList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lisType = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reservation_list, container, false);

        recyclerInit(view);
        ReaderSetter(view);
        backToDash(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    //Instantiating Setting for the recycler View .
    private void recyclerInit(View view){
        recyclerView = Objects.requireNonNull(view).findViewById(R.id.frl_recV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        reservations = new ArrayList<>();
    }

    //Setting The Reader
    private void ReaderSetter(View view){
        switch (lisType){
            case 1:
                DatabaseReference refNci = mainRef.child("reservations").child("nci");
                firebaseReader(refNci, view);
                break;

            case 2:
                DatabaseReference refCi = mainRef.child("reservations").child("ci");
                firebaseReader(refCi, view);
                break;

            case 3:
                DatabaseReference refCo = mainRef.child("reservations").child("co");
                firebaseReader(refCo, view);
                break;

        }
    }

    //Reading From firebase.
    private void firebaseReader(DatabaseReference ref, View view){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot d1: snapshot.getChildren()){
                    for(DataSnapshot d2: d1.getChildren()){
                        String id = "";
                        String name = "";
                        String phone = "";
                        String email = "";
                        String nob = "";
                        String dci = "";
                        String dco = "";
                        String total = "";
                        String roomNo = "";

                        for (DataSnapshot d3: d2.getChildren()){
                            if(Objects.requireNonNull(d3.getKey()).equals("id")){ id = Objects.requireNonNull(d3.getValue()).toString();}
                            if(Objects.requireNonNull(d3.getKey()).equals("name")){ name = Objects.requireNonNull(d3.getValue()).toString();}
                            if(Objects.requireNonNull(d3.getKey()).equals("phone")){ phone = Objects.requireNonNull(d3.getValue()).toString();}
                            if(Objects.requireNonNull(d3.getKey()).equals("email")){ email = Objects.requireNonNull(d3.getValue()).toString();}
                            if(Objects.requireNonNull(d3.getKey()).equals("nob")){ nob = Objects.requireNonNull(d3.getValue()).toString();}
                            if(Objects.requireNonNull(d3.getKey()).equals("dci")){ dci = Objects.requireNonNull(d3.getValue()).toString();}
                            if(Objects.requireNonNull(d3.getKey()).equals("dco")){ dco = Objects.requireNonNull(d3.getValue()).toString();}
                            if(Objects.requireNonNull(d3.getKey()).equals("total")){ total = Objects.requireNonNull(d3.getValue()).toString();}
                            if(Objects.requireNonNull(d3.getKey()).equals("roomNo")){ roomNo = Objects.requireNonNull(d3.getValue()).toString();}
                        }
                        Reservation tempRes = new Reservation(id, name, phone, email, dci, dco, nob,
                                total);
                        tempRes.setRoomNo(roomNo);
                        afterReadingSingle(tempRes);
                    }
                }
                afterReadingAll(view);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    //After Reading Single Reservation
    private void afterReadingSingle(Reservation res){
        reservations.add(res);
    }

    //After Reading All Reservations
    private void afterReadingAll(View view){
        adapter = new FListResAdapter(view.getContext(), reservations,this);
        recyclerView.setAdapter(adapter);
    }

    //Back To Dashboard
    private void backToDash(View view){
        btd = view.findViewById(R.id.frl_btnBack);

        btd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ReservationDash();
                FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.reservations, fragment).commit();
            }
        });


    }

    @Override
    public void onItemClick(int position) {
        //Getting Selected Reservation .
        Reservation selectedRes = reservations.get(position);
        //Setting Bundle Keys .
        String ARG_PARAM1 = "rdfId";
        String ARG_PARAM2 = "rdfName";
        String ARG_PARAM3 = "rdfPhone";
        String ARG_PARAM4 = "rdfEmail";
        String ARG_PARAM5 = "rdfDci";
        String ARG_PARAM6 = "rdfDco";
        String ARG_PARAM7 = "rdfNob";
        String ARG_PARAM8 = "rdfTotal";

        // Making Bundle And Assigning Data.
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM1, selectedRes.getId());
        bundle.putString(ARG_PARAM2, selectedRes.getName());
        bundle.putString(ARG_PARAM3, selectedRes.getPhone());
        bundle.putString(ARG_PARAM4, selectedRes.getEmail());
        bundle.putString(ARG_PARAM5, selectedRes.getDci());
        bundle.putString(ARG_PARAM6, selectedRes.getDco());
        bundle.putString(ARG_PARAM7, selectedRes.getNob());
        bundle.putString(ARG_PARAM8, selectedRes.getTotal());
        //
        if (selectedRes.getRoomNo()!= null){
            String ARG_PARAM9 = "roomNo";
            bundle.putString(ARG_PARAM9, selectedRes.getRoomNo());
        } else {
            String ARG_PARAM9 = "roomNo";
            bundle.putString(ARG_PARAM9, "//");
        }
        //
        if (selectedRes.getStaffCki() != null){
            String ARG_PARAM10 = "staffCki";
            bundle.putString(ARG_PARAM10, selectedRes.getStaffCki());
        } else {
            String ARG_PARAM10 = "staffCki";
            bundle.putString(ARG_PARAM10, "//");
        }
        //
        if (selectedRes.getStaffCko() != null){
            String ARG_PARAM11 = "staffCko";
            bundle.putString(ARG_PARAM11, selectedRes.getStaffCko());
        } else {
            String ARG_PARAM11 = "staffCko";
            bundle.putString(ARG_PARAM11, "//");
        }
        //
        String ARG_PARAM12 = "param1";
        bundle.putInt(ARG_PARAM12, lisType);

        //Navigate To Details Fragment .
        Fragment fragment = new ReservationDetails();
        fragment.setArguments(bundle);
        FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.reservations, fragment).commit();
    }
}