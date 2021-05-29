package com.ahmedalraziki.g_admin_final.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ahmedalraziki.g_admin_final.Classes.Staff;
import com.ahmedalraziki.g_admin_final.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SignIn extends AppCompatActivity {

    EditText etUserName;
    EditText etPassword;
    TextView tvLabel;
    Button btnSignIn;
    Button btnScanId;
    Animation sia1;
    DatabaseReference refMain = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Assigner();
        sA();
        Clicker();
        signInQr();
    }

    //Assign Views
    private void Assigner(){
        etUserName = findViewById(R.id.sia_etun);
        etPassword = findViewById(R.id.sia_etpa);
        tvLabel    = findViewById(R.id.sia_tvsi);
        btnSignIn  = findViewById(R.id.sia_btnsi);
        btnScanId  = findViewById(R.id.sia_btnsmi);
        sia1 = AnimationUtils.loadAnimation(this, R.anim.sia_1);
    }

    //Start Animation
    private void sA(){
        tvLabel.startAnimation(sia1);
    }

    //Set OnClick Methods
    private void Clicker(){
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUP();
            }
        });

        btnScanId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInQrBefore();
            }
        });
    }

    //Sign In With QR Code .
    private void signInQrBefore(){
        Intent intent = new Intent(SignIn.this, Scanner.class);
        intent.putExtra("tf", 1);
        startActivity(intent);
        finish();
    }

    private void signInQr(){
        Intent intent = getIntent();
        String QrData = intent.getStringExtra("qd");

        if(QrData!= null && !QrData.equals("")){
        signInQr2(QrData);
        saveID(QrData);
        }

    }

    private void signInQr2(String id){
        refMain.child("management").child("staff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot d : snapshot.getChildren()){
                    if (Objects.equals(d.getKey(), id)){signInQr3();}
                } }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }});
    }

    private void signInQr3(){
        Intent intent = new Intent(SignIn.this, Dashboard.class);
        startActivity(intent);
        finish();
    }


    //Sign in with Username And Password
    private void signInUP(){

        refMain.child("management").child("staff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot d1 : snapshot.getChildren()){
                    String dbId = "";
                    String dbUn = "";
                    String dbPa = "";

                    for (DataSnapshot d2 : d1.getChildren()){
                        if(Objects.equals(d2.getKey(), "id")){
                            dbId = Objects.requireNonNull(d2.getValue()).toString();}

                        else if (Objects.requireNonNull(d2.getKey()).equals("username")){
                            dbUn = Objects.requireNonNull(d2.getValue()).toString();}

                        else if (Objects.requireNonNull(d2.getKey()).equals("password")){
                            dbPa = Objects.requireNonNull(d2.getValue()).toString();}
                    }
                    Staff temp = new Staff(dbId, dbUn, dbPa);
                    SignInUP2(temp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SignInUP2(Staff staff){
        String etUn = etUserName.getText().toString();
        String etPa = etPassword.getText().toString();

        saveID(staff.getId());

        if(staff.getUsername().equals(etUn) && staff.getPassword().equals(etPa)){
            Intent intent = new Intent(SignIn.this, Dashboard.class);
            startActivity(intent);
        }
    }

    //Save This User's ID .
    private void saveID(String id){
        SharedPreferences prefs = this.getSharedPreferences("prefs1", Context.MODE_PRIVATE);

        prefs.edit().putString("uid", id).apply();
    }


}