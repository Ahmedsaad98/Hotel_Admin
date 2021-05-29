package com.ahmedalraziki.g_admin_final;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ahmedalraziki.g_admin_final.receptionPackage.ReceptionFragment;

public class Reception extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reception_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.containerRec, ReceptionFragment.newInstance())
                    .commitNow();
        }
    }
}