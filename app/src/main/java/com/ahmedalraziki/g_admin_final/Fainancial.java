package com.ahmedalraziki.g_admin_final;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ahmedalraziki.g_admin_final.FainancialPackage.FainancialFragment;

public class Fainancial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fainancial_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.containerFai, FainancialFragment.newInstance())
                    .commitNow();
        }
    }
}