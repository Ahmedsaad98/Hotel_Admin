package com.ahmedalraziki.g_admin_final.StaffPackage;

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

import com.ahmedalraziki.g_admin_final.Classes.FListResAdapter;
import com.ahmedalraziki.g_admin_final.Classes.FListStaAdapter;
import com.ahmedalraziki.g_admin_final.Classes.Reservation;
import com.ahmedalraziki.g_admin_final.Classes.Staff;
import com.ahmedalraziki.g_admin_final.R;
import com.ahmedalraziki.g_admin_final.reservationPackage.ReservationDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StaffList extends Fragment implements FListStaAdapter.OnItemListener {

    RecyclerView recyclerView;
    List<Staff> members ;
    RecyclerView.Adapter adapter;

    DatabaseReference mainRef = FirebaseDatabase.getInstance().getReference();

    public StaffList() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_staff_list, container, false); }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Assigner(view);
        recyclerInit(view);
        Reader(view);

    }

    private void Assigner(View view){
        recyclerView = view.findViewById(R.id.fsl_recid);
    }

    private void recyclerInit(View view){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        members = new ArrayList<>();
    }

    private void Reader(View view){
        // Setting A Ref for the staff
        DatabaseReference ref = mainRef.child("management").child("staff");

        // Reading The Reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot d1 : snapshot.getChildren()){
                    //
                    String id = "";
                    String username = "";
                    String password = "";
                    String name = "";
                    String phone = "";
                    String address = "";
                    String position = "";
                    String salary = "";
                    String email = "";
                    int lvl = 99;
                    //
                    for (DataSnapshot d2 : d1.getChildren()){
                        if (d2.getKey().equals("id")){ id = d2.getValue().toString();}
                        if (d2.getKey().equals("username")){ username = d2.getValue().toString();}
                        if (d2.getKey().equals("password")){ password = d2.getValue().toString();}
                        if (d2.getKey().equals("name")){ name = d2.getValue().toString();}
                        if (d2.getKey().equals("phone")){ phone = d2.getValue().toString();}
                        if (d2.getKey().equals("address")){ address = d2.getValue().toString();}
                        if (d2.getKey().equals("position")){ position = d2.getValue().toString();}
                        if (d2.getKey().equals("salary")){ salary = d2.getValue().toString();}
                        if (d2.getKey().equals("email")){ email = d2.getValue().toString();}
                        if (d2.getKey().equals("lvl")){ lvl = Integer.parseInt(d2.getValue().toString());}
                    }
                    //
                    Staff tempMem = new Staff(""+id, ""+username, ""+password, ""+name, ""+phone,""+address,""+position
                    ,""+salary,""+email, lvl);
                    //
                    tempMem.setStaRef(d1.getRef());
                    AddMember(tempMem);
                }
                setRecyclerView(view);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) { }});
    }

    private void AddMember(Staff mem){
        members.add(mem);
    }

    private void setRecyclerView(View view){
        adapter = new FListStaAdapter(view.getContext(), members,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {
        Staff selMem = members.get(position);
        String Staff_ID = "idS";
        Bundle bundle = new Bundle();
        bundle.putString(Staff_ID, selMem.getId());
        Fragment fragment = new StaffMemDetails();
        fragment.setArguments(bundle);
        FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.staff, fragment).commit();
    }
}