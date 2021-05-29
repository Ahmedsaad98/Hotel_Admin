package com.ahmedalraziki.g_admin_final.FainancialPackage;

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

import com.ahmedalraziki.g_admin_final.Classes.FListFinAdapter;
import com.ahmedalraziki.g_admin_final.Classes.Income;
import com.ahmedalraziki.g_admin_final.Classes.Outlay;
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

public class FinancialLists extends Fragment implements FListFinAdapter.OnItemListener{

    private static final String ARG_PARAM1 = "flt";
    private int flt;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    List<Income> incomes;
    List<Outlay> outlays;

    Button btnBack;

    public FinancialLists() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            flt = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_financial_lists, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Assigner(view);
        ListInit();
        inOuReader(view);
        Clicker();
    }

    private void Assigner(View view){
        recyclerView = view.findViewById(R.id.ffl_recV);
        btnBack      = view.findViewById(R.id.ffl_BtnB);
    }

    private void ListInit() {
        incomes = new ArrayList<>();
        outlays = new ArrayList<>();
    }

    private void Clicker(){
        btnBack.setOnClickListener(v -> {
            Fragment fragment = new FinancialAnalysis();
            FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fainancial, fragment).commit();
        });
    }

    private void inOuReader(View view){
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref2 = null;
        if (flt == 1){
            ref2 = ref1.child("financial").child("income");
        } else {
            ref2 = ref1.child("financial").child("outlay");
        }

            if (ref2 != null){
            ref2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    for (DataSnapshot d1 : snapshot.getChildren()) {
                        String amount = "";
                        String date = "";
                        String fromAny = "";
                        String id = "";
                        String staffID = "";
                        String type = "";
                        int year = 0;
                        int month = 0;
                        int day = 0;

                        for (DataSnapshot d2 : d1.getChildren()) {
                            if (d2.getKey().equals("amount")) {
                                amount = d2.getValue().toString();
                            }
                            if (d2.getKey().equals("date")) {
                                date = d2.getValue().toString();
                            }
                            if (d2.getKey().equals("fromAny")) {
                                fromAny = d2.getValue().toString();
                            }
                            if (d2.getKey().equals("id")) {
                                id = d2.getValue().toString();
                            }
                            if (d2.getKey().equals("staffID")) {
                                staffID = d2.getValue().toString();
                            }
                            if (d2.getKey().equals("type")) {
                                type = d2.getValue().toString();
                            }
                            if (d2.getKey().equals("year")) {
                                year = Integer.parseInt(d2.getValue().toString());
                            }
                            if (d2.getKey().equals("month")) {
                                month = Integer.parseInt(d2.getValue().toString());
                            }
                            if (d2.getKey().equals("day")) {
                                day = Integer.parseInt(d2.getValue().toString());
                            }
                        }
                            //Making A temp Income Object.
                        if(flt == 1){
                            Income tmpInc = new Income(id, date, amount, fromAny, staffID, type);
                            tmpInc.setYear(year);
                            tmpInc.setMonth(month);
                            tmpInc.setDay(day);
                            AddIn(tmpInc);
                        } else{
                            Outlay tmpOut = new Outlay(id, date, amount, fromAny, staffID, type);
                            tmpOut.setYear(year);
                            tmpOut.setMonth(month);
                            tmpOut.setDay(day);
                            AddOu(tmpOut);
                        }

                    }
                    //After Reading All Incomes We Initiate Phase 2.
                    setRecView(view);

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) { }});
            }
        }

        private void AddIn(Income income){
            incomes.add(income);
        }
        private void AddOu(Outlay outlay){
            outlays.add(outlay);
        }

        private void setRecView(View view){
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

            if (flt == 1) {
                adapter = new FListFinAdapter(view.getContext(), 1, incomes, outlays, this);
            } else {
                adapter = new FListFinAdapter(view.getContext(), 2, incomes, outlays, this);
            }
            recyclerView.setAdapter(adapter);
        }

    @Override
    public void onItemClick(int position) {

    }
}