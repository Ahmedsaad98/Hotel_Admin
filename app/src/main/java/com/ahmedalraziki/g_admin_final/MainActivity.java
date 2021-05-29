package com.ahmedalraziki.g_admin_final;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.ahmedalraziki.g_admin_final.Activities.Dashboard;
import com.ahmedalraziki.g_admin_final.Activities.SignIn;
import com.ahmedalraziki.g_admin_final.Classes.FileUtils;

public class MainActivity extends AppCompatActivity {

    private int currentApiVersion;
    TextView mt1;
    Animation ma1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GetPermissions();

        Assigner();

        sA();

        fullScreenActivation();

        splashscreenActivation();
    }

    //Assign Views
    private void Assigner(){
        mt1 = findViewById(R.id.ma_ts);
        ma1 = AnimationUtils.loadAnimation(this, R.anim.ma_1);
    }

    //Get Permissions
    private void GetPermissions(){
        //1- Write External Storage

        int check1 = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int check2 = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int check3 = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int check4 = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (check1 != PackageManager.PERMISSION_GRANTED || check2 != PackageManager.PERMISSION_GRANTED
                || check3 != PackageManager.PERMISSION_GRANTED || check4 != PackageManager.PERMISSION_GRANTED) {
            String [] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_COARSE_LOCATION};
            requestPermissions(permissions,12);
        }

    }

    //Start Animation
    private void sA(){
        mt1.startAnimation(ma1);
    }

    //Enable Splash Screen
    private void splashscreenActivation() {
        new Handler().postDelayed(() -> {

            Intent i =new Intent(MainActivity.this, SignIn.class);
            i.putExtra("from" , 0);
            startActivity(i);
            finish();
        }, 2000);
    }


    //Enable full screen
    private void fullScreenActivation() {
        currentApiVersion = Build.VERSION.SDK_INT;
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        decorView.setSystemUiVisibility(flags);
                    }
                }
            });
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

}