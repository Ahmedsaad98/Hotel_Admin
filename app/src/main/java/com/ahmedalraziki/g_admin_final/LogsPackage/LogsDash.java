package com.ahmedalraziki.g_admin_final.LogsPackage;

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

import com.ahmedalraziki.g_admin_final.FainancialPackage.FainancialAddIncome;
import com.ahmedalraziki.g_admin_final.R;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class LogsDash extends Fragment {

    Button finLogs;

    public LogsDash() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_logs_dash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Assigner(view);
        Clicker();
    }

    private void Assigner(View view){
        finLogs = view.findViewById(R.id.fld_btnFinLogs);
    }

    private void Clicker(){
        finLogs.setOnClickListener(v -> {
            Fragment fragment = new Logs_Financial();
            FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.logs, fragment).commit();
        });
    }
}