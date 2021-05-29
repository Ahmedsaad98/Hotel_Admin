package com.ahmedalraziki.g_admin_final;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ahmedalraziki.g_admin_final.StaffPackage.StaffFragment;

public class Staff extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, StaffFragment.newInstance())
                    .commitNow();
        }
    }
}