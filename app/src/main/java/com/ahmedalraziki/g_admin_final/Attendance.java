package com.ahmedalraziki.g_admin_final;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ahmedalraziki.g_admin_final.AttendancePackage.AttendanceFragment;

public class Attendance extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, AttendanceFragment.newInstance())
                    .commitNow();
        }
    }
}