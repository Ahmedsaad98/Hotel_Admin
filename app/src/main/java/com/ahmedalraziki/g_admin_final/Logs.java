package com.ahmedalraziki.g_admin_final;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ahmedalraziki.g_admin_final.LogsPackage.LogsFragment;

public class Logs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logs_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, LogsFragment.newInstance())
                    .commitNow();
        }
    }
}