package com.ahmedalraziki.g_admin_final.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;

import com.ahmedalraziki.g_admin_final.Classes.Staff;
import com.ahmedalraziki.g_admin_final.MainActivity;
import com.ahmedalraziki.g_admin_final.R;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;

public class Scanner extends AppCompatActivity {

    private CodeScanner mCodeScanner;
    String[] PERMISSIONS = {Manifest.permission.CAMERA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        if (this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            this.requestPermissions(PERMISSIONS , 2002); }
        else { StartScan(); }
    }


    //Start QR code Scanning .
    private void StartScan() {
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(result -> runOnUiThread(() -> sendBack(result.getText())));
        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
    }

    //Send Back the code to its origin .
    private void sendBack(String data){
        Intent intent = getIntent();
        int taskFrom = intent.getIntExtra("tf", 0);

        switch (taskFrom){
            case 1:
                Intent intent3 = new Intent(Scanner.this, SignIn.class);
                intent3.putExtra("qd", data);
                startActivity(intent3);
                finish();

                break;

            default:
                Intent intent2 = new Intent(Scanner.this, MainActivity.class);
                startActivity(intent2);
                finish();
        }
    }

    //Basics
    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}