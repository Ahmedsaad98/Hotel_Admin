package com.ahmedalraziki.g_admin_final.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ahmedalraziki.g_admin_final.Attendance;
import com.ahmedalraziki.g_admin_final.Fainancial;
import com.ahmedalraziki.g_admin_final.Logs;
import com.ahmedalraziki.g_admin_final.R;
import com.ahmedalraziki.g_admin_final.Reception;
import com.ahmedalraziki.g_admin_final.Reservations;
import com.ahmedalraziki.g_admin_final.Staff;

public class Dashboard extends AppCompatActivity {

    Button btnRec;
    Button btnRes;
    Button btnAte;
    Button btnSta;
    Button btnFai;
    Button btnLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Assigner();

        Clicker();
    }

    private void Assigner(){
        btnRec = findViewById(R.id.da_btnRec);
        btnRes = findViewById(R.id.da_btnRes);
        btnAte = findViewById(R.id.da_btnAte);
        btnSta = findViewById(R.id.da_btnSta);
        btnFai = findViewById(R.id.da_btnFai);
        btnLog = findViewById(R.id.da_btnLog);
    }

    private void Clicker(){

        //Btn Reception
        btnRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, Reception.class);
                startActivity(intent);
            }
        });
        // Btn Reservations
        btnRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, Reservations.class);
                startActivity(intent);
            }
        });

        //Btn Financial
        btnFai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, Fainancial.class);
                startActivity(intent);
            }
        });

        //Btn Attendance
        btnAte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, Attendance.class);
                startActivity(intent);
            }
        });

        //Btn Staff
        btnSta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, Staff.class);
                startActivity(intent);
            }
        });

        //Btn Logs
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, Logs.class);
                startActivity(intent);
            }
        });


    }
}