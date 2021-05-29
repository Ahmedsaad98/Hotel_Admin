package com.ahmedalraziki.g_admin_final.LogsPackage;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ahmedalraziki.g_admin_final.R;
import com.ahmedalraziki.g_admin_final.StaffPackage.StaffDash;

import java.util.Objects;

public class LogsFragment extends Fragment {

    private LogsViewModel mViewModel;

    public static LogsFragment newInstance() {
        return new LogsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_logs, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LogsViewModel.class);
        Fragment fragment = new LogsDash();
        FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.logs, fragment).commit();
    }

}