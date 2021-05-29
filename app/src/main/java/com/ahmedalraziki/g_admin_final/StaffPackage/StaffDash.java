package com.ahmedalraziki.g_admin_final.StaffPackage;

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

public class StaffDash extends Fragment {


    Button addStaff;
    Button listStaff;


    public StaffDash() { }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_staff_dash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Assigner(view);
        Clicker();
    }

    private void Assigner(View view){
        addStaff = view.findViewById(R.id.fsd_btnAdd);
        listStaff = view.findViewById(R.id.fsd_btnList);
    }

    private void Clicker(){
        //Adding Staff Member
        addStaff.setOnClickListener(v ->{
            Fragment fragment = new StaffAddMember();
            FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.staff, fragment).commit();
        });

        //View List Of Members
        listStaff.setOnClickListener(v ->{
            Fragment fragment = new StaffList();
            FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.staff, fragment).commit();
        });


    }
}